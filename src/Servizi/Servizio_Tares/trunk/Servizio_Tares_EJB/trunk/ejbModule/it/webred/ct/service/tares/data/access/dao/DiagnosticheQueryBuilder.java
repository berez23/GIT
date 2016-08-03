package it.webred.ct.service.tares.data.access.dao;

import it.webred.ct.service.tares.data.access.dto.DiagnosticheSearchCriteria;

public class DiagnosticheQueryBuilder {
	
	private DiagnosticheSearchCriteria criteria;
	
	private String chiave = "";
	
	public DiagnosticheQueryBuilder(DiagnosticheSearchCriteria criteria) {
		this.criteria = criteria;
		this.chiave = criteria.getChiave();
	}//-------------------------------------------------------------------------
	
	
	public String createQuery() {
					
		String sql = "SELECT diag FROM SetarDia diag";
		
		String whereCond = getSQLCriteria();
		
		if (!"".equals(whereCond)) {
			sql += " WHERE 1 = 1 " + whereCond;
		}
		
		sql += " ORDER BY descrizione ";

		return sql;
	}//-------------------------------------------------------------------------
	
	public String createQueryCount() {
		
		String sql = "select count(*) FROM SetarDia diag";
		
		String whereCond = getSQLCriteria();
		
		if (!"".equals(whereCond)) {
			sql += " WHERE 1 = 1 " + whereCond;
		}
		
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
