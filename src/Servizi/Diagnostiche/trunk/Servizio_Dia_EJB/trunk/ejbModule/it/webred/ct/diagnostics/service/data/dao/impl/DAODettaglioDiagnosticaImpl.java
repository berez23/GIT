package it.webred.ct.diagnostics.service.data.dao.impl;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.persistence.Query;


import it.webred.ct.diagnostics.service.data.SuperDia;
import it.webred.ct.diagnostics.service.data.dao.DIABaseDAO;
import it.webred.ct.diagnostics.service.data.dao.IDAODettaglioDiagnostica;
import it.webred.ct.diagnostics.service.data.exception.DIAServiceException;
import it.webred.ct.diagnostics.service.data.model.DiaTestata;
import it.webred.ct.diagnostics.service.data.util.DiaUtility;

//@Repository
//@Service 
public class DAODettaglioDiagnosticaImpl extends DIABaseDAO implements IDAODettaglioDiagnostica {

	public List<? extends SuperDia> getDiagnosticaListaDettagli(int start,int maxrows,Long idDiaTestata, String modelClassname) throws DIAServiceException {
		
		List<? extends SuperDia> ll = null;
		
		try {
			String entity = DiaUtility.getNameFromClass(modelClassname);
			logger.debug("Elenco record da tabella "+entity);
			
			//recupero l'idCatalogoDia
			String hqlKey = "select dt from DiaTestata dt where dt.id = :idDiaTestata";
			Query qKey = manager.createQuery(hqlKey);
			qKey.setParameter("idDiaTestata", idDiaTestata);
			List llKey = qKey.getResultList();
			String key = null;
			String orderBy = "";
			for (Object obj : llKey) {
				try {
					key = "dia." + ((DiaTestata)obj).getIdCatalogoDia();
				} catch (Throwable t) {}
			}
			if (key != null) {
				Properties p = new Properties();
				p.load(this.getClass().getResourceAsStream("/diaOrderBy.properties"));
				if (p.getProperty(key) != null) {
					orderBy = p.getProperty(key);
					if (orderBy.toLowerCase().startsWith("order by ")) {
						orderBy = orderBy.substring("order by ".length()).trim();
					}
					orderBy = "order by " + orderBy;
				}
			}
			try {
				//creazione hql command
				StringBuilder hql = new StringBuilder("select dia from ");
				hql.append(entity);
				hql.append(" dia where dia.diaTestata.id = :idDiaTestata ");
				hql.append(orderBy);
				logger.debug("HQL query: "+hql.toString());
				
				Query q = manager.createQuery(hql.toString());
				q.setFirstResult(start);
				q.setMaxResults(maxrows);
				q.setParameter("idDiaTestata", idDiaTestata);
				
				ll = q.getResultList();
			} catch (Throwable t) {
				logger.debug("Errore nel recupero dati diagnostica con order by: " + t.getMessage() 
								+ "; i dati saranno recuperati senza ordinamento");
				//riprovo senza order by				
				StringBuilder hql = new StringBuilder("select dia from ");
				hql.append(entity);
				hql.append(" dia where dia.diaTestata.id = :idDiaTestata ");
				logger.debug("HQL query: "+hql.toString());
				
				Query q = manager.createQuery(hql.toString());
				q.setFirstResult(start);
				q.setMaxResults(maxrows);
				q.setParameter("idDiaTestata", idDiaTestata);
				
				ll = q.getResultList();
			}

		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage());
			throw new DIAServiceException(t);
		}
		
		
		return ll;
	}

	public List<String[]> getDiagnosticaArrayDettagli(int start,int maxrows,Long idDiaTestata,String modelClassname) throws DIAServiceException {

		 List<String[]> dett = new ArrayList<String[]>();
		
		try {
			List<? extends SuperDia> ll = getDiagnosticaListaDettagli(start,maxrows,idDiaTestata,modelClassname);
			
			Class c = Class.forName(modelClassname);
			Method[] mm = c.getDeclaredMethods();
			List<String> metodi = DiaUtility.getOrderedModelClassGETMethods(DiaUtility.getModelClassGETMethods(c), c);
						
			//ciclo sulla lista degli oggetti dati
			for(int i=0; i<ll.size(); i++) {
				Object o = ll.get(i);
				//per ogni oggetto richiamo i metodi get
				int fieldCount = 0;
				String[] dd = new String[metodi.size()];
				for(String metodo: metodi) {
					Method m = c.getMethod(metodo);
					Object obj = m.invoke(c.cast(o));
					if(obj != null) {
						//logger.debug("[Oggetto] valore: "+obj.toString());
						if(obj instanceof DiaTestata) {
							dd[fieldCount] = "";
						}
						else dd[fieldCount] = obj.toString();
					}
					else
						dd[fieldCount] = "";
					
					fieldCount++;
				}
				
				dett.add(dd);
			}
			
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage());
			throw new DIAServiceException(t);
		}
		
		return dett;
	}

	public Long getCount(Long idDiaTestata, String modelClassname) throws DIAServiceException {
		
		Long count = new Long(0);
		
		try {
			String entity = DiaUtility.getNameFromClass(modelClassname);
			logger.debug("Record count da tabella "+entity);
			
			//creazione hql command
			StringBuilder hql = new StringBuilder("select count(dia) from ");
			hql.append(entity);
			hql.append(" dia where dia.diaTestata.id = :idDiaTestata ");
			logger.debug("HQL query: "+hql.toString());
			
			//Query q = manager.createNativeQuery(hql.toString());
			Query q = manager.createQuery(hql.toString());
			q.setParameter("idDiaTestata", idDiaTestata);
			
			count = (Long)q.getSingleResult();

		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage());
			throw new DIAServiceException(t);
		}
		
		return count;
	}

	
	public List<? extends SuperDia> getDiagnosticaListaDettagli(Long idDiaTestata, String modelClassname) throws DIAServiceException {

		List<? extends SuperDia> ll = null;
		
		try {
			String entity = DiaUtility.getNameFromClass(modelClassname);
			logger.debug("Elenco record da tabella "+entity);
			
			//creazione hql command
			StringBuilder hql = new StringBuilder("select dia from ");
			hql.append(entity);
			hql.append(" dia where dia.diaTestata.id = :idDiaTestata ");
			logger.debug("HQL query: "+hql.toString());
			
			Query q = manager.createQuery(hql.toString());
			q.setParameter("idDiaTestata", idDiaTestata);
			
			ll = q.getResultList();
		}catch (Throwable t) {
			logger.error("Eccezione: "+t.getMessage());
			throw new DIAServiceException(t);
		}
		
		return ll;
	}

	

}

