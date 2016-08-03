package it.webred.ct.service.comma340.web.catasto;

import it.webred.ct.data.access.basic.catasto.*;
import it.webred.ct.data.access.basic.cnc.flusso290.Flusso290Service;
import it.webred.ct.service.comma340.web.Comma340BaseBean;

public class CatastoBaseBean extends Comma340BaseBean {
	
	protected CatastoService catastoService;
	
	
	public void setCatastoService(CatastoService catastoService) {
		this.catastoService = catastoService;
	}

	public CatastoService getCatastoService() {
		
		return this.catastoService;
		//return (CatastoService) super.getCTRemoteService("CatastoServiceBean");
	}

}
