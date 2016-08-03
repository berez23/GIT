package it.webred.cet.service.gestprep.web.beans.operazione;

import it.webred.ct.service.gestprep.data.access.GestAnagService;
import it.webred.ct.service.gestprep.data.access.GestOperazioniService;
import it.webred.ct.service.gestprep.data.access.dto.OperazioneDTO;
import it.webred.ct.service.gestprep.data.access.dto.OperazioneSearchCriteria;
import it.webred.ct.service.gestprep.data.access.dto.SoggettoSearchCriteria;
import it.webred.ct.service.gestprep.data.model.GestPrepSoggetti;

import java.util.List;



public class OperazioniDataProviderImpl implements OperazioniDataProvider {

	private GestOperazioniService operazioniService;

	
	public List<OperazioneDTO> getDataByRange(int start, int rowNumber,
			OperazioneSearchCriteria criteria) {

		List<OperazioneDTO> result = operazioniService.getListOperazioni(criteria, start, rowNumber);

		return result;
		
	}

	public long getRecordCount(OperazioneSearchCriteria criteria) {
		Long result = operazioniService.getOperazioniCount(criteria);
		return result;
	}

	public void resetData() {
	}

	public GestOperazioniService getOperazioniService() {
		return operazioniService;
	}

	public void setOperazioniService(GestOperazioniService operazioniService) {
		this.operazioniService = operazioniService;
	}

	
	

	

}
