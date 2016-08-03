package it.escsolution.escwebgis.mappe.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;

public class Link extends EscObject implements Serializable {

	private String gruppo;
	private String descrizione;
	private String path;
	
	public String getGruppo() {
		return gruppo;
	}
	public void setGruppo(String gruppo) {
		this.gruppo = gruppo;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
