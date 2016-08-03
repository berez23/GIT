package it.webred.ct.service.tsSoggiorno.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the IS_SOGGETTO database table.
 * 
 */
@Entity
@Table(name="IS_SOGGETTO")
public class IsSoggetto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	@Column(name="CAP_RES")
	private String capRes;

	private String cell;

	private String codfisc;

	private String cognome;

	@Column(name="COMUNE_NASC")
	private String comuneNasc;

	@Column(name="COMUNE_RES")
	private String comuneRes;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_FINE")
	private Date dtFine;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_NASC")
	private Date dtNasc;

	private String email;

	private String fax;

	@Column(name="FLAG_ACCREDITATO")
	private BigDecimal flagAccreditato;

	@Column(name="INDIRIZZO_RES")
	private String indirizzoRes;

	private String nome;

	@Column(name="NUMERO_CIV_RES")
	private String numeroCivRes;

	@Column(name="SIGLA_PROV_NASC")
	private String siglaProvNasc;

	@Column(name="SIGLA_PROV_RES")
	private String siglaProvRes;

	@Column(name="STATO_ESTERO_NASC")
	private String statoEsteroNasc;

	@Column(name="STATO_ESTERO_RES")
	private String statoEsteroRes;

	private String tel;

	@Column(name="USR_INS")
	private String usrIns;

	@Column(name="USR_MOD")
	private String usrMod;

    public IsSoggetto() {
    }

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCapRes() {
		return this.capRes;
	}

	public void setCapRes(String capRes) {
		this.capRes = capRes;
	}

	public String getCell() {
		return this.cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public String getCodfisc() {
		return this.codfisc;
	}

	public void setCodfisc(String codfisc) {
		this.codfisc = codfisc;
	}

	public String getCognome() {
		return this.cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getComuneNasc() {
		return this.comuneNasc;
	}

	public void setComuneNasc(String comuneNasc) {
		this.comuneNasc = comuneNasc;
	}

	public String getComuneRes() {
		return this.comuneRes;
	}

	public void setComuneRes(String comuneRes) {
		this.comuneRes = comuneRes;
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

	public Date getDtNasc() {
		return this.dtNasc;
	}

	public void setDtNasc(Date dtNasc) {
		this.dtNasc = dtNasc;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public BigDecimal getFlagAccreditato() {
		return this.flagAccreditato;
	}

	public void setFlagAccreditato(BigDecimal flagAccreditato) {
		this.flagAccreditato = flagAccreditato;
	}

	public String getIndirizzoRes() {
		return this.indirizzoRes;
	}

	public void setIndirizzoRes(String indirizzoRes) {
		this.indirizzoRes = indirizzoRes;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNumeroCivRes() {
		return this.numeroCivRes;
	}

	public void setNumeroCivRes(String numeroCivRes) {
		this.numeroCivRes = numeroCivRes;
	}

	public String getSiglaProvNasc() {
		return this.siglaProvNasc;
	}

	public void setSiglaProvNasc(String siglaProvNasc) {
		this.siglaProvNasc = siglaProvNasc;
	}

	public String getSiglaProvRes() {
		return this.siglaProvRes;
	}

	public void setSiglaProvRes(String siglaProvRes) {
		this.siglaProvRes = siglaProvRes;
	}

	public String getStatoEsteroNasc() {
		return this.statoEsteroNasc;
	}

	public void setStatoEsteroNasc(String statoEsteroNasc) {
		this.statoEsteroNasc = statoEsteroNasc;
	}

	public String getStatoEsteroRes() {
		return this.statoEsteroRes;
	}

	public void setStatoEsteroRes(String statoEsteroRes) {
		this.statoEsteroRes = statoEsteroRes;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
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