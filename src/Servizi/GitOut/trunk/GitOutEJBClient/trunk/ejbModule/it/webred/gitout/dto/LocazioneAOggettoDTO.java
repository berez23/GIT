package it.webred.gitout.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="locazioneAOggettoDTO")
public class LocazioneAOggettoDTO implements Serializable{

	private static final long serialVersionUID = -9201423601234237169L;
	
	private String ufficioReg = "";
	private String annoReg = "";
	private String serieReg = "";
	private String numeroReg = "";
	private String sottonumeroReg = "";
	private String progressivoNegozio = "";
	private String codiceNegozio = "";
	private String dataReg = "";
	private String dataStipula = "";
	private String oggettoLocazione = "";
	private String tipoCanone = "";
	private String importoCanone = "";
	private String dataInizio = "";
	private String dataFine = "";

	public LocazioneAOggettoDTO() {
	}//-------------------------------------------------------------------------

	public String getUfficioReg() {
		return ufficioReg;
	}

	public void setUfficioReg(String ufficioReg) {
		this.ufficioReg = ufficioReg;
	}

	public String getAnnoReg() {
		return annoReg;
	}

	public void setAnnoReg(String annoReg) {
		this.annoReg = annoReg;
	}

	public String getSerieReg() {
		return serieReg;
	}

	public void setSerieReg(String serieReg) {
		this.serieReg = serieReg;
	}

	public String getNumeroReg() {
		return numeroReg;
	}

	public void setNumeroReg(String numeroReg) {
		this.numeroReg = numeroReg;
	}

	public String getSottonumeroReg() {
		return sottonumeroReg;
	}

	public void setSottonumeroReg(String sottonumeroReg) {
		this.sottonumeroReg = sottonumeroReg;
	}

	public String getProgressivoNegozio() {
		return progressivoNegozio;
	}

	public void setProgressivoNegozio(String progressivoNegozio) {
		this.progressivoNegozio = progressivoNegozio;
	}

	public String getCodiceNegozio() {
		return codiceNegozio;
	}

	public void setCodiceNegozio(String codiceNegozio) {
		this.codiceNegozio = codiceNegozio;
	}

	public String getDataReg() {
		return dataReg;
	}

	public void setDataReg(String dataReg) {
		this.dataReg = dataReg;
	}

	public String getDataStipula() {
		return dataStipula;
	}

	public void setDataStipula(String dataStipula) {
		this.dataStipula = dataStipula;
	}

	public String getOggettoLocazione() {
		return oggettoLocazione;
	}

	public void setOggettoLocazione(String oggettoLocazione) {
		this.oggettoLocazione = oggettoLocazione;
	}

	public String getTipoCanone() {
		return tipoCanone;
	}

	public void setTipoCanone(String tipoCanone) {
		this.tipoCanone = tipoCanone;
	}

	public String getImportoCanone() {
		return importoCanone;
	}

	public void setImportoCanone(String importoCanone) {
		this.importoCanone = importoCanone;
	}

	public String getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}

	public String getDataFine() {
		return dataFine;
	}

	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
