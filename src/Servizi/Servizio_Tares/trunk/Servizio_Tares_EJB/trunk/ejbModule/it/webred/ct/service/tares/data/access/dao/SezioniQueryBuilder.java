package it.webred.ct.service.tares.data.access.dao;

public class SezioniQueryBuilder {
	
	private String chiave = "";
	
	public SezioniQueryBuilder() {

	}//-------------------------------------------------------------------------
	
	
	public String nativeQueryOnLoadCatPrmIncr() {
		
		String sql = "SELECT MIN (TO_DATE (p_min.dt_rif, 'DD/MM/YYYY')) AS data_ini, MAX (TO_DATE (p_max.dt_rif, 'DD/MM/YYYY')) AS data_agg " +
				"FROM load_cat_prm_incr p_min, load_cat_prm_incr p_max ";

		sql += " WHERE p_min.dt_carico = (SELECT MIN (dt_carico) FROM load_cat_prm_incr) AND p_max.dt_carico = (SELECT MAX (dt_carico) FROM load_cat_prm_incr) ";
		
		return sql;
	}//-------------------------------------------------------------------------
	
	public String nativeQueryUnitaImmoDaCatasto(String belfiore) {
		
		String sql = "select distinct sc.codi_fisc_luna, sc.id_sezc, ui.cod_nazionale, " +
				"ui.zona, ui.microzona, to_char(ui.foglio) foglio, ui.particella, ui.sub, to_char(ui.unimm) unimm, " +
				"ui.categoria, ui.classe, ui.consistenza, ui.rendita, ui.sup_cat " +
				"from sitiuiu ui, siticomu sc " +
				"where ui.cod_nazionale=sc.cod_nazionale " +
				"and data_fine_val=to_date('99991231','yyyymmdd') " +
				"and sc.codi_fisc_luna = '" + belfiore + "' "; 
        
		return sql;
	}//-------------------------------------------------------------------------

	
	public String getSQLCriteria() {
		String sqlCriteria = "";

		sqlCriteria = (chiave == null || chiave.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(c.descrizione) LIKE '%" + chiave.toUpperCase() + "%'");

		return sqlCriteria;
	}//-------------------------------------------------------------------------
	
	protected String addOperator(String criteria) {  	    
    	    return criteria += " AND ";
    }//-------------------------------------------------------------------------
	
	
}
