package it.webred.ct.service.spclass.web.bean.util;

import it.webred.ct.config.model.AmComune;
import it.webred.ct.config.parameters.comune.ComuneService;
import it.webred.ct.service.spclass.web.SpProfClassamentoBaseBean;
import it.webred.ct.service.spprof.data.access.SpProfAnagService;
import it.webred.ct.service.spprof.data.access.dto.SoggettoSearchCriteria;
import it.webred.ct.service.spprof.data.model.SSpSoggetto;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;

import org.apache.log4j.Logger;

public class UserBean extends SpProfClassamentoBaseBean{

	public static final String NOME_PROPERTIES = "datarouter.properties";
	public static final String NOME_PATH_JBOSS_CONF = "jboss.server.config.url";
	
	public ComuneService comuneService = (ComuneService) getEjb("CT_Service", "CT_Config_Manager", "ComuneServiceBean");
	
	protected SpProfAnagService spProfAnagService = (SpProfAnagService) getEjb("Servizio_SpProf", "Servizio_SpProf_EJB", "SpProfAnagServiceBean");
	
	private static Logger logger = Logger.getLogger(UserBean.class.getName());

	private String username;
	
	private String nomeEnte;
	
	private String datasource;
	
	private Long idSogg;
	
	private boolean noLogout;
	private String foglioSpProf;
	private String partSpProf;
	private String categoriaSpProf;
	
	public void doLogout(){
		
		getRequest().getSession().invalidate();
		try {
			getResponse().sendRedirect(getRequest().getContextPath() + "/");
		} catch (IOException e) {
			logger.error("Eccezione: "+e.getMessage(),e);
		}
		
	}

	public String getUsername() {
		
		if(foglioSpProf == null || getRequest().getParameter("fgl") != null){
			foglioSpProf = (String) getRequest().getParameter("fgl");
			if(foglioSpProf != null){
				noLogout = true;
			}else noLogout = false;
		}
		if(partSpProf == null || getRequest().getParameter("par") != null)
			partSpProf = (String) getRequest().getParameter("par");
		if(categoriaSpProf == null || getRequest().getParameter("par") != null)
			categoriaSpProf = (String) getRequest().getParameter("cat");
		if(foglioSpProf != null)
			logger.info("Foglio: " + foglioSpProf);
		if(partSpProf != null)
			logger.info("Particella: " + partSpProf);
		if(categoriaSpProf != null)
			logger.info("Categoria: " + categoriaSpProf);
		if (getRequest().getUserPrincipal()==null || getRequest().getUserPrincipal().getName()==null)
			return "admin";
		
		username = getRequest().getUserPrincipal().getName();
		
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNomeEnte() {
		if(nomeEnte == null && getUser() != null){
			AmComune am = comuneService.getComune(getUser().getCurrentEnte());
			nomeEnte = am != null? am.getDescrizione(): "";
			nomeEnte = nomeEnte.substring(0,1).toUpperCase() + nomeEnte.substring(1).toLowerCase();
		}
		return nomeEnte;
	}

	public void setNomeEnte(String nomeEnte) {
		this.nomeEnte = nomeEnte;
	}

	public String getDatasource() {
		if(datasource == null){
			try {
				Properties props = new Properties();
				String path = System.getProperty(NOME_PATH_JBOSS_CONF)
						+ NOME_PROPERTIES;
				URL url;
				url = new URL(path);
				props.load(url.openStream());
				String route = props.getProperty(getEnte());
				if (route == null)
					System.out.println("!Data Source " + getEnte() + " Not Found");
				datasource = "Diogene_DS_" + route;

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}
	
	private boolean controllaAccesso(String username){
		
		boolean ok = false;
		
		try{
			SoggettoSearchCriteria criteria = new SoggettoSearchCriteria();
			criteria.setUsername(username);
			criteria.setEnteId(getEnte());
			List<SSpSoggetto> lista = spProfAnagService.getSoggettiList(criteria, 0, 2);
			if(lista.size()>0){
				idSogg = lista.get(0).getIdSogg();
				ok = true;
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ok;
	}

	public Long getIdSogg() {
		return idSogg;
	}

	public void setIdSogg(Long idSogg) {
		this.idSogg = idSogg;
	}

	public String getFoglioSpProf() {
		return foglioSpProf;
	}

	public void setFoglioSpProf(String foglioSpProf) {
		this.foglioSpProf = foglioSpProf;
	}

	public String getPartSpProf() {
		return partSpProf;
	}

	public void setPartSpProf(String partSpProf) {
		this.partSpProf = partSpProf;
	}

	public String getCategoriaSpProf() {
		return categoriaSpProf;
	}

	public void setCategoriaSpProf(String categoriaSpProf) {
		this.categoriaSpProf = categoriaSpProf;
	}

	public boolean isNoLogout() {
		return noLogout;
	}

	public void setNoLogout(boolean noLogout) {
		this.noLogout = noLogout;
	}
	
}
