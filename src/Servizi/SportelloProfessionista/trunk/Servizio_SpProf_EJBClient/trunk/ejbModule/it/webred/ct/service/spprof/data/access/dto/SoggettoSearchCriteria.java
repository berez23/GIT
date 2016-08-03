package it.webred.ct.service.spprof.data.access.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class SoggettoSearchCriteria extends CeTBaseObject implements Serializable {
	
	private String nome;
	private String cognome;
	private String username;
	private String denom;
	private String pIVA;
	private String idQualifica;
	private String codFisc;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDenom() {
		return denom;
	}
	public void setDenom(String denom) {
		this.denom = denom;
	}
	public String getpIVA() {
		return pIVA;
	}
	public void setpIVA(String pIVA) {
		this.pIVA = pIVA;
	}
	public String getIdQualifica() {
		return idQualifica;
	}
	public void setIdQualifica(String idQualifica) {
		this.idQualifica = idQualifica;
	}
	public String getCodFisc() {
		return codFisc;
	}
	public void setCodFisc(String codFisc) {
		this.codFisc = codFisc;
	}
}
