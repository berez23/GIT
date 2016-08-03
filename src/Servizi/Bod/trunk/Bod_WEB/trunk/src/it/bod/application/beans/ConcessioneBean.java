package it.bod.application.beans;

import it.bod.application.common.MasterItem;

public class ConcessioneBean extends MasterItem{

	private static final long serialVersionUID = -7769197374017571669L;
	
	private String foglio = "";
	private String particella = "";
	private String subalterno = "";
	
	private String protocollo = "";
	private String concessione = "";
	private String progressivo = "";
	
	private String oggetto = "";
	private String tipoIntervento = "";
	private String dataRilascio = "";
	private String dataFineLavori = "";
	private String posizione = "";

	private String dataProtocollo = null;
	private String codiceFiscale = "";
	private String partitaIva = "";
	private String denominazione = "";
	private String cognome = "";
	private String nome = "";
	private String tipoSoggetto = "";
	
	public String getProtocollo() {
		return protocollo;
	}
	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getPartitaIva() {
		return partitaIva;
	}
	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public String getTipoSoggetto() {
		return tipoSoggetto;
	}
	public void setTipoSoggetto(String tipoSogetto) {
		this.tipoSoggetto = tipoSogetto;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getDataProtocollo() {
		return dataProtocollo;
	}
	public void setDataProtocollo(String dataProtocolo) {
		this.dataProtocollo = dataProtocolo;
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
	public String getConcessione() {
		return concessione;
	}
	public void setConcessione(String concessione) {
		this.concessione = concessione;
	}
	public String getProgressivo() {
		return progressivo;
	}
	public void setProgressivo(String progressivo) {
		this.progressivo = progressivo;
	}
	public String getOggetto() {
		return oggetto;
	}
	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}
	public String getTipoIntervento() {
		return tipoIntervento;
	}
	public void setTipoIntervento(String tipoIntervento) {
		this.tipoIntervento = tipoIntervento;
	}
	public String getDataRilascio() {
		return dataRilascio;
	}
	public void setDataRilascio(String dataRilascio) {
		this.dataRilascio = dataRilascio;
	}
	public String getDataFineLavori() {
		return dataFineLavori;
	}
	public void setDataFineLavori(String dataFineLavori) {
		this.dataFineLavori = dataFineLavori;
	}
	public String getPosizione() {
		return posizione;
	}
	public void setPosizione(String posizione) {
		this.posizione = posizione;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	

}
