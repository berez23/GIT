package it.webred.ct.service.tares.data.access.dao;


import it.webred.ct.service.tares.data.access.TaresServiceException;

import it.webred.ct.service.tares.data.access.dto.ValutazioniSearchCriteria;
import it.webred.ct.service.tares.data.model.SetarElab;
import it.webred.ct.service.tares.data.model.SetarSegnalazione;

import java.util.List;

import javax.persistence.Query;

public class ValutazioniJPAImpl extends TaresBaseDAO implements ValutazioniDAO {

	private static final long serialVersionUID = 1L;
	
	public List<SetarElab> getElaborazioni(ValutazioniSearchCriteria criteria){
		
		try {
			Query q = manager.createQuery(new ValutazioniQueryBuilder(criteria).createQuery());
			
			if (criteria != null && criteria.getLimit() != null && criteria.getLimit()>0){
				//punto di partenza
				q.setFirstResult(criteria.getOffset());
				//numero di righe da recuperare
				q.setMaxResults(criteria.getLimit());
			}//-----------------------------------------------------------------

			List<SetarElab> lst = q.getResultList();

			return lst;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public Number getElaborazioniCount(ValutazioniSearchCriteria criteria){
		
		try {
			Query q = manager.createQuery(new ValutazioniQueryBuilder(criteria).createQueryCount());

			Number cnt = (Number)q.getSingleResult();

			return cnt;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	
	public SetarElab getElaborazione(SetarElab elaborazione){
		SetarElab se = null;
		try {
			se = manager.find(SetarElab.class, elaborazione.getId());
			
			return se;

		} catch (Throwable t) {
			throw new TaresServiceException(t);
		}
		
	}//-------------------------------------------------------------------------
	

}
