package it.webred.ct.service.muidocfa.web.bean;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.comune.ComuneService;
import it.webred.ct.config.parameters.dto.AmKeyValueDTO;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.diagnostiche.ici.DocfaIciReportService;
import it.webred.ct.data.access.basic.diagnostiche.tarsu.DocfaTarReportService;
import it.webred.ct.data.access.basic.docfa.DocfaService;
import it.webred.ct.data.access.basic.tarsu.TarsuService;
import it.webred.ct.support.datarouter.CeTBaseObject;

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

public class MuiDocfaBaseBean {
	
	protected static Logger logger = Logger.getLogger("muidocfa.log");
	
	private static ResourceBundle bundle;
	
	protected DocfaService docfaService = (DocfaService) getEjb("CT_Service", "CT_Service_Data_Access", "DocfaServiceBean");
	
	protected DocfaIciReportService docfaIciReportService = (DocfaIciReportService) getEjb("CT_Service", "CT_Service_Data_Access", "DocfaIciReportServiceBean");
	
	protected DocfaTarReportService docfaTarReportService = (DocfaTarReportService) getEjb("CT_Service", "CT_Service_Data_Access", "DocfaTarReportServiceBean");
	
	protected CatastoService catastoService = (CatastoService) getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
	
	protected ComuneService comuneService = (ComuneService) getEjb("CT_Service", "CT_Config_Manager", "ComuneServiceBean");
	
	protected ParameterService paramService = (ParameterService) getEjb("CT_Service", "CT_Config_Manager", "ParameterBaseService");
	
	protected TarsuService tarsuService = (TarsuService) getEjb("CT_Service", "CT_Service_Data_Access", "TarsuServiceBean");
	
	static {
		bundle = ResourceBundle.getBundle("it.webred.ct.service.muidocfa.web.resources.messages");
	}
	
	private void addMessage(String messageKey, FacesMessage.Severity severity,String details) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String txt = bundle.getString(messageKey);

		facesContext.addMessage(null, new FacesMessage(severity, txt,
				details == null ? " " : details));
	}
	
	
	public void addInfoMessage(String messageKey) {
		addMessage(messageKey, FacesMessage.SEVERITY_INFO, null);
	}

	public void addErrorMessage(String messageKey, String details) {
		addMessage(messageKey, FacesMessage.SEVERITY_ERROR, details);
	}

	public TimeZone getTimeZone() {
		return TimeZone.getDefault();
	}

	public HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
	}

	public HttpSession getSession() {
		return (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
	}

	public HttpServletResponse getResponse() {
		return (HttpServletResponse) FacesContext.getCurrentInstance()
				.getExternalContext().getResponse();
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
	
	private String getParamValue(String paramName) {
		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String path = ctx.getInitParameter(paramName);
		return path;
	}
	
	protected String getRootPathDatiDiogene(){
		String path = null;
	
		//path = getParamValue("root_path_dati_diogene");
		
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
	
	private String doGetListaKeyValue(ParameterSearchCriteria criteria)
	{
		try
		{
			if (paramService==null)
			{
				logger.warn("ParameterService == NULL");
				return "";
			}
			
			List<AmKeyValueDTO> l = paramService.getListaKeyValue(criteria);
			if (l!=null && l.size()>0)
				return ((AmKeyValueDTO)l.get(0)).getAmKeyValueExt().getValueConf();
			else
			{
				logger.warn(" LISTA RITORNO DA parameterService.getListaKeyValue VUOTA");
				return "";
			}
		}
		catch (Exception ex)
		{
			logger.error(" doGetListaKeyValue ERRORE = " + ex.getMessage());
			return "";
		}

	}
	
	protected Logger getLogger() {
		return logger;
	}
	
	public CeTUser getUser(){
		
		return (CeTUser) getSession().getAttribute("user");
	}
	
	public CeTBaseObject fillEnte(CeTBaseObject ro){
		
		CeTUser user = getUser();
		if(user != null){
			ro.setEnteId(user.getCurrentEnte());
			ro.setUserId(user.getUsername());
			ro.setSessionId(user.getSessionId());
			
		}
		
		return ro;
	
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
