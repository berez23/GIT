package it.webred.cet.service.ff.web.beans.common;

import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.service.ff.data.model.FFRichieste;

public class FFFascicolo {

	private FFRichieste richiesta;
	private RicercaOggettoCatDTO ricercaOggetto;
	
	
	public FFRichieste getRichiesta() {
		return richiesta;
	}
	public void setRichiesta(FFRichieste richiesta) {
		this.richiesta = richiesta;
	}
	public RicercaOggettoCatDTO getRicercaOggetto() {
		return ricercaOggetto;
	}
	public void setRicercaOggetto(RicercaOggettoCatDTO ricercaOggetto) {
		this.ricercaOggetto = ricercaOggetto;
	}
}
