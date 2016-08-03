package it.webred.gitland.dao;

import it.webred.gitland.data.model.AuditEntitaEliminate;
import it.webred.gitland.data.model.MasterItem;
import it.webred.gitland.ejb.dto.FiltroDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.security.SecurityContextAssociation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Named
@Transactional
public class GitLandDAO implements Serializable {
	
	private static final long serialVersionUID = -1894402297549577081L;

	public static Logger logger = Logger.getLogger("gitland.log");
	
	@PersistenceContext(unitName="GitLand_DataModel")
	protected EntityManager em;
		
	public GitLandDAO() {
	}//-------------------------------------------------------------------------
	
	public MasterItem getItemById(Long id, Class cls) {
		MasterItem mi = em.find(cls, id);
		return mi;
	}//-------------------------------------------------------------------------
	
	public void addItem(MasterItem mi) {
		em.persist(mi);
	}//-------------------------------------------------------------------------
	
	public MasterItem updItem(MasterItem mi) {
		MasterItem iMaster = em.merge(mi);
		return iMaster;
	}//-------------------------------------------------------------------------
	
	public List getList(String strQry, Hashtable<String, Object> parametri, Boolean hql){
		Query qry = null;
		if (hql){
			qry = em.createQuery(strQry);
		}else{
			qry = em.createNativeQuery(strQry);	
		}
		if(parametri!=null){
			for (String  parName : parametri.keySet()) {
				qry.setParameter(parName, parametri.get(parName));
			}
		}
		List lst = qry.getResultList();
		return lst;
	}//-------------------------------------------------------------------------
	
	
	public List<Object[]> getList(String strQry, Boolean hql){
		Query qry = null;
		if (hql){
			qry = em.createQuery(strQry);
		}else{
			qry = em.createNativeQuery(strQry);	
		}
		List<Object[]> lst = qry.getResultList();
		return lst;
	}//-------------------------------------------------------------------------
	
	public List getList(Hashtable htQry, Class cls){
		String strQry = " from " + cls.getSimpleName() + " " + cls.getSimpleName().toUpperCase() + " ";
		if (htQry != null && htQry.size()>0){
			if (htQry.containsKey("where")){
				String strPar = "where ";
				ArrayList<FiltroDTO> par = (ArrayList)htQry.get("where");
				FiltroDTO fi = null;
				for (int i=0; i<par.size(); i++){
					fi = par.get(i);
					if (fi.getOperatore().equals("LIKE")){
						strPar = strPar + " upper(" + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + ") " + fi.getOperatore() + " upper(:" + fi.getParametro() + ") and";
					}else if (fi.getOperatore().equals("_DAL_")){
						strPar = strPar + " " + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + " >= :" + fi.getParametro() + " and";					
					}else if (fi.getOperatore().equals("_AL_")){ 
						strPar = strPar + " " + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + " <= :" + fi.getParametro() + " and";
					}else if (fi.getOperatore().equals("_IL_")){ 
						strPar = strPar + " " + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + " = :" + fi.getParametro() + " and";
					}else if (fi.getOperatore().equals("_IS_")){ 
						strPar = strPar + " " + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + " is " + fi.getParametro() + " and";
					}else if (fi.getOperatore().equals("_IS_NULL_")){ 
						strPar = strPar + " " + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + " is null and";
					}else if (fi.getOperatore().equals("_IS_NOT_NULL_")){ 
						strPar = strPar + " " + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + " is not null and";
					}else{
						if (fi.getValore() instanceof String)
							strPar = strPar + " upper(" + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + ") " + fi.getOperatore() + " upper(:" + fi.getParametro() + ") and";						
						else
							strPar = strPar + " " + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + " " + fi.getOperatore() + " :" + fi.getParametro() + " and";
					}
				}
				strQry = strQry + " " + strPar.substring(0, strPar.length()-4);
			}
		}
		
		if (htQry.containsKey("orderBy"))
			strQry += " order by " + cls.getSimpleName().toUpperCase() + "." + (String)htQry.get("orderBy");
		
		Query qry = em.createQuery(strQry);
		if (htQry != null && htQry.size()>0){
			if (htQry.containsKey("where")){
				ArrayList<FiltroDTO> alFilters = (ArrayList)htQry.get("where");
				FiltroDTO fi = null;
				for (int i=0; i<alFilters.size(); i++){
					fi = alFilters.get(i);
					if (fi.getValore() != null)
						qry.setParameter(fi.getParametro(), fi.getValore());
				}				
			}
		}
		
		if (htQry.containsKey("limit")){
			Integer limit = (Integer)htQry.get("limit");
			if (limit != null && limit.intValue()>0)
				qry = qry.setFirstResult(0).setMaxResults(limit);
		}
		
		List lst = qry.getResultList();
		
		return lst;
	}//-------------------------------------------------------------------------

	public Boolean delItemById(Long id, Class cls, Boolean audit) {
		Boolean ret=false;
		// Metodo che elimina l'entita partendo dal suo ID e se richiesto ne fa l'audit 
		try {
			MasterItem entity= em.find(cls, id);
			em.remove(entity);
			if(audit){
				AuditEntitaEliminate aee= new AuditEntitaEliminate();
				aee.setData(new Date());
				aee.setUtente(SecurityContextAssociation.getPrincipal().getName());
				aee.setIdEntita(id);
				aee.setEntita(cls.getName());
				em.persist(aee);
			}
			ret=true;
		} catch (Exception e) {
			logger.error(e);
			
		}
		return ret;
	}//-------------------------------------------------------------------------
	
	public Boolean delItemById(Object id, Class cls) {
		Boolean ret=false;
		// Metodo che elimina l'entita partendo dal suo ID e se richiesto ne fa l'audit 
		try {
			MasterItem entity= em.find(cls, id);
			em.remove(entity);
			ret=true;
		} catch (Exception e) {
			logger.error(e);
			
		}
		return ret;
	}//-------------------------------------------------------------------------
	
	public Integer delList(Hashtable htQry, Class cls){
		String strQry = "delete from " + cls.getSimpleName() + " " + cls.getSimpleName().toUpperCase() + " ";
		if (htQry != null && htQry.size()>0){
			if (htQry.containsKey("where")){
				String strPar = "where ";
				ArrayList<FiltroDTO> par = (ArrayList)htQry.get("where");
				FiltroDTO fi = null;
				for (int i=0; i<par.size(); i++){
					fi = par.get(i);
					if (fi.getOperatore().equals("LIKE")){
						strPar = strPar + " upper(" + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + ") " + fi.getOperatore() + " upper(:" + fi.getParametro() + ") and";
					}else if (fi.getOperatore().equals("_DAL_")){
						strPar = strPar + " " + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + " >= :" + fi.getParametro() + " and";					
					}else if (fi.getOperatore().equals("_AL_")){ 
						strPar = strPar + " " + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + " <= :" + fi.getParametro() + " and";
					}else if (fi.getOperatore().equals("_IL_")){ 
						strPar = strPar + " " + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + " = :" + fi.getParametro() + " and";
					}else if (fi.getOperatore().equals("_IS_")){ 
						strPar = strPar + " " + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + " is " + fi.getParametro() + " and";
					}else if (fi.getOperatore().equals("_IS_NULL_")){ 
						strPar = strPar + " " + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + " is null and";
					}else if (fi.getOperatore().equals("_IS_NOT_NULL_")){ 
						strPar = strPar + " " + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + " is not null and";
					}else{
						if (fi.getValore() instanceof String)
							strPar = strPar + " upper(" + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + ") " + fi.getOperatore() + " upper(:" + fi.getParametro() + ") and";						
						else
							strPar = strPar + " " + cls.getSimpleName().toUpperCase() + "." + fi.getAttributo() + " " + fi.getOperatore() + " :" + fi.getParametro() + " and";
					}
				}
				strQry = strQry + " " + strPar.substring(0, strPar.length()-4);
			}
		}
		
		Query qry = em.createQuery(strQry);
		if (htQry != null && htQry.size()>0){
			if (htQry.containsKey("where")){
				ArrayList<FiltroDTO> alFilters = (ArrayList)htQry.get("where");
				FiltroDTO fi = null;
				for (int i=0; i<alFilters.size(); i++){
					fi = alFilters.get(i);
					if (fi.getValore() != null)
						qry.setParameter(fi.getParametro(), fi.getValore());
				}				
			}
		}
		
		Integer ris = qry.executeUpdate();
		
		return ris;
	}//-------------------------------------------------------------------------

}
