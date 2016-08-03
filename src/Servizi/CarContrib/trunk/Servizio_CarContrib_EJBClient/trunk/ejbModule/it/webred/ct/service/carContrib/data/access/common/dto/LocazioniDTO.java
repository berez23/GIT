package it.webred.ct.service.carContrib.data.access.common.dto;

import it.webred.ct.data.model.locazioni.LocazioniA;
import it.webred.ct.data.model.locazioni.LocazioniB;
import it.webred.ct.data.model.locazioni.LocazioniI;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;
import java.util.List;

public class LocazioniDTO extends CeTBaseObject implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private LocazioniA datiOggLocazione;
	private LocazioniI datiOggLocImmobile;
	//dati in più per la scheda ICI/TARSU
	private String fps;
	private String tipoSoggetto;
	private String comune;
	private String indirizzo;
	//dati in più per la scheda tarsu
	private String supCat;
	private String supTarsu;
	
	public LocazioniA getDatiOggLocazione() {
		return datiOggLocazione;
	}
	public void setDatiOggLocazione(LocazioniA datiOggLocazione) {
		this.datiOggLocazione = datiOggLocazione;
	}
	public LocazioniI getDatiOggLocImmobile() {
		return datiOggLocImmobile;
	}
	public void setDatiOggLocImmobile(LocazioniI datiOggLocImmobile) {
		this.datiOggLocImmobile = datiOggLocImmobile;
	}
	
	public String getSupCat() {
		return supCat;
	}
	public void setSupCat(String supCat) {
		this.supCat = supCat;
	}
	public String getTipoSoggetto() {
		return tipoSoggetto;
	}
	public void setTipoSoggetto(String tipoSoggetto) {
		this.tipoSoggetto = tipoSoggetto;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getSupTarsu() {
		return supTarsu;
	}
	public void setSupTarsu(String supTarsu) {
		this.supTarsu = supTarsu;
	}
	public String getComune() {
		return comune;
	}
	public void setComune(String comune) {
		this.comune = comune;
	}
	public String getFps() {
		return fps;
	}
	public void setFps(String fps) {
		this.fps = fps;
	}

}
