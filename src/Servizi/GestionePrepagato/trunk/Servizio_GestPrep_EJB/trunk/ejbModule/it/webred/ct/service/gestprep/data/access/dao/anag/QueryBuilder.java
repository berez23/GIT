package it.webred.ct.service.gestprep.data.access.dao.anag;

import it.webred.ct.service.gestprep.data.access.dto.SoggettoSearchCriteria;


public class QueryBuilder  {

	private SoggettoSearchCriteria criteria;
	
	private String nome;
	private String cognome;
	private String denom;
	private String pIVA;
	private String idQualifica;
	private String codFisc;
	
	public QueryBuilder(SoggettoSearchCriteria criteria) {
		this.criteria = criteria;
		
		nome = criteria.getNome();
		cognome = criteria.getCognome();
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
		
				
		sql += " FROM GestPrepSoggetti sogg";
		
		if (!isCount) {
			sql += " , GestPrepQualifica qual";
		}
		
		
		String whereCond = getSQLCriteria();
		
		if (whereCond.equals(""))
			return null;
		
		if (!"".equals(whereCond)) {
			sql += " WHERE " + whereCond;
		}
		
		// Add join conditions if necessary
		
		if (!isCount) {
			if (!"".equals(whereCond))
				sql += " AND";
			sql += " sogg.idQualifica = qual.idQual";
		}
		
		sql += " ORDER BY sogg.cognome, sogg.denomSoc, sogg.nome";

		//System.out.println("SQL ["+sql+"]");
		return sql;
	}
	
	private String getSQLCriteria() {
		String sqlCriteria = "";
		
		sqlCriteria = (nome == null  || nome.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " sogg.nome='" + nome + "'");
		
		sqlCriteria = (cognome == null || cognome.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " sogg.cognome LIKE '" + cognome +"'");
		
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
