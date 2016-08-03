package it.webred.ct.service.tsSoggiorno.data.access.dao;

import it.webred.ct.service.tsSoggiorno.data.access.TsSoggiornoServiceException;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.DichiarazioneSearchCriteria;
import it.webred.ct.service.tsSoggiorno.data.access.dto.RimbSanzDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.RimbSanzSearchCriteria;
import it.webred.ct.service.tsSoggiorno.data.access.dto.TariffaDTO;
import it.webred.ct.service.tsSoggiorno.data.model.IsClasse;
import it.webred.ct.service.tsSoggiorno.data.model.IsClassiStruttura;
import it.webred.ct.service.tsSoggiorno.data.model.IsDichiarazione;
import it.webred.ct.service.tsSoggiorno.data.model.IsPeriodo;
import it.webred.ct.service.tsSoggiorno.data.model.IsRimborso;
import it.webred.ct.service.tsSoggiorno.data.model.IsSanzione;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocieta;
import it.webred.ct.service.tsSoggiorno.data.model.IsStruttura;
import it.webred.ct.service.tsSoggiorno.data.model.IsStrutturaSnap;
import it.webred.ct.service.tsSoggiorno.data.model.IsTariffa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class RimbSanzJPAImpl extends TsSoggiornoBaseDAO implements
		RimbSanzDAO {
	
	public List<RimbSanzDTO> getRimbSanzByCriteria(
			RimbSanzSearchCriteria criteria) {

		List<RimbSanzDTO> listaDTO = new ArrayList<RimbSanzDTO>();
		try {

			String sql = "";
			if("R".equals(criteria.getRimbsanz()))
			sql = (new RimbSanzQueryBuilder(criteria))
					.createQueryRimborso(false);
			else sql = (new RimbSanzQueryBuilder(criteria))
					.createQuerySanzione(false);
			Query q = manager.createQuery(sql);
			if (criteria.getStart() != 0 || criteria.getRowNumber() != 0) {
				q.setFirstResult(criteria.getStart());
				q.setMaxResults(criteria.getRowNumber());
			}
			List<Object[]> lista = q.getResultList();
			for (Object[] obj : lista) {
				RimbSanzDTO dto = new RimbSanzDTO();
				if("R".equals(criteria.getRimbsanz()))
					dto.setRimborso((IsRimborso) obj[0]);
				else dto.setSanzione((IsSanzione) obj[0]);
				dto.setClasse((IsClassiStruttura) obj[1]);
				dto.setSocieta((IsSocieta) obj[2]);
				dto.setStruttura((IsStruttura) obj[3]);
				dto.setPeriodo((IsPeriodo) obj[4]);
				listaDTO.add(dto);
			}

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
		return listaDTO;
	}
	
	public Long getRimbSanzCountByCriteria(RimbSanzSearchCriteria criteria) {

		List<DichiarazioneDTO> listaDTO = new ArrayList<DichiarazioneDTO>();
		try {

			String sql = "";
			if("R".equals(criteria.getRimbsanz()))
			sql = (new RimbSanzQueryBuilder(criteria))
					.createQueryRimborso(true);
			else sql = (new RimbSanzQueryBuilder(criteria))
					.createQuerySanzione(true);
			Query q = manager.createQuery(sql);
			Long l = (Long) q.getSingleResult();
			return l == null? new Long(0): l;

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public void saveRimborso(IsRimborso r) throws TsSoggiornoServiceException {
		try {

			manager.persist(r);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public void saveSanzione(IsSanzione s) throws TsSoggiornoServiceException {
		try {

			manager.persist(s);

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public void deleteRimborso(Long id) throws TsSoggiornoServiceException {
		try {

			Query q = manager.createNamedQuery(
					"IsRimborso.deleteRimborsoById")
					.setParameter("id", id);
			q.executeUpdate();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
	
	public void deleteSanzione(Long id) throws TsSoggiornoServiceException {
		try {

			Query q = manager.createNamedQuery(
					"IsSanzione.deleteSanzioneById")
					.setParameter("id", id);
			q.executeUpdate();

		} catch (Throwable t) {
			logger.error(t.getMessage(),t);
			throw new TsSoggiornoServiceException(t);
		}
	}
}
