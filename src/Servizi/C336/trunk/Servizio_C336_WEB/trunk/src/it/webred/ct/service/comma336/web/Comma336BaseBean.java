package it.webred.ct.service.comma336.web;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.comune.ComuneService;
import it.webred.ct.config.parameters.dto.AmKeyValueDTO;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.data.access.aggregator.elaborazioni.ElaborazioniService;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.common.CommonService;
import it.webred.ct.data.access.basic.concedilizie.ConcessioniEdilizieService;
import it.webred.ct.data.access.basic.docfa.DocfaService;
import it.webred.ct.data.access.basic.c336.C336GesPraticaService;
import it.webred.ct.data.access.basic.c336.C336PraticaService;
import it.webred.ct.service.comma336.data.access.C336CommonService;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class Comma336BaseBean {

	private static ResourceBundle bundle;
	private static String codNazionaleEnte;
		
	protected C336CommonService c336CommonService = (C336CommonService) getEjb("Servizio_C336", "Servizio_C336_EJB", "C336CommonServiceBean");
	
	protected C336GesPraticaService praticaManagerService = (C336GesPraticaService) getEjb("CT_Service", "CT_Service_Data_Access", "C336GesPraticaServiceBean");
	
	protected C336PraticaService praticaService = (C336PraticaService) getEjb("CT_Service", "CT_Service_Data_Access", "C336PraticaServiceBean");
		
	private ParameterService paramService = (ParameterService) getEjb("CT_Service", "CT_Config_Manager", "ParameterBaseService");
	
	protected ComuneService comuneService = (ComuneService) getEjb("CT_Service", "CT_Config_Manager", "ComuneServiceBean");
	
	protected CatastoService catastoService = (CatastoService) getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
	
	protected DocfaService docfaService = (DocfaService) getEjb("CT_Service", "CT_Service_Data_Access", "DocfaServiceBean");
	
	protected ConcessioniEdilizieService concEdilizieService = (ConcessioniEdilizieService) getEjb("CT_Service", "CT_Service_Data_Access", "ConcessioniEdilizieServiceBean");
	
	protected ElaborazioniService elaborazioniService = (ElaborazioniService) getEjb("CT_Service", "CT_Service_Data_Access", "ElaborazioniServiceBean");
	
	static {
		bundle = ResourceBundle.getBundle("it.webred.ct.service.comma336.web.resources.messages");
	}
	
	protected final String DIR_SERVIZIO = "servizioc336";
	
	public ParameterService getParamService() {
		return paramService;
	}

	public void setParamService(ParameterService paramService) {
		this.paramService = paramService;
	}

	protected static final Logger logger = Logger.getLogger("C336service.log");
	

	/**
	 * Recupera il valore del codice nazionale del Comune Corrente
	 * 
	 * @return Codice nazionale Comune
	 */
	public String getCodNazionaleFromUser() {		
		
		if(codNazionaleEnte == null || codNazionaleEnte.length()==0){
			String msg = "codNazionaleEnte";
			try{
			/*	
			CeTBaseObject cet = new CeTBaseObject();
			codNazionaleEnte = getCommonService().getEnte(cet).getCodent();
				
				CeTUser user = this.getUser();
				if(user != null)
					codNazionaleEnte = user.getCurrentEnte();
				*/
			
			codNazionaleEnte = this.getCurrentEnte();
			}catch(Throwable t) {
				addErrorMessage(msg+".error", t.getMessage());
				logger.error(t.getMessage(), t);
			}
		}
		return codNazionaleEnte;
	}
	
	protected String getCurrentEnte(){
		codNazionaleEnte = null;
		
		CeTUser user = this.getUser();
		if(user != null)
			codNazionaleEnte = user.getCurrentEnte();
		else
			logger.info("User null");
		
		//logger.info("Ente Corrente:"+ codNazionaleEnte);
		
		return codNazionaleEnte;
	}
	
	
	protected Date getCurrentDate(){

		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}
	
	protected String getTimeStamp(){
		
		Date date = this.getCurrentDate();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sf.format(date);
	}
	
	protected String getParamValue(String paramName) {
		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String path = ctx.getInitParameter(paramName);
		return path;
	}
	
	
	protected Logger getLogger() {
		return logger;
	}

	/*public Object getCTRemoteService(String serviceName) {		
		Object o = getRemoteService(serviceName,"CT_Service");				
		return o;
	}
	
	private Object getRemoteService(String serviceName, String context) {
		ServiceLocator locator = ServiceLocator.getInstance();
		Object o = locator.getReference(context + "/" + serviceName + "/remote");
		return o;
	}

	public Object getSFRemoteService(String serviceName) {
		HttpSession session = getSession();
		Object o = session.getAttribute(serviceName);

		if (o != null)
			return o;

		ServiceLocator locator = ServiceLocator.getInstance();
		o = locator.getReference("CT_Service/" + serviceName + "/remote");
		session.setAttribute(serviceName, o);
		return o;
	}*/

	private void addMessage(String messageKey, FacesMessage.Severity severity,String details) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String txt = bundle.getString(messageKey);
		facesContext.addMessage(null, new FacesMessage(severity, txt,details == null ? " " : details));
	}


	public void addInfoMessage(String messageKey) {
		addMessage(messageKey, FacesMessage.SEVERITY_INFO, null);
	}

	public void addErrorMessage(String messageKey, String details) {
		addMessage(messageKey, FacesMessage.SEVERITY_ERROR, details);
	}
	
/*	public void addWarnMessage(String messageKey, String details) {
		addMessage(messageKey, FacesMessage.SEVERITY_WARN, details);
	}*/

	public TimeZone getTimeZone() {
		return TimeZone.getDefault();
	}

	public HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
	}

	public HttpSession getSession() {
		return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
	}

	public HttpServletResponse getResponse() {
		return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	}

	public String getMessage(String messageKey) {
		String txt = bundle.getString(messageKey);
		return txt;
	}

	public Object getBeanReference(String beanName) {
		FacesContext context = FacesContext.getCurrentInstance();
		Application a = context.getApplication();
		Object o = a.getVariableResolver().resolveVariable(context, beanName);
		return o;
	}
	
	public CeTUser getUser(){
		return (CeTUser) getSession().getAttribute("user");
	}

	public void fillEnte(CeTBaseObject ro){
		
		ro.setEnteId(this.getCurrentEnte());
		
	}
	
	protected String getRootPathDatiDiogene(){
		String path = null;
	
		try{
		  
          ParameterSearchCriteria criteria = new ParameterSearchCriteria();
  			criteria.setKey("dir.files.datiDiogene");
  			criteria.setComune(null);
  			criteria.setSection(null);
  		
  			return doGetListaKeyValue(criteria) ;
		}catch(Exception e){
			
		}
		
		return path;
	}
	
	protected String getRootPathDatiApplicazione(){
		String path = null;
	
		try{
		  
          ParameterSearchCriteria criteria = new ParameterSearchCriteria();
  			//criteria.setKey("dir.files.dati");
          	criteria.setKey("dir.files.datiDiogene");
  			criteria.setComune(null);
  			criteria.setSection(null);
  		
  			return doGetListaKeyValue(criteria) ;
		}catch(Exception e){
			
		}
		
		return path;
	}
	
	
	private String doGetListaKeyValue(ParameterSearchCriteria criteria)
	{
		try
		{
			if (paramService==null)
			{
				this.getLogger().warn("ParameterService == NULL");
				return "";
			}
			
			List<AmKeyValueDTO> l = paramService.getListaKeyValue(criteria);
			if (l!=null && l.size()>0)
				return ((AmKeyValueDTO)l.get(0)).getAmKeyValueExt().getValueConf();
			else
			{
				this.getLogger().warn(" LISTA RITORNO DA parameterService.getListaKeyValue VUOTA");
				return "";
			}
		}
		catch (Exception ex)
		{
			this.getLogger().error(" doGetListaKeyValue ERRORE = " + ex.getMessage());
			return "";
		}

	}

	public void setComuneService(ComuneService comuneService) {
		this.comuneService = comuneService;
	}

	public ComuneService getComuneService() {
		return comuneService;
	}

	public void setC336CommonService(C336CommonService c336CommonService) {
		this.c336CommonService = c336CommonService;
	}

	public C336CommonService getC336CommonService() {
		return c336CommonService;
	}
	
	protected int getCurrentYear(){
		Date date = this.getCurrentDate();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy");
		String d = sf.format(date);
		return Integer.parseInt(d);
	}

	public void setCatastoService(CatastoService catastoService) {
		this.catastoService = catastoService;
	}

	public CatastoService getCatastoService() {
		return catastoService;
	}

	public C336GesPraticaService getPraticaManagerService() {
		return praticaManagerService;
	}

	public void setPraticaManagerService(C336GesPraticaService praticaManagerService) {
		this.praticaManagerService = praticaManagerService;
	}

	public C336PraticaService getPraticaService() {
		return praticaService;
	}

	public void setPraticaService(C336PraticaService praticaService) {
		this.praticaService = praticaService;
	}

	public Object getEjb(String ear, String module, String ejbName){
		Context cont;
		try {
			cont = new InitialContext();
			return cont.lookup("java:global/" + ear + "/" + module + "/" + ejbName);
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
}

