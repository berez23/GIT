package it.webred.ct.data.access.basic.anagrafe.dto;

import it.webred.ct.data.model.anagrafe.SitComune;
import it.webred.ct.data.model.anagrafe.SitDCivicoV;
import it.webred.ct.data.model.anagrafe.SitDPersona;
import it.webred.ct.data.model.anagrafe.SitDVia;
import it.webred.ct.data.model.anagrafe.SitProvincia;

import java.io.Serializable;

public class AnagraficaDTO extends AnagrafeBaseDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SitDPersona persona;
	private SitDCivicoV civico;
	private SitDVia via;
	private SitComune comuneNascita;
	private SitComune comuneResidenza;
	private SitProvincia provNascita;
	private SitProvincia provResidenza;
	
	public SitDPersona getPersona() {
		return persona;
	}
	public void setPersona(SitDPersona persona) {
		this.persona = persona;
	}
	public SitDCivicoV getCivico() {
		return civico;
	}
	public void setCivico(SitDCivicoV civico) {
		this.civico = civico;
	}
	public SitDVia getVia() {
		return via;
	}
	public void setVia(SitDVia via) {
		this.via = via;
	}
	public SitComune getComuneNascita() {
		return comuneNascita;
	}
	public void setComuneNascita(SitComune comuneNascita) {
		this.comuneNascita = comuneNascita;
	}
	public SitComune getComuneResidenza() {
		return comuneResidenza;
	}
	public void setComuneResidenza(SitComune comuneResidenza) {
		this.comuneResidenza = comuneResidenza;
	}
	public SitProvincia getProvNascita() {
		return provNascita;
	}
	public void setProvNascita(SitProvincia provNascita) {
		this.provNascita = provNascita;
	}
	public SitProvincia getProvResidenza() {
		return provResidenza;
	}
	public void setProvResidenza(SitProvincia provResidenza) {
		this.provResidenza = provResidenza;
	}

}
