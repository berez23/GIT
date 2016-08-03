package it.webred.fb.ejb.dto;

import java.io.Serializable;

public class MappaleDTO implements Serializable {

	private String codInventario;
	
	private String id;
	private String sezione;
	private String foglio;
	private String mappale;
	private String tipo;
	private String codComune;
	private String desComune;
	private String prov;
	private String provenienza;
	
	private String[] coordinate;
	
	public String getSezione() {
		return sezione;
	}
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}
	public String getFoglio() {
		return foglio;
	}
	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}
	public String getMappale() {
		return mappale;
	}
	public void setMappale(String mappale) {
		this.mappale = mappale;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getCodComune() {
		return codComune;
	}
	public void setCodComune(String codComune) {
		this.codComune = codComune;
	}
	public String getDesComune() {
		return desComune;
	}
	public void setDesComune(String desComune) {
		this.desComune = desComune;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String[] getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(String[] coordinate) {
		this.coordinate = coordinate;
	}
	public String getProvenienza() {
		return provenienza;
	}
	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}
	public String getCodInventario() {
		return codInventario;
	}
	public void setCodInventario(String codInventario) {
		this.codInventario = codInventario;
	}
	
	
}
