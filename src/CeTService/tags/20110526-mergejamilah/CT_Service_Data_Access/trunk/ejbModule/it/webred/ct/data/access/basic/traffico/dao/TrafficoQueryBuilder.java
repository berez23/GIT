package it.webred.ct.data.access.basic.traffico.dao;

import it.webred.ct.data.access.basic.traffico.dto.TrafficoSearchCriteria;


public class TrafficoQueryBuilder {
	
	private TrafficoSearchCriteria criteria;
	
	private String codFiscale;
	private String numVerbale;
	private String targa;
	
	public TrafficoQueryBuilder(TrafficoSearchCriteria criteria) {
		this.criteria = criteria;
		codFiscale = criteria.getCodFiscale();
		numVerbale = criteria.getNumVerbale();
		targa = criteria.getTarga();
		
	}
	
	
	public String createQuery() {
					
		String sql = "SELECT multe FROM SitTrffMulte multe";
		
		String whereCond = getSQLCriteria();
		
		if (!"".equals(whereCond)) {
			sql += " WHERE 1 = 1 " + whereCond;
		}
		
		sql += " ORDER BY multe.dataMulta desc";

		return sql;
	}
	
	public String getSQLCriteria() {
		String sqlCriteria = "";
		
		sqlCriteria = (codFiscale == null || codFiscale.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(multe.codFisc) LIKE '" + codFiscale.toUpperCase() + "'");
				
		sqlCriteria = (numVerbale == null || numVerbale.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(multe.nrVerbale) LIKE '" + numVerbale.toUpperCase() + "'");
		
		sqlCriteria = (targa == null || targa.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(multe.targa) LIKE '" + targa.toUpperCase() + "'");

		return sqlCriteria;
	}
	
	protected String addOperator(String criteria) {  	    
    	    return criteria += " AND ";
    }
	
}
