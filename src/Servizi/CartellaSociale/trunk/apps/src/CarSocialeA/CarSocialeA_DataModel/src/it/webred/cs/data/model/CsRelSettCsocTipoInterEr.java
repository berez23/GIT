package it.webred.cs.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the CS_REL_SETT_CSOC_TIPO_INTER_ER database table.
 * 
 */
@Entity
@Table(name="CS_REL_SETT_CSOC_TIPO_INTER_ER")
@NamedQuery(name="CsRelSettCsocTipoInterEr.findAll", query="SELECT c FROM CsRelSettCsocTipoInterEr c")
public class CsRelSettCsocTipoInterEr implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CsRelSettCsocTipoInterErPK id;

	private BigDecimal abilitato;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_INS")
	private Date dtIns;

	@Temporal(TemporalType.DATE)
	@Column(name="DT_MOD")
	private Date dtMod;

	@Column(name="USER_INS")
	private String userIns;

	@Column(name="USR_MOD")
	private String usrMod;

	//bi-directional many-to-one association to CsIIntervento
	@OneToMany(mappedBy="csRelSettCsocTipoInterEr")
	private List<CsIIntervento> csIInterventos;

	//bi-directional many-to-one association to CsRelCatsocTipoInter
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="CSTI_TIPO_INTERVENTO_ID", referencedColumnName="TIPO_INTERVENTO_ID",insertable=false, updatable=false),
		@JoinColumn(name="SCS_CATEGORIA_SOCIALE_ID", referencedColumnName="CATEGORIA_SOCIALE_ID",insertable=false, updatable=false)
		})
	private CsRelCatsocTipoInter csRelCatsocTipoInter;

	//bi-directional many-to-one association to CsRelSettoreCatsoc
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="SCS_CATEGORIA_SOCIALE_ID", referencedColumnName="CATEGORIA_SOCIALE_ID",insertable=false, updatable=false),
		@JoinColumn(name="SCS_SETTORE_ID", referencedColumnName="SETTORE_ID",insertable=false, updatable=false)
		})
	private CsRelSettoreCatsoc csRelSettoreCatsoc;

	public CsRelSettCsocTipoInterEr() {
	}

	public CsRelSettCsocTipoInterErPK getId() {
		return this.id;
	}

	public void setId(CsRelSettCsocTipoInterErPK id) {
		this.id = id;
	}

	public BigDecimal getAbilitato() {
		return this.abilitato;
	}

	public void setAbilitato(BigDecimal abilitato) {
		this.abilitato = abilitato;
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

	public List<CsIIntervento> getCsIInterventos() {
		return this.csIInterventos;
	}

	public void setCsIInterventos(List<CsIIntervento> csIInterventos) {
		this.csIInterventos = csIInterventos;
	}

	public CsIIntervento addCsIIntervento(CsIIntervento csIIntervento) {
		getCsIInterventos().add(csIIntervento);
		csIIntervento.setCsRelSettCsocTipoInterEr(this);

		return csIIntervento;
	}

	public CsIIntervento removeCsIIntervento(CsIIntervento csIIntervento) {
		getCsIInterventos().remove(csIIntervento);
		csIIntervento.setCsRelSettCsocTipoInterEr(null);

		return csIIntervento;
	}

	public CsRelCatsocTipoInter getCsRelCatsocTipoInter() {
		return this.csRelCatsocTipoInter;
	}

	public void setCsRelCatsocTipoInter(CsRelCatsocTipoInter csRelCatsocTipoInter) {
		this.csRelCatsocTipoInter = csRelCatsocTipoInter;
	}

	public CsRelSettoreCatsoc getCsRelSettoreCatsoc() {
		return this.csRelSettoreCatsoc;
	}

	public void setCsRelSettoreCatsoc(CsRelSettoreCatsoc csRelSettoreCatsoc) {
		this.csRelSettoreCatsoc = csRelSettoreCatsoc;
	}

}