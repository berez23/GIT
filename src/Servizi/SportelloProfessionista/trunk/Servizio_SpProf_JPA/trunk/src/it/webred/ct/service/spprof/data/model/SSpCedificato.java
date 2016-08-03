package it.webred.ct.service.spprof.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the S_SP_CEDIFICATO database table.
 * 
 */
@Entity
@Table(name="S_SP_CEDIFICATO")
public class SSpCedificato implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_SP_CEDIFICATO")
	private Long idSpCedificato;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Column(name="FK_SP_AREA_PART")
	private Long fkSpAreaPart;

	private String note;

	@Column(name="USER_INS")
	private String userIns;

	@Column(name="USER_MOD")
	private String userMod;

    public SSpCedificato() {
    }

	public long getIdSpCedificato() {
		return this.idSpCedificato;
	}

	public void setIdSpCedificato(long idSpCedificato) {
		this.idSpCedificato = idSpCedificato;
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

	public Long getFkSpAreaPart() {
		return this.fkSpAreaPart;
	}

	public void setFkSpAreaPart(Long fkSpAreaPart) {
		this.fkSpAreaPart = fkSpAreaPart;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
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