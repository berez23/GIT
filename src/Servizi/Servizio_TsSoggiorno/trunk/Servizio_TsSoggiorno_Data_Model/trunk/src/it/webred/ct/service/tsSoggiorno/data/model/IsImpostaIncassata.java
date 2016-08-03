package it.webred.ct.service.tsSoggiorno.data.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the IS_IMPOSTA_INCASSATA database table.
 * 
 */
@Entity
@Table(name="IS_IMPOSTA_INCASSATA")
public class IsImpostaIncassata implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Column(name="FK_IS_DICHIARAZIONE")
	private BigDecimal fkIsDichiarazione;

	@Column(name="FK_IS_PERIODO")
	private BigDecimal fkIsPeriodo;

	private BigDecimal importo;

	@Column(name="IMPORTO_MESI_PREC")
	private BigDecimal importoMesiPrec;

	@Column(name="USR_INS")
	private String usrIns;

	@Column(name="USR_MOD")
	private String usrMod;

    public IsImpostaIncassata() {
    }

	public Long getId() {
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

	public BigDecimal getFkIsDichiarazione() {
		return this.fkIsDichiarazione;
	}

	public void setFkIsDichiarazione(BigDecimal fkIsDichiarazione) {
		this.fkIsDichiarazione = fkIsDichiarazione;
	}

	public BigDecimal getFkIsPeriodo() {
		return this.fkIsPeriodo;
	}

	public void setFkIsPeriodo(BigDecimal fkIsPeriodo) {
		this.fkIsPeriodo = fkIsPeriodo;
	}

	public BigDecimal getImporto() {
		return this.importo;
	}

	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	public BigDecimal getImportoMesiPrec() {
		return this.importoMesiPrec;
	}

	public void setImportoMesiPrec(BigDecimal importoMesiPrec) {
		this.importoMesiPrec = importoMesiPrec;
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