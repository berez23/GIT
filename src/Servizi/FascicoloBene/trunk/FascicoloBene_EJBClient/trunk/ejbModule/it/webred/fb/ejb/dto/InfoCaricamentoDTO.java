package it.webred.fb.ejb.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class InfoCaricamentoDTO extends CeTBaseObject {

	private String tabella;
	private String descrizione;
	private Timestamp dtElab;
	
	private String tipoCaricamento;
	
	private String provenienza;
	private String tipoBene;
	private BigDecimal numAttivi;
	
	//Da caricare
	private BigDecimal numNuovi;
	private BigDecimal numRimossi;
	private BigDecimal numModificati;
	
	public String getTabella() {
		return tabella;
	}
	public void setTabella(String tabella) {
		this.tabella = tabella;
	}
	public String getProvenienza() {
		return provenienza;
	}
	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}
	public String getTipoBene() {
		return tipoBene;
	}
	public void setTipoBene(String tipoBene) {
		this.tipoBene = tipoBene;
	}
	public BigDecimal getNumAttivi() {
		return numAttivi;
	}
	public void setNumAttivi(BigDecimal numAttivi) {
		this.numAttivi = numAttivi;
	}
	public BigDecimal getNumNuovi() {
		return numNuovi;
	}
	public void setNumNuovi(BigDecimal numNuovi) {
		this.numNuovi = numNuovi;
	}
	public BigDecimal getNumRimossi() {
		return numRimossi;
	}
	public void setNumRimossi(BigDecimal numRimossi) {
		this.numRimossi = numRimossi;
	}
	public BigDecimal getNumModificati() {
		return numModificati;
	}
	public void setNumModificati(BigDecimal numModificati) {
		this.numModificati = numModificati;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public Timestamp getDtElab() {
		return dtElab;
	}
	public void setDtElab(Timestamp dtElab) {
		this.dtElab = dtElab;
	}
	public String getTipoCaricamento() {
		return tipoCaricamento;
	}
	public void setTipoCaricamento(String tipoCaricamento) {
		this.tipoCaricamento = tipoCaricamento;
	}

}
