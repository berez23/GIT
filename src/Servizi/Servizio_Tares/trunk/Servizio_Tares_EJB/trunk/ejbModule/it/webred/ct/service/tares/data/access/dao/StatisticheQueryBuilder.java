package it.webred.ct.service.tares.data.access.dao;

import it.webred.ct.service.tares.data.access.dto.StatisticheSearchCriteria;

public class StatisticheQueryBuilder {
	
	private StatisticheSearchCriteria criteria;
	
	private String categoria = "";
	private String categoriaNo = "";
	
	public StatisticheQueryBuilder(StatisticheSearchCriteria criteria) {
		this.criteria = criteria;
		this.categoria = criteria.getCategoria();
		this.categoriaNo = criteria.getCategoriaNo();
		
	}//-------------------------------------------------------------------------
	
	
	public String createQuery() {
					
		String sql = "SELECT statis FROM SetarStat statis";
		
		String whereCond = getSQLCriteria();
		
		if (!"".equals(whereCond)) {
			sql += " WHERE 1 = 1 " + whereCond;
		}
		
		sql += " ORDER BY id ";

		return sql;
	}//-------------------------------------------------------------------------
	
	public String createQueryCount() {
		
		String sql = "select count(*) FROM SetarStat statis";
		
		String whereCond = getSQLCriteria();
		
		if (!"".equals(whereCond)) {
			sql += " WHERE 1 = 1 " + whereCond;
		}
		
		return sql;
	}//-------------------------------------------------------------------------
	
	public String getSQLCriteria() {
		String sqlCriteria = "";
		
		sqlCriteria = (categoria == null || categoria.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(statis.categoria) IN ( " );
		if (categoria != null && !categoria.trim().equalsIgnoreCase("")){
			String[] aryCat = categoria.split(",");
			String opzIn = "";
			if (aryCat != null && aryCat.length>0){
				for (int index=0; index<aryCat.length; index++){
					opzIn += "'" + aryCat[index].toUpperCase() + "',"; 
				}
			}
			sqlCriteria += opzIn.substring(0, opzIn.lastIndexOf(",")) + " )";
		}
		
		sqlCriteria = (categoriaNo == null || categoriaNo.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(statis.categoria) NOT IN ( " );
		if (categoriaNo != null && !categoriaNo.trim().equalsIgnoreCase("")){
			String[] aryCat = categoriaNo.split(","); 
			String opzIn = "";
			if (aryCat != null && aryCat.length>0){
				for (int index=0; index<aryCat.length; index++){
					opzIn += "'" + aryCat[index].toUpperCase() + "',"; 
				}
			}
			sqlCriteria += opzIn.substring(0, opzIn.lastIndexOf(",")) + " )";
		}
		

		return sqlCriteria;
	}//-------------------------------------------------------------------------
	
	protected String addOperator(String criteria) {  	    
    	    return criteria += " AND ";
    }//-------------------------------------------------------------------------
	
	
}
