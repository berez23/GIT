package it.webred.gitland.ejb.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

public class BaseDTO extends CeTBaseObject {

	private static final long serialVersionUID = 6654037754769134600L;
	
	private Object obj;

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
}
