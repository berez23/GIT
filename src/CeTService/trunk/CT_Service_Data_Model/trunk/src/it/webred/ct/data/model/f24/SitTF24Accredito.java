package it.webred.ct.data.model.f24;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table(name="SIT_T_F24_ACCREDITO")
public class SitTF24Accredito implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="COD_ENTE")
	private String codEnte;

	@Column(name="COD_VALUTA")
	private String codValuta;

	@Column(name="CTR_HASH")
	private String ctrHash;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_BONIFICO")
	private Date dtBonifico;

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
	@Column(name="DT_FORNITURA")
	private Date dtFornitura;

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

	@Temporal(TemporalType.DATE)
	@Column(name="DT_RIPARTIZIONE")
	private Date dtRipartizione;

	@Column(name="FK_ENTE_SORGENTE")
	private BigDecimal fkEnteSorgente;

	@Column(name="FLAG_DT_VAL_DATO")
	private BigDecimal flagDtValDato;

	@Column(name="ID_EXT")
	private String idExt;

	@Column(name="ID_EXT_TESTATA")
	private String idExtTestata;

	@Column(name="ID_ORIG")
	private String idOrig;

	@Column(name="IMP_ACCREDITO")
	private BigDecimal impAccredito;

	private String processid;

	@Column(name="PROG_FORNITURA")
	private BigDecimal progFornitura;

	@Column(name="PROG_RIPARTIZIONE")
	private BigDecimal progRipartizione;

	@Column(name="TIPO_IMPOSTA")
	private String tipoImposta;

	public SitTF24Accredito() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCodEnte() {
		return this.codEnte;
	}

	public void setCodEnte(String codEnte) {
		this.codEnte = codEnte;
	}

	public String getCodValuta() {
		return this.codValuta;
	}

	public void setCodValuta(String codValuta) {
		this.codValuta = codValuta;
	}

	public String getCtrHash() {
		return this.ctrHash;
	}

	public void setCtrHash(String ctrHash) {
		this.ctrHash = ctrHash;
	}

	public Date getDtBonifico() {
		return this.dtBonifico;
	}

	public void setDtBonifico(Date dtBonifico) {
		this.dtBonifico = dtBonifico;
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

	public Date getDtFornitura() {
		return this.dtFornitura;
	}

	public void setDtFornitura(Date dtFornitura) {
		this.dtFornitura = dtFornitura;
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

	public Date getDtRipartizione() {
		return this.dtRipartizione;
	}

	public void setDtRipartizione(Date dtRipartizione) {
		this.dtRipartizione = dtRipartizione;
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

	public String getIdExt() {
		return this.idExt;
	}

	public void setIdExt(String idExt) {
		this.idExt = idExt;
	}

	public String getIdExtTestata() {
		return this.idExtTestata;
	}

	public void setIdExtTestata(String idExtTestata) {
		this.idExtTestata = idExtTestata;
	}

	public String getIdOrig() {
		return this.idOrig;
	}

	public void setIdOrig(String idOrig) {
		this.idOrig = idOrig;
	}

	public BigDecimal getImpAccredito() {
		return this.impAccredito;
	}

	public void setImpAccredito(BigDecimal impAccredito) {
		this.impAccredito = impAccredito;
	}

	public String getProcessid() {
		return this.processid;
	}

	public void setProcessid(String processid) {
		this.processid = processid;
	}

	public BigDecimal getProgFornitura() {
		return this.progFornitura;
	}

	public void setProgFornitura(BigDecimal progFornitura) {
		this.progFornitura = progFornitura;
	}

	public BigDecimal getProgRipartizione() {
		return this.progRipartizione;
	}

	public void setProgRipartizione(BigDecimal progRipartizione) {
		this.progRipartizione = progRipartizione;
	}

	public String getTipoImposta() {
		return this.tipoImposta;
	}

	public void setTipoImposta(String tipoImposta) {
		this.tipoImposta = tipoImposta;
	}

}