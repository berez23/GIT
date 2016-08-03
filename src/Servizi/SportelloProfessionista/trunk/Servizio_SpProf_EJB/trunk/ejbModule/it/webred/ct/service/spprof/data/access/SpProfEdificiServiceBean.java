package it.webred.ct.service.spprof.data.access;

import java.util.List;

import it.webred.ct.service.spprof.data.access.dao.edifici.SpProfEdificiDAO;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfDAOException;
import it.webred.ct.service.spprof.data.access.exception.SpProfException;
import it.webred.ct.service.spprof.data.model.SSpCedificato;
import it.webred.ct.service.spprof.data.model.SSpDestUrb;
import it.webred.ct.service.spprof.data.model.SSpEdificio;
import it.webred.ct.service.spprof.data.model.SSpEdificioMinore;
import it.webred.ct.service.spprof.data.model.SSpTipologiaEdi;
import it.webred.ct.service.spprof.data.model.SSpTipologiaEdiMin;
import it.webred.ct.service.spprof.data.model.SSpUiu;
import it.webred.ct.service.spprof.data.model.SSpUnitaVol;

import javax.ejb.Stateless;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class SpProfInterventoServiceBean
 */
@Stateless
public class SpProfEdificiServiceBean implements SpProfEdificiService {

	@Autowired
	private SpProfEdificiDAO edificiDAO;

	public List<SSpTipologiaEdi> getTipologiaEdifici(SpProfDTO dto)
			throws SpProfDAOException {
		try {
			return edificiDAO.getTipologiaEdifici();
		} catch (Throwable t) {
			throw new SpProfException(t);
		}
	}

	public List<SSpTipologiaEdiMin> getTipologiaEdificiMinori(SpProfDTO dto)
			throws SpProfDAOException {
		try {
			return edificiDAO.getTipologiaEdificiMinori();
		} catch (Throwable t) {
			throw new SpProfException(t);
		}
	}

	public SSpTipologiaEdi getTipologiaEdificiById(SpProfDTO dto)
			throws SpProfDAOException {

		try {
			return edificiDAO.getTipologiaEdificiById((String) dto.getObj());
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public SSpTipologiaEdiMin getTipologiaEdificiMinoriById(SpProfDTO dto)
			throws SpProfDAOException {

		try {
			return edificiDAO.getTipologiaEdificiMinoriById((String) dto
					.getObj());
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public SSpCedificato getEdificatoById(SpProfDTO dto)
			throws SpProfDAOException {

		try {
			return edificiDAO.getEdificatoById((Long) dto.getObj());
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public List<SSpEdificio> getEdificiByintervento(SpProfDTO dto)
			throws SpProfDAOException {

		try {
			return edificiDAO.getEdificiByintervento((Long) dto.getObj());
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public List<SSpEdificioMinore> getEdificiMinoriByintervento(SpProfDTO dto)
			throws SpProfDAOException {

		try {
			return edificiDAO.getEdificiMinoriByintervento((Long) dto.getObj());
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public Long saveEdificato(SpProfDTO dto) throws SpProfDAOException {

		try {
			return edificiDAO.saveEdificato(dto);
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public void saveEdificio(SpProfDTO dto) throws SpProfDAOException {

		try {
			edificiDAO.saveEdificio(dto);
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public void saveEdificioMinore(SpProfDTO dto) throws SpProfDAOException {

		try {
			edificiDAO.saveEdificioMinore(dto);
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public void updateEdificato(SpProfDTO dto) throws SpProfDAOException {

		try {
			edificiDAO.updateEdificato(dto);
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public void updateEdificio(SpProfDTO dto) throws SpProfDAOException {

		try {
			edificiDAO.updateEdificio(dto);
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public void updateEdificioMinore(SpProfDTO dto) throws SpProfDAOException {

		try {
			edificiDAO.updateEdificioMinore(dto);
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public void deleteEdificato(SpProfDTO dto) throws SpProfDAOException {

		try {
			edificiDAO.deleteEdificato((Long) dto.getObj());
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public void deleteEdificio(SpProfDTO dto) throws SpProfDAOException {

		try {
			edificiDAO.deleteEdificio((Long) dto.getObj());
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public void deleteEdificioMinore(SpProfDTO dto) throws SpProfDAOException {

		try {
			edificiDAO.deleteEdificioMinore((Long) dto.getObj());
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}

	public List<SSpDestUrb> getDestinazioneUso(SpProfDTO dto)
			throws SpProfDAOException {
		try {
			return edificiDAO.getDestinazioneUso();
		} catch (Throwable t) {
			throw new SpProfException(t);
		}
	}

	public SSpDestUrb getDestinazioneUsoById(SpProfDTO dto)
			throws SpProfDAOException {
		try {
			return edificiDAO.getDestinazioneUsoById((String) dto.getObj());
		} catch (Throwable t) {
			throw new SpProfException(t);
		}
	}
	
	public void saveUiu(SpProfDTO dto) throws SpProfDAOException {

		try {
			edificiDAO.saveUiu(dto);
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}
	
	public void saveUnitaVol(SpProfDTO dto) throws SpProfDAOException {

		try {
			edificiDAO.saveUnitaVol(dto);
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}
	
	public void updateUiu(SpProfDTO dto) throws SpProfDAOException {

		try {
			edificiDAO.updateUiu(dto);
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}
	
	public void updateUnitaVol(SpProfDTO dto) throws SpProfDAOException {

		try {
			edificiDAO.updateUnitaVol(dto);
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}
	
	public void deleteUiu(SpProfDTO dto) throws SpProfDAOException {

		try {
			edificiDAO.deleteUiu((Long) dto.getObj());
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}
	
	public void deleteUnitaVol(SpProfDTO dto) throws SpProfDAOException {

		try {
			edificiDAO.deleteUnitaVol((Long) dto.getObj());
		} catch (Throwable t) {
			throw new SpProfDAOException(t);
		}

	}
	
	public List<SSpUiu> getUiuByEdificio(SpProfDTO dto)
			throws SpProfDAOException {
		try {
			return edificiDAO.getUiuByEdificio((Long) dto.getObj());
		} catch (Throwable t) {
			throw new SpProfException(t);
		}
	}
	
	public List<SSpUiu> getUiuByUnitaVol(SpProfDTO dto)
			throws SpProfDAOException {
		try {
			return edificiDAO.getUiuByUnitaVol((Long) dto.getObj());
		} catch (Throwable t) {
			throw new SpProfException(t);
		}
	}
	
	public List<SSpUnitaVol> getUnitaVolByEdificio(SpProfDTO dto)
			throws SpProfDAOException {
		try {
			return edificiDAO.getUnitaVolByEdificio((Long) dto.getObj());
		} catch (Throwable t) {
			throw new SpProfException(t);
		}
	}
}
