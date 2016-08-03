package it.bod.application.beans;

import it.bod.application.common.MasterItem;

public class DestinazioneFunzionaleBean extends MasterItem{

	private static final long serialVersionUID = -3250658818595674717L;
	
	private String codut = "";
	private String descrizione = "";
	private Long id_cat = null;
	
	public String getCodut() {
		return codut;
	}
	public void setCodut(String codut) {
		this.codut = codut;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public Long getId_cat() {
		return id_cat;
	}
	public void setId_cat(Long id_cat) {
		this.id_cat = id_cat;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
}
