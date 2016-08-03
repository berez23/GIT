package it.webred.ct.service.comma340.web;

import it.webred.ct.data.access.basic.common.CommonService;
import it.webred.ct.service.comma340.data.access.C340CommonService;
import it.webred.ct.service.comma340.data.access.C340GestionePraticheService;
import it.webred.ct.service.comma340.web.common.ServiceLocator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class Comma340BaseBean {

	private static ResourceBundle bundle;
	private static String codNazionaleEnte;
	private CommonService commonService;
	private C340CommonService c340CommonService;
	

	static {
		bundle = ResourceBundle.getBundle("it.webred.ct.service.comma340.web.resources.messages");
	}

	private static Logger logger = Logger.getLogger("C340service_log");
	
	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public CommonService getCommonService() {
		return this.commonService;
		//return (CommonService)getCTRemoteService("CommonServiceBean");
	}

	public C340CommonService getC340CommonService() {
		return this.c340CommonService;
		//return (C340CommonService) this.getRemoteService("C340CommonServiceBean", "Servizio_C340");
	}
	
	public void setC340CommonService(C340CommonService c340CommonService) {
		this.c340CommonService = c340CommonService;
	}

	
	public String getCodNazionaleFromUser() {		
		
		if(codNazionaleEnte == null || codNazionaleEnte.isEmpty()){
			String msg = "codNazionaleEnte";
			try{
				codNazionaleEnte = getCommonService().getEnte().getCodent();
			}catch(Throwable t) {
				addErrorMessage(msg+".error", t.getMessage());
				t.printStackTrace();
			}
		}
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
		return this.logger;
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

	
	
}

