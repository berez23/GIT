/*
 * Created on 27-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.tributi.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
/**
 * @author administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class OggettiTARSU extends EscObject implements Serializable{
	
	private String comune, codEnte;
	private String anagrafe;
	private String codSoggetto;
	private String foglio;
	private String particella;
	private String subalterno;
	private String parCatastali;
	private String civico;
	private String garage;
	private String fondo;
	private String soffitta;
	private String commerciale;
	private String artigianale;
	private String produttivo;
	private String servizi;
	private String accessori;
	private String abitazione;
	private String supTotale;
	private String contribuente;
	private String dataCaricamento;
	private String chiave;
	private String descrCategoria;
	private String provenienza;
	private String indirizzo;
	private String supVani;
	private String vano;
	private String piano;
	private String edificio;
	private String ambiente;
	private String altezzaMin;
	private String altezzaMax;
	
	private String latitudine;
	private String longitudine;
	
	public OggettiTARSU(){
		comune = "";
		anagrafe = "";
		codSoggetto = "";
		foglio = "";
		particella = "";
		subalterno = "";
		parCatastali = "";
		civico = "";
		garage = "";
		fondo = "";
		soffitta = "";
		commerciale = "";
		artigianale = "";
		produttivo = "";
		servizi = "";
		accessori = "";
		abitazione = "";
		supTotale = "";
		contribuente = "";
		dataCaricamento = "";
		descrCategoria="";
		provenienza="";
		indirizzo="";
		latitudine="";
		longitudine="";
	}
	
	/**
	 * @return
	 */
	public String getAbitazione() {
		return abitazione;
	}

	/**
	 * @return
	 */
	public String getAccessori() {
		return accessori;
	}

	/**
	 * @return
	 */
	public String getAnagrafe() {
		return anagrafe;
	}

	/**
	 * @return
	 */
	public String getArtigianale() {
		return artigianale;
	}

	/**
	 * @return
	 */
	public String getCivico() {
		return civico;
	}

	/**
	 * @return
	 */
	public String getCodSoggetto() {
		return codSoggetto;
	}

	/**
	 * @return
	 */
	public String getCommerciale() {
		return commerciale;
	}

	/**
	 * @return
	 */
	public String getComune() {
		return comune;
	}

	/**
	 * @return
	 */
	public String getContribuente() {
		return contribuente;
	}

	/**
	 * @return
	 */
	public String getDataCaricamento() {
		return dataCaricamento;
	}

	/**
	 * @return
	 */
	public String getFoglio() {
		return foglio;
	}

	/**
	 * @return
	 */
	public String getFondo() {
		return fondo;
	}

	/**
	 * @return
	 */
	public String getGarage() {
		return garage;
	}

	/**
	 * @return
	 */
	public String getParCatastali() {
		return parCatastali;
	}

	/**
	 * @return
	 */
	public String getParticella() {
		return particella;
	}

	/**
	 * @return
	 */
	public String getProduttivo() {
		return produttivo;
	}

	/**
	 * @return
	 */
	public String getServizi() {
		return servizi;
	}

	/**
	 * @return
	 */
	public String getSoffitta() {
		return soffitta;
	}

	/**
	 * @return
	 */
	public String getSubalterno() {
		return subalterno;
	}

	/**
	 * @return
	 */
	public String getSupTotale() {
		return supTotale;
	}

	/**
	 * @param string
	 */
	public void setAbitazione(String string) {
		abitazione = string;
	}

	/**
	 * @param string
	 */
	public void setAccessori(String string) {
		accessori = string;
	}

	/**
	 * @param string
	 */
	public void setAnagrafe(String string) {
		anagrafe = string;
	}

	/**
	 * @param string
	 */
	public void setArtigianale(String string) {
		artigianale = string;
	}

	/**
	 * @param string
	 */
	public void setCivico(String string) {
		civico = string;
	}

	/**
	 * @param string
	 */
	public void setCodSoggetto(String string) {
		codSoggetto = string;
	}

	/**
	 * @param string
	 */
	public void setCommerciale(String string) {
		commerciale = string;
	}

	/**
	 * @param string
	 */
	public void setComune(String string) {
		comune = string;
	}

	/**
	 * @param string
	 */
	public void setContribuente(String string) {
		contribuente = string;
	}

	/**
	 * @param string
	 */
	public void setDataCaricamento(String string) {
		dataCaricamento = string;
	}

	/**
	 * @param string
	 */
	public void setFoglio(String string) {
		foglio = string;
	}

	/**
	 * @param string
	 */
	public void setFondo(String string) {
		fondo = string;
	}

	/**
	 * @param string
	 */
	public void setGarage(String string) {
		garage = string;
	}

	/**
	 * @param string
	 */
	public void setParCatastali(String string) {
		parCatastali = string;
	}

	/**
	 * @param string
	 */
	public void setParticella(String string) {
		particella = string;
	}

	/**
	 * @param string
	 */
	public void setProduttivo(String string) {
		produttivo = string;
	}

	/**
	 * @param string
	 */
	public void setServizi(String string) {
		servizi = string;
	}

	/**
	 * @param string
	 */
	public void setSoffitta(String string) {
		soffitta = string;
	}

	/**
	 * @param string
	 */
	public void setSubalterno(String string) {
		subalterno = string;
	}

	/**
	 * @param string
	 */
	public void setSupTotale(String string) {
		supTotale = string;
	}

	/**
	 * @return
	 */
	public String getChiave() {
		return chiave;
	}

	/**
	 * @param string
	 */
	public void setChiave(String string) {
		chiave = string;
	}

	/**
	 * @return Returns the descrCategoria.
	 */
	public String getDescrCategoria() {
		return descrCategoria;
	}
	/**
	 * @param descrCategoria The descrCategoria to set.
	 */
	public void setDescrCategoria(String descrCategoria) {
		this.descrCategoria = descrCategoria;
	}
	public String getProvenienza() {
		return provenienza;
	}
	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public String getCodEnte()
	{
		return codEnte;
	}

	public void setCodEnte(String codEnte)
	{
		this.codEnte = codEnte;
	}

	public String getIndirizzo()
	{
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo)
	{
		this.indirizzo = indirizzo;
	}

	public String getSupVani()
	{
		return supVani;
	}

	public void setSupVani(String supVani)
	{
		this.supVani = supVani;
	}

	public String getAltezzaMax()
	{
		return altezzaMax;
	}

	public void setAltezzaMax(String altezzaMax)
	{
		this.altezzaMax = altezzaMax;
	}

	public String getAltezzaMin()
	{
		return altezzaMin;
	}

	public void setAltezzaMin(String altezzaMin)
	{
		this.altezzaMin = altezzaMin;
	}

	public String getAmbiente()
	{
		return ambiente;
	}

	public void setAmbiente(String ambiente)
	{
		this.ambiente = ambiente;
	}

	public String getEdificio()
	{
		return edificio;
	}

	public void setEdificio(String edificio)
	{
		this.edificio = edificio;
	}

	public String getPiano()
	{
		return piano;
	}

	public void setPiano(String piano)
	{
		this.piano = piano;
	}

	public String getVano()
	{
		return vano;
	}

	public void setVano(String vano)
	{
		this.vano = vano;
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

}
