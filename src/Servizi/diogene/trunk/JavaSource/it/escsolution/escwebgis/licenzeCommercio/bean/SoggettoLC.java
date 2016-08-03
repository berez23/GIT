/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.licenzeCommercio.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SoggettoLC extends EscObject implements Serializable{

	private String idLicA;
	private String codEnte;
	private String numEsercizio;
	private String provenienza;
	private String cognome;
	private String nome;
	private String codiceFiscale;
	private String formaGiuridica;
	private String indirizzoResidenza;
	private String comuneResidenza;
		
	
	public SoggettoLC(){
		
		idLicA = "";
		codEnte = "";
		numEsercizio = "";
		provenienza = "";
		cognome = "";
		nome = "";
		codiceFiscale = "";
		formaGiuridica = "";
		indirizzoResidenza = "";
		comuneResidenza = "";
	    }
	
	public String getChiave(){ 
		return idLicA;
	}
	

	public String getCodEnte() {
		return codEnte;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public String getComuneResidenza() {
		return comuneResidenza;
	}
	public String getFormaGiuridica() {
		return formaGiuridica;
	}
	public String getIndirizzoResidenza() {
		return indirizzoResidenza;
	}
	public String getNumEsercizio() {
		return numEsercizio;
	}
	public String getProvenienza() {
		return provenienza;
	}
	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public void setComuneResidenza(String comuneResidenza) {
		this.comuneResidenza = comuneResidenza;
	}
	public void setFormaGiuridica(String formaGiuridica) {
		this.formaGiuridica = formaGiuridica;
	}
	public void setIndirizzoResidenza(String indirizzoResidenza) {
		this.indirizzoResidenza = indirizzoResidenza;
	}
	public void setNumEsercizio(String numEsercizio) {
		this.numEsercizio = numEsercizio;
	}
	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}
	public String getCognome() {
		return cognome;
	}
	public String getIdLicA() {
		return idLicA;
	}
	public String getNome() {
		return nome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public void setIdLicA(String idLicA) {
		this.idLicA = idLicA;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}
