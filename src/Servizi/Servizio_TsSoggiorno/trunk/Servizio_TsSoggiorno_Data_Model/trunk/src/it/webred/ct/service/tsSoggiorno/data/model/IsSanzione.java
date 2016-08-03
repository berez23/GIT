package it.webred.ct.service.tsSoggiorno.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * The persistent class for the IS_SANZIONE database table.
 * 
 */
@Entity
@Table(name="IS_SANZIONE")
public class IsSanzione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String descrizione;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Column(name="FK_IS_CLASSE")
	private String fkIsClasse;

	@Column(name="FK_IS_PERIODO")
	private BigDecimal fkIsPeriodo;

	@Column(name="FK_IS_STRUTTURA")
	private BigDecimal fkIsStruttura;

	@Column(name="USR_INS")
	private String usrIns;

	@Column(name="USR_MOD")
	private String usrMod;

	private BigDecimal valore;

	public IsSanzione() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
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
	
	public String getDtInsFormatted() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(this.dtIns);
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

	public String getFkIsClasse() {
		return this.fkIsClasse;
	}

	public void setFkIsClasse(String fkIsClasse) {
		this.fkIsClasse = fkIsClasse;
	}

	public BigDecimal getFkIsPeriodo() {
		return this.fkIsPeriodo;
	}

	public void setFkIsPeriodo(BigDecimal fkIsPeriodo) {
		this.fkIsPeriodo = fkIsPeriodo;
	}

	public BigDecimal getFkIsStruttura() {
		return this.fkIsStruttura;
	}

	public void setFkIsStruttura(BigDecimal fkIsStruttura) {
		this.fkIsStruttura = fkIsStruttura;
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

	public BigDecimal getValore() {
		return this.valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}

}