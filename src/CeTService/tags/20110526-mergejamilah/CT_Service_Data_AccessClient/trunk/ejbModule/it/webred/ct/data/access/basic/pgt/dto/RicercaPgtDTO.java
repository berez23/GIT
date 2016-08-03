package it.webred.ct.data.access.basic.pgt.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class RicercaPgtDTO extends CeTBaseObject implements Serializable{
	private static final long serialVersionUID = 1L;
	private String codNazionale;  
	private long id;
	private String desLayer;
	private String tipoLayer;
	private String standard;
	private String sezione;
	private Integer foglio;
	private String particella;
	
	
	public String getCodNazionale() {
		return codNazionale;
	}
	public void setCodNazionale(String codNazionale) {
		this.codNazionale = codNazionale;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTipoLayer() {
		return tipoLayer;
	}
	public void setTipoLayer(String tipoLayer) {
		this.tipoLayer = tipoLayer;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getDesLayer() {
		return desLayer;
	}
	public void setDesLayer(String desLayer) {
		this.desLayer = desLayer;
	}
	
	public String getSezione() {
		return sezione;
	}
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}
	public Integer getFoglio() {
		return foglio;
	}
	public void setFoglio(Integer foglio) {
		this.foglio = foglio;
	}
	public String getParticella() {
		return particella;
	}
	public void setParticella(String particella) {
		this.particella = particella;
	}
	

}
