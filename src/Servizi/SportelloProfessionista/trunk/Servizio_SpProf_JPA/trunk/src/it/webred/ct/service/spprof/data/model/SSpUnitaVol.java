package it.webred.ct.service.spprof.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the S_SP_UNITA_VOL database table.
 * 
 */
@Entity
@Table(name="S_SP_UNITA_VOL")
public class SSpUnitaVol implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_SP_UNITA_VOL")
	private Long idSpUnitaVol;

	private BigDecimal altezza;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Column(name="FK_SP_EDIFICIO")
	private Long fkSpEdificio;

	private String note;

	@Column(name="PIANI_ET")
	private BigDecimal pianiEt;

	@Column(name="PIANI_FT")
	private BigDecimal pianiFt;

	private BigDecimal slp;

	@Column(name="SUP_DBT")
	private BigDecimal supDbt;

	@Column(name="USER_INS")
	private String userIns;

	@Column(name="USER_MOD")
	private String userMod;

	private BigDecimal volume;

    public SSpUnitaVol() {
    }

	public long getIdSpUnitaVol() {
		return this.idSpUnitaVol;
	}

	public void setIdSpUnitaVol(long idSpUnitaVol) {
		this.idSpUnitaVol = idSpUnitaVol;
	}

	public BigDecimal getAltezza() {
		return this.altezza;
	}

	public void setAltezza(BigDecimal altezza) {
		this.altezza = altezza;
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

	public Long getFkSpEdificio() {
		return this.fkSpEdificio;
	}

	public void setFkSpEdificio(Long fkSpEdificio) {
		this.fkSpEdificio = fkSpEdificio;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BigDecimal getPianiEt() {
		return this.pianiEt;
	}

	public void setPianiEt(BigDecimal pianiEt) {
		this.pianiEt = pianiEt;
	}

	public BigDecimal getPianiFt() {
		return this.pianiFt;
	}

	public void setPianiFt(BigDecimal pianiFt) {
		this.pianiFt = pianiFt;
	}

	public BigDecimal getSlp() {
		return this.slp;
	}

	public void setSlp(BigDecimal slp) {
		this.slp = slp;
	}

	public BigDecimal getSupDbt() {
		return this.supDbt;
	}

	public void setSupDbt(BigDecimal supDbt) {
		this.supDbt = supDbt;
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

	public BigDecimal getVolume() {
		return this.volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

}