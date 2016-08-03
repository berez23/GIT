package it.bod.business.service.impl;

import java.util.Hashtable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.bod.application.beans.CategoriaBean;
import it.bod.application.common.MasterClass;
import it.bod.business.service.CategoriaService;
import it.bod.persistence.dao.CategoriaDao;

@Service
@Transactional
public class CategoriaServiceImpl extends MasterClass implements CategoriaService{

	private static final long serialVersionUID = 3340745947994340732L;

	@Autowired
	private CategoriaDao dao = null;

	public List<CategoriaBean> getList(Hashtable htQry, Class<CategoriaBean> cls) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<CategoriaBean> getList(Hashtable htQry) {
		List<CategoriaBean> lst = dao.getList(htQry);
		return lst;
	}
	
	public CategoriaDao getDao() {
		return dao;
	}
	
	public void setDao(CategoriaDao dao) {
		this.dao = dao;
	}
	
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}


}
