/*
 * Created on 13-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.soggetto.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
/**
 * @author Giulio Quaresima - WebRed
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Soggetto extends EscObject implements Serializable{
	
	private String codSoggetto;
	private String regola;
	private String dataCaricamento;
	private String comune;
	private String codiceFiscale;
	private String partitaIva;
	private String tipoPersona;
	private String denominazione;
	private String cognome;
	private String nome;
	private String sesso;
	private String dataNascita;
	private String luogoNascita;
	private String comuneNascita;
	
	
	
	public Soggetto(){
		codSoggetto = "";
		regola = "";
		dataCaricamento = "";
		comune = "";
		codiceFiscale = "";
		partitaIva = "";
		tipoPersona = "";
		denominazione = "";
		cognome = "";
		nome = "";
		sesso = "";
		dataNascita = "";
		luogoNascita = "";
		comuneNascita = "";		
	
	}

	/**
	 * @return
	 */
	public String getChiave() {
			return codSoggetto;
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
		/**
	 * @param string
	 */
	public void setcodSoggetto(String string) {
		codSoggetto = string;
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
	

	
	/**
	 * @return
	 */
	public String getCodiceFiscale() {
		return codiceFiscale;
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
	public String getCognome() {
		return cognome;
	}

	/**
	 * @return
	 */
	public String getComuneNascita() {
		return comuneNascita;
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
	public String getDataNascita() {
		return dataNascita;
	}

	/**
	 * @return
	 */
	public String getDenominazione() {
		return denominazione;
	}

	/**
	 * @return
	 */
	public String getLuogoNascita() {
		return luogoNascita;
	}

	/**
	 * @return
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return
	 */
	public String getPartitaIva() {
		return partitaIva;
	}

	/**
	 * @return
	 */
	public String getRegola() {
		return regola;
	}

	/**
	 * @return
	 */
	public String getSesso() {
		return sesso;
	}

	/**
	 * @return
	 */
	public String getTipoPersona() {
		return tipoPersona;
	}

	/**
	 * @param string
	 */
	public void setCodiceFiscale(String string) {
		codiceFiscale = string;
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
	public void setCognome(String string) {
		cognome = string;
	}

	/**
	 * @param string
	 */
	public void setComuneNascita(String string) {
		comuneNascita = string;
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
	public void setDataNascita(String string) {
		dataNascita = string;
	}

	/**
	 * @param string
	 */
	public void setDenominazione(String string) {
		denominazione = string;
	}

	/**
	 * @param string
	 */
	public void setLuogoNascita(String string) {
		luogoNascita = string;
	}

	/**
	 * @param string
	 */
	public void setNome(String string) {
		nome = string;
	}

	/**
	 * @param string
	 */
	public void setPartitaIva(String string) {
		partitaIva = string;
	}

	/**
	 * @param string
	 */
	public void setRegola(String string) {
		regola = string;
	}

	/**
	 * @param string
	 */
	public void setSesso(String string) {
		sesso = string;
	}

	/**
	 * @param string
	 */
	public void setTipoPersona(String string) {
		tipoPersona = string;
	}

}
