package it.webred.ct.service.gestprep.data.access.dao.operazioni;

import it.webred.ct.service.gestprep.data.access.dto.OperazioneSearchCriteria;




public class QueryBuilder  {

	private OperazioneSearchCriteria criteria;
	
	private String nome;
	private String cognome;
	private String denom;
	private String pIVA;
	private String idQualifica;
	private String codFisc;
	
	public QueryBuilder(OperazioneSearchCriteria criteria) {
		this.criteria = criteria;
		
		nome = criteria.getNome();
		cognome = criteria.getCognome();
		denom = criteria.getDenom();
		pIVA = criteria.getpIVA();
		codFisc = criteria.getCodFisc();
	}
	
	
	public String createQuery(boolean isCount) {
		
		String sql = "";
		
		if (isCount)
			sql = "SELECT COUNT(oper)";
		else
			sql = "SELECT oper, sogg, tar, vis";
		
				
		sql += " FROM GestPrepOperazVisure oper,  GestPrepSoggetti sogg";
		
		if (!isCount) {
			sql += " , GestPrepTariffaVisura tar, GestPrepAnagVisura vis";
		}
		
		
		String whereCond = getSQLCriteria();
		
		if (whereCond.equals(""))
			return null;
		
		sql += " WHERE ";
		if (!"".equals(whereCond)) {
			sql += ( whereCond + " AND " );
		}
		
		// Add join conditions if necessary
		
		sql += " sogg.idSogg = oper.idSoggetto";
		
		if (!isCount) {
			sql += " AND";
			sql += " oper.idTariffa = tar.idTar AND oper.idVisura = vis.idVis";
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

		sqlCriteria = (pIVA == null || pIVA.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " sogg.pIVA = '" + pIVA + "'");
		
		
		return sqlCriteria;
	}
	
	
	private String addOperator(String criteria) {
    	if (criteria == null || criteria.equals(""))
    	    return criteria;    	
    	else    	    
    	    return criteria += " AND ";
    }
}
