package it.webred.ct.service.cnc.web.flusso290;

import it.webred.ct.data.access.basic.cnc.flusso290.Flusso290Service;
import it.webred.ct.service.cnc.web.CNCBaseBean;
import it.webred.ct.service.cnc.web.common.ServiceLocator;

public class Flusso290BaseBean extends CNCBaseBean {
	
	protected Flusso290Service service;
	
	
	public Flusso290Service getFlusso290Service() {
	
		return this.service;
		//throw new UnsupportedOperationException();
		//return (Flusso290Service) super.getCTRemoteService("Flusso290ServiceBean");
		
	}


	public Flusso290Service getService() {
		return service;
	}


	public void setService(Flusso290Service service) {
		this.service = service;
	}
	
	

}
