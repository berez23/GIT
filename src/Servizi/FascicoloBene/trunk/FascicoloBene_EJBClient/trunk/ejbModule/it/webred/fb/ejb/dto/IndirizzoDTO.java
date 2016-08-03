package it.webred.fb.ejb.dto;

import java.io.Serializable;

public class IndirizzoDTO implements Serializable {

	private String tipoVia;
	private String codVia;
	private String descrVia;
	private String civico;
	private String codComune;
	private String desComune;
	private String prov;
	
	public String getTipoVia() {
		return tipoVia;
	}
	public void setTipoVia(String tipoVia) {
		this.tipoVia = tipoVia;
	}
	public String getCodVia() {
		return codVia;
	}
	public void setCodVia(String codVia) {
		this.codVia = codVia;
	}
	public String getDescrVia() {
		return descrVia;
	}
	public void setDescrVia(String descrVia) {
		this.descrVia = descrVia;
	}
	public String getCivico() {
		return civico;
	}
	public void setCivico(String civico) {
		this.civico = civico;
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
		
}
