package it.bod.business.service.impl;

import java.util.Hashtable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.bod.application.beans.DatiGeneraliBean;
import it.bod.application.common.MasterClass;
import it.bod.business.service.DatiGeneraliService;
import it.bod.persistence.dao.DatiGeneraliDao;

@Service
@Transactional
public class DatiGeneraliServiceImpl  extends MasterClass implements DatiGeneraliService{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private DatiGeneraliDao dao = null;

	public List<DatiGeneraliBean> getList(Hashtable htQry) {
		List<DatiGeneraliBean> lst = dao.getList(htQry);
		return lst;
	}
	
	public DatiGeneraliDao getDao() {
		return dao;
	}
	public void setDao(DatiGeneraliDao dao) {
		this.dao = dao;
	}
	
	

}
