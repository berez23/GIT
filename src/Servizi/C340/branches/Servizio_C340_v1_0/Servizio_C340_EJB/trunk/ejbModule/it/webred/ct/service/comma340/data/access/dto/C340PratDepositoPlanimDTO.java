package it.webred.ct.service.comma340.data.access.dto;

import java.io.Serializable;

import it.webred.ct.service.comma340.data.model.pratica.C340PratDepositoPlanim;

public class C340PratDepositoPlanimDTO extends ModuloPraticaDTO implements Serializable {
	
	private C340PratDepositoPlanim pratica;

	public C340PratDepositoPlanim getPratica() {
		return pratica;
	}
	
	public void setPratica(C340PratDepositoPlanim pratica) {
		this.pratica = pratica;
	}

}
