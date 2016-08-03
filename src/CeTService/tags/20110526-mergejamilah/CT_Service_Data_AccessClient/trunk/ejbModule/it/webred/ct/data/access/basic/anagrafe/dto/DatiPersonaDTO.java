package it.webred.ct.data.access.basic.anagrafe.dto;

import it.webred.ct.data.model.anagrafe.SitDPersona;

import java.io.Serializable;

public class DatiPersonaDTO extends AnagrafeBaseDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SitDPersona persona;
	private String desComNas;
	private String desProvNas;
	private String siglaProvNas;
	private String dtNasStr;
	private String desLuogoNascita;
	private String indirizzoResidenza;
	public SitDPersona getPersona() {
		return persona;
	}
	public void setPersona(SitDPersona persona) {
		this.persona = persona;
	}
	public String getDesComNas() {
		return desComNas;
	}
	public void setDesComNas(String desComNas) {
		this.desComNas = desComNas;
	}
	public String getDesProvNas() {
		return desProvNas;
	}
	public void setDesProvNas(String desProvNas) {
		this.desProvNas = desProvNas;
	}
	public String getSiglaProvNas() {
		return siglaProvNas;
	}
	public void setSiglaProvNas(String siglaProvNas) {
		this.siglaProvNas = siglaProvNas;
	}
	public String getIndirizzoResidenza() {
		return indirizzoResidenza;
	}
	public void setIndirizzoResidenza(String indirizzoResidenza) {
		this.indirizzoResidenza = indirizzoResidenza;
	}
	public String getDtNasStr() {
		return dtNasStr;
	}
	public void setDtNasStr(String dtNasStr) {
		this.dtNasStr = dtNasStr;
	}
	public String getDesLuogoNascita() {
		return desLuogoNascita;
	}
	public void setDesLuogoNascita(String desLuogoNascita) {
		this.desLuogoNascita = desLuogoNascita;
	}
	

}
