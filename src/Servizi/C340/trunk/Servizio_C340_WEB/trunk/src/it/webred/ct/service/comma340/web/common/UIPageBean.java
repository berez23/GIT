package it.webred.ct.service.comma340.web.common;

import java.io.Serializable;

public class UIPageBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String pageResultList = "/jsp/protected/empty.xhtml";
	
	private String page = "/jsp/protected/empty.xhtml";

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public void setPageResultList(String pageResultList) {
		this.pageResultList = pageResultList;
	}

	public String getPageResultList() {
		return pageResultList;
	}

}


