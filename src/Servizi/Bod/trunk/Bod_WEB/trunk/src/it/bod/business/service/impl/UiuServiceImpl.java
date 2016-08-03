package it.bod.business.service.impl;

import java.util.Hashtable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.bod.application.beans.UiuBean;
import it.bod.application.common.MasterClass;
import it.bod.business.service.UiuService;
import it.bod.persistence.dao.UiuDao;

@Service
@Transactional
public class UiuServiceImpl extends MasterClass implements UiuService {

	private static final long serialVersionUID = -2543746846477323384L;
	
	@Autowired
	private UiuDao dao = null;

	public List<UiuBean> getListaIndirizzi() {
		List<UiuBean> lst = dao.getListaIndirizzi();
		return lst;
	}
	
	public List<UiuBean> getListaIndirizzi(String des) {
		List<UiuBean> lst = dao.getListaIndirizzi(des);
		return lst;
	}
	
	public UiuDao getDao() {
		return dao;
	}

	public void setDao(UiuDao dao) {
		this.dao = dao;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public List getList(Hashtable htQry, Class cls) {
		List lst = dao.getList(htQry, cls);
		return lst;
	}
	
	public List getList(Hashtable htQry) {
		List lst = dao.getList(htQry);
		return lst;
	}
	
}
