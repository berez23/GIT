package it.webred.ct.service.spprof.data.access.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class SpProfDTO extends CeTBaseObject implements Serializable {

	private Object obj;
	
	private int start;
	
	private int record;

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getRecord() {
		return record;
	}

	public void setRecord(int record) {
		this.record = record;
	}
	
}
