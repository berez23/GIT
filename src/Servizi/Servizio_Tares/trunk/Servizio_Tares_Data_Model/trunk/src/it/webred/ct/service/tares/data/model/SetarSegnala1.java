package it.webred.ct.service.tares.data.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="SETAR_SEGNALA_1")
public class SetarSegnala1 extends BaseItem{

	private static final long serialVersionUID = -7162342031675766641L;
	
	private Long id = null;
	
	private String codiceAmministrativo = "";
	private String sezioneCensuaria = "";
	private String identificativoImmobile = "";
	private String progressivo = "";
	private String tipoRecord = "";

	private String sezioneUrbana = "";
	private String foglio = "";
	private String numero = "";
	private String denominatore = "";
	private String subalterno = "";
	private String superficieTotale = "";
	private String superficie = "";
	private String note = "";
	private Date dataInserimento = null;
	private Boolean superficieTotaleCalcolata = false;
	private Boolean esportata = false;
	private Long segnalazioneId = null;
	private String difforme = "";
	
	public SetarSegnala1() {
		
	}//-------------------------------------------------------------------------

	@Id
	@Column(name="ID")
	@SequenceGenerator(name="SeqSetar", sequenceName="SEQ_SETAR")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SeqSetar")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="CODICE_AMMINISTRATIVO")
	public String getCodiceAmministrativo() {
		return codiceAmministrativo;
	}

	public void setCodiceAmministrativo(String codiceAmministrativo) {
		this.codiceAmministrativo = codiceAmministrativo;
	}

	@Column(name="SEZIONE_CENSUARIA")
	public String getSezioneCensuaria() {
		return sezioneCensuaria;
	}

	public void setSezioneCensuaria(String sezioneCensuaria) {
		this.sezioneCensuaria = sezioneCensuaria;
	}

	@Column(name="IDENTIFICATIVO_IMMOBILE")
	public String getIdentificativoImmobile() {
		return identificativoImmobile;
	}

	public void setIdentificativoImmobile(String identificativoImmobile) {
		this.identificativoImmobile = identificativoImmobile;
	}

	@Column(name="PROGRESSIVO")
	public String getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(String progressivo) {
		this.progressivo = progressivo;
	}

	@Column(name="TIPO_RECORD")
	public String getTipoRecord() {
		return tipoRecord;
	}

	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}

	@Column(name="SEZIONE_URBANA")
	public String getSezioneUrbana() {
		return sezioneUrbana;
	}

	public void setSezioneUrbana(String sezioneUrbana) {
		this.sezioneUrbana = sezioneUrbana;
	}

	@Column(name="FOGLIO")
	public String getFoglio() {
		return foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	@Column(name="NUMERO")
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name="DENOMINATORE")
	public String getDenominatore() {
		return denominatore;
	}

	public void setDenominatore(String denominatore) {
		this.denominatore = denominatore;
	}

	@Column(name="SUBALTERNO")
	public String getSubalterno() {
		return subalterno;
	}

	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}

	@Column(name="SUPERFICIE_TOTALE")
	public String getSuperficieTotale() {
		return superficieTotale;
	}

	public void setSuperficieTotale(String superficieTotale) {
		this.superficieTotale = superficieTotale;
	}

	@Column(name="SUPERFICIE")
	public String getSuperficie() {
		return superficie;
	}

	public void setSuperficie(String superficie) {
		this.superficie = superficie;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Column(name="NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name="DATA_INSERIMENTO")
	public Date getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	@Column(name="SUPERFICIE_TOTALE_CALCOLATA")
	public Boolean getSuperficieTotaleCalcolata() {
		return superficieTotaleCalcolata;
	}

	public void setSuperficieTotaleCalcolata(Boolean superficieTotaleCalcolata) {
		this.superficieTotaleCalcolata = superficieTotaleCalcolata;
	}

	@Column(name="ESPORTATA")
	public Boolean getEsportata() {
		return esportata;
	}

	public void setEsportata(Boolean esportata) {
		this.esportata = esportata;
	}

	@Column(name="FK_SEGNALAZIONE")
	public Long getSegnalazioneId() {
		return segnalazioneId;
	}

	public void setSegnalazioneId(Long segnalazioneId) {
		this.segnalazioneId = segnalazioneId;
	}

	public String getDifforme() {
		return difforme;
	}

	public void setDifforme(String difforme) {
		this.difforme = difforme;
	}

	
	

}
