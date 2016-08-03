package it.webred.fb.data.model;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the DM_B_TERRENO database table.
 * 
 */
@Entity
@Table(name="DM_B_TERRENO")
@NamedQuery(name="DmBTerreno.findAll", query="SELECT d FROM DmBTerreno d")
public class DmBTerreno implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String annotazione;

	private String classe;

	@Column(name="COD_COMUNE")
	private String codComune;

	@Column(name="COD_UTILIZZO")
	private String codUtilizzo;

	@Column(name="CTR_HASH")
	private String ctrHash;

	private String dismesso;

	@Lob
	private String contratti;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DT_ELAB")
	private Date dtElab;

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
	@Column(name="DT_MOD")
	private Date dtMod;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_VENDITA")
	private Date dataVendita;

	private String finalita;

	private String foglio;

	private String fonte;

	private String mappale;

	private String note;

	private BigDecimal partita;

	private String provenienza;

	private String qualita;

	@Column(name="QUOTA_POSS")
	private BigDecimal quotaPoss;

	@Column(name="REDD_AGR")
	private BigDecimal reddAgr;

	@Column(name="REDD_DOMIN")
	private BigDecimal reddDomin;

	@Column(name="RENDITA_PRESUNTA")
	private String renditaPresunta;

	private String sezione;

	private String sub;

	private BigDecimal superficie;

	private String tipo;

	private String tipologia;

	private BigDecimal unita;

	@Column(name="USO_ATTUALE")
	private String usoAttuale;

	@Column(name="VAL_ACQUISTO")
	private BigDecimal valAcquisto;

	@Column(name="VAL_CATASTALE")
	private BigDecimal valCatastale;

	@Column(name="VAL_INVENTARIO")
	private BigDecimal valInventario;

	//bi-directional many-to-one association to DmBBene
	@ManyToOne
	@JoinColumn(name="DM_B_BENE_ID")
	private DmBBene dmBBene;

	public DmBTerreno() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAnnotazione() {
		return this.annotazione;
	}

	public void setAnnotazione(String annotazione) {
		this.annotazione = annotazione;
	}

	public String getClasse() {
		return this.classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getCodComune() {
		return this.codComune;
	}

	public void setCodComune(String codComune) {
		this.codComune = codComune;
	}

	public String getCodUtilizzo() {
		return this.codUtilizzo;
	}

	public void setCodUtilizzo(String codUtilizzo) {
		this.codUtilizzo = codUtilizzo;
	}

	public String getCtrHash() {
		return this.ctrHash;
	}

	public void setCtrHash(String ctrHash) {
		this.ctrHash = ctrHash;
	}

	public String getDismesso() {
		return this.dismesso;
	}

	public void setDismesso(String dismesso) {
		this.dismesso = dismesso;
	}

	public Date getDtElab() {
		return this.dtElab;
	}

	public void setDtElab(Date dtElab) {
		this.dtElab = dtElab;
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

	public Date getDtMod() {
		return this.dtMod;
	}

	public void setDtMod(Date dtMod) {
		this.dtMod = dtMod;
	}

	public String getFinalita() {
		return this.finalita;
	}

	public void setFinalita(String finalita) {
		this.finalita = finalita;
	}

	public String getFoglio() {
		return this.foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public String getFonte() {
		return this.fonte;
	}

	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	public String getMappale() {
		return this.mappale;
	}

	public void setMappale(String mappale) {
		this.mappale = mappale;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BigDecimal getPartita() {
		return this.partita;
	}

	public void setPartita(BigDecimal partita) {
		this.partita = partita;
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

	public BigDecimal getQuotaPoss() {
		return this.quotaPoss;
	}

	public void setQuotaPoss(BigDecimal quotaPoss) {
		this.quotaPoss = quotaPoss;
	}

	public BigDecimal getReddAgr() {
		return this.reddAgr;
	}

	public void setReddAgr(BigDecimal reddAgr) {
		this.reddAgr = reddAgr;
	}

	public BigDecimal getReddDomin() {
		return this.reddDomin;
	}

	public void setReddDomin(BigDecimal reddDomin) {
		this.reddDomin = reddDomin;
	}

	public String getRenditaPresunta() {
		return this.renditaPresunta;
	}

	public void setRenditaPresunta(String renditaPresunta) {
		this.renditaPresunta = renditaPresunta;
	}

	public String getSezione() {
		return this.sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getSub() {
		return this.sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public BigDecimal getSuperficie() {
		return this.superficie;
	}

	public void setSuperficie(BigDecimal superficie) {
		this.superficie = superficie;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipologia() {
		return this.tipologia;
	}

	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

	public BigDecimal getUnita() {
		return this.unita;
	}

	public void setUnita(BigDecimal unita) {
		this.unita = unita;
	}

	public String getUsoAttuale() {
		return this.usoAttuale;
	}

	public void setUsoAttuale(String usoAttuale) {
		this.usoAttuale = usoAttuale;
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

	public BigDecimal getValInventario() {
		return this.valInventario;
	}

	public void setValInventario(BigDecimal valInventario) {
		this.valInventario = valInventario;
	}

	public DmBBene getDmBBene() {
		return this.dmBBene;
	}

	public void setDmBBene(DmBBene dmBBene) {
		this.dmBBene = dmBBene;
	}

	public String getContratti() {
		return contratti;
	}

	public void setContratti(String contratti) {
		this.contratti = contratti;
	}

	public Date getDataVendita() {
		return dataVendita;
	}

	public void setDataVendita(Date dataVendita) {
		this.dataVendita = dataVendita;
	}

	
}