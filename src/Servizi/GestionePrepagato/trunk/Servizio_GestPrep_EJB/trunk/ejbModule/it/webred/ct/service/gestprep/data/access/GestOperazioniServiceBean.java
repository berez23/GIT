package it.webred.ct.service.gestprep.data.access;

import it.webred.ct.service.gestprep.data.access.dao.operazioni.GestOperazioniDAO;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.access.dto.OperazioneDTO;
import it.webred.ct.service.gestprep.data.access.dto.OperazioneSearchCriteria;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class GestOperazioniServiceBean
 */
@Stateless
public class GestOperazioniServiceBean implements GestOperazioniService {

	@Autowired
	private GestOperazioniDAO operazioniDAO;
	
	public List<OperazioneDTO> getListOperazioni(OperazioneSearchCriteria criteria, int startm, int numberRecord) {
		try {
			return operazioniDAO.getOperazioniList(criteria, startm, numberRecord);
		}
		catch(Throwable t) {
			throw new GestPrepServiceException();
	
		}

	}

	public Long getOperazioniCount(OperazioneSearchCriteria criteria) {
		try {
			return operazioniDAO.getRecordCount(criteria);
		}
		catch(Throwable t) {
			throw new GestPrepServiceException();
	
		}
	}
	
	
	public Long saveOperazione(GestPrepDTO operazioneDTO) {
		try {
			return operazioniDAO.createOperazione(operazioneDTO);
		}
		catch(Throwable t) {
			throw new GestPrepServiceException();
	
		}
		
		
	}
}
