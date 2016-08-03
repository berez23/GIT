package it.webred.ct.service.tares.web.tarsu;

import javax.persistence.Entity;

@Entity
public class ClasseTarsu {
	
	private Boolean sel = false;
	private String descrizione = "";

	public ClasseTarsu() {
		
	}//-------------------------------------------------------------------------

	public Boolean getSel() {
		return sel;
	}

	public void setSel(Boolean sel) {
		this.sel = sel;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	

}
