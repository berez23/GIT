package it.webred.indice.fastsearch.bean;

import it.escsolution.escwebgis.common.EscComboInterface;
import it.escsolution.escwebgis.common.EscComboObject;

import java.io.Serializable;

public class TipoRicercaBean extends EscComboObject implements Serializable, EscComboInterface  {
	
	
	private static final long serialVersionUID = 1L;
	private String code;
	private String descrizione;
	
	public TipoRicercaBean(String codice, String descrizione){
		this.code = codice;
		this.descrizione = descrizione;
	}
	

	
	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public String getDescrizione() {
		return descrizione;
	}



	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}



	public String toString() {
		return descrizione;
	}
	
}
