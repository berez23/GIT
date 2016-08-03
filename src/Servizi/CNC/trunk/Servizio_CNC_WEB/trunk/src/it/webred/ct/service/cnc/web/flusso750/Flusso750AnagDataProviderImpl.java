package it.webred.ct.service.cnc.web.flusso750;


import it.webred.ct.data.access.basic.cnc.flusso750.dto.Flusso750SearchCriteria;
import it.webred.ct.data.model.cnc.flusso750.VAnagrafica;

import java.util.List;



public class Flusso750AnagDataProviderImpl extends Flusso750BaseBean implements Flusso750AnagDataProvider {

	@Override
	public List<VAnagrafica> getDataByRange(int start, int rowNumber,
			Flusso750SearchCriteria criteria) {
		
		List<VAnagrafica> result = super.getFlusso750Service().getAnagraficaRuoliVistati(criteria,start, rowNumber);
		return result;
	}

	@Override
	public long getRecordCount(Flusso750SearchCriteria criteria) {
		Long result = super.getFlusso750Service().getRecordCountAnagraficaRuoliVistati(criteria);
		return result;
	}

	@Override
	public void resetData() {
		
	}



}
