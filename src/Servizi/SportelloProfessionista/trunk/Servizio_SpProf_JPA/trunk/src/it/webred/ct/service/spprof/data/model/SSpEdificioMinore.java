package it.webred.ct.service.spprof.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the S_SP_EDIFICIO_MINORE database table.
 * 
 */
@Entity
@Table(name="S_SP_EDIFICIO_MINORE")
public class SSpEdificioMinore implements Serializable {
	private static final long serialVersionUID = 1L;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Id
	@Column(name="FK_SP_CEDIFICATO")
	private Long fkSpCedificato;

	@Column(name="FK_SP_TIPOLOGIA_EDI_MIN")
	private String fkSpTipologiaEdiMin;

	private BigDecimal precario;

	@Column(name="USER_MOD")
	private String userMod;

    public SSpEdificioMinore() {
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

	public Long getFkSpCedificato() {
		return this.fkSpCedificato;
	}

	public void setFkSpCedificato(Long fkSpCedificato) {
		this.fkSpCedificato = fkSpCedificato;
	}

	public String getFkSpTipologiaEdiMin() {
		return this.fkSpTipologiaEdiMin;
	}

	public void setFkSpTipologiaEdiMin(String fkSpTipologiaEdiMin) {
		this.fkSpTipologiaEdiMin = fkSpTipologiaEdiMin;
	}

	public BigDecimal getPrecario() {
		return this.precario;
	}

	public void setPrecario(BigDecimal precario) {
		this.precario = precario;
	}

	public String getUserMod() {
		return this.userMod;
	}

	public void setUserMod(String userMod) {
		this.userMod = userMod;
	}

}