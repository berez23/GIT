package it.webred.diogene.metadata.beans;

/**
 * Bean che contiene i dati necessari per la definizione di una colonna come parte di una chiave.
 * @author Filippo Mazzini
 * @version $Revision: 1.1 $ $Date: 2007/05/16 07:35:33 $
 */
public class Key {
	
	/**
	 * Identificativo della chiave.
	 */
	private Long id;
	/**
	 * Nome della chiave.
	 */
	private String name;
	/**
	 * Identificativo della colonna che fa parte della chiave.
	 */
	private Long columnId;
	/**
	 * Nome (SQL) della colonna che fa parte della chiave.
	 */
	private String columnName;
	
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public Long getColumnId() {
		return columnId;
	}
	
	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		Key retVal = new Key();
		retVal.setId(getId() == null ? null : new Long(getId().longValue()));
		retVal.setName(getName() == null ? null : new String(getName()));
		retVal.setColumnId(getColumnId() == null ? null : new Long(getColumnId().longValue()));
		retVal.setColumnName(getColumnName() == null ? null : new String(getColumnName()));
		return retVal;
	}
	
}
