package it.webred.fb.data.model;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the DM_B_INDIRIZZO database table.
 * 
 */
@Entity
@Table(name="DM_B_INDIRIZZO")
@NamedQuery(name="DmBIndirizzo.findAll", query="SELECT d FROM DmBIndirizzo d")
public class DmBIndirizzo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DEMANIO_IND_ID_GENERATOR", sequenceName="SEQ_DEM")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DEMANIO_IND_ID_GENERATOR")
	private String id;

	private String civico;

	@Column(name="COD_COMUNE")
	private String codComune;

	@Column(name="COD_VIA")
	private BigDecimal codVia;

	@Column(name="CTR_HASH")
	private String ctrHash;

	@Column(name="DES_COMUNE")
	private String desComune;

	@Column(name="DESCR_VIA")
	private String descrVia;

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

	@Column(name="FLG_PRINCIPALE")
	private BigDecimal flgPrincipale;

	private String provenienza;

	private String tipo;

	@Column(name="TIPO_VIA")
	private String tipoVia;

	private String prov;
	
	//bi-directional many-to-one association to DmBBene
	@ManyToOne
	@JoinColumn(name="DM_B_BENE_ID")
	private DmBBeneNOTEAGER dmBBene;

	public DmBIndirizzo() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCivico() {
		return this.civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public String getCodComune() {
		return this.codComune;
	}

	public void setCodComune(String codComune) {
		this.codComune = codComune;
	}

	public BigDecimal getCodVia() {
		return this.codVia;
	}

	public void setCodVia(BigDecimal codVia) {
		this.codVia = codVia;
	}

	public String getCtrHash() {
		return this.ctrHash;
	}

	public void setCtrHash(String ctrHash) {
		this.ctrHash = ctrHash;
	}

	public String getDesComune() {
		return this.desComune;
	}

	public void setDesComune(String desComune) {
		this.desComune = desComune;
	}

	public String getDescrVia() {
		return this.descrVia;
	}

	public void setDescrVia(String descrVia) {
		this.descrVia = descrVia;
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

	public BigDecimal getFlgPrincipale() {
		return this.flgPrincipale;
	}

	public void setFlgPrincipale(BigDecimal flgPrincipale) {
		this.flgPrincipale = flgPrincipale;
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

	public String getTipoVia() {
		return this.tipoVia;
	}

	public void setTipoVia(String tipoVia) {
		this.tipoVia = tipoVia;
	}

	public DmBBeneNOTEAGER getDmBBene() {
		return this.dmBBene;
	}

	public void setDmBBene(DmBBeneNOTEAGER dmBBene) {
		this.dmBBene = dmBBene;
	}

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

}