package it.webred.ct.service.tares.data.access.dao;


import it.webred.ct.service.tares.data.access.TaresServiceException;


import it.webred.ct.service.tares.data.access.dto.TariffeSearchCriteria;
import it.webred.ct.service.tares.data.model.SetarBilancioAnnoCorr;
import it.webred.ct.service.tares.data.model.SetarCoeff;
import it.webred.ct.service.tares.data.model.SetarConsuntivoAnnoPrec;
import it.webred.ct.service.tares.data.model.SetarTariffa;

import java.util.List;

import javax.persistence.Query;

public class TariffeJPAImpl extends TaresBaseDAO implements TariffeDAO {

	private static final long serialVersionUID = -7171175635120754495L;

	public List<SetarCoeff> getCoeff(TariffeSearchCriteria criteria){
		
		try {
			Query q = manager.createQuery(new TariffeQueryBuilder(criteria).createQuery());
			
			if (criteria != null && criteria.getLimit() != null && criteria.getLimit()>0){
				//punto di partenza
				q.setFirstResult(criteria.getOffset());
				//numero di righe da recuperare
				q.setMaxResults(criteria.getLimit());
			}//-----------------------------------------------------------------

			List<SetarCoeff> lst = q.getResultList();

			return lst;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public SetarCoeff updCoeff(SetarCoeff setarCoeff){
		SetarCoeff sc = null;
		try {
			sc = manager.merge(setarCoeff);
			
			return sc;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public List<SetarTariffa> getTariffe(TariffeSearchCriteria criteria){
		
		try {
			Query q = manager.createQuery(new TariffeQueryBuilder(criteria).createQueryTariffa());
			
			if (criteria != null && criteria.getLimit() != null && criteria.getLimit()>0){
				//punto di partenza
				q.setFirstResult(criteria.getOffset());
				//numero di righe da recuperare
				q.setMaxResults(criteria.getLimit());
			}//-----------------------------------------------------------------

			List<SetarTariffa> lst = q.getResultList();

			return lst;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public SetarTariffa updTariffa(SetarTariffa setarTariffa){
		SetarTariffa st = null;
		try {
			st = manager.merge(setarTariffa);
			
			return st;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public void addTariffa(SetarTariffa setarTariffa){

		try {
			manager.persist(setarTariffa);
			
		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public List<SetarBilancioAnnoCorr> getBilancio(TariffeSearchCriteria criteria){
		try {
			Query q = manager.createQuery(new TariffeQueryBuilder(criteria).createQueryBilancio());
			
			if (criteria != null && criteria.getLimit() != null && criteria.getLimit()>0){
				//punto di partenza
				q.setFirstResult(criteria.getOffset());
				//numero di righe da recuperare
				q.setMaxResults(criteria.getLimit());
			}//-----------------------------------------------------------------

			List<SetarBilancioAnnoCorr> lst = q.getResultList();

			return lst;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
	}//-------------------------------------------------------------------------
	
	public SetarBilancioAnnoCorr updBilancio(SetarBilancioAnnoCorr setarBilancio){
		SetarBilancioAnnoCorr st = null;
		try {
			st = manager.merge(setarBilancio);
			
			return st;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
	}//-------------------------------------------------------------------------
	
	public List<SetarConsuntivoAnnoPrec> getConsuntivo(TariffeSearchCriteria criteria){
		try {
			Query q = manager.createQuery(new TariffeQueryBuilder(criteria).createQueryConsuntivo());
			
			if (criteria != null && criteria.getLimit() != null && criteria.getLimit()>0){
				//punto di partenza
				q.setFirstResult(criteria.getOffset());
				//numero di righe da recuperare
				q.setMaxResults(criteria.getLimit());
			}//-----------------------------------------------------------------

			List<SetarConsuntivoAnnoPrec> lst = q.getResultList();

			return lst;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}		
	}//-------------------------------------------------------------------------
	
	public SetarConsuntivoAnnoPrec updConsuntivo(SetarConsuntivoAnnoPrec setarConsuntivo){
		SetarConsuntivoAnnoPrec st = null;
		try {
			st = manager.merge(setarConsuntivo);
			
			return st;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
	}//-------------------------------------------------------------------------
	
	public List<Object> getClassiTarsu(TariffeSearchCriteria criteria){
		
		try {
			Query q = manager.createNativeQuery(new TariffeQueryBuilder(criteria).nativeQueryClassiTarsu());
			
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
	
	public List<Object> getDistribuzioneSupTotTarsu(TariffeSearchCriteria criteria){
		
		try {
			Query q = manager.createNativeQuery(new TariffeQueryBuilder(criteria).nativeQueryDistribuzioneSupTotTarsu());
			
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
	
	public List<Object> getDistribuzioneComponenti(TariffeSearchCriteria criteria){
		
		try {
			Query q = manager.createNativeQuery(new TariffeQueryBuilder(criteria).nativeQueryDistribuzioneComponenti());
			
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
	
	


}
