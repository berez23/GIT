package it.webred.amprofiler.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;
import javax.persistence.CascadeType;

/**
 * The persistent class for the AM_SECTION database table.
 * 
 */
@Entity
@Table(name="AM_SECTION")
public class AmSection implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String name;

	//bi-directional many-to-one association to AmKeyValue
	@OneToMany(mappedBy="amSection", cascade=CascadeType.ALL )
	private Set<AmKeyValue> amKeyValues;

	//bi-directional many-to-one association to AmApplication
    @ManyToOne
	@JoinColumn(name="FK_AM_APPLICATION")
	private AmApplication amApplication;

	//bi-directional many-to-one association to AmComune
    @ManyToOne
	@JoinColumn(name="FK_AM_COMUNE")
	private AmComune amComune;

    public AmSection() {
    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<AmKeyValue> getAmKeyValues() {
		return this.amKeyValues;
	}

	public void setAmKeyValues(Set<AmKeyValue> amKeyValues) {
		this.amKeyValues = amKeyValues;
	}
	
	public AmApplication getAmApplication() {
		return this.amApplication;
	}

	public void setAmApplication(AmApplication amApplication) {
		this.amApplication = amApplication;
	}
	
	public AmComune getAmComune() {
		return this.amComune;
	}

	public void setAmComune(AmComune amComune) {
		this.amComune = amComune;
	}
	
}