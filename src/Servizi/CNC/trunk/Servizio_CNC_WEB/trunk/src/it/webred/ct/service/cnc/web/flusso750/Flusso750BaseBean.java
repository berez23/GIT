package it.webred.ct.service.cnc.web.flusso750;


import it.webred.ct.data.access.basic.cnc.flusso750.Flusso750Service;
import it.webred.ct.service.cnc.web.CNCBaseBean;

public class Flusso750BaseBean extends CNCBaseBean {
	
	protected Flusso750Service service;
	
	public Flusso750Service getFlusso750Service() {
		return service;
		//return (Flusso750Service) super.getCTRemoteService("Flusso750ServiceBean");
	}

	public Flusso750Service getService() {
		return service;
	}

	public void setService(Flusso750Service service) {
		this.service = service;
	}
	
	

}
