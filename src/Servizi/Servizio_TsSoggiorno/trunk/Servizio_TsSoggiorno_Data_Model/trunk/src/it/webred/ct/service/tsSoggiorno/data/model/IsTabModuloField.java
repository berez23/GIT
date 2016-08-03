package it.webred.ct.service.tsSoggiorno.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the IS_TAB_MODULO_FIELD database table.
 * 
 */
@Entity
@Table(name="IS_TAB_MODULO_FIELD")
public class IsTabModuloField implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String codice;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Column(name="FIELD_NAME")
	private String fieldName;

	@Column(name="FK_IS_TAB_MODULO")
	private String fkIsTabModulo;

	@Column(name="LABEL_FIELD")
	private String labelField;

	@Column(name="USR_INS")
	private String usrIns;

	@Column(name="USR_MOD")
	private String usrMod;

    public IsTabModuloField() {
    }

	public String getCodice() {
		return this.codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
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

	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFkIsTabModulo() {
		return this.fkIsTabModulo;
	}

	public void setFkIsTabModulo(String fkIsTabModulo) {
		this.fkIsTabModulo = fkIsTabModulo;
	}

	public String getLabelField() {
		return this.labelField;
	}

	public void setLabelField(String labelField) {
		this.labelField = labelField;
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