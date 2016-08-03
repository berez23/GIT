package it.webred.ct.service.intTerritorio.web.bean.util;



import it.webred.ct.service.intTerritorio.web.bean.InterrogazioneTerritorioBaseBean;

import org.apache.log4j.Logger;

public class PageUiBean extends InterrogazioneTerritorioBaseBean{

	private static Logger logger = Logger.getLogger(PageUiBean.class.getName());

	private String pageResultList = "/jsp/protected/empty.xhtml";
	
	public String getPageResultList() {
		return pageResultList;
	}

	public void setPageResultList(String pageResultList) {
		this.pageResultList = pageResultList;
	}

	
}
