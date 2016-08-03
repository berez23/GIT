package it.webred.fb.data.model;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the DM_B_INFO database table.
 * 
 */
@Entity
@Table(name="DM_B_INFO")
@NamedQuery(name="DmBInfo.findAll", query="SELECT d FROM DmBInfo d")
public class DmBInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CTR_HASH")
	private String ctrHash;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_AGG")
	private Date dataAgg;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_CENSIMENTO")
	private Date dataCensimento;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INS")
	private Date dataIns;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_RIL")
	private Date dataRil;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_ELAB")
	private Date dtElab;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_FINE_VAL")
	private Date dtFineVal;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_INIZIO_VAL")
	private Date dtInizioVal;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Column(name="FL_ANTICO_POSSESSO")
	private String flAnticoPossesso;

	@Column(name="FL_CONGELATO")
	private String flCongelato;

	@Id
	private BigDecimal id;

	@Column(name="METRI_Q")
	private BigDecimal metriQ;

	@Column(name="NUM_BOX")
	private BigDecimal numBox;

	@Column(name="NUM_PIANI_F")
	private BigDecimal numPianiF;

	@Column(name="NUM_PIANI_I")
	private String numPianiI;

	@Column(name="NUM_VANI")
	private BigDecimal numVani;

	private String provenienza;

	private String qualita;

	@Column(name="QUOTA_PROPRIETA")
	private String quotaProprieta;

	@Column(name="REND_CATAS")
	private BigDecimal rendCatas;

	@Column(name="STATO_CENSIMENTO")
	private BigDecimal statoCensimento;

	@Column(name="SUP_CO_SE")
	private BigDecimal supCoSe;

	@Column(name="SUP_COMMERCIALE")
	private BigDecimal supCommerciale;

	@Column(name="SUP_COP")
	private BigDecimal supCop;

	@Column(name="SUP_LOC")
	private BigDecimal supLoc;

	@Column(name="SUP_OPER")
	private BigDecimal supOper;

	@Column(name="SUP_TOT")
	private BigDecimal supTot;

	@Column(name="SUP_TOT_SLP")
	private BigDecimal supTotSlp;

	private String tipo;

	@Column(name="TOT_ABITATIVA")
	private BigDecimal totAbitativa;

	@Column(name="TOT_UNITA")
	private String totUnita;

	@Column(name="TOT_UNITA_COMUNALI")
	private BigDecimal totUnitaComunali;

	@Column(name="TOT_USI_DIVERSI")
	private BigDecimal totUsiDiversi;

	@Column(name="VAL_ACQUISTO")
	private BigDecimal valAcquisto;

	@Column(name="VAL_CATASTALE")
	private BigDecimal valCatastale;

	@Column(name="VAL_INVENTARIALE")
	private BigDecimal valInventariale;

	@Column(name="VOLUME_F")
	private BigDecimal volumeF;

	@Column(name="VOLUME_I")
	private BigDecimal volumeI;

	@Column(name="VOLUME_TOT")
	private BigDecimal volumeTot;

	//bi-directional many-to-one association to DmBBene
	@ManyToOne
	@JoinColumn(name="DM_B_BENE_ID")
	private DmBBeneNOTEAGER dmBBene;

	public DmBInfo() {
	}

	public String getCtrHash() {
		return this.ctrHash;
	}

	public void setCtrHash(String ctrHash) {
		this.ctrHash = ctrHash;
	}

	public Date getDataAgg() {
		return this.dataAgg;
	}

	public void setDataAgg(Date dataAgg) {
		this.dataAgg = dataAgg;
	}

	public Date getDataCensimento() {
		return this.dataCensimento;
	}

	public void setDataCensimento(Date dataCensimento) {
		this.dataCensimento = dataCensimento;
	}

	public Date getDataIns() {
		return this.dataIns;
	}

	public void setDataIns(Date dataIns) {
		this.dataIns = dataIns;
	}

	public Date getDataRil() {
		return this.dataRil;
	}

	public void setDataRil(Date dataRil) {
		this.dataRil = dataRil;
	}

	public Date getDtElab() {
		return this.dtElab;
	}

	public void setDtElab(Date dtElab) {
		this.dtElab = dtElab;
	}

	public Date getDtFineVal() {
		return this.dtFineVal;
	}

	public void setDtFineVal(Date dtFineVal) {
		this.dtFineVal = dtFineVal;
	}

	public Date getDtInizioVal() {
		return this.dtInizioVal;
	}

	public void setDtInizioVal(Date dtInizioVal) {
		this.dtInizioVal = dtInizioVal;
	}

	public Date getDtMod() {
		return this.dtMod;
	}

	public void setDtMod(Date dtMod) {
		this.dtMod = dtMod;
	}

	public String getFlAnticoPossesso() {
		return this.flAnticoPossesso;
	}

	public void setFlAnticoPossesso(String flAnticoPossesso) {
		this.flAnticoPossesso = flAnticoPossesso;
	}

	public String getFlCongelato() {
		return this.flCongelato;
	}

	public void setFlCongelato(String flCongelato) {
		this.flCongelato = flCongelato;
	}

	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public BigDecimal getMetriQ() {
		return this.metriQ;
	}

	public void setMetriQ(BigDecimal metriQ) {
		this.metriQ = metriQ;
	}

	public BigDecimal getNumBox() {
		return this.numBox;
	}

	public void setNumBox(BigDecimal numBox) {
		this.numBox = numBox;
	}

	public BigDecimal getNumPianiF() {
		return this.numPianiF;
	}

	public void setNumPianiF(BigDecimal numPianiF) {
		this.numPianiF = numPianiF;
	}

	public String getNumPianiI() {
		return this.numPianiI;
	}

	public void setNumPianiI(String numPianiI) {
		this.numPianiI = numPianiI;
	}

	public BigDecimal getNumVani() {
		return this.numVani;
	}

	public void setNumVani(BigDecimal numVani) {
		this.numVani = numVani;
	}

	public String getProvenienza() {
		return this.provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public String getQualita() {
		return this.qualita;
	}

	public void setQualita(String qualita) {
		this.qualita = qualita;
	}

	public String getQuotaProprieta() {
		return this.quotaProprieta;
	}

	public void setQuotaProprieta(String quotaProprieta) {
		this.quotaProprieta = quotaProprieta;
	}

	public BigDecimal getRendCatas() {
		return this.rendCatas;
	}

	public void setRendCatas(BigDecimal rendCatas) {
		this.rendCatas = rendCatas;
	}

	public BigDecimal getStatoCensimento() {
		return this.statoCensimento;
	}

	public void setStatoCensimento(BigDecimal statoCensimento) {
		this.statoCensimento = statoCensimento;
	}

	public BigDecimal getSupCoSe() {
		return this.supCoSe;
	}

	public void setSupCoSe(BigDecimal supCoSe) {
		this.supCoSe = supCoSe;
	}

	public BigDecimal getSupCommerciale() {
		return this.supCommerciale;
	}

	public void setSupCommerciale(BigDecimal supCommerciale) {
		this.supCommerciale = supCommerciale;
	}

	public BigDecimal getSupCop() {
		return this.supCop;
	}

	public void setSupCop(BigDecimal supCop) {
		this.supCop = supCop;
	}

	public BigDecimal getSupLoc() {
		return this.supLoc;
	}

	public void setSupLoc(BigDecimal supLoc) {
		this.supLoc = supLoc;
	}

	public BigDecimal getSupOper() {
		return this.supOper;
	}

	public void setSupOper(BigDecimal supOper) {
		this.supOper = supOper;
	}

	public BigDecimal getSupTot() {
		return this.supTot;
	}

	public void setSupTot(BigDecimal supTot) {
		this.supTot = supTot;
	}

	public BigDecimal getSupTotSlp() {
		return this.supTotSlp;
	}

	public void setSupTotSlp(BigDecimal supTotSlp) {
		this.supTotSlp = supTotSlp;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getTotAbitativa() {
		return this.totAbitativa;
	}

	public void setTotAbitativa(BigDecimal totAbitativa) {
		this.totAbitativa = totAbitativa;
	}

	public String getTotUnita() {
		return this.totUnita;
	}

	public void setTotUnita(String totUnita) {
		this.totUnita = totUnita;
	}

	public BigDecimal getTotUnitaComunali() {
		return this.totUnitaComunali;
	}

	public void setTotUnitaComunali(BigDecimal totUnitaComunali) {
		this.totUnitaComunali = totUnitaComunali;
	}

	public BigDecimal getTotUsiDiversi() {
		return this.totUsiDiversi;
	}

	public void setTotUsiDiversi(BigDecimal totUsiDiversi) {
		this.totUsiDiversi = totUsiDiversi;
	}

	public BigDecimal getValAcquisto() {
		return this.valAcquisto;
	}

	public void setValAcquisto(BigDecimal valAcquisto) {
		this.valAcquisto = valAcquisto;
	}

	public BigDecimal getValCatastale() {
		return this.valCatastale;
	}

	public void setValCatastale(BigDecimal valCatastale) {
		this.valCatastale = valCatastale;
	}

	public BigDecimal getValInventariale() {
		return this.valInventariale;
	}

	public void setValInventariale(BigDecimal valInventariale) {
		this.valInventariale = valInventariale;
	}

	public BigDecimal getVolumeF() {
		return this.volumeF;
	}

	public void setVolumeF(BigDecimal volumeF) {
		this.volumeF = volumeF;
	}

	public BigDecimal getVolumeI() {
		return this.volumeI;
	}

	public void setVolumeI(BigDecimal volumeI) {
		this.volumeI = volumeI;
	}

	public BigDecimal getVolumeTot() {
		return this.volumeTot;
	}

	public void setVolumeTot(BigDecimal volumeTot) {
		this.volumeTot = volumeTot;
	}

	public DmBBeneNOTEAGER getDmBBene() {
		return dmBBene;
	}

	public void setDmBBene(DmBBeneNOTEAGER dmBBene) {
		this.dmBBene = dmBBene;
	}

}