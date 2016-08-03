package it.bod.persistence.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.stereotype.Service;


import it.bod.application.beans.CausaleBean;
import it.bod.persistence.dao.CausaleDao;

@Service
public class CausaleDaoImpl extends MasterDaoImpl implements CausaleDao{
	
	private static final long serialVersionUID = 5439604042303249592L;
	private static Logger logger = Logger.getLogger("it.bod.persistence.dao.impl.CausaleDaoImpl");
	
	public CausaleDaoImpl() {
		// TODO Auto-generated constructor stub
	}//-------------------------------------------------------------------------

	public List<CausaleBean> getList() {
		logger.info("getListaCausali");
		 //List<CausaleBean> lst = hibernateTemplate.findByNamedQuery("getListaCausali");
		 Query q = sessionFactory.getCurrentSession().getNamedQuery("getListaCausali");
		 List lst = q.list();
		 return lst;
	}//-------------------------------------------------------------------------

	public List<CausaleBean> getListDescrizioni() {
		logger.info("getListaCausaliDescrizioni");
		 /*Object[] p = new Object[1];
	     p[0]= "A%";
		 List<CausaleBean> lst = hibernateTemplate.findByNamedQueryAndNamedParam("getListaCausaliDescrizioni", "des" , p);*/
		 Query q = sessionFactory.getCurrentSession().getNamedQuery("getListaCausaliDescrizioni");
		 q.setParameter("des", "A%");
		 List lst = q.list();
		 return lst;
	}//-------------------------------------------------------------------------


}
