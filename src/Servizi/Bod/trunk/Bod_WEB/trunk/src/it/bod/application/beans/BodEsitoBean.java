package it.bod.application.beans;

import java.math.BigDecimal;

import java.util.Date;

import it.bod.application.common.MasterItem;

public class BodEsitoBean extends MasterItem {

	private static final long serialVersionUID = -3018373531046222042L;
	
	private Date dataUpload = null;
	private String nomeFileZip = "";
	private String nomeFileEsito = "";
	private String comune = "";
	private String uiuProtocolloReg = "";
	private String uiuSezioneUrbana = "";
	private String uiuFoglio = "";
	private String uiuNumero = "";
	private String uiuDenominatore = "";
	private String uiuSubalterno = "";
	private Boolean incConsistenza = false;
	private Boolean incDestinazione = false;
	private Boolean incClassamento = false;
	private Boolean incPlanimetria = false;
	private String esito = "";
	private String datZona = "";
	private String datCategoria = "";
	private String datClasse = "";
	private String datConsistenza = "";
	private String datFlagClassamento = "";
	private String datSuperficie = "";
	private BigDecimal datRenditaInEuro = null;
	private String datRiepilogo = "";

	public BodEsitoBean() {
		
	}//-------------------------------------------------------------------------

	public Date getDataUpload() {
		return dataUpload;
	}

	public void setDataUpload(Date dataUpload) {
		this.dataUpload = dataUpload;
	}

	public String getNomeFileZip() {
		return nomeFileZip;
	}

	public void setNomeFileZip(String nomeFileZip) {
		this.nomeFileZip = nomeFileZip;
	}

	public String getNomeFileEsito() {
		return nomeFileEsito;
	}

	public void setNomeFileEsito(String nomeFileEsito) {
		this.nomeFileEsito = nomeFileEsito;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getUiuProtocolloReg() {
		return uiuProtocolloReg;
	}

	public void setUiuProtocolloReg(String uiuProtocolloReg) {
		this.uiuProtocolloReg = uiuProtocolloReg;
	}

	public String getUiuSezioneUrbana() {
		return uiuSezioneUrbana;
	}

	public void setUiuSezioneUrbana(String uiuSezioneUrbana) {
		this.uiuSezioneUrbana = uiuSezioneUrbana;
	}

	public String getUiuFoglio() {
		return uiuFoglio;
	}

	public void setUiuFoglio(String uiuFoglio) {
		this.uiuFoglio = uiuFoglio;
	}

	public String getUiuNumero() {
		return uiuNumero;
	}

	public void setUiuNumero(String uiuNumero) {
		this.uiuNumero = uiuNumero;
	}

	public String getUiuDenominatore() {
		return uiuDenominatore;
	}

	public void setUiuDenominatore(String uiuDenominatore) {
		this.uiuDenominatore = uiuDenominatore;
	}

	public String getUiuSubalterno() {
		return uiuSubalterno;
	}

	public void setUiuSubalterno(String uiuSubalterno) {
		this.uiuSubalterno = uiuSubalterno;
	}

	public Boolean getIncConsistenza() {
		return incConsistenza;
	}

	public void setIncConsistenza(Boolean incConsistenza) {
		this.incConsistenza = incConsistenza;
	}

	public Boolean getIncDestinazione() {
		return incDestinazione;
	}

	public void setIncDestinazione(Boolean incDestinazione) {
		this.incDestinazione = incDestinazione;
	}

	public Boolean getIncClassamento() {
		return incClassamento;
	}

	public void setIncClassamento(Boolean incClassamento) {
		this.incClassamento = incClassamento;
	}

	public Boolean getIncPlanimetria() {
		return incPlanimetria;
	}

	public void setIncPlanimetria(Boolean incPlanimetria) {
		this.incPlanimetria = incPlanimetria;
	}

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public String getDatZona() {
		return datZona;
	}

	public void setDatZona(String datZona) {
		this.datZona = datZona;
	}

	public String getDatCategoria() {
		return datCategoria;
	}

	public void setDatCategoria(String datCategoria) {
		this.datCategoria = datCategoria;
	}

	public String getDatClasse() {
		return datClasse;
	}

	public void setDatClasse(String datClasse) {
		this.datClasse = datClasse;
	}

	public String getDatConsistenza() {
		return datConsistenza;
	}

	public void setDatConsistenza(String datConsistenza) {
		this.datConsistenza = datConsistenza;
	}

	public String getDatFlagClassamento() {
		return datFlagClassamento;
	}

	public void setDatFlagClassamento(String datFlagClassamento) {
		this.datFlagClassamento = datFlagClassamento;
	}

	public String getDatSuperficie() {
		return datSuperficie;
	}

	public void setDatSuperficie(String datSuperficie) {
		this.datSuperficie = datSuperficie;
	}

	public String getDatRiepilogo() {
		return datRiepilogo;
	}

	public void setDatRiepilogo(String datRiepilogo) {
		this.datRiepilogo = datRiepilogo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BigDecimal getDatRenditaInEuro() {
		return datRenditaInEuro;
	}

	public void setDatRenditaInEuro(BigDecimal datRenditaInEuro) {
		this.datRenditaInEuro = datRenditaInEuro;
	}

	

}
