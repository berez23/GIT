package it.webred.ct.service.spprof.data.access.dao.edifici;

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

public interface SpProfEdificiDAO {
	
	public List<SSpTipologiaEdi> getTipologiaEdifici() throws SpProfDAOException;
	public List<SSpTipologiaEdiMin> getTipologiaEdificiMinori() throws SpProfDAOException;
	public SSpTipologiaEdi getTipologiaEdificiById(String string)throws SpProfDAOException;
	public SSpTipologiaEdiMin getTipologiaEdificiMinoriById(String id)throws SpProfDAOException;
	public SSpCedificato getEdificatoById(Long id) throws SpProfDAOException;
	public List<SSpEdificio> getEdificiByintervento(Long intervento) throws SpProfDAOException;
	public List<SSpEdificioMinore> getEdificiMinoriByintervento(Long intervento)throws SpProfDAOException;
	public Long saveEdificato(SpProfDTO dto) throws SpProfDAOException;
	public void saveEdificio(SpProfDTO dto) throws SpProfDAOException;
	public void saveEdificioMinore(SpProfDTO dto) throws SpProfDAOException;
	public void updateEdificato(SpProfDTO dto) throws SpProfDAOException;
	public void updateEdificio(SpProfDTO dto) throws SpProfDAOException;
	public void updateEdificioMinore(SpProfDTO dto) throws SpProfDAOException;
	public void deleteEdificato(Long idEdificato) throws SpProfDAOException;
	public void deleteEdificio(Long idEdificato) throws SpProfDAOException;
	public void deleteEdificioMinore(Long idEdificato) throws SpProfDAOException;
	public List<SSpDestUrb> getDestinazioneUso()throws SpProfDAOException;
	public SSpDestUrb getDestinazioneUsoById(String id)throws SpProfDAOException;
	public void saveUiu(SpProfDTO dto) throws SpProfDAOException;
	public void saveUnitaVol(SpProfDTO dto) throws SpProfDAOException;
	public void updateUiu(SpProfDTO dto) throws SpProfDAOException;
	public void updateUnitaVol(SpProfDTO dto) throws SpProfDAOException;
	public void deleteUiu(Long id) throws SpProfDAOException;
	public void deleteUnitaVol(Long id) throws SpProfDAOException;
	public List<SSpUiu> getUiuByEdificio(Long id) throws SpProfDAOException;
	public List<SSpUiu> getUiuByUnitaVol(Long id) throws SpProfDAOException;
	public List<SSpUnitaVol> getUnitaVolByEdificio(Long id) throws SpProfDAOException;
}
