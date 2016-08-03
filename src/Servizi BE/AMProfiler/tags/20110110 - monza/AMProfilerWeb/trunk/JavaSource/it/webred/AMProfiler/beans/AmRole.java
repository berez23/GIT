package it.webred.AMProfiler.beans;

// Generated 7-lug-2006 12.31.07 by Hibernate Tools 3.1.0.beta5

import java.util.HashSet;
import java.util.Set;

/**
 * AmRole generated by hbm2java
 */
public class AmRole implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private Set<AmItemRole> amItemRoles = new HashSet<AmItemRole>(0);

	// Constructors

	/** default constructor */
	public AmRole() {
	}

	/** minimal constructor */
	public AmRole(String name) {
		this.name = name;
	}

	/** full constructor */
	public AmRole(String name, Set<AmItemRole> amItemRoles) {
		this.name = name;
		this.amItemRoles = amItemRoles;
	}

	// Property accessors
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<AmItemRole> getAmItemRoles() {
		return amItemRoles;
	}

	public void setAmItemRoles(Set<AmItemRole> amItemRoles) {
		this.amItemRoles = amItemRoles;
	}

}