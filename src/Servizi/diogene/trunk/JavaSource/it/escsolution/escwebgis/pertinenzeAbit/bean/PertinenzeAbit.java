package it.escsolution.escwebgis.pertinenzeAbit.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
import java.util.Date;

public class PertinenzeAbit extends EscObject implements Serializable {

	private static final long serialVersionUID = 9125995906068488719L;
	
	private String foglio = "";
	private String particella = "";
	private String subalterno = "";
	private String categoria = "";
	private String cf = "";
	private String percPoss = null;
	private String tipoTitolo = "";
	private String cognome = "";
	private String nome = "";
	private String dataNascita = "";
	private String indirizzo = "";
	private String civico = "";
	private String chiave = "";
	private String latitudine = "";
	private String longitudine = "";
	private String codNazionale = "";
	
	
	public String getCodNazionale() {
		return codNazionale;
	}
	public void setCodNazionale(String codNazionale) {
		this.codNazionale = codNazionale;
	}
	public String getLatitudine() {
		return latitudine;
	}
	public void setLatitudine(String latitudine) {
		this.latitudine = latitudine;
	}
	public String getLongitudine() {
		return longitudine;
	}
	public void setLongitudine(String longitudine) {
		this.longitudine = longitudine;
	}
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
	public String getSubalterno() {
		return subalterno;
	}
	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getCf() {
		return cf;
	}
	public void setCf(String cf) {
		this.cf = cf;
	}
	public String getPercPoss() {
		return percPoss;
	}
	public void setPercPoss(String percPoss) {
		this.percPoss = percPoss;
	}
	public String getTipoTitolo() {
		return tipoTitolo;
	}
	public void setTipoTitolo(String tipoTitolo) {
		this.tipoTitolo = tipoTitolo;
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
	public String getDataNascita() {
		return dataNascita;
	}
	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getCivico() {
		return civico;
	}
	public void setCivico(String civico) {
		this.civico = civico;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getChiave() {
		return chiave;
	}
	public void setChiave(String key) {
		this.chiave = key;
	}
	
	
}
