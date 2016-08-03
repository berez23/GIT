package it.webred.cs.data.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

/**
 * The persistent class for the CS_TB_TIPO_RETTA database table.
 * 
 */
@Entity
@Table(name="CS_TB_TIPO_RETTA")
@NamedQuery(name="CsTbTipoRetta.findAll", query="SELECT c FROM CsTbTipoRetta c")
public class CsTbTipoRetta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CS_TB_TIPO_RETTA_ID_GENERATOR", sequenceName="SQ_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CS_TB_TIPO_RETTA_ID_GENERATOR")
	private long id;

	private String abilitato;

	private String descrizione;

	private String tooltip;

	//bi-directional many-to-one association to CsIResiMinore
	@OneToMany(mappedBy="csTbTipoRetta")
	private List<CsIResiMinore> csIResiMinores;

	//bi-directional many-to-one association to CsIResiAdulti
	@OneToMany(mappedBy="csTbTipoRetta")
	private List<CsIResiAdulti> csIResiAdultis;

	//bi-directional many-to-one association to CsISemiResiMin
	@OneToMany(mappedBy="csTbTipoRetta")
	private List<CsISemiResiMin> csISemiResiMins;
	
	public CsTbTipoRetta() {
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

	public List<CsIResiMinore> getCsIResiMinores() {
		return this.csIResiMinores;
	}

	public void setCsIResiMinores(List<CsIResiMinore> csIResiMinores) {
		this.csIResiMinores = csIResiMinores;
	}

	public CsIResiMinore addCsIResiMinore(CsIResiMinore csIResiMinore) {
		getCsIResiMinores().add(csIResiMinore);
		csIResiMinore.setCsTbTipoRetta(this);

		return csIResiMinore;
	}

	public CsIResiMinore removeCsIResiMinore(CsIResiMinore csIResiMinore) {
		getCsIResiMinores().remove(csIResiMinore);
		csIResiMinore.setCsTbTipoRetta(null);

		return csIResiMinore;
	}
	
	public List<CsIResiAdulti> getCsIResiAdultis() {
		return this.csIResiAdultis;
	}

	public void setCsIResiAdultis(List<CsIResiAdulti> csIResiAdultis) {
		this.csIResiAdultis = csIResiAdultis;
	}

	public CsIResiAdulti addCsIResiAdulti(CsIResiAdulti csIResiAdulti) {
		getCsIResiAdultis().add(csIResiAdulti);
		csIResiAdulti.setCsTbTipoRetta(this);

		return csIResiAdulti;
	}

	public CsIResiAdulti removeCsIResiAdulti(CsIResiAdulti csIResiAdulti) {
		getCsIResiAdultis().remove(csIResiAdulti);
		csIResiAdulti.setCsTbTipoRetta(null);

		return csIResiAdulti;
	}

	public List<CsISemiResiMin> getCsISemiResiMins() {
		return this.csISemiResiMins;
	}

	public void setCsISemiResiMins(List<CsISemiResiMin> csISemiResiMins) {
		this.csISemiResiMins = csISemiResiMins;
	}

	public CsISemiResiMin addCsISemiResiMin(CsISemiResiMin csISemiResiMin) {
		getCsISemiResiMins().add(csISemiResiMin);
		csISemiResiMin.setCsTbTipoRetta(this);

		return csISemiResiMin;
	}

	public CsISemiResiMin removeCsISemiResiMin(CsISemiResiMin csISemiResiMin) {
		getCsISemiResiMins().remove(csISemiResiMin);
		csISemiResiMin.setCsTbTipoRetta(null);

		return csISemiResiMin;
	}

}