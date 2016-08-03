package it.bod.business.service;

import it.bod.application.beans.UiuBean;

import java.util.Hashtable;
import java.util.List;

public interface UiuService {
	
	public List<UiuBean> getListaIndirizzi();
	public List<UiuBean> getListaIndirizzi(String des);
	public List<UiuBean> getList(Hashtable htQry,  Class cls);
	public List<Object> getList(Hashtable htQry);

}
