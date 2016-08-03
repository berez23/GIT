package it.escsolution.escwebgis.catasto.bean;

import it.escsolution.escwebgis.common.EscComboInterface;
import it.escsolution.escwebgis.common.EscComboObject;

import java.io.Serializable;

public class TipoAtto extends EscComboObject implements Serializable, EscComboInterface{
	
	private String code = "";
	private String descrizione = "";

	public TipoAtto() {
		
	}//-------------------------------------------------------------------------

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

}
