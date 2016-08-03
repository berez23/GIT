package it.webred.ct.data.access.basic.tarsu.dto;

import java.io.Serializable;
import it.webred.ct.data.model.tarsu.*;

public class SoggettoTarsuDTO implements Serializable{
	
	private String titolo;
	private SitTTarSogg soggetto;
	
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public SitTTarSogg getSoggetto() {
		return soggetto;
	}
	public void setSoggetto(SitTTarSogg soggetto) {
		this.soggetto = soggetto;
	}
	
	

}
