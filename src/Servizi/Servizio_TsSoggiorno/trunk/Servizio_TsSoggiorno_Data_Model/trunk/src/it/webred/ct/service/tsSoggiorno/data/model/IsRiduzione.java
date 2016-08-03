package it.webred.ct.service.tsSoggiorno.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the IS_RIDUZIONE database table.
 * 
 */
@Entity
@Table(name="IS_RIDUZIONE")
public class IsRiduzione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Column(name="FK_IS_MODULO_DATI")
	private BigDecimal fkIsModuloDati;

	@Column(name="PERC_RIDUZ")
	private BigDecimal percRiduz;

	@Column(name="USR_INS")
	private String usrIns;

	@Column(name="USR_MOD")
	private String usrMod;

	@Column(name="VALORE_RIDUZ")
	private BigDecimal valoreRiduz;

    public IsRiduzione() {
    }

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
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

	public BigDecimal getFkIsModuloDati() {
		return this.fkIsModuloDati;
	}

	public void setFkIsModuloDati(BigDecimal fkIsModuloDati) {
		this.fkIsModuloDati = fkIsModuloDati;
	}

	public BigDecimal getPercRiduz() {
		return this.percRiduz;
	}

	public void setPercRiduz(BigDecimal percRiduz) {
		this.percRiduz = percRiduz;
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

	public BigDecimal getValoreRiduz() {
		return this.valoreRiduz;
	}

	public void setValoreRiduz(BigDecimal valoreRiduz) {
		this.valoreRiduz = valoreRiduz;
	}

}