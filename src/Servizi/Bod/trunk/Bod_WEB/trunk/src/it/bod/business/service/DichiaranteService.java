package it.bod.business.service;

import it.bod.application.beans.DichiaranteBean;

import java.util.Hashtable;
import java.util.List;

public interface DichiaranteService {
	
	public List<DichiaranteBean> getList();
	public List<Object> getList(Hashtable htQry);
	public List<DichiaranteBean> getListDicCognomi(String cog);
	public List<DichiaranteBean> getListProCognomi(String cog);
	
}
