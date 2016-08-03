package it.bod.persistence.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.stereotype.Service;

import it.bod.application.beans.UiuBean;
import it.bod.persistence.dao.UiuDao;
import it.bod.persistence.dao.MasterDao;

@Service
public class UiuDaoImpl  extends MasterDaoImpl implements UiuDao {

	private static final long serialVersionUID = 6873918619743849120L;
	private static Logger logger = Logger.getLogger("it.bod.persistence.dao.impl.UiuDaoImpl");
	
	public List<UiuBean> getListaIndirizzi() {
		logger.info("getListaIndirizzi");
		//List<UiuBean> lst = hibernateTemplate.findByNamedQuery("getListaIndirizzi");
		Query q = sessionFactory.getCurrentSession().getNamedQuery("getListaIndirizzi");
		List lst = q.list();
		return lst;
	}

	public List<UiuBean> getListaIndirizzi(String des) {
		logger.info("getListaIndirizzi");
		/*Object[] p = new Object[1];
	     p[0]= des;
		List<UiuBean> lst = hibernateTemplate.findByNamedQueryAndNamedParam("getListaIndirizzi", "des", p);*/
		Query q = sessionFactory.getCurrentSession().getNamedQuery("getListaIndirizzi");
		q.setParameter("des", des);
		List lst = q.list();
		return lst;
	}
	

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

}
