package it.webred.diogene.metadata.beans;

import java.util.HashMap;
import java.util.Date;
import java.util.Iterator;

/**
 * Bean che contiene i dati necessari per la configurazione di una tabella nelle pagine di mappatura tabelle e 
 * mappatura colonne.
 * @author Filippo Mazzini
 * @version $Revision: 1.2 $ $Date: 2007/09/13 13:26:48 $
 */
public class Table {
	
	/**
	 *	Identificativo della tabella.
	 */
	private Long id;
	/**
	 *	Descrizione estesa della tabella.
	 */
	private String longDesc;
	/**
	 *	Nome SQL della tabella.
	 */
	private String sqlName;
	/**
	 *	Nome (descrittivo) della tabella.
	 */
	private String name;
	/**
	 *	Corrisponde al valore da inserire nel campo OWNER della tabella DC_ENTITY del DB di destinazione.
	 */
	private String owner;
	/**
	 *	HashMap delle colonne definite per la tabella.
	 */
	private HashMap<String, Column> columns;
	/**
	 *	HashMap delle colonne definite come chiave per la tabella.
	 */
	private HashMap<String, Key> keys;
	/**
	 *	Flag che indica se la tabella è stata effettivamente modificata.
	 */
	private boolean updated;
	/**
	 *	Data ultima modifica.
	 */
	private Date dtMod;
	
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getSqlName() {
		return sqlName;
	}

	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public HashMap<String, Column> getColumns() {
		return columns;
	}
	
	public void setColumns(HashMap<String, Column> columns) {
		this.columns = columns;
	}
	
	public HashMap<String, Key> getKeys() {
		return keys;
	}
	
	public void setKeys(HashMap<String, Key> keys) {
		this.keys = keys;
	}
	
	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public Date getDtMod() {
		return dtMod;
	}

	public void setDtMod(Date dtMod) {
		this.dtMod = dtMod;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		Table retVal = new Table();
		retVal.setId(this.getId());
		if (this.getColumns() == null) {
			retVal.setColumns(null);
		}else{
			HashMap<String, Column> newColumns = new HashMap<String, Column>();
			HashMap<String, Column> columns = this.getColumns();
			Iterator it = columns.keySet().iterator();
			while (it.hasNext()) {
				String key = (String)it.next();
				newColumns.put(key, columns.get(key));
			}
			retVal.setColumns(newColumns);
		}		
		retVal.setDtMod(this.getDtMod());
		if (this.getKeys() == null) {
			retVal.setKeys(null);
		}else{
			HashMap<String, Key> newKeys = new HashMap<String, Key>();
			HashMap<String, Key> keys = this.getKeys();
			Iterator it = keys.keySet().iterator();
			while (it.hasNext()) {
				String key = (String)it.next();
				newKeys.put(key, keys.get(key));
			}
			retVal.setKeys(newKeys);
		}
		retVal.setLongDesc(this.getLongDesc());
		retVal.setName(this.getName());
		retVal.setOwner(this.getOwner());
		retVal.setSqlName(this.getSqlName());
		retVal.setUpdated(this.isUpdated());
		return retVal;
	}
	
}
