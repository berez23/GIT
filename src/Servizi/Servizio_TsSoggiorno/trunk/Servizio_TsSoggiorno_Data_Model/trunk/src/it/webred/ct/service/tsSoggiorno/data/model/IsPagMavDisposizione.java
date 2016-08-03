package it.webred.ct.service.tsSoggiorno.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the IS_PAG_MAV_DISPOSIZIONE database table.
 * 
 */
@Entity
@Table(name="IS_PAG_MAV_DISPOSIZIONE")
public class IsPagMavDisposizione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="ID_FLUSSO")
	private String idFlusso;

	private Long progressivo;
	
	@Column(name="ABI_ASSUNTRICE")
	private BigDecimal abiAssuntrice;

	@Column(name="ABI_ESATTRICE")
	private BigDecimal abiEsattrice;

	@Column(name="CAB_ASSUNTRICE")
	private BigDecimal cabAssuntrice;

	@Column(name="CAB_ESATTRICE")
	private BigDecimal cabEsattrice;

	private String causale;

	@Column(name="CODICE_CREDITRICE")
	private String codiceCreditrice;

	@Column(name="CODICE_DEBITORE")
	private String codiceDebitore;

	@Column(name="CODICE_DIVISA")
	private String codiceDivisa;

	@Column(name="CODICE_IDENTIFICATIVO")
	private String codiceIdentificativo;

	private String conto;

	@Column(name="CTR_HASH")
	private String ctrHash;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_CONTABILE")
	private Date dataContabile;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_RND")
	private Date dataRnd;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_SCADENZA")
	private Date dataScadenza;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_VALUTA")
	private Date dataValuta;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_EXP_DATO")
	private Date dtExpDato;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_FINE_DATO")
	private Date dtFineDato;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_FINE_VAL")
	private Date dtFineVal;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_INIZIO_DATO")
	private Date dtInizioDato;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_INIZIO_VAL")
	private Date dtInizioVal;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Column(name="FK_ENTE_SORGENTE")
	private BigDecimal fkEnteSorgente;

	@Column(name="FLAG_DT_VAL_DATO")
	private BigDecimal flagDtValDato;

	@Id
	private String id;

	@Column(name="ID_EXT")
	private String idExt;

	@Column(name="ID_ORIG")
	private String idOrig;

	private String importo;

	@Column(name="IMPORTO_SPESE_COMM")
	private BigDecimal importoSpeseComm;

	private String processid;

	@Column(name="RIFERIMENTO_RIEPILOGO")
	private String riferimentoRiepilogo;

	@Column(name="TIPO_CONTO")
	private String tipoConto;

	@Column(name="TIPO_CREDITRICE")
	private BigDecimal tipoCreditrice;

	public IsPagMavDisposizione() {
	}

	public String getIdFlusso() {
		return idFlusso;
	}

	public void setIdFlusso(String idFlusso) {
		this.idFlusso = idFlusso;
	}

	public long getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(long progressivo) {
		this.progressivo = progressivo;
	}

	public BigDecimal getAbiAssuntrice() {
		return this.abiAssuntrice;
	}

	public void setAbiAssuntrice(BigDecimal abiAssuntrice) {
		this.abiAssuntrice = abiAssuntrice;
	}

	public BigDecimal getAbiEsattrice() {
		return this.abiEsattrice;
	}

	public void setAbiEsattrice(BigDecimal abiEsattrice) {
		this.abiEsattrice = abiEsattrice;
	}

	public BigDecimal getCabAssuntrice() {
		return this.cabAssuntrice;
	}

	public void setCabAssuntrice(BigDecimal cabAssuntrice) {
		this.cabAssuntrice = cabAssuntrice;
	}

	public BigDecimal getCabEsattrice() {
		return this.cabEsattrice;
	}

	public void setCabEsattrice(BigDecimal cabEsattrice) {
		this.cabEsattrice = cabEsattrice;
	}

	public String getCausale() {
		return this.causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public String getCodiceCreditrice() {
		return this.codiceCreditrice;
	}

	public void setCodiceCreditrice(String codiceCreditrice) {
		this.codiceCreditrice = codiceCreditrice;
	}

	public String getCodiceDebitore() {
		return this.codiceDebitore;
	}

	public void setCodiceDebitore(String codiceDebitore) {
		this.codiceDebitore = codiceDebitore;
	}

	public String getCodiceDivisa() {
		return this.codiceDivisa;
	}

	public void setCodiceDivisa(String codiceDivisa) {
		this.codiceDivisa = codiceDivisa;
	}

	public String getCodiceIdentificativo() {
		return this.codiceIdentificativo;
	}

	public void setCodiceIdentificativo(String codiceIdentificativo) {
		this.codiceIdentificativo = codiceIdentificativo;
	}

	public String getConto() {
		return this.conto;
	}

	public void setConto(String conto) {
		this.conto = conto;
	}

	public String getCtrHash() {
		return this.ctrHash;
	}

	public void setCtrHash(String ctrHash) {
		this.ctrHash = ctrHash;
	}

	public Date getDataContabile() {
		return this.dataContabile;
	}

	public void setDataContabile(Date dataContabile) {
		this.dataContabile = dataContabile;
	}

	public Date getDataRnd() {
		return this.dataRnd;
	}

	public void setDataRnd(Date dataRnd) {
		this.dataRnd = dataRnd;
	}

	public Date getDataScadenza() {
		return this.dataScadenza;
	}

	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public Date getDataValuta() {
		return this.dataValuta;
	}

	public void setDataValuta(Date dataValuta) {
		this.dataValuta = dataValuta;
	}

	public Date getDtExpDato() {
		return this.dtExpDato;
	}

	public void setDtExpDato(Date dtExpDato) {
		this.dtExpDato = dtExpDato;
	}

	public Date getDtFineDato() {
		return this.dtFineDato;
	}

	public void setDtFineDato(Date dtFineDato) {
		this.dtFineDato = dtFineDato;
	}

	public Date getDtFineVal() {
		return this.dtFineVal;
	}

	public void setDtFineVal(Date dtFineVal) {
		this.dtFineVal = dtFineVal;
	}

	public Date getDtInizioDato() {
		return this.dtInizioDato;
	}

	public void setDtInizioDato(Date dtInizioDato) {
		this.dtInizioDato = dtInizioDato;
	}

	public Date getDtInizioVal() {
		return this.dtInizioVal;
	}

	public void setDtInizioVal(Date dtInizioVal) {
		this.dtInizioVal = dtInizioVal;
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

	public BigDecimal getFkEnteSorgente() {
		return this.fkEnteSorgente;
	}

	public void setFkEnteSorgente(BigDecimal fkEnteSorgente) {
		this.fkEnteSorgente = fkEnteSorgente;
	}

	public BigDecimal getFlagDtValDato() {
		return this.flagDtValDato;
	}

	public void setFlagDtValDato(BigDecimal flagDtValDato) {
		this.flagDtValDato = flagDtValDato;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdExt() {
		return this.idExt;
	}

	public void setIdExt(String idExt) {
		this.idExt = idExt;
	}

	public String getIdOrig() {
		return this.idOrig;
	}

	public void setIdOrig(String idOrig) {
		this.idOrig = idOrig;
	}

	public String getImporto() {
		return this.importo;
	}

	public void setImporto(String importo) {
		this.importo = importo;
	}

	public BigDecimal getImportoSpeseComm() {
		return this.importoSpeseComm;
	}

	public void setImportoSpeseComm(BigDecimal importoSpeseComm) {
		this.importoSpeseComm = importoSpeseComm;
	}

	public String getProcessid() {
		return this.processid;
	}

	public void setProcessid(String processid) {
		this.processid = processid;
	}

	public String getRiferimentoRiepilogo() {
		return this.riferimentoRiepilogo;
	}

	public void setRiferimentoRiepilogo(String riferimentoRiepilogo) {
		this.riferimentoRiepilogo = riferimentoRiepilogo;
	}

	public String getTipoConto() {
		return this.tipoConto;
	}

	public void setTipoConto(String tipoConto) {
		this.tipoConto = tipoConto;
	}

	public BigDecimal getTipoCreditrice() {
		return this.tipoCreditrice;
	}

	public void setTipoCreditrice(BigDecimal tipoCreditrice) {
		this.tipoCreditrice = tipoCreditrice;
	}

}