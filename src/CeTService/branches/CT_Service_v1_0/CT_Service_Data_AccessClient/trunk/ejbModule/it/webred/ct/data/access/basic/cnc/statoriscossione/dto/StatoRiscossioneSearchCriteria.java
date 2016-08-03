package it.webred.ct.data.access.basic.cnc.statoriscossione.dto;

import it.webred.ct.data.model.cnc.statoriscossione.ChiaveULStatoRiscossione;

import java.io.Serializable;

public class StatoRiscossioneSearchCriteria implements Serializable {

	private ChiaveULStatoRiscossione chiaveRiscossione = new ChiaveULStatoRiscossione();
	private String codiceFiscale;
	
	public ChiaveULStatoRiscossione getChiaveRiscossione() {
		return chiaveRiscossione;
	}
	public void setChiaveRiscossione(ChiaveULStatoRiscossione chiaveRiscossione) {
		this.chiaveRiscossione = chiaveRiscossione;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	
	
}
