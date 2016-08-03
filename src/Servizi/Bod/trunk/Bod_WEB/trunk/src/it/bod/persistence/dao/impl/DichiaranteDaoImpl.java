package it.bod.persistence.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import it.bod.application.beans.DichiaranteBean;
import it.bod.persistence.dao.DichiaranteDao;

@Service
public class DichiaranteDaoImpl extends MasterDaoImpl implements DichiaranteDao{
	
	private static final long serialVersionUID = -5546567519317398045L;
	private static Logger logger = Logger.getLogger("it.bod.persistence.dao.impl.DichiaranteDaoImpl");
	
	public List<DichiaranteBean> getList() {
		logger.info("getListaDichiaranti");
		//List<DichiaranteBean> lst = hibernateTemplate.findByNamedQuery("getListaDichiaranti");
		Query q = sessionFactory.getCurrentSession().getNamedQuery("getListaDichiaranti");
		List lst = q.list();
		return lst;
	}
	
	public List<DichiaranteBean> getListDicCognomi(String cog) {
		logger.info("getListaDichiarantiCognomi");
		/*Object[] p = new Object[1];
	     p[0]= cog;
		List<DichiaranteBean> lst = hibernateTemplate.findByNamedQueryAndNamedParam("getListaDichiarantiCognomi", "cog", p);*/
		Query q = sessionFactory.getCurrentSession().getNamedQuery("getListaDichiarantiCognomi");
		q.setParameter("cog", cog);
		List lst = q.list();
		return lst;
	}

	public List<DichiaranteBean> getListProCognomi(String cog) {
		/*Object[] p = new Object[1];
	     p[0]= cog;
		List<DichiaranteBean> lst = hibernateTemplate.findByNamedQueryAndNamedParam("getListaProfessionistiCognomi", "cog", p);*/
		Query q = sessionFactory.getCurrentSession().getNamedQuery("getListaProfessionistiCognomi");
		q.setParameter("cog", cog);
		List lst = q.list();
		return lst;
	}

	
}
