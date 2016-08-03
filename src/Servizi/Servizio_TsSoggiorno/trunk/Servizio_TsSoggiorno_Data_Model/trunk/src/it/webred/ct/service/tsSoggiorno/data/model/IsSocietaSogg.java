package it.webred.ct.service.tsSoggiorno.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the IS_SOCIETA_SOGG database table.
 * 
 */
@Entity
@Table(name="IS_SOCIETA_SOGG")
public class IsSocietaSogg implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="DESC_TITOLO")
	private String descTitolo;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_FINE")
	private Date dtFine;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Column(name="FK_IS_SOCIETA")
	private BigDecimal fkIsSocieta;

	@Column(name="FK_IS_SOGGETTO")
	private BigDecimal fkIsSoggetto;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name="USR_INS")
	private String usrIns;

	@Column(name="USR_MOD")
	private String usrMod;

    public IsSocietaSogg() {
    }

	public String getDescTitolo() {
		return this.descTitolo;
	}

	public void setDescTitolo(String descTitolo) {
		this.descTitolo = descTitolo;
	}

	public Date getDtFine() {
		return this.dtFine;
	}

	public void setDtFine(Date dtFine) {
		this.dtFine = dtFine;
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

	public BigDecimal getFkIsSocieta() {
		return this.fkIsSocieta;
	}

	public void setFkIsSocieta(BigDecimal fkIsSocieta) {
		this.fkIsSocieta = fkIsSocieta;
	}

	public BigDecimal getFkIsSoggetto() {
		return this.fkIsSoggetto;
	}

	public void setFkIsSoggetto(BigDecimal fkIsSoggetto) {
		this.fkIsSoggetto = fkIsSoggetto;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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