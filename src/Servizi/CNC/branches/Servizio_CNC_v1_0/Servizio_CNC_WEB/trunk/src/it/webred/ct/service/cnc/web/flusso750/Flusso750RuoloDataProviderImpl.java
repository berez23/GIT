package it.webred.ct.service.cnc.web.flusso750;


import it.webred.ct.data.access.basic.cnc.flusso750.dto.Flusso750SearchCriteria;
import it.webred.ct.data.access.basic.cnc.flusso750.dto.InfoRuoloDTO;


import java.util.List;



public class Flusso750RuoloDataProviderImpl extends Flusso750BaseBean implements Flusso750RuoloDataProvider {

	@Override
	public List<InfoRuoloDTO> getDataByRange(int start, int rowNumber,
			Flusso750SearchCriteria criteria) {
		
		List<InfoRuoloDTO> result = super.getFlusso750Service().getRuoliVistati(criteria,start, rowNumber);
		
		for (InfoRuoloDTO item : result) {
			item.setDescrAmbito(super.getCommonService().getAmbitoDescr(Long.parseLong(item.getInfoRuolo().getChiaveRuolo().getCodAmbito())));
		}
		
		return result;
	}

	@Override
	public long getRecordCount(Flusso750SearchCriteria criteria) {
		Long result = super.getFlusso750Service().getRecordCountRuoliVistati(criteria);
		return result;
	}

	@Override
	public void resetData() {
		
	}



}
