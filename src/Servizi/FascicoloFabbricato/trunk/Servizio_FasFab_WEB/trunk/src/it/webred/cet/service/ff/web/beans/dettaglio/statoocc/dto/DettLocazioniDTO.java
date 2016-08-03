package it.webred.cet.service.ff.web.beans.dettaglio.statoocc.dto;

import it.webred.ct.data.model.locazioni.LocazioniA;
import it.webred.ct.data.model.locazioni.LocazioniB;

import java.io.Serializable;

public class DettLocazioniDTO implements Serializable {
	
	private LocazioniB soggetto;
	private LocazioniA oggetto;
	private String posizAnagSoggetto;
	private String tipoSoggLocazione;
	public LocazioniB getSoggetto() {
		return soggetto;
	}
	public void setSoggetto(LocazioniB soggetto) {
		this.soggetto = soggetto;
	}
	public LocazioniA getOggetto() {
		return oggetto;
	}
	public void setOggetto(LocazioniA oggetto) {
		this.oggetto = oggetto;
	}
	public String getPosizAnagSoggetto() {
		return posizAnagSoggetto;
	}
	public void setPosizAnagSoggetto(String posizAnagSoggetto) {
		this.posizAnagSoggetto = posizAnagSoggetto;
	}
	public String getTipoSoggLocazione() {
		return tipoSoggLocazione;
	}
	public void setTipoSoggLocazione(String tipoSoggLocazione) {
		this.tipoSoggLocazione = tipoSoggLocazione;
	}
	

}
