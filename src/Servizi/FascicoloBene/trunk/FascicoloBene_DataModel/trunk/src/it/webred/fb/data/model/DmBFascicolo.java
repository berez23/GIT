package it.webred.fb.data.model;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the DM_B_FASCICOLO database table.
 * 
 */
@Entity
@Table(name="DM_B_FASCICOLO")
@NamedQuery(name="DmBFascicolo.findAll", query="SELECT d FROM DmBFascicolo d")
public class DmBFascicolo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private BigDecimal id;
	
	@Column(name="COD_FASCICOLO")
	private String codFascicolo;

	@Column(name="DES_FASCICOLO")
	private String desFascicolo;

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

	private String provenienza;

	private String tipo;
	
	@Column(name="CTR_HASH")
	private String ctrHash;

	//bi-directional many-to-one association to DmBBene
	@ManyToOne
	@JoinColumn(name="DM_B_BENE_ID")
	private DmBBene dmBBene;

	public DmBFascicolo() {
	}

	public String getCodFascicolo() {
		return this.codFascicolo;
	}

	public void setCodFascicolo(String codFascicolo) {
		this.codFascicolo = codFascicolo;
	}

	public String getDesFascicolo() {
		return this.desFascicolo;
	}

	public void setDesFascicolo(String desFascicolo) {
		this.desFascicolo = desFascicolo;
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

	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
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