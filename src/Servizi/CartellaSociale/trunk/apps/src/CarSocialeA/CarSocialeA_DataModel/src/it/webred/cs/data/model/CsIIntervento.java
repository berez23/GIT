package it.webred.cs.data.model;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the CS_I_INTERVENTO database table.
 * 
 */
@Entity
@Table(name="CS_I_INTERVENTO")
@NamedQuery(name="CsIIntervento.findAll", query="SELECT c FROM CsIIntervento c")
public class CsIIntervento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CS_I_INTERVENTO_ID_GENERATOR", sequenceName="SQ_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CS_I_INTERVENTO_ID_GENERATOR")
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DT_INS")
	private Date dtIns;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Temporal(TemporalType.DATE)
	@Column(name="FINE_AL")
	private Date fineAl;

	@Temporal(TemporalType.DATE)
	@Column(name="FINE_DAL")
	private Date fineDal;

	@Column(name="FLAG_PRIMAER_RINNOVO")
	private String flagPrimaerRinnovo;

	@Column(name="FLAG_UNATANTUM")
	private Boolean flagUnatantum;
	
	@Temporal(TemporalType.DATE)
	@Column(name="INIZIO_AL")
	private Date inizioAl;

	@Temporal(TemporalType.DATE)
	@Column(name="INIZIO_DAL")
	private Date inizioDal;

	@Column(name="USER_INS")
	private String userIns;

	@Column(name="USR_MOD")
	private String usrMod;

	//bi-directional many-to-one association to CsRelSettCsocTipoInter
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="SCTI_CATEGORIA_SOCIALE_ID", referencedColumnName="SCS_CATEGORIA_SOCIALE_ID"),
		@JoinColumn(name="SCTI_SETTORE_ID", referencedColumnName="SCS_SETTORE_ID"),
		@JoinColumn(name="SCTI_TIPO_INTERVENTO_ID", referencedColumnName="CSTI_TIPO_INTERVENTO_ID")
		})
	private CsRelSettCsocTipoInter csRelSettCsocTipoInter;

	//bi-directional many-to-one association to CsRelSettCsocTipoInterEr
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="SCTI_ER_CATEGORIA_SOCIALE_ID", referencedColumnName="SCS_CATEGORIA_SOCIALE_ID"),
		@JoinColumn(name="SCTI_ER_SETTORE_ID", referencedColumnName="SCS_SETTORE_ID"),
		@JoinColumn(name="SCTI_ER_TIPO_INTERVENTO_ID", referencedColumnName="CSTI_TIPO_INTERVENTO_ID")
		})
	private CsRelSettCsocTipoInterEr csRelSettCsocTipoInterEr;
	
	//bi-directional many-to-one association to CsFlgIntervento
	@OneToMany(mappedBy="csIIntervento", fetch=FetchType.EAGER)
	private List<CsFlgIntervento> csFlgInterventos;
	
	//bi-directional one-to-one association to CsIBuonoSoc
	@OneToOne(mappedBy="csIIntervento", cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private CsIBuonoSoc csIBuonoSoc;

	//bi-directional one-to-one association to CsICentrod
	@OneToOne(mappedBy="csIIntervento", cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private CsICentrod csICentrod;
	
	//bi-directional one-to-one association to CsIContrEco
	@OneToOne(mappedBy="csIIntervento", cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private CsIContrEco csIContrEco;

	//bi-directional one-to-one association to CsIPasti
	@OneToOne(mappedBy="csIIntervento", cascade = CascadeType.ALL )
	@PrimaryKeyJoinColumn
	private CsIPasti csIPasti;

	//bi-directional one-to-one association to CsIResiMinore
	@OneToOne(mappedBy="csIIntervento",cascade = CascadeType.ALL )
	@PrimaryKeyJoinColumn
	private CsIResiMinore csIResiMinore;

	//bi-directional one-to-one association to CsIVouchersad
	@OneToOne(mappedBy="csIIntervento",cascade = CascadeType.ALL )
	@PrimaryKeyJoinColumn
	private CsIVouchersad csIVouchersad;
	
	//bi-directional one-to-one association to CsIAdmAdh
	@OneToOne(mappedBy="csIIntervento",cascade = CascadeType.ALL)
	private CsIAdmAdh csIAdmAdh;

	//bi-directional one-to-one association to CsIAffidoFam
	@OneToOne(mappedBy="csIIntervento",cascade = CascadeType.ALL)
	private CsIAffidoFam csIAffidoFam;

	//bi-directional one-to-one association to CsIResiAdulti
	@OneToOne(mappedBy="csIIntervento",cascade = CascadeType.ALL)
	private CsIResiAdulti csIResiAdulti;

	//bi-directional one-to-one association to CsISemiResiMin
	@OneToOne(mappedBy="csIIntervento",cascade = CascadeType.ALL)
	private CsISemiResiMin csISemiResiMin;
	
	//bi-directional one-to-one association to CsISchedaLavoro
	@OneToOne(mappedBy="csIIntervento",cascade = CascadeType.ALL)
	private CsISchedaLavoro csISchedaLavoro;


	public CsIIntervento() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Date getInizioAl() {
		return this.inizioAl;
	}

	public void setInizioAl(Date inizioAl) {
		this.inizioAl = inizioAl;
	}

	public Date getInizioDal() {
		return this.inizioDal;
	}

	public void setInizioDal(Date inizioDal) {
		this.inizioDal = inizioDal;
	}

	public String getUserIns() {
		return this.userIns;
	}

	public void setUserIns(String userIns) {
		this.userIns = userIns;
	}

	public String getUsrMod() {
		return this.usrMod;
	}

	public void setUsrMod(String usrMod) {
		this.usrMod = usrMod;
	}

	public List<CsFlgIntervento> getCsFlgInterventos() {
		return this.csFlgInterventos;
	}

	public void setCsFlgInterventos(List<CsFlgIntervento> csFlgInterventos) {
		this.csFlgInterventos = csFlgInterventos;
	}

	public CsFlgIntervento addCsFlgIntervento(CsFlgIntervento csFlgIntervento) {
		getCsFlgInterventos().add(csFlgIntervento);
		csFlgIntervento.setCsIIntervento(this);

		return csFlgIntervento;
	}

	public CsFlgIntervento removeCsFlgIntervento(CsFlgIntervento csFlgIntervento) {
		getCsFlgInterventos().remove(csFlgIntervento);
		csFlgIntervento.setCsIIntervento(null);

		return csFlgIntervento;
	}

	public CsIContrEco getCsIContrEco() {
		return this.csIContrEco;
	}

	public void setCsIContrEco(CsIContrEco csIContrEco) {
		this.csIContrEco = csIContrEco;
	}

	public CsIPasti getCsIPasti() {
		return this.csIPasti;
	}

	public void setCsIPasti(CsIPasti csIPasti) {
		this.csIPasti = csIPasti;
	}

	public CsIResiMinore getCsIResiMinore() {
		return this.csIResiMinore;
	}

	public void setCsIResiMinore(CsIResiMinore csIResiMinore) {
		this.csIResiMinore = csIResiMinore;
	}

	public CsIVouchersad getCsIVouchersad() {
		return this.csIVouchersad;
	}

	public void setCsIVouchersad(CsIVouchersad csIVouchersad) {
		this.csIVouchersad = csIVouchersad;
	}

	public CsIBuonoSoc getCsIBuonoSoc() {
		return csIBuonoSoc;
	}

	public void setCsIBuonoSoc(CsIBuonoSoc csIBuonoSoc) {
		this.csIBuonoSoc = csIBuonoSoc;
	}

	public CsICentrod getCsICentrod() {
		return csICentrod;
	}

	public void setCsICentrod(CsICentrod csICentrod) {
		this.csICentrod = csICentrod;
	}

	public Date getFineAl() {
		return fineAl;
	}

	public void setFineAl(Date fineAl) {
		this.fineAl = fineAl;
	}

	public Date getFineDal() {
		return fineDal;
	}

	public void setFineDal(Date fineDal) {
		this.fineDal = fineDal;
	}

	public String getFlagPrimaerRinnovo() {
		return flagPrimaerRinnovo;
	}

	public void setFlagPrimaerRinnovo(String flagPrimaerRinnovo) {
		this.flagPrimaerRinnovo = flagPrimaerRinnovo;
	}

	public CsRelSettCsocTipoInter getCsRelSettCsocTipoInter() {
		return this.csRelSettCsocTipoInter;
	}

	public void setCsRelSettCsocTipoInter(CsRelSettCsocTipoInter csRelSettCsocTipoInter) {
		this.csRelSettCsocTipoInter = csRelSettCsocTipoInter;
	}

	public CsRelSettCsocTipoInterEr getCsRelSettCsocTipoInterEr() {
		return this.csRelSettCsocTipoInterEr;
	}

	public void setCsRelSettCsocTipoInterEr(CsRelSettCsocTipoInterEr csRelSettCsocTipoInterEr) {
		this.csRelSettCsocTipoInterEr = csRelSettCsocTipoInterEr;
	}

	public CsIAdmAdh getCsIAdmAdh() {
		return csIAdmAdh;
	}

	public void setCsIAdmAdh(CsIAdmAdh csIAdmAdh) {
		this.csIAdmAdh = csIAdmAdh;
	}

	public CsIAffidoFam getCsIAffidoFam() {
		return csIAffidoFam;
	}

	public void setCsIAffidoFam(CsIAffidoFam csIAffidoFam) {
		this.csIAffidoFam = csIAffidoFam;
	}

	public CsIResiAdulti getCsIResiAdulti() {
		return csIResiAdulti;
	}

	public void setCsIResiAdulti(CsIResiAdulti csIResiAdulti) {
		this.csIResiAdulti = csIResiAdulti;
	}

	public CsISemiResiMin getCsISemiResiMin() {
		return csISemiResiMin;
	}

	public void setCsISemiResiMin(CsISemiResiMin csISemiResiMin) {
		this.csISemiResiMin = csISemiResiMin;
	}

	public CsISchedaLavoro getCsISchedaLavoro() {
		return csISchedaLavoro;
	}

	public void setCsISchedaLavoro(CsISchedaLavoro csISchedaLavoro) {
		this.csISchedaLavoro = csISchedaLavoro;
	}

	public Boolean getFlagUnatantum() {
		return flagUnatantum;
	}

	public void setFlagUnatantum(Boolean flagUnatantum) {
		this.flagUnatantum = flagUnatantum;
	}
	
}