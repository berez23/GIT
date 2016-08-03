package it.webred.ct.service.tsSoggiorno.web.bean.util;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsSoggetto;
import it.webred.ct.service.tsSoggiorno.web.bean.TsSoggiornoBaseBean;

import org.apache.log4j.Logger;

public class UserBean extends TsSoggiornoBaseBean{

	private String username;
	private String nomeCognome;
	private String ente;
	
	public String doLogout(){
		
		getRequest().getSession().invalidate();
		
		try {
			getResponse().sendRedirect(getRequest().getContextPath() + "/");
			HttpSession ses = getRequest().getSession(true);
			ses.setAttribute("user", null);
		} catch (IOException e) {
			getLogger().error("Eccezione: "+e.getMessage(),e);
		}
		
		return "";
		
	}

	public String getUsername() {
		
		//TODO gestire utenti
		if (getRequest().getUserPrincipal()==null || getRequest().getUserPrincipal().getName()==null)
			return "RSSMRO123TGAAA12";
		
		username = getRequest().getUserPrincipal().getName();
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setNomeCognome(String nomeCognome) {
		this.nomeCognome = nomeCognome;
	}

	public String getEnte() {
		return null;
	}
	
}
