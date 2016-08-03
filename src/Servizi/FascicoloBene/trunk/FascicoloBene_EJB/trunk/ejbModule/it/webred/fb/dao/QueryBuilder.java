package it.webred.fb.dao;

import it.webred.fb.ejb.dto.RicercaBeneDTO;

public class QueryBuilder{
	
	private RicercaBeneDTO criteria;
	
	private String SQL_LISTA_BY_INDIRIZZO = "SELECT b FROM DmBIndirizzo i, DmBBene b WHERE i.dtFineVal is null and b.id=i.dmBBene.id";
	private String SQL_LISTA_BY_MAPPALE = "SELECT b FROM DmBMappale i, DmBBene b WHERE i.dtFineVal is null and b.id=i.dmBBene.id ";
	private String SQL_LISTA_BY_INVENTARIO = "SELECT b FROM DmBBeneInv i, DmBBene b WHERE i.dtFineVal is null and b.id=i.dmBBene.id ";
	private String SQL_LISTA_BY_INVENTARIO_TITOLO = "SELECT b FROM DmBBeneInv i, DmBBene b, DmBTitolo t "
												  + "WHERE i.dtFineVal is null and t.dtFineVal is null "
												  + "and b.id=i.dmBBene.id and b.id=t.dmBBene.id ";
	private String SQL_ORDER = " order by to_number(b.codChiave1), to_number(b.codChiave2)";
	
	public QueryBuilder() {}
	
	public QueryBuilder(RicercaBeneDTO criteria) {
		this.criteria = criteria;
	}
	
	protected String createQueryByImmobile(){
		String sql = this.SQL_LISTA_BY_MAPPALE;
		sql+=this.getSQL_ImmobileCriteria();
		return sql;
	}
	
	protected String createQueryByInventario(){
		String sql = this.SQL_LISTA_BY_INVENTARIO;
		sql+=this.getSQL_InventarioCriteria();
		return sql;
	}
	
	protected String createQueryByInventarioTitolo(){
		String sql = this.SQL_LISTA_BY_INVENTARIO_TITOLO;
		sql+=this.getSQL_InventarioCriteria();
		return sql;
	}
	
	protected String createQueryByIndirizzo(){
		
		String sql = this.SQL_LISTA_BY_INDIRIZZO;
		sql += this.getSQL_IndirizzoCriteria();
		
		return sql;
	}
	
	public String createQuery(boolean isCount) {
		
		String sql = null;
		String sqlUiu = null;
		
		if(criteria.isRicercaCatasto())
			sqlUiu= this.createQueryByImmobile();
		else if(criteria.isRicercaIndirizzo())
			sqlUiu= this.createQueryByIndirizzo();
		else if(criteria.isRicercaInventario() && criteria.getTipoDirittoReale()!=null  && !criteria.getTipoDirittoReale().trim().equals(""))
			sqlUiu= this.createQueryByInventarioTitolo();
		else
			sqlUiu= this.createQueryByInventario();
		
		sqlUiu+= this.SQL_ORDER;
			
		/*if(sqlUiu != null){
			if (isCount){
				sql = this.SQL_SELECT_COUNT_LISTA;
				sql = sql.replace("@SQL_SELECT_BASE_LISTA@", sqlUiu);
			}else{
				sql = this.SQL_SELECT_LISTA;
				sql = sql.replace("@SQL_SELECT_BASE_LISTA@", sqlUiu);
			}
		}*/
		
		return sqlUiu;
	}

	private String getSQL_InventarioCriteria() {
		String sqlCriteria = "";
		
		sqlCriteria = (criteria.getCodCatInventario()== null  || criteria.getCodCatInventario().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + addCondition("i.codCatInventariale","=",criteria.getCodCatInventario()));
		sqlCriteria = (criteria.getCodInventario() == null || criteria.getCodInventario().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + addCondition("i.codInventario","=",criteria.getCodInventario()));
		sqlCriteria = (criteria.getCodTipo() == null || criteria.getCodTipo().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + addCondition("i.codTipo","=",criteria.getCodTipo()));
		
		sqlCriteria = (criteria.getTipoDirittoReale() == null || criteria.getTipoDirittoReale().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + addCondition("t.tipoDirittoReale","=",criteria.getTipoDirittoReale()));
		

		sqlCriteria = (criteria.getCodFascicolo() == null || criteria.getCodFascicolo().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + addCondition("i.codCartella","=",criteria.getCodFascicolo()));
		
		return sqlCriteria;
	}
	
	
	private String getSQL_ImmobileCriteria() {
		String sqlCriteria = "";
		String codComune = criteria.getComuneCat()!=null ? criteria.getComuneCat().getCodice() : null;
  		
		sqlCriteria = (codComune == null  || codComune.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + addCondition("i.codComune", "=", codComune));
		sqlCriteria = (criteria.getFoglio() == null  || criteria.getFoglio().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + addCondition("i.foglio","=",criteria.getFoglio()));
		sqlCriteria = (criteria.getMappale() == null || criteria.getMappale().trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + addCondition("i.mappale","=",criteria.getMappale()));
			
		return sqlCriteria;
	}
	
	private String getSQL_IndirizzoCriteria() {
		
		String sqlCriteria = "";
		String civico = criteria.getCivico()!=null ? criteria.getCivico().getCodice() : null;
		String via = criteria.getVia()!=null ? criteria.getVia().getCodice() : null;
		String codComune = criteria.getComuneInd()!=null ? criteria.getComuneInd().getCodice() : null;
			  		
		sqlCriteria = (codComune == null  || codComune.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + addCondition("i.codComune", "=", codComune));
		sqlCriteria = (via == null  || via.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + addCondition("i.codVia", "=", via));
    	sqlCriteria = (civico == null  || civico.trim().equals("") ? sqlCriteria : addOperator(sqlCriteria) + addCondition("i.civico", "=", civico));
		      
		return sqlCriteria;
	}
	
	private String getSQL_SoggettoCriteria(){
		String sqlCriteria = "";
		
		return sqlCriteria;
	}
	protected String addCondition(String field, String operator, String param) {
    	
		String criteria = "";
		operator = operator.trim();
		param = param.trim();
		param = param.replaceAll("\\'", "\\'\\'");
		
		if (operator.equals("="))
    	    criteria += " ("+ field +") "+ operator +" '" + param.toUpperCase() + "' " ;    	
    	else if (operator.equalsIgnoreCase("LIKE"))  	    
    		criteria += " ("+ field +") "+ operator +" '%' || '"+ param.toUpperCase()+"' || '%' " ;  
    	else if (operator.equalsIgnoreCase("IN")){
    		String lista = param.replace(",", "','");
    		String inClause = "( '" + lista + "' )";
    		criteria += " ("+ field +") "+ operator + inClause;
    	}
		
		return criteria;
    }
	
	protected String addOperator(String criteria) {
    	if (criteria == null)
    	    return criteria;    	
    	else    	    
    	    return criteria += " AND ";
    }
	
	
}
