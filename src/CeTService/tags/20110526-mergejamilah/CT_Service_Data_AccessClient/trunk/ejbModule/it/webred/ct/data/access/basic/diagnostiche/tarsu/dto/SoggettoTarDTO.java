package it.webred.ct.data.access.basic.diagnostiche.tarsu.dto;

import java.io.Serializable;

import it.webred.ct.data.model.tarsu.SitTTarSogg;

public class SoggettoTarDTO implements Serializable{
	
	private String titolo;
	private SitTTarSogg sitTTarSogg;
	
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public SitTTarSogg getSitTTarSogg() {
		return sitTTarSogg;
	}
	public void setSitTTarSogg(SitTTarSogg sitTTarSogg) {
		this.sitTTarSogg = sitTTarSogg;
	}

}
