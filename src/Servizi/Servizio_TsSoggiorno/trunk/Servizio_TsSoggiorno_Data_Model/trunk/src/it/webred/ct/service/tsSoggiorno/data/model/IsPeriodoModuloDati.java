package it.webred.ct.service.tsSoggiorno.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the IS_PERIODO_MODULO_DATI database table.
 * 
 */
@Entity
@Table(name="IS_PERIODO_MODULO_DATI")
public class IsPeriodoModuloDati implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private IsPeriodoModuloDatiPK id;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Column(name="USR_INS")
	private String usrIns;

	@Column(name="USR_MOD")
	private String usrMod;

    public IsPeriodoModuloDati() {
    }

	public IsPeriodoModuloDatiPK getId() {
		return this.id;
	}

	public void setId(IsPeriodoModuloDatiPK id) {
		this.id = id;
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

	public String getUsrIns() {
		return this.usrIns;
	}

	public void setUsrIns(String usrIns) {
		this.usrIns = usrIns;
	}

	public String getUsrMod() {
		return this.usrMod;
	}

	public void setUsrMod(String usrMod) {
		this.usrMod = usrMod;
	}

}