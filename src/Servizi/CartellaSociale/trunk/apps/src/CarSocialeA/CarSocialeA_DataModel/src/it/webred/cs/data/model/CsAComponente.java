package it.webred.cs.data.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the CS_A_COMPONENTE database table.
 * 
 */
@Entity
@Table(name="CS_A_COMPONENTE")
@NamedQuery(name="CsAComponente.findAll", query="SELECT c FROM CsAComponente c")
public class CsAComponente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CS_A_COMPONENTE_ID_GENERATOR", sequenceName="SQ_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CS_A_COMPONENTE_ID_GENERATOR")
	private Long id;

	//bi-directional many-to-one association to CsAAnagrafica
	@ManyToOne
	@JoinColumn(name="ANAGRAFICA_ID")
	private CsAAnagrafica csAAnagrafica;
	
	
	@Column(name="COM_ALTRO_COD")
	private String comAltroCod;

	@Column(name="COM_ALTRO_DES")
	private String comAltroDes;

	@Column(name="COM_RES_COD")
	private String comResCod;

	@Column(name="COM_RES_DES")
	private String comResDes;

	private Boolean convivente;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DT_INS")
	private Date dtIns;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Column(name="INDIRIZZO_ALTRO")
	private String indirizzoAltro;

	@Column(name="INDIRIZZO_RES")
	private String indirizzoRes;

	@Column(name="NUM_CIV_ALTRO")
	private String numCivAltro;

	@Column(name="NUM_CIV_RES")
	private String numCivRes;

	@Column(name="PROV_ALTRO_COD")
	private String provAltroCod;

	@Column(name="PROV_RES_COD")
	private String provResCod;

	@Column(name="USER_INS")
	private String userIns;

	@Column(name="USR_MOD")
	private String usrMod;

	//bi-directional many-to-one association to CsAFamigliaGruppo
	@ManyToOne
	@JoinColumn(name="FAMIGLIA_GRUPPO_ID")
	private CsAFamigliaGruppo csAFamigliaGruppo;

	//bi-directional many-to-one association to CsTbContatto
	@ManyToOne
	@JoinColumn(name="CONTATTO_ID")
	private CsTbContatto csTbContatto;

	//bi-directional many-to-one association to CsTbDisponibilita
	@ManyToOne
	@JoinColumn(name="DISPONIBILITA_ID")
	private CsTbDisponibilita csTbDisponibilita;


	//bi-directional many-to-one association to CsTbPotesta
	@ManyToOne
	@JoinColumn(name="POTESTA_ID")
	private CsTbPotesta csTbPotesta;

	//bi-directional many-to-one association to CsTbTipoRapportoCon
	@ManyToOne
	@JoinColumn(name="PARENTELA_ID")
	private CsTbTipoRapportoCon csTbTipoRapportoCon;

	//bi-directional many-to-one association to CsFlgIntervento
	@OneToMany(mappedBy="csAComponente")
	private List<CsFlgIntervento> csFlgInterventos;
	
	//bi-directional many-to-one association to CsIBuonoSoc
	@OneToMany(mappedBy="csAComponente")
	private List<CsIBuonoSoc> csIBuonoSocs;

	//bi-directional many-to-one association to CsIContrEco
	@OneToMany(mappedBy="csAComponente")
	private List<CsIContrEco> csIContrEcos;

	//bi-directional many-to-one association to CsIResiMinore
	@OneToMany(mappedBy="csAComponente")
	private List<CsIResiMinore> csIResiMinores;
	
	public CsAComponente() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getComAltroCod() {
		return this.comAltroCod;
	}

	public void setComAltroCod(String comAltroCod) {
		this.comAltroCod = comAltroCod;
	}

	public String getComAltroDes() {
		return this.comAltroDes;
	}

	public void setComAltroDes(String comAltroDes) {
		this.comAltroDes = comAltroDes;
	}

	public String getComResCod() {
		return this.comResCod;
	}

	public void setComResCod(String comResCod) {
		this.comResCod = comResCod;
	}

	public String getComResDes() {
		return this.comResDes;
	}

	public void setComResDes(String comResDes) {
		this.comResDes = comResDes;
	}

	public Boolean getConvivente() {
		return convivente;
	}

	public void setConvivente(Boolean convivente) {
		this.convivente = convivente;
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

	public String getIndirizzoAltro() {
		return this.indirizzoAltro;
	}

	public void setIndirizzoAltro(String indirizzoAltro) {
		this.indirizzoAltro = indirizzoAltro;
	}

	public String getIndirizzoRes() {
		return this.indirizzoRes;
	}

	public void setIndirizzoRes(String indirizzoRes) {
		this.indirizzoRes = indirizzoRes;
	}

	public String getNumCivAltro() {
		return this.numCivAltro;
	}

	public void setNumCivAltro(String numCivAltro) {
		this.numCivAltro = numCivAltro;
	}

	public String getNumCivRes() {
		return this.numCivRes;
	}

	public void setNumCivRes(String numCivRes) {
		this.numCivRes = numCivRes;
	}

	public String getProvAltroCod() {
		return this.provAltroCod;
	}

	public void setProvAltroCod(String provAltroCod) {
		this.provAltroCod = provAltroCod;
	}

	public String getProvResCod() {
		return this.provResCod;
	}

	public void setProvResCod(String provResCod) {
		this.provResCod = provResCod;
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

	public CsAFamigliaGruppo getCsAFamigliaGruppo() {
		return this.csAFamigliaGruppo;
	}

	public void setCsAFamigliaGruppo(CsAFamigliaGruppo csAFamigliaGruppo) {
		this.csAFamigliaGruppo = csAFamigliaGruppo;
	}

	public CsTbContatto getCsTbContatto() {
		return this.csTbContatto;
	}

	public void setCsTbContatto(CsTbContatto csTbContatto) {
		this.csTbContatto = csTbContatto;
	}

	public CsTbDisponibilita getCsTbDisponibilita() {
		return this.csTbDisponibilita;
	}

	public void setCsTbDisponibilita(CsTbDisponibilita csTbDisponibilita) {
		this.csTbDisponibilita = csTbDisponibilita;
	}

	public CsAAnagrafica getCsAAnagrafica() {
		return this.csAAnagrafica;
	}

	public void setCsAAnagrafica(CsAAnagrafica csAAnagrafica) {
		this.csAAnagrafica = csAAnagrafica;
	}

	public CsTbPotesta getCsTbPotesta() {
		return this.csTbPotesta;
	}

	public void setCsTbPotesta(CsTbPotesta csTbPotesta) {
		this.csTbPotesta = csTbPotesta;
	}

	public CsTbTipoRapportoCon getCsTbTipoRapportoCon() {
		return this.csTbTipoRapportoCon;
	}

	public void setCsTbTipoRapportoCon(CsTbTipoRapportoCon csTbTipoRapportoCon) {
		this.csTbTipoRapportoCon = csTbTipoRapportoCon;
	}

	public List<CsFlgIntervento> getCsFlgInterventos() {
		return this.csFlgInterventos;
	}

	public void setCsFlgInterventos(List<CsFlgIntervento> csFlgInterventos) {
		this.csFlgInterventos = csFlgInterventos;
	}

	public CsFlgIntervento addCsFlgIntervento(CsFlgIntervento csFlgIntervento) {
		getCsFlgInterventos().add(csFlgIntervento);
		csFlgIntervento.setCsAComponente(this);

		return csFlgIntervento;
	}

	public CsFlgIntervento removeCsFlgIntervento(CsFlgIntervento csFlgIntervento) {
		getCsFlgInterventos().remove(csFlgIntervento);
		csFlgIntervento.setCsAComponente(null);

		return csFlgIntervento;
	}
	
	public List<CsIBuonoSoc> getCsIBuonoSocs() {
		return this.csIBuonoSocs;
	}

	public void setCsIBuonoSocs(List<CsIBuonoSoc> csIBuonoSocs) {
		this.csIBuonoSocs = csIBuonoSocs;
	}

	public CsIBuonoSoc addCsIBuonoSoc(CsIBuonoSoc csIBuonoSoc) {
		getCsIBuonoSocs().add(csIBuonoSoc);
		csIBuonoSoc.setCsAComponente(this);

		return csIBuonoSoc;
	}

	public CsIBuonoSoc removeCsIBuonoSoc(CsIBuonoSoc csIBuonoSoc) {
		getCsIBuonoSocs().remove(csIBuonoSoc);
		csIBuonoSoc.setCsAComponente(null);

		return csIBuonoSoc;
	}

	public List<CsIContrEco> getCsIContrEcos() {
		return this.csIContrEcos;
	}

	public void setCsIContrEcos(List<CsIContrEco> csIContrEcos) {
		this.csIContrEcos = csIContrEcos;
	}

	public CsIContrEco addCsIContrEco(CsIContrEco csIContrEco) {
		getCsIContrEcos().add(csIContrEco);
		csIContrEco.setCsAComponente(this);

		return csIContrEco;
	}

	public CsIContrEco removeCsIContrEco(CsIContrEco csIContrEco) {
		getCsIContrEcos().remove(csIContrEco);
		csIContrEco.setCsAComponente(null);

		return csIContrEco;
	}

	public List<CsIResiMinore> getCsIResiMinores() {
		return this.csIResiMinores;
	}

	public void setCsIResiMinores(List<CsIResiMinore> csIResiMinores) {
		this.csIResiMinores = csIResiMinores;
	}

	public CsIResiMinore addCsIResiMinore(CsIResiMinore csIResiMinore) {
		getCsIResiMinores().add(csIResiMinore);
		csIResiMinore.setCsAComponente(this);

		return csIResiMinore;
	}

	public CsIResiMinore removeCsIResiMinore(CsIResiMinore csIResiMinore) {
		getCsIResiMinores().remove(csIResiMinore);
		csIResiMinore.setCsAComponente(null);

		return csIResiMinore;
	}

}