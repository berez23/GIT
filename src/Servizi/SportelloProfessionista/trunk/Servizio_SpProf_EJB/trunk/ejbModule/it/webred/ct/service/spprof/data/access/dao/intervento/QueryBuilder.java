package it.webred.ct.service.spprof.data.access.dao.intervento;

import java.text.SimpleDateFormat;
import java.util.Date;

import it.webred.ct.service.spprof.data.access.dto.InterventoSearchCriteria;

public class QueryBuilder {

	private InterventoSearchCriteria criteria;
	
	private String stato;
	private String fkSoggetto;
	private Date dataDa;
	private Date dataA;
	private String concNumero;
	private String protNumero;
	private Date protDataDa;
	private Date protDataA;
	
	public QueryBuilder(InterventoSearchCriteria criteria) {
		super();
		this.criteria = criteria;
		
		stato = criteria.getStato();
		fkSoggetto = criteria.getFkSoggetto();
		dataDa = criteria.getDataDa();
		dataA = criteria.getDataA();
		concNumero = criteria.getConcNumero();
		protNumero = criteria.getProtNumero();
		protDataDa = criteria.getProtDataDa();
		protDataA = criteria.getProtDataA();
	}
	
	
	public String createQuery(boolean isCount) {
		String sql = "";
		
		if (isCount)
			sql = "SELECT COUNT(i)";
		else
			sql = "SELECT i";
		
				
		sql += " FROM SSpIntervento i";
		
		String whereCond = getSQLCriteria();
		
		if (!"".equals(whereCond)) {
			sql += " WHERE " + whereCond;
		}
		
		sql += " ORDER BY i.dtIns DESC";

		System.out.println("SQL ["+sql+"]");
		return sql;
	}
	
	
	private String getSQLCriteria() {
		String sqlCriteria = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		sqlCriteria = (stato == null  || stato.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " i.stato='" + stato + "'");
		
		sqlCriteria = (fkSoggetto == null || fkSoggetto.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " i.fkSogg = " + fkSoggetto);
		
		sqlCriteria = (concNumero == null || concNumero.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " i.cConcessioneNumero = '" + concNumero +"'");
		
		sqlCriteria = (protNumero == null || protNumero.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " i.cProtocolloNumero = '" + protNumero + "'");

		sqlCriteria = (dataDa == null? sqlCriteria : addOperator(sqlCriteria) + " i.dtIns >= TO_DATE('"+ sdf.format(dataDa) +"','DD/MM/YYYY')");
		
		sqlCriteria = (dataA == null? sqlCriteria : addOperator(sqlCriteria) + " i.dtIns <= TO_DATE('"+ sdf.format(dataA) +"','DD/MM/YYYY')");
		
		sqlCriteria = (protDataDa == null? sqlCriteria : addOperator(sqlCriteria) + " i.cProtocolloData >= TO_DATE('"+ sdf.format(protDataDa) +"','DD/MM/YYYY')");

		sqlCriteria = (protDataA == null? sqlCriteria : addOperator(sqlCriteria) + " i.cProtocolloData <= TO_DATE('"+ sdf.format(protDataA) +"','DD/MM/YYYY')");

		return sqlCriteria;
	}
	
	
	private String addOperator(String criteria) {
    	if (criteria == null || criteria.equals(""))
    	    return criteria;    	
    	else    	    
    	    return criteria += " AND ";
    }
}
