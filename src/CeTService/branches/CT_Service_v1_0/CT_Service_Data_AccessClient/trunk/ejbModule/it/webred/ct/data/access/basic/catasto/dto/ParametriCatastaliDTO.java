package it.webred.ct.data.access.basic.catasto.dto;

import java.io.Serializable;

public class ParametriCatastaliDTO implements Serializable{
	
	private String foglio;
	private String particella;
	private String subalterno;
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
	public String getSubalterno() {
		return subalterno;
	}
	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}

}
