package it.webred.ct.service.tsSoggiorno.web.bean;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.service.gestioneDBDoc.data.access.DocumentoService;
import it.webred.ct.service.tsSoggiorno.data.access.AnagraficaService;
import it.webred.ct.service.tsSoggiorno.data.access.DichiarazioneService;
import it.webred.ct.support.datarouter.CeTBaseObject;

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

public class TsSoggiornoBaseBean {
	
	protected static Logger logger = Logger.getLogger("tssoggiorno.log");
	
	private AnagraficaService anagraficaService;
	
	private DichiarazioneService dichiarazioneService;
	private DocumentoService documentoService;
	
	
	protected boolean showMessage=true;
	
	private static ResourceBundle bundle;
	
	static {
		bundle = ResourceBundle.getBundle("it.webred.ct.service.tsSoggiorno.web.resources.messages");
		
	}
	
	protected DocumentoService  getDocumentoService() {
		if(documentoService==null){
			documentoService = (DocumentoService) getEjb("GestioneDBDoc", "GestioneDBDoc_EJB", "DocumentoServiceBean");
		}
		return documentoService;
	}
	
	protected DichiarazioneService  getDichiarazioneService(){
		if(dichiarazioneService==null){
			dichiarazioneService = (DichiarazioneService) getEjb("Servizio_TsSoggiorno", "Servizio_TsSoggiorno_EJB", "DichiarazioneServiceBean");
		}
		return dichiarazioneService;
	}
	
	protected AnagraficaService  getAnagraficaService() {
		if(anagraficaService == null){
			anagraficaService = (AnagraficaService) getEjb("Servizio_TsSoggiorno", "Servizio_TsSoggiorno_EJB", "AnagraficaServiceBean");
		}
		return anagraficaService;
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
	
	public void addWarningMessage(String messageKey) {
		addMessage(messageKey, FacesMessage.SEVERITY_WARN, null);
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
	
	protected Logger getLogger() {
		return logger;
	}
	
	public CeTUser getUser(){
		
		return (CeTUser) getSession().getAttribute("user");
	}
	
	public CeTBaseObject fillEnte(CeTBaseObject ro){
		
		CeTUser user = getUser();
		//TEST
		if(user == null){
			ro.setEnteId("F704");
			ro.setUserId("Test");
		}
		//TEST
		if(user != null){
			ro.setEnteId(user.getCurrentEnte());
			ro.setUserId(user.getUsername());
			ro.setSessionId(user.getSessionId());
			
		}
		
		return ro;
	
	}

	public boolean isShowMessage() {
		return showMessage;
	}

	public void setShowMessage(boolean showMessage) {
		this.showMessage = showMessage;
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
