package it.escsolution.escwebgis.pertinenzeAbit.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
import java.util.Date;

public class PertinenzeAbitTitolare  extends EscObject implements Serializable {

	private static final long serialVersionUID = 5171004847308760895L;
	
	private String chiave = "";
	private String cognome = "";
	private String nome = "";
	private String cf = "";
	private String dataNascita = "";
	private String sesso = "";
	private String posizioneAnagrafica = "";
	private String indirizzo = "";
	private String civico = "";
	private String dataImmigrazione = "";
	private String dataEmigrazione = "";
	private String dataMorte = "";
	
	
	public String getCivico() {
		return civico;
	}
	public void setCivico(String civico) {
		this.civico = civico;
	}
	public String getDataImmigrazione() {
		return dataImmigrazione;
	}
	public void setDataImmigrazione(String dataImmigrazione) {
		this.dataImmigrazione = dataImmigrazione;
	}
	public String getDataEmigrazione() {
		return dataEmigrazione;
	}
	public void setDataEmigrazione(String dataEmigrazione) {
		this.dataEmigrazione = dataEmigrazione;
	}
	public String getDataMorte() {
		return dataMorte;
	}
	public void setDataMorte(String dataMorte) {
		this.dataMorte = dataMorte;
	}
	public String getSesso() {
		return sesso;
	}
	public void setSesso(String sesso) {
		this.sesso = sesso;
	}
	public String getPosizioneAnagrafica() {
		return posizioneAnagrafica;
	}
	public void setPosizioneAnagrafica(String posizioneAnagrafica) {
		this.posizioneAnagrafica = posizioneAnagrafica;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
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
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

}
