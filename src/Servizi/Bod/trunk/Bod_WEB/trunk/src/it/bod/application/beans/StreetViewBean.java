package it.bod.application.beans;

import it.bod.application.common.MasterItem;

public class StreetViewBean extends MasterItem{

	private static final long serialVersionUID = 115797715721718404L;
	
	private String codiceEnte = "";
	private Double latitudine = 0d;
	private Double longitudine = 0d;
	
	public String getCodiceEnte() {
		return codiceEnte;
	}
	public void setCodiceEnte(String codiceEnte) {
		this.codiceEnte = codiceEnte;
	}
	public Double getLatitudine() {
		return latitudine;
	}
	public void setLatitudine(Double latitudine) {
		this.latitudine = latitudine;
	}
	public Double getLongitudine() {
		return longitudine;
	}
	public void setLongitudine(Double longitudine) {
		this.longitudine = longitudine;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}


}
