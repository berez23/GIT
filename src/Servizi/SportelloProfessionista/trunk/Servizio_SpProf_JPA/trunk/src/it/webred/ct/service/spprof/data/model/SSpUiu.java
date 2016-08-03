package it.webred.ct.service.spprof.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the S_SP_UIU database table.
 * 
 */
@Entity
@Table(name="S_SP_UIU")
public class SSpUiu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_SP_UIU")
	private Long idSpUiu;

	private String categoria;

	private String classe;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Column(name="FK_SP_EDIFICIO")
	private Long fkSpEdificio;

	@Column(name="FK_SP_UNITA_VOL")
	private Long fkSpUnitaVol;

	private String note;

	@Column(name="NUM_UIU")
	private BigDecimal numUiu;

	@Column(name="USER_INS")
	private String userIns;

	@Column(name="USER_MOD")
	private String userMod;

    public SSpUiu() {
    }

	public long getIdSpUiu() {
		return this.idSpUiu;
	}

	public void setIdSpUiu(long idSpUiu) {
		this.idSpUiu = idSpUiu;
	}

	public String getCategoria() {
		return this.categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getClasse() {
		return this.classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
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

	public Long getFkSpUnitaVol() {
		return this.fkSpUnitaVol;
	}

	public void setFkSpUnitaVol(Long fkSpUnitaVol) {
		this.fkSpUnitaVol = fkSpUnitaVol;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BigDecimal getNumUiu() {
		return this.numUiu;
	}

	public void setNumUiu(BigDecimal numUiu) {
		this.numUiu = numUiu;
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