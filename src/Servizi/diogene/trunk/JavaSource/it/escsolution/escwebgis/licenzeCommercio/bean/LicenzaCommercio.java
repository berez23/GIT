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
public class LicenzaCommercio extends EscObject implements Serializable{

	private String idLicenza;
	private String codEnte;
	private String numEsercizio;
	private String provenienza;
	private String tipologia;
	private String numCivico;
	private String supMinuto;
	private String supTotale;
	private String cognome;
	private String nome;
	private String codiceFiscale;
	private String formaGiuridica;
	private String indirizzoResidenza;
	private String comuneResidenza;
	private String sedime;
	private String nomeVia;
		
	
	public LicenzaCommercio(){
		
		idLicenza = "";
		codEnte = "";
		numEsercizio = "";
		provenienza = "";
		tipologia = "";
		numCivico = "";
		supMinuto = "";
		supTotale = "";
		cognome = "";
		nome = "";
		codiceFiscale = "";
		formaGiuridica = "";
		indirizzoResidenza = "";
		comuneResidenza = "";
		sedime = "";
		nomeVia = "";
	    }
	
	public String getChiave(){ 
		//return ""+codEnte+"|"+numEsercizio+"|"+provenienza;
		return idLicenza;
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
	public String getNomeVia() {
		return nomeVia;
	}
	public String getFormaGiuridica() {
		return formaGiuridica;
	}
	public String getIndirizzoResidenza() {
		return indirizzoResidenza;
	}
	public String getNumCivico() {
		return numCivico;
	}
	public String getNumEsercizio() {
		return numEsercizio;
	}
	public String getProvenienza() {
		return provenienza;
	}
	public String getSedime() {
		return sedime;
	}
	public String getSupMinuto() {
		return supMinuto;
	}
	public String getSupTotale() {
		return supTotale;
	}
	public String getTipologia() {
		return tipologia;
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
	public void setNomeVia(String nomeVia) {
		this.nomeVia = nomeVia;
	}
	public void setFormaGiuridica(String formaGiuridica) {
		this.formaGiuridica = formaGiuridica;
	}
	public void setIndirizzoResidenza(String indirizzoResidenza) {
		this.indirizzoResidenza = indirizzoResidenza;
	}
	public void setNumCivico(String numCivico) {
		this.numCivico = numCivico;
	}
	public void setNumEsercizio(String numEsercizio) {
		this.numEsercizio = numEsercizio;
	}
	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}
	public void setSedime(String sedime) {
		this.sedime = sedime;
	}
	public void setSupMinuto(String supMinuto) {
		this.supMinuto = supMinuto;
	}
	public void setSupTotale(String supTotale) {
		this.supTotale = supTotale;
	}
	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}
	public String getCognome() {
		return cognome;
	}
	public String getIdLicenza() {
		return idLicenza;
	}
	public String getNome() {
		return nome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public void setIdLicenza(String idLicenza) {
		this.idLicenza = idLicenza;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}
