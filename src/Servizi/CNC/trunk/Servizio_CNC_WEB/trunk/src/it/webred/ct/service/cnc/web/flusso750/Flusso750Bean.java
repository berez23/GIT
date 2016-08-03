package it.webred.ct.service.cnc.web.flusso750;

import it.webred.ct.data.access.basic.cnc.CNCDataIn;
import it.webred.ct.data.access.basic.cnc.flusso750.Flusso750ServiceException;
import it.webred.ct.data.access.basic.cnc.flusso750.dto.AnagraficaSoggettiDTO;
import it.webred.ct.data.access.basic.cnc.flusso750.dto.DettaglioRuoloVistatoDTO;
import it.webred.ct.data.access.basic.cnc.flusso750.dto.InfoRuoloDTO;
import it.webred.ct.data.model.cnc.flusso750.ChiaveULPartita;
import it.webred.ct.data.access.basic.cnc.CNCDTO;


import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;

public class Flusso750Bean extends Flusso750BaseBean implements Serializable {

	private String codicePartita;
	private List<AnagraficaSoggettiDTO> anagraficaSoggettiList;
	private ChiaveULPartita chiavePartita;
	private DettaglioRuoloVistatoDTO dettaglio;
	private InfoRuoloDTO infoRuolo;
	
	
	@PostConstruct
	public void init() {
		chiavePartita = new ChiaveULPartita();
	}
	
	
	public void doFindAnagrafica() {
		try {
			getLogger().info("Recupero informazioni 750 Anagrafica. Codice partita ["+codicePartita+"]");
			
			anagraficaSoggettiList = super.getFlusso750Service().getAnagraficaByCodicePartita(super.createDataIn(codicePartita));
			
			CNCDTO dto = new CNCDTO();
			fillEnte(dto);
			
			for (int i=0; i < anagraficaSoggettiList.size(); i++) {
				AnagraficaSoggettiDTO anag = anagraficaSoggettiList.get(i);
				dto.setCodAmbito(Long.parseLong(anag.getInfoRuolo().getChiaveRuolo().getCodAmbito()));
				anag.setDescrAmbito(super.getCommonService().getAmbitoDescr(dto));
			}
			
			getLogger().debug("Anagrafica ["+anagraficaSoggettiList+"]");
		}
		catch(Flusso750ServiceException fse) {
			super.getLogger().error("", fse);
			super.addErrorMessage("flusso750.anagrafica.error", fse.getCause().getMessage());
		}
		
		
	}

	public String getCodicePartita() {
		return codicePartita;
	}

	public void setCodicePartita(String codicePartita) {
		System.out.println("Cod Partita ["+codicePartita+"]");
		this.codicePartita = codicePartita;
	}

	public List<AnagraficaSoggettiDTO> getAnagraficaSoggettiList() {
		return anagraficaSoggettiList;
	}

	public void setAnagraficaSoggettiList(
			List<AnagraficaSoggettiDTO> anagraficaSoggettiList) {
		this.anagraficaSoggettiList = anagraficaSoggettiList;
	}
	
	
	public ChiaveULPartita getChiavePartita() {
		return chiavePartita;
	}

	public void setChiavePartia(ChiaveULPartita chiavePartia) {
		
		this.chiavePartita = chiavePartia;
	}

	@RolesAllowed(value="PIPPO")
	public String doFindDettaglioRuoliVistati() {
		try {
			dettaglio = super.getFlusso750Service().getDettaglioRuoloVistato(createDataIn(chiavePartita));
			AnagraficaSoggettiDTO anag = dettaglio.getAnagrafica();
			
			CNCDTO dto = new CNCDTO();
			fillEnte(dto);
			dto.setCodAmbito(Long.parseLong(anag.getInfoRuolo().getChiaveRuolo().getCodAmbito()));
			
			anag.setDescrAmbito(super.getCommonService().getAmbitoDescr(dto));
			getLogger().debug("Dettaglio Ruolo Vistato [" + dettaglio + "]");
		}
		catch(Flusso750ServiceException fse) {
			super.getLogger().error("", fse);
			super.addErrorMessage("flusso750.dettaglio.error", fse.getCause().getMessage());
		}
		
		return "flusso750.dettaglioRuoloVistato";
	}

	public DettaglioRuoloVistatoDTO getDettaglio() {
		return dettaglio;
	}

	public void setDettaglio(DettaglioRuoloVistatoDTO dettaglio) {
		this.dettaglio = dettaglio;
	}
	
	public String doViewAnagrafica() {
		return "flusso750.anagVistati";
	}
	
	public String doViewFrontespizio() {
		try {
			infoRuolo = super.getFlusso750Service().getFrontespizioRuolo(createDataIn(chiavePartita));
		}
		catch(Flusso750ServiceException fse) {
			super.getLogger().error("", fse);
			super.addErrorMessage("flusso750.fronte.error", fse.getCause().getMessage());
		}
		System.out.println("DoView");
		return "flusso750.dettaglioContabileRuoloVistato";
	}

	public InfoRuoloDTO getInfoRuolo() {
		return infoRuolo;
	}

	public void setInfoRuolo(InfoRuoloDTO infoRuolo) {
		this.infoRuolo = infoRuolo;
	}
	
	public String doGotoList() {
		return "flusso750.listAnagRuoliVistati";
	}
	
	public String doGotoRiscossioni() {
		return "flusso750.go.listRiscossioni";
	}
	
	public String doGotoListRuoli() {
		return "flusso750.listRuoliVistati";
	}

}
