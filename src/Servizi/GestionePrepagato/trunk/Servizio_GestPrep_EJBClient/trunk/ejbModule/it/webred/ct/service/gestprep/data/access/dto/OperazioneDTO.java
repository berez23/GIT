package it.webred.ct.service.gestprep.data.access.dto;

import it.webred.ct.service.gestprep.data.model.GestPrepAnagVisura;
import it.webred.ct.service.gestprep.data.model.GestPrepOperazVisure;
import it.webred.ct.service.gestprep.data.model.GestPrepSoggetti;
import it.webred.ct.service.gestprep.data.model.GestPrepTariffaVisura;

import java.io.Serializable;

public class OperazioneDTO implements Serializable {
	
	private GestPrepTariffaVisura tariffa;
	private GestPrepAnagVisura visura;
	private GestPrepSoggetti soggetto;
	private GestPrepOperazVisure operazione;
	
	public OperazioneDTO(GestPrepTariffaVisura tariffa,
			GestPrepAnagVisura visura, GestPrepSoggetti soggetto, GestPrepOperazVisure operazione) {
		super();
		this.tariffa = tariffa;
		this.visura = visura;
		this.soggetto = soggetto;
		this.operazione = operazione;
	}

	public GestPrepOperazVisure getOperazione() {
		return operazione;
	}

	public void setOperazione(GestPrepOperazVisure operazione) {
		this.operazione = operazione;
	}

	public GestPrepTariffaVisura getTariffa() {
		return tariffa;
	}

	public void setTariffa(GestPrepTariffaVisura tariffa) {
		this.tariffa = tariffa;
	}

	public GestPrepAnagVisura getVisura() {
		return visura;
	}

	public void setVisura(GestPrepAnagVisura visura) {
		this.visura = visura;
	}

	public GestPrepSoggetti getSoggetto() {
		return soggetto;
	}

	public void setSoggetto(GestPrepSoggetti soggetto) {
		this.soggetto = soggetto;
	}
	
	

}
