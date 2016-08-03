package it.webred.ct.service.segnalazioniqualificate.web.bean.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.config.model.AmComune;
import it.webred.ct.service.segnalazioniqualificate.web.bean.SegnalazioniQualificateBaseBean;

import org.apache.log4j.Logger;

public class UserBean extends SegnalazioniQualificateBaseBean{

	private String username;
	
	private String ente;
	
	private boolean supervisore;
	
	
	public void doLogout(){
		
		getRequest().getSession().invalidate();
		try {
			getResponse().sendRedirect(getRequest().getContextPath() + "/");
		} catch (IOException e) {
			logger.error("Eccezione: "+e.getMessage(),e);
		}
		
	}

	public String getUsername() {
		
		if (getRequest().getUserPrincipal()==null || getRequest().getUserPrincipal().getName()==null)
			return "admin";
		
		username = getRequest().getUserPrincipal().getName();
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEnte() {
		
		String es = getRequest().getParameter("es");
		if(es != null && !es.equals("") && getUser() != null){
			AmComune am = comuneService.getComune(getUser().getCurrentEnte());
			ente = am != null? am.getDescrizione(): "";
			ente = ente.substring(0,1).toUpperCase() + ente.substring(1).toLowerCase();
		}
		return ente;
	}

	public void setEnte(String ente) {
		this.ente = ente;
	}

	public void setSupervisore(boolean supervisore) {
		this.supervisore = supervisore;
	}
	
	public boolean isSupervisore() {
		CeTUser user= super.getUser();
		supervisore = false;
		
		HashMap<String,String> permessi = user.getPermList();
		
		String permesso = "permission@-@AgendaSegnalazioni@-@Agenda Segnalazioni@-@Supervisione";
		if(permessi.get(permesso)!=null){
			supervisore = true;
			logger.info("L'utente " + user.getUsername() + " è supervisore.");
		}else
			logger.info("L'utente " + user.getUsername() + " non è supervisore.");
		
		return supervisore;
	}
}
