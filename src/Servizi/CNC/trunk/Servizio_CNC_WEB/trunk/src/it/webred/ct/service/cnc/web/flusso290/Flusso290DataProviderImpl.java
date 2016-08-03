package it.webred.ct.service.cnc.web.flusso290;

import java.util.List;

import it.webred.ct.data.access.basic.cnc.flusso290.Flusso290Service;
import it.webred.ct.data.access.basic.cnc.flusso290.dto.AnagraficaImpostaDTO;
import it.webred.ct.data.access.basic.cnc.flusso290.dto.Flusso290SearchCriteria;


public class Flusso290DataProviderImpl extends Flusso290BaseBean implements Flusso290DataProvider {

	
	
	@Override
	public List<AnagraficaImpostaDTO> getDataByRange(int start, int rowNumber,
			Flusso290SearchCriteria criteria) {
		
		List<AnagraficaImpostaDTO> result = super.getFlusso290Service().getAnagraficaImpostaSintesi(criteria, start, rowNumber);
		
		//List<AnagraficaImpostaDTO> result = service.getAnagraficaImpostaSintesi(criteria, start, rowNumber);
		return result;
	}

	@Override
	public long getRecordCount(Flusso290SearchCriteria criteria) {
		Long result = super.getFlusso290Service().getRecordCount(criteria);
		return result;
	}

	@Override
	public void resetData() {
		
	}
	

}
