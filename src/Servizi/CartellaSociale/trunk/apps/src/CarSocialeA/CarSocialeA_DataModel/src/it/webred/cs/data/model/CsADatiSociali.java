package it.webred.cs.data.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the CS_A_DATI_SOCIALI database table.
 * 
 */
@Entity
@Table(name="CS_A_DATI_SOCIALI")
@NamedQuery(name="CsADatiSociali.findAll", query="SELECT c FROM CsADatiSociali c")
public class CsADatiSociali implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CS_A_DATI_SOCIALI_ID_GENERATOR", sequenceName="SQ_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CS_A_DATI_SOCIALI_ID_GENERATOR")
	private Long id;

	@Column(name="TIPOLOGIA_FAMILIARE_ID")
	private BigDecimal tipologiaFamiliareId;
	
	@Column(name="PROBLEMATICA_ID")
	private BigDecimal problematicaId;
	
	@Column(name="STESURA_RELAZIONI_PER_ID")
	private BigDecimal stesuraRelazioniPerId;
	
	@Column(name="INTERVENTI_SU_NUCLEO")
	private String interventiSuNucleo;
	
	@Column(name="TITOLO_STUDIO_ID")
	private BigDecimal titoloStudioId;
	
	@Column(name="PROFESSIONE_ID")
	private BigDecimal professioneId;
	
	@Column(name="SETT_IMPIEGO_ID")
	private BigDecimal settImpiegoId;
	
	@Column(name="TIPO_CONTRATTO_ID")
	private BigDecimal tipoContrattoId;
	
	@Column(name="TUTELA_ID")
	private BigDecimal tutelaId;
	
	@Column(name="TUTELANTE_ID")
	private BigDecimal tutelanteId;
	
	@Column(name="N_MINORI")
	private BigDecimal nMinori;
	
	@Column(name="INVIANTE_ID")
	private BigDecimal invianteId;
	
	@Column(name="INVIATO_A_ID")
	private BigDecimal inviatoAId;
	
	private String cps;
	private String noa;
	private String sert;
	private String autosufficienza;
	private String patologia;
	
	@Column(name="TIPO_INTERVENTI")
	private String tipoInterventi;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATA_FINE_APP")
	private Date dataFineApp;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_FINE_SYS")
	private Date dataFineSys;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INIZIO_APP")
	private Date dataInizioApp;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INIZIO_SYS")
	private Date dataInizioSys;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DT_INS")
	private Date dtIns;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Column(name="MOTIVO_FINE")
	private String motivoFine;

	@Column(name="MOTIVO_INIZIO")
	private String motivoInizio;

	@Column(name="USER_INS")
	private String userIns;

	@Column(name="USR_MOD")
	private String usrMod;

	//bi-directional many-to-one association to CsASoggetto
	@ManyToOne
	@JoinColumn(name="SOGGETTO_ANAGRAFICA_ID")
	private CsASoggetto csASoggetto;

	public CsADatiSociali() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataFineApp() {
		return this.dataFineApp;
	}

	public void setDataFineApp(Date dataFineApp) {
		this.dataFineApp = dataFineApp;
	}

	public Date getDataFineSys() {
		return this.dataFineSys;
	}

	public void setDataFineSys(Date dataFineSys) {
		this.dataFineSys = dataFineSys;
	}

	public Date getDataInizioApp() {
		return this.dataInizioApp;
	}

	public void setDataInizioApp(Date dataInizioApp) {
		this.dataInizioApp = dataInizioApp;
	}

	public Date getDataInizioSys() {
		return this.dataInizioSys;
	}

	public void setDataInizioSys(Date dataInizioSys) {
		this.dataInizioSys = dataInizioSys;
	}

	public Date getDtIns() {
		return this.dtIns;
	}

	public void setDtIns(Date dtIns) {
		this.dtIns = dtIns;
	}

	public Date getDtMod() {
		return this.dtMod;
	}

	public void setDtMod(Date dtMod) {
		this.dtMod = dtMod;
	}

	public String getInterventiSuNucleo() {
		return this.interventiSuNucleo;
	}

	public void setInterventiSuNucleo(String interventiSuNucleo) {
		this.interventiSuNucleo = interventiSuNucleo;
	}

	public String getMotivoFine() {
		return this.motivoFine;
	}

	public void setMotivoFine(String motivoFine) {
		this.motivoFine = motivoFine;
	}

	public String getMotivoInizio() {
		return this.motivoInizio;
	}

	public void setMotivoInizio(String motivoInizio) {
		this.motivoInizio = motivoInizio;
	}

	public String getUserIns() {
		return this.userIns;
	}

	public void setUserIns(String userIns) {
		this.userIns = userIns;
	}

	public String getUsrMod() {
		return this.usrMod;
	}

	public void setUsrMod(String usrMod) {
		this.usrMod = usrMod;
	}

	public CsASoggetto getCsASoggetto() {
		return this.csASoggetto;
	}

	public void setCsASoggetto(CsASoggetto csASoggetto) {
		this.csASoggetto = csASoggetto;
	}

	public BigDecimal getTipologiaFamiliareId() {
		return tipologiaFamiliareId;
	}

	public void setTipologiaFamiliareId(BigDecimal tipologiaFamiliareId) {
		this.tipologiaFamiliareId = tipologiaFamiliareId;
	}

	public BigDecimal getProblematicaId() {
		return problematicaId;
	}

	public void setProblematicaId(BigDecimal problematicaId) {
		this.problematicaId = problematicaId;
	}

	public BigDecimal getStesuraRelazioniPerId() {
		return stesuraRelazioniPerId;
	}

	public void setStesuraRelazioniPerId(BigDecimal stesuraRelazioniPerId) {
		this.stesuraRelazioniPerId = stesuraRelazioniPerId;
	}

	public BigDecimal getTitoloStudioId() {
		return titoloStudioId;
	}

	public void setTitoloStudioId(BigDecimal titoloStudioId) {
		this.titoloStudioId = titoloStudioId;
	}

	public BigDecimal getProfessioneId() {
		return professioneId;
	}

	public void setProfessioneId(BigDecimal professioneId) {
		this.professioneId = professioneId;
	}

	public BigDecimal getTutelaId() {
		return tutelaId;
	}

	public void setTutelaId(BigDecimal tutelaId) {
		this.tutelaId = tutelaId;
	}

	public BigDecimal getTutelanteId() {
		return tutelanteId;
	}

	public void setTutelanteId(BigDecimal tutelanteId) {
		this.tutelanteId = tutelanteId;
	}

	public BigDecimal getnMinori() {
		return nMinori;
	}

	public void setnMinori(BigDecimal nMinori) {
		this.nMinori = nMinori;
	}

	public BigDecimal getInvianteId() {
		return invianteId;
	}

	public void setInvianteId(BigDecimal invianteId) {
		this.invianteId = invianteId;
	}

	public BigDecimal getInviatoAId() {
		return inviatoAId;
	}

	public void setInviatoAId(BigDecimal inviatoAId) {
		this.inviatoAId = inviatoAId;
	}

	public String getCps() {
		return cps;
	}

	public void setCps(String cps) {
		this.cps = cps;
	}

	public String getTipoInterventi() {
		return tipoInterventi;
	}

	public void setTipoInterventi(String tipoInterventi) {
		this.tipoInterventi = tipoInterventi;
	}

	public String getNoa() {
		return noa;
	}

	public void setNoa(String noa) {
		this.noa = noa;
	}

	public String getSert() {
		return sert;
	}

	public void setSert(String sert) {
		this.sert = sert;
	}

	public String getAutosufficienza() {
		return autosufficienza;
	}

	public void setAutosufficienza(String autosufficienza) {
		this.autosufficienza = autosufficienza;
	}

	public BigDecimal getSettImpiegoId() {
		return settImpiegoId;
	}

	public void setSettImpiegoId(BigDecimal settImpiegoId) {
		this.settImpiegoId = settImpiegoId;
	}

	public BigDecimal getTipoContrattoId() {
		return tipoContrattoId;
	}

	public void setTipoContrattoId(BigDecimal tipoContrattoId) {
		this.tipoContrattoId = tipoContrattoId;
	}

	public String getPatologia() {
		return patologia;
	}

	public void setPatologia(String patologia) {
		this.patologia = patologia;
	}

}