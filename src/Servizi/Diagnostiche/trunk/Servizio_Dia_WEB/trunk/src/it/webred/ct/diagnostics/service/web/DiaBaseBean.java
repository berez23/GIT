package it.webred.ct.diagnostics.service.web;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.config.model.AmComune;
import it.webred.ct.config.parameters.comune.ComuneService;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class DiaBaseBean implements Serializable {
	
	private static ResourceBundle bundle;
	
	protected ComuneService comuneService = (ComuneService) getEjb(
			"CT_Service", "CT_Config_Manager", "ComuneServiceBean");
	
	protected String enteDescr;
	
	private static Logger logger = Logger.getLogger("dia.log");
	
	static {
		bundle = ResourceBundle
				.getBundle("it.webred.ct.diagnostics.service.web.resources.messages");
	}
	
	
	protected Logger getLogger() {
		return this.logger;
	}
	
	public String getEnte() {
		CeTUser user = this.getUser();
		if (user != null) {
			return user.getCurrentEnte();
		}
		
		return null;
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
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String txt = bundle.getString(messageKey);
		return txt;
	}
	
	public String getMessage(String messageKey, Object param) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String txt = bundle.getString(messageKey);
		
		if (param != null) {
			Object[] params = {param};
			txt = MessageFormat.format(txt, params);
		}
		return txt;
	}	



	public Object getBeanReference(String beanName) {
		FacesContext context = FacesContext.getCurrentInstance();
		Application a = context.getApplication();
		Object o = a.getVariableResolver().resolveVariable(context, beanName);
		return o;
	}
	
	private void addMessage(String messageKey, FacesMessage.Severity severity,
			String details, Object param) {
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		/*
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String txt = bundle.getString(messageKey);
		*/
		String txt = getMessage(messageKey, param);
		
		facesContext.addMessage(null, new FacesMessage(severity, txt,
				details == null ? " " : details));

	}

	public void addInfoMessage(String messageKey) {
		addMessage(messageKey, FacesMessage.SEVERITY_INFO, null, null);
	}
	
	public void addInfoMessage(String messageKey, Object param) {
		addMessage(messageKey, FacesMessage.SEVERITY_INFO, null, param);
	}
	

	public void addErrorMessage(String messageKey, String details) {
		addMessage(messageKey, FacesMessage.SEVERITY_ERROR, details, null);
	}

	public TimeZone getTimeZone() {
		return TimeZone.getDefault();
	}

	public CeTUser getUser(){
		return (CeTUser) getSession().getAttribute("user");
	}
	

	public String getUsername() {
		CeTUser user = this.getUser();
		if (user != null) 
			return user.getUsername();
		
		return null;

	}
	
	public String getEnteDescr() {
		enteDescr = "";
		String es = getRequest().getParameter("es");
		if(es != null && !es.equals("") && getUser() != null){
			AmComune am = comuneService.getComune(getUser().getCurrentEnte());
			enteDescr = am != null? am.getDescrizione(): "";
			enteDescr = enteDescr.substring(0,1).toUpperCase() + enteDescr.substring(1).toLowerCase();
		}
		return enteDescr;
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
