package it.webred.fb.data.model;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the DM_B_TIPO_USO database table.
 * 
 */
@Entity
@Table(name="DM_B_TIPO_USO")
@NamedQuery(name="DmBTipoUso.findAll", query="SELECT d FROM DmBTipoUso d")
public class DmBTipoUso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DEMANIO_USO_ID_GENERATOR", sequenceName="SEQ_DEM")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DEMANIO_USO_ID_GENERATOR")
	private BigDecimal id;

	@Column(name="COD_TIPO_USO")
	private String codTipoUso;

	@Column(name="DES_TIPO_USO")
	private String desTipoUso;

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
	private DmBBeneNOTEAGER dmBBene;

	public DmBTipoUso() {
	}

	public String getCodTipoUso() {
		return this.codTipoUso;
	}

	public void setCodTipoUso(String codTipoUso) {
		this.codTipoUso = codTipoUso;
	}

	public String getDesTipoUso() {
		return this.desTipoUso;
	}

	public void setDesTipoUso(String desTipoUso) {
		this.desTipoUso = desTipoUso;
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

	public String getCtrHash() {
		return ctrHash;
	}

	public void setCtrHash(String ctrHash) {
		this.ctrHash = ctrHash;
	}

	public DmBBeneNOTEAGER getDmBBene() {
		return dmBBene;
	}

	public void setDmBBene(DmBBeneNOTEAGER dmBBene) {
		this.dmBBene = dmBBene;
	}

}