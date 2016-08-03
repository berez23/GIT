package it.escsolution.escwebgis.versamenti.bean;

import it.escsolution.escwebgis.common.EscComboInterface;
import it.escsolution.escwebgis.common.EscComboObject;

import java.io.Serializable;

public class VersComboObject extends EscComboObject implements Serializable, EscComboInterface {
	
	private String codice;
	private String descrizione;
	
	public String getCode(){
		return codice;
	}
	
	public String getDescrizione(){
		return descrizione;
	}
	
	public VersComboObject() {
	}
	
	public VersComboObject(String cod, String  des) {
		codice = cod;
		descrizione = des;
	}
	
	public String toString() {
		return descrizione;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

}

