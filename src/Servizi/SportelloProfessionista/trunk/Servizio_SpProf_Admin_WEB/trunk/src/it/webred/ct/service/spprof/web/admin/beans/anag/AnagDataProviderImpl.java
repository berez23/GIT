package it.webred.ct.service.spprof.web.admin.beans.anag;


import it.webred.ct.service.spprof.data.access.SpProfAnagService;
import it.webred.ct.service.spprof.data.access.dto.SoggettoSearchCriteria;
import it.webred.ct.service.spprof.data.model.SSpSoggetto;

import java.util.List;


public class AnagDataProviderImpl implements AnagDataProvider {
	
	private SpProfAnagService anagService;

	public List<SSpSoggetto> getDataByRange(int start, int rowNumber,
			SoggettoSearchCriteria criteria) {
		
		List<SSpSoggetto> result = anagService.getSoggettiList(criteria, start, rowNumber);
		
		return result;
	}

	public long getRecordCount(SoggettoSearchCriteria criteria) {
		Long result = anagService.getSoggettiCount(criteria);
		return result;
	}

	public void resetData() {
		// TODO Auto-generated method stub
		
	}

	public SpProfAnagService getAnagService() {
		return anagService;
	}

	public void setAnagService(SpProfAnagService anagService) {
		this.anagService = anagService;
	}

}
