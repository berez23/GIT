package it.webred.ct.service.carContrib.data.access.cc.dto;

import it.webred.ct.service.carContrib.data.model.GesRic;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class GesRicDTO  extends CeTBaseObject implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private GesRic gesRic;
	public GesRic getGesRic() {
		return gesRic;
	}
	public void setGesRic(GesRic gesRic) {
		this.gesRic = gesRic;
	}
	

}
