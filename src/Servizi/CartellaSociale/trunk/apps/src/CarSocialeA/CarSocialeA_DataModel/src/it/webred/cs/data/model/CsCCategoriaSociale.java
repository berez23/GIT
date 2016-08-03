package it.webred.cs.data.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;
import java.util.Set;


/**
 * The persistent class for the CS_C_CATEGORIA_SOCIALE database table.
 * 
 */
@Entity
@Table(name="CS_C_CATEGORIA_SOCIALE")
@NamedQuery(name="CsCCategoriaSociale.findAll", query="SELECT c FROM CsCCategoriaSociale c")
public class CsCCategoriaSociale implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CS_C_CATEGORIA_SOCIALE_ID_GENERATOR", sequenceName="SQ_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CS_C_CATEGORIA_SOCIALE_ID_GENERATOR")
	private long id;

	private String abilitato;

	private String descrizione;

	private String tooltip;
	
	@Column(name="N_ORD")
	private Integer nOrd;

	//bi-directional many-to-one association to CsASoggettoCategoriaSoc
	@OneToMany(mappedBy="csCCategoriaSociale")
	private List<CsASoggettoCategoriaSoc> csASoggettoCategoriaSocs;

	//bi-directional many-to-one association to CsRelCatsocTipoInter
	@OneToMany(mappedBy="csCCategoriaSociale")
	private List<CsRelCatsocTipoInter> csRelCatsocTipoInters;

	//bi-directional many-to-one association to CsRelSettoreCatsoc
	@OneToMany(mappedBy="csCCategoriaSociale", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@OrderBy("id.settoreId DESC")
	private Set<CsRelSettoreCatsoc> csRelSettoreCatsocs;
	
	public CsCCategoriaSociale() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAbilitato() {
		return this.abilitato;
	}

	public void setAbilitato(String abilitato) {
		this.abilitato = abilitato;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getTooltip() {
		return this.tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public List<CsASoggettoCategoriaSoc> getCsASoggettoCategoriaSocs() {
		return this.csASoggettoCategoriaSocs;
	}

	public void setCsASoggettoCategoriaSocs(List<CsASoggettoCategoriaSoc> csASoggettoCategoriaSocs) {
		this.csASoggettoCategoriaSocs = csASoggettoCategoriaSocs;
	}

	public CsASoggettoCategoriaSoc addCsASoggettoCategoriaSoc(CsASoggettoCategoriaSoc csASoggettoCategoriaSoc) {
		getCsASoggettoCategoriaSocs().add(csASoggettoCategoriaSoc);
		csASoggettoCategoriaSoc.setCsCCategoriaSociale(this);

		return csASoggettoCategoriaSoc;
	}

	public CsASoggettoCategoriaSoc removeCsASoggettoCategoriaSoc(CsASoggettoCategoriaSoc csASoggettoCategoriaSoc) {
		getCsASoggettoCategoriaSocs().remove(csASoggettoCategoriaSoc);
		csASoggettoCategoriaSoc.setCsCCategoriaSociale(null);

		return csASoggettoCategoriaSoc;
	}

	public List<CsRelCatsocTipoInter> getCsRelCatsocTipoInters() {
		return this.csRelCatsocTipoInters;
	}

	public void setCsRelCatsocTipoInters(List<CsRelCatsocTipoInter> csRelCatsocTipoInters) {
		this.csRelCatsocTipoInters = csRelCatsocTipoInters;
	}

	public CsRelCatsocTipoInter addCsRelCatsocTipoInter(CsRelCatsocTipoInter csRelCatsocTipoInter) {
		getCsRelCatsocTipoInters().add(csRelCatsocTipoInter);
		csRelCatsocTipoInter.setCsCCategoriaSociale(this);

		return csRelCatsocTipoInter;
	}

	public CsRelCatsocTipoInter removeCsRelCatsocTipoInter(CsRelCatsocTipoInter csRelCatsocTipoInter) {
		getCsRelCatsocTipoInters().remove(csRelCatsocTipoInter);
		csRelCatsocTipoInter.setCsCCategoriaSociale(null);

		return csRelCatsocTipoInter;
	}

	public Set<CsRelSettoreCatsoc> getCsRelSettoreCatsocs() {
		return this.csRelSettoreCatsocs;
	}

	public void setCsRelSettoreCatsocs(Set<CsRelSettoreCatsoc> csRelSettoreCatsocs) {
		this.csRelSettoreCatsocs = csRelSettoreCatsocs;
	}

	public CsRelSettoreCatsoc addCsRelSettoreCatsoc(CsRelSettoreCatsoc csRelSettoreCatsoc) {
		getCsRelSettoreCatsocs().add(csRelSettoreCatsoc);
		csRelSettoreCatsoc.setCsCCategoriaSociale(this);

		return csRelSettoreCatsoc;
	}

	public CsRelSettoreCatsoc removeCsRelSettoreCatsoc(CsRelSettoreCatsoc csRelSettoreCatsoc) {
		getCsRelSettoreCatsocs().remove(csRelSettoreCatsoc);
		csRelSettoreCatsoc.setCsCCategoriaSociale(null);

		return csRelSettoreCatsoc;
	}

	public Integer getnOrd() {
		return nOrd;
	}

	public void setnOrd(Integer nOrd) {
		this.nOrd = nOrd;
	}
	
}