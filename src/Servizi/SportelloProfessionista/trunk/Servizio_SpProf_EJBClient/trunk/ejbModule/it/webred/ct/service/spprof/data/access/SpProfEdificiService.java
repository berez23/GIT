package it.webred.ct.service.spprof.data.access;

import java.util.List;

import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfDAOException;
import it.webred.ct.service.spprof.data.model.SSpCedificato;
import it.webred.ct.service.spprof.data.model.SSpDestUrb;
import it.webred.ct.service.spprof.data.model.SSpEdificio;
import it.webred.ct.service.spprof.data.model.SSpEdificioMinore;
import it.webred.ct.service.spprof.data.model.SSpTipologiaEdi;
import it.webred.ct.service.spprof.data.model.SSpTipologiaEdiMin;
import it.webred.ct.service.spprof.data.model.SSpUiu;
import it.webred.ct.service.spprof.data.model.SSpUnitaVol;

import javax.ejb.Remote;

@Remote
public interface SpProfEdificiService {

	public List<SSpTipologiaEdi> getTipologiaEdifici(SpProfDTO dto)
			throws SpProfDAOException;

	public List<SSpTipologiaEdiMin> getTipologiaEdificiMinori(SpProfDTO dto)
			throws SpProfDAOException;

	public SSpTipologiaEdi getTipologiaEdificiById(SpProfDTO dto)
			throws SpProfDAOException;

	public SSpTipologiaEdiMin getTipologiaEdificiMinoriById(SpProfDTO dto)
			throws SpProfDAOException;

	public SSpCedificato getEdificatoById(SpProfDTO dto)
			throws SpProfDAOException;

	public List<SSpEdificio> getEdificiByintervento(SpProfDTO dto)
			throws SpProfDAOException;

	public List<SSpEdificioMinore> getEdificiMinoriByintervento(SpProfDTO dto)
			throws SpProfDAOException;

	public Long saveEdificato(SpProfDTO dto) throws SpProfDAOException;

	public void saveEdificio(SpProfDTO dto) throws SpProfDAOException;

	public void saveEdificioMinore(SpProfDTO dto) throws SpProfDAOException;

	public void updateEdificato(SpProfDTO dto) throws SpProfDAOException;

	public void updateEdificio(SpProfDTO dto) throws SpProfDAOException;

	public void updateEdificioMinore(SpProfDTO dto) throws SpProfDAOException;

	public void deleteEdificato(SpProfDTO dto) throws SpProfDAOException;

	public void deleteEdificio(SpProfDTO dto) throws SpProfDAOException;

	public void deleteEdificioMinore(SpProfDTO dto) throws SpProfDAOException;

	public List<SSpDestUrb> getDestinazioneUso(SpProfDTO dto)
			throws SpProfDAOException;

	public SSpDestUrb getDestinazioneUsoById(SpProfDTO dto)
			throws SpProfDAOException;

	public void saveUiu(SpProfDTO dto) throws SpProfDAOException;

	public void saveUnitaVol(SpProfDTO dto) throws SpProfDAOException;

	public void updateUiu(SpProfDTO dto) throws SpProfDAOException;

	public void updateUnitaVol(SpProfDTO dto) throws SpProfDAOException;

	public void deleteUiu(SpProfDTO dto) throws SpProfDAOException;

	public void deleteUnitaVol(SpProfDTO dto) throws SpProfDAOException;

	public List<SSpUiu> getUiuByEdificio(SpProfDTO dto)
			throws SpProfDAOException;

	public List<SSpUiu> getUiuByUnitaVol(SpProfDTO dto)
			throws SpProfDAOException;

	public List<SSpUnitaVol> getUnitaVolByEdificio(SpProfDTO dto)
			throws SpProfDAOException;

}
