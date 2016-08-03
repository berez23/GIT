package it.webred.ct.service.comma340.web;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.comune.ComuneService;
import it.webred.ct.config.parameters.dto.AmKeyValueDTO;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.service.comma340.data.access.C340CommonService;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.File;
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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class Comma340BaseBean {
	
	protected static Logger logger = Logger.getLogger("C340service.log");
	private static ResourceBundle bundle;
	private static String codNazionaleEnte;
	private C340CommonService c340CommonService;
	private ParameterService paramService;
	private ComuneService comuneService;
	
	protected final String DIR_SERVIZIO = "servizio_c340";
	private final String DIR_PLANIMETRIEC340 = "planimetrieComma340";
	
	public ParameterService getParamService() {
		return paramService;
	}

	public void setParamService(ParameterService paramService) {
		this.paramService = paramService;
	}

	static {
		bundle = ResourceBundle.getBundle("it.webred.ct.service.comma340.web.resources.messages");
	}

	
	public C340CommonService getC340CommonService() {
		return this.c340CommonService;
	}
	
	public void setC340CommonService(C340CommonService c340CommonService) {
		this.c340CommonService = c340CommonService;
	}


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

	
	public CeTBaseObject fillEnte(CeTBaseObject ro){
		
		CeTUser user = getUser();
		if(user != null){
			ro.setEnteId(user.getCurrentEnte());
			ro.setUserId(user.getUsername());
			ro.setSessionId(user.getSessionId());
			
		}
		
		return ro;
	
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
	
	protected String getRootFolderPath() {
		String pathDatiDiogene = getRootPathDatiDiogene();
		String rootPathEnte= pathDatiDiogene + this.getCurrentEnte();
		return rootPathEnte;
	}
	
	
	protected String getPathPlanimetrieC340() {
		String path = getRootFolderPath() + File.separatorChar + DIR_PLANIMETRIEC340 ;
		this.getLogger().info("Percorso Planimetrie C340:" +path);
		return path;
	}
	
	/*protected String getRootPathDatiApplicazione(){
		String path = null;
	
		try{
		  
          ParameterSearchCriteria criteria = new ParameterSearchCriteria();
  			criteria.setKey("dir.files.dati");
  			criteria.setComune(null);
  			criteria.setSection(null);
  		
  			return doGetListaKeyValue(criteria) ;
		}catch(Exception e){
			
		}
		
		return path;
	}
	*/
	
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

	
}

