package it.webred.ct.service.tsSoggiorno.data.access.dao;

import it.webred.ct.service.tsSoggiorno.data.access.dto.RimbSanzSearchCriteria;


public class RimbSanzQueryBuilder {
	
	private RimbSanzSearchCriteria criteria;
	
	public RimbSanzQueryBuilder(RimbSanzSearchCriteria criteria) {
		this.criteria = criteria;
	}
	
	
	public String createQueryRimborso(boolean isCount) {
				
		String sql = "";
		if (isCount)
			sql = "SELECT COUNT(DISTINCT rs) ";
		else sql = "SELECT DISTINCT rs, cs, so, st, p ";
		sql += " FROM IsRimborso rs, IsClassiStruttura cs, IsStruttura st, IsSocieta so, IsPeriodo p " +
				"WHERE rs.fkIsStruttura = st.id AND st.fkIsSocieta = so.id AND rs.fkIsPeriodo = p.id AND rs.fkIsClasse = cs.id.fkIsClasse ";
		
		String whereCond = getSQLCriteria();
		
		if (!"".equals(whereCond)) {
			sql += whereCond;
		}
		
		sql += " ORDER BY rs.fkIsPeriodo desc, rs.fkIsStruttura";

		return sql;
	}
	
	public String createQuerySanzione(boolean isCount) {
		
		String sql = "";
		if (isCount)
			sql = "SELECT COUNT(DISTINCT rs) ";
		else sql = "SELECT DISTINCT rs, cs, so, st, p ";
		sql += " FROM IsSanzione rs, IsClassiStruttura cs, IsStruttura st, IsSocieta so, IsPeriodo p " +
				"WHERE rs.fkIsStruttura = st.id AND st.fkIsSocieta = so.id AND rs.fkIsPeriodo = p.id AND rs.fkIsClasse = cs.id.fkIsClasse ";
		
		String whereCond = getSQLCriteria();
		
		if (!"".equals(whereCond)) {
			sql += whereCond;
		}
		
		sql += " ORDER BY rs.fkIsPeriodo desc, rs.fkIsStruttura";

		return sql;
	}
	
	public String getSQLCriteria() {
		String sqlCriteria = "";
		
		sqlCriteria = (criteria.getDescrizione() == null || "".equals(criteria.getDescrizione())? sqlCriteria : addOperator(sqlCriteria) + " UPPER(rs.descrizione) like UPPER('%" + criteria.getDescrizione() + "%')");
		
		sqlCriteria = (criteria.getIdClasse() == null || "".equals(criteria.getIdClasse())? sqlCriteria : addOperator(sqlCriteria) + " rs.fkIsClasse = '" + criteria.getIdClasse() + "'");
				
		sqlCriteria = (criteria.getIdSocieta() == null || criteria.getIdSocieta().longValue() == 0? sqlCriteria : addOperator(sqlCriteria) + " st.fkIsSocieta = " + criteria.getIdSocieta());
		
		sqlCriteria = (criteria.getIdStruttura() == null || criteria.getIdStruttura().longValue() == 0? sqlCriteria : addOperator(sqlCriteria) + " rs.fkIsStruttura = " + criteria.getIdStruttura());
		
		sqlCriteria = (criteria.getIdPeriodo() == null || criteria.getIdPeriodo().longValue() == 0? sqlCriteria : addOperator(sqlCriteria) + " rs.fkIsPeriodo = " + criteria.getIdPeriodo());
		
		return sqlCriteria;
	}
	
	protected String addOperator(String criteria) {  	    
    	    return criteria += " AND ";
    }
	
}
