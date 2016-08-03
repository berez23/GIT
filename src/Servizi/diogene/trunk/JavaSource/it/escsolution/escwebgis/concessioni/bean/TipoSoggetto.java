package it.escsolution.escwebgis.concessioni.bean;

import it.escsolution.escwebgis.common.EscComboInterface;
import it.escsolution.escwebgis.common.EscComboObject;

import java.io.Serializable;

public class TipoSoggetto extends EscComboObject implements Serializable,EscComboInterface {
	
	private String valore;
	private String testo;

	public String getCode(){
		return valore;
	}
	public String getDescrizione(){
		return testo;
	}
	public TipoSoggetto() {
	}
	public TipoSoggetto(String valore, String testo) {
		this.valore = valore;
		this.testo = testo;
	}
	public String toString() {
		return testo;
	}
	public String getValore() {
		return valore;
	}
	public String getTesto() {
		return testo;
	}
	public void setValore(String valore) {
		this.valore = valore;
	}
	public void setTesto(String testo) {
		this.testo = testo;
	}

}

