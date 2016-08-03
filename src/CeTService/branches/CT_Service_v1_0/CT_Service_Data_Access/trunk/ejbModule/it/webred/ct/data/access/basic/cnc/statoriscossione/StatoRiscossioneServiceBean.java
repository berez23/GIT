package it.webred.ct.data.access.basic.cnc.statoriscossione;

import java.util.ArrayList;
import java.util.List;

import it.webred.ct.data.access.basic.cnc.CNCBaseService;
import it.webred.ct.data.access.basic.cnc.CNCSearchCriteria;
import it.webred.ct.data.access.basic.cnc.statoriscossione.dto.FullRiscossioneInfo;
import it.webred.ct.data.access.basic.cnc.statoriscossione.dto.StatoRiscossioneSearchCriteria;
import it.webred.ct.data.model.cnc.statoriscossione.ChiaveULStatoRiscossione;
import it.webred.ct.data.model.cnc.statoriscossione.SRiscossioni;
import it.webred.ct.data.model.cnc.statoriscossione.SRiversamenti;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * Session Bean implementation class StatoRiscossioneServiceBean
 */
@Stateless
public class StatoRiscossioneServiceBean  extends CNCBaseService implements StatoRiscossioneService {

	@Override
	public List<SRiscossioni> getRiscossioni(
			StatoRiscossioneSearchCriteria criteria, int start, int numRecord) {
		
		List<SRiscossioni> riscossioniList = new ArrayList<SRiscossioni>();
		
		try {
			String sql = ((new StatoQueryBuilder(criteria))).createQuery(false);
			logger.debug("SQL ["+sql+"]");
		
			if (sql == null)
				return riscossioniList;
			
			Query q = manager.createQuery(sql);
			q.setFirstResult(start);
			q.setMaxResults(numRecord);
			riscossioniList = q.getResultList();	
			
			logger.debug("Result size ["+riscossioniList.size()+"]");
		}
		catch(Throwable t) {
			throw new StatoRiscossioneServiceException(t);			
		}
		
		return riscossioniList;
		
	}

	@Override
	public Long getRecordCountRiscossioni(
			StatoRiscossioneSearchCriteria criteria) {
		try {
			String sql = ((new StatoQueryBuilder(criteria))).createQuery(true);
			
			if (sql == null)
				return 0L;

			Query q = manager.createQuery(sql);
			Object o = q.getSingleResult();
			
			return (Long) o;
		}
		catch(Throwable t) {
			throw new StatoRiscossioneServiceException(t);
		}
	}

	@Override
	public FullRiscossioneInfo getRiscossioneInfo(
			ChiaveULStatoRiscossione chiave) {
		
		FullRiscossioneInfo info = new FullRiscossioneInfo();
		
		try {
			Query q = manager.createNamedQuery("StatoRiscossioni.getRiscossioneByChiaveULRiscossione");
			q.setParameter("annoRuolo", chiave.getAnnoRuolo());
			q.setParameter("codAmbito", chiave.getCodAmbito());
			q.setParameter("codEnteCreditore", chiave.getCodEnteCreditore());
			q.setParameter("codPartita", chiave.getCodPartita());
			q.setParameter("dtRegistrazione", chiave.getDtRegistrInformaz());
			q.setParameter("numeroOperazContab", chiave.getNumeroOperazContab());
			q.setParameter("progressivoArticoloRuolo", chiave.getProgressivoArticoloRuolo());
			q.setParameter("progressivoOperazContab", chiave.getProgressivoOperazContab());
			q.setParameter("progressivoRuolo", chiave.getProgressivoRuolo());
			
			logger.debug("Anno Ruolo ["+chiave.getAnnoRuolo()+"]");
			logger.debug("Cod Ambito ["+chiave.getCodAmbito()+"]");
			logger.debug("Cod Ente cred. ["+chiave.getCodEnteCreditore()+"]");
			logger.debug("Cod. part. ["+chiave.getCodPartita()+"]");
			logger.debug("dtRegistrazione ["+chiave.getDtRegistrInformaz()+"]");
			logger.debug("numeroOperazContab ["+chiave.getNumeroOperazContab()+"]");
			logger.debug("progressivoArticoloRuolo ["+chiave.getProgressivoArticoloRuolo()+"]");
			logger.debug("progressivoOperazContab ["+chiave.getProgressivoOperazContab()+"]");
			logger.debug("progressivoRuolo ["+chiave.getProgressivoRuolo()+"]");
			
			List<SRiscossioni> risList = q.getResultList();
			
			logger.debug("Riscossione OK. Size ["+risList.size()+"]");
			
			Query q1 = manager.createNamedQuery("StatoRiscossione.getRiversamentiByChiaveULRiscossione");
			q1.setParameter("annoRuolo", chiave.getAnnoRuolo());
			q1.setParameter("codAmbito", chiave.getCodAmbito());
			q1.setParameter("codEnteCreditore", chiave.getCodEnteCreditore());
			q1.setParameter("codPartita", chiave.getCodPartita());
			q1.setParameter("dtRegistrazione", chiave.getDtRegistrInformaz());
			q1.setParameter("numeroOperazContab", chiave.getNumeroOperazContab());
			q1.setParameter("progressivoArticoloRuolo", chiave.getProgressivoArticoloRuolo());
			q1.setParameter("progressivoOperazContab", chiave.getProgressivoOperazContab());
			q1.setParameter("progressivoRuolo", chiave.getProgressivoRuolo());
			
			List<SRiversamenti> rivList = q1.getResultList();
			
			logger.debug("Riversamenti OK. Size ["+rivList.size()+"]");
			
			info.setRiscossioniList(risList);
			info.setRiversamentiList(rivList);
		}
		catch(Throwable t) {
			throw new StatoRiscossioneServiceException(t);
		}
		
		return info;
	}



}
