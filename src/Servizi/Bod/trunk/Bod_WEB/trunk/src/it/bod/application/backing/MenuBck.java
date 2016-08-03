package it.bod.application.backing;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import it.bod.application.beans.EnteBean;
import it.bod.application.common.MasterClass;
import it.bod.business.service.BodLogicService;
import it.doro.tools.Character;
import it.persistance.common.SqlHandler;
import it.webred.cet.permission.CeTUser;

public class MenuBck extends MasterClass{

	private static final long serialVersionUID = -3380002088440237190L;
	private static Logger logger = Logger.getLogger("it.bod.application.backing.MenuBck");
	
	private BodLogicService bodLogicService = null;
	
	private String dataFornituraMin = "";
	private String dataFornituraMax = "";
	private BigDecimal totDocfa = null;
	private BigDecimal totDocfaUiResConAnomClass = null;
	private BigDecimal totDocfaUi_C_A10_ConAnomClass = null;
	private BigDecimal totDocfaPresiInCarico = null;
	private BigDecimal totDocfaPresiInCaricoUte = null;
	private BigDecimal totDocfaIstruiti = null;
	private BigDecimal totDocfaEsito662 = null;
	private BigDecimal totDocfaEsito311 = null;
	private BigDecimal totDocfaEsito80 = null;
	private BigDecimal totDocfaEsitoNA = null;
	
	private Long numIstruttorie = null;
	
	public MenuBck() {
		super.initializer();
	}//-------------------------------------------------------------------------
	
	public String home(){
		String esito = "menuBck.home";
		/*
		 * Caricare i dati per la home
		 */
		
		logger.info("Link Home");
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String logout(){
		String esito = "menuBck.logout";
		try {
			logger.info("Link Logout");
		}
		catch (Exception e) {
			logger.info("ERRORE Logout:" + e);
		}
		
		return esito;
	}//-------------------------------------------------------------------------
	
	public String starting(){
		
		String esito = "menuBck.starting";
		logger.info("Starting ... ");
		
		super.initializer();
		
		Properties prop = SqlHandler.loadProperties(propName);
		Hashtable htQry = new Hashtable();
		/*
		 * DOCFA Fornitura Dal - Al
		 */
		String sql = prop.getProperty("qryDataFornituraMin");
		htQry.put("queryString", sql);
		List<Object> lst = bodLogicService.getList(htQry);
		dataFornituraMin = (String)lst.get(0);

		sql = prop.getProperty("qryDataFornituraMax");
		htQry.put("queryString", sql);
		lst = bodLogicService.getList(htQry);
		dataFornituraMax = (String)lst.get(0);
		
		sql = prop.getProperty("qryTotDOCFA");
		htQry.put("queryString", sql);
		lst = bodLogicService.getList(htQry);
		totDocfa = (BigDecimal)lst.get(0);

		sql = prop.getProperty("qryTotDocfaUiResConAnomClass");
		htQry.put("queryString", sql);
		lst = bodLogicService.getList(htQry);
		totDocfaUiResConAnomClass = (BigDecimal)lst.get(0);
		
		sql = prop.getProperty("qryTotDocfaUi_C_A10_ConAnomClass");
		htQry.put("queryString", sql);
		lst = bodLogicService.getList(htQry);
		totDocfaUi_C_A10_ConAnomClass = (BigDecimal)lst.get(0);
		
		sql = prop.getProperty("qryTotDocfaPresiInCarico");
		htQry.put("queryString", sql);
		lst = bodLogicService.getList(htQry);
		totDocfaPresiInCarico = (BigDecimal)lst.get(0);
		/*
		 * Finisce con la parentesi chiusa, quindi dovendo aggiungere la clausola 
		 * per l'operatore dentro la parentesi devo accorciare di uno e richiuderla 
		 * in seguito
		 */
		sql = sql.substring(0, sql.length()-1); 
		sql += " and operatore = '" + utente.getName().trim() + "') ";
		htQry.put("queryString", sql);
		lst = bodLogicService.getList(htQry);
		totDocfaPresiInCaricoUte = (BigDecimal)lst.get(0);
		
		sql = prop.getProperty("qryTotDocfaIstr");
		htQry.put("queryString", sql);
		lst = bodLogicService.getList(htQry);
		totDocfaIstruiti = (BigDecimal)lst.get(0);
		
		sql = prop.getProperty("qryTotDocfaEsito662");
		htQry.put("queryString", sql);
		lst = bodLogicService.getList(htQry);
		totDocfaEsito662 = (BigDecimal)lst.get(0);
		
		sql = prop.getProperty("qryTotDocfaEsito311");
		htQry.put("queryString", sql);
		lst = bodLogicService.getList(htQry);
		totDocfaEsito311 = (BigDecimal)lst.get(0);
		
		sql = prop.getProperty("qryTotDocfaEsito80");
		htQry.put("queryString", sql);
		lst = bodLogicService.getList(htQry);
		totDocfaEsito80 = (BigDecimal)lst.get(0);

		sql = prop.getProperty("qryTotDocfaEsitoNA");
		htQry.put("queryString", sql);
		lst = bodLogicService.getList(htQry);
		totDocfaEsitoNA = (BigDecimal)lst.get(0);
		
		return esito;
	}//-------------------------------------------------------------------------
	

	
	public BodLogicService getBodLogicService() {
		return bodLogicService;
	}

	public void setBodLogicService(BodLogicService bodLogicService) {
		this.bodLogicService = bodLogicService;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Long getNumIstruttorie() {
		return numIstruttorie;
	}

	public void setNumIstruttorie(Long numIstruttorie) {
		this.numIstruttorie = numIstruttorie;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		MenuBck.logger = logger;
	}

	public String getDataFornituraMin() {
		return dataFornituraMin;
	}

	public void setDataFornituraMin(String dataFornituraMin) {
		this.dataFornituraMin = dataFornituraMin;
	}

	public String getDataFornituraMax() {
		return dataFornituraMax;
	}

	public void setDataFornituraMax(String dataFornituraMax) {
		this.dataFornituraMax = dataFornituraMax;
	}

	public BigDecimal getTotDocfa() {
		return totDocfa;
	}

	public void setTotDocfa(BigDecimal totDocfa) {
		this.totDocfa = totDocfa;
	}

	public BigDecimal getTotDocfaUiResConAnomClass() {
		return totDocfaUiResConAnomClass;
	}

	public void setTotDocfaUiResConAnomClass(BigDecimal totDocfaUiResConAnomClass) {
		this.totDocfaUiResConAnomClass = totDocfaUiResConAnomClass;
	}

	public BigDecimal getTotDocfaUi_C_A10_ConAnomClass() {
		return totDocfaUi_C_A10_ConAnomClass;
	}

	public void setTotDocfaUi_C_A10_ConAnomClass(
			BigDecimal totDocfaUi_C_A10_ConAnomClass) {
		this.totDocfaUi_C_A10_ConAnomClass = totDocfaUi_C_A10_ConAnomClass;
	}

	public BigDecimal getTotDocfaPresiInCarico() {
		return totDocfaPresiInCarico;
	}

	public void setTotDocfaPresiInCarico(BigDecimal totDocfaPresiInCarico) {
		this.totDocfaPresiInCarico = totDocfaPresiInCarico;
	}

	public BigDecimal getTotDocfaPresiInCaricoUte() {
		return totDocfaPresiInCaricoUte;
	}

	public void setTotDocfaPresiInCaricoUte(BigDecimal totDocfaPresiInCaricoUte) {
		this.totDocfaPresiInCaricoUte = totDocfaPresiInCaricoUte;
	}

	public BigDecimal getTotDocfaIstruiti() {
		return totDocfaIstruiti;
	}

	public void setTotDocfaIstruiti(BigDecimal totDocfaIstruiti) {
		this.totDocfaIstruiti = totDocfaIstruiti;
	}

	public BigDecimal getTotDocfaEsito662() {
		return totDocfaEsito662;
	}

	public void setTotDocfaEsito662(BigDecimal totDocfaEsito662) {
		this.totDocfaEsito662 = totDocfaEsito662;
	}

	public BigDecimal getTotDocfaEsito311() {
		return totDocfaEsito311;
	}

	public void setTotDocfaEsito311(BigDecimal totDocfaEsito311) {
		this.totDocfaEsito311 = totDocfaEsito311;
	}

	public BigDecimal getTotDocfaEsito80() {
		return totDocfaEsito80;
	}

	public void setTotDocfaEsito80(BigDecimal totDocfaEsito80) {
		this.totDocfaEsito80 = totDocfaEsito80;
	}

	public BigDecimal getTotDocfaEsitoNA() {
		return totDocfaEsitoNA;
	}

	public void setTotDocfaEsitoNA(BigDecimal totDocfaEsitoNA) {
		this.totDocfaEsitoNA = totDocfaEsitoNA;
	}

	

//	public String statistica(){
//		String esito = "success";
//		/*
//		 * Caricare i dati per la home
//		 */
////		FacesContext context = FacesContext.getCurrentInstance();
////		HttpSession jsfSession = (HttpSession) context.getExternalContext().getSession(true);
////		ServletContext servletContext =
////			(ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
////		WebApplicationContext appContext = 
////			WebApplicationContextUtils.getWebApplicationContext(servletContext);
//				
//
//		logger.info("Link Statistica ");
//
////		jsfSession.setAttribute("ctxName", appContext.getServletContext().getContextPath());
//		
//		return esito;
//	}//-------------------------------------------------------------------------
	
//	public String esporta(){
//		String esito = "success";
//		/*
//		 * Caricare i dati per la home
//		 */
//		FacesContext context = FacesContext.getCurrentInstance();
//		HttpSession jsfSession = (HttpSession) context.getExternalContext().getSession(true);
//		ServletContext servletContext =
//			(ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
//		WebApplicationContext appContext = 
//			WebApplicationContextUtils.getWebApplicationContext(servletContext);
//		jsfSession.setAttribute("ctxName", appContext.getServletContext().getContextPath());
//		logger.info("Link Esporta");
//		
//		return esito;
//	}//-------------------------------------------------------------------------
	
}
