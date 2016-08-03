package it.webred.ct.service.tares.web.bean.util;

import it.webred.ct.service.tares.web.bean.TaresBaseBean;

import java.io.IOException;

import org.apache.log4j.Logger;

public class UserBean extends TaresBaseBean{

	private String username;
	private String ente;
	
	public String doLogout(){
		
		getRequest().getSession().invalidate();
		try {
			getResponse().sendRedirect(getRequest().getContextPath() + "/");
		} catch (IOException e) {
			logger.error("Eccezione: "+e.getMessage(),e);
		}
		
		return "";
		
	}

	public String getUsername() {
		
		if (getRequest().getUserPrincipal()==null || getRequest().getUserPrincipal().getName()==null)
			return "Mario Rossi";
		
		username = getRequest().getUserPrincipal().getName();
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEnte() {
		return null;
	}
	
}
