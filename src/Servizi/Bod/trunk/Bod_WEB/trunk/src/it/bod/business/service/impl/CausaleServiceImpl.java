package it.bod.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.bod.application.beans.CausaleBean;
import it.bod.application.common.MasterClass;
import it.bod.business.service.CausaleService;
import it.bod.persistence.dao.CausaleDao;

@Service
@Transactional
public class CausaleServiceImpl extends MasterClass implements CausaleService{
	
	private static final long serialVersionUID = 8603743556507150761L;
	
	@Autowired
	private CausaleDao dao = null;

	public CausaleServiceImpl() {
		// TODO Auto-generated constructor stub
	}//-------------------------------------------------------------------------

	public List<CausaleBean> getList() {
		List<CausaleBean> lst = null;
		lst = dao.getList();
		return lst;
	}//-------------------------------------------------------------------------

	public List<CausaleBean> getListDescrizioni(){
		List<CausaleBean> lst = null;
		lst = dao.getListDescrizioni();
		return lst;
	}//-------------------------------------------------------------------------

	public CausaleDao getDao() {
		return dao;
	}

	public void setDao(CausaleDao dao) {
		this.dao = dao;
	}

	
}
