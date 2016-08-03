package it.webred.ct.service.tsSoggiorno.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the IS_STRUTTURA_SNAP database table.
 * 
 */
@Entity
@Table(name="IS_STRUTTURA_SNAP")
public class IsStrutturaSnap implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name="CAP_RES")
	private String capRes;

	private String cell;

	private String cf;

	private String codfisc;

	private String cognome;

	@Column(name="COMUNE_NASC")
	private String comuneNasc;

	@Column(name="COMUNE_RES")
	private String comuneRes;

	@Column(name="COMUNE_SEDE")
	private String comuneSede;

	@Column(name="DESCRIZIONE_CLASSE_STRUT")
	private String descrizioneClasseStrut;

	@Column(name="DESCRIZIONE_CLASSE")
	private String descrizioneClasse;
	
	@Column(name="DESCRIZIONE_TITOLO")
	private String descrizioneTitolo;

	@Column(name="DESCRIZIONE_STRUT")
	private String descrizioneStrut;
	
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

	@Column(name="FK_IS_CLASSE")
	private String fkIsClasse;

	@Column(name="FK_IS_SOCIETA")
	private java.math.BigDecimal fkIsSocieta;

	@Column(name="FK_IS_SOGGETTO")
	private java.math.BigDecimal fkIsSoggetto;

	@Column(name="FK_IS_STRUTTURA")
	private java.math.BigDecimal fkIsStruttura;

	@Column(name="INDIRIZZO_RES")
	private String indirizzoRes;

	@Column(name="INDIRIZZO_SEDE")
	private String indirizzoSede;

	@Column(name="INDIRIZZO_STRUT")
	private String indirizzoStrut;

	private String nome;

	@Column(name="NUM_CIV_STRUT")
	private String numCivStrut;

	@Column(name="NUMERO_CIV_RES")
	private String numeroCivRes;

	@Column(name="NUMERO_CIV_SEDE")
	private String numeroCivSede;

	@Column(name="\"PI\"")
	private String pi;

	@Column(name="RAG_SOC")
	private String ragSoc;

	@Column(name="SIGLA_PROV_NASC")
	private String siglaProvNasc;

	@Column(name="SIGLA_PROV_RES")
	private String siglaProvRes;

	@Column(name="SIGLA_PROV_SEDE")
	private String siglaProvSede;

	@Column(name="STATO_ESTERO_NASC")
	private String statoEsteroNasc;

	@Column(name="STATO_ESTERO_RES")
	private String statoEsteroRes;

	@Column(name="STATO_ESTERO_SEDE")
	private String statoEsteroSede;
	
	@Column(name="EMAIL_STRUT")
	private String emailStrut;

	private String tel;

	@Column(name="USR_INS")
	private String usrIns;

	@Column(name="USR_MOD")
	private String usrMod;

    public IsStrutturaSnap() {
    }

	public String getDescrizioneTitolo() {
		return descrizioneTitolo;
	}

	public void setDescrizioneTitolo(String descrizioneTitolo) {
		this.descrizioneTitolo = descrizioneTitolo;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
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

	public String getCf() {
		return this.cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
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

	public String getComuneSede() {
		return this.comuneSede;
	}

	public void setComuneSede(String comuneSede) {
		this.comuneSede = comuneSede;
	}

	public String getDescrizioneClasseStrut() {
		return this.descrizioneClasseStrut;
	}

	public void setDescrizioneClasseStrut(String descrizioneClasseStrut) {
		this.descrizioneClasseStrut = descrizioneClasseStrut;
	}

	public String getDescrizioneClasse() {
		return this.descrizioneClasse;
	}

	public void setDescrizioneClasse(String descrizioneClasse) {
		this.descrizioneClasse = descrizioneClasse;
	}

	public String getDescrizioneStrut() {
		return this.descrizioneStrut;
	}

	public String getEmailStrut() {
		return emailStrut;
	}

	public void setEmailStrut(String emailStrut) {
		this.emailStrut = emailStrut;
	}

	public void setDescrizioneStrut(String descrizioneStrut) {
		this.descrizioneStrut = descrizioneStrut;
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

	public String getFkIsClasse() {
		return this.fkIsClasse;
	}

	public void setFkIsClasse(String fkIsClasse) {
		this.fkIsClasse = fkIsClasse;
	}

	public java.math.BigDecimal getFkIsSocieta() {
		return this.fkIsSocieta;
	}

	public void setFkIsSocieta(java.math.BigDecimal fkIsSocieta) {
		this.fkIsSocieta = fkIsSocieta;
	}

	public java.math.BigDecimal getFkIsSoggetto() {
		return this.fkIsSoggetto;
	}

	public void setFkIsSoggetto(java.math.BigDecimal fkIsSoggetto) {
		this.fkIsSoggetto = fkIsSoggetto;
	}

	public java.math.BigDecimal getFkIsStruttura() {
		return this.fkIsStruttura;
	}

	public void setFkIsStruttura(java.math.BigDecimal fkIsStruttura) {
		this.fkIsStruttura = fkIsStruttura;
	}

	public String getIndirizzoRes() {
		return this.indirizzoRes;
	}

	public void setIndirizzoRes(String indirizzoRes) {
		this.indirizzoRes = indirizzoRes;
	}

	public String getIndirizzoSede() {
		return this.indirizzoSede;
	}

	public void setIndirizzoSede(String indirizzoSede) {
		this.indirizzoSede = indirizzoSede;
	}

	public String getIndirizzoStrut() {
		return this.indirizzoStrut;
	}

	public void setIndirizzoStrut(String indirizzoStrut) {
		this.indirizzoStrut = indirizzoStrut;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNumCivStrut() {
		return this.numCivStrut;
	}

	public void setNumCivStrut(String numCivStrut) {
		this.numCivStrut = numCivStrut;
	}

	public String getNumeroCivRes() {
		return this.numeroCivRes;
	}

	public void setNumeroCivRes(String numeroCivRes) {
		this.numeroCivRes = numeroCivRes;
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

	public String getSiglaProvSede() {
		return this.siglaProvSede;
	}

	public void setSiglaProvSede(String siglaProvSede) {
		this.siglaProvSede = siglaProvSede;
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

	public String getStatoEsteroSede() {
		return this.statoEsteroSede;
	}

	public void setStatoEsteroSede(String statoEsteroSede) {
		this.statoEsteroSede = statoEsteroSede;
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