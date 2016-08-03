package it.escsolution.escwebgis.common;

import java.io.Serializable;

public class IndirizzoBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String sedime;
	private String indirizzo;
	
	private String colSedime;
	
	private String colIndirizzo;
	
	public String getColSedime() {
		return colSedime;
	}
	public void setColSedime(String colSedime) {
		this.colSedime = colSedime;
	}
	public String getColIndirizzo() {
		return colIndirizzo;
	}
	public void setColIndirizzo(String colIndirizzo) {
		this.colIndirizzo = colIndirizzo;
	}
	public String getSedime() {
		return sedime;
	}
	public void setSedime(String sedime) {
		this.sedime = sedime;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	

}
