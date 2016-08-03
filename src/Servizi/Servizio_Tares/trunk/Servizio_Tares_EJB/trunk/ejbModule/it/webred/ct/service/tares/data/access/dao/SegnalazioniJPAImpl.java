package it.webred.ct.service.tares.data.access.dao;

import it.webred.ct.service.tares.data.access.TaresServiceException;
import it.webred.ct.service.tares.data.access.dto.SegnalazioniSearchCriteria;
import it.webred.ct.service.tares.data.model.SetarSegnala1;
import it.webred.ct.service.tares.data.model.SetarSegnala2;
import it.webred.ct.service.tares.data.model.SetarSegnala3;
import it.webred.ct.service.tares.data.model.SetarSegnalazione;

import java.util.List;

import javax.persistence.Query;

public class SegnalazioniJPAImpl  extends TaresBaseDAO implements SegnalazioniDAO {

	private static final long serialVersionUID = -5391595905655468449L;

	public SegnalazioniJPAImpl() {
		
	}//-------------------------------------------------------------------------

	public List<SetarSegnala1> getSegnalazioni1(SegnalazioniSearchCriteria criteria){
		
		try {
			Query q = manager.createQuery(new SegnalazioniQueryBuilder(criteria).createQuerySegnalazioni1());
			
			if (criteria != null && criteria.getLimit() != null && criteria.getLimit()>0){
				//punto di partenza
				q.setFirstResult(criteria.getOffset());
				//numero di righe da recuperare
				q.setMaxResults(criteria.getLimit());
			}//-----------------------------------------------------------------

			List<SetarSegnala1> lst = q.getResultList();

			return lst;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public int delSegnala1ById(SetarSegnala1 setarSegnala1){
		int ris = 0; 
		try {
			Query q = manager.createQuery("DELETE FROM SetarSegnala1 t WHERE t.id = '" + setarSegnala1.getId() + "' ");
			
			ris = q.executeUpdate();

			return ris;
		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
	}//-------------------------------------------------------------------------
	
	public int delSegnalazioni1(SegnalazioniSearchCriteria criteria){
		int ris = 0;
		try {
			Query q = manager.createQuery(new SegnalazioniQueryBuilder(criteria).createDelQuerySegnalazioni1());
			
			if (criteria != null && criteria.getLimit() != null && criteria.getLimit()>0){
				//punto di partenza
				q.setFirstResult(criteria.getOffset());
				//numero di righe da recuperare
				q.setMaxResults(criteria.getLimit());
			}//-----------------------------------------------------------------

			ris = q.executeUpdate();

			return ris;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public SetarSegnala1 getSegnala1(SetarSegnala1 setarSegnala1){
		SetarSegnala1 ss = null;
		try {
			ss = manager.find(SetarSegnala1.class, setarSegnala1.getId());
			
			return ss;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public SetarSegnala1 updSegnala1(SetarSegnala1 setarSegnala1){
		SetarSegnala1 ss = null;
		try {
			ss = manager.merge(setarSegnala1);
			
			return ss;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public SetarSegnala1 addSegnala1(SetarSegnala1 setarSegnala1){

		try {
			manager.persist(setarSegnala1);
						
			return setarSegnala1;
		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------

	public List<SetarSegnala2> getSegnalazioni2(SegnalazioniSearchCriteria criteria){
		
		try {
			Query q = manager.createQuery(new SegnalazioniQueryBuilder(criteria).createQuerySegnalazioni2());
			
			if (criteria != null && criteria.getLimit() != null && criteria.getLimit()>0){
				//punto di partenza
				q.setFirstResult(criteria.getOffset());
				//numero di righe da recuperare
				q.setMaxResults(criteria.getLimit());
			}//-----------------------------------------------------------------

			List<SetarSegnala2> lst = q.getResultList();

			return lst;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public SetarSegnala2 getSegnala2(SetarSegnala2 setarSegnala2){
		SetarSegnala2 ss = null;
		try {
			ss = manager.find(SetarSegnala2.class, setarSegnala2.getId());
			
			return ss;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public int delSegnala2ById(SetarSegnala2 setarSegnala2){
		int ris = 0; 
		try {
			Query q = manager.createQuery("DELETE FROM SetarSegnala2 t WHERE t.id = '" + setarSegnala2.getId() + "' ");
			
			ris = q.executeUpdate();

			return ris;
		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
	}//-------------------------------------------------------------------------
	
	public int delSegnalazioni2(SegnalazioniSearchCriteria criteria){
		int ris = 0;
		try {
			Query q = manager.createQuery(new SegnalazioniQueryBuilder(criteria).createDelQuerySegnalazioni2());
			
			if (criteria != null && criteria.getLimit() != null && criteria.getLimit()>0){
				//punto di partenza
				q.setFirstResult(criteria.getOffset());
				//numero di righe da recuperare
				q.setMaxResults(criteria.getLimit());
			}//-----------------------------------------------------------------

			ris = q.executeUpdate();

			return ris;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public SetarSegnala2 updSegnala2(SetarSegnala2 setarSegnala2){
		SetarSegnala2 ss = null;
		try {
			ss = manager.merge(setarSegnala2);
			
			return ss;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public SetarSegnala2 addSegnala2(SetarSegnala2 setarSegnala2){

		try {
			manager.persist(setarSegnala2);
			
			return setarSegnala2;
			
		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------

	public List<SetarSegnala3> getSegnalazioni3(SegnalazioniSearchCriteria criteria){
		
		try {
			Query q = manager.createQuery(new SegnalazioniQueryBuilder(criteria).createQuerySegnalazioni3());
			
			if (criteria != null && criteria.getLimit() != null && criteria.getLimit()>0){
				//punto di partenza
				q.setFirstResult(criteria.getOffset());
				//numero di righe da recuperare
				q.setMaxResults(criteria.getLimit());
			}//-----------------------------------------------------------------

			List<SetarSegnala3> lst = q.getResultList();

			return lst;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public SetarSegnala3 getSegnala3(SetarSegnala3 setarSegnala3){
		SetarSegnala3 ss = null;
		try {
			ss = manager.find(SetarSegnala3.class, setarSegnala3.getId());
			
			return ss;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public int delSegnala3ById(SetarSegnala3 setarSegnala3){
		int ris = 0; 
		try {
			Query q = manager.createQuery("DELETE FROM SetarSegnala3 t WHERE t.id = '" + setarSegnala3.getId() + "' ");
			
			ris = q.executeUpdate();

			return ris;
		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
	}//-------------------------------------------------------------------------
	
	public int delSegnalazioni3(SegnalazioniSearchCriteria criteria){
		int ris = 0;
		try {
			Query q = manager.createQuery(new SegnalazioniQueryBuilder(criteria).createDelQuerySegnalazioni3());
			
			if (criteria != null && criteria.getLimit() != null && criteria.getLimit()>0){
				//punto di partenza
				q.setFirstResult(criteria.getOffset());
				//numero di righe da recuperare
				q.setMaxResults(criteria.getLimit());
			}//-----------------------------------------------------------------

			ris = q.executeUpdate();

			return ris;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public SetarSegnala3 updSegnala3(SetarSegnala3 setarSegnala3){
		SetarSegnala3 ss = null;
		try {
			ss = manager.merge(setarSegnala3);
			
			return ss;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public SetarSegnala3 addSegnala3(SetarSegnala3 setarSegnala3){

		try {
			manager.persist(setarSegnala3);
			
			return setarSegnala3;
		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public List<Object> getUiu(SegnalazioniSearchCriteria criteria){
		
		try {
			Query q = manager.createNativeQuery(new SegnalazioniQueryBuilder(criteria).nativeQueryOnSitiuiu());
			
			if (criteria != null && criteria.getLimit() != null && criteria.getLimit()>0){
				//punto di partenza
				q.setFirstResult(criteria.getOffset());
				//numero di righe da recuperare
				q.setMaxResults(criteria.getLimit());
			}//-----------------------------------------------------------------

			List<Object> lst = q.getResultList();

			return lst;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public List<Object> getVani(SegnalazioniSearchCriteria criteria){
		
		try {
			Query q = manager.createNativeQuery(new SegnalazioniQueryBuilder(criteria).nativeQueryOnSitiedi_vani());
			
			if (criteria != null && criteria.getLimit() != null && criteria.getLimit()>0){
				//punto di partenza
				q.setFirstResult(criteria.getOffset());
				//numero di righe da recuperare
				q.setMaxResults(criteria.getLimit());
			}//-----------------------------------------------------------------

			List<Object> lst = q.getResultList();

			return lst;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public List<Object> getAmbienti(SegnalazioniSearchCriteria criteria){
		
		try {
			Query q = manager.createNativeQuery(new SegnalazioniQueryBuilder(criteria).nativeQueryAmbienti());
			
			if (criteria != null && criteria.getLimit() != null && criteria.getLimit()>0){
				//punto di partenza
				q.setFirstResult(criteria.getOffset());
				//numero di righe da recuperare
				q.setMaxResults(criteria.getLimit());
			}//-----------------------------------------------------------------

			List<Object> lst = q.getResultList();

			return lst;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public Long getSegnalazioniMaxProgressivo(SegnalazioniSearchCriteria criteria){
		Object obj = null;
		try {
			Query q = manager.createQuery("SELECT max(t.progressivo) FROM SetarSegnalazione t ");
			
			obj = q.getSingleResult();

			return (Long)obj;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public Long getSegnala1MaxProgressivo(SegnalazioniSearchCriteria criteria){
		Object obj = null;
		Long lng = null;
		try {
			Query q = manager.createQuery("SELECT max(t.progressivo) FROM SetarSegnala1 t ");
			
			obj = q.getSingleResult();
			if (obj instanceof String){
				lng = new Long((String)obj);
			}else{
				lng = (Long)obj; 
			}

			return lng;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public SetarSegnalazione getSegnalazione(SetarSegnalazione setarSegnalazione){
		SetarSegnalazione ss = null;
		try {
			ss = manager.find(SetarSegnalazione.class, setarSegnalazione.getId());
			
			return ss;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public SetarSegnalazione updSegnalazione(SetarSegnalazione setarSegnalazione){
		SetarSegnalazione ss = null;
		try {
			ss = manager.merge(setarSegnalazione);
			
			return ss;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public SetarSegnalazione addSegnalazione(SetarSegnalazione setarSegnalazione){

		try {
			manager.persist(setarSegnalazione);
						
			return setarSegnalazione;
		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public int delSegnalazioneById(SetarSegnalazione setarSegnalazione){
		int ris = 0; 
		try {
			Query q = manager.createQuery("DELETE FROM SetarSegnalazione t WHERE t.id = '" + setarSegnalazione.getId() + "' ");
			
			ris = q.executeUpdate();

			return ris;
		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public List<SetarSegnalazione> getSegnalazioni(SegnalazioniSearchCriteria criteria){
		
		try {
			Query q = manager.createQuery(new SegnalazioniQueryBuilder(criteria).createQuerySegnalazioni());
			
			if (criteria != null && criteria.getLimit() != null && criteria.getLimit()>0){
				//punto di partenza
				q.setFirstResult(criteria.getOffset());
				//numero di righe da recuperare
				q.setMaxResults(criteria.getLimit());
			}//-----------------------------------------------------------------

			List<SetarSegnalazione> lst = q.getResultList();

			return lst;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------

}
