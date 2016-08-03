package it.webred.ct.service.spprof.data.access.dao.edifici;

import java.util.List;

import javax.persistence.Query;

import it.webred.ct.service.spprof.data.access.dao.SpProfBaseDAO;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfDAOException;
import it.webred.ct.service.spprof.data.model.SSpAreaFabb;
import it.webred.ct.service.spprof.data.model.SSpCedificato;
import it.webred.ct.service.spprof.data.model.SSpDestUrb;
import it.webred.ct.service.spprof.data.model.SSpEdificio;
import it.webred.ct.service.spprof.data.model.SSpEdificioMinore;
import it.webred.ct.service.spprof.data.model.SSpTipologiaEdi;
import it.webred.ct.service.spprof.data.model.SSpTipologiaEdiMin;
import it.webred.ct.service.spprof.data.model.SSpUiu;
import it.webred.ct.service.spprof.data.model.SSpUnitaVol;

public class SpProfEdificiDAOImpl extends SpProfBaseDAO implements SpProfEdificiDAO {

	public List<SSpTipologiaEdi> getTipologiaEdifici()throws SpProfDAOException {
		
		try {
			Query q = manager.createNamedQuery("SpProf.getSSpTipologiaEdi");
			return q.getResultList();
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}

	}
	
	public List<SSpTipologiaEdiMin> getTipologiaEdificiMinori()throws SpProfDAOException {
		
		try {
			Query q = manager.createNamedQuery("SpProf.getSSpTipologiaEdiMin");
			return q.getResultList();
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}

	}
	
	public SSpTipologiaEdi getTipologiaEdificiById(String id)throws SpProfDAOException {
		
		try {
			Query q = manager.createNamedQuery("SpProf.getTipologiaEdiById");
			q.setParameter("id", id);
			return (SSpTipologiaEdi) q.getSingleResult();
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}

	}
	
	public SSpTipologiaEdiMin getTipologiaEdificiMinoriById(String id)throws SpProfDAOException {
		
		try {
			Query q = manager.createNamedQuery("SpProf.getTipologiaEdiMinById");
			q.setParameter("id", id);
			return (SSpTipologiaEdiMin) q.getSingleResult();
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}

	}
	
	public SSpCedificato getEdificatoById(Long id)throws SpProfDAOException {
		
		try {
			Query q = manager.createNamedQuery("SpProf.getSSpCedificatoById");
			q.setParameter("id", id);
			return (SSpCedificato) q.getSingleResult();
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}

	}
	
	public List<SSpEdificio> getEdificiByintervento(Long intervento)throws SpProfDAOException {
		
		try {
			Query q = manager.createNamedQuery("SpProf.getSSpEdificiByIntervento");
			q.setParameter("intervento", intervento);
			return q.getResultList();
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}

	}
	
	public List<SSpEdificioMinore> getEdificiMinoriByintervento(Long intervento)throws SpProfDAOException {
		
		try {
			Query q = manager.createNamedQuery("SpProf.getSSpEdificiMinByIntervento");
			q.setParameter("intervento", intervento);
			return q.getResultList();
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}

	}
	
	public Long saveEdificato(SpProfDTO dto) throws SpProfDAOException {
		super.logger.info("SpProfEdificiDAOImpl::saveEdificato");
		try {
			SSpCedificato edi = (SSpCedificato)dto.getObj();
			manager.persist(edi);
			return edi.getIdSpCedificato();
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	public void saveEdificio(SpProfDTO dto) throws SpProfDAOException {
		super.logger.info("SpProfEdificiDAOImpl::saveEdificio");
		try {
			SSpEdificio edi = (SSpEdificio)dto.getObj();
			manager.persist(edi);
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	public void saveEdificioMinore(SpProfDTO dto) throws SpProfDAOException {
		super.logger.info("SpProfEdificiDAOImpl::saveEdificioMinore");
		try {
			SSpEdificioMinore edi = (SSpEdificioMinore)dto.getObj();
			manager.persist(edi);
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	public void updateEdificato(SpProfDTO dto) throws SpProfDAOException {
		super.logger.info("SpProfEdificiDAOImpl::updateEdificato");
		try {
			SSpCedificato edi = (SSpCedificato)dto.getObj();
			manager.merge(edi);
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	public void updateEdificio(SpProfDTO dto) throws SpProfDAOException {
		super.logger.info("SpProfEdificiDAOImpl::updateEdificio");
		try {
			SSpEdificio edi = (SSpEdificio)dto.getObj();
			manager.merge(edi);
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	public void updateEdificioMinore(SpProfDTO dto) throws SpProfDAOException {
		super.logger.info("SpProfEdificiDAOImpl::updateEdificioMinore");
		try {
			SSpEdificioMinore edi = (SSpEdificioMinore)dto.getObj();
			manager.merge(edi);
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	public void deleteEdificato(Long idEdificato) throws SpProfDAOException {
		super.logger.info("SpProfEdificiDAOImpl::deleteEdificato");
		try {
			Query q = manager.createNamedQuery("SpProf.deleteEdificato");
			q.setParameter("edificato", idEdificato);
			q.executeUpdate();
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	public void deleteEdificio(Long idEdificato) throws SpProfDAOException {
		super.logger.info("SpProfEdificiDAOImpl::deleteEdificio");
		try {
			Query q = manager.createNamedQuery("SpProf.deleteEdificio");
			q.setParameter("edificato", idEdificato);
			q.executeUpdate();
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	public void deleteEdificioMinore(Long idEdificato) throws SpProfDAOException {
		super.logger.info("SpProfEdificiDAOImpl::deleteEdificioMinore");
		try {
			Query q = manager.createNamedQuery("SpProf.deleteEdificioMinore");
			q.setParameter("edificato", idEdificato);
			q.executeUpdate();
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	public List<SSpDestUrb> getDestinazioneUso()throws SpProfDAOException {
		
		try {
			Query q = manager.createNamedQuery("SpProf.getSSpDestUrb");
			return q.getResultList();
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}

	}
	
	public SSpDestUrb getDestinazioneUsoById(String id)throws SpProfDAOException {
		
		try {
			Query q = manager.createNamedQuery("SpProf.getSSpDestUrbById");
			q.setParameter("id", id);
			return (SSpDestUrb) q.getSingleResult();
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}

	}
	
	public void saveUiu(SpProfDTO dto) throws SpProfDAOException {
		super.logger.info("SpProfEdificiDAOImpl::saveUiu");
		try {
			SSpUiu sp = (SSpUiu)dto.getObj();
			manager.persist(sp);
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	public void saveUnitaVol(SpProfDTO dto) throws SpProfDAOException {
		super.logger.info("SpProfEdificiDAOImpl::saveUnitaVol");
		try {
			SSpUnitaVol sp = (SSpUnitaVol)dto.getObj();
			manager.persist(sp);
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	public void updateUiu(SpProfDTO dto) throws SpProfDAOException {
		super.logger.info("SpProfEdificiDAOImpl::updateUiu");
		try {
			SSpUiu sp = (SSpUiu)dto.getObj();
			manager.merge(sp);
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	public void updateUnitaVol(SpProfDTO dto) throws SpProfDAOException {
		super.logger.info("SpProfEdificiDAOImpl::updateUnitaVol");
		try {
			SSpUnitaVol sp = (SSpUnitaVol)dto.getObj();
			manager.merge(sp);
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	public void deleteUiu(Long id) throws SpProfDAOException {
		super.logger.info("SpProfEdificiDAOImpl::deleteUiu");
		try {
			Query q = manager.createNamedQuery("SpProf.deleteUiu");
			q.setParameter("id", id);
			q.executeUpdate();
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	public void deleteUnitaVol(Long id) throws SpProfDAOException {
		super.logger.info("SpProfEdificiDAOImpl::deleteUnitaVol");
		try {
			Query q = manager.createNamedQuery("SpProf.deleteUnitaVol");
			q.setParameter("id", id);
			q.executeUpdate();
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	public List<SSpUiu> getUiuByEdificio(Long id) throws SpProfDAOException {
		super.logger.info("SpProfEdificiDAOImpl::getUiuByEdificio");
		try {
			Query q = manager.createNamedQuery("SpProf.getUiuByEdificio");
			q.setParameter("edificio", id);
			return q.getResultList();
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	public List<SSpUiu> getUiuByUnitaVol(Long id) throws SpProfDAOException {
		super.logger.info("SpProfEdificiDAOImpl::getUiuByUnitaVol");
		try {
			Query q = manager.createNamedQuery("SpProf.getUiuByUnitaVol");
			q.setParameter("uv", id);
			return q.getResultList();
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
	
	public List<SSpUnitaVol> getUnitaVolByEdificio(Long id) throws SpProfDAOException {
		super.logger.info("SpProfEdificiDAOImpl::getUnitaVolByEdificio");
		try {
			Query q = manager.createNamedQuery("SpProf.getUnitaVolByEdificio");
			q.setParameter("edificio", id);
			return q.getResultList();
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
	}
}
