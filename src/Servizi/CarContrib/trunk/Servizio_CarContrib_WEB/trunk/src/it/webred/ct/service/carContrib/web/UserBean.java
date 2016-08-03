package it.webred.ct.service.carContrib.web;

import it.webred.cet.permission.CeTUser;
import it.webred.ct.config.model.AmComune;
import it.webred.ct.config.parameters.comune.ComuneService;
import it.webred.ct.service.carContrib.web.utils.Permessi;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class UserBean {

	private boolean inserimetoRichiesta;
	private boolean creazioneCartella;
	
	protected Logger logger = Logger.getLogger("carcontrib.log");
	
	protected ComuneService comuneService = (ComuneService) Utility.getEjb("CT_Service", "CT_Config_Manager", "ComuneServiceBean");

	private String username = "admin";
	private String ente;
	
	/*
	public void doLogout(){
		
		getRequest().getSession().invalidate();
		try {
			getResponse().sendRedirect(getRequest().getContextPath() + "/");
		} catch (IOException e) {
			logger.error("Eccezione: "+e.getMessage(),e);
		}
		
	}
	*/
	
	public String doLogout() {
		try {
			getRequest().getSession().invalidate();
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
        
        return "goLogout";
      }
	

	public String getUsername() {

		username = getRequest().getUserPrincipal().getName();
		return username;
	}
	
	public CeTUser getUser()  {
		if (getRequest().getSession().getAttribute("user") == null)
			return null;
		CeTUser user = (CeTUser)getRequest().getSession().getAttribute("user");
		return user;
	}
	
	public String getEnteID()  {
		String enteID=null;
		if (getRequest().getSession().getAttribute("user") == null)
			return null;
		CeTUser user = (CeTUser)getRequest().getSession().getAttribute("user");
		if (user != null ) {
				enteID= user.getCurrentEnte();
		}
		return enteID;
		
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
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
	}
	
	public HttpServletResponse getResponse() {
		return (HttpServletResponse) FacesContext.getCurrentInstance()
				.getExternalContext().getResponse();
	}

	

	public boolean isInserimetoRichiesta() {
		
		return Permessi.controlla(this.getUser(), Permessi.PERMESSO_INSERIMENTO_RICHIESTE_CARTELLA);
	}
	
	public boolean isCreazioneCartella() {
		return Permessi.controlla(this.getUser(), Permessi.PERMESSO_GESTIONE_RICHIESTE_CARTELLA);
	}

}
