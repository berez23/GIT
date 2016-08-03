package it.webred.cs.csa.web.manbean.fascicolo;

import it.webred.cs.csa.ejb.client.AccessTableCasoSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableDiarioSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableInterventoSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableSoggettoSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.data.model.CsCCategoriaSociale;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;
import it.webred.ct.service.jaxb.intTerritorio.datiCivico.response.Risposta;

public class FascicoloCompBaseBean extends CsUiCompBaseBean {

	protected AccessTableDiarioSessionBeanRemote diarioService = (AccessTableDiarioSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableDiarioSessionBean");
	protected AccessTableInterventoSessionBeanRemote interventoService = (AccessTableInterventoSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableInterventoSessionBean");
	protected AccessTableSoggettoSessionBeanRemote soggettoService = (AccessTableSoggettoSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableSoggettoSessionBean");
	protected AccessTableCasoSessionBeanRemote casoService = (AccessTableCasoSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableCasoSessionBean");

	protected Long idCaso;
	protected Long idSoggetto;
	protected CsASoggetto csASoggetto;
	protected CsCCategoriaSociale catsocCorrente;
	
	protected boolean readOnly;
	
	public void initialize(CsASoggetto soggetto) {
		
		try{
			
			csASoggetto = soggetto;
			idSoggetto = soggetto.getAnagraficaId();
			idCaso = soggetto.getCsACaso().getId();
			
			//Recupero la categoria sociale
			BaseDTO b = new BaseDTO();
			fillEnte(b);
			b.setObj(idSoggetto);
			catsocCorrente = soggettoService.getCatSocAttualeBySoggetto(b);
						
			initializeData();
			
		}catch(Exception e){
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}

	}

	protected void initializeData() {
		
	}
	
	public Long getIdCaso() {
		return idCaso;
	}

	public void setIdCaso(Long idCaso) {
		this.idCaso = idCaso;
	}

	public Long getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

	public CsCCategoriaSociale getCatsocCorrente() {
		return catsocCorrente;
	}

	public void setCatsocCorrente(CsCCategoriaSociale catsocCorrente) {
		this.catsocCorrente = catsocCorrente;
	}

	public CsASoggetto getCsASoggetto() {
		return csASoggetto;
	}

	public void setCsASoggetto(CsASoggetto csASoggetto) {
		this.csASoggetto = csASoggetto;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
}
