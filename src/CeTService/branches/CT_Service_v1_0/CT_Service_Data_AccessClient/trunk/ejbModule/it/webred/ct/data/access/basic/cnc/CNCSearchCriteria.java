package it.webred.ct.data.access.basic.cnc;

import java.io.Serializable;

public class CNCSearchCriteria implements Serializable {
	
	private int executeSearch;

	
	
	public int getExecuteSearch() {
		return executeSearch;
	}

	public void setExecuteSearch(int executeSearch) {
		this.executeSearch = executeSearch;
	}
	
	
	public boolean executeSearch() {
		return executeSearch == 1;
	}
}
