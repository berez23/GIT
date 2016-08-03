package it.webred.ct.service.tares.data.access.dao;

import it.webred.ct.service.tares.data.access.dto.SegnalazioniSearchCriteria;

public class SegnalazioniQueryBuilder {
	
	private SegnalazioniSearchCriteria criteria = null;
	private String foglio = "";
	private String numero = "";
	private String denominatore = "";
	private String subalterno = "";
	private Boolean esportata = null;
	private Long id = null;
	
	public SegnalazioniQueryBuilder(SegnalazioniSearchCriteria criteria) {
		if (criteria != null){
			foglio = criteria.getFoglio();
			numero = criteria.getNumero();
			denominatore = criteria.getDenominatore();
			subalterno = criteria.getSubalterno();
			this.esportata = criteria.getEsportata();
			this.id = criteria.getId();
			this.criteria = criteria;
		}
	}//-------------------------------------------------------------------------
	
	public String nativeQueryOnSitiuiu() {
		
		String sql = "select * from SITIUIU ";

		String whereCond = getSQLCriteriaSitiuiu();

		if (!"".equals(whereCond)) {
			sql += " WHERE 1 = 1 and IDE_MUTA_FINE is null " + whereCond;
		}
		
		sql += " ORDER BY IDE_IMMO, FOGLIO, PARTICELLA, SUB, UNIMM ";

		return sql;
	}//-------------------------------------------------------------------------
	
	public String getSQLCriteriaSitiuiu() {
		String sqlCriteria = "";
		
		sqlCriteria = (criteria.getFoglio() == null || criteria.getFoglio().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " FOGLIO = '" + criteria.getFoglio() + "'");		
		sqlCriteria = (criteria.getNumero() == null || criteria.getNumero().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " PARTICELLA = '" + criteria.getNumero() + "'");
		sqlCriteria = (criteria.getDenominatore()== null || criteria.getDenominatore().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " SUB = '" + criteria.getDenominatore() + "'");
		sqlCriteria = (criteria.getSubalterno()== null || criteria.getSubalterno().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UNIMM = '" + criteria.getSubalterno() + "'");
		
		return sqlCriteria;
	}//-------------------------------------------------------------------------
	
	public String nativeQueryOnSitiedi_vani() {
		
		String sql = "select AMBIENTE, SUPE_AMBIENTE, ALTEZZAMIN, ALTEZZAMAX from SITIEDI_VANI ";

		String whereCond = getSQLCriteriaSitiedi_vani();

		if (!"".equals(whereCond)) {
			//sql += " WHERE 1 = 1 and ROWNUM <= 10 " + whereCond;
			sql += " WHERE 1 = 1 " + whereCond;
		}
		
		sql += " ORDER BY AMBIENTE, FOGLIO, PARTICELLA, SUB, UNIMM ";

		return sql;
	}//-------------------------------------------------------------------------
	
	public String nativeQueryAmbienti() {
		
		String sql = "select distinct AMBIENTE from SITIEDI_VANI ";

		sql += " ORDER BY AMBIENTE ";

		return sql;
	}//-------------------------------------------------------------------------
	
	public String getSQLCriteriaSitiedi_vani() {
		String sqlCriteria = "";
		
		sqlCriteria = (criteria.getFoglio() == null || criteria.getFoglio().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " FOGLIO = '" + criteria.getFoglio() + "'");		
		sqlCriteria = (criteria.getNumero() == null || criteria.getNumero().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " PARTICELLA = '" + criteria.getNumero() + "'");
		sqlCriteria = (criteria.getDenominatore()== null || criteria.getDenominatore().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " (SUB is null or SUB = ' ' or SUB = '') ");
		sqlCriteria = (criteria.getSubalterno()== null || criteria.getSubalterno().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UNIMM = '" + criteria.getSubalterno() + "'");
		
		return sqlCriteria;
	}//-------------------------------------------------------------------------
	
	public String createQuerySegnalazioni() {
		
		String sql = "SELECT t FROM SetarSegnalazione t  ";
		
		String whereCond = getSQLCriteriaSegnalazione();
		
		if (!"".equals(whereCond)) {
			sql += " WHERE 1 = 1 " + whereCond;
		}
		
		sql += " ORDER BY t.progressivo DESC ";

		return sql;
	}//-------------------------------------------------------------------------
	
	public String createQuerySegnalazioni1() {
		
		String sql = "SELECT t FROM SetarSegnala1 t";
		
		String whereCond = getSQLCriteriaSegnala1();
		
		if (!"".equals(whereCond)) {
			sql += " WHERE 1 = 1 " + whereCond;
		}
		/*
		 * XXX AAA: l'ordinamento t.segnalazioneId DESC è usato dal metodo segnalaBean.goEsportateLst
		 * per recuperare le SetarSegnalazione in ordine di inserimento DESC 
		 */
		sql += " ORDER BY t.segnalazioneId DESC, t.foglio, t.numero, t.denominatore, t.subalterno ";

		return sql;
	}//-------------------------------------------------------------------------
	
	public String createDelQuerySegnalazioni1() {
		
		String sql = "DELETE FROM SetarSegnala1 t";
		
		String whereCond = getSQLCriteriaSegnala1();
		
		if (!"".equals(whereCond)) {
			sql += " WHERE 1 = 1 " + whereCond;
		}
	
		return sql;
	}//-------------------------------------------------------------------------
	
	public String createQuerySegnalazioni2() {
		
		String sql = "SELECT t FROM SetarSegnala2 t";
		
		String whereCond = getSQLCriteriaSegnala2();
		
		if (!"".equals(whereCond)) {
			sql += " WHERE 1 = 1 " + whereCond;
		}
		
		sql += " ORDER BY t.ambiente ";

		return sql;
	}//-------------------------------------------------------------------------
	
	public String createDelQuerySegnalazioni2() {
		
		String sql = "DELETE FROM SetarSegnala2 t";
		
		String whereCond = getSQLCriteriaSegnala2();
		
		if (!"".equals(whereCond)) {
			sql += " WHERE 1 = 1 " + whereCond;
		}
	
		return sql;
	}//-------------------------------------------------------------------------

	public String createQuerySegnalazioni3() {
		
		String sql = "SELECT t FROM SetarSegnala3 t";
		
		String whereCond = getSQLCriteriaSegnala3();
		
		if (!"".equals(whereCond)) {
			sql += " WHERE 1 = 1 " + whereCond;
		}
		
		sql += " ORDER BY t.cognome, t.nome ";
	
		return sql;
	}//-------------------------------------------------------------------------
	
	public String createDelQuerySegnalazioni3() {
		
		String sql = "DELETE FROM SetarSegnala3 t";
		
		String whereCond = getSQLCriteriaSegnala3();
		
		if (!"".equals(whereCond)) {
			sql += " WHERE 1 = 1 " + whereCond;
		}
	
		return sql;
	}//-------------------------------------------------------------------------

	public String getSQLCriteriaSegnala1() {
		String sqlCriteria = "";

		sqlCriteria = (criteria.getId() == null ? sqlCriteria : addOperator(sqlCriteria) + " t.id = '" + criteria.getId() + "'");
		sqlCriteria = (criteria.getFoglio() == null || criteria.getFoglio().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " t.foglio = '" + criteria.getFoglio() + "'");		
		sqlCriteria = (criteria.getNumero() == null || criteria.getNumero().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " t.numero = '" + criteria.getNumero() + "'");
		sqlCriteria = (criteria.getDenominatore()== null || criteria.getDenominatore().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " t.denominatore = '" + criteria.getDenominatore() + "'");
		sqlCriteria = (criteria.getSubalterno()== null || criteria.getSubalterno().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " t.subalterno = '" + criteria.getSubalterno() + "'");
		sqlCriteria = (criteria.getIdentificativoImmobile() == null || criteria.getIdentificativoImmobile().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " t.identificativoImmobile = '" + criteria.getIdentificativoImmobile() + "'");
		if (criteria.getEsportata()!=null){
			sqlCriteria = addOperator(sqlCriteria);
			sqlCriteria += " t.esportata = '" + (criteria.getEsportata()?"1":"0") + "'";
		}
//		sqlCriteria = ( criteria.getEsportata()!=null ? sqlCriteria : addOperator(sqlCriteria) + " t.esportata = '" + criteria.getEsportata() + "'");
		sqlCriteria = (criteria.getProgressivo() == null || criteria.getProgressivo().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " t.progressivo = '" + criteria.getProgressivo() + "'");
		sqlCriteria = (criteria.getSegnalazioneId() == null ? sqlCriteria : addOperator(sqlCriteria) + " t.segnalazioneId = '" + criteria.getSegnalazioneId() + "'");
		
		return sqlCriteria;
	}//-------------------------------------------------------------------------
	
	public String getSQLCriteriaSegnala2() {
		String sqlCriteria = "";

		sqlCriteria = (criteria.getId() == null ? sqlCriteria : addOperator(sqlCriteria) + " t.id = '" + criteria.getId() + "'");
		sqlCriteria = (criteria.getIdentificativoImmobile() == null || criteria.getIdentificativoImmobile().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " t.identificativoImmobile = '" + criteria.getIdentificativoImmobile() + "'");
		sqlCriteria = (criteria.getSegnala1Id() == null ? sqlCriteria : addOperator(sqlCriteria) + " t.segnala1Id = '" + criteria.getSegnala1Id() + "'");
		sqlCriteria = (criteria.getSegnalazioneId() == null ? sqlCriteria : addOperator(sqlCriteria) + " t.segnalazioneId = '" + criteria.getSegnalazioneId() + "'");
		
		return sqlCriteria;
	}//-------------------------------------------------------------------------
	
	public String getSQLCriteriaSegnala3() {
		String sqlCriteria = "";

		return sqlCriteria;
	}//-------------------------------------------------------------------------
	
	public String getSQLCriteriaSegnalazione() {
		String sqlCriteria = "";

		if (criteria.getScaricata()!=null){
			sqlCriteria = addOperator(sqlCriteria);
			sqlCriteria += " t.scaricata = '" + (criteria.getScaricata()?"1":"0") + "'";
		}
		sqlCriteria = (criteria.getId() == null ? sqlCriteria : addOperator(sqlCriteria) + " t.id = '" + criteria.getId() + "'");
		sqlCriteria = (criteria.getProgressivo() == null || criteria.getProgressivo().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " t.progressivo = '" + criteria.getProgressivo() + "'");
		
		return sqlCriteria;
	}//-------------------------------------------------------------------------
	
	protected String addOperator(String criteria) {  	    
	    return criteria += " AND ";
	}//-------------------------------------------------------------------------

}
