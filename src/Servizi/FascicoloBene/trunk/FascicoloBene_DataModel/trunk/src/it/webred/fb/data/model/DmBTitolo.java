package it.webred.fb.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the DM_B_TITOLO database table.
 * 
 */
@Entity
@Table(name="DM_B_TITOLO")
@NamedQuery(name="DmBTitolo.findAll", query="SELECT d FROM DmBTitolo d")
public class DmBTitolo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	@Column(name="ID_TITOLO")
	private String idTitolo;
	
	@Column(name="ANNO_ACQUISIZIONE")
	private String annoAcquisizione;

	private String annotazioni;

	@Column(name="CTR_HASH")
	private String ctrHash;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_FINE_DATO")
	private Date dataFineDato;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_INIZIO_DATO")
	private Date dataInizioDato;

	@Lob
	@Column(name="DOCPROP_CONTRAENTI")
	private String docpropContraenti;

	@Column(name="DOCPROP_NUMERO")
	private String docpropNumero;

	@Column(name="DOCPROP_ORIGINE")
	private String docpropOrigine;

	@Lob
	@Column(name="DOCPROP_PAGAMENTO")
	private String docpropPagamento;

	@Lob
	@Column(name="DOCPROP_PATTICOND")
	private String docpropPatticond;

	@Lob
	@Column(name="DOCPROP_SERVATT")
	private String docpropServatt;

	@Lob
	@Column(name="DOCPROP_SERVPASS")
	private String docpropServpass;

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

	@Column(name="NORMATIVA_UTILIZZATA")
	private String normativaUtilizzata;

	@Lob
	private String oggetto;

	private String provenienza;

	@Column(name="STRUMENTO_COSTRUTTIVO")
	private String strumentoCostruttivo;

	private String tipo;

	@Column(name="TIPO_ACQUISIZIONE")
	private String tipoAcquisizione;

	@Column(name="TIPO_DIRITTO_REALE")
	private String tipoDirittoReale;

	@Temporal(TemporalType.DATE)
	@Column(name="TIT_DATA")
	private Date titData;

	@Temporal(TemporalType.DATE)
	@Column(name="TIT_REG_DATA")
	private Date titRegData;

	@Column(name="TIT_REG_NUMERO")
	private String titRegNumero;
	
	@Column(name="TIT_REG_ATTI")
	private String titRegAtti;

	@Column(name="TIT_REG_SERIE")
	private String titRegSerie;

	@Column(name="TIT_REG_UFFICIO")
	private String titRegUfficio;

	public String getIdTitolo() {
		return idTitolo;
	}

	public void setIdTitolo(String idTitolo) {
		this.idTitolo = idTitolo;
	}

	@Column(name="TIT_REG_VOLUME")
	private String titRegVolume;

	@Temporal(TemporalType.DATE)
	@Column(name="TRASCR_DATA")
	private Date trascrData;

	@Column(name="TRASCR_LOCALITA")
	private String trascrLocalita;

	@Column(name="TRASCR_NUM")
	private String trascrNum;

	@Column(name="TRASCR_NUMORDINE")
	private String trascrNumordine;

	//bi-directional many-to-one association to DmBBene
	@ManyToOne
	@JoinColumn(name="DM_B_BENE_ID")
	private DmBBene dmBBene;

	public DmBTitolo() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAnnoAcquisizione() {
		return this.annoAcquisizione;
	}

	public void setAnnoAcquisizione(String annoAcquisizione) {
		this.annoAcquisizione = annoAcquisizione;
	}

	public String getAnnotazioni() {
		return this.annotazioni;
	}

	public void setAnnotazioni(String annotazioni) {
		this.annotazioni = annotazioni;
	}

	public String getCtrHash() {
		return this.ctrHash;
	}

	public void setCtrHash(String ctrHash) {
		this.ctrHash = ctrHash;
	}

	public Date getDataFineDato() {
		return this.dataFineDato;
	}

	public void setDataFineDato(Date dataFineDato) {
		this.dataFineDato = dataFineDato;
	}

	public Date getDataInizioDato() {
		return this.dataInizioDato;
	}

	public void setDataInizioDato(Date dataInizioDato) {
		this.dataInizioDato = dataInizioDato;
	}

	public String getDocpropContraenti() {
		return this.docpropContraenti;
	}

	public void setDocpropContraenti(String docpropContraenti) {
		this.docpropContraenti = docpropContraenti;
	}

	public String getDocpropNumero() {
		return this.docpropNumero;
	}

	public void setDocpropNumero(String docpropNumero) {
		this.docpropNumero = docpropNumero;
	}

	public String getDocpropOrigine() {
		return this.docpropOrigine;
	}

	public void setDocpropOrigine(String docpropOrigine) {
		this.docpropOrigine = docpropOrigine;
	}

	public String getDocpropPagamento() {
		return this.docpropPagamento;
	}

	public void setDocpropPagamento(String docpropPagamento) {
		this.docpropPagamento = docpropPagamento;
	}

	public String getDocpropPatticond() {
		return this.docpropPatticond;
	}

	public void setDocpropPatticond(String docpropPatticond) {
		this.docpropPatticond = docpropPatticond;
	}

	public String getDocpropServatt() {
		return this.docpropServatt;
	}

	public void setDocpropServatt(String docpropServatt) {
		this.docpropServatt = docpropServatt;
	}

	public String getDocpropServpass() {
		return this.docpropServpass;
	}

	public void setDocpropServpass(String docpropServpass) {
		this.docpropServpass = docpropServpass;
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

	public String getNormativaUtilizzata() {
		return this.normativaUtilizzata;
	}

	public void setNormativaUtilizzata(String normativaUtilizzata) {
		this.normativaUtilizzata = normativaUtilizzata;
	}

	public String getOggetto() {
		return this.oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getProvenienza() {
		return this.provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public String getStrumentoCostruttivo() {
		return this.strumentoCostruttivo;
	}

	public void setStrumentoCostruttivo(String strumentoCostruttivo) {
		this.strumentoCostruttivo = strumentoCostruttivo;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipoAcquisizione() {
		return this.tipoAcquisizione;
	}

	public void setTipoAcquisizione(String tipoAcquisizione) {
		this.tipoAcquisizione = tipoAcquisizione;
	}

	public String getTipoDirittoReale() {
		return this.tipoDirittoReale;
	}

	public void setTipoDirittoReale(String tipoDirittoReale) {
		this.tipoDirittoReale = tipoDirittoReale;
	}

	public Date getTitData() {
		return titData;
	}

	public void setTitData(Date titData) {
		this.titData = titData;
	}


	public Date getTrascrData() {
		return trascrData;
	}

	public void setTrascrData(Date trascrData) {
		this.trascrData = trascrData;
	}

	public String getTrascrLocalita() {
		return this.trascrLocalita;
	}

	public void setTrascrLocalita(String trascrLocalita) {
		this.trascrLocalita = trascrLocalita;
	}

	public String getTrascrNum() {
		return this.trascrNum;
	}

	public void setTrascrNum(String trascrNum) {
		this.trascrNum = trascrNum;
	}

	public String getTrascrNumordine() {
		return this.trascrNumordine;
	}

	public void setTrascrNumordine(String trascrNumordine) {
		this.trascrNumordine = trascrNumordine;
	}

	public DmBBene getDmBBene() {
		return this.dmBBene;
	}

	public void setDmBBene(DmBBene dmBBene) {
		this.dmBBene = dmBBene;
	}

	public Date getTitRegData() {
		return titRegData;
	}

	public void setTitRegData(Date titRegData) {
		this.titRegData = titRegData;
	}

	public String getTitRegNumero() {
		return titRegNumero;
	}

	public void setTitRegNumero(String titRegNumero) {
		this.titRegNumero = titRegNumero;
	}

	public String getTitRegAtti() {
		return titRegAtti;
	}

	public void setTitRegAtti(String titRegAtti) {
		this.titRegAtti = titRegAtti;
	}

	public String getTitRegSerie() {
		return titRegSerie;
	}

	public void setTitRegSerie(String titRegSerie) {
		this.titRegSerie = titRegSerie;
	}

	public String getTitRegUfficio() {
		return titRegUfficio;
	}

	public void setTitRegUfficio(String titRegUfficio) {
		this.titRegUfficio = titRegUfficio;
	}

	public String getTitRegVolume() {
		return titRegVolume;
	}

	public void setTitRegVolume(String titRegVolume) {
		this.titRegVolume = titRegVolume;
	}

}