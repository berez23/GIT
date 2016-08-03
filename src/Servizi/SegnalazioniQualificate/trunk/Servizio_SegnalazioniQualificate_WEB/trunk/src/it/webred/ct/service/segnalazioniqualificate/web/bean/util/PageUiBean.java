package it.webred.ct.service.segnalazioniqualificate.web.bean.util;

import it.webred.ct.service.segnalazioniqualificate.web.bean.SegnalazioniQualificateBaseBean;

import org.apache.log4j.Logger;

public class PageUiBean extends SegnalazioniQualificateBaseBean{

	private String pageOperatoriList = "/jsp/protected/empty.xhtml";
	private String pageResultList = "/jsp/protected/empty.xhtml";
	private String pageChiudiIstruttoria = "/jsp/protected/empty.xhtml";
	
	public String getPageResultList() {
		return pageResultList;
	}
	
	public String getPageOperatoriList() {
		return pageOperatoriList;
	}

	public void setPageOperatoriList(String pageOperatoriList) {
		this.pageOperatoriList = pageOperatoriList;
	}


	public void setPageResultList(String pageResultList) {
		this.pageResultList = pageResultList;
	}

	public void setPageChiudiIstruttoria(String pageChiudiIstruttoria) {
		this.pageChiudiIstruttoria = pageChiudiIstruttoria;
	}

	public String getPageChiudiIstruttoria() {
		return pageChiudiIstruttoria;
	}

}
