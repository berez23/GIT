package it.bod.application.beans;

import it.bod.application.common.MasterItem;

public class GraffatoBean extends MasterItem{

	private static final long serialVersionUID = -428328992315009343L;
	
	private String foglio = "";
	private String particella = "";
	private String subalterno = "";
	
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
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	

}
