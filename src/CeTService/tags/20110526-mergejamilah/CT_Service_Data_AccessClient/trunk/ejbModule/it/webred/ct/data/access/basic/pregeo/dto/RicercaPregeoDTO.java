package it.webred.ct.data.access.basic.pregeo.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;
import java.math.BigDecimal;

public class RicercaPregeoDTO extends CeTBaseObject implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private BigDecimal foglio;
	private String particella;
	public BigDecimal getFoglio() {
		return foglio;
	}
	public void setFoglio(BigDecimal foglio) {
		this.foglio = foglio;
	}
	public String getParticella() {
		return particella;
	}
	public void setParticella(String particella) {
		this.particella = particella;
	}
	

}
