package it.webred.ct.service.carContrib.data.access.cc.dto;

import it.webred.ct.service.carContrib.data.model.Richieste;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class RichiesteDTO  extends CeTBaseObject implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Richieste rich;
	public Richieste getRich() {
		return rich;
	}
	public void setRich(Richieste rich) {
		this.rich = rich;
	}
	

}
