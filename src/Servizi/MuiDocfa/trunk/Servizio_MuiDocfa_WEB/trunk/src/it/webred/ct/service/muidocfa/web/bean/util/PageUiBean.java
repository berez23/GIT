package it.webred.ct.service.muidocfa.web.bean.util;

import it.webred.ct.service.muidocfa.web.bean.MuiDocfaBaseBean;

import org.apache.log4j.Logger;

public class PageUiBean extends MuiDocfaBaseBean{

	private String pageResultList = "/jsp/protected/empty.xhtml";
	private String pageSubjectList = "/jsp/protected/empty.xhtml";
	private String pageDocfa = "/jsp/protected/empty.xhtml";
	private String pageIciList = "/jsp/protected/empty.xhtml";
	private String pageTarList = "/jsp/protected/empty.xhtml";
	private String pageIciSubjectList = "/jsp/protected/empty.xhtml";
	private String pageTarSubjectList = "/jsp/protected/empty.xhtml";
	private String pageCategorieList = "/jsp/protected/empty.xhtml";
	private String pageCausaliList = "/jsp/protected/empty.xhtml";
	
	public String getPageResultList() {
		return pageResultList;
	}

	public void setPageResultList(String pageResultList) {
		this.pageResultList = pageResultList;
	}

	public String getPageSubjectList() {
		return pageSubjectList;
	}

	public void setPageSubjectList(String pageSubjectList) {
		this.pageSubjectList = pageSubjectList;
	}

	public String getPageIciList() {
		return pageIciList;
	}

	public void setPageIciList(String pageIciList) {
		this.pageIciList = pageIciList;
	}

	public String getPageDocfa() {
		return pageDocfa;
	}

	public void setPageDocfa(String pageDocfa) {
		this.pageDocfa = pageDocfa;
	}

	public String getPageIciSubjectList() {
		return pageIciSubjectList;
	}

	public void setPageIciSubjectList(String pageIciSubjectList) {
		this.pageIciSubjectList = pageIciSubjectList;
	}

	public String getPageTarList() {
		return pageTarList;
	}

	public void setPageTarList(String pageTarList) {
		this.pageTarList = pageTarList;
	}

	public String getPageTarSubjectList() {
		return pageTarSubjectList;
	}

	public void setPageTarSubjectList(String pageTarSubjectList) {
		this.pageTarSubjectList = pageTarSubjectList;
	}

	public String getPageCategorieList() {
		return pageCategorieList;
	}

	public void setPageCategorieList(String pageCategorieList) {
		this.pageCategorieList = pageCategorieList;
	}

	public String getPageCausaliList() {
		return pageCausaliList;
	}

	public void setPageCausaliList(String pageCausaliList) {
		this.pageCausaliList = pageCausaliList;
	}
	
}
