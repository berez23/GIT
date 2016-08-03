package it.webred.ct.service.comma340.data.access.dto;

import it.webred.ct.service.comma340.data.model.pratica.C340PratDepositoPlanim;

public class C340PratDepositoPlanimDTO extends ModuloPraticaDTO  {
	
	private static final long serialVersionUID = 1L;
	private C340PratDepositoPlanim pratica;

	public C340PratDepositoPlanim getPratica() {
		return pratica;
	}
	
	public void setPratica(C340PratDepositoPlanim pratica) {
		this.pratica = pratica;
	}

}
