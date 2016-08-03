package it.webred.ct.service.tares.data.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
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
@Table(name="SETAR_COEFF")
public class SetarCoeff extends BaseItem {

	private static final long serialVersionUID = 6707486426129303413L;
	
	private Long id = null;
	private String geo = "";
	private BigDecimal min = null;
	private BigDecimal med = null;
	private BigDecimal max = null;
	private BigDecimal adhoc = null;
	private String coeff = "";
	private SetarCoeffDesc voce = null;
	private String selezione = "";
	private String abit = "";
	
	public SetarCoeff() {

	}//-------------------------------------------------------------------------

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGeo() {
		return geo;
	}

	public void setGeo(String geo) {
		this.geo = geo;
	}

	public BigDecimal getMin() {
		return min;
	}

	public void setMin(BigDecimal min) {
		this.min = min;
	}

	public BigDecimal getMed() {
		return med;
	}

	public void setMed(BigDecimal med) {
		this.med = med;
	}

	public BigDecimal getMax() {
		return max;
	}

	public void setMax(BigDecimal max) {
		this.max = max;
	}

	public BigDecimal getAdhoc() {
		return adhoc;
	}

	public void setAdhoc(BigDecimal adhoc) {
		this.adhoc = adhoc;
	}

	public String getCoeff() {
		return coeff;
	}

	public void setCoeff(String coeff) {
		this.coeff = coeff;
	}

	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumns({
	    @JoinColumn(name="FK_DES", referencedColumnName="ID", insertable=true, updatable=true)})
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

	public String getSelezione() {
		return selezione;
	}

	public void setSelezione(String selezione) {
		this.selezione = selezione;
	}

	public String getAbit() {
		return abit;
	}

	public void setAbit(String abit) {
		this.abit = abit;
	}

		
	

}
