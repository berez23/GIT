package it.bod.business.service.impl;

import java.util.Hashtable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.bod.application.beans.DichiaranteBean;
import it.bod.application.common.MasterClass;
import it.bod.business.service.DichiaranteService;
import it.bod.persistence.dao.DichiaranteDao;

@Service
@Transactional
public class DichiaranteServiceImpl extends MasterClass implements DichiaranteService {

	private static final long serialVersionUID = 406235932674268247L;
	
	@Autowired
	private DichiaranteDao dao = null;

	public List<DichiaranteBean> getList() {
		List<DichiaranteBean> lst = dao.getList();
		return lst;
	}
	
	public List<Object> getList(Hashtable htQry) {
		List<Object> lst = dao.getList(htQry);
		return lst;
	}

	public List<DichiaranteBean> getListDicCognomi(String cog) {
		List<DichiaranteBean> lst = dao.getListDicCognomi(cog);
		return lst;
	}
	
	public DichiaranteDao getDao() {
		return dao;
	}

	public void setDao(DichiaranteDao dao) {
		this.dao = dao;
	}

	public List<DichiaranteBean> getListProCognomi(String cog) {
		List<DichiaranteBean> lst = dao.getListProCognomi(cog);
		return lst;
	}
	
}
