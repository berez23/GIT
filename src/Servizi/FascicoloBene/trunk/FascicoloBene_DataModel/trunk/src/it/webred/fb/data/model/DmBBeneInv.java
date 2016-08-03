package it.webred.fb.data.model;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the DM_B_BENE_INV database table.
 * 
 */
@Entity
@Table(name="DM_B_BENE_INV")
@NamedQuery(name="DmBBeneInv.findAll", query="SELECT d FROM DmBBeneInv d")
public class DmBBeneInv implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DEMANIO_INV_ID_GENERATOR", sequenceName="SEQ_DEM")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DEMANIO_INV_ID_GENERATOR")
	private BigDecimal id;
	
	@Column(name="COD_CAT_INVENTARIALE")
	private String codCatInventariale;

	@Column(name="COD_CATEGORIA")
	private String codCategoria;

	@Column(name="COD_INVENTARIO")
	private String codInventario;

	@Column(name="COD_SOTTO_CATEGORIA")
	private String codSottoCategoria;

	@Column(name="COD_TIPO")
	private String codTipo;

	@Column(name="COD_TIPO_PROPRIETA")
	private String codTipoProprieta;

	@Column(name="DES_CAT_INVENTARIALE")
	private String desCatInventariale;

	@Column(name="DES_CATEGORIA")
	private String desCategoria;

	@Column(name="DES_SOTTO_CATEGORIA")
	private String desSottoCategoria;

	@Column(name="DES_TIPO")
	private String desTipo;

	@Column(name="DES_TIPO_PROPRIETA")
	private String desTipoProprieta;

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

	@Column(name="MEF_FINALITA")
	private String mefFinalita;

	@Column(name="MEF_NATURA_GIURIDICA")
	private String mefNaturaGiuridica;

	@Column(name="MEF_TIPOLOGIA")
	private String mefTipologia;

	@Column(name="MEF_UTILIZZO")
	private String mefUtilizzo;

	private String provenienza;

	private String tipo;
	
	@Column(name="CTR_HASH")
	private String ctrHash;
	
	@Column(name="COD_CARTELLA")
	private String codCartella;
	
	@Column(name="DES_CARTELLA")
	private String desCartella;
	
	//bi-directional one-to-one association to DmBBene
	@OneToOne
	@JoinColumn(name="DM_B_BENE_ID")
	private DmBBene dmBBene;

	public DmBBeneInv() {
	}

	public String getCodCatInventariale() {
		return this.codCatInventariale;
	}

	public void setCodCatInventariale(String codCatInventariale) {
		this.codCatInventariale = codCatInventariale;
	}

	public String getCodCategoria() {
		return this.codCategoria;
	}

	public void setCodCategoria(String codCategoria) {
		this.codCategoria = codCategoria;
	}

	public String getCodInventario() {
		return this.codInventario;
	}

	public void setCodInventario(String codInventario) {
		this.codInventario = codInventario;
	}

	public String getCodSottoCategoria() {
		return this.codSottoCategoria;
	}

	public void setCodSottoCategoria(String codSottoCategoria) {
		this.codSottoCategoria = codSottoCategoria;
	}

	public String getCodTipo() {
		return this.codTipo;
	}

	public void setCodTipo(String codTipo) {
		this.codTipo = codTipo;
	}

	public String getCodTipoProprieta() {
		return this.codTipoProprieta;
	}

	public void setCodTipoProprieta(String codTipoProprieta) {
		this.codTipoProprieta = codTipoProprieta;
	}

	public String getDesCatInventariale() {
		return this.desCatInventariale;
	}

	public void setDesCatInventariale(String desCatInventariale) {
		this.desCatInventariale = desCatInventariale;
	}

	public String getDesCategoria() {
		return this.desCategoria;
	}

	public void setDesCategoria(String desCategoria) {
		this.desCategoria = desCategoria;
	}

	public String getDesSottoCategoria() {
		return this.desSottoCategoria;
	}

	public void setDesSottoCategoria(String desSottoCategoria) {
		this.desSottoCategoria = desSottoCategoria;
	}

	public String getDesTipo() {
		return this.desTipo;
	}

	public void setDesTipo(String desTipo) {
		this.desTipo = desTipo;
	}

	public String getDesTipoProprieta() {
		return this.desTipoProprieta;
	}

	public void setDesTipoProprieta(String desTipoProprieta) {
		this.desTipoProprieta = desTipoProprieta;
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

	public String getMefFinalita() {
		return this.mefFinalita;
	}

	public void setMefFinalita(String mefFinalita) {
		this.mefFinalita = mefFinalita;
	}

	public String getMefNaturaGiuridica() {
		return this.mefNaturaGiuridica;
	}

	public void setMefNaturaGiuridica(String mefNaturaGiuridica) {
		this.mefNaturaGiuridica = mefNaturaGiuridica;
	}

	public String getMefTipologia() {
		return this.mefTipologia;
	}

	public void setMefTipologia(String mefTipologia) {
		this.mefTipologia = mefTipologia;
	}

	public String getMefUtilizzo() {
		return this.mefUtilizzo;
	}

	public void setMefUtilizzo(String mefUtilizzo) {
		this.mefUtilizzo = mefUtilizzo;
	}

	public String getProvenienza() {
		return this.provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public DmBBene getDmBBene() {
		return this.dmBBene;
	}

	public void setDmBBene(DmBBene dmBBene) {
		this.dmBBene = dmBBene;
	}
	public String getCtrHash() {
		return ctrHash;
	}

	public void setCtrHash(String ctrHash) {
		this.ctrHash = ctrHash;
	}

}