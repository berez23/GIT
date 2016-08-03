package it.webred.ct.service.tsSoggiorno.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the IS_CLASSI_STRUTTURA database table.
 * 
 */
@Entity
@Table(name="IS_CLASSI_STRUTTURA")
public class IsClassiStruttura implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private IsClassiStrutturaPK id;

	private String descrizione;

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

    public IsClassiStruttura() {
    }

	public IsClassiStrutturaPK getId() {
		return this.id;
	}

	public void setId(IsClassiStrutturaPK id) {
		this.id = id;
	}
	
	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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