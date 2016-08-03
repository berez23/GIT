package it.webred.ct.service.tsSoggiorno.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the IS_MODULO_OSPITI database table.
 * 
 */
@Entity
@Table(name="IS_MODULO_OSPITI")
public class IsModuloOspiti implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	private String field1;

	private String field2;

	@Column(name="FK_IS_DICHIARAZIONE")
	private BigDecimal fkIsDichiarazione;
	
	@Column(name="FK_IS_MODULO_DATI")
	private BigDecimal fkIsModuloDati;

	@Column(name="FK_IS_PERIODO")
	private BigDecimal fkIsPeriodo;

	@Column(name="N_OSPITI")
	private BigDecimal nOspiti;

	@Column(name="N_SOGGIORNI")
	private BigDecimal nSoggiorni;

	@Column(name="USR_INS")
	private String usrIns;

	@Column(name="USR_MOD")
	private String usrMod;

    public IsModuloOspiti() {
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

	public String getField1() {
		return this.field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public String getField2() {
		return this.field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public BigDecimal getFkIsModuloDati() {
		return this.fkIsModuloDati;
	}

	public void setFkIsModuloDati(BigDecimal fkIsModuloDati) {
		this.fkIsModuloDati = fkIsModuloDati;
	}

	public BigDecimal getFkIsPeriodo() {
		return this.fkIsPeriodo;
	}

	public void setFkIsPeriodo(BigDecimal fkIsPeriodo) {
		this.fkIsPeriodo = fkIsPeriodo;
	}

	public BigDecimal getNOspiti() {
		return this.nOspiti;
	}

	public void setNOspiti(BigDecimal nOspiti) {
		this.nOspiti = nOspiti;
	}

	public BigDecimal getNSoggiorni() {
		return this.nSoggiorni;
	}

	public void setNSoggiorni(BigDecimal nSoggiorni) {
		this.nSoggiorni = nSoggiorni;
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

	public BigDecimal getFkIsDichiarazione() {
		return fkIsDichiarazione;
	}

	public void setFkIsDichiarazione(BigDecimal fkIsDichiarazione) {
		this.fkIsDichiarazione = fkIsDichiarazione;
	}

}