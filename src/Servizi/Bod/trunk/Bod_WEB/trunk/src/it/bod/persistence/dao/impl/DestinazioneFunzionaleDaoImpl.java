package it.bod.persistence.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import it.bod.application.beans.DestinazioneFunzionaleBean;
import it.bod.persistence.dao.DestinazioneFunzionaleDao;
import it.bod.persistence.dao.MasterDao;

@Service
public class DestinazioneFunzionaleDaoImpl extends MasterDaoImpl implements DestinazioneFunzionaleDao{

	private static final long serialVersionUID = 6343394957710325653L;
	private static Logger logger = Logger.getLogger("it.bod.persistence.dao.impl.DestinazioneFunzionaleDaoImpl");
	
	public List<DestinazioneFunzionaleBean> getListaDestinazioniFunzionali() {
		logger.info("getListaDestinazioniFunzionali");
		//List<DestinazioneFunzionaleBean> lst = hibernateTemplate.findByNamedQuery("getListaDestinazioniFunzionali");
		Query q = sessionFactory.getCurrentSession().getNamedQuery("getListaDestinazioniFunzionali");
		List lst = q.list();
		return lst;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

}
