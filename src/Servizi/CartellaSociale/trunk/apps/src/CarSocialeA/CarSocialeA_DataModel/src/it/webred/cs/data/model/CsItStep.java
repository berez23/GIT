package it.webred.cs.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the CS_IT_STEP database table.
 * 
 */
@Entity
@Table(name="CS_IT_STEP")
@NamedQuery(name="CsItStep.findAll", query="SELECT c FROM CsItStep c")
public class CsItStep implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CS_IT_STEP_ID_GENERATOR", sequenceName="SQ_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CS_IT_STEP_ID_GENERATOR")
	private Long id;

	private String nota;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATA_CREAZIONE")
	private Date dataCreazione;

	//bi-directional many-to-one association to CsACaso
	@ManyToOne(optional=false)
	@JoinColumn(name="CASO_ID")
	private CsACaso csACaso;

	//bi-directional many-to-one association to CsDDiario
	@ManyToOne(optional=false)
	@JoinColumn(name="DIARIO_ID")
	private CsDDiario csDDiario;

	//uni-directional many-to-one association to CsCfgItStato
	@ManyToOne(optional=false)
	@JoinColumn(name="CFG_IT_STATO_ID")
	private CsCfgItStato csCfgItStato;

	//bi-directional many-to-one association to CsOOperatoreSettore
	@ManyToOne
	@JoinColumn(name="OPERATORE_ID")
	private CsOOperatore csOOperatore1;

	//bi-directional many-to-one association to CsOOperatoreSettore
	@ManyToOne
	@JoinColumn(name="OPERATORE_TO")
	private CsOOperatore csOOperatore2;

	//bi-directional many-to-one association to CsOOrganizzazione
	@ManyToOne
	@JoinColumn(name="ORGANIZZAZIONE_ID_TO")
	private CsOOrganizzazione csOOrganizzazione2;

	//bi-directional many-to-one association to CsOOrganizzazione
	@ManyToOne
	@JoinColumn(name="ORGANIZZAZIONE_ID")
	private CsOOrganizzazione csOOrganizzazione1;

	//bi-directional many-to-one association to CsOSettore
	@ManyToOne
	@JoinColumn(name="SETTORE_ID_TO")
	private CsOSettore csOSettore2;

	//bi-directional many-to-one association to CsOSettore
	@ManyToOne
	@JoinColumn(name="SETTORE_ID")
	private CsOSettore csOSettore1;

	@OneToMany(mappedBy="csItStep", cascade = {CascadeType.PERSIST})
	private List<CsItStepAttrValue> csItStepAttrValues;
	
	public CsItStep() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataCreazione() {
		return this.dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public CsACaso getCsACaso() {
		return this.csACaso;
	}

	public void setCsACaso(CsACaso csACaso) {
		this.csACaso = csACaso;
	}

	public CsDDiario getCsDDiario() {
		return this.csDDiario;
	}

	public void setCsDDiario(CsDDiario csDDiario) {
		this.csDDiario = csDDiario;
	}

	public CsCfgItStato getCsCfgItStato() {
		return this.csCfgItStato;
	}

	public void setCsCfgItStato(CsCfgItStato csCfgItStato) {
		this.csCfgItStato = csCfgItStato;
	}

	public CsOOperatore getCsOOperatore1() {
		return this.csOOperatore1;
	}

	public void setCsOOperatore1(CsOOperatore csOOperatore1) {
		this.csOOperatore1 = csOOperatore1;
	}

	public CsOOperatore getCsOOperatore2() {
		return this.csOOperatore2;
	}

	public void setCsOOperatore2(CsOOperatore csOOperatore2) {
		this.csOOperatore2 = csOOperatore2;
	}

	public CsOOrganizzazione getCsOOrganizzazione1() {
		return this.csOOrganizzazione1;
	}

	public void setCsOOrganizzazione1(CsOOrganizzazione csOOrganizzazione1) {
		this.csOOrganizzazione1 = csOOrganizzazione1;
	}

	public CsOOrganizzazione getCsOOrganizzazione2() {
		return this.csOOrganizzazione2;
	}

	public void setCsOOrganizzazione2(CsOOrganizzazione csOOrganizzazione2) {
		this.csOOrganizzazione2 = csOOrganizzazione2;
	}

	public CsOSettore getCsOSettore1() {
		return this.csOSettore1;
	}

	public void setCsOSettore1(CsOSettore csOSettore1) {
		this.csOSettore1 = csOSettore1;
	}

	public CsOSettore getCsOSettore2() {
		return this.csOSettore2;
	}

	public void setCsOSettore2(CsOSettore csOSettore2) {
		this.csOSettore2 = csOSettore2;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public List<CsItStepAttrValue> getCsItStepAttrValues() {
		return csItStepAttrValues;
	}

	public void setCsItStepAttrValues(List<CsItStepAttrValue> csItStepAttrValues) {
		this.csItStepAttrValues = csItStepAttrValues;
	}

	
}