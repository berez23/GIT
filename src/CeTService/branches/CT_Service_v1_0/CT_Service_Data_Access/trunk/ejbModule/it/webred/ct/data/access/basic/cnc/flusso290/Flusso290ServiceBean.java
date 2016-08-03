package it.webred.ct.data.access.basic.cnc.flusso290;

import it.webred.ct.data.access.basic.cnc.CNCBaseService;
import it.webred.ct.data.access.basic.cnc.flusso290.Flusso290Service;
import it.webred.ct.data.access.basic.cnc.flusso290.Flusso290ServiceException;
import it.webred.ct.data.access.basic.cnc.flusso290.dto.AnagraficaImpostaDTO;
import it.webred.ct.data.access.basic.cnc.flusso290.dto.Flusso290SearchCriteria;
import it.webred.ct.data.model.cnc.flusso290.RAnagraficaIntestatarioRuolo;
import it.webred.ct.data.model.cnc.flusso290.RDatiContabili;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * Session Bean implementation class Flusso290ServiceBean
 */
@Stateless
public class Flusso290ServiceBean extends CNCBaseService implements Flusso290Service {

	@Override
	public List<AnagraficaImpostaDTO> getAnagraficaImpostaSintesi(
			Flusso290SearchCriteria criteria, int startm, int numberRecord) {
		
		List<AnagraficaImpostaDTO> result = new ArrayList<AnagraficaImpostaDTO>();
		
		try {
			
			String sql = (new QueryBuilder(criteria)).createQuery(false);
			
			if (sql == null)
				return result;
			
			Query q = manager.createQuery(sql);
			q.setFirstResult(startm);
			q.setMaxResults(numberRecord);
			
			List<RAnagraficaIntestatarioRuolo> anagList = q.getResultList();
			
			Query q1 = manager.createNamedQuery("Flusso290.getDatiContabileByPartitaComuneProgressivo");
			
			for (RAnagraficaIntestatarioRuolo anag : anagList) {
				
				q1.setParameter("codComune", anag.getCodComuneIscrRuolo());
				q1.setParameter("codPartita", anag.getCodPartita());
				q1.setParameter("progr", anag.getProgressivoMinuta());
				
				AnagraficaImpostaDTO anagImp = new AnagraficaImpostaDTO();
				
				anagImp.setAnagraficaIntestatario(anag);
				
				List<RDatiContabili> datiContList = q1.getResultList();
				
				anagImp.setDatiContabili(datiContList);
				
				result.add(anagImp);
			}
			
		}
		catch(Throwable t) {
			throw new Flusso290ServiceException(t);
		}
		
		return result;
	}

	@Override
	public Long getRecordCount(Flusso290SearchCriteria criteria) {
		try {
			
			String sql = (new QueryBuilder(criteria)).createQuery(true);
			
			if (sql == null)
				return 0L;
			
			Query q = manager.createQuery(sql);
			Object o = q.getSingleResult();
			
			return (Long) o;
		}
		catch(Throwable t) {
			throw new Flusso290ServiceException(t);
		}
	}





}
