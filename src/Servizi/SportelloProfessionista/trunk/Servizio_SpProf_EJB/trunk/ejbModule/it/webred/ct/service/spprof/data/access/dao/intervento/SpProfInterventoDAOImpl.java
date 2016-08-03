package it.webred.ct.service.spprof.data.access.dao.intervento;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import it.webred.ct.data.access.basic.pgt.PgtServiceException;
import it.webred.ct.service.spprof.data.access.dao.SpProfBaseDAO;
import it.webred.ct.service.spprof.data.access.dao.intervento.QueryBuilder;
import it.webred.ct.service.spprof.data.access.dto.InterventoSearchCriteria;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfDAOException;
import it.webred.ct.service.spprof.data.model.SSpIntervento;
import it.webred.ct.service.spprof.data.model.SSpInterventoLayer;
import it.webred.ct.service.spprof.data.model.SSpInterventoLayerPK;
import it.webred.ct.service.spprof.data.model.SSpSoggetto;

public class SpProfInterventoDAOImpl extends SpProfBaseDAO implements
		SpProfInterventoDAO {

	public List<SSpIntervento> getIntervento(InterventoSearchCriteria criteria)
			throws SpProfDAOException {

		try {
			String sql = (new QueryBuilder(criteria)).createQuery(false);
			logger.debug("getIntervento SQL["+sql+"]");
			Query q = manager.createQuery(sql);
			q.setFirstResult(criteria.getStart());
			q.setMaxResults(criteria.getRecord());
			return q.getResultList();
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public Long getCountIntervento(InterventoSearchCriteria criteria) throws SpProfDAOException {

		try {
			String sql = (new QueryBuilder(criteria)).createQuery(true);
			logger.debug("getCountIntervento SQL["+sql+"]");
			if (sql == null)
				return 0L;

			Query q = manager.createQuery(sql);
			return (Long) q.getSingleResult();
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public List<SSpIntervento> getInterventoByUser(String username, int start,
			int record) throws SpProfDAOException {

		try {
			Query q = manager.createNamedQuery("SpProf.getSSpInterventoByUser");
			q.setParameter("user", username);
			q.setFirstResult(start);
			q.setMaxResults(record);
			return q.getResultList();
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public Long getCountInterventoByUser(String username)
			throws SpProfDAOException {

		try {
			Query q = manager
					.createNamedQuery("SpProf.getCountSSpInterventoByUser");
			q.setParameter("user", username);
			return (Long) q.getSingleResult();
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public SSpIntervento save(SpProfDTO dto) throws SpProfDAOException {

		try {
			SSpIntervento intervento = (SSpIntervento) dto.getObj();
			if (intervento.getIdSpIntervento() == null)
				manager.persist(intervento);
			else
				manager.merge(intervento);
			return intervento;
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public SSpIntervento getIntervento(Long idIntervento)
			throws SpProfDAOException {
		SSpIntervento intervento = null;

		try {
			// recupero oggetto da db e modifica
			Query q = manager.createNamedQuery("SpProf.getSSpIntervento");
			q.setParameter("idSpIntervento", idIntervento);
			intervento = (SSpIntervento) q.getSingleResult();
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

		return intervento;
	}

	public Long update(SpProfDTO dto) throws SpProfDAOException {
		try {
			SSpIntervento intervento = (SSpIntervento) dto.getObj();
			Query q = manager.createNamedQuery("SpProf.updateSSpIntervento");
			q.setParameter("stato", intervento.getStato());
			q.setParameter("idSpIntervento", intervento.getIdSpIntervento());

			q.executeUpdate();
			return intervento.getIdSpIntervento();
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}
	}

	public List<SSpInterventoLayer> getSSpInterventoLayerByFkSpIntervento(
			Long idIntervento) throws SpProfDAOException {
		List<SSpInterventoLayer> ret = new ArrayList<SSpInterventoLayer>();

		try {
			Query q = manager
					.createNamedQuery("SpProf.getSSpInterventoLayerByFkSpIntervento");
			q.setParameter("fkSpIntervento", idIntervento);
			ret = q.getResultList();
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

		return ret;
	}

	public SSpInterventoLayer saveInterventoLayer(Long idIntervento, Long idPgt)
			throws SpProfDAOException {

		try {
			SSpInterventoLayer interventoLayer = new SSpInterventoLayer();
			interventoLayer.setFkPgtLayer(idPgt);

			SSpInterventoLayerPK pk = new SSpInterventoLayerPK();
			pk.setTipoRelazione("A");
			pk.setFkSpIntervento(idIntervento);

			interventoLayer.setId(pk);

			manager.persist(interventoLayer);
			return interventoLayer;
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}
	}

	public void deleteInteventoLayer(SpProfDTO dto) throws SpProfDAOException {
		try {
			SSpInterventoLayer delete = (SSpInterventoLayer) dto.getObj();
			logger.debug("CANCELLAZIONE INTERVENTO LAYER ID: "
					+ delete.getId().getFkSpIntervento());
			SSpInterventoLayer lay = manager.getReference(
					SSpInterventoLayer.class, delete.getId());
			manager.remove(lay);
		} catch (Throwable t) {
			logger.error("", t);
			throw new PgtServiceException(t);
		}

	}

	public void delete(SpProfDTO dto) throws SpProfDAOException {
		try {
			Long idIntervento = (Long) dto.getObj();
			Query q = manager.createNamedQuery("SpProf.deleteIntervento");
			q.setParameter("idSpIntervento", idIntervento);

			q.executeUpdate();

		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}
	}

	public List<String> getStatiForSearch() throws SpProfDAOException {

		try {
			Query q = manager.createNamedQuery("SpProf.getStatiForSearch");
			return q.getResultList();
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public List<SSpSoggetto> getSoggettiForSearch() throws SpProfDAOException {

		try {
			Query q = manager.createNamedQuery("SpProf.getSoggettiForSearch");
			return q.getResultList();
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public List<String> getConcNumeroForSearch() throws SpProfDAOException {

		try {
			Query q = manager.createNamedQuery("SpProf.getConcNumeroForSearch");
			return q.getResultList();
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public List<String> getProtDataForSearch() throws SpProfDAOException {

		try {
			Query q = manager.createNamedQuery("SpProf.getProtDataForSearch");
			return q.getResultList();
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public List<String> getProtNumeroForSearch() throws SpProfDAOException {

		try {
			Query q = manager.createNamedQuery("SpProf.getProtNumeroForSearch");
			return q.getResultList();
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

}
