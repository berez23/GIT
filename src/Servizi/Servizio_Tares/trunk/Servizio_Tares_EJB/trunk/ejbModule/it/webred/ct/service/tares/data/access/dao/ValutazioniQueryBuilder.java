package it.webred.ct.service.tares.data.access.dao;

import it.webred.ct.service.tares.data.access.dto.ValutazioniSearchCriteria;

public class ValutazioniQueryBuilder {
	
	private ValutazioniSearchCriteria criteria;
	
	private String foglio = "";
	private String particella = "";
	private String sub = "";
	private String unimm = "";
	private String categoria = "";
	private String categoriaNo = "";
	private String classe = "";
	private String idSezc = "";
	private String statoCatasto = "";
	private String renditaDal = "";
	private String renditaAl = "";
	private String vanoDal = "";
	private String vanoAl = "";
	private String supCatTarsuDal = "";
	private String supCatTarsuAl = "";
	private String difforme = "";
	
	public ValutazioniQueryBuilder(ValutazioniSearchCriteria criteria) {
		this.criteria = criteria;
		this.foglio = criteria.getFoglio();
		this.particella = criteria.getParticella();
		this.sub = criteria.getSub();
		this.unimm = criteria.getUnimm();
		this.categoria = criteria.getCategoria();
		this.categoriaNo = criteria.getCategoriaNo();
		this.classe = criteria.getClasse();
		this.idSezc = criteria.getIdSezc();
		this.statoCatasto = criteria.getStatoCatasto();
		this.renditaDal = criteria.getRenditaDal();
		this.renditaAl = criteria.getRenditaAl();
		this.vanoDal = criteria.getVanoDal();
		this.vanoAl = criteria.getVanoAl();
		this.supCatTarsuDal = criteria.getSupCatTarsuDal();
		this.supCatTarsuAl = criteria.getSupCatTarsuAl();
		this.difforme = criteria.getDifformita();
	}//-------------------------------------------------------------------------
	
	
	public String createQuery() {
					
		String sql = "SELECT elab FROM SetarElab elab ";
		
		//String sql = "SELECT elab, ss1 FROM SetarElab elab LEFT JOIN SetarSegnala1 ss1 WITH elab.id.foglio = ss1.foglio AND elab.id.particella = ss1.numero AND elab.id.unimm = ss1.subalterno AND ss1.difforme is not null  ";
		
		String whereCond = getSQLCriteria();
		
		if (!"".equals(whereCond)) {
			sql += " WHERE 1 = 1 " + whereCond;
			//sql += " AND " + whereCond;
		}
		
		sql += " ORDER BY elab.id.foglio, elab.id.particella, elab.id.unimm ";

		return sql;
	}//-------------------------------------------------------------------------
	
	public String createQueryCount() {
		
		String sql = "select count(*) FROM SetarElab elab  ";
		
		String whereCond = getSQLCriteria();
		
		if (!"".equals(whereCond)) {
			sql += " WHERE 1 = 1 " + whereCond;
		}
		
		return sql;
	}//-------------------------------------------------------------------------
	
	public String getSQLCriteria() {
		String sqlCriteria = "";
		
		sqlCriteria = (foglio == null || foglio.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(elab.id.foglio) LIKE '" + foglio.toUpperCase() + "'");

		sqlCriteria = (particella == null || particella.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(elab.id.particella) LIKE lpad('" + particella.toUpperCase() + "' ,5,'0') ");
		
		sqlCriteria = (sub == null || sub.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(elab.sub) LIKE '" + sub.toUpperCase() + "' ");
		
		sqlCriteria = (unimm == null || unimm.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(elab.id.unimm) LIKE '" + unimm.toUpperCase() + "'");
		
		sqlCriteria = (categoria == null || categoria.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(elab.categoria) IN ( " );
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
		
		sqlCriteria = (categoriaNo == null || categoriaNo.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(elab.categoria) NOT IN ( " );
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
		
		sqlCriteria = (classe == null || classe.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(elab.classe) LIKE '" + classe.toUpperCase() + "'");
		
		sqlCriteria = (idSezc == null || idSezc.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(elab.idSezc) LIKE '" + idSezc.toUpperCase() + "'");

		sqlCriteria = (statoCatasto == null || statoCatasto.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " UPPER(elab.statoCatasto) LIKE '" + statoCatasto.toUpperCase() + "'");
		
		sqlCriteria = (renditaDal == null || renditaDal.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " elab.rendita >= '" + renditaDal.replace('.', ',') + "'");
		
		sqlCriteria = (renditaAl == null || renditaAl.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " elab.rendita <= '" + renditaAl.replace('.', ',') + "'");
		
		sqlCriteria = (vanoDal == null || vanoDal.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " elab.vano >= '" + vanoDal.replace('.', ',') + "'");
		
		sqlCriteria = (vanoAl == null || vanoAl.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " elab.vano <= '" + vanoAl.replace('.', ',') + "'");
		
		sqlCriteria = (supCatTarsuDal == null || supCatTarsuDal.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " nvl(elab.supCatTarsu,0) >= '" + supCatTarsuDal.replace('.', ',') + "'");
		
		sqlCriteria = (supCatTarsuAl == null || supCatTarsuAl.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " nvl(elab.supCatTarsu,0) <= '" + supCatTarsuAl.replace('.', ',') + "'");
		
		sqlCriteria = (difforme == null || difforme.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + " EXISTS (select ss1 from SetarSegnala1 ss1 WHERE elab.id.foglio = ss1.foglio AND elab.id.particella = ss1.numero AND elab.id.unimm = ss1.subalterno AND ss1.difforme LIKE '" + difforme + "' ) ");

		return sqlCriteria;
	}//-------------------------------------------------------------------------
	
	protected String addOperator(String criteria) {  	    
    	    return criteria += " AND ";
    }//-------------------------------------------------------------------------
	
	
}
