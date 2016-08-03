package it.webred.fb.ejb.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;
import it.webred.fb.data.model.DmConfDocDir;
import it.webred.fb.data.model.DmConfDocLog;

public class StatoConfDoc extends CeTBaseObject {
	
	private static final long serialVersionUID = 1L;
	private DmConfDocDir configurazione;
	private DmConfDocLog log;
	
	public DmConfDocDir getConfigurazione() {
		return configurazione;
	}
	public void setConfigurazione(DmConfDocDir configurazione) {
		this.configurazione = configurazione;
	}
	public DmConfDocLog getLog() {
		return log;
	}
	public void setLog(DmConfDocLog log) {
		this.log = log;
	}

}
