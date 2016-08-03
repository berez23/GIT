package it.bod.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.bod.application.beans.DestinazioneFunzionaleBean;
import it.bod.application.common.MasterClass;
import it.bod.business.service.DestinazioneFunzionaleService;
import it.bod.persistence.dao.DestinazioneFunzionaleDao;

@Service
@Transactional
public class DestinazioneFunzionaleServiceImpl extends MasterClass implements DestinazioneFunzionaleService {

	private static final long serialVersionUID = 3995075678110840921L;
	
	@Autowired
	private DestinazioneFunzionaleDao dao = null;

	public List<DestinazioneFunzionaleBean> getListaDestinazioniFunzionali() {
		List<DestinazioneFunzionaleBean> lst = dao.getListaDestinazioniFunzionali();
		return lst;
	}

	public DestinazioneFunzionaleDao getDao() {
		return dao;
	}

	public void setDao(DestinazioneFunzionaleDao dao) {
		this.dao = dao;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

}
