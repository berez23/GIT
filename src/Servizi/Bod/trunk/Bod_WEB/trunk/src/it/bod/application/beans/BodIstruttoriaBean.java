package it.bod.application.beans;

import java.util.Date;

import java.util.Set;

import it.bod.application.common.MasterItem;

public class BodIstruttoriaBean extends MasterItem{

	private static final long serialVersionUID = -2981046570313146818L;
	
	private Long idIst = new Long(0);
	private String protocollo = "";
	private Date fornitura = null;
	private Boolean presaInCarico = false;
	private Boolean esitoIst662 = false;
	private Boolean esitoIst80 = false;
	private Boolean esitoIst311 = false;
	private Boolean esitoIstNA = false;
	private Boolean chiusa = false;
	private String operatore = "";
	private String annotazioni = "";
	private String ruolo = "";
	private Boolean generato = false;
	private Set<BodSegnalazioneBean> segnalazioni = null;
	private Set<BodAllegatiBean> allegati = null;
	
	private BodModello662Bean modello662 = null;
	private BodModello80Bean modello80 = null;
	
	private Boolean segnalazioneDisponibile = false;
	
	public Long getIdIst() {
		return idIst;
	}
	public void setIdIst(Long idIst) {
		this.idIst = idIst;
	}
	public String getProtocollo() {
		return protocollo;
	}
	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}
	public String getOperatore() {
		return operatore;
	}
	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}
	public String getRuolo() {
		return ruolo;
	}
	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public Boolean getEsitoIstNA() {
		return esitoIstNA;
	}
	public void setEsitoIstNA(Boolean esitoIstNA) {
		this.esitoIstNA = esitoIstNA;
	}
	public Boolean getPresaInCarico() {
		return presaInCarico;
	}
	public void setPresaInCarico(Boolean presaInCarico) {
		this.presaInCarico = presaInCarico;
	}
	public Boolean getEsitoIst662() {
		return esitoIst662;
	}
	public void setEsitoIst662(Boolean esitoIst662) {
		this.esitoIst662 = esitoIst662;
	}
	public Boolean getEsitoIst80() {
		return esitoIst80;
	}
	public void setEsitoIst80(Boolean esitoIst80) {
		this.esitoIst80 = esitoIst80;
	}
	public Boolean getEsitoIst311() {
		return esitoIst311;
	}
	public void setEsitoIst311(Boolean esitoIst311) {
		this.esitoIst311 = esitoIst311;
	}
	public Boolean getChiusa() {
		return chiusa;
	}
	public void setChiusa(Boolean chiusa) {
		this.chiusa = chiusa;
	}
	public String getAnnotazioni() {
		return annotazioni;
	}
	public void setAnnotazioni(String annotazioni) {
		this.annotazioni = annotazioni;
	}
	public Date getFornitura() {
		return fornitura;
	}
	public void setFornitura(Date fornitura) {
		this.fornitura = fornitura;
	}
	public Set<BodSegnalazioneBean> getSegnalazioni() {
		return segnalazioni;
	}
	public void setSegnalazioni(Set<BodSegnalazioneBean> segnalazioni) {
		this.segnalazioni = segnalazioni;
	}
	public Boolean getGenerato() {
		return generato;
	}
	public void setGenerato(Boolean generato) {
		this.generato = generato;
	}
	public BodModello662Bean getModello662() {
		return modello662;
	}
	public void setModello662(BodModello662Bean modello662) {
		this.modello662 = modello662;
	}
	public Set<BodAllegatiBean> getAllegati() {
		return allegati;
	}
	public void setAllegati(Set<BodAllegatiBean> allegati) {
		this.allegati = allegati;
	}
	public BodModello80Bean getModello80() {
		return modello80;
	}
	public void setModello80(BodModello80Bean modello80) {
		this.modello80 = modello80;
	}
	public Boolean getSegnalazioneDisponibile() {
		return segnalazioneDisponibile = segnalazioni.size()>0;
	}
	
}
