package it.webred.gitland.web.bean;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.config.model.AmGroup;
import it.webred.ct.config.model.AmKeyValueExt;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;
import it.webred.ct.support.datarouter.CeTBaseObject;
import it.webred.ejb.utility.ClientUtility;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

public class ComponentBaseBean {

	public static Logger logger = Logger.getLogger("gitland.log");

	private static ResourceBundle bundle;

	static {
		bundle = ResourceBundle
				.getBundle("it.webred.gitland.web.resources.messages");
	}
	private String logoBasePath;

	protected HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
	}

	public static HttpSession getSession() {
		return (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
	}

	protected HttpServletResponse getResponse() {
		return (HttpServletResponse) FacesContext.getCurrentInstance()
				.getExternalContext().getResponse();
	}

	protected void addError(String summary, String descrizione) {
		addMessage(summary, descrizione, FacesMessage.SEVERITY_ERROR);
	}

	protected void addWarning(String summary, String descrizione) {
		addMessage(summary, descrizione, FacesMessage.SEVERITY_WARN);
	}

	protected void addInfo(String summary, String descrizione) {
		addMessage(summary, descrizione, FacesMessage.SEVERITY_INFO);
	}

	protected void addMessage(String summary, String descrizione,
			FacesMessage.Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary,
				descrizione != null ? descrizione : "");
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public static void addMessageFromProperties(String messageKey,
			FacesMessage.Severity severity) {
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
	}

	public static CeTBaseObject fillUserData(CeTBaseObject ro) {
		CeTUser user = getUser();
		if (user != null) {
			ro.setEnteId(user.getCurrentEnte());
			ro.setUserId(user.getUsername());
			ro.setSessionId(user.getSessionId());
		}
		return ro;
	}

	@SuppressWarnings("unchecked")
	public List<AmGroup> getListaGruppi() {
		return (List<AmGroup>) getSession().getAttribute("gruppi");
	}

	public Object getEjb(String ear, String module, String ejbName) {
		Context cont;
		try {
			cont = new InitialContext();
			return cont.lookup("java:global/" + ear + "/" + module + "/"
					+ ejbName);
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public UIComponent findComponent(final String id) {
		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();
		final UIComponent[] found = new UIComponent[1];
		root.visitTree(VisitContext.createVisitContext(context),
				new VisitCallback() {
					@Override
					public VisitResult visit(VisitContext context,
							UIComponent component) {
						if (component.getId().equals(id)) {
							found[0] = component;
							return VisitResult.COMPLETE;
						}
						return VisitResult.ACCEPT;
					}
				});
		return found[0];
	}

	public static boolean checkPermesso(String permesso) {
		@SuppressWarnings("unchecked")
		HashMap<String, String> map = (HashMap<String, String>) getSession()
				.getAttribute("permessiGruppoSettore");
		if (map != null && map.get(getPatternPermesso(permesso)) != null)
			return true;

		return false;
	}

	private static String getPatternPermesso(String nomePermesso) {

		// ES. permission@-@CarSociale@-@CarSociale@-@Crea un caso
		String istanza = "GitLand";
		String item = "GitLand";
		return "permission@-@" + istanza + "@-@" + item + "@-@" + nomePermesso;

	}

	@SuppressWarnings("unused")
	private String getPatternPermesso(String item, String nomePermesso) {

		String istanza = "GitLand";
		return "permission@-@" + istanza + "@-@" + item + "@-@" + nomePermesso;

	}

	protected String getDirDatiDiogene() {
		if (this.logoBasePath == null) {
			ParameterSearchCriteria criteria = new ParameterSearchCriteria();
			criteria.setKey("dir.files.datiDiogene");
			criteria.setComune(null);
			criteria.setSection(null);
			this.logoBasePath = getParametro(criteria);
			if (this.logoBasePath == null)
				logger.error("Attenzione: Il path per il recupero logo del report non è impostato");
		}

		return logoBasePath;
	}

	private String getParametro(ParameterSearchCriteria criteria) {
		ParameterService paramService;
		try {
			paramService = (ParameterService) ClientUtility.getEjbInterface("CT_Service", "CT_Config_Manager", "ParameterBaseService");
			AmKeyValueExt amKey = paramService.getAmKeyValueExt(criteria);
			if (amKey != null)
				return amKey.getValueConf();
			else
				return null;
		} catch (NamingException e) {
			logger.error(e);
		}
		return null;
	}
	public double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}	
}
