package it.webred.cs.data.model;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


/**
 * The persistent class for the CS_O_SETTORE database table.
 * 
 */
@Entity
@Table(name="CS_O_SETTORE")
@NamedQuery(name="CsOSettore.findAll", query="SELECT c FROM CsOSettore c")
public class CsOSettore implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CS_O_SETTORE_ID_GENERATOR", sequenceName="SQ_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CS_O_SETTORE_ID_GENERATOR")
	private Long id;

	private String abilitato;

	private String cell;

	private String email;

	private String fax;

	private String nome;

	private String nome2;
	
	private String tel;

	private String tooltip;
	
	private String orario;

	//mono-directional one-to-one association to CsAAnaIndirizzo
	@OneToOne
	@JoinColumn(name="ANA_INDIRIZZO_ID")
	private CsAAnaIndirizzo csAAnaIndirizzo;
	
	//bi-directional many-to-one association to CsItStep
	@OneToMany(mappedBy="csOSettore1")
	private List<CsItStep> csItSteps1;

	//bi-directional many-to-one association to CsItStep
	@OneToMany(mappedBy="csOSettore2")
	private List<CsItStep> csItSteps2;

	//bi-directional many-to-one association to CsOOperatoreSettore
	@OneToMany(mappedBy="csOSettore")
	private List<CsOOperatoreSettore> csOOperatoreSettores;

	//bi-directional many-to-one association to CsOOrganizzazione
	@ManyToOne
	@JoinColumn(name="ORGANIZZAZIONE_ID")
	private CsOOrganizzazione csOOrganizzazione;

	//bi-directional many-to-one association to CsAlert
	@OneToMany(mappedBy="csOSettore1")
	private List<CsAlert> csAlerts1;

	//bi-directional many-to-one association to CsAlert
	@OneToMany(mappedBy="csOSettore2")
	private List<CsAlert> csAlerts2;

	//bi-directional one-to-one association to CsCComunita
	@OneToOne(mappedBy="csOSettore")
	private CsCComunita csCComunita;

	//bi-directional many-to-one association to CsRelSettoreCatsoc
	@OneToMany(mappedBy="csOSettore", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<CsRelSettoreCatsoc> csRelSettoreCatsocs;

	public CsOSettore() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAbilitato() {
		return this.abilitato;
	}

	public void setAbilitato(String abilitato) {
		this.abilitato = abilitato;
	}

	public CsAAnaIndirizzo getCsAAnaIndirizzo() {
		return csAAnaIndirizzo;
	}

	public void setCsAAnaIndirizzo(CsAAnaIndirizzo csAAnaIndirizzo) {
		this.csAAnaIndirizzo = csAAnaIndirizzo;
	}

	public String getCell() {
		return this.cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
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

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getTooltip() {
		return this.tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public List<CsItStep> getCsItSteps1() {
		return this.csItSteps1;
	}

	public void setCsItSteps1(List<CsItStep> csItSteps1) {
		this.csItSteps1 = csItSteps1;
	}

	public CsItStep addCsItSteps1(CsItStep csItSteps1) {
		getCsItSteps1().add(csItSteps1);
		csItSteps1.setCsOSettore1(this);

		return csItSteps1;
	}

	public CsItStep removeCsItSteps1(CsItStep csItSteps1) {
		getCsItSteps1().remove(csItSteps1);
		csItSteps1.setCsOSettore1(null);

		return csItSteps1;
	}

	public List<CsItStep> getCsItSteps2() {
		return this.csItSteps2;
	}

	public void setCsItSteps2(List<CsItStep> csItSteps2) {
		this.csItSteps2 = csItSteps2;
	}

	public CsItStep addCsItSteps2(CsItStep csItSteps2) {
		getCsItSteps2().add(csItSteps2);
		csItSteps2.setCsOSettore2(this);

		return csItSteps2;
	}

	public CsItStep removeCsItSteps2(CsItStep csItSteps2) {
		getCsItSteps2().remove(csItSteps2);
		csItSteps2.setCsOSettore2(null);

		return csItSteps2;
	}

	public List<CsOOperatoreSettore> getCsOOperatoreSettores() {
		return this.csOOperatoreSettores;
	}

	public void setCsOOperatoreSettores(List<CsOOperatoreSettore> csOOperatoreSettores) {
		this.csOOperatoreSettores = csOOperatoreSettores;
	}

	public CsOOperatoreSettore addCsOOperatoreSettore(CsOOperatoreSettore csOOperatoreSettore) {
		getCsOOperatoreSettores().add(csOOperatoreSettore);
		csOOperatoreSettore.setCsOSettore(this);

		return csOOperatoreSettore;
	}

	public CsOOperatoreSettore removeCsOOperatoreSettore(CsOOperatoreSettore csOOperatoreSettore) {
		getCsOOperatoreSettores().remove(csOOperatoreSettore);
		csOOperatoreSettore.setCsOSettore(null);

		return csOOperatoreSettore;
	}

	public CsOOrganizzazione getCsOOrganizzazione() {
		return this.csOOrganizzazione;
	}

	public void setCsOOrganizzazione(CsOOrganizzazione csOOrganizzazione) {
		this.csOOrganizzazione = csOOrganizzazione;
	}

	public List<CsAlert> getCsAlerts1() {
		return this.csAlerts1;
	}

	public void setCsAlerts1(List<CsAlert> csAlerts1) {
		this.csAlerts1 = csAlerts1;
	}

	public CsAlert addCsAlerts1(CsAlert csAlerts1) {
		getCsAlerts1().add(csAlerts1);
		csAlerts1.setCsOSettore1(this);

		return csAlerts1;
	}

	public CsAlert removeCsAlerts1(CsAlert csAlerts1) {
		getCsAlerts1().remove(csAlerts1);
		csAlerts1.setCsOSettore1(null);

		return csAlerts1;
	}

	public List<CsAlert> getCsAlerts2() {
		return this.csAlerts2;
	}

	public void setCsAlerts2(List<CsAlert> csAlerts2) {
		this.csAlerts2 = csAlerts2;
	}

	public CsAlert addCsAlerts2(CsAlert csAlerts2) {
		getCsAlerts2().add(csAlerts2);
		csAlerts2.setCsOSettore2(this);

		return csAlerts2;
	}

	public CsAlert removeCsAlerts2(CsAlert csAlerts2) {
		getCsAlerts2().remove(csAlerts2);
		csAlerts2.setCsOSettore2(null);

		return csAlerts2;
	}

	public CsCComunita getCsCComunita() {
		return this.csCComunita;
	}

	public void setCsCComunita(CsCComunita csCComunita) {
		this.csCComunita = csCComunita;
	}

	public Set<CsRelSettoreCatsoc> getCsRelSettoreCatsocs() {
		return this.csRelSettoreCatsocs;
	}

	public void setCsRelSettoreCatsocs(Set<CsRelSettoreCatsoc> csRelSettoreCatsocs) {
		this.csRelSettoreCatsocs = csRelSettoreCatsocs;
	}

	public CsRelSettoreCatsoc addCsRelSettoreCatsoc(CsRelSettoreCatsoc csRelSettoreCatsoc) {
		getCsRelSettoreCatsocs().add(csRelSettoreCatsoc);
		csRelSettoreCatsoc.setCsOSettore(this);

		return csRelSettoreCatsoc;
	}

	public CsRelSettoreCatsoc removeCsRelSettoreCatsoc(CsRelSettoreCatsoc csRelSettoreCatsoc) {
		getCsRelSettoreCatsocs().remove(csRelSettoreCatsoc);
		csRelSettoreCatsoc.setCsOSettore(null);

		return csRelSettoreCatsoc;
	}

	public String getNome2() {
		return nome2;
	}

	public void setNome2(String nome2) {
		this.nome2 = nome2;
	}

	public String getOrario() {
		return orario;
	}

	public void setOrario(String orario) {
		this.orario = orario;
	}

}