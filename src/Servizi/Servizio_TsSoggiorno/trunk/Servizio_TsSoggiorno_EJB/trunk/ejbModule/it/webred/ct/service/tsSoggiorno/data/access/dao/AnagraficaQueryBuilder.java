package it.webred.ct.service.tsSoggiorno.data.access.dao;

import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneSearchCriteria;


public class AnagraficaQueryBuilder {
	
	private DichiarazioneSearchCriteria criteria;
	
	public AnagraficaQueryBuilder(DichiarazioneSearchCriteria criteria) {
		this.criteria = criteria;
	}
	
	
	public String createQueryStrutture() {
					
		String sql = "SELECT DISTINCT str FROM IsStruttura str, IsClassiStruttura cs, IsSocieta s, IsSocietaSogg ss, IsSoggetto so";
		
		String whereCond = getSQLCriteria();
		
		if (!"".equals(whereCond)) {
			sql += " WHERE str.id = cs.id.fkIsStruttura " +
					"AND so.id = ss.fkIsSoggetto " +
					"AND ss.fkIsSocieta = s.id " +
					"AND s.id = str.fkIsSocieta " + whereCond;
		}
		
		sql += " ORDER BY str.descrizione";

		return sql;
	}
	
	public String getSQLCriteria() {
		String sqlCriteria = "";
		
		sqlCriteria = (criteria.getCodFiscale() == null || "".equals(criteria.getCodFiscale())? sqlCriteria : addOperator(sqlCriteria) + " UPPER(so.codfisc) = UPPER('" + criteria.getCodFiscale() + "')");
		
		sqlCriteria = (criteria.getIdClasse() == null || "".equals(criteria.getIdClasse())? sqlCriteria : addOperator(sqlCriteria) + " cs.id.fkIsClasse = '" + criteria.getIdClasse() + "'");
				
		sqlCriteria = (criteria.getIdStruttura() == null || criteria.getIdStruttura().longValue() == 0? sqlCriteria : addOperator(sqlCriteria) + " str.id = " + criteria.getIdStruttura());
		
		sqlCriteria = (criteria.getIdSocieta() == null || criteria.getIdSocieta().longValue() == 0? sqlCriteria : addOperator(sqlCriteria) + " str.fkIsSocieta = " + criteria.getIdSocieta());

		return sqlCriteria;
	}
	
	protected String addOperator(String criteria) {  	    
    	    return criteria += " AND ";
    }
	
}
