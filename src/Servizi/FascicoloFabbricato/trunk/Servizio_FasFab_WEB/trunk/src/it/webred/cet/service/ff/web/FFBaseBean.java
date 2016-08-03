package it.webred.cet.service.ff.web;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.config.model.AmKeyValueExt;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.comune.ComuneService;
import it.webred.ct.config.parameters.dto.AmKeyValueDTO;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.service.ff.data.access.common.FFCommonService;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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

public class FFBaseBean implements Serializable  {

	private static ResourceBundle bundle;
	private ComuneService comuneService;
	
	static {
		bundle = ResourceBundle
				.getBundle("it.webred.cet.service.ff.web.resources.messages");
	}

	protected static Logger logger = Logger.getLogger("ff_log");

	protected Logger getLogger() {
		return this.logger;
	}
	
	public String getEnte() {
		CeTUser user = this.getCeTUser();
		if (user != null) {
			return user.getCurrentEnte();
		}
		
		return null;
	}
	
	public CeTUser getCeTUser() {
		HttpSession session = this.getSession();
		return  (CeTUser) session.getAttribute("user");
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


	public String getUsername() {
		HttpSession session = this.getSession();
		CeTUser user = (CeTUser) session.getAttribute("user");
		if (user != null) {
			return user.getUsername();
		}
		
		return null;

	}
	
	public Map<String, String> getGlobalConfig() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ServletContext ctx = (ServletContext) facesContext.getExternalContext().getContext();
		return (Map<String, String>) ctx.getAttribute("ff.paramConfig");
	}
	
	public String getHostFtpScambioPortale(ParameterService parameterService) {
		
		String value = null;
		try
		{
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("ftp.host.scambio.portale");
			criteria.setComune(getEnte());
	
			it.webred.ct.config.model.AmKeyValueExt param = parameterService.getAmKeyValueExt(criteria);
			value= param.getValueConf();
		}
		catch(Exception e) {
			logger.error("Eccezione config: "+e.getMessage(),e);
		}
		return value;
	}
	
	public String getDirFtpScambioPortale(ParameterService parameterService) {
		
		String value = null;
		try
		{
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("ftp.pathff.scambio.portale");
			criteria.setComune(getEnte());
	
			it.webred.ct.config.model.AmKeyValueExt param = parameterService.getAmKeyValueExt(criteria);
			value= param.getValueConf() + "/export";
		}
		catch(Exception e) {
			logger.error("Eccezione config: "+e.getMessage(),e);
		}
		return value;
	}
	
	public String getUserFtpScambioPortale(ParameterService parameterService) {
		
		String value = null;
		try
		{
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("ftp.user.scambio.portale");
			criteria.setComune(getEnte());
	
			it.webred.ct.config.model.AmKeyValueExt param = parameterService.getAmKeyValueExt(criteria);
			value= param.getValueConf();
		}
		catch(Exception e) {
			System.out.println("Eccezione config: "+e.getMessage());
		}
		return value;
	}
	
	public String getPwdFtpScambioPortale(ParameterService parameterService) {
		
		String value = null;
		try
		{
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("ftp.pwd.scambio.portale");
			criteria.setComune(getEnte());
	
			it.webred.ct.config.model.AmKeyValueExt param = parameterService.getAmKeyValueExt(criteria);
			value= param.getValueConf();
		}
		catch(Exception e) {
			logger.error("Eccezione config: "+e.getMessage(),e);
		}
		return value;
	}
	

	
	public String getDirFilesDati(ParameterService parameterService) {
		return this.getPathDatiDiogene(parameterService);
	}
	
	
	private String getPathDatiDiogene(ParameterService parameterService) {
		
		ParameterSearchCriteria criteria = new ParameterSearchCriteria();
		criteria.setKey("dir.files.datiDiogene");
		criteria.setComune(null);
		criteria.setSection(null);
		
		it.webred.ct.config.model.AmKeyValueExt param = parameterService.getAmKeyValueExt(criteria);
		
		String path = param.getValueConf();
		
		logger.debug("getPathDatiDiogene "+ path);
		
		return path;
	
	}
	
	public String getPathDatiDiogeneEnte(ParameterService parameterService) {
		
		String datiDiogene = this.getPathDatiDiogene(parameterService);
		String path = datiDiogene + getEnte().trim();
		
		logger.debug("getPathDatiDiogeneEnte "+ path);
		
		return path;
	
	}
	
	public String getPathDirEnteFascicoloFabbricato(ParameterService parameterService){
		 String pathDirFascFabb =  this.getPathDatiDiogeneEnte(parameterService) + File.separatorChar +  this.getGlobalConfig().get("dirFascicoloFabbricato");
		 return pathDirFascFabb;
	}
	
	public String getPathDirFascicoloFabbricato(ParameterService parameterService){
		 String pathDirFascFabb =  this.getPathDatiDiogene(parameterService) + File.separatorChar +  this.getGlobalConfig().get("dirFascicoloFabbricato");
		 return pathDirFascFabb;
	}
	
	public String getMailServer(ParameterService parameterService) {
		
		if (getEnte() != null){
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("mail.server");
			criteria.setComune(getEnte());
			criteria.setSection(null);
			
			it.webred.ct.config.model.AmKeyValueExt param = parameterService.getAmKeyValueExt(criteria);
			if(param != null)
				return param.getValueConf();
		}

		return "";
	}
	
	public String getPortMailServer(ParameterService parameterService) {
		
		if (getEnte() != null)
		{
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("mail.server.port");
			criteria.setComune(getEnte());
			criteria.setSection(null);
			
			it.webred.ct.config.model.AmKeyValueExt param = parameterService.getAmKeyValueExt(criteria);
			if(param != null)
				return param.getValueConf();
		}

		return "";
	}
	
	public String getEmailFrom(ParameterService parameterService) {
		
		if (getEnte() != null)
		{		
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("email.fascicoloF");
			criteria.setComune(getEnte());
			criteria.setSection("param.fascicoloF");
			
			List<AmKeyValueDTO> Lparam = parameterService.getListaKeyValue(criteria);
			if(Lparam != null && Lparam.size() > 0)
				return Lparam.get(0).getAmKeyValueExt().getValueConf();
		}
		
		return "";
	}
	
	public void setComuneService(ComuneService comuneService) {
		this.comuneService = comuneService;
	}

	public ComuneService getComuneService() {
		return comuneService;
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
