package it.bod.application.beans;

import it.bod.application.common.MasterItem;

public class VincoloBean extends MasterItem{

	private static final long serialVersionUID = -7380945351617951329L;
	
	private String foglio = "";
	private String particella = "";
	private String vincolo = "";
	private String descrizione = "";
	private String areaPart = "";
	private String areaVin = "";
	private String areaInt = "";
	
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
	public String getVincolo() {
		return vincolo;
	}
	public void setVincolo(String vincolo) {
		this.vincolo = vincolo;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getAreaPart() {
		return areaPart;
	}
	public void setAreaPart(String areaPart) {
		this.areaPart = areaPart;
	}
	public String getAreaVin() {
		return areaVin;
	}
	public void setAreaVin(String areaVin) {
		this.areaVin = areaVin;
	}
	public String getAreaInt() {
		return areaInt;
	}
	public void setAreaInt(String areaInt) {
		this.areaInt = areaInt;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	
}
