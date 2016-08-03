package it.webred.ct.service.tares.data.model;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


@Entity
@Table(name="SETAR_SEGNALA_2")
public class SetarSegnala2 extends BaseItem{

	private static final long serialVersionUID = 8266031629767496500L;
	
	private Long id = null;
	
	private String codiceAmministrativo = "";
	private String sezioneCensuaria = "";
	private String identificativoImmobile = "";
	private String progressivo = "";
	private String tipoRecord = "";
	
	private String ambiente = "";
	private String superficieAmbiente = "";
	private String altezza = "";
	private String altezzaMax = "";
	private Long segnala1Id = null;
	private SetarSegnala1 setarSegnala1 = null;
	private Long segnalazioneId = null;
	private Date dataInserimento = null;

	public SetarSegnala2() {

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

	@Column(name="AMBIENTE")
	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	@Column(name="SUPERFICIE_AMBIENTE")
	public String getSuperficieAmbiente() {
		return superficieAmbiente;
	}

	public void setSuperficieAmbiente(String superficieAmbiente) {
		this.superficieAmbiente = superficieAmbiente;
	}

	@Column(name="ALTEZZA")
	public String getAltezza() {
		return altezza;
	}

	public void setAltezza(String altezza) {
		this.altezza = altezza;
	}

	@Column(name="ALTEZZA_MAX")
	public String getAltezzaMax() {
		return altezzaMax;
	}

	public void setAltezzaMax(String altezzaMax) {
		this.altezzaMax = altezzaMax;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Transient
	public SetarSegnala1 getSetarSegnala1() {
		return setarSegnala1;
	}

	public void setSetarSegnala1(SetarSegnala1 setarSegnala1) {
		this.setarSegnala1 = setarSegnala1;
	}

	@Column(name="DATA_INSERIMENTO")
	public Date getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	@Column(name="FK_SEGNALA_1")
	public Long getSegnala1Id() {
		return segnala1Id;
	}

	public void setSegnala1Id(Long segnala1Id) {
		this.segnala1Id = segnala1Id;
	}

	@Column(name="FK_SEGNALAZIONE")
	public Long getSegnalazioneId() {
		return segnalazioneId;
	}

	public void setSegnalazioneId(Long segnalazioneId) {
		this.segnalazioneId = segnalazioneId;
	}
	
	


}
