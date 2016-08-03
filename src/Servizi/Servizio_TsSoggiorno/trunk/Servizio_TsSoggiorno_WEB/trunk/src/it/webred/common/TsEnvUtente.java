package it.webred.common;

import it.webred.cet.permission.CeTUser;

import it.webred.ct.config.model.AmInstance;
import it.webred.ct.config.parameters.application.ApplicationService;

/**
 * classe che contiene i riferimenti all'utente
 * Viene utilizzata per passare l'utente dallo strato servlet allo strato logic
 */
public class TsEnvUtente extends TsEnvBase{
	//private Principal utente;
	private String dataSource;
	private String nomeApplicazione;
	private String nomeIstanza;
	private CeTUser utente;
	
	public static final String ENTE_DEFAULT = "F704";
	public static final String NOME_APP_AM_PROFILER_DEFAULT = "TsSoggiorno";
	
	
	public String getNomeApplicazione() {
		return nomeApplicazione;
	}

	public void setNomeApplicazione(String nomeApplicazione) {
		this.nomeApplicazione = nomeApplicazione;
	}


	public TsEnvUtente (CeTUser utente , String dataSource, String nomeApplicazione ) {
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
		return utente.getCurrentEnte();
	}

	public String getNomeIstanza() {
		return nomeIstanza;
	}

	public void setNomeIstanza(String nomeIstanza) {
		this.nomeIstanza = nomeIstanza;
	}	
	
}
