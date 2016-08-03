package it.webred.ct.service.comma340.data.access.dto;

import it.webred.ct.service.comma340.data.model.pratica.C340PratAllegato;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class C340PratAllegatoDTO extends CeTBaseObject {
	
	private static final long serialVersionUID = 1L;
	private C340PratAllegato allegato;

	public C340PratAllegato getAllegato() {
		return allegato;
	}

	public void setAllegato(C340PratAllegato allegato) {
		this.allegato = allegato;
	}	

}
