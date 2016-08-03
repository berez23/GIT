package it.webred.fb.web.bean.util;

import it.webred.fb.web.bean.FascicoloBeneBaseBean;
import it.webred.fb.web.manbean.BeneSearchBean;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class NavigationBean extends FascicoloBeneBaseBean {
	
	public void goHome() {
		getSession().setAttribute("navigationHistory", "");
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("home.faces");
		} catch (IOException e) {
			addError("Errore", "Errore durante il reindirizzamento");
		}
	}
	
	public void goGestoreFlussi() {
		getSession().setAttribute("navigationHistory", "gestoreFlussi");
		
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("caricatoreDati.faces");
		} catch (IOException e) {
			addError("Errore", "Errore durante il reindirizzamento");
		}
	}
	
	
	public void goDettaglioBene() {
		getSession().setAttribute("navigationHistory", "dettaglioBene");
		
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("dettaglioBene.faces");
		} catch (IOException e) {
			addError("Errore", "Errore durante il reindirizzamento");
		}
	}
	
	public void goRicercaBene() {
		getSession().setAttribute("navigationHistory", "ricercaBene");
		
		BeneSearchBean bsb = (BeneSearchBean) this.getBeanReference("beneSearchBean");
		bsb.initRicerca();
		
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("listaBeni.faces");
		} catch (IOException e) {
			addError("Errore", "Errore durante il reindirizzamento");
		}
	}
	
	public void goBackListaRicercaBene() {
		getSession().setAttribute("navigationHistory", "ricercaBene");

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("listaBeni.faces");
		} catch (IOException e) {
			addError("Errore", "Errore durante il reindirizzamento");
		}
	}
	
	public boolean isAutorizzatoGestioneFlussi() {
		return this.checkPermesso(this.PERM_GESTIONE_FLUSSI);
	}
	
	public boolean isAutorizzatoUploadDocumenti() {
		return this.checkPermesso(this.PERM_UPLOAD_DOCS);
	}
	
	private String getNavigationHistory() {
		String nav = (String) getSession().getAttribute("navigationHistory");
		if(nav == null)
			return "";
		return nav;
	}
}
