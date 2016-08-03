package it.webred.cs.jsf.manbean.superc;

import it.webred.amprofiler.model.AmGroup;
import it.webred.cet.permission.CeTUser;
import it.webred.cs.csa.ejb.client.AccessTableConfigurazioneSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.data.model.CsCfgParametri;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.ct.config.model.AmComune;
import it.webred.ct.config.model.AmKeyValueExt;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.comune.ComuneService;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

public class CsUiCompBaseBean {
	
	private String logoBasePath;
	protected String dirLoghi = "/images/logo/";
	
	private StreamedContent logoComune;
	
	public static Logger logger = Logger.getLogger("carsociale.log");
	
	private static ResourceBundle bundle;
	
	static {
		bundle = ResourceBundle.getBundle("it.webred.cs.resources.messages");
	}
	
	protected ComuneService comuneService = (ComuneService) getEjb("CT_Service", "CT_Config_Manager", "ComuneServiceBean");
	
	protected HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}

	public static HttpSession getSession() {
		return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
	}

	protected static HttpServletResponse getResponse() {
		return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	}

	protected void addError( String summary, String descrizione ) {
		addMessage(summary, descrizione, FacesMessage.SEVERITY_ERROR);
	}

	protected void addWarning( String summary, String descrizione ) {
		addMessage(summary, descrizione, FacesMessage.SEVERITY_WARN);
	}

	protected void addInfo( String summary, String descrizione ) {
		addMessage(summary, descrizione, FacesMessage.SEVERITY_INFO);
	}
	
	protected void addMessage( String summary, String descrizione, FacesMessage.Severity severity ) {
		FacesMessage message = new FacesMessage(severity, summary, descrizione != null? descrizione: "" ); 
	    FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public static void addMessageFromProperties(String messageKey, FacesMessage.Severity severity) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String txt = bundle.getString(messageKey);

		facesContext.addMessage(null, new FacesMessage(severity, txt, ""));
	}
	
	public void addInfoFromProperties(String msgKey) {  
		addMessageFromProperties(msgKey, FacesMessage.SEVERITY_INFO);  
    }
	
	public void addWarningFromProperties(String msgKey) {  
		addMessageFromProperties(msgKey, FacesMessage.SEVERITY_WARN);  
    }
	
	public static void addErrorFromProperties(String msgKey) {  
		addMessageFromProperties(msgKey, FacesMessage.SEVERITY_ERROR);  
    }
	
	public static CeTUser getUser() {		
		return (CeTUser) getSession().getAttribute("user");
		/*CeTUser user = new CeTUser();
		user.setUsername("SLVABT234Tf94");
		user.setSessionId("dsf34r3d4f3f");
		user.setPermList(new HashMap<String, String>());
		user.setEnteList(null);
		user.setCurrentEnte("F704");
		return user;*/
	}
	
	public static CeTBaseObject fillEnte(CeTBaseObject ro) {	
	
		CeTUser user = getUser();
		CsOOperatoreSettore opSettore = getCurrentOpSettore();
		if(user != null){
			ro.setEnteId(user.getCurrentEnte());
			ro.setUserId(user.getUsername());
			ro.setSessionId(user.getSessionId());
		}
		if(opSettore != null && opSettore.getCsOSettore().getCsOOrganizzazione().getBelfiore() != null)
			ro.setEnteId(opSettore.getCsOSettore().getCsOOrganizzazione().getBelfiore());
		return ro;
	}

	public UIComponent findComponent(final String id){
	    FacesContext context = FacesContext.getCurrentInstance(); 
	    UIViewRoot root = context.getViewRoot();
	    final UIComponent[] found = new UIComponent[1];
	    root.visitTree(VisitContext.createVisitContext(context), new VisitCallback() {     
	        @Override
	        public VisitResult visit(VisitContext context, UIComponent component) {
	            if(component.getId().equals(id)){
	                found[0] = component;
	                return VisitResult.COMPLETE;
	            }
	            return VisitResult.ACCEPT;              
	        }
	    });
	    return found[0];
	}
	
	public static CsOOperatoreSettore getCurrentOpSettore() {
		return (CsOOperatoreSettore) getSession().getAttribute("operatoresettore");
	}
	
	@SuppressWarnings("unchecked")
	public List<AmGroup> getListaGruppi() {
		return (List<AmGroup>) getSession().getAttribute("gruppi");
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
	
	public static boolean checkPermesso(String item, String permesso) {
		@SuppressWarnings("unchecked")
		HashMap<String, String> map = (HashMap<String, String>) getSession().getAttribute("permessiGruppoSettore");
		if(map != null && map.get(getPatternPermesso(item, permesso)) != null)
			return true;
		
		return false;
	}
	
	public static boolean checkPermesso(String permesso) {
		@SuppressWarnings("unchecked")
		HashMap<String, String> map = (HashMap<String, String>) getSession().getAttribute("permessiGruppoSettore");
		if(map != null && map.get(getPatternPermesso(permesso)) != null)
			return true;
		
		return false;
	}
	
	private static String getPatternPermesso(String nomePermesso) {
		
		// ES. permission@-@CarSociale@-@CarSociale@-@Crea un caso
		String istanza = "CarSociale";
		String item = "CarSociale";
		return "permission@-@"+ istanza +"@-@"+ item +"@-@"+ nomePermesso;
		
	}
	
	@SuppressWarnings("unused")
	private static String getPatternPermesso(String item, String nomePermesso) {
		
		String istanza = "CarSociale";
		return "permission@-@"+ istanza +"@-@"+ item +"@-@"+ nomePermesso;
		
	}
	
	protected String getParametro(String sezione, String chiave) {
		
		try {
			AccessTableConfigurazioneSessionBeanRemote confService = (AccessTableConfigurazioneSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableConfigurazioneSessionBean");
			BaseDTO dto = new BaseDTO();
			fillEnte(dto);
			dto.setObj(sezione);
			dto.setObj2(chiave);
			CsCfgParametri csParam = confService.getParametro(dto);
			
			if(csParam != null)
				return csParam.getValore();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		return null;
		
	}
	
	public String getNomeEnte() {
		AmComune am = comuneService.getComune(getUser().getCurrentEnte());
		String nomeEnte = am != null ? am.getDescrizione() : "";
		nomeEnte = nomeEnte.substring(0, 1).toUpperCase()
				+ nomeEnte.substring(1).toLowerCase();
		return nomeEnte;
	}
	
	protected String getDirDatiDiogene() {
		if(this.logoBasePath==null){
			ParameterService paramService = (ParameterService) getEjb("CT_Service","CT_Config_Manager" , "ParameterBaseService");
		    ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("dir.files.datiDiogene");
			criteria.setComune(null);
			criteria.setSection(null);
			
			AmKeyValueExt amKey = paramService.getAmKeyValueExt(criteria);
			if(amKey != null) 
				 this.logoBasePath =  amKey.getValueConf();
			else 
				logger.error("Attenzione: Il path per il recupero logo del report non è impostato");
		}
		return logoBasePath;
	}
	
	protected String getPathLoghi(){
		BaseDTO baseDto = new BaseDTO();
		fillEnte(baseDto);
		String pathLoghi = null;
		if(baseDto.getEnteId()!=null){
			String dir = this.getDirDatiDiogene();
			if(dir != null) 
				pathLoghi = dir + baseDto.getEnteId() + dirLoghi;
		}
		return pathLoghi;
	}

	public StreamedContent getLogoComune() {
		
		String pathLoghi = this.getPathLoghi();
		if(pathLoghi!=null){
			String slogo = pathLoghi + "logo_intestazione.jpg";
			File logo = new File(slogo);
			if(logo.exists()){
				try {
					this.logoComune = new DefaultStreamedContent(new FileInputStream(logo), "image/jpeg");
				} catch (FileNotFoundException e) {
					logger.error("Errore recupero Logo Comune", e);
					this.logoComune=null;
				}
			}else {
				logger.error("Attenzione: Il logo del comune non è presente ["+slogo+"]");
				this.logoComune=null;
			}
			
		}	
		return logoComune;
	}
	
}
