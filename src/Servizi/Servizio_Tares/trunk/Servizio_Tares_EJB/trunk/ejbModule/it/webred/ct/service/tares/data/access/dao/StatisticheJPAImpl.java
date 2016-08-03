package it.webred.ct.service.tares.data.access.dao;


import it.webred.ct.service.tares.data.access.TaresServiceException;

import it.webred.ct.service.tares.data.access.dto.DiagnosticheSearchCriteria;
import it.webred.ct.service.tares.data.access.dto.StatisticheSearchCriteria;
import it.webred.ct.service.tares.data.model.SetarDia;
import it.webred.ct.service.tares.data.model.SetarStat;

import java.util.List;

import javax.persistence.Query;

public class StatisticheJPAImpl extends TaresBaseDAO implements StatisticheDAO {

	private static final long serialVersionUID = 1L;
	
	public List<SetarStat> getStatistiche(StatisticheSearchCriteria criteria){
		
		try {
			Query q = manager.createQuery(new StatisticheQueryBuilder(criteria).createQuery());
			
			if (criteria != null && criteria.getLimit() != null && criteria.getLimit()>0){
				//punto di partenza
				q.setFirstResult(criteria.getOffset());
				//numero di righe da recuperare
				q.setMaxResults(criteria.getLimit());
			}//-----------------------------------------------------------------

			List<SetarStat> lst = q.getResultList();

			return lst;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public Number getStatisticheCount(StatisticheSearchCriteria criteria){
		
		try {
			Query q = manager.createQuery(new StatisticheQueryBuilder(criteria).createQueryCount());

			Number cnt = (Number)q.getSingleResult();

			return cnt;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public List<SetarDia> getDiagnostiche(DiagnosticheSearchCriteria criteria){
		
		try {
			Query q = manager.createQuery(new DiagnosticheQueryBuilder(criteria).createQuery());
			
			if (criteria != null && criteria.getLimit() != null && criteria.getLimit()>0){
				//punto di partenza
				q.setFirstResult(criteria.getOffset());
				//numero di righe da recuperare
				q.setMaxResults(criteria.getLimit());
			}//-----------------------------------------------------------------

			List<SetarDia> lst = q.getResultList();

			return lst;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	

}
