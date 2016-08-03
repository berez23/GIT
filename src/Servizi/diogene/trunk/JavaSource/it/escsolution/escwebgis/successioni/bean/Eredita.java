/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.successioni.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Eredita extends EscObject implements Serializable{
	
	//chiave
	private String ufficio;
	private String anno;
	private String volume;
	private String numero;
	private String sottonumero;
	private String comune;
	private String progressivo;
	
	//oggetto
	private String progressivoImmobile;
	private String progressivoParticella;
	private String numeratoreQuotaDef;
	private String denominatoreQuotaDef;
	private String foglio;
	private String particella;
	private String subalterno;
	private String indirizzoImmobile;

	//erede
	private String numeratoreQuotaEre;
	private String denominatoreQuotaEre;
	private String progressivoErede;
	private String cfErede;
	private String denominazione;
	
	//defunto
	private String cfDefunto;
	private String Cognome;
	private String Nome;
	private String progressivoDef;
	
	public Eredita(){
		
		ufficio = "";
		anno = "";
		volume = "";
		numero = "";
		sottonumero = "";
		comune = "";
		progressivo = "";
		
		progressivoDef = "";
		progressivoImmobile = "";
		progressivoParticella ="";
		numeratoreQuotaDef= "";
		denominatoreQuotaDef= "";
		foglio= "";
		particella= "";
		subalterno= "";
		indirizzoImmobile= "";
		
		numeratoreQuotaEre= "";
		denominatoreQuotaEre= "";
		progressivoErede= "";
		cfErede= "";
		denominazione= "";

    }

	
	/**
	 * @return Returns the denominatoreQuotaDef.
	 */
	public String getDenominatoreQuotaDef() {
		return denominatoreQuotaDef;
	}
	/**
	 * @return Returns the numeratoreQuotaDef.
	 */
	public String getNumeratoreQuotaDef() {
		return numeratoreQuotaDef;
	}
	/**
	 * @return Returns the progressivoParticella.
	 */
	public String getProgressivoParticella() {
		return progressivoParticella;
	}
	/**
	 * @param denominatoreQuotaDef The denominatoreQuotaDef to set.
	 */
	public void setDenominatoreQuotaDef(String denominatoreQuotaDef) {
		this.denominatoreQuotaDef = denominatoreQuotaDef;
	}
	/**
	 * @param numeratoreQuotaDef The numeratoreQuotaDef to set.
	 */
	public void setNumeratoreQuotaDef(String numeratoreQuotaDef) {
		this.numeratoreQuotaDef = numeratoreQuotaDef;
	}
	/**
	 * @param progressivoParticella The progressivoParticella to set.
	 */
	public void setProgressivoParticella(String progressivoParticella) {
		this.progressivoParticella = progressivoParticella;
	}

	
	
	/**
	 * @return Returns the anno.
	 */
	public String getAnno() {
		return anno;
	}
	/**
	 * @param anno The anno to set.
	 */
	public void setAnno(String anno) {
		this.anno = anno;
	}
	/**
	 * @return Returns the comune.
	 */
	public String getComune() {
		return comune;
	}
	/**
	 * @param comune The comune to set.
	 */
	public void setComune(String comune) {
		this.comune = comune;
	}
	/**
	 * @return Returns the foglio.
	 */
	public String getFoglio() {
		return foglio;
	}
	/**
	 * @param foglio The foglio to set.
	 */
	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}
	/**
	 * @return Returns the indirizzoImmobile.
	 */
	public String getIndirizzoImmobile() {
		return indirizzoImmobile;
	}
	/**
	 * @param indirizzoImmobile The indirizzoImmobile to set.
	 */
	public void setIndirizzoImmobile(String indirizzoImmobile) {
		this.indirizzoImmobile = indirizzoImmobile;
	}
	/**
	 * @return Returns the numero.
	 */
	public String getNumero() {
		return numero;
	}
	/**
	 * @param numero The numero to set.
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}
	/**
	 * @return Returns the particella.
	 */
	public String getParticella() {
		return particella;
	}
	/**
	 * @param particella The particella to set.
	 */
	public void setParticella(String particella) {
		this.particella = particella;
	}
	/**
	 * @return Returns the progressivo.
	 */
	public String getProgressivo() {
		return progressivo;
	}
	/**
	 * @param progressivo The progressivo to set.
	 */
	public void setProgressivo(String progressivo) {
		this.progressivo = progressivo;
	}
	/**
	 * @return Returns the progressivoImmobile.
	 */
	public String getProgressivoImmobile() {
		return progressivoImmobile;
	}
	/**
	 * @param progressivoImmobile The progressivoImmobile to set.
	 */
	public void setProgressivoImmobile(String progressivoImmobile) {
		this.progressivoImmobile = progressivoImmobile;
	}
	/**
	 * @return Returns the sottonumero.
	 */
	public String getSottonumero() {
		return sottonumero;
	}
	/**
	 * @param sottonumero The sottonumero to set.
	 */
	public void setSottonumero(String sottonumero) {
		this.sottonumero = sottonumero;
	}
	/**
	 * @return Returns the subalterno.
	 */
	public String getSubalterno() {
		return subalterno;
	}
	/**
	 * @param subalterno The subalterno to set.
	 */
	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}
	/**
	 * @return Returns the ufficio.
	 */
	public String getUfficio() {
		return ufficio;
	}
	/**
	 * @param ufficio The ufficio to set.
	 */
	public void setUfficio(String ufficio) {
		this.ufficio = ufficio;
	}
	/**
	 * @return Returns the volume.
	 */
	public String getVolume() {
		return volume;
	}
	/**
	 * @param volume The volume to set.
	 */
	public void setVolume(String volume) {
		this.volume = volume;
	}
	/**
	 * @return Returns the cfErede.
	 */
	public String getCfErede() {
		return cfErede;
	}
	/**
	 * @return Returns the denominatoreQuotaEre.
	 */
	public String getDenominatoreQuotaEre() {
		return denominatoreQuotaEre;
	}
	/**
	 * @return Returns the denominazione.
	 */
	public String getDenominazione() {
		return denominazione;
	}
	/**
	 * @return Returns the numeratoreQuotaEre.
	 */
	public String getNumeratoreQuotaEre() {
		return numeratoreQuotaEre;
	}
	/**
	 * @return Returns the progressivoErede.
	 */
	public String getProgressivoErede() {
		return progressivoErede;
	}
	/**
	 * @param cfErede The cfErede to set.
	 */
	public void setCfErede(String cfErede) {
		this.cfErede = cfErede;
	}
	/**
	 * @param denominatoreQuotaEre The denominatoreQuotaEre to set.
	 */
	public void setDenominatoreQuotaEre(String denominatoreQuotaEre) {
		this.denominatoreQuotaEre = denominatoreQuotaEre;
	}
	/**
	 * @param denominazione The denominazione to set.
	 */
	public void setDenominazione(String denom) {
		denominazione = denom;
	}
	/**
	 * @param numeratoreQuotaEre The numeratoreQuotaEre to set.
	 */
	public void setNumeratoreQuotaEre(String numeratoreQuotaEre) {
		this.numeratoreQuotaEre = numeratoreQuotaEre;
	}
	/**
	 * @param progressivoErede The progressivoErede to set.
	 */
	public void setProgressivoErede(String progressivoErede) {
		this.progressivoErede = progressivoErede;
	}
	/**
	 * @return Returns the cfDefunto.
	 */
	public String getCfDefunto() {  
		return cfDefunto;
	}
	/**
	 * @return Returns the cognome.
	 */
	public String getCognome() {
		return Cognome;
	}
	/**
	 * @return Returns the nome.
	 */
	public String getNome() {
		return Nome;
	}
	/**
	 * @param cfDefunto The cfDefunto to set.
	 */
	public void setCfDefunto(String cfDefunto) {
		this.cfDefunto = cfDefunto;
	}
	/**
	 * @param cognome The cognome to set.
	 */
	public void setCognome(String cognome) {
		Cognome = cognome;
	}
	/**
	 * @param nome The nome to set.
	 */
	public void setNome(String nome) {
		Nome = nome;
	}
	public String getProgressivoDef() {
		return progressivoDef;
	}
	public void setProgressivoDef(String progressivoDef) {
		this.progressivoDef = progressivoDef;
	}
}
