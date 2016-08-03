package it.webred.cs.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the CS_C_MEDICO database table.
 * 
 */
@Entity
@Table(name="CS_C_MEDICO")
@NamedQuery(name="CsCMedico.findAll", query="SELECT c FROM CsCMedico c")
public class CsCMedico implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CS_C_MEDICO_ID_GENERATOR", sequenceName="SQ_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CS_C_MEDICO_ID_GENERATOR")
	private long id;

	private String abilitato;

	private String cell;

	private String citta;

	@Column(name="CODICE_REGIONALE")
	private String codiceRegionale;

	private String cognome;

	private String distretto;

	private String nome;

	private String tel;

	@Column(name="TIPO_MEDICO")
	private String tipoMedico;

	private String tooltip;

	//bi-directional many-to-one association to CsASoggettoMedico
	@OneToMany(mappedBy="csCMedico")
	private List<CsASoggettoMedico> csASoggettoMedicos;

	public CsCMedico() {
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

	public String getCell() {
		return this.cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public String getCitta() {
		return this.citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getCodiceRegionale() {
		return this.codiceRegionale;
	}

	public void setCodiceRegionale(String codiceRegionale) {
		this.codiceRegionale = codiceRegionale;
	}

	public String getCognome() {
		return this.cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getDistretto() {
		return this.distretto;
	}

	public void setDistretto(String distretto) {
		this.distretto = distretto;
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

	public String getTipoMedico() {
		return this.tipoMedico;
	}

	public void setTipoMedico(String tipoMedico) {
		this.tipoMedico = tipoMedico;
	}

	public String getTooltip() {
		return this.tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public List<CsASoggettoMedico> getCsASoggettoMedicos() {
		return this.csASoggettoMedicos;
	}

	public void setCsASoggettoMedicos(List<CsASoggettoMedico> csASoggettoMedicos) {
		this.csASoggettoMedicos = csASoggettoMedicos;
	}

	public CsASoggettoMedico addCsASoggettoMedico(CsASoggettoMedico csASoggettoMedico) {
		getCsASoggettoMedicos().add(csASoggettoMedico);
		csASoggettoMedico.setCsCMedico(this);

		return csASoggettoMedico;
	}

	public CsASoggettoMedico removeCsASoggettoMedico(CsASoggettoMedico csASoggettoMedico) {
		getCsASoggettoMedicos().remove(csASoggettoMedico);
		csASoggettoMedico.setCsCMedico(null);

		return csASoggettoMedico;
	}

}