package it.escsolution.escwebgis.cosap.bean;

import it.escsolution.escwebgis.common.EscComboInterface;
import it.escsolution.escwebgis.common.EscComboObject;
import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;

public class TipoOccupazione extends EscComboObject implements Serializable,EscComboInterface {
	private String codice;
	private String descrizione;
	
	public TipoOccupazione(String codice, String descrizione) {
		this.codice = codice;
		this.descrizione = descrizione;
	}
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getCode(){
		return codice;
	}
	
}
