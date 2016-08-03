package it.webred.ct.service.cnc.web.statoriscossione;


import it.webred.ct.data.access.basic.cnc.flusso750.Flusso750Service;
import it.webred.ct.data.access.basic.cnc.statoriscossione.StatoRiscossioneService;
import it.webred.ct.service.cnc.web.CNCBaseBean;

public class StatoRiscossioneBaseBean extends CNCBaseBean {
	
	protected StatoRiscossioneService service;
	
	public StatoRiscossioneService getStatoRiscossioneService() {
		return service;
		// return (StatoRiscossioneService) super.getCTRemoteService("StatoRiscossioneServiceBean");
	}

	public StatoRiscossioneService getService() {
		return service;
	}

	public void setService(StatoRiscossioneService service) {
		this.service = service;
	}
	

}
