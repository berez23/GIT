package it.webred.fb.ejb.dto;

import it.webred.fb.data.model.DmBBene;

import java.io.Serializable;

public class DettaglioBeneDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DmBBene bene;

	public DmBBene getBene() {
		return bene;
	}

	public void setBene(DmBBene bene) {
		this.bene = bene;
	}
	
	

}
