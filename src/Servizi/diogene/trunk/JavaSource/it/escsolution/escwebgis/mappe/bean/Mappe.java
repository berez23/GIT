package it.escsolution.escwebgis.mappe.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;

public class Mappe extends EscObject implements Serializable {

	private String comune;
	private String foglio;
	private String particella;
	private String latitudine;
	private String longitudine;
	
	public String getComune() {
		return comune;
	}
	public void setComune(String comune) {
		this.comune = comune;
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
	public String getLatitudine() {
		return latitudine;
	}
	public void setLatitudine(String latitudine) {
		this.latitudine = latitudine;
	}
	public String getLongitudine() {
		return longitudine;
	}
	public void setLongitudine(String longitudine) {
		this.longitudine = longitudine;
	}
	
}
