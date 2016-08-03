package it.webred.gitout.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="rogitoDTO")
public class RogitoDTO implements Serializable{
	
	private static final long serialVersionUID = 6034813545082016603L;
	
	private String tipo = "";
	private String registroParticolare = "";
	private String dataPresentazioneAtto = "";
	private String roganteNominativo = "";
	private String roganteCodiceFiscale = "";
	private String sede = "";
	private String anno = "";
	private String numeroRepertorio = "";
	private String dataValiditaAtto = "";
	private String esito = "";
	private String dataRegistrazioneAttivita = "";
	private String rettifica = "";
	private String registrazioneDiff = "";
	private String dataDiff = "";
	
	public RogitoDTO() {
	}//-------------------------------------------------------------------------


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public String getRegistroParticolare() {
		return registroParticolare;
	}


	public void setRegistroParticolare(String registroParticolare) {
		this.registroParticolare = registroParticolare;
	}


	public String getDataPresentazioneAtto() {
		return dataPresentazioneAtto;
	}


	public void setDataPresentazioneAtto(String dataPresentazioneAtto) {
		this.dataPresentazioneAtto = dataPresentazioneAtto;
	}


	public String getRoganteNominativo() {
		return roganteNominativo;
	}


	public void setRoganteNominativo(String roganteNominativo) {
		this.roganteNominativo = roganteNominativo;
	}


	public String getRoganteCodiceFiscale() {
		return roganteCodiceFiscale;
	}


	public void setRoganteCodiceFiscale(String roganteCodiceFiscale) {
		this.roganteCodiceFiscale = roganteCodiceFiscale;
	}


	public String getSede() {
		return sede;
	}


	public void setSede(String sede) {
		this.sede = sede;
	}


	public String getAnno() {
		return anno;
	}


	public void setAnno(String anno) {
		this.anno = anno;
	}


	public String getNumeroRepertorio() {
		return numeroRepertorio;
	}


	public void setNumeroRepertorio(String numeroRepertorio) {
		this.numeroRepertorio = numeroRepertorio;
	}


	public String getDataValiditaAtto() {
		return dataValiditaAtto;
	}


	public void setDataValiditaAtto(String dataValiditaAtto) {
		this.dataValiditaAtto = dataValiditaAtto;
	}


	public String getEsito() {
		return esito;
	}


	public void setEsito(String esito) {
		this.esito = esito;
	}


	public String getDataRegistrazioneAttivita() {
		return dataRegistrazioneAttivita;
	}


	public void setDataRegistrazioneAttivita(String dataRegistrazioneAttivita) {
		this.dataRegistrazioneAttivita = dataRegistrazioneAttivita;
	}


	public String getRettifica() {
		return rettifica;
	}


	public void setRettifica(String rettifica) {
		this.rettifica = rettifica;
	}


	public String getRegistrazioneDiff() {
		return registrazioneDiff;
	}


	public void setRegistrazioneDiff(String registrazioneDiff) {
		this.registrazioneDiff = registrazioneDiff;
	}


	public String getDataDiff() {
		return dataDiff;
	}


	public void setDataDiff(String dataDiff) {
		this.dataDiff = dataDiff;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
