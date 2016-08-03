package it.webred.cet.service.ff.web;

import it.webred.cet.permission.CeTUser;
import it.webred.cet.service.ff.web.util.PermessiHandler;
import it.webred.ct.config.model.AmComune;

import java.io.IOException;
import java.io.Serializable;

public class UserBean extends FFBaseBean  implements Serializable {

	private String ente; 
	
	public CeTUser getUser()  {
		if (getRequest().getSession().getAttribute("user") == null)
			return null;
		CeTUser user = (CeTUser)getRequest().getSession().getAttribute("user");
		return user;
	}
	
	public String getEnte() {
		
		String es = getRequest().getParameter("es");
		if(es != null && !es.equals("") && getUser() != null){
			AmComune am = super.getComuneService().getComune(getUser().getCurrentEnte());
			ente = am != null? am.getDescrizione(): "";
			ente = ente.substring(0,1).toUpperCase() + ente.substring(1).toLowerCase();
		}
		return ente;
	}

	public void setEnte(String ente) {
		this.ente = ente;
	}
	
	public boolean isInserimetoRichiesta() {
		
		return PermessiHandler.controlla(this.getUser(), PermessiHandler.PERMESSO_INSERIMENTO_RICHIESTE_FASCICOLO);
	}
	
	public boolean isCreazioneCartella() {
		return PermessiHandler.controlla(this.getUser(), PermessiHandler.PERMESSO_GESTIONE_RICHIESTE_FASCICOLO);
	}	
	

}
