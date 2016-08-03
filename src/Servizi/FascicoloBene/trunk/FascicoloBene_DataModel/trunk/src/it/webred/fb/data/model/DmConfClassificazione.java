package it.webred.fb.data.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


/**
 * The persistent class for the DM_CONF_CLASSIFICAZIONE database table.
 * 
 */
@Entity
@Table(name="DM_CONF_CLASSIFICAZIONE")
@NamedQuery(name="DmConfClassificazione.findAll", query="SELECT d FROM DmConfClassificazione d")
public class DmConfClassificazione implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DmConfClassificazionePK id;

	@Column(name="DES_CATEGORIA")
	private String desCategoria;

	@Column(name="DES_MACRO")
	private String desMacro;

	private String tipo;
	
	//bi-directional many-to-one association to DmDDoc
	@OneToMany(mappedBy="dmConfClassificazione")
	private List<DmDDoc> dmDDocs;

	public DmConfClassificazione() {
	}

	public DmConfClassificazionePK getId() {
		return this.id;
	}

	public void setId(DmConfClassificazionePK id) {
		this.id = id;
	}

	public String getDesCategoria() {
		return this.desCategoria;
	}

	public void setDesCategoria(String desCategoria) {
		this.desCategoria = desCategoria;
	}

	public String getDesMacro() {
		return this.desMacro;
	}

	public void setDesMacro(String desMacro) {
		this.desMacro = desMacro;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public List<DmDDoc> getDmDDocs() {
		return this.dmDDocs;
	}

	public void setDmDDocs(List<DmDDoc> dmDDocs) {
		this.dmDDocs = dmDDocs;
	}

	public DmDDoc addDmDDoc(DmDDoc dmDDoc) {
		getDmDDocs().add(dmDDoc);
		dmDDoc.setDmConfClassificazione(this);

		return dmDDoc;
	}

	public DmDDoc removeDmDDoc(DmDDoc dmDDoc) {
		getDmDDocs().remove(dmDDoc);
		dmDDoc.setDmConfClassificazione(null);

		return dmDDoc;
	}

}