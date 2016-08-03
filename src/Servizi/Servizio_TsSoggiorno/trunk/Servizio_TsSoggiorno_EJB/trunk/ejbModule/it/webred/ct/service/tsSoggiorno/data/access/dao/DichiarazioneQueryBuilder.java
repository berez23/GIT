package it.webred.ct.service.tsSoggiorno.data.access.dao;

import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneSearchCriteria;


public class DichiarazioneQueryBuilder {
	
	private DichiarazioneSearchCriteria criteria;
	
	public DichiarazioneQueryBuilder(DichiarazioneSearchCriteria criteria) {
		this.criteria = criteria;
	}
	
	
	public String createQueryDichiarazioni(boolean isCount) {
				
		String sql = "";
		if (isCount)
			sql = "SELECT COUNT(DISTINCT dic) ";
		else sql = "SELECT DISTINCT dic, snp, p ";
		sql += " FROM IsDichiarazione dic, IsStrutturaSnap snp, IsPeriodo p";
		
		String whereCond = getSQLCriteria();
		
		if (!"".equals(whereCond)) {
			sql += " WHERE dic.fkIsStrutturaSnap = snp.id AND dic.fkIsPeriodo = p.id " + whereCond;
		}
		
		sql += " ORDER BY dic.fkIsPeriodo desc, snp.fkIsStruttura, dic.dtIns desc, dic.dtMod desc";

		return sql;
	}
	
	public String getSQLCriteria() {
		String sqlCriteria = "";
		
		sqlCriteria = (criteria.getCodFiscale() == null || "".equals(criteria.getCodFiscale())? sqlCriteria : addOperator(sqlCriteria) + " UPPER(snp.codfisc) = UPPER('" + criteria.getCodFiscale() + "')");
		
		sqlCriteria = (criteria.getIdClasse() == null || "".equals(criteria.getIdClasse())? sqlCriteria : addOperator(sqlCriteria) + " snp.fkIsClasse = '" + criteria.getIdClasse() + "'");
				
		sqlCriteria = (criteria.getIdStruttura() == null || criteria.getIdStruttura().longValue() == 0? sqlCriteria : addOperator(sqlCriteria) + " snp.fkIsStruttura = " + criteria.getIdStruttura());
		
		sqlCriteria = (criteria.getIdSocieta() == null || criteria.getIdSocieta().longValue() == 0? sqlCriteria : addOperator(sqlCriteria) + " snp.fkIsSocieta = " + criteria.getIdSocieta());

		sqlCriteria = (criteria.getIdPeriodo() == null || criteria.getIdPeriodo().longValue() == 0? sqlCriteria : addOperator(sqlCriteria) + " dic.fkIsPeriodo = " + criteria.getIdPeriodo());
		
		sqlCriteria = (criteria.getDataIns() == null || "".equals(criteria.getDataIns())? sqlCriteria : addOperator(sqlCriteria) + " dic.dtIns >= to_date('" + criteria.getDataIns() + "' , 'dd/MM/yyyy')");
		
		sqlCriteria = (criteria.getIdPeriodoDa() == null || criteria.getIdPeriodoDa().longValue() == 0? sqlCriteria : addOperator(sqlCriteria) + " p.dataDal >= (select da.dataDal FROM IsPeriodo da WHERE da.id = " + criteria.getIdPeriodoDa()) +")";
		
		sqlCriteria = (criteria.getIdPeriodoA() == null || criteria.getIdPeriodoA().longValue() == 0? sqlCriteria : addOperator(sqlCriteria) + " p.dataAl <= (select a.dataAl FROM IsPeriodo a WHERE a.id = " + criteria.getIdPeriodoA()) +")";
		
		return sqlCriteria;
	}
	
	protected String addOperator(String criteria) {  	    
    	    return criteria += " AND ";
    }
	
}
