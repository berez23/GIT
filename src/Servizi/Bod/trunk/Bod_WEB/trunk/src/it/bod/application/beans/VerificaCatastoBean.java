package it.bod.application.beans;

import java.util.Date;

import it.bod.application.common.MasterItem;

public class VerificaCatastoBean extends MasterItem{

	private static final long serialVersionUID = 8948015034556472417L;
	
	private Double foglio = 0d;
	private String particella = "";
	private Double subalterno = 0d;
	private Date dataInizioVal = null;
	private Date dataFineVal = null;
	private String categoria = "";
	private String classe = "";
	private Double consistenza = 0d;
	private Double rendita = 0d;
	private Double superficie = 0d;
	private String zona = "";
	private String edificio = "";
	private String scala = "";
	private String interno = "";
	private String piano = "";
	private String indirizzo = "";
	private String civico = "";
	
	public String getParticella() {
		return particella;
	}
	public void setParticella(String particella) {
		this.particella = particella;
	}
	public Date getDataInizioVal() {
		return dataInizioVal;
	}
	public void setDataInizioVal(Date dataInizioVal) {
		this.dataInizioVal = dataInizioVal;
	}
	public Date getDataFineVal() {
		return dataFineVal;
	}
	public void setDataFineVal(Date dataFineVal) {
		this.dataFineVal = dataFineVal;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getClasse() {
		return classe;
	}
	public void setClasse(String classe) {
		this.classe = classe;
	}
	public Double getRendita() {
		return rendita;
	}
	public void setRendita(Double rendita) {
		this.rendita = rendita;
	}
	public Double getSuperficie() {
		return superficie;
	}
	public void setSuperficie(Double superficie) {
		this.superficie = superficie;
	}
	public String getZona() {
		return zona;
	}
	public void setZona(String zona) {
		this.zona = zona;
	}
	public String getEdificio() {
		return edificio;
	}
	public void setEdificio(String edificio) {
		this.edificio = edificio;
	}
	public String getScala() {
		return scala;
	}
	public void setScala(String scala) {
		this.scala = scala;
	}
	public String getInterno() {
		return interno;
	}
	public void setInterno(String interno) {
		this.interno = interno;
	}
	public String getPiano() {
		return piano;
	}
	public void setPiano(String piano) {
		this.piano = piano;
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
	public Double getFoglio() {
		return foglio;
	}
	public void setFoglio(Double foglio) {
		this.foglio = foglio;
	}
	public Double getSubalterno() {
		return subalterno;
	}
	public void setSubalterno(Double subalterno) {
		this.subalterno = subalterno;
	}
	public Double getConsistenza() {
		return consistenza;
	}
	public void setConsistenza(Double consistenza) {
		this.consistenza = consistenza;
	}
	
	

}
