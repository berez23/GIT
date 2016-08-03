
package it.escsolution.escwebgis.docfa.bean;

import it.escsolution.escwebgis.common.EscObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PrgDocfa extends EscObject implements Serializable{
	
	String foglio; 
	String particella;
	String allegato;
	String destFunz; 
	String legenda; 
	String areaPRG; 
	String areaInt; 
	String areaPart;
	
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
	public String getAllegato() {
		return allegato;
	}
	public void setAllegato(String allegato) {
		this.allegato = allegato;
	}
	public String getDestFunz() {
		return destFunz;
	}
	public void setDestFunz(String destFunz) {
		this.destFunz = destFunz;
	}
	public String getLegenda() {
		return legenda;
	}
	public void setLegenda(String legenda) {
		this.legenda = legenda;
	}
	public String getAreaPRG() {
		return areaPRG;
	}
	public void setAreaPRG(String areaPRG) {
		this.areaPRG = areaPRG;
	}
	public String getAreaInt() {
		return areaInt;
	}
	public void setAreaInt(String areaInt) {
		this.areaInt = areaInt;
	}
	public String getAreaPart() {
		return areaPart;
	}
	public void setAreaPart(String areaPart) {
		this.areaPart = areaPart;
	} 
}
