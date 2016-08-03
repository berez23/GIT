package it.webred.cs.data.model;

import it.webred.cs.data.base.ICsDDiarioChild;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the CS_D_COLLOQUIO database table.
 * 
 */
@Entity
@Table(name="CS_D_COLLOQUIO")
@NamedQuery(name="CsDColloquio.findAll", query="SELECT c FROM CsDColloquio c")
public class CsDColloquio implements ICsDDiarioChild {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DIARIO_ID")
	private Long diarioId;

	//bi-directional many-to-one association to CsDRelazione
	@ManyToOne
	@JoinColumn(name="DIARIO_CONCHI_ID")
	private CsCDiarioConchi csCDiarioConchi;

	//bi-directional many-to-one association to CsCDiarioDove
	@ManyToOne
	@JoinColumn(name="DIARIO_DOVE_ID")
	private CsCDiarioDove csCDiarioDove;
	
	//bi-directional many-to-one association to CsCDiarioDove
	@ManyToOne
	@JoinColumn(name="TIPO_COLLOQUIO_ID")
	private CsCTipoColloquio csCTipoColloquio;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATA_COLLOQUIO")
	private Date dataColloquio;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Column(name="TESTO_DIARIO")
	private String testoDiario;

	@Column(name="USER_INS")
	private String userIns;

	@Column(name="USR_MOD")
	private String usrMod;
	
	@Column(name="RISERVATO")
	private String riservato;
	
	//bi-directional one-to-one association to CsDDiario
	@OneToOne
	@JoinColumn(name="DIARIO_ID")
	private CsDDiario csDDiario;
	
	public CsDColloquio() {
	}

	public CsCTipoColloquio getCsCTipoColloquio() {
		return csCTipoColloquio;
	}

	public void setCsCTipoColloquio(CsCTipoColloquio csCTipoColloquio) {
		this.csCTipoColloquio = csCTipoColloquio;
	}

	public CsCDiarioDove getCsCDiarioDove() {
		return this.csCDiarioDove;
	}

	public void setCsCDiarioDove(CsCDiarioDove csCDiarioDove) {
		this.csCDiarioDove = csCDiarioDove;
	}
	
	public Long getDiarioId() {
		return this.diarioId;
	}

	public void setDiarioId(Long diarioId) {
		this.diarioId = diarioId;
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

	public CsCDiarioConchi getCsCDiarioConchi() {
		return this.csCDiarioConchi;
	}

	public void setCsCDiarioConchi(CsCDiarioConchi csCDiarioConchi) {
		this.csCDiarioConchi = csCDiarioConchi;
	}

	public String getTestoDiario() {
		return this.testoDiario;
	}

	public void setTestoDiario(String testoDiario) {
		this.testoDiario = testoDiario;
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

	public CsDDiario getCsDDiario() {
		return this.csDDiario;
	}

	public void setCsDDiario(CsDDiario csDDiario) {
		this.csDDiario = csDDiario;
	}

	public Date getDataColloquio() {
		return dataColloquio;
	}

	public void setDataColloquio(Date dataColloquio) {
		this.dataColloquio = dataColloquio;
	}
	
	public String getRiservato() {
		return riservato;
	}

	public void setRiservato(String riservato) {
		this.riservato = riservato;
	}
}