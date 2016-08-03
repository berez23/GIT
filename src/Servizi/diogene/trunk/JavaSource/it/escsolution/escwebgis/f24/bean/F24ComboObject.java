package it.escsolution.escwebgis.f24.bean;

import it.escsolution.escwebgis.common.EscComboInterface;
import it.escsolution.escwebgis.common.EscComboObject;

import java.io.Serializable;

public class F24ComboObject extends EscComboObject implements Serializable, EscComboInterface {
	
	private String codice;
	private String descrizione;
	
	public String getCode(){
		return codice;
	}
	
	public String getDescrizione(){
		return descrizione;
	}
	
	public F24ComboObject() {
	}
	
	public F24ComboObject(String cod, String  des) {
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

