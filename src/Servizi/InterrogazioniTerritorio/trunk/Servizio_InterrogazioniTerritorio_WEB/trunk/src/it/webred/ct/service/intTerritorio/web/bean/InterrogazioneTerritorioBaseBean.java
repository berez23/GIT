package it.webred.ct.service.intTerritorio.web.bean;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.aggregator.ejb.DatiCivicoService;
import it.webred.ct.config.parameters.comune.ComuneService;

import java.util.Calendar;
import java.util.Date;
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

public class InterrogazioneTerritorioBaseBean {
	
	private static Logger logger = Logger.getLogger(InterrogazioneTerritorioBaseBean.class.getName());
	
	private static ResourceBundle bundle;

	protected DatiCivicoService datiCivicoService = (DatiCivicoService) getEjb("CT_Service", "CT_Service_Aggregator_EJB", "DatiCivicoServiceBean");
	
	public ComuneService comuneService = (ComuneService) getEjb("CT_Service", "CT_Config_Manager", "ComuneServiceBean");
	
	static {
		bundle = ResourceBundle.getBundle("it.webred.ct.service.intTerritorio.web.resources.messages");
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
	
	protected String getParamValue(String paramName) {
		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String path = ctx.getInitParameter(paramName);
		return path;
	}
	
	protected Logger getLogger() {
		return this.logger;
	}
	
	public CeTUser getUser(){
		
		return (CeTUser) getSession().getAttribute("user");
		
	}
	
	protected Date getCurrentDate(){

		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}
	
	public static Object getEjb(String ear, String module, String ejbName){
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
