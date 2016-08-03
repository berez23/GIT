package it.bod.business.service;

import it.bod.application.beans.CategoriaBean;

import java.util.Hashtable;
import java.util.List;

public interface CategoriaService {
	
	 public List<CategoriaBean> getList(Hashtable htQry, Class<CategoriaBean> cls);
	 public List<CategoriaBean> getList(Hashtable htQry);

}
