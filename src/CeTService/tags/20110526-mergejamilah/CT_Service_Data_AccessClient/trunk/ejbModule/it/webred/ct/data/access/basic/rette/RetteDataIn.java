package it.webred.ct.data.access.basic.rette;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class RetteDataIn extends CeTBaseObject implements Serializable{

	private Object obj;
	private Object obj2;
	
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public Object getObj2() {
		return obj2;
	}
	public void setObj2(Object obj2) {
		this.obj2 = obj2;
	}
	
}
