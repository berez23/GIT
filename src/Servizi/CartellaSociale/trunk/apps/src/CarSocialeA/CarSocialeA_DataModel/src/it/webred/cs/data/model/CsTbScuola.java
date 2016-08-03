package it.webred.cs.data.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the CS_TB_SCUOLA database table.
 * 
 */
@Entity
@Table(name="CS_TB_SCUOLA")
@NamedQuery(name="CsTbScuola.findAll", query="SELECT c FROM CsTbScuola c")
public class CsTbScuola implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CS_TB_SCUOLA_ID_GENERATOR", sequenceName="SQ_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CS_TB_SCUOLA_ID_GENERATOR")
	private long id;

	private String abilitato;

	private String descrizione;

	private String tooltip;
	
	@Column(name="ANNO_SCOLASTICO")
	private String annoScolastico;
	
	@Column(name="TIPO_SCUOLA_ID")
	private Long tipoScuolaId;

	public CsTbScuola() {
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

	public String getAnnoScolastico() {
		return annoScolastico;
	}

	public void setAnnoScolastico(String annoScolastico) {
		this.annoScolastico = annoScolastico;
	}

	public Long getTipoScuolaId() {
		return tipoScuolaId;
	}

	public void setTipoScuolaId(Long tipoScuolaId) {
		this.tipoScuolaId = tipoScuolaId;
	}
	
}