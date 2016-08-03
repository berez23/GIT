package it.webred.cs.csa.ejb.dao;

import it.webred.cs.csa.ejb.dto.CasoSearchCriteria;
import it.webred.cs.data.DataModelCostanti;

public class SoggettoQueryBuilder {

	private CasoSearchCriteria criteria;
	
	public SoggettoQueryBuilder() {
	}

	public SoggettoQueryBuilder(CasoSearchCriteria criteria) {
		this.criteria = criteria;
	}

	public String createQueryListaCasi(boolean count) {

		//I casi segnalati verso l'utente loggato sono in prima posizione
		//seguiti dai casi per cui ha i permessi in ordine di iter decrescente
		String sql = "";
		
		if(count)
			sql += "SELECT count(DISTINCT ANAGRAFICA_ID) ";
		else sql += "SELECT DISTINCT ANAGRAFICA_ID, DATA_CREAZIONE, CFG_IT_STATO_ID ";

		String sqlCasi = "SELECT DISTINCT A.COGNOME || ' ' || A.NOME DENOMINAZIONE, A.DATA_NASCITA, A.CF," +
			" S.ANAGRAFICA_ID, IT.DATA_CREAZIONE, IT.CFG_IT_STATO_ID" +
			" FROM CS_A_SOGGETTO S, CS_A_ANAGRAFICA a, CS_A_CASO c, CS_A_SOGGETTO_CATEGORIA_SOC sc," +
			" CS_REL_SETTORE_CATSOC cs, CS_IT_STEP it," +
			" CS_A_CASO_OPE_TIPO_OPE coto," +
            " CS_O_OPERATORE_TIPO_OPERATORE oto," +
            " CS_O_OPERATORE_SETTORE os," +
            " CS_O_OPERATORE o," +
            " CS_O_SETTORE sett" + 
			" WHERE S.CASO_ID = C.ID " +
			" AND S.ANAGRAFICA_ID = A.ID" +
			" AND SC.SOGGETTO_ANAGRAFICA_ID = S.ANAGRAFICA_ID" +
			" AND SC.DATA_FINE_APP >= sysdate" +
			" AND SC.CATEGORIA_SOCIALE_ID = CS.CATEGORIA_SOCIALE_ID" +
			" AND CS.SETTORE_ID = SETT.ID " +
			" AND (CS.SETTORE_ID=IT.SETTORE_ID OR it.settore_id is null)" + //Condizione per recuperare casi corrispondenti al settore nel caso in cui una stessa categoria sociale sia associata a settori diversi di organizzazioni diverse
			" AND CS.ABILITATO = 1" +
			" AND IT.ID = (SELECT MAX(it2.id) FROM CS_IT_STEP it2 WHERE IT2.CASO_ID = S.CASO_ID)" +
			" AND COTO.CASO_ID (+)= C.ID" +
            " AND OTO.ID (+)= COTO.OPERATORE_TIPO_OPERATORE_ID" +
            " AND OS.ID (+)= OTO.OPERATORE_SETTORE_ID" +
            " AND O.ID (+)= OS.OPERATORE_ID" +
            " AND COTO.DATA_FINE_APP (+)>= SYSDATE";
		
		if(criteria != null) {
			//filtro sempre per il settore scelto e se non ha il permesso casi settore
			//filtro i casi dove l'utente è inserito come tipo operatore (CS_A_CASO_OPE_TIPO_OPE)
			//e quelli inseriti dall'utente che non hanno responsabile (CFG_IT_STATO_ID != 3)
			String sqlCasiTipoOpe = " O.USERNAME = '" + criteria.getUsername() + "'" +
					" OR (C.USER_INS = '" + criteria.getUsername() + "' " +
					" AND " + DataModelCostanti.IterStatoInfo.PRESO_IN_CARICO +
					" NOT IN (SELECT IT2.CFG_IT_STATO_ID FROM CS_IT_STEP it2 WHERE IT2.CASO_ID = S.CASO_ID))"; 

			if(!criteria.isPermessoCasiSettore() && !criteria.isPermessoCasiOrganizzazione())
				sqlCasi += " AND (" + sqlCasiTipoOpe + ")";

			if(criteria.getIdSettore() != null && !criteria.isPermessoCasiOrganizzazione()) {
				sqlCasi += " AND CS.SETTORE_ID = " + criteria.getIdSettore();
			} 
			
			if(criteria.getIdOrganizzazione() != null) {
				sqlCasi += " AND SETT.ORGANIZZAZIONE_ID = " + criteria.getIdOrganizzazione();
			} 
		}
		
		/*Casi segnalati all'organizzazione di cui è responsabile o settore (se non possiede permessi sull'organizzione) 
		  o direttamente all'utente corrente */
		String sqlCasiSegnalati = "SELECT DISTINCT A.COGNOME || ' ' || A.NOME DENOMINAZIONE, A.DATA_NASCITA, A.CF," +
			" S.ANAGRAFICA_ID, IT.DATA_CREAZIONE, IT.CFG_IT_STATO_ID" +
			" FROM CS_A_ANAGRAFICA A, CS_A_SOGGETTO S, CS_IT_STEP it" +
			" WHERE IT.ID = (SELECT MAX(it2.id) FROM CS_IT_STEP it2 WHERE IT2.CASO_ID = S.CASO_ID)" +
			" AND S.ANAGRAFICA_ID = A.ID" +
			" AND IT.CFG_IT_STATO_ID IN (" + DataModelCostanti.IterStatoInfo.SEGNALATO + 
			" ," + DataModelCostanti.IterStatoInfo.SEGNALATO_OP +
			" ," + DataModelCostanti.IterStatoInfo.SEGNALATO_ENTE + ")";
		
		//se non è presente il permesso per vedere tutti i casi segnalati al settore o organizzazione filtro per operatore
		if(criteria.getIdOrganizzazione() != null && criteria.isPermessoCasiOrganizzazione())
			sqlCasiSegnalati += " AND IT.ORGANIZZAZIONE_ID_TO = " + criteria.getIdOrganizzazione();
		if(criteria.getIdSettore() != null && !criteria.isPermessoCasiOrganizzazione())
			sqlCasiSegnalati += " AND IT.SETTORE_ID_TO = " + criteria.getIdSettore();
		if(criteria.getIdOperatore() != null && !criteria.isPermessoCasiSettore() && !criteria.isPermessoCasiOrganizzazione())
			sqlCasiSegnalati += " AND IT.OPERATORE_TO = " + criteria.getIdOperatore();
		
		sql += " FROM (" + sqlCasi + " UNION "+ sqlCasiSegnalati + " ) ";
		
		String whereCond = getSQLCriteria();
		sql += " WHERE 1 = 1 " + whereCond;
		
		if(!count)
			sql += " ORDER BY CASE WHEN CFG_IT_STATO_ID = " + DataModelCostanti.IterStatoInfo.SEGNALATO_OP + " THEN 1 " +
					" WHEN CFG_IT_STATO_ID = " + DataModelCostanti.IterStatoInfo.SEGNALATO + " THEN 2" +
					" WHEN CFG_IT_STATO_ID = " + DataModelCostanti.IterStatoInfo.SEGNALATO_ENTE + " THEN 3" +
					" ELSE 4 END, DATA_CREAZIONE DESC";
		
		return sql;

	}
	
	private String getSQLCriteria() {
		//campi usati per il filter 
		String sqlCriteria = "";
				
		sqlCriteria += (criteria.getDenominazione() == null  || criteria.getDenominazione().trim().equals("") ? "" : " AND UPPER(DENOMINAZIONE) LIKE '%" + criteria.getDenominazione().toUpperCase() + "%'");

		sqlCriteria += (criteria.getDataNascita() == null  || criteria.getDataNascita().trim().equals("") ? "" : " AND TO_CHAR(DATA_NASCITA, 'dd/mm/yyyy') like '%" + criteria.getDataNascita().toUpperCase() + "%'");
		
		sqlCriteria += (criteria.getCodiceFiscale() == null  || criteria.getCodiceFiscale().trim().equals("") ? "" : " AND UPPER(CF) LIKE '%" + criteria.getCodiceFiscale().toUpperCase() + "%'");
		
		return sqlCriteria;
	}
	
	public String createQueryCasiPerCategoria(boolean count) {

		String sql = "";
		
		if(count)
			sql += "SELECT count(DISTINCT S.ANAGRAFICA_ID) ";
		else sql += "SELECT DISTINCT S.ANAGRAFICA_ID, IT.DATA_CREAZIONE, IT.CFG_IT_STATO_ID ";
		
		sql += "FROM CS_A_SOGGETTO S, " +
		       "CS_A_ANAGRAFICA a, " +
		       "CS_A_CASO c, " +
		       "CS_A_SOGGETTO_CATEGORIA_SOC sc, " +
		       "CS_IT_STEP it, " +
		       "CS_A_CASO_OPE_TIPO_OPE coto " +
		       "WHERE S.CASO_ID = C.ID " +
		       "AND S.ANAGRAFICA_ID = A.ID " +
		       "AND SC.SOGGETTO_ANAGRAFICA_ID = S.ANAGRAFICA_ID " +
		       "AND SC.DATA_FINE_APP >= SYSDATE " +
		       "AND COTO.CASO_ID (+)= C.ID " +
		       "AND IT.ID = (SELECT MAX (it2.id) FROM CS_IT_STEP it2 WHERE IT2.CASO_ID = S.CASO_ID) ";
		
		//Potrebbero esserci categorie di settori diversi (di organizzazioni diverse)
	/*	if(criteria.getIdSettore() !=null)
			sql += "AND it.SETTORE_id="+criteria.getIdSettore()+" "; */
		
		if(criteria.getIdCatSociale() != null)
			sql += "AND SC.CATEGORIA_SOCIALE_ID = " + criteria.getIdCatSociale() + " ";
		
		if(criteria.isWithResponsabile())
			sql += "AND COTO.FLAG_RESPONSABILE = 1 ";
		
		if(criteria.getIdLastIter() != null)
			sql += "AND IT.CFG_IT_STATO_ID = " + criteria.getIdLastIter() + " ";
		
		return sql;
	}

}
