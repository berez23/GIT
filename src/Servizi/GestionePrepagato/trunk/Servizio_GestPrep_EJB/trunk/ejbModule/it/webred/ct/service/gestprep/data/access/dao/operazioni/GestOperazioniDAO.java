package it.webred.ct.service.gestprep.data.access.dao.operazioni;

import java.util.List;

import it.webred.ct.service.gestprep.data.access.dao.GestPrepDAOException;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.access.dto.OperazioneDTO;
import it.webred.ct.service.gestprep.data.access.dto.OperazioneSearchCriteria;


public interface GestOperazioniDAO {
	
	public Long createOperazione(GestPrepDTO operazioneDTO) throws GestPrepDAOException ;
	

	public Long getRecordCount(OperazioneSearchCriteria criteria) throws GestPrepDAOException;

	public List<OperazioneDTO> getOperazioniList(OperazioneSearchCriteria criteria, int startm, int numberRecord) throws GestPrepDAOException ;

}
