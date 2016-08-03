package it.webred.ct.service.spclass.web.bean.util;

import it.webred.ct.service.spclass.web.SpProfClassamentoBaseBean;
import it.webred.ct.service.spclass.web.bean.ClassamentoDueBean;
import it.webred.ct.service.spclass.web.bean.ClassamentoTreBean;
import it.webred.ct.service.spclass.web.bean.ClassamentoUnoBean;

import org.apache.log4j.Logger;

public class PageBean extends SpProfClassamentoBaseBean{
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(PageBean.class.getName());

	
	public String goHome(){
		return "classamento.home";
	}
	
	public String goCalcolo1(){
		ClassamentoUnoBean uno = (ClassamentoUnoBean)this.getBeanReference("classamentoUnoBck");
		uno.init();
		return "classamento.calcolo1";
	}
	
	public String goCalcolo2(){
		ClassamentoDueBean due = (ClassamentoDueBean)this.getBeanReference("classamentoDueBck");
		due.initDue();
		return "classamento.calcolo2";
	}
	
	public String goCalcolo3(){
		ClassamentoDueBean due = (ClassamentoDueBean)this.getBeanReference("classamentoDueBck");
		due.initTre();
		return "classamento.calcolo3";
	}
	
	public String goCalcolo4(){
		ClassamentoTreBean tre = (ClassamentoTreBean)this.getBeanReference("classamentoTreBck");
		tre.init();
		return "classamento.calcolo4";
	}
}
