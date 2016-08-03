package it.webred.ct.data.access.basic.common;

import java.io.Serializable;
import java.util.Date;

import it.webred.ct.data.model.ici.VTIciCiviciAll;
import it.webred.ct.support.datarouter.CeTBaseObject;

public class CommonDataIn extends CeTBaseObject implements Serializable{
	private static final long serialVersionUID = 1L;
	private Object obj;
	
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
}
