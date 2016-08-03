package it.webred.ct.service.tsSoggiorno.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the IS_STRUTTURA database table.
 * 
 */
@Entity
@Table(name="IS_STRUTTURA")
public class IsStruttura implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String descrizione;

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
	private java.math.BigDecimal fkIsSocieta;

	@Column(name="COD_INDIRIZZO")
	private String codIndirizzo;
	
	private String indirizzo;

	@Column(name="NUM_CIV")
	private String numCiv;

	@Column(name="USR_INS")
	private String usrIns;

	@Column(name="USR_MOD")
	private String usrMod;
	
	private String email;

    public IsStruttura() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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

	public java.math.BigDecimal getFkIsSocieta() {
		return this.fkIsSocieta;
	}

	public void setFkIsSocieta(java.math.BigDecimal fkIsSocieta) {
		this.fkIsSocieta = fkIsSocieta;
	}

	public String getIndirizzo() {
		return this.indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getNumCiv() {
		return this.numCiv;
	}

	public void setNumCiv(String numCiv) {
		this.numCiv = numCiv;
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

	public String getCodIndirizzo() {
		return codIndirizzo;
	}

	public void setCodIndirizzo(String codIndirizzo) {
		this.codIndirizzo = codIndirizzo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}