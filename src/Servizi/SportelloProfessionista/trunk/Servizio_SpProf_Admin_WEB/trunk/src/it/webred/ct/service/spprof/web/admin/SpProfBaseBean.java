package it.webred.ct.service.spprof.web.admin;

import it.webred.cet.permission.CeTUser;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.TimeZone;

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

public class SpProfBaseBean implements Serializable {

private static ResourceBundle bundle;
	
	static {
		bundle = ResourceBundle
				.getBundle("it.webred.ct.service.spprof.web.admin.resources.messages");
	}
	
	
	private static Logger logger = Logger.getLogger("spprof_log");

	protected Logger getLogger() {
		return this.logger;
	}
	
	public String getEnte() {
		HttpSession session = this.getSession();
		CeTUser user = (CeTUser) session.getAttribute("user");
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
		HttpSession session = this.getSession();
		CeTUser user = (CeTUser) session.getAttribute("user");
		if (user != null) {
			return user.getUsername();
		}
		
		return null;

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
