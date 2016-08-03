package it.webred.diogene.metadata.beans;

/**
 * Bean che contiene i dati necessari per la configurazione di una colonna nella pagina di mappatura colonne.
 * @author Filippo Mazzini
 * @version $Revision: 1.3 $ $Date: 2007/08/03 14:42:04 $
 */
public class Column {
	
	/**
	 * Identificativo della colonna.
	 */
	private Long id;
	/**
	 * Descrizione estesa della colonna.
	 */
	private String longDesc;
	/**
	 *	Nome (SQL) della colonna.
	 */
	private String name;
	/**
	 *	Alias della colonna.
	 */
	private String alias;
	/**
	 * Identificativo della tabella di cui fa parte la colonna.
	 */
	private Long tableId;
	/**
	 * Tipo java (classe Java secondo getClass().getName()) corrispondente al tipo SQL della colonna nel DB di origine.
	 */
	private String colType;
	/**
	 * Campo String (valori possibili: "true", "false" e "unknown") che contiene il valore da inserire nel campo 
	 * ATTRIBUTE_VAL (con ATTRIBUTE_SPEC = "NULLABLE") della tabella DC_ATTRIBUTE.
	 */
	private String nullable;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getColType() {
		return colType;
	}

	public void setColType(String colType) {
		this.colType = colType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLongDesc() {
		return longDesc;
	}

	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}

	public Long getTableId() {
		return tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
	public String getNullable() {
		return nullable;
	}

	public void setNullable(String nullable) {
		this.nullable = nullable;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		Column retVal = new Column();
		retVal.setId(getId() == null ? null : new Long(getId().longValue()));
		retVal.setLongDesc(getLongDesc() == null ? null : new String(getLongDesc()));
		retVal.setName(getName() == null ? null : new String(getName()));
		retVal.setAlias(getAlias() == null ? null : new String(getAlias()));
		retVal.setTableId(getTableId() == null ? null : new Long(getTableId().longValue()));
		retVal.setColType(getColType() == null ? null : new String(getColType()));
		retVal.setNullable(getNullable() == null ? null : new String(getNullable()));
		return retVal;
	}

}
