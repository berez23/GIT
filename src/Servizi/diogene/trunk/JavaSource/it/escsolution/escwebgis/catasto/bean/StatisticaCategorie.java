package it.escsolution.escwebgis.catasto.bean;

import java.io.Serializable;


public class StatisticaCategorie implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String sezione;
	private String particella;
	private String foglio;
	private Integer occorrenze;
	private String categoria;
	public String getSezione() {
		return sezione;
	}
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}
	public String getParticella() {
		return particella;
	}
	public void setParticella(String particella) {
		this.particella = particella;
	}
	public String getFoglio() {
		return foglio;
	}
	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}
	
	public Integer getOccorrenze() {
		return occorrenze;
	}
	public void setOccorrenze(Integer occorrenze) {
		this.occorrenze = occorrenze;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	
}
