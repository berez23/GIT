package it.webred.ct.service.cnc.web.common;

import java.io.IOException;

import javax.ejb.EJB;

import it.webred.ct.config.model.AmComune;
import it.webred.ct.config.parameters.comune.ComuneService;
import it.webred.ct.data.access.basic.cnc.CNCDTO;
import it.webred.ct.service.cnc.web.CNCBaseBean;

import org.apache.log4j.Logger;

public class UserBean extends CNCBaseBean{

	private String username;
	
	private String ente;
	
	protected ComuneService comuneService = (ComuneService) getEjb(
			"CT_Service", "CT_Config_Manager", "ComuneServiceBean");
	
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
		
		/*TODO TEST*/
		CNCDTO dto = new CNCDTO();
		dto.setEnteId("F704");
		getCncService().getAmbitoDescr(dto);
		
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
	
}
