package it.webred.ct.service.tares.data.access.dto;

import java.io.Serializable;

public class CriteriaBase implements Serializable{

	private static final long serialVersionUID = -1967309227485041969L;
	
	private Integer limit = null;		//numero delle righe da recuperare
	private Integer offset = null;		//punto di partenza

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	

}
