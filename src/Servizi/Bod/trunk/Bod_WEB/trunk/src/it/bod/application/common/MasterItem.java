package it.bod.application.common;

import java.io.Serializable;

public class MasterItem implements Serializable{
	
	private static final long serialVersionUID = -7871719791387214471L;
	
	private Long id = null;
	private String descrizione = "";

	public MasterItem() {

	}//-------------------------------------------------------------------------

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

}
