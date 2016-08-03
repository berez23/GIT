package it.webred.cet.service.gestprep.web.beans.anag;

import it.webred.ct.service.gestprep.data.access.GestAnagService;
import it.webred.ct.service.gestprep.data.access.dto.SoggettoSearchCriteria;
import it.webred.ct.service.gestprep.data.model.GestPrepSoggetti;

import java.util.List;



public class AnagDataProviderImpl implements AnagDataProvider {

	private GestAnagService anagService;

	
	public List<GestPrepSoggetti> getDataByRange(int start, int rowNumber,
			SoggettoSearchCriteria criteria) {

		List<GestPrepSoggetti> result = anagService.getSoggettiList(criteria, start, rowNumber);

		return result;

		
	}

	public long getRecordCount(SoggettoSearchCriteria criteria) {
		Long result = anagService.getSoggettiCount(criteria);
		return result;
	}

	public void resetData() {
		// TODO Auto-generated method stub
		
	}

	public GestAnagService getAnagService() {
		return anagService;
	}

	public void setAnagService(GestAnagService anagService) {
		this.anagService = anagService;
	}
	

	

}
