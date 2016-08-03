package it.bod.application.beans;

import java.io.File;

import it.bod.application.common.MasterItem;

public class PlanimetriaBean  extends MasterItem{

	private static final long serialVersionUID = 5660079951843366520L;
	
	private File file = null;
	private String progressivo = "";
	private int formato;
	private boolean esiste = false;
	private String tipoOperazione = "";
	private String foglio = "";
	private String particella = "";
	private String subalterno = "";
	private String protocollo = "";
	private String fornitura = "";
	private String idImmobile = "";
	private Object oggetto = null;
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getProgressivo() {
		return progressivo;
	}
	public void setProgressivo(String progressivo) {
		this.progressivo = progressivo;
	}	
	public int getFormato() {
		return formato;
	}
	public void setFormato(int formato) {
		this.formato = formato;
	}
	public boolean isEsiste() {
		return esiste;
	}
	public void setEsiste(boolean esiste) {
		this.esiste = esiste;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getTipoOperazione() {
		return tipoOperazione;
	}
	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getIdImmobile() {
		return idImmobile;
	}
	public void setIdImmobile(String idImmobile) {
		this.idImmobile = idImmobile;
	}
	public String getProtocollo() {
		return protocollo;
	}
	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}
	public String getFornitura() {
		return fornitura;
	}
	public void setFornitura(String fornitura) {
		this.fornitura = fornitura;
	}
	public Object getOggetto() {
		return oggetto;
	}
	public void setOggetto(Object oggetto) {
		this.oggetto = oggetto;
	}


}
