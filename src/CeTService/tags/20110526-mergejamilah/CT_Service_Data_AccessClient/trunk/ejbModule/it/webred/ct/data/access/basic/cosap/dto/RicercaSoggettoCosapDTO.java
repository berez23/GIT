package it.webred.ct.data.access.basic.cosap.dto;

import it.webred.ct.data.access.basic.common.dto.RicercaSoggettoDTO;

import java.io.Serializable;

public class RicercaSoggettoCosapDTO extends RicercaSoggettoDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String idSoggCosap;
	
	public String getIdSoggCosap() {
		return idSoggCosap;
	}
	public void setIdSoggCosap(String idSoggCosap) {
		this.idSoggCosap = idSoggCosap;
	}
		
		
}
