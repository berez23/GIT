package it.webred.ct.service.tsSoggiorno.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the IS_SOCIETA database table.
 * 
 */
@Entity
@Table(name="IS_SOCIETA")
public class IsSocieta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String cf;

	@Column(name="COMUNE_SEDE")
	private String comuneSede;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_FINE")
	private Date dtFine;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

    @Temporal( TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Column(name="INDIRIZZO_SEDE")
	private String indirizzoSede;

	@Column(name="NUMERO_CIV_SEDE")
	private String numeroCivSede;

	@Column(name="\"PI\"")
	private String pi;

	@Column(name="RAG_SOC")
	private String ragSoc;

	@Column(name="SIGLA_PROV_SEDE")
	private String siglaProvSede;

	@Column(name="STATO_ESTERO_SEDE")
	private String statoEsteroSede;

	@Column(name="USR_INS")
	private String usrIns;

	@Column(name="USR_MOD")
	private String usrMod;

    public IsSocieta() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCf() {
		return this.cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public String getComuneSede() {
		return this.comuneSede;
	}

	public void setComuneSede(String comuneSede) {
		this.comuneSede = comuneSede;
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

	public String getIndirizzoSede() {
		return this.indirizzoSede;
	}

	public void setIndirizzoSede(String indirizzoSede) {
		this.indirizzoSede = indirizzoSede;
	}

	public String getNumeroCivSede() {
		return this.numeroCivSede;
	}

	public void setNumeroCivSede(String numeroCivSede) {
		this.numeroCivSede = numeroCivSede;
	}

	public String getPi() {
		return this.pi;
	}

	public void setPi(String pi) {
		this.pi = pi;
	}

	public String getRagSoc() {
		return this.ragSoc;
	}

	public void setRagSoc(String ragSoc) {
		this.ragSoc = ragSoc;
	}

	public String getSiglaProvSede() {
		return this.siglaProvSede;
	}

	public void setSiglaProvSede(String siglaProvSede) {
		this.siglaProvSede = siglaProvSede;
	}

	public String getStatoEsteroSede() {
		return this.statoEsteroSede;
	}

	public void setStatoEsteroSede(String statoEsteroSede) {
		this.statoEsteroSede = statoEsteroSede;
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