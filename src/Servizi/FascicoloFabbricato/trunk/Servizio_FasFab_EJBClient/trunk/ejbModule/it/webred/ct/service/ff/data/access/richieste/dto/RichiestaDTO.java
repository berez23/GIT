package it.webred.ct.service.ff.data.access.richieste.dto;

import it.webred.ct.service.ff.data.model.FFGestioneRichieste;
import it.webred.ct.service.ff.data.model.FFRichieste;
import it.webred.ct.service.ff.data.model.FFSoggetti;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class RichiestaDTO extends CeTBaseObject implements Serializable {
	
	private FFSoggetti soggetto = new FFSoggetti();
	private FFRichieste richiesta = new FFRichieste();
	private FFGestioneRichieste gestRichiesta = new FFGestioneRichieste();
	
	public FFSoggetti getSoggetto() {
		return soggetto;
	}
	public void setSoggetto(FFSoggetti soggetto) {
		this.soggetto = soggetto;
	}
	public FFRichieste getRichiesta() {
		return richiesta;
	}
	public void setRichiesta(FFRichieste richiesta) {
		this.richiesta = richiesta;
	}
	public FFGestioneRichieste getGestRichiesta() {
		return gestRichiesta;
	}
	public void setGestRichiesta(FFGestioneRichieste gestRichiesta) {
		this.gestRichiesta = gestRichiesta;
	}
	
	
	

}
