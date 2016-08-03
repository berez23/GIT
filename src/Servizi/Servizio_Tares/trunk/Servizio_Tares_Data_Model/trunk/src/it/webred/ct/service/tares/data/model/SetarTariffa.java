package it.webred.ct.service.tares.data.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name="SETAR_TARIFFA")
public class SetarTariffa extends BaseItem{

	private static final long serialVersionUID = 2023836381053971787L;
	
	private Long id = null;
	private BigDecimal cvBilancio = null;
	private BigDecimal cfBilancio = null;
	private String geo = "";
	private BigDecimal supTot = null;
	private BigDecimal numEntita = null;
	private SetarCoeffDesc voce = null;
	//private BigDecimal occupantiNonResidenti = null;
	private String abitanti = "";

	public SetarTariffa() {

	}//-------------------------------------------------------------------------

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="CV_BILANCIO")
	public BigDecimal getCvBilancio() {
		return cvBilancio;
	}

	public void setCvBilancio(BigDecimal cvBilancio) {
		this.cvBilancio = cvBilancio;
	}

	@Column(name="CF_BILANCIO")
	public BigDecimal getCfBilancio() {
		return cfBilancio;
	}

	public void setCfBilancio(BigDecimal cfBilancio) {
		this.cfBilancio = cfBilancio;
	}

	@Column(name="GEO")
	public String getGeo() {
		return geo;
	}

	public void setGeo(String geo) {
		this.geo = geo;
	}

	@Column(name="SUP_TOT")
	public BigDecimal getSupTot() {
		return supTot;
	}

	public void setSupTot(BigDecimal supTot) {
		this.supTot = supTot;
	}

	@Column(name="NUM_ENTITA")
	public BigDecimal getNumEntita() {
		return numEntita;
	}

	public void setNumEntita(BigDecimal numEntita) {
		this.numEntita = numEntita;
	}

	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumns({
	    @JoinColumn(name="FK_VOCE", referencedColumnName="ID", insertable=true, updatable=true)})
	@NotFound(action=NotFoundAction.IGNORE)
	public SetarCoeffDesc getVoce() {
		return voce;
	}

	public void setVoce(SetarCoeffDesc voce) {
		this.voce = voce;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

//	@Column(name="OCCUPANTI_NON_RESIDENTI")
//	public BigDecimal getOccupantiNonResidenti() {
//		return occupantiNonResidenti;
//	}
//
//	public void setOccupantiNonResidenti(BigDecimal occupantiNonResidenti) {
//		this.occupantiNonResidenti = occupantiNonResidenti;
//	}

	public String getAbitanti() {
		return abitanti;
	}

	public void setAbitanti(String abitanti) {
		this.abitanti = abitanti;
	}
	
	

}
