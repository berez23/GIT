package it.webred.ct.service.spprof.web.user.bean.util;

import it.webred.ct.config.model.AmComune;
import it.webred.ct.config.parameters.comune.ComuneService;
import it.webred.ct.service.spprof.data.access.SpProfAnagService;
import it.webred.ct.service.spprof.data.access.dto.SoggettoSearchCriteria;
import it.webred.ct.service.spprof.data.model.SSpSoggetto;
import it.webred.ct.service.spprof.web.user.SpProfBaseBean;
import it.webred.ct.service.spprof.web.user.bean.interventi.DatiBean;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;

import it.webred.cet.permission.CeTUser;
import it.webred.cet.permission.GestionePermessi;
import it.webred.ct.config.model.AmInstance;
import it.webred.ct.config.parameters.application.ApplicationService;

import org.apache.log4j.Logger;

public class UserBean extends SpProfBaseBean {

	public static final String NOME_PROPERTIES = "datarouter.properties";
	public static final String NOME_PATH_JBOSS_CONF = "jboss.server.config.dir";

	private static final String SPPROF_APPLICATION_AM = "SpProf";
	private static final String SPPROF_ITEM_AM = "SpProf";
	private static final String SPPROF_PERMISSION_AM = "Amministrazione progetti";
	
	public ComuneService comuneService = (ComuneService) getEjb("CT_Service", "CT_Config_Manager", "ComuneServiceBean");

	protected SpProfAnagService spProfAnagService = (SpProfAnagService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfAnagServiceBean");
	
	public ApplicationService applicationService = (ApplicationService) getEjb("CT_Service", "CT_Config_Manager", "ApplicationServiceBean");

	private String username;

	private String nomeEnte;

	private String datasource;

	private Boolean autorizzato;
	
	private Boolean administrator;

	private Long idSogg;
	
	private String instance;

	public void doLogout() {
		
		autorizzato = null;
		administrator = null;
		idSogg = null;
		username = null;
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

	public String getNomeEnte() {
		String es = getRequest().getParameter("es");
		if(es != null && !es.equals("") && getUser() != null){
			AmComune am = comuneService.getComune(getUser().getCurrentEnte());
			nomeEnte = am != null ? am.getDescrizione() : "";
			nomeEnte = nomeEnte.substring(0, 1).toUpperCase()
					+ nomeEnte.substring(1).toLowerCase();
		}
		return nomeEnte;
	}

	public void setNomeEnte(String nomeEnte) {
		this.nomeEnte = nomeEnte;
	}

	public String getDatasource() {
		if (datasource == null) {
			try {
				Properties props = new Properties();
				String path = System.getProperty(NOME_PATH_JBOSS_CONF)
						+ "\\" + NOME_PROPERTIES;
				String newpath = "file:///" + path.replaceAll("\\\\", "/");
				URL url;
				url = new URL(newpath);
				props.load(url.openStream());
				String route = props.getProperty(getEnte());
				if (route == null)
					System.out.println("!Data Source " + getEnte()
							+ " Not Found");
				datasource = "java:/jdbc/Diogene_DS_" + route;

			} catch (MalformedURLException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public Long controllaAccesso() {

		boolean ok = false;
		Long idSogg = null;

		try {
			SoggettoSearchCriteria criteria = new SoggettoSearchCriteria();
			criteria.setUsername(getUsername());
			criteria.setEnteId(getEnte());
			List<SSpSoggetto> lista = spProfAnagService.getSoggettiList(
					criteria, 0, 2);
			if (lista.size() > 0) {
				idSogg = lista.get(0).getIdSogg();
				ok = true;
			}

		} catch (Exception e) {
			logger.error(e);
		}
		autorizzato = ok;
		return idSogg;
	}
	
	public void controllaAdmin() {
			CeTUser u = getUser();
			if (u != null) {
				AmInstance ist = applicationService.getInstanceByApplicationComune(SPPROF_APPLICATION_AM, u.getCurrentEnte());
				instance = ist.getName();
				if(ist != null)
					administrator = GestionePermessi.autorizzato(u, ist.getName(), SPPROF_ITEM_AM, SPPROF_PERMISSION_AM);
			}	
			getLogger().debug("administrator: " + administrator);
			if(administrator)
				autorizzato = true;
	}

	public Long getIdSogg() {
		return idSogg;
	}

	public void setIdSogg(Long idSogg) {
		this.idSogg = idSogg;
	}

	public Boolean getAutorizzato() {
		if (autorizzato == null && username != null){
			controllaAccesso();
		}
		if(administrator)
			autorizzato = true;
		return autorizzato;
	}

	public void setAutorizzato(Boolean autorizzato) {
		this.autorizzato = autorizzato;
	}

	public Boolean getAdministrator() {
		if(administrator == null)
			controllaAdmin();
			
		return administrator;
	}

	public void setAdministrator(Boolean administrator) {
		this.administrator = administrator;
	}

	public boolean isInserimetoRichiesta() {
		
		return PermessiHandler.controlla(this.getUser(), PermessiHandler.PERMESSO_INSERIMENTO_RICHIESTE_FASCICOLO);
	}
	
	public boolean isCreazioneCartella() {
		return PermessiHandler.controlla(this.getUser(), PermessiHandler.PERMESSO_GESTIONE_RICHIESTE_FASCICOLO);
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}	
	
}
