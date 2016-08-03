package it.bod.business.service;

import it.bod.application.beans.DocfaClasseBean;
import it.bod.application.common.MasterItem;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.List;

import javax.faces.model.SelectItem;

public interface BodLogicService {
	
	public List<Object> getList(Hashtable htQry);
	public List<Object> getListCaronte(Hashtable htQry);
	public List getList(Hashtable htQry, Class cls);
	public List getListCaronte(Hashtable htQry, Class cls);
	public MasterItem getItemById(Long id, Class cls);
	public MasterItem updItem(MasterItem mi, Class cls);
	public Long addItem(MasterItem item, Class cls);
	public Long addAndFlushItem(MasterItem item, Class cls);
	public void delItem(MasterItem sheet, Class cls);
	
	public List<DocfaClasseBean> getListaClassiComp(String zc, String categoriaEdilizia, String tipologiaIntervento, Hashtable hashClassiMax, String flgAscensore, Double consistenza, Double tariffa);
	public List<DocfaClasseBean> getListaClassiComp(String zc, String categoriaEdilizia, Double tariffa);
	public BigDecimal getValoreCommercialeMq(String microzona, String tipologiaEdilizia, String stato);
	public Double getDocfaRapporto();
	public  List<SelectItem> getListaZC(String foglio);
	public String getMicrozona(String foglio, String zc);
}
