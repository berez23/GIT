package it.escsolution.escwebgis.concessioni.bean;
import java.io.Serializable;


public class ConcessioniINFORMOggetti  implements Serializable
{
	private String foglio;
	private String particella;
	private String subalterno;
	private String indirizzo;
	private String civico; 
	private String scala;
	private String piano;
	private String destinazioneUso;
	private String vincoloSoprintendenza;
	private String dataFineACatasto;
	private String codEnte;
	
	public String getCodEnte() {
		return codEnte;
	}
	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}
	public String getDataFineACatasto() {
		return dataFineACatasto;
	}
	public void setDataFineACatasto(String dataFineACatasto) {
		this.dataFineACatasto = dataFineACatasto;
	}
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
	public String getScala() {
		return scala;
	}
	public void setScala(String scala) {
		this.scala = scala;
	}
	public String getPiano() {
		return piano;
	}
	public void setPiano(String piano) {
		this.piano = piano;
	}

	public String getDestinazioneUso() {
		return destinazioneUso;
	}
	public void setDestinazioneUso(String destinazioneUso) {
		this.destinazioneUso = destinazioneUso;
	}
	public String getVincoloSoprintendenza() {
		return vincoloSoprintendenza;
	}
	public void setVincoloSoprintendenza(String vincoloSoprintendenza) {
		this.vincoloSoprintendenza = vincoloSoprintendenza;
	}

}
