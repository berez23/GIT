package it.webred.ct.service.spprof.data.access.dao.anag;

import it.webred.ct.service.spprof.data.access.dto.SoggettoSearchCriteria;

public class QueryBuilder {

	private SoggettoSearchCriteria criteria;
	
	private String nome;
	private String cognome;
	private String username;
	private String denom;
	private String pIVA;
	private String idQualifica;
	private String codFisc;
	
	public QueryBuilder(SoggettoSearchCriteria criteria) {
		super();
		this.criteria = criteria;
		
		nome = criteria.getNome();
		cognome = criteria.getCognome();
		username = criteria.getUsername();
		denom = criteria.getDenom();
		pIVA = criteria.getpIVA();
		idQualifica = criteria.getIdQualifica();
		codFisc = criteria.getCodFisc();
	}
	
	
	public String createQuery(boolean isCount) {
		String sql = "";
		
		if (isCount)
			sql = "SELECT COUNT(sogg)";
		else
			sql = "SELECT sogg, qual";
		
				
		sql += " FROM SSpSoggetto sogg";
		
		if (!isCount) {
			sql += " , SSpQualifica qual";
		}
		
		
		String whereCond = getSQLCriteria();
		
		if (!"".equals(whereCond)) {
			sql += " WHERE " + whereCond;
		}
		
		// Add join conditions if necessary
		
		if (!isCount) {
			if (!"".equals(whereCond))
				sql += " AND";
			else sql += " WHERE";
			sql += " sogg.idQualifica = qual.idQual";
		}
		
		sql += " ORDER BY sogg.cognome, sogg.denomSoc, sogg.nome";

		System.out.println("SQL ["+sql+"]");
		return sql;
	}
	
	
	private String getSQLCriteria() {
		String sqlCriteria = "";
		
		sqlCriteria = (nome == null  || nome.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " sogg.nome='" + nome + "'");
		
		sqlCriteria = (cognome == null || cognome.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " sogg.cognome LIKE '" + cognome +"'");
		
		sqlCriteria = (username == null || username.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " sogg.username LIKE '" + username +"'");
		
		sqlCriteria = (denom == null || denom.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " sogg.denomSoc LIKE '" + denom + "'");

		sqlCriteria = (pIVA == null || pIVA.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " sogg.parIva = '" + pIVA + "'");
		
		sqlCriteria = (idQualifica == null || idQualifica.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " sogg.idQualifica = " + idQualifica);
		
		sqlCriteria = (codFisc == null || codFisc.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " sogg.codFis = '" + codFisc + "'");

		return sqlCriteria;
	}
	
	
	private String addOperator(String criteria) {
    	if (criteria == null || criteria.equals(""))
    	    return criteria;    	
    	else    	    
    	    return criteria += " AND ";
    }
}
