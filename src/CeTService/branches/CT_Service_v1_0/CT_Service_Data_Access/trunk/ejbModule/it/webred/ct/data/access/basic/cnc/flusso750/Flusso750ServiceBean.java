package it.webred.ct.data.access.basic.cnc.flusso750;

import java.util.ArrayList;
import java.util.List;

import it.webred.ct.data.access.basic.cnc.CNCBaseService;
import it.webred.ct.data.access.basic.cnc.flusso750.Flusso750Service;
import it.webred.ct.data.access.basic.cnc.flusso750.dto.AnagraficaSoggettiDTO;
import it.webred.ct.data.access.basic.cnc.flusso750.dto.DettaglioRuoloVistatoDTO;
import it.webred.ct.data.access.basic.cnc.flusso750.dto.Flusso750SearchCriteria;
import it.webred.ct.data.access.basic.cnc.flusso750.dto.InfoRuoloDTO;
import it.webred.ct.data.model.cnc.flusso750.ChiaveULPartita;
import it.webred.ct.data.model.cnc.flusso750.ChiaveULRuolo;
import it.webred.ct.data.model.cnc.flusso750.VAnagrafeCointestatario;
import it.webred.ct.data.model.cnc.flusso750.VAnagrafeDebitore;
import it.webred.ct.data.model.cnc.flusso750.VAnagrafica;
import it.webred.ct.data.model.cnc.flusso750.VArticolo;
import it.webred.ct.data.model.cnc.flusso750.VFrontespizioRuolo;
import it.webred.ct.data.model.cnc.flusso750.VInizioRuolo;
import it.webred.ct.data.model.cnc.flusso750.VUlterioriBeneficiari;

import javax.ejb.Stateless;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

/**
 * Session Bean implementation class Flusso750ServiceBean
 */
@Stateless
public class Flusso750ServiceBean extends CNCBaseService implements Flusso750Service {

	@Override
	public List<AnagraficaSoggettiDTO> getAnagraficaByCodicePartita(String codicePartita) {
		
		logger.debug("Recupero Anagrafica soggetti 750 con codice partita ["+codicePartita+"]");
		List<AnagraficaSoggettiDTO> result = new ArrayList<AnagraficaSoggettiDTO>();
		
		try {
			Query q = manager.createNamedQuery("Flusso750.getAnagraficaDebitoreByParita");
			q.setParameter("codPartita", codicePartita);
			List<VAnagrafeDebitore> debList = q.getResultList();
			
			logger.debug("Lista anagrafica debitori ["+debList+"]");
			
			Query q1 = manager.createNamedQuery("Flusso750.getInfoRuoloByChiaveULRuolo");
			for (int i=0; i < debList.size(); i++) {
				VAnagrafeDebitore deb = debList.get(i);
				logger.debug("--- Chiave UL Ruolo ---");
				logger.debug("Anno ruolo ["+deb.getChiavePartita().getAnnoRuolo()+"]");
				logger.debug("Numero ["+deb.getChiavePartita().getProgressivoRuolo()+"]");
				logger.debug("Ambito ["+deb.getChiavePartita().getCodAmbito()+"]");
				logger.debug("Ente Cred. ["+deb.getChiavePartita().getCodEnteCreditore()+"]");
				
				q1.setParameter("annoRuolo", deb.getChiavePartita().getAnnoRuolo());
				q1.setParameter("codAmbito", deb.getChiavePartita().getCodAmbito());
				q1.setParameter("codEnteCreditore", deb.getChiavePartita().getCodEnteCreditore());
				q1.setParameter("progressivoRuolo", deb.getChiavePartita().getProgressivoRuolo());
				VInizioRuolo infoRuolo = (VInizioRuolo) q1.getSingleResult();
				
				AnagraficaSoggettiDTO anagSogg = new AnagraficaSoggettiDTO();
				
				anagSogg.addAnagDebitore(deb);
				anagSogg.setInfoRuolo(infoRuolo);
				
				result.add(anagSogg);
			}
		}
		catch (EntityNotFoundException  enfe) {
		}
		catch (Throwable t) {
			throw new Flusso750ServiceException(t);
		}
		
		return result;
	}

	@Override
	public List<VAnagrafica> getAnagraficaRuoliVistati(			
			Flusso750SearchCriteria criteria, int start, int numRecord) {
		
		
		List<VAnagrafica> anagList = new ArrayList<VAnagrafica>();
		
		try {
			String sql = ((new AnagQueryBuilder(criteria))).createQuery(false);
			logger.debug("SQL ["+sql+"]");
			
			
			if (sql == null)
				return anagList;
			
			Query q = manager.createQuery(sql);
			q.setFirstResult(start);
			q.setMaxResults(numRecord);
			anagList = q.getResultList();	
			
			logger.debug("Result size ["+anagList.size()+"]");
		}
		catch(Throwable t) {
			throw new Flusso750ServiceException(t);
		}
		
		return anagList;
		
	}

	@Override
	public Long getRecordCountAnagraficaRuoliVistati(
			Flusso750SearchCriteria criteria) {
		
		try {
			String sql = ((new AnagQueryBuilder(criteria))).createQuery(true);
			
			if (sql == null)
				return 0L;

			Query q = manager.createQuery(sql);
			Object o = q.getSingleResult();
			
			return (Long) o;
		}
		catch(Throwable t) {
			throw new Flusso750ServiceException(t);
		}
	}

	@Override
	public DettaglioRuoloVistatoDTO getDettaglioRuoloVistato(
			ChiaveULPartita chiave) {
		
		DettaglioRuoloVistatoDTO dettaglio = new DettaglioRuoloVistatoDTO();
		
		try {
			logger.debug("-- Chiave Ruolo --");
			logger.debug("Cod. Ente Cred. ["+chiave.getCodEnteCreditore()+"]");
			logger.debug("Cod. Ambito ["+chiave.getCodAmbito()+"]");
			logger.debug("Num/Anno Ruolo ["+chiave.getProgressivoRuolo()+"/" + chiave.getAnnoRuolo() + "]");

			logger.debug("-- Chiave Partita --");
			logger.debug("Tipo Uff. ["+chiave.getCodTipoUfficio()+"]");
			logger.debug("Cod. Uff. ["+chiave.getCodUfficio()+"]");
			logger.debug("Anno rif. ["+chiave.getAnnoRiferimento()+"]");
			logger.debug("Cod. Parita ["+chiave.getCodicePartita()+"]");
			
			Query q = createQuery("Flusso750.getAnagraficaDebitoreByChiavePartita", chiave, false);
			List<VAnagrafeDebitore> debList = q.getResultList();
			
			logger.debug("Lista anagrafe deb. OK. ["+debList+"]");
			
			Query q1 = createQuery("Flusso750.getAnagraficaCointestatarioByChiavePartita", chiave, false);
			List<VAnagrafeCointestatario> cointList = q1.getResultList();
			logger.debug("Lista anagrafe coint. OK. ["+cointList+"]");
			
			Query q2 = createQuery("Flusso750.getArticoliByChiavePartita", chiave, false);
			List<VArticolo> artList = q2.getResultList();
			logger.debug("Lista art. OK. ["+artList+"]");
			
			Query q3 = createQuery("Flusso750.getInfoRuoloByChiaveULRuolo", chiave, true);
			VInizioRuolo infoRuolo = (VInizioRuolo) q3.getSingleResult();
			logger.debug("Info ruolo. OK. ["+infoRuolo+"]");

			Query q4 = createQuery("Flusso750.getUlterioriBenefByChiavePartita", chiave, false);
			List<VUlterioriBeneficiari>  benefList = q4.getResultList();
			logger.debug("Lista ult benef. OK. ["+benefList+"]");

			AnagraficaSoggettiDTO anagDTO = new AnagraficaSoggettiDTO();
			anagDTO.setAnagraficaIntestatarioList(debList);
			anagDTO.setAnagraficaCointestatarioList(cointList);
			anagDTO.setInfoRuolo(infoRuolo);
			
			dettaglio.setArticoliList(artList);
			dettaglio.setAnagrafica(anagDTO);
			dettaglio.setUlterioriBeneficiari(benefList);
		}
		catch(Throwable t) {
			throw new Flusso750ServiceException(t);
		}
		
		return dettaglio;
	}

 
	private Query createQuery(String namedQueryName, ChiaveULPartita chiave, boolean isRuoloKey) {
		Query q = manager.createNamedQuery(namedQueryName);
		q.setParameter("codEnteCreditore", chiave.getCodEnteCreditore());
		q.setParameter("codAmbito", chiave.getCodAmbito());
		q.setParameter("annoRuolo", chiave.getAnnoRuolo());
		q.setParameter("progressivoRuolo", chiave.getProgressivoRuolo());
		
		if (!isRuoloKey) {
			q.setParameter("annoRiferimento", chiave.getAnnoRiferimento());
			q.setParameter("codicePartita", chiave.getCodicePartita());
			q.setParameter("codTipoUfficio", chiave.getCodTipoUfficio());
			q.setParameter("codUfficio", chiave.getCodUfficio());
		}
		
		return q;
	}

	@Override
	public Long getRecordCountRuoliVistati(Flusso750SearchCriteria criteria) {
		try {
			String sql = ((new RuoloQueryBuilder(criteria))).createQuery(true);
			
			if (sql == null)
				return 0L;

			Query q = manager.createQuery(sql);
			Object o = q.getSingleResult();
			
			return (Long) o;
		}
		catch(Throwable t) {
			throw new Flusso750ServiceException(t);
		}
	}

	@Override
	public List<InfoRuoloDTO> getRuoliVistati(Flusso750SearchCriteria criteria,
			int start, int numRecord) {
		
		List<InfoRuoloDTO> ruoliList = new ArrayList<InfoRuoloDTO>();
		
		try {
			String sql = ((new RuoloQueryBuilder(criteria))).createQuery(false);
			logger.debug("SQL ["+sql+"]");
			
			
			if (sql == null)
				return ruoliList;
			
			Query q = manager.createQuery(sql);
			q.setFirstResult(start);
			q.setMaxResults(numRecord);
			ruoliList = q.getResultList();	
			
			logger.debug("Result size ["+ruoliList.size()+"]");
		}
		catch(Throwable t) {
			throw new Flusso750ServiceException(t);
		}
		
		return ruoliList;
	}

	@Override
	public InfoRuoloDTO getFrontespizioRuolo(ChiaveULRuolo chiaveRuolo) {
		InfoRuoloDTO infoRuolo = new InfoRuoloDTO();
		
		try {
			Query q = manager.createNamedQuery("Flusso750.getFrontespizioRuoloByChiaveRuolo");
			q.setParameter("codEnteCreditore", chiaveRuolo.getCodEnteCreditore());
			q.setParameter("annoRuolo", chiaveRuolo.getAnnoRuolo());
			q.setParameter("progressivoRuolo", chiaveRuolo.getProgressivoRuolo());
			q.setParameter("codAmbito", chiaveRuolo.getCodAmbito());
			
			List<VFrontespizioRuolo> fronteList = q.getResultList();
			
			infoRuolo.setInfoFrontespizioList(fronteList);
		}
		catch (Throwable t) {
			throw new Flusso750ServiceException(t); 
		}
		
		return infoRuolo;
	}
}
