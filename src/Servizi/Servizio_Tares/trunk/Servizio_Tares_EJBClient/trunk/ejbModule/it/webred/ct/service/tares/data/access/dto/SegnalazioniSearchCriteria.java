package it.webred.ct.service.tares.data.access.dto;

public class SegnalazioniSearchCriteria extends CriteriaBase{

	private static final long serialVersionUID = -316562881521897458L;
	
	private String sezioneCensuaria = "";
	private String identificativoImmobile = "";
	private String progressivo = "";
	private String tipoRecord = "";
	private String sezioneUrbana = "";
	private String foglio = "";
	private String numero = "";
	private String denominatore = "";
	private String subalterno = "";
	private Boolean esportata = null;
	private Boolean scaricata = null;
	private Long id = null;
	private Long segnala1Id = null;
	private Long segnalazioneId = null;

	public SegnalazioniSearchCriteria() {

	}//-------------------------------------------------------------------------


	public String getSezioneCensuaria() {
		return sezioneCensuaria;
	}


	public void setSezioneCensuaria(String sezioneCensuaria) {
		this.sezioneCensuaria = sezioneCensuaria;
	}


	public String getIdentificativoImmobile() {
		return identificativoImmobile;
	}


	public void setIdentificativoImmobile(String identificativoImmobile) {
		this.identificativoImmobile = identificativoImmobile;
	}


	public String getProgressivo() {
		return progressivo;
	}


	public void setProgressivo(String progressivo) {
		this.progressivo = progressivo;
	}


	public String getTipoRecord() {
		return tipoRecord;
	}


	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}


	public String getSezioneUrbana() {
		return sezioneUrbana;
	}


	public void setSezioneUrbana(String sezioneUrbana) {
		this.sezioneUrbana = sezioneUrbana;
	}


	public String getFoglio() {
		return foglio;
	}


	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}


	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}


	public String getDenominatore() {
		return denominatore;
	}


	public void setDenominatore(String denominatore) {
		this.denominatore = denominatore;
	}


	public String getSubalterno() {
		return subalterno;
	}


	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}


	public Boolean getEsportata() {
		return esportata;
	}


	public void setEsportata(Boolean esportata) {
		this.esportata = esportata;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public Boolean getScaricata() {
		return scaricata;
	}


	public void setScaricata(Boolean scaricata) {
		this.scaricata = scaricata;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getSegnala1Id() {
		return segnala1Id;
	}


	public void setSegnala1Id(Long segnala1Id) {
		this.segnala1Id = segnala1Id;
	}


	public Long getSegnalazioneId() {
		return segnalazioneId;
	}


	public void setSegnalazioneId(Long segnalazioneId) {
		this.segnalazioneId = segnalazioneId;
	}
	
	

}
