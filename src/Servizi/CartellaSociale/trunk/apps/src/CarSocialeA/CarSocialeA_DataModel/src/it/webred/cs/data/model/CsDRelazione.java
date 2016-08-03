package it.webred.cs.data.model;

import it.webred.cs.data.base.ICsDDiarioChild;

import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * The persistent class for the CS_D_RELAZIONE database table.
 * 
 */
@Entity
@Table(name="CS_D_RELAZIONE")
@NamedQuery(name="CsDRelazione.findAll", query="SELECT c FROM CsDRelazione c")
public class CsDRelazione implements ICsDDiarioChild {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DIARIO_ID")
	private Long diarioId;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_PROPOSTA")
	private Date dataProposta;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATA_PROSSIMA_RELAZIONE_AL")
	private Date dataProssimaRelazioneAl;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_PROSSIMA_RELAZIONE_DAL")
	private Date dataProssimaRelazioneDal;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Column(name="FONTI_UTILIZZATE")
	private String fontiUtilizzate;
	
	private String proposta;

	@Column(name="SITUAZIONE_AMB")
	private String situazioneAmb;

	@Column(name="SITUAZIONE_PARENTALE")
	private String situazioneParentale;

	@Column(name="SITUAZIONE_SANITARIA")
	private String situazioneSanitaria;

	@Column(name="USER_INS")
	private String userIns;

	@Column(name="USR_MOD")
	private String usrMod;

	//bi-directional many-to-one association to CsIInterventoEseg
	@OneToMany(mappedBy="csDRelazione")
	private List<CsIInterventoEseg> csIInterventoEsegs;

	//bi-directional one-to-one association to CsDDiario
	@OneToOne
	@JoinColumn(name="DIARIO_ID")
	private CsDDiario csDDiario;
	
	//bi-directional many-to-many association to CsCTipoIntervento
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
		name="CS_REL_RELAZIONE_TIPOINT"
		, joinColumns={
			@JoinColumn(name="RELAZIONE_DIARIO_ID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="TIPO_INTERVENTO_ID")
			}
		)
	private Set<CsCTipoIntervento> csCTipoInterventos;
		
	public CsDRelazione() {
	}

	public Long getDiarioId() {
		return diarioId;
	}

	public void setDiarioId(Long diarioId) {
		this.diarioId = diarioId;
	}

	public CsDDiario getCsDDiario() {
		return csDDiario;
	}

	public void setCsDDiario(CsDDiario csDDiario) {
		this.csDDiario = csDDiario;
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

	public String getProposta() {
		return this.proposta;
	}

	public void setProposta(String proposta) {
		this.proposta = proposta;
	}

	public String getSituazioneAmb() {
		return this.situazioneAmb;
	}

	public void setSituazioneAmb(String situazioneAmb) {
		this.situazioneAmb = situazioneAmb;
	}

	public String getSituazioneParentale() {
		return this.situazioneParentale;
	}

	public void setSituazioneParentale(String situazioneParentale) {
		this.situazioneParentale = situazioneParentale;
	}

	public String getSituazioneSanitaria() {
		return this.situazioneSanitaria;
	}

	public void setSituazioneSanitaria(String situazioneSanitaria) {
		this.situazioneSanitaria = situazioneSanitaria;
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

	public List<CsIInterventoEseg> getCsIInterventoEsegs() {
		return this.csIInterventoEsegs;
	}

	public void setCsIInterventoEsegs(List<CsIInterventoEseg> csIInterventoEsegs) {
		this.csIInterventoEsegs = csIInterventoEsegs;
	}

	public CsIInterventoEseg addCsIInterventoEseg(CsIInterventoEseg csIInterventoEseg) {
		getCsIInterventoEsegs().add(csIInterventoEseg);
		csIInterventoEseg.setCsDRelazione(this);

		return csIInterventoEseg;
	}

	public CsIInterventoEseg removeCsIInterventoEseg(CsIInterventoEseg csIInterventoEseg) {
		getCsIInterventoEsegs().remove(csIInterventoEseg);
		csIInterventoEseg.setCsDRelazione(null);

		return csIInterventoEseg;
	}

	public Date getDataProposta() {
		return dataProposta;
	}

	public void setDataProposta(Date dataProposta) {
		this.dataProposta = dataProposta;
	}

	public Date getDataProssimaRelazioneAl() {
		return dataProssimaRelazioneAl;
	}

	public void setDataProssimaRelazioneAl(Date dataProssimaRelazioneAl) {
		this.dataProssimaRelazioneAl = dataProssimaRelazioneAl;
	}

	public Date getDataProssimaRelazioneDal() {
		return dataProssimaRelazioneDal;
	}

	public void setDataProssimaRelazioneDal(Date dataProssimaRelazioneDal) {
		this.dataProssimaRelazioneDal = dataProssimaRelazioneDal;
	}

	public String getFontiUtilizzate() {
		return fontiUtilizzate;
	}

	public void setFontiUtilizzate(String fontiUtilizzate) {
		this.fontiUtilizzate = fontiUtilizzate;
	}

	public Set<CsCTipoIntervento> getCsCTipoInterventos() {
		return csCTipoInterventos;
	}

	public void setCsCTipoInterventos(Set<CsCTipoIntervento> csCTipoInterventos) {
		this.csCTipoInterventos = csCTipoInterventos;
	}
	
}