package it.webred.indice.fastsearch.oggetto.bean;

import it.webred.indice.fastsearch.bean.TotaleBean;

import java.io.Serializable;
import java.util.List;

public class OggettoTotaleBean extends TotaleBean {
	
	private String fkOggetto;
	private String foglio;
	private String particella;
	private String sub;
	
	private List<List<OggettoTotaleBean>> oggettiAssociati;
	
	public String getFkOggetto() {
		return fkOggetto;
	}
	public void setFkOggetto(String fkOggetto) {
		this.fkOggetto = fkOggetto;
	}
	public String getFoglio() {
		return foglio;
	}
	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}
	public String getParticella() {
		return particella;
	}
	public void setParticella(String particella) {
		this.particella = particella;
	}
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	public List<List<OggettoTotaleBean>> getOggettiAssociati() {
		return oggettiAssociati;
	}
	public void setOggettiAssociati(List<List<OggettoTotaleBean>> oggettiAssociati) {
		this.oggettiAssociati = oggettiAssociati;
	}
	
	
}
