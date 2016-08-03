package it.escsolution.escwebgis.common;

import it.webred.cet.permission.CeTUser;

import it.webred.ct.config.model.AmInstance;
import it.webred.ct.config.parameters.application.ApplicationService;

/**
 * @author marcoric
 * classe che contiene i riferimenti all'utente
 * Viene utilizzata per passare l'utente dallo strato servlet allo strato logic
 */
public class EnvUtente extends EnvBase{
	//private Principal utente;
	private String dataSource;
	private String nomeApplicazione;
	private String nomeIstanza;
	private CeTUser utente;
	
	public static final String ENTE_DEFAULT = "F704";
	public static final String NOME_APP_AM_PROFILER_DEFAULT = "diogene";
	
	
	public String getNomeApplicazione() {
		return nomeApplicazione;
	}

	public void setNomeApplicazione(String nomeApplicazione) {
		this.nomeApplicazione = nomeApplicazione;
	}

	/*
	public EnvUtente (Principal utente , String dataSource, String nomeApplicazione ) {
		this.dataSource = dataSource;
		this.utente		= utente;
		this.nomeApplicazione = nomeApplicazione;
	}
	*/

	public EnvUtente (CeTUser utente , String dataSource, String nomeApplicazione ) {
		this.dataSource = dataSource;
		this.utente		= utente;
		this.nomeApplicazione = nomeApplicazione;
		try {
			ApplicationService service = (ApplicationService) getEjb("CT_Service", "CT_Config_Manager", "ApplicationServiceBean");
			
			AmInstance ist = service.getInstanceByApplicationComune(nomeApplicazione != null? nomeApplicazione: NOME_APP_AM_PROFILER_DEFAULT, utente.getCurrentEnte());
			if(ist!=null)
				this.nomeIstanza = ist.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	
	public CeTUser getUtente() {
		return utente;
	}
	
	public void setUtente(CeTUser utente) {
		this.utente = utente;
	}
	
	public String getEnte() {
		/*
		try {
			String nomeAppAm = new EscLogic(this).getProperty("NOME_APP_AM_PROFILER", this.getClass());
			if (nomeAppAm == null) {
				nomeAppAm = NOME_APP_AM_PROFILER_DEFAULT;
			}
			
			SSOUser ssoUser = (SSOUser)utente;
			for (int i = 0; i < ssoUser.getProperties().length; i++) {
				SSONameValuePair vp = ssoUser.getProperties()[i];
				String p = vp.getName();								
				if(p.indexOf("property@-@ente@-@" + nomeAppAm) == 0 && !p.toLowerCase().startsWith("property@-@ente@-@diogenedb")) {
					return vp.getValue();
				}
			}
		} catch (Throwable t) {
			return ENTE_DEFAULT;
		}
		*/
		//return ENTE_DEFAULT;
		return utente.getCurrentEnte();
	}

	public String getNomeIstanza() {
		return nomeIstanza;
	}

	public void setNomeIstanza(String nomeIstanza) {
		this.nomeIstanza = nomeIstanza;
	}	
	
}
