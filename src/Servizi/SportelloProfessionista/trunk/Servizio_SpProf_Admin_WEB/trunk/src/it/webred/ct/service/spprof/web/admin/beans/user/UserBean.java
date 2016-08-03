package it.webred.ct.service.spprof.web.admin.beans.user;

import it.webred.cet.permission.CeTUser;
import it.webred.cet.permission.GestionePermessi;
import it.webred.ct.config.model.AmComune;
import it.webred.ct.config.model.AmInstance;
import it.webred.ct.config.parameters.application.ApplicationService;
import it.webred.ct.config.parameters.comune.ComuneService;
import it.webred.ct.service.spprof.web.admin.SpProfBaseBean;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;

import org.apache.log4j.Logger;

public class UserBean extends SpProfBaseBean implements Serializable {

	private static final String SPPROF_APPLICATION_AM = "SpProf";
	private static final String SPPROF_ITEM_AM = "SpProf";
	private static final String SPPROF_PERMISSION_AM = "Amministrazione progetti";

	public ComuneService comuneService = (ComuneService) getEjb("CT_Service", "CT_Config_Manager", "ComuneServiceBean");

	public ApplicationService applicationService = (ApplicationService) getEjb("CT_Service", "CT_Config_Manager", "ApplicationServiceBean");;

	private static Logger logger = Logger.getLogger(UserBean.class.getName());

	private String username;
	private String ente;

	private Boolean administrator;

	public void doLogout() {

		getRequest().getSession().invalidate();
		try {
			getResponse().sendRedirect(getRequest().getContextPath() + "/");
		} catch (IOException e) {
			logger.error("Eccezione: " + e.getMessage(), e);
		}

	}

	public String getUsername() {
		if (getRequest().getUserPrincipal() == null
				|| getRequest().getUserPrincipal().getName() == null)
			return "admin";

		username = getRequest().getUserPrincipal().getName();

		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEnte() {
		if (ente == null && getUser() != null) {
			AmComune am = comuneService.getComune(getUser().getCurrentEnte());
			ente = am != null ? am.getDescrizione() : "";
			ente = ente.substring(0, 1).toUpperCase()
					+ ente.substring(1).toLowerCase();
		}

		return ente;
	}

	public void setEnte(String ente) {
		this.ente = ente;
	}

	public void controllaAdmin() {
		CeTUser u = getUser();
		if (u != null) {
			AmInstance ist = applicationService.getInstanceByApplicationComune(
					SPPROF_APPLICATION_AM, u.getCurrentEnte());
			if (ist != null)
				administrator = GestionePermessi.autorizzato(u, ist.getName(),
						SPPROF_ITEM_AM, SPPROF_PERMISSION_AM);
		}
	}

	public Boolean getAdministrator() {
		if(administrator == null)
			controllaAdmin();
		return administrator;
	}

	public void setAdministrator(Boolean administrator) {
		this.administrator = administrator;
	}

}
