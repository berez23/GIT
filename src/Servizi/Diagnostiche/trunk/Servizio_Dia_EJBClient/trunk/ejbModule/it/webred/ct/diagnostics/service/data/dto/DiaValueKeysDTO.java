package it.webred.ct.diagnostics.service.data.dto;

import java.io.Serializable;
/**
 * Classe usata per memorizzare i dati delle chiavi trovate nelle diagnostiche e restituite dall'EJB DiaFindKeysService
 * @author Luca
 *
 */
public class DiaValueKeysDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String propertyName = "";
	private Object value;
	private String typeDati = "";
	
	public DiaValueKeysDTO(String propertyName, String typeDati) {
		super();
		this.propertyName = propertyName;
		this.typeDati = typeDati;
	}
	
	public DiaValueKeysDTO(String propertyName, Object value, String typeDati) {
		super();
		this.propertyName = propertyName;
		this.value = value;
		this.typeDati = typeDati;
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getTypeDati() {
		return typeDati;
	}
	public void setTypeDati(String typeDati) {
		this.typeDati = typeDati;
	}

}
