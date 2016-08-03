package it.webred.ct.data.access.basic.compravendite.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class SoggettoCompravenditeDTO extends CeTBaseObject implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String tipoSoggetto;
	private String cognome ;
	private String nome;
	private String codiceFiscale;
	private String denominazione;
	private String codiceFiscaleG;
	private String flagTipoTitolContro;
	private String flagTipoTitolFavore;
	public String getTipoSoggetto() {
		return tipoSoggetto;
	}
	public void setTipoSoggetto(String tipoSoggetto) {
		this.tipoSoggetto = tipoSoggetto;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public String getCodiceFiscaleG() {
		return codiceFiscaleG;
	}
	public void setCodiceFiscaleG(String codiceFiscaleG) {
		this.codiceFiscaleG = codiceFiscaleG;
	}
	public String getFlagTipoTitolContro() {
		return flagTipoTitolContro;
	}
	public void setFlagTipoTitolContro(String flagTipoTitolContro) {
		this.flagTipoTitolContro = flagTipoTitolContro;
	}
	public String getFlagTipoTitolFavore() {
		return flagTipoTitolFavore;
	}
	public void setFlagTipoTitolFavore(String flagTipoTitolFavore) {
		this.flagTipoTitolFavore = flagTipoTitolFavore;
	}
	

}
