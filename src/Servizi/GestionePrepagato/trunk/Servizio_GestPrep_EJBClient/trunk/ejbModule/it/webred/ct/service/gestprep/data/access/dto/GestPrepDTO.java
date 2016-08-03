package it.webred.ct.service.gestprep.data.access.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class GestPrepDTO extends CeTBaseObject implements Serializable {
	
	private Object obj;

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	

}
