package it.webred.ct.service.segnalazioniqualificate.web.bean;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.comune.ComuneService;
import it.webred.ct.config.parameters.dto.AmKeyValueDTO;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.segnalazionequalificata.SegnalazioneQualificataService;
import it.webred.ct.data.access.basic.segnalazionequalificata.SegnalazioniDataIn;
import it.webred.ct.data.access.basic.segnalazionequalificata.dto.RicercaPraticaSegnalazioneDTO;
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

public class SegnalazioniQualificateBaseBean {
	
	protected static Logger logger = Logger.getLogger("agendasegnalazioni.log");
	
	private static ResourceBundle bundle;
	
	public ComuneService comuneService = (ComuneService) getEjb("CT_Service", "CT_Config_Manager", "ComuneServiceBean");
	
	public CatastoService catastoService = (CatastoService) getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");
	
	public SegnalazioneQualificataService segnalazioneService = (SegnalazioneQualificataService) getEjb("CT_Service", "CT_Service_Data_Access", "SegnalazioneQualificataServiceBean");
	
	private ParameterService paramService = (ParameterService) getEjb("CT_Service", "CT_Config_Manager", "ParameterBaseService");

	protected static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	protected static final SimpleDateFormat sdfHHmmss = new SimpleDateFormat("dd/MM/yyyy");
	
	static {
		bundle = ResourceBundle.getBundle("it.webred.ct.service.segnalazioniqualificate.web.resources.messages");
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
	
	protected String getSiatelWebSite(){
		
		String s = null;
		
		try{
		  
          ParameterSearchCriteria criteria = new ParameterSearchCriteria();
  			criteria.setKey("web.site.siatel");
  			criteria.setComune(null);
  			criteria.setSection(null);
  			
  			s = doGetListaKeyValue(criteria) ;
  			
		}catch(Exception e){
			
		}
		
		return s;
		
	}
	
	protected String getRootPathDatiDiogene(){
		String path = null;
	
		try{
		  
          ParameterSearchCriteria criteria = new ParameterSearchCriteria();
  			criteria.setKey("dir.files.datiDiogene");
  			criteria.setComune(null);
  			criteria.setSection(null);
  		
  			path =  doGetListaKeyValue(criteria) ;
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
	
	public String getCurrentEnte(){
		CeTUser user = getUser();
		if(user != null)
			return user.getCurrentEnte();
		else 
			return null;
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
	
	public RicercaPraticaSegnalazioneDTO addOperatore(RicercaPraticaSegnalazioneDTO ro){
		CeTUser user = getUser();
		if(user != null)
			ro.addCodOperatore(user.getUsername());
		
		return ro;
	
	}
	
	public String getCurrentUsernameUtente(){
		return this.getUser().getUsername();
	}
	
	protected Date getCurrentDate(){

		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}
	
	protected int getCurrentYear(){
		Date date = this.getCurrentDate();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy");
		String d = sf.format(date);
		return Integer.parseInt(d);
	}
	
    protected String getTime(){
		
		Date date = this.getCurrentDate();
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return sf.format(date);
	}
	
	protected String getTimeStamp(){
		
		Date date = this.getCurrentDate();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sf.format(date);
	}
	
	protected SegnalazioniDataIn getInitRicercaParams(){
		RicercaPraticaSegnalazioneDTO ricercaPratica = new RicercaPraticaSegnalazioneDTO(this.getCurrentUsernameUtente());
		fillEnte(ricercaPratica);
		SegnalazioniDataIn dataIn = new SegnalazioniDataIn();
		fillEnte(dataIn);
		dataIn.setRicercaPratica(ricercaPratica);
		return dataIn;
	}
	
	protected String getDescEnte(String codEnte){
		String descEnte = "";
		if(codEnte!=null){
			RicercaOggettoCatDTO ricercaCatasto = new RicercaOggettoCatDTO();
			fillEnte(ricercaCatasto);
			ricercaCatasto.setCodEnte(codEnte);
			descEnte = catastoService.getSitiComu(ricercaCatasto).getNome();
		}
		return descEnte;
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
