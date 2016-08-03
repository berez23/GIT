package it.webred.diogene.querybuilder.beans;

import java.io.Serializable;

public class EntityNamesToExport implements Serializable{

	private static final long serialVersionUID = -1L;
	
	private String entityFromName;
	private String entityToName;
	
	public EntityNamesToExport() {
		super();
	}
	
	public EntityNamesToExport(String entityFromName, String entityToName) {
		this();
		this.entityFromName = entityFromName;
		this.entityToName = entityToName;
	}
	
	public String getEntityFromName() {
		return entityFromName;
	}
	public void setEntityFromName(String entityFromName) {
		this.entityFromName = entityFromName;
	}
	public String getEntityToName() {
		return entityToName;
	}
	public void setEntityToName(String entityToName) {
		this.entityToName = entityToName;
	}
	
}
