package it.webred.ct.service.comma336.web.bean.util;

import it.webred.ct.service.comma336.web.Comma336BaseBean;

import org.apache.log4j.Logger;

public class PageUiBean extends Comma336BaseBean{

	private static Logger logger = Logger.getLogger(PageUiBean.class.getName());

	private String pageOperatoriList = "/jsp/protected/empty.xhtml";
	private String pageResultList = "/jsp/protected/empty.xhtml";
	private String pageChiudiIstruttoria = "/jsp/protected/empty.xhtml";
	private String pageCategorieList = "/jsp/protected/empty.xhtml";
	private String pageCausaliList = "/jsp/protected/empty.xhtml";
	private String pageDocfa = "/jsp/protected/empty.xhtml";
	private String pageDocfaPlanimetrie = "/jsp/protected/empty.xhtml";
	private String pageCatSubjectList = "/jsp/protected/empty.xhtml";
	private String pageClassamento = "/jsp/protected/empty.xhtml";
	private String pageClassamentoZc = "/jsp/protected/empty.xhtml";
	
	public final String PAGE_EMPTY= "/jsp/protected/empty.xhtml"; 
		
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

	public void setPageDocfa(String pageDocfa) {
		this.pageDocfa = pageDocfa;
	}

	public String getPageDocfa() {
		return pageDocfa;
	}

	public void setPageDocfaPlanimetrie(String pageDocfaPlanimetrie) {
		this.pageDocfaPlanimetrie = pageDocfaPlanimetrie;
	}

	public String getPageDocfaPlanimetrie() {
		return pageDocfaPlanimetrie;
	}

	public void setPageCatSubjectList(String pageCatSubjectList) {
		this.pageCatSubjectList = pageCatSubjectList;
	}

	public String getPageCatSubjectList() {
		return pageCatSubjectList;
	}

	public void setPageClassamento(String pageClassamento) {
		this.pageClassamento = pageClassamento;
	}

	public String getPageClassamento() {
		return pageClassamento;
	}

	public String getPageClassamentoZc() {
		return pageClassamentoZc;
	}

	public void setPageClassamentoZc(String pageClassamentoZc) {
		this.pageClassamentoZc = pageClassamentoZc;
	}

}
