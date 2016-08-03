package it.webred.gitland.data.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "INT_CATEGORIA")
public class IntCategoria extends MasterItem {
	@Id
	@Column(name="ID", unique = true, nullable = false)
	private Long id = 0l;
	
	@Column(name="DESCRIZIONE")
	private String descrizione = "";
	
	@OneToMany(mappedBy="categoria")
	private List<IntQuery> interrogazioni=null;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7717439305449623382L;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public List<IntQuery> getInterrogazioni() {
		return interrogazioni;
	}

	public void setInterrogazioni(List<IntQuery> interrogazioni) {
		this.interrogazioni = interrogazioni;
	}

}
