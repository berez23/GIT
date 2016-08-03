package it.webred.cs.data.model;

import it.webred.cs.data.base.ICsDDiarioChild;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the CS_I_INTERVENTO_ESEG database table.
 * 
 */
@Entity
@Table(name="CS_I_INTERVENTO_ESEG")
@NamedQuery(name="CsIInterventoEseg.findAll", query="SELECT c FROM CsIInterventoEseg c")
public class CsIInterventoEseg implements ICsDDiarioChild {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DIARIO_ID")
	private Long diarioId;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_EROGAZIONE_AL")
	private Date dataErogazioneAl;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_EROGAZIONE_DAL")
	private Date dataErogazioneDal;

	@Column(name="DESCRIZIONE_INTERVENTO")
	private String descrizioneIntervento;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Column(name="INTERVENTO_ID")
	private CsIIntervento csIIntervento;

	@Column(name="N_ALTRI_INTERVENTI_PREVISTI")
	private BigDecimal nAltriInterventiPrevisti;

	//bi-directional many-to-one association to CsDRelazione
	@ManyToOne
	@JoinColumn(name="OPERATORE_SETTORE_ID")
	private CsOOperatoreSettore csOOperatoreSettore;

	@Column(name="ORGANIZZAZIONE_ID")
	private CsOOrganizzazione csOOrganizzazione;

	@Column(name="ORGANIZZAZIONE_ID_COINV")
	private CsOOrganizzazione csOOrganizzazioneCoinv;

	@Column(name="SETTORE_ID")
	private CsOSettore csOSettore;

	@Column(name="TIPO_INTERVENTO_ID")
	private CsCTipoIntervento csCTipoIntervento;

	@Column(name="USER_INS")
	private String userIns;

	@Column(name="USR_MOD")
	private String usrMod;

	private String valore;

	@Column(name="VALORE_PARTEC_ALTRA_ORG")
	private String valorePartecAltraOrg;

	@Column(name="VALORE_UTENTE")
	private String valoreUtente;

	//bi-directional one-to-one association to CsDDiario
	@OneToOne
	@JoinColumn(name="DIARIO_ID")
	private CsDDiario csDDiario;

	//bi-directional many-to-one association to CsDRelazione
	@ManyToOne
	@JoinColumn(name="RELAZIONE_ID")
	private CsDRelazione csDRelazione;

	//bi-directional many-to-one association to CsTbUnitaMisura
	@ManyToOne
	@JoinColumn(name="UNITA_MISURA_ID")
	private CsTbUnitaMisura csTbUnitaMisura;

	public CsIInterventoEseg() {
	}

	public Long getDiarioId() {
		return this.diarioId;
	}

	public void setDiarioId(Long diarioId) {
		this.diarioId = diarioId;
	}

	public Date getDataErogazioneAl() {
		return this.dataErogazioneAl;
	}

	public void setDataErogazioneAl(Date dataErogazioneAl) {
		this.dataErogazioneAl = dataErogazioneAl;
	}

	public Date getDataErogazioneDal() {
		return this.dataErogazioneDal;
	}

	public void setDataErogazioneDal(Date dataErogazioneDal) {
		this.dataErogazioneDal = dataErogazioneDal;
	}

	public String getDescrizioneIntervento() {
		return this.descrizioneIntervento;
	}

	public void setDescrizioneIntervento(String descrizioneIntervento) {
		this.descrizioneIntervento = descrizioneIntervento;
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

	public CsIIntervento getCsIIntervento() {
		return this.csIIntervento;
	}

	public void setCsIIntervento(CsIIntervento csIIntervento) {
		this.csIIntervento = csIIntervento;
	}

	public BigDecimal getNAltriInterventiPrevisti() {
		return this.nAltriInterventiPrevisti;
	}

	public void setNAltriInterventiPrevisti(BigDecimal nAltriInterventiPrevisti) {
		this.nAltriInterventiPrevisti = nAltriInterventiPrevisti;
	}

	public CsOOperatoreSettore getCsOOperatoreSettore() {
		return this.csOOperatoreSettore;
	}

	public void setCsOOperatoreSettore(CsOOperatoreSettore csOOperatoreSettore) {
		this.csOOperatoreSettore = csOOperatoreSettore;
	}

	public CsOOrganizzazione getCsOOrganizzazione() {
		return this.csOOrganizzazione;
	}

	public void setCsOOrganizzazione(CsOOrganizzazione csOOrganizzazione) {
		this.csOOrganizzazione = csOOrganizzazione;
	}

	public CsOOrganizzazione getCsOOrganizzazioneCoinv() {
		return this.csOOrganizzazioneCoinv;
	}

	public void setCsOOrganizzazioneCoinv(CsOOrganizzazione csOOrganizzazioneCoinv) {
		this.csOOrganizzazioneCoinv = csOOrganizzazioneCoinv;
	}

	public CsOSettore CsOSettore() {
		return this.csOSettore;
	}

	public void setCsOSettore(CsOSettore csOSettore) {
		this.csOSettore = csOSettore;
	}

	public CsCTipoIntervento getCsCTipoIntervento() {
		return this.csCTipoIntervento;
	}

	public void setCsCTipoIntervento(CsCTipoIntervento csCTipoIntervento) {
		this.csCTipoIntervento = csCTipoIntervento;
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

	public String getValore() {
		return this.valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	public String getValorePartecAltraOrg() {
		return this.valorePartecAltraOrg;
	}

	public void setValorePartecAltraOrg(String valorePartecAltraOrg) {
		this.valorePartecAltraOrg = valorePartecAltraOrg;
	}

	public String getValoreUtente() {
		return this.valoreUtente;
	}

	public void setValoreUtente(String valoreUtente) {
		this.valoreUtente = valoreUtente;
	}

	public CsDDiario getCsDDiario() {
		return this.csDDiario;
	}

	public void setCsDDiario(CsDDiario csDDiario) {
		this.csDDiario = csDDiario;
	}

	public CsDRelazione getCsDRelazione() {
		return this.csDRelazione;
	}

	public void setCsDRelazione(CsDRelazione csDRelazione) {
		this.csDRelazione = csDRelazione;
	}

	public CsTbUnitaMisura getCsTbUnitaMisura() {
		return this.csTbUnitaMisura;
	}

	public void setCsTbUnitaMisura(CsTbUnitaMisura csTbUnitaMisura) {
		this.csTbUnitaMisura = csTbUnitaMisura;
	}

}