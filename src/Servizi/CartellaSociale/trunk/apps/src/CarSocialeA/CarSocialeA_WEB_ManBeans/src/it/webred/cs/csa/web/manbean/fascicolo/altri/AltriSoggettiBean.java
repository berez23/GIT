package it.webred.cs.csa.web.manbean.fascicolo.altri;

import it.webred.cs.csa.ejb.client.AccessTableSoggettoSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.data.model.CsCCategoriaSociale;
import it.webred.cs.jsf.interfaces.IAltriSoggetti;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;
import it.webred.ct.data.access.basic.anagrafe.AnagrafeService;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaSoggettoAnagrafeDTO;
import it.webred.ct.data.model.anagrafe.SitDPersona;

import java.util.ArrayList;
import java.util.List;

public class AltriSoggettiBean extends CsUiCompBaseBean implements IAltriSoggetti {

	private AccessTableSoggettoSessionBeanRemote soggettoService = (AccessTableSoggettoSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableSoggettoSessionBean");
	private AnagrafeService anagrafeService = (AnagrafeService) getEjb("CT_Service", "CT_Service_Data_Access", "AnagrafeServiceBean");
	
	private CsASoggetto csASoggetto;
	private CsCCategoriaSociale catsocCorrente;
	
	private List<CsASoggetto> listaAltriSoggetti;
	private String labelSoggetto;
		
	public void initialize(CsASoggetto soggetto) {
		
		try{
			
			if(soggetto != null) {
				csASoggetto = soggetto;
				
				labelSoggetto = csASoggetto.getCsAAnagrafica().getCognome() + " " + csASoggetto.getCsAAnagrafica().getNome();
				
				//Recupero la categoria sociale
				BaseDTO b = new BaseDTO();
				fillEnte(b);
				b.setObj(csASoggetto.getAnagraficaId());
				catsocCorrente = soggettoService.getCatSocAttualeBySoggetto(b);
								
				caricaAltriSoggetti();
			}
			
		}catch(Exception e){
			addErrorFromProperties("caricamento.error");
			logger.error(e.getMessage(),e);
		}
		

	}
	
	private void caricaAltriSoggetti() {
		
		
		
		if(listaAltriSoggetti == null)
			listaAltriSoggetti = new ArrayList<CsASoggetto>();
		
		BaseDTO b = new BaseDTO();
		fillEnte(b);
		
		RicercaSoggettoAnagrafeDTO rs = new RicercaSoggettoAnagrafeDTO();
		fillEnte(rs);
		rs.setCodFis(csASoggetto.getCsAAnagrafica().getCf());
		List<SitDPersona> listaFamiglia = anagrafeService.getFamigliaByCF(rs);
		
		for(SitDPersona persona: listaFamiglia) {
			b.setObj(persona.getCodfisc());
			CsASoggetto altroSoggetto = soggettoService.getSoggettiByCF(b);
			if(altroSoggetto != null)
				listaAltriSoggetti.add(altroSoggetto);
		}
		
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

	public List<CsASoggetto> getListaAltriSoggetti() {
		return listaAltriSoggetti;
	}

	@Override
	public void setListaAltriSoggetti(List<CsASoggetto> listaAltriSoggetti) {
		this.listaAltriSoggetti = listaAltriSoggetti;
	}

	@Override
	public String getLabelSoggetto() {
		return labelSoggetto;
	}

	public void setLabelSoggetto(String labelSoggetto) {
		this.labelSoggetto = labelSoggetto;
	}
	
}
