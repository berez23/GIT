package it.webred.ct.diagnostics.service.data.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import it.webred.ct.diagnostics.service.data.dao.DIABaseDAO;
import it.webred.ct.diagnostics.service.data.dao.IDAOListaDiagnostiche;
import it.webred.ct.diagnostics.service.data.dto.DiaLogAccessoDTO;
import it.webred.ct.diagnostics.service.data.exception.DIAServiceException;

import it.webred.ct.diagnostics.service.data.model.DiaLogAccesso;
import it.webred.ct.diagnostics.service.data.model.DiaTestata;
import it.webred.ct.diagnostics.service.data.model.controller.DiaCatalogo;

public class DAOListaDiagnosticheImpl extends DIABaseDAO implements	IDAOListaDiagnostiche {

	public DiaCatalogo getDiagnostica(long idDiagnostica)
			throws DIAServiceException {

		DiaCatalogo dc = null;
		try {
			logger.debug("DIADiagnosticheJpaImpl.getDiagnostica idDiagnostica "
					+ idDiagnostica);
			Query q = manager.createNamedQuery("DIA.getDiagnosticaById");
			q.setParameter("idDiagnostica", idDiagnostica);
			dc = (DiaCatalogo) q.getSingleResult();
		} catch (NoResultException nre) {
			logger.warn("DIADiagnosticheJpaImpl.getDiagnostica - No Result "
					+ nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
		return dc;
	}

	public List<DiaCatalogo> getDiagnostiche() throws DIAServiceException {

		try {
			logger.debug("LISTA DIAGNOSTICHE GIA' ESEGUITE ");
			Query q = manager.createNamedQuery("DIA.getDiagnostiche");
			return q.getResultList();

		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}

	public List<DiaCatalogo> getDiagnosticheByCodCmdGrp(String codCommandGroup)
			throws DIAServiceException {
		try {
			logger.debug("LISTA DIAGNOSTICHE BY CodCommand ");
			Query q = manager
					.createNamedQuery("DIA.getDiagnosticheByCodCmdGrp");
			q.setParameter("codCommandGrp", codCommandGroup);
			return q.getResultList();

		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}
	
	public List<DiaTestata> getDiaEsecuzioneByIdDia(int start,
			int maxrows,long idCatalogoDia)
			throws DIAServiceException {
		try {
			logger.debug("LISTA ESECUZIONE DIAGNOSTICHE BY IdCatalogoDia ");
			Query q = manager
					.createNamedQuery("DIA.getDiaEsecuzioneByIdDia");
			q.setParameter("idCatalogoDia", idCatalogoDia);
			q.setFirstResult(start);
			q.setMaxResults(maxrows);
			return q.getResultList();

		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}

	public List<DiaCatalogo> getDiagnosticheByCodCommand(String codCommand)
			throws DIAServiceException {
		try {
			logger.debug("LISTA DIAGNOSTICHE BY CodCommand ");
			Query q = manager
					.createNamedQuery("DIA.getDiagnosticheByCodCommand");
			q.setParameter("codCommand", codCommand);
			return q.getResultList();

		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}

	public List<DiaLogAccesso> getLogAccessiByDiaTestata(int start,
			int maxrows, Long idDiaTestata)
			throws DIAServiceException {
		try {
			logger.debug("LISTA LOG ACCESSI BY IdDiaTestata ");
			Query q = manager
					.createNamedQuery("DIA.getLogAccessiByIdDiaTestata");
			q.setParameter("idDiaTestata", idDiaTestata);
			q.setFirstResult(start);
			q.setMaxResults(maxrows);
			return q.getResultList();
			

		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
	}

	public void insertLogAccesso(DiaLogAccessoDTO logAccesso)
			throws DIAServiceException {
		try {
			logger.debug("insertLogAccesso ");	
			DiaLogAccesso dla = logAccesso.getObjDiaLogAccessi();
			if (dla != null)
				manager.persist(dla);			

		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
		
	}

	public Long getCountLogByDiaTestata(Long idDiaTestata)
			throws DIAServiceException {
		
		Long count = new Long(0);
		
		try {
			logger.debug("LISTA LOG ACCESSI BY IdDiaTestata ");
			Query q = manager
					.createNamedQuery("DIA.getCountLogByDiaTestata");
			q.setParameter("idDiaTestata", idDiaTestata);
			count = (Long)q.getSingleResult();

		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
					
		return count;
	}

	public List<DiaCatalogo> getDiagnostiche(int start, int maxrows,
			String belfiore, List<Long> fonti, Long tipoComando) throws DIAServiceException {
		
		List<DiaCatalogo> ll = new ArrayList<DiaCatalogo>();
		
		try {
			logger.debug("LISTA DIAGNOSTICHE ");
			Query q = manager.createNamedQuery("DIA.getDiagnostichePag");
			
			q.setFirstResult(start);
			q.setMaxResults(maxrows);
			q.setParameter("tipoComando", tipoComando);
			q.setParameter("fonti", fonti);
			q.setParameter("idEnte", "%"+belfiore+"%");
			
			ll = q.getResultList();

		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
		
		return ll;
	}

	public Long getDiagnosticheCount(int start, int maxrows,
					String belfiore, List<Long> fonti, Long tipoComando) throws DIAServiceException {
		Long count = new Long(0);
		
		try {
			logger.debug("LISTA Diagnostiche count ");
			Query q = manager.createNamedQuery("DIA.getDiagnosticheCount");
			
			q.setFirstResult(start);
			q.setMaxResults(maxrows);
			q.setParameter("tipoComando", tipoComando);
			q.setParameter("fonti", fonti);
			q.setParameter("idEnte", "%"+belfiore+"%");
			
			count = (Long)q.getSingleResult();

		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
					
		return count;
	}
	
	public List<DiaCatalogo> getAllDiagnosticheFonte(String belfiore, Long fonte) throws DIAServiceException {
		List<DiaCatalogo> ll = new ArrayList<DiaCatalogo>();
		
		try {
			logger.debug("LISTA All Diagnostiche Fonte ");
			Query q = manager.createNamedQuery("DIA.getAllDiagnosticheFonte");
			
			q.setParameter("fonte", fonte);
			q.setParameter("idEnte", "%"+belfiore+"%");
			
			ll = q.getResultList();
		
		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
					
		return ll;
	}

	public DiaTestata getDiaTestata(Long idDiaTestata) throws DIAServiceException {
		DiaTestata dt = null;
		try {
			logger.debug("DIADiagnosticheJpaImpl.getDiaTestata idDiaTestata "
					+ idDiaTestata);
			Query q = manager.createNamedQuery("DIA.getDiaTestata");
			q.setParameter("idDiaTestata", idDiaTestata);
			dt = (DiaTestata) q.getSingleResult();
		} catch (NoResultException nre) {
			logger.warn("DIADiagnosticheJpaImpl.getDiaTestata - No Result "
					+ nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
		return dt;
	}
	
	/**
	 * Il metodo lancia un warning gestito quando per l'idCatalogoDia non esistono
	 * elaborazioni
	 */
	public DiaTestata getLastEsecuzioneByIdDia(long idCatalogoDia)
			throws DIAServiceException {
		DiaTestata dt = null;
		try {
			logger.debug("DIADiagnosticheJpaImpl.getLastEsecuzioneByIdDia idCatalogoDia "
					+ idCatalogoDia);
			Query q = manager.createNamedQuery("DIA.getLastEsecuzioneByIdDia");
			q.setParameter("idCatalogoDia", idCatalogoDia);
			dt = (DiaTestata) q.getSingleResult();
		}  catch (Throwable t) {
			logger.warn("getLastEsecuzioneByIdDia nessuna esecuzione per idCatalogoDia:" + idCatalogoDia);
			//throw new DIAServiceException(t);
		}
		return dt;
	}

	public Long getStoricoEsecuzioniCount(long idCatalogoDia)
			throws DIAServiceException {
		Long count = new Long(0);
		
		try {			
			Query q = manager
					.createNamedQuery("DIA.getStoricoEsecuzioniCount");
			q.setParameter("idCatalogoDia", idCatalogoDia);
			count = (Long)q.getSingleResult();

		} catch (Throwable t) {
			logger.error("", t);
			throw new DIAServiceException(t);
		}
					
		return count;
	}


}
