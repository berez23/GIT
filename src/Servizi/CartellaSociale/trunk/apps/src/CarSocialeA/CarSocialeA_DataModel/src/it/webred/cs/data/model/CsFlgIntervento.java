package it.webred.cs.data.model;

import it.webred.cs.data.base.ICsDDiarioChild;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the CS_FLG_INTERVENTO database table.
 * 
 */
@Entity
@Table(name="CS_FLG_INTERVENTO")
@NamedQuery(name="CsFlgIntervento.findAll", query="SELECT c FROM CsFlgIntervento c")
public class CsFlgIntervento implements ICsDDiarioChild {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DIARIO_ID")
	private Long diarioId;

	@Temporal(TemporalType.DATE)
	@Column(name="ATT_SOSP_C_AL")
	private Date attSospCAl;

	@Temporal(TemporalType.DATE)
	@Column(name="ATT_SOSP_C_DAL")
	private Date attSospCDal;

	@Column(name="COMP_CITTA")
	private String compCitta;

	@Column(name="COMP_DENOMINAZIONE")
	private String compDenominazione;

	@Column(name="COMP_INDIRIZZO")
	private String compIndirizzo;

	@Column(name="COMP_TEL")
	private String compTel;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_DOMANDA")
	private Date dataDomanda;

	@Column(name="DESCR_SOSPENSIONE")
	private String descrSospensione;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DT_INS")
	private Date dtIns;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Column(name="FLAG_ATT_SOSP_C")
	private String flagAttSospC;

	@Column(name="FLAG_RESPINTO")
	private BigDecimal flagRespinto;

	@Column(name="MOTIVO_RESPINTO")
	private String motivoRespinto;

	@Column(name="TIPO_ATTIVAZIONE")
	private String tipoAttivazione;

	@Column(name="USER_INS")
	private String userIns;

	@Column(name="USR_MOD")
	private String usrMod;

	//bi-directional many-to-one association to CsAComponente
	@ManyToOne
	@JoinColumn(name="COMPONENTE_ID")
	private CsAComponente csAComponente;

	//bi-directional one-to-one association to CsDDiario
	@OneToOne
	@JoinColumn(name="DIARIO_ID")
	private CsDDiario csDDiario;
	
	//bi-directional many-to-one association to CsIIntervento
	@ManyToOne
	@JoinColumn(name="INTERVENTO_ID")
	private CsIIntervento csIIntervento;

	//bi-directional many-to-one association to CsTbMotivoChiusuraInt
	@ManyToOne
	@JoinColumn(name="MOTIVO_CHIUSURA_INT_ID")
	private CsTbMotivoChiusuraInt csTbMotivoChiusuraInt;

	public CsFlgIntervento() {
	}

	public Long getDiarioId() {
		return this.diarioId;
	}

	public void setDiarioId(Long diarioId) {
		this.diarioId = diarioId;
	}

	public Date getAttSospCAl() {
		return this.attSospCAl;
	}

	public void setAttSospCAl(Date attSospCAl) {
		this.attSospCAl = attSospCAl;
	}

	public Date getAttSospCDal() {
		return this.attSospCDal;
	}

	public void setAttSospCDal(Date attSospCDal) {
		this.attSospCDal = attSospCDal;
	}

	public String getCompCitta() {
		return this.compCitta;
	}

	public void setCompCitta(String compCitta) {
		this.compCitta = compCitta;
	}

	public String getCompDenominazione() {
		return this.compDenominazione;
	}

	public void setCompDenominazione(String compDenominazione) {
		this.compDenominazione = compDenominazione;
	}

	public String getCompIndirizzo() {
		return this.compIndirizzo;
	}

	public void setCompIndirizzo(String compIndirizzo) {
		this.compIndirizzo = compIndirizzo;
	}

	public String getCompTel() {
		return this.compTel;
	}

	public void setCompTel(String compTel) {
		this.compTel = compTel;
	}

	public Date getDataDomanda() {
		return this.dataDomanda;
	}

	public void setDataDomanda(Date dataDomanda) {
		this.dataDomanda = dataDomanda;
	}

	public String getDescrSospensione() {
		return this.descrSospensione;
	}

	public void setDescrSospensione(String descrSospensione) {
		this.descrSospensione = descrSospensione;
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

	public String getFlagAttSospC() {
		return this.flagAttSospC;
	}

	public void setFlagAttSospC(String flagAttSospC) {
		this.flagAttSospC = flagAttSospC;
	}

	public BigDecimal getFlagRespinto() {
		return this.flagRespinto;
	}

	public void setFlagRespinto(BigDecimal flagRespinto) {
		this.flagRespinto = flagRespinto;
	}

	public String getMotivoRespinto() {
		return this.motivoRespinto;
	}

	public void setMotivoRespinto(String motivoRespinto) {
		this.motivoRespinto = motivoRespinto;
	}

	public String getTipoAttivazione() {
		return this.tipoAttivazione;
	}

	public void setTipoAttivazione(String tipoAttivazione) {
		this.tipoAttivazione = tipoAttivazione;
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

	public CsAComponente getCsAComponente() {
		return this.csAComponente;
	}

	public void setCsAComponente(CsAComponente csAComponente) {
		this.csAComponente = csAComponente;
	}

	public CsDDiario getCsDDiario() {
		return this.csDDiario;
	}

	public void setCsDDiario(CsDDiario csDDiario) {
		this.csDDiario = csDDiario;
	}

	public CsIIntervento getCsIIntervento() {
		return this.csIIntervento;
	}

	public void setCsIIntervento(CsIIntervento csIIntervento) {
		this.csIIntervento = csIIntervento;
	}

	public CsTbMotivoChiusuraInt getCsTbMotivoChiusuraInt() {
		return this.csTbMotivoChiusuraInt;
	}

	public void setCsTbMotivoChiusuraInt(CsTbMotivoChiusuraInt csTbMotivoChiusuraInt) {
		this.csTbMotivoChiusuraInt = csTbMotivoChiusuraInt;
	}

}