package it.webred.ct.service.tares.data.access.dao;

import it.webred.ct.service.tares.data.access.dto.TariffeSearchCriteria;

public class TariffeQueryBuilder {
	
	private TariffeSearchCriteria criteria;
	
	/*
	 * tipo per il coeff KA = ABIT_UNDER o ABIT_OVER, per gli altri invece MIN, MED, MAX, ADHOC  
	 */
	private String tipo = "";
	private String geo = "";
	private String coeff = "";
	private String codice = "";
	private String abit = "";
	
	public TariffeQueryBuilder(TariffeSearchCriteria criteria) {
		this.criteria = criteria;
		this.coeff = criteria.getCoeff();
		this.tipo = criteria.getUtenzaTipo();
		this.geo = criteria.getGeo();
		this.codice = criteria.getCodice();
		this.abit = criteria.getAbit();
	}//-------------------------------------------------------------------------
	
	public String nativeQueryDistribuzioneSupTotTarsu(){
		String sql = "select n_componenti, count(*),  sum(sup_tarsu_tot) from( " +
						"select tab1.*, tab2.n_componenti from( " +
						"select sogg.cod_fisc, sogg.cog_denom, sogg.nome, sogg.dt_nsc, ogg.des_cls_rsu, " +
						"ogg.sup_tot sup_tarsu_tot, pefa.id_ext_d_famiglia " +
						"from sit_t_tar_sogg sogg " +
						"inner join sit_t_tar_contrib cont " +
						"on sogg.id_ext=cont.id_ext_sogg " +
						"inner join sit_t_tar_oggetto ogg " +
						"on cont.id_ext_ogg_rsu=ogg.id_ext " +
						"inner join sit_d_persona pe " +
						"on (sogg.cod_fisc=pe.codfisc or (sogg.cog_denom=pe.cognome and sogg.nome=pe.nome and sogg.dt_nsc=pe.data_nascita) ) " +
						"inner join sit_d_pers_fam pefa " +
						"on pe.id_ext=pefa.id_ext " +
						"where sogg.dt_fine_val is null " +
						"and ogg.dt_fine_val is null " +
						"and ogg.dat_fin is null " +
						getSQLCriteriaDistribuzioneSupTotTarsu() +
						"and cont.dt_fine_val is null " +
						"and pe.dt_fine_val is null " +
						"and pe.posiz_ana='A' " +
						"and pefa.dt_fine_val is null) tab1 " +
						"inner join " + 
						"(select distinct pefa.id_ext_d_famiglia, count(*) n_componenti " + 
						"from sit_d_persona pe " +
						"left join sit_d_pers_fam pefa " +
						"on pe.id_ext=pefa.id_ext " +
						"where pe.dt_fine_val is null " +
						"and pe.posiz_ana='A' " +
						"and pefa.dt_fine_val is null " +
						"group by id_ext_d_famiglia) tab2 " +
						"on tab1.id_ext_d_famiglia=tab2.id_ext_d_famiglia) " +
						"group by n_componenti " +
						"order by n_componenti";
		
		return sql;
	}//-------------------------------------------------------------------------
	
	public String getSQLCriteriaDistribuzioneSupTotTarsu() {
		String sqlCriteria = "";
		
		sqlCriteria = (codice == null || codice.trim().equals("") ? sqlCriteria : codice);

		return sqlCriteria;
	}//-------------------------------------------------------------------------
	
	
	public String nativeQueryClassiTarsu(){
		String sql = "select distinct des_cls_rsu " +
				"from sit_t_tar_oggetto " +
				"where dt_fine_val is null and dat_fin is null " +
				"order by des_cls_rsu";
		
		return sql;
	}//-------------------------------------------------------------------------
	
	public String nativeQueryDistribuzioneComponenti(){
		String sql = "select n_componenti, count(*) from( " +
					"select distinct pefa.id_ext_d_famiglia, count(*) n_componenti " + 
					"from sit_d_persona pe " +
					"left join sit_d_pers_fam pefa " +
					"on pe.id_ext=pefa.id_ext " +
					"where pe.dt_fine_val is null " +
					"and (pe.posiz_ana='A' or pe.posiz_ana='ISCRITTO NELL''A.P.R.' ) " +
					"and pefa.dt_fine_val is null " +
					"group by id_ext_d_famiglia) " +
					"group by n_componenti " +
					"order by n_componenti";
		
		return sql;
	}//-------------------------------------------------------------------------
	
	public String createQuery() {
					
		String sql = "SELECT c FROM SetarCoeff c";
		
		String whereCond = getSQLCriteria();
		
		if (!"".equals(whereCond)) {
			sql += " WHERE 1 = 1 " + whereCond;
		}
		
		sql += " ORDER BY c.voce.id ";

		return sql;
	}//-------------------------------------------------------------------------
	
	public String getSQLCriteria() {
		String sqlCriteria = "";
		
		sqlCriteria = (geo == null || geo.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(c.geo) LIKE '%" + geo.toUpperCase() + "%'");		
		sqlCriteria = (coeff == null || coeff.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(c.coeff) LIKE '" + coeff.toUpperCase() + "'");
		sqlCriteria = (tipo == null || tipo.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(c.voce.utenzaTipo) LIKE '" + tipo.toUpperCase() + "'");
		sqlCriteria = (abit == null || abit.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(c.abit) LIKE '%" + abit.toUpperCase() + "%'");
		
		return sqlCriteria;
	}//-------------------------------------------------------------------------
	
	public String createQueryTariffa() {
		
		String sql = "SELECT t FROM SetarTariffa t";
		
		String whereCond = getSQLCriteriaTariffe();
		
		if (!"".equals(whereCond)) {
			sql += " WHERE 1 = 1 " + whereCond;
		}
		
		sql += " ORDER BY t.voce.id ";

		return sql;
	}//-------------------------------------------------------------------------
	
	public String getSQLCriteriaTariffe() {
		String sqlCriteria = "";
		
		sqlCriteria = (geo == null || geo.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(t.geo) LIKE '%" + geo.toUpperCase() + "%'");		
		sqlCriteria = (tipo == null || tipo.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(t.voce.utenzaTipo) LIKE '" + tipo.toUpperCase() + "'");

		return sqlCriteria;
	}//-------------------------------------------------------------------------
	
	public String createQueryBilancio() {
		
		String sql = "SELECT t FROM SetarBilancioAnnoCorr t";
		
		String whereCond = getSQLCriteriaBilancio();
		
		if (!"".equals(whereCond)) {
			sql += " WHERE 1 = 1 " + whereCond;
		}
		
		sql += " ORDER BY t.id ";

		return sql;
	}//-------------------------------------------------------------------------
	
	public String getSQLCriteriaBilancio() {
		String sqlCriteria = "";
		
		sqlCriteria = (codice == null || codice.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(t.codice) LIKE '" + codice.toUpperCase() + "'");

		return sqlCriteria;
	}//-------------------------------------------------------------------------
	
	public String createQueryConsuntivo() {
		
		String sql = "SELECT t FROM SetarConsuntivoAnnoPrec t";
		
		String whereCond = getSQLCriteriaConsuntivo();
		
		if (!"".equals(whereCond)) {
			sql += " WHERE 1 = 1 " + whereCond;
		}
		
		sql += " ORDER BY t.id ";

		return sql;
	}//-------------------------------------------------------------------------
	
	public String getSQLCriteriaConsuntivo() {
		String sqlCriteria = "";
		
		sqlCriteria = (codice == null || codice.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(t.codice) LIKE '" + codice.toUpperCase() + "'");

		return sqlCriteria;
	}//-------------------------------------------------------------------------
	
	protected String addOperator(String criteria) {  	    
    	    return criteria += " AND ";
    }//-------------------------------------------------------------------------
	
	
}
