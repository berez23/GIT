package it.webred.ct.data.access.basic.concedilizie.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

public class RicercaConcEdilizieDTO extends CeTBaseObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idConc;
	private String idExtConc;
	
	public String getIdConc() {
		return idConc;
	}

	public void setIdConc(String idConc) {
		this.idConc = idConc;
	}

	public String getIdExtConc() {
		return idExtConc;
	}

	public void setIdExtConc(String idExtConc) {
		this.idExtConc = idExtConc;
	}
	

}
