package it.bod.persistence.dao.impl;

import it.bod.application.common.FilterItem;

import it.bod.application.common.MasterClass;
import it.bod.application.common.MasterItem;
import it.bod.persistence.dao.MasterDao;
import it.persistance.common.CustomHibernateCallBack;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MasterDaoImpl implements MasterDao{

	private static final long serialVersionUID = 2406399310037632878L;
	private static Logger logger = Logger.getLogger(MasterDaoImpl.class.getName());
	
	@Autowired
	protected SessionFactory sessionFactory;
	
	@Autowired
	private SessionFactory caronteSessionFactory;
	
	public Long addItem(MasterItem item, Class cls){
		Long id = (Long)sessionFactory.getCurrentSession().save(item);
		return id;
	}
	public Long addAndFlushItem(MasterItem item, Class cls){
		Long id = (Long)sessionFactory.getCurrentSession().save(item);
		sessionFactory.getCurrentSession().flush();
		return id;
	}
	public Long addItemCaronte(MasterItem item, Class cls){
		Long id = (Long)caronteSessionFactory.getCurrentSession().save(item);
		return id;
	}

	//-------------------------------------------------------------------------

	public List getList(Hashtable htQry, Class cls) {
		return this.getList(htQry, cls, sessionFactory);
	}
	public List getListCaronte(Hashtable htQry, Class cls) {
		return this.getList(htQry, cls, caronteSessionFactory);
	}
	
	private List getList(Hashtable htQry, Class cls, SessionFactory mySessionFactory){

		String strQry = " from " + cls.getSimpleName() + " " + cls.getSimpleName().toUpperCase() + " ";
		if (htQry != null && htQry.size()>0){
			if (htQry.containsKey("where")){
				String strPar = "where ";
				ArrayList<FilterItem> par = (ArrayList<FilterItem>)htQry.get("where");
				FilterItem fi = null;
				for (int i=0; i<par.size(); i++){
					fi = par.get(i);
					if (fi.getOperatore().equals("LIKE")){
						strPar = strPar + " " + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + " " + fi.getOperatore() + " :" + fi.getParametro() + " and";
					}else if (fi.getOperatore().equals("_DAL_")){
						strPar = strPar + " " + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + " >= :" + fi.getParametro() + " and";					
					}else if (fi.getOperatore().equals("_AL_")){ 
						strPar = strPar + " " + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + " <= :" + fi.getParametro() + " and";
					}else if (fi.getOperatore().equals("_IL_")){ 
						strPar = strPar + " " + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + " = :" + fi.getParametro() + " and";
					}
					else{
						strPar = strPar + " " + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + " " + fi.getOperatore() + " :" + fi.getParametro() + " and";						
					}
				}
				strQry = strQry + " " + strPar.substring(0, strPar.length()-4);
			}
			if (htQry.containsKey("orderBy"))
				strQry += " order by " + cls.getSimpleName().toUpperCase() + "." + (String)htQry.get("orderBy");
		}
		
		Query q = mySessionFactory.getCurrentSession().createQuery(strQry);
		
		List lst = null;
		if (htQry != null && htQry.size()>0){
			if (htQry.containsKey("where")){
				ArrayList<FilterItem> par = (ArrayList)htQry.get("where");
				FilterItem fi = null;
				String[] paramNames = new String[par.size()];
				Object[] paramValues = new Object[par.size()];
				for (int i=0; i<par.size(); i++){
					fi = par.get(i);
					q.setParameter(fi.getParametro(), fi.getValore());
					//paramNames[i] = fi.getParametro();
					//paramValues[i] = fi.getValore();
					logger.info("Parametro "+fi.getParametro()+"["+fi.getValore()+"]");
				}
				/*
				 * lst = hibernateTemplate.find(strQry, paramValues);
				 * Questa tipologia di query vuole come segna posto dei parametri
				 * il carattere ? 
				 */
				//lst = mySessionFactory.getCurrentSession().findByNamedParam(strQry, paramNames, paramValues);
			}				
			lst = q.list();

		}
		logger.info("getList(Hashtable htQry, Class cls): " + strQry);
		return lst;
	}//-------------------------------------------------------------------------
	
	public MasterItem getItemById(Long id, Class cls){
		logger.info("getItemById(Long id, Class cls)");
		MasterItem mi = (MasterItem)sessionFactory.getCurrentSession().get(cls.getName(), new Long(id));
		return mi;
	}//-------------------------------------------------------------------------
	
	public void delItem(MasterItem sheet, Class cls){
		logger.info("delItem(MasterItem sheet, Class cls)");
		sessionFactory.getCurrentSession().delete(sheet);
	}//-------------------------------------------------------------------------
	
	public MasterItem updItem(MasterItem mi, Class cls){
		logger.info("updItem(MasterItem mi, Class cls)");
		MasterItem item = (MasterItem)sessionFactory.getCurrentSession().merge(cls.getName(), mi);
		return item;
	}//-------------------------------------------------------------------------
	
	public List getList(String strQry){
		logger.info("getList(String strQry): " + strQry);
		//List lst = sessionFactory.getCurrentSession().find(strQry);
		Query q = sessionFactory.getCurrentSession().createSQLQuery(strQry);
		List lst = q.list();
		return lst;
	}
	public List getListCaronte(String strQry){
		logger.info("getList(String strQry): " + strQry);
		//List lst = caronteSessionFactory.getCurrentSession().find(strQry);
		Query q =sessionFactory.getCurrentSession().createSQLQuery(strQry);
		List lst = q.list();
		return lst;
	}
	
	//-------------------------------------------------------------------------
	
	public List getList(Hashtable htQry){
		return getList(htQry,sessionFactory );
	}
	public List getListCaronte(Hashtable htQry){
		return getList(htQry,caronteSessionFactory );
	}
	private List getList(Hashtable htQry, SessionFactory mySessionFactory){
		List lst = null;
		if (htQry != null && htQry.size()>0){
			if (htQry.containsKey("queryName")){
				String queryName = (String)htQry.get("queryName");
				Query q = mySessionFactory.getCurrentSession().getNamedQuery(queryName);
				if (htQry.containsKey("where")){
					ArrayList<FilterItem> par = (ArrayList<FilterItem>)htQry.get("where");
					FilterItem fi = null;
					String[] paramNames = new String[par.size()];
					Object[] paramValues = new String[par.size()];
					for (int i=0; i<par.size(); i++){
						fi = par.get(i);
						q.setParameter(fi.getParametro(), fi.getValore());
						//paramNames[i] = fi.getParametro();
						//paramValues[i] = fi.getValore();
						logger.info("Parametro "+fi.getParametro()+"["+fi.getValore()+"]");
					}
					//lst = mySessionFactory.getCurrentSession().findByNamedQueryAndNamedParam(queryName, paramNames, paramValues);
				}
				lst = q.list();
			
				logger.info("getList(Hashtable htQry) queryName: " + queryName);
			}else if (htQry.containsKey("queryString")){
				String queryString = (String)htQry.get("queryString");
				//lst = hibernateTemplate.find(queryString);
				logger.info("getList(Hashtable htQry) queryString: " + queryString);
				//HibernateCallback hc = new CustomHibernateCallBack(queryString);
				//hiberanteTemplate.executeFind(hc);
				Query q = mySessionFactory.getCurrentSession().createSQLQuery(queryString);
				lst = q.list();
			}
		}

		return lst;
	}
	
}//-----------------------------------------------------------------------------
