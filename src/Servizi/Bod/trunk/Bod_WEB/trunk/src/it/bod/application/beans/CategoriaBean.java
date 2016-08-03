package it.bod.application.beans;

import it.bod.application.common.MasterItem;

public class CategoriaBean  extends MasterItem{

	private static final long serialVersionUID = -5617423147641078311L;
	
	private String codice = "";

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	

}
