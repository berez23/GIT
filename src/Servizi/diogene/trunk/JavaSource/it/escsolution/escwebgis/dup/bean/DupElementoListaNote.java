/*
 * Created on 13-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.dup.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
/**
 * @author Giulio Quaresima - WebRed
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DupElementoListaNote extends EscObject implements Serializable{
	
	
	
	private String idFornitura;
	private String idNota;
	private String idSoggettoNota;
	private String cognome;
	private String nome;
	private String denominazione;
	private String sede;
	private String codiceFiscale;
	private String indirizzoImmobile;
	private String tipoSoggetto;
	private String regimePatrimoniale;
	private String favoreContro;
	private String foglio;
	private String numero;
	private String subalterno; 
	private String flagRettifica;
	
	private String latitudine;
	private String longitudine;
	private String codEnte;
	
	
	
	public DupElementoListaNote(){
		   
		idFornitura="";
		idNota="";
		idSoggettoNota="";
		cognome="";
		nome="";
		denominazione="";
		sede="";
		codiceFiscale="";
		indirizzoImmobile="";
		tipoSoggetto="";
		regimePatrimoniale="";
		favoreContro=""; 
		foglio="";
		numero="";
		subalterno="";
		flagRettifica="";
		latitudine="";
		longitudine="";
		codEnte="";
		
		}

	
	/* (non-Javadoc)
	 * @see it.escsolution.escwebgis.common.EscObject#getChiave()
	 */
	public String getChiave() {
		// TODO Auto-generated method stub
		return idFornitura;
	}
	
	
	/**
	 * @return Returns the codiceFiscale.
	 */
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	/**
	 * @param codiceFiscale The codiceFiscale to set.
	 */
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	/**
	 * @return Returns the cognome.
	 */
	public String getCognome() {
		return cognome;
	}
	/**
	 * @param cognome The cognome to set.
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	/**
	 * @return Returns the favoreContro.
	 */
	public String getFavoreContro() {
		return favoreContro;
	}
	/**
	 * @param favoreContro The favoreContro to set.
	 */
	public void setFavoreContro(String favoreContro) {
		this.favoreContro = favoreContro;
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
	 * @return Returns the idFornitura.
	 */
	public String getIdFornitura() {
		return idFornitura;
	}
	/**
	 * @param idFornitura The idFornitura to set.
	 */
	public void setIdFornitura(String idFornitura) {
		this.idFornitura = idFornitura;
	}
	/**
	 * @return Returns the idNota.
	 */
	public String getIdNota() {
		return idNota;
	}
	/**
	 * @param idNota The idNota to set.
	 */
	public void setIdNota(String idNota) {
		this.idNota = idNota;
	}
	/**
	 * @return Returns the idSoggettoNota.
	 */
	public String getIdSoggettoNota() {
		return idSoggettoNota;
	}
	/**
	 * @param idSoggettoNota The idSoggettoNota to set.
	 */
	public void setIdSoggettoNota(String idSoggettoNota) {
		this.idSoggettoNota = idSoggettoNota;
	}
	/**
	 * @return Returns the indirizzoSoggetto.
	 */

	/**
	 * @return Returns the nome.
	 */
	public String getNome() {
		return nome;
	}
	/**
	 * @param nome The nome to set.
	 */
	public void setNome(String nome) {
		this.nome = nome;
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
	 * @return Returns the tipoSoggetto.
	 */
	public String getTipoSoggetto() {
		return tipoSoggetto;
	}
	/**
	 * @param tipoSoggetto The tipoSoggetto to set.
	 */
	public void setTipoSoggetto(String tipoSoggetto) {
		this.tipoSoggetto = tipoSoggetto;
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
	 * @return Returns the regimePatrimoniale.
	 */
	public String getRegimePatrimoniale() {
		return regimePatrimoniale;
	}
	/**
	 * @param regimePatrimoniale The regimePatrimoniale to set.
	 */
	public void setRegimePatrimoniale(String regimePatrimoniale) {
		this.regimePatrimoniale = regimePatrimoniale;
	}
	
	/**
	 * @return Returns the flagRettifica.
	 */
	public String getFlagRettifica() {
		return flagRettifica;
	}
	/**
	 * @param flagRettifica The flagRettifica to set.
	 */
	public void setFlagRettifica(String flagRettifica) {
		this.flagRettifica = flagRettifica;
	}
	
	
	/**
	 * @return Returns the denominazione.
	 */
	public String getDenominazione() {
		return denominazione;
	}
	/**
	 * @param denominazione The denominazione to set.
	 */
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	
	/**
	 * @return Returns the sede.
	 */
	public String getSede() {
		return sede;
	}


	public void setSede(String sede) {
		this.sede = sede;
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


	public String getCodEnte() {
		return codEnte;
	}


	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}
	
	
	
	
	
}
