package it.bod.application.beans;

import it.bod.application.common.MasterItem;

public class PdfDocfaBean extends MasterItem{

	private static final long serialVersionUID = 7081962629864218747L;
	
	private String link = "";
	private String nomeFile = "";
	private Boolean esiste = false; 
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Boolean getEsiste() {
		return esiste;
	}

	public void setEsiste(Boolean esiste) {
		this.esiste = esiste;
	}

	
	
}
