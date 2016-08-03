package it.webred.cs.csa.web.bean.util;

import java.io.IOException;

import it.webred.cs.data.DataModelCostanti;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

@ManagedBean
@ViewScoped
public class NavigationBean extends CsUiCompBaseBean {
	
	public void goHome() {
		getSession().setAttribute("navigationHistory", "");
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("home.faces");
		} catch (IOException e) {
			addError("Errore", "Errore durante il reindirizzamento");
		}
	}
	
	public void goListaCasi() {
		getSession().setAttribute("navigationHistory", "listaCasi");
		
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("listaCasi.faces");
		} catch (IOException e) {
			addError("Errore", "Errore durante il reindirizzamento");
		}
	}
	
	public boolean isListaCasiVisible() {
		return getNavigationHistory().contains("listaCasi");
	}
	
	public void goSchedeSegretariato() {
		getSession().setAttribute("navigationHistory", "listaSchedeSegretariato");

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("listaSchedeSegretariato.faces");
		} catch (IOException e) {
			addError("Errore", "Errore durante il reindirizzamento");
		}
	}
	
	public boolean isSchedeSegretariatoVisible() {
		return getNavigationHistory().contains("listaSchedeSegretariato");
	}
	
	public void goConfigurazione() {
		getSession().setAttribute("navigationHistory", "configurazione");
		
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("configurazione.faces");
		} catch (IOException e) {
			addError("Errore", "Errore durante il reindirizzamento");
		}
	}
	
	public void goSegrSociale() {		
		try {
			String serverName = getRequest().getServerName();
			Integer serverPort = getRequest().getServerPort();
			String urlSegrSociale = "http://" + serverName;
			if(serverPort != null)
				urlSegrSociale += ":" + serverPort;
			urlSegrSociale += "/SegretariatoSoc_WEB";
			FacesContext.getCurrentInstance().getExternalContext().redirect(urlSegrSociale);
		} catch (Exception e) {
			addError("Errore", "Errore durante il reindirizzamento");
		}
	}
	
	public boolean isConfigurazioneVisible() {
		return getNavigationHistory().contains("configurazione");
	}
	
	public boolean isCasoVisible() {
		String currentUrl = getRequest().getRequestURL().toString();
		if(currentUrl.contains("scheda.faces") || currentUrl.contains("fascicoloCartellaUtente.faces"))
			return true;
		else return false;
	}
	
	public boolean isDocIndividualiVisible() {
		String currentUrl = getRequest().getRequestURL().toString();
		if(currentUrl.contains("docIndividuali.faces"))
			return true;
		else return false;
	}
	
	public boolean isAutorizzatoConfigurazione() {
		return CsUiCompBaseBean.checkPermesso(DataModelCostanti.PermessiGenerali.AMMINISTRAZIONE_ORG_SETT_OP);
	}
	
	private String getNavigationHistory() {
		String nav = (String) getSession().getAttribute("navigationHistory");
		if(nav == null)
			return "";
		return nav;
	}
	
}
