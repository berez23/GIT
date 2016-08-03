package it.escsolution.escwebgis.pertinenzeAbit.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;

public class DatiTitolarita extends EscObject implements Serializable {

	private static final long serialVersionUID = 293851277421950182L;
	private String chiave = "";
	private String cognome = "";
	private String nome = "";
	private String cf = "";
	private String dataNascita = "";
	private String percentualePossesso = "";
	private String tipoTitolo = "";
	
	
	public String getChiave() {
		return chiave;
	}
	public void setChiave(String chiave) {
		this.chiave = chiave;
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
	public String getCf() {
		return cf;
	}
	public void setCf(String cf) {
		this.cf = cf;
	}
	public String getDataNascita() {
		return dataNascita;
	}
	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}
	public String getPercentualePossesso() {
		return percentualePossesso;
	}
	public void setPercentualePossesso(String percentualePossesso) {
		this.percentualePossesso = percentualePossesso;
	}
	public String getTipoTitolo() {
		return tipoTitolo;
	}
	public void setTipoTitolo(String tipoTitolo) {
		this.tipoTitolo = tipoTitolo;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	

}
