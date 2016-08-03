package it.webred.gitland.web.bean.util;

import it.webred.gitland.web.bean.GitLandBaseBean;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class NavigationBean extends GitLandBaseBean {
	
	public void goHome() {
		getSession().setAttribute("navigationHistory", "");
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("home.faces");
		} catch (IOException e) {
			addError("Errore", "Errore durante il reindirizzamento");
		}
	}//-------------------------------------------------------------------------
	
	public void goLottiLst() {
		getSession().setAttribute("navigationHistory", "lottiLst");
		
		try {
			
			
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("lottiLst.faces");
		} catch (IOException e) {
			addError("Errore", "Errore durante il reindirizzamento");
		}
	}//-------------------------------------------------------------------------

	
	public void goDettaglioBene() {
		getSession().setAttribute("navigationHistory", "dettaglioBene");
		
		try {
			
			
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("dettaglioBene.faces");
		} catch (IOException e) {
			addError("Errore", "Errore durante il reindirizzamento");
		}
	}//-------------------------------------------------------------------------
	
	public void goRicercaLotti() {
		getSession().setAttribute("navigationHistory", "ricercaLotti");
		
		//BeneSearchBean bsb = (BeneSearchBean) this.getBeanReference("beneSearchBean");
		//bsb.initRicerca();
		
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("lottiLst.faces");
		} catch (IOException e) {
			addError("Errore", "Errore durante il reindirizzamento");
		}
	}//-------------------------------------------------------------------------
	
	public void goBackListaRicercaLotti() {
		getSession().setAttribute("navigationHistory", "ricercaLotti");

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("lottiLst.faces");
		} catch (IOException e) {
			addError("Errore", "Errore durante il reindirizzamento");
		}
	}//-------------------------------------------------------------------------
	
	public void goAziendeLst() {
		getSession().setAttribute("navigationHistory", "aziendeLst");
		
		try {
			
//			AziendeBean ab =  (AziendeBean)this.getBeanReference("aziendeBean");
//			ab.init(true);
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("aziendeLst.faces");
		} catch (IOException e) {
			addError("Errore", "Errore durante il reindirizzamento");
		}
	}//-------------------------------------------------------------------------
	
	public void goAziendaNew() {
		getSession().setAttribute("navigationHistory", "aziendaNew");
		
		try {
			
		
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("aziendaNew.faces");
		} catch (IOException e) {
			addError("Errore", "Errore durante il reindirizzamento");
		}
	}//-------------------------------------------------------------------------
	
	public void goAziendaMod() {
		getSession().setAttribute("navigationHistory", "aziendaMod");
		
		try {
			
			
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("aziendaMod.faces");
		} catch (IOException e) {
			addError("Errore", "Errore durante il reindirizzamento");
		}
	}//-------------------------------------------------------------------------
	
	public void goEmpty() {
		getSession().setAttribute("navigationHistory", "empty");
		
		try {
			
			
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("empty.faces");
		} catch (IOException e) {
			addError("Errore", "Errore durante il reindirizzamento");
		}
	}//-------------------------------------------------------------------------
	
	private String getNavigationHistory() {
		String nav = (String) getSession().getAttribute("navigationHistory");
		if(nav == null)
			return "";
		return nav;
	}//-------------------------------------------------------------------------
}
