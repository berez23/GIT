package it.webred.indice.fastsearch.fabbricato.bean;

import it.webred.indice.fastsearch.bean.TotaleBean;

import java.util.List;

public class FabbricatoTotaleBean extends TotaleBean {
	
	private String fkFabbricato;
	private String foglio;
	private String particella;
	private String sezione;
	
	private  List<List<FabbricatoTotaleBean>> fabbAssociati;
	
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
	public String getFkFabbricato() {
		return fkFabbricato;
	}
	public void setFkFabbricato(String fkFabbricato) {
		this.fkFabbricato = fkFabbricato;
	}
	public String getSezione() {
		return sezione;
	}
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}
	public List<List<FabbricatoTotaleBean>> getFabbAssociati() {
		return fabbAssociati;
	}
	public void setFabbAssociati(List<List<FabbricatoTotaleBean>> fabbAssociati) {
		this.fabbAssociati = fabbAssociati;
	}
	

}
