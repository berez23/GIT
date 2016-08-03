package it.webred.ct.service.comma340.data.access.dto;

import java.io.Serializable;

import it.webred.ct.service.comma340.data.model.pratica.C340PratRettificaSup;

public class C340PratRettificaSupDTO extends ModuloPraticaDTO implements Serializable {
	
	private C340PratRettificaSup pratica;

	public void setPratica(C340PratRettificaSup pratica) {
		this.pratica = pratica;
	}

	public C340PratRettificaSup getPratica() {
		return pratica;
	}

}
