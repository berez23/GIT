package it.bod.application.common;


import it.bod.application.beans.EnteBean;
import it.persistance.common.SqlHandler;
import it.webred.cet.permission.CeTUser;
import it.webred.ct.config.model.AmKeyValueExt;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.data.access.basic.common.CommonService;
import it.webred.ct.data.model.common.SitEnte;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;
import java.security.Principal;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class MasterClass implements Serializable {
	
	private static Logger logger = Logger.getLogger(MasterClass.class.getName());
	
	private static final long serialVersionUID = -5764761443438639064L;
	protected static String resourcesProp = "/resources.properties";
	protected static String propName = "/queryFiltro.properties";
	
	protected static String logConfName = "/log4j_bod.properties";
	
	protected Principal utente = null;
	protected String nomeApplicazione = "";
	
	protected HttpServletRequest req = null;
	protected HttpServletResponse res = null;
	protected HttpSession jsfSession = null;
	protected String sessionId = "";
	
	private String pathDatiDiogene = null;
	private String pathLoad = null;
	
	private String modalitaCalcoloValoreCommerciale = null;
	
	protected final String dirTemplateBod = "template/bod/";
	protected final String TEMPORARY_FILES = "temporaryFiles";
	protected final String ALLEGATI_SEGNALAZIONI = "allegati_segnalazioni";
	
	protected String pathTemplateBod;
	
	private ApplicationContext appContext;
	
	protected EnteBean ente;
	
	public MasterClass(){
		/*
		 * Configurazione del log4j
		 */		
	
		PropertyConfigurator.configure( SqlHandler.loadProperties(logConfName) );
	}//-------------------------------------------------------------------------

	public void initializer(){
		/*
		 * Imposta la variabile in sessione per il percorso delle immagini
		 */
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext extContext = context.getExternalContext();
		ServletContext servletContext = (ServletContext)extContext.getContext();
		HttpServletRequest req = (HttpServletRequest)extContext.getRequest();
		HttpSession jsfSession = (HttpSession)req.getSession(false);
		WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		jsfSession.setAttribute("ctxName", req.getContextPath());
		sessionId = jsfSession.getId();
		
		/*
		 * Caricare i dati per la combo delle causali
		 */
		Info.htTipiOperazioni.put("C", "COSTITUITA");
		Info.htTipiOperazioni.put("S", "SOPPRESSA");
		Info.htTipiOperazioni.put("V", "VARIATA");
		
		utente = req.getUserPrincipal();
	
		nomeApplicazione = req.getContextPath().substring(1);
		
		ente = this.getEnte();
		
		logger.info(nomeApplicazione);
		

	}//-------------------------------------------------------------------------
	
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Principal getUtente() {
		return utente;
	}

	public void setUtente(Principal utente) {
		this.utente = utente;
	}

	public String getNomeApplicazione() {
		return nomeApplicazione;
	}

	public void setNomeApplicazione(String nomeApplicazione) {
		this.nomeApplicazione = nomeApplicazione;
	}

	public HttpServletRequest getReq() {
		return req;
	}

	public void setReq(HttpServletRequest req) {
		this.req = req;
	}

	public HttpServletResponse getRes() {
		return res;
	}

	public void setRes(HttpServletResponse res) {
		this.res = res;
	}

	public HttpSession getJsfSession() {
		return jsfSession;
	}

	public void setJsfSession(HttpSession jsfSession) {
		this.jsfSession = jsfSession;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	protected String getPathDatiDiogene() {
		
		if(pathDatiDiogene == null || pathDatiDiogene.equals("")){
			ParameterSearchCriteria criteria= new ParameterSearchCriteria();
			criteria.setKey("dir.files.datiDiogene");
						
			ParameterService parameterService= (ParameterService) getEjb("CT_Service", "CT_Config_Manager", "ParameterBaseService");
			
			AmKeyValueExt amKey=parameterService.getAmKeyValueExt(criteria);
			pathDatiDiogene= amKey.getValueConf();
			
			logger.info("PATH DATI DIOGENE: " + pathDatiDiogene);
			
		}
		return pathDatiDiogene;
	}//-------------------------------------------------------------------------



	protected String getModalitaCalcoloValoreCommerciale() {
		
		if(modalitaCalcoloValoreCommerciale == null || modalitaCalcoloValoreCommerciale.equals("")){
			ParameterSearchCriteria criteria= new ParameterSearchCriteria();
			criteria.setKey("docfa.modalita.calcolo.valore.commerciale");
			criteria.setComune(getEnte().getBelfiore());
			
			try{
			Context cont = new InitialContext();
			
			ParameterService parameterService= (ParameterService) getEjb("CT_Service", "CT_Config_Manager", "ParameterBaseService");
			
			AmKeyValueExt amKey=parameterService.getAmKeyValueExt(criteria);
			modalitaCalcoloValoreCommerciale= amKey.getValueConf();
			
			logger.info("MODALITA CALCOLO VALORE COMMERCIALE: " + modalitaCalcoloValoreCommerciale);
			
			}catch (NamingException e){
				modalitaCalcoloValoreCommerciale="";
			}
		}
		return modalitaCalcoloValoreCommerciale;
	}//-------------------------------------------------------------------------



	protected String getPathLoad() {
		
		if(pathLoad == null || pathLoad.equals("")){
			ParameterSearchCriteria criteria= new ParameterSearchCriteria();
			criteria.setKey("dir.files.dati");
			
			try{
			Context cont = new InitialContext();
			
			ParameterService parameterService= (ParameterService)	getEjb("CT_Service","CT_Config_Manager" , "ParameterBaseService");

			
			AmKeyValueExt amKey=parameterService.getAmKeyValueExt(criteria);
			pathLoad= amKey.getValueConf();
			
			logger.info("PATH LOAD: " + pathLoad);
			
			}catch (NamingException e){
				pathLoad="";
			}
		}
		return pathLoad;
	}//-------------------------------------------------------------------------

	public void setEnte(EnteBean ente) {
		this.ente = ente;
	}
	
	public EnteBean getEnte() {
		
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
			HttpSession sessione = request.getSession(false);
			
			CeTUser utente = (CeTUser) sessione.getAttribute("user");
			ente = new EnteBean();
			ente.setBelfiore(utente.getCurrentEnte());
						
			CeTBaseObject cet = new CeTBaseObject();
			cet.setEnteId(ente.getBelfiore());
			cet.setUserId(utente.getUsername());
						
			CommonService common = (CommonService) getEjb("CT_Service", "CT_Service_Data_Access", "CommonServiceBean");
			
			SitEnte sitente = common.getEnte(cet);
			ente.setDescrizione(sitente.getDescrizione());

		return ente;
	}//-------------------------------------------------------------------------

	protected static Object getEjb(String ear, String module, String ejbName){
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
