package it.webred.cs.data.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the CS_TB_UNITA_MISURA database table.
 * 
 */
@Entity
@Table(name="CS_TB_UNITA_MISURA")
@NamedQuery(name="CsTbUnitaMisura.findAll", query="SELECT c FROM CsTbUnitaMisura c")
public class CsTbUnitaMisura implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CS_TB_UNITA_MISURA_ID_GENERATOR", sequenceName="SQ_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CS_TB_UNITA_MISURA_ID_GENERATOR")
	private long id;

	private String abilitato;

	private String descrizione;

	private String tooltip;

	//bi-directional many-to-one association to CsIInterventoEseg
	@OneToMany(mappedBy="csTbUnitaMisura")
	private List<CsIInterventoEseg> csIInterventoEsegs;

	public CsTbUnitaMisura() {
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

	public List<CsIInterventoEseg> getCsIInterventoEsegs() {
		return this.csIInterventoEsegs;
	}

	public void setCsIInterventoEsegs(List<CsIInterventoEseg> csIInterventoEsegs) {
		this.csIInterventoEsegs = csIInterventoEsegs;
	}

	public CsIInterventoEseg addCsIInterventoEseg(CsIInterventoEseg csIInterventoEseg) {
		getCsIInterventoEsegs().add(csIInterventoEseg);
		csIInterventoEseg.setCsTbUnitaMisura(this);

		return csIInterventoEseg;
	}

	public CsIInterventoEseg removeCsIInterventoEseg(CsIInterventoEseg csIInterventoEseg) {
		getCsIInterventoEsegs().remove(csIInterventoEseg);
		csIInterventoEseg.setCsTbUnitaMisura(null);

		return csIInterventoEseg;
	}

}