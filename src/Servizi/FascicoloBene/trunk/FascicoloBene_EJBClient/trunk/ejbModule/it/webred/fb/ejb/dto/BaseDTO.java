package it.webred.fb.ejb.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

public class BaseDTO extends CeTBaseObject {

	private Object obj;

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
}
