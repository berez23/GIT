package it.webred.amprofiler.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the AM_APPLICATION database table.
 * 
 */
@Entity
@Table(name="AM_APPLICATION")
public class AmApplication implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String name;

	@Column(name="APP_CATEGORY")
	private String appCategory;

	@Column(name="APP_TYPE")
	private String appType;

	private String url;

	//bi-directional many-to-one association to AmSection
	@OneToMany(mappedBy="amApplication")
	private Set<AmSection> amSections;

	//bi-directional many-to-one association to AmComune
    @ManyToOne
	@JoinColumn(name="FK_AM_COMUNE")
	private AmComune amComune;

    public AmApplication() {
    }

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAppCategory() {
		return this.appCategory;
	}

	public void setAppCategory(String appCategory) {
		this.appCategory = appCategory;
	}

	public String getAppType() {
		return this.appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Set<AmSection> getAmSections() {
		return this.amSections;
	}

	public void setAmSections(Set<AmSection> amSections) {
		this.amSections = amSections;
	}
	
	public AmComune getAmComune() {
		return this.amComune;
	}

	public void setAmComune(AmComune amComune) {
		this.amComune = amComune;
	}
	
}