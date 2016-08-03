package it.webred.amprofiler.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the AM_COMUNE database table.
 * 
 */
@Entity
@Table(name="AM_COMUNE")
public class AmComune implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String belfiore;

	private String descrizione;

	//bi-directional many-to-one association to AmApplication
	@OneToMany(mappedBy="amComune")
	private Set<AmApplication> amApplications;

	//bi-directional many-to-one association to AmSection
	@OneToMany(mappedBy="amComune")
	private Set<AmSection> amSections;

    public AmComune() {
    }

	public String getBelfiore() {
		return this.belfiore;
	}

	public void setBelfiore(String belfiore) {
		this.belfiore = belfiore;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Set<AmApplication> getAmApplications() {
		return this.amApplications;
	}

	public void setAmApplications(Set<AmApplication> amApplications) {
		this.amApplications = amApplications;
	}
	
	public Set<AmSection> getAmSections() {
		return this.amSections;
	}

	public void setAmSections(Set<AmSection> amSections) {
		this.amSections = amSections;
	}
	
}