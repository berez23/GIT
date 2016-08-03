package it.webred.ct.service.tares.data.model;

import java.io.Serializable;


import javax.persistence.Transient;

public class BaseItem implements Serializable{

	private static final long serialVersionUID = -2815339098301458350L;
	
	@Transient
	private Boolean sel = false;

	public Boolean getSel() {
		return sel;
	}
	
	public void setSel(Boolean sel) {
		this.sel = sel;
	}
	
}
