package it.escsolution.escwebgis.pertinenzeAbit.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;

public class DatiCatastali extends EscObject implements Serializable {

	private static final long serialVersionUID = -6999003814469762281L;
	
	private String foglio = "";
	private String particella = "";
	private String subalterno = "";
	private String subalternoTerreno = "";
	private String dataInizioVal = "";
	
	private String categoria = "";
	private String superficeCatastale = "";
	private String chiave = "";
	
	

	private String classe = "";
	private String consistenza = "";
	private String rendita = "";
	private String zona = "";
	private String edificio = "";
	private String scala = "";
	private String interno = "";
	private String piano = "";
	private String indirizzo = "";
	private String civico = "";
	
	
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
	public String getSuperficeCatastale() {
		return superficeCatastale;
	}
	public void setSuperficeCatastale(String superficeCatastale) {
		this.superficeCatastale = superficeCatastale;
	}
	public String getChiave() {
		return chiave;
	}
	public void setChiave(String chiave) {
		this.chiave = chiave;
	}
	public String getSubalternoTerreno() {
		return subalternoTerreno;
	}
	public void setSubalternoTerreno(String subalternoTerreno) {
		this.subalternoTerreno = subalternoTerreno;
	}
	public String getDataInizioVal() {
		return dataInizioVal;
	}
	public void setDataInizioVal(String dataInizioVal) {
		this.dataInizioVal = dataInizioVal;
	}
	public String getClasse() {
		return classe;
	}
	public void setClasse(String classe) {
		this.classe = classe;
	}
	public String getConsistenza() {
		return consistenza;
	}
	public void setConsistenza(String consistenza) {
		this.consistenza = consistenza;
	}
	public String getRendita() {
		return rendita;
	}
	public void setRendita(String rendita) {
		this.rendita = rendita;
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
	
	
}
