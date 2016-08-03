/*
 * Created on 7-nov-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.escsolution.escwebgis.aziende.bean;

import it.escsolution.escwebgis.common.EscObject;

/**
 * @author Giulio Quaresima - WebRed
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Azienda extends EscObject {
	private String chiave;
	private Integer NREA;
	private String ragioneSociale;
	private String CF;
	private String denominazione;
	private String provincia;
	private String comune;
	private String frazione;
	private String CAP;
	private String sedime;
	private String nomeVia;
	private String civico;
	private String dataCessazione;
	private String descCarica;
	private String CFRappresentante;
	private String cognomeRappresentante;
	private String nomeRappresentante;
	private String descrizione;
	/**
	 * @return Returns the descrizione.
	 */
	public String getDescrizione() {
		return descrizione;
	}
	/**
	 * @param descrizione The descrizione to set.
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	/**
	 * @return Returns the cAP.
	 */
	public String getCAP() {
		return CAP;
	}
	/**
	 * @param cap The cAP to set.
	 */
	public void setCAP(String cap) {
		CAP = cap;
	}
	/**
	 * @return Returns the cFRappresentante.
	 */
	public String getCFRappresentante() {
		return CFRappresentante;
	}
	/**
	 * @param rappresentante The cFRappresentante to set.
	 */
	public void setCFRappresentante(String rappresentante) {
		CFRappresentante = rappresentante;
	}
	/**
	 * @return Returns the civico.
	 */
	public String getCivico() {
		return civico;
	}
	/**
	 * @param civico The civico to set.
	 */
	public void setCivico(String civico) {
		this.civico = civico;
	}
	/**
	 * @return Returns the cognomeRappresentante.
	 */
	public String getCognomeRappresentante() {
		return cognomeRappresentante;
	}
	/**
	 * @param cognomeRappresentante The cognomeRappresentante to set.
	 */
	public void setCognomeRappresentante(String cognomeRappresentante) {
		this.cognomeRappresentante = cognomeRappresentante;
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
	 * @return Returns the dataCessazione.
	 */
	public String getDataCessazione() {
		return dataCessazione;
	}
	/**
	 * @param dataCessazione The dataCessazione to set.
	 */
	public void setDataCessazione(String dataCessazione) {
		this.dataCessazione = dataCessazione;
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
	 * @return Returns the descCarica.
	 */
	public String getDescCarica() {
		return descCarica;
	}
	/**
	 * @param descCarica The descCarica to set.
	 */
	public void setDescCarica(String descCarica) {
		this.descCarica = descCarica;
	}
	/**
	 * @return Returns the frazione.
	 */
	public String getFrazione() {
		return frazione;
	}
	/**
	 * @param frazione The frazione to set.
	 */
	public void setFrazione(String frazione) {
		this.frazione = frazione;
	}
	/**
	 * @return Returns the nomeRappresentante.
	 */
	public String getNomeRappresentante() {
		return nomeRappresentante;
	}
	/**
	 * @param nomeRappresentante The nomeRappresentante to set.
	 */
	public void setNomeRappresentante(String nomeRappresentante) {
		this.nomeRappresentante = nomeRappresentante;
	}
	/**
	 * @return Returns the nomeVia.
	 */
	public String getNomeVia() {
		return nomeVia;
	}
	/**
	 * @param nomeVia The nomeVia to set.
	 */
	public void setNomeVia(String nomeVia) {
		this.nomeVia = nomeVia;
	}
	/**
	 * @return Returns the provincia.
	 */
	public String getProvincia() {
		return provincia;
	}
	/**
	 * @param provincia The provincia to set.
	 */
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	/**
	 * @return Returns the sedime.
	 */
	public String getSedime() {
		return sedime;
	}
	/**
	 * @param sedime The sedime to set.
	 */
	public void setSedime(String sedime) {
		this.sedime = sedime;
	}
	/**
	 * @return Returns the pkAzienda.
	 */
	public String getChiave() {
		return chiave;
	}
	/**
	 * @param pkAzienda The pkAzienda to set.
	 */
	public void setChiave(String chiave) {
		this.chiave = chiave;
	}
	/**
	 * @return Returns the nREA.
	 */
	public Integer getNREA() {
		return NREA;
	}
	/**
	 * @param nrea The nREA to set.
	 */
	public void setNREA(Integer nrea) {
		NREA = nrea;
	}
	/**
	 * @return Returns the CF.
	 */
	public String getCF() {
		return CF;
	}
	/**
	 * @param cf The CF to set.
	 */
	public void setCF(String cf) {
		CF = cf;
	}
	/**
	 * @return Returns the ragioneSociale.
	 */
	public String getRagioneSociale() {
		return ragioneSociale;
	}
	/**
	 * @param ragioneSociale The ragioneSociale to set.
	 */
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
}
