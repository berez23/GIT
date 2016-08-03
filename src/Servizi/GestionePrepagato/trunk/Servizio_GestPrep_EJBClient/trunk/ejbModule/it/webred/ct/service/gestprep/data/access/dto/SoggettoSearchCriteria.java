package it.webred.ct.service.gestprep.data.access.dto;

import java.io.Serializable;

import it.webred.ct.support.datarouter.CeTBaseObject;

public class SoggettoSearchCriteria extends CeTBaseObject implements Serializable {

	private String nome;
	private String cognome;
	private String denom;
	private String pIVA;
	private String idQualifica;
	private String codFisc;
	
	public String getCodFisc() {
		return codFisc;
	}
	public void setCodFisc(String codFisc) {
		this.codFisc = codFisc;
	}
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
	
	
	
}
