package it.webred.ct.service.spprof.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the S_SP_POSTI_AUTO database table.
 * 
 */
@Entity
@Table(name="S_SP_POSTI_AUTO")
public class SSpPostiAuto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_SP_POSTI_AUTO")
	private Long idSpPostiAuto;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Column(name="FK_SP_CEDIFICATO")
	private Long fkSpCedificato;

	@Column(name="N_ACCESSI_CARRAI_PREV")
	private BigDecimal nAccessiCarraiPrev;

	@Column(name="N_BOX_AUTO")
	private BigDecimal nBoxAuto;

	@Column(name="N_PASSI_CARRAI_PREV")
	private BigDecimal nPassiCarraiPrev;

	@Column(name="N_POSTI_AUTO")
	private BigDecimal nPostiAuto;

	@Column(name="USER_INS")
	private String userIns;

	@Column(name="USER_MOD")
	private String userMod;

    public SSpPostiAuto() {
    }

	public long getIdSpPostiAuto() {
		return this.idSpPostiAuto;
	}

	public void setIdSpPostiAuto(long idSpPostiAuto) {
		this.idSpPostiAuto = idSpPostiAuto;
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

	public BigDecimal getNAccessiCarraiPrev() {
		return this.nAccessiCarraiPrev;
	}

	public void setNAccessiCarraiPrev(BigDecimal nAccessiCarraiPrev) {
		this.nAccessiCarraiPrev = nAccessiCarraiPrev;
	}

	public BigDecimal getNBoxAuto() {
		return this.nBoxAuto;
	}

	public void setNBoxAuto(BigDecimal nBoxAuto) {
		this.nBoxAuto = nBoxAuto;
	}

	public BigDecimal getNPassiCarraiPrev() {
		return this.nPassiCarraiPrev;
	}

	public void setNPassiCarraiPrev(BigDecimal nPassiCarraiPrev) {
		this.nPassiCarraiPrev = nPassiCarraiPrev;
	}

	public BigDecimal getNPostiAuto() {
		return this.nPostiAuto;
	}

	public void setNPostiAuto(BigDecimal nPostiAuto) {
		this.nPostiAuto = nPostiAuto;
	}

	public String getUserIns() {
		return this.userIns;
	}

	public void setUserIns(String userIns) {
		this.userIns = userIns;
	}

	public String getUserMod() {
		return this.userMod;
	}

	public void setUserMod(String userMod) {
		this.userMod = userMod;
	}

}