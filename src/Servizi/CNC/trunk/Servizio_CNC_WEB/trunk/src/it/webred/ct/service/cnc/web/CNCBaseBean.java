package it.webred.ct.service.cnc.web;


import it.webred.cet.permission.CeTUser;
import it.webred.ct.data.access.basic.cnc.CNCDTO;
import it.webred.ct.data.access.basic.cnc.CNCDataIn;
import it.webred.ct.service.cnc.data.access.CNCCommonService;

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



public class CNCBaseBean {

	private static ResourceBundle bundle;
	private CNCCommonService cncService;
	private it.webred.ct.data.access.basic.cnc.CNCCommonService cncCommonService;
	
	static {
		bundle = ResourceBundle
				.getBundle("it.webred.ct.service.cnc.web.resources.messages");
	}

	protected static Logger logger = Logger.getLogger("cnc.log");

	protected Logger getLogger() {
		return this.logger;
	}
	
	public CeTUser getUser(){
		
		return (CeTUser) getSession().getAttribute("user");
		
	}
	
	public String getEnte() {
		HttpSession session = this.getSession();
		CeTUser user = (CeTUser) session.getAttribute("user");
		if (user != null) {
			return user.getCurrentEnte();
		}
		
		return null;
	}
	
/*
	public Object getCTRemoteService(String serviceName) {		
		Object o = getRemoteService(serviceName, "CT_Service");				
		return o;
	}
	
	private Object getRemoteService(String serviceName, String context) {
		ServiceLocator locator = ServiceLocator.getInstance();
		Object o = locator
				.getReference(context + "/" + serviceName + "/remote");
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
	}
*/
	
	
	private void addMessage(String messageKey, FacesMessage.Severity severity,
			String details) {
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
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String txt = bundle.getString(messageKey);
		return txt;
	}



	public Object getBeanReference(String beanName) {
		FacesContext context = FacesContext.getCurrentInstance();
		Application a = context.getApplication();
		Object o = a.getVariableResolver().resolveVariable(context, beanName);
		return o;
	}


	public CNCCommonService getCncService() {
		return cncService;
	}


	public void setCncService(CNCCommonService cncService) {
		this.cncService = cncService;
	}
	
	public CNCCommonService getCommonService() {
		return this.cncService;
		//return (CNCCommonService) this.getRemoteService("CNCCommonServiceBean", "Servizio_CNC");
	}
	
	public CNCDataIn createDataIn(Object o) {
		CNCDataIn dataIn = new CNCDataIn();
		dataIn.setObj(o);
		dataIn.setEnteId(this.getEnte());
		return dataIn;
	}
	
	public CNCDTO fillEnte(CNCDTO dto){
		
		CeTUser user = getUser();
		if(user != null)
			dto.setEnteId(user.getCurrentEnte());
			dto.setUserId(user.getUsername());
			dto.setSessionId(user.getSessionId());
		return dto;
	
	}

	public it.webred.ct.data.access.basic.cnc.CNCCommonService getCncCommonService() {
		return cncCommonService;
	}

	public void setCncCommonService(
			it.webred.ct.data.access.basic.cnc.CNCCommonService cncCommonService) {
		this.cncCommonService = cncCommonService;
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
