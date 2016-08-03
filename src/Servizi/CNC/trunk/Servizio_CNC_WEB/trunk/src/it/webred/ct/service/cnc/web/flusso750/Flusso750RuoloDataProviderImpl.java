package it.webred.ct.service.cnc.web.flusso750;


import it.webred.ct.data.access.basic.cnc.flusso750.dto.Flusso750SearchCriteria;
import it.webred.ct.data.access.basic.cnc.flusso750.dto.InfoRuoloDTO;
import it.webred.ct.data.access.basic.cnc.CNCDTO;


import java.util.List;



public class Flusso750RuoloDataProviderImpl extends Flusso750BaseBean implements Flusso750RuoloDataProvider {

	@Override
	public List<InfoRuoloDTO> getDataByRange(int start, int rowNumber,
			Flusso750SearchCriteria criteria) {
		
		List<InfoRuoloDTO> result = super.getFlusso750Service().getRuoliVistati(criteria,start, rowNumber);
		
		CNCDTO dto = new CNCDTO();
		fillEnte(dto);
		
		for (InfoRuoloDTO item : result) {
			dto.setCodAmbito(Long.parseLong(item.getInfoRuolo().getChiaveRuolo().getCodAmbito()));
			item.setDescrAmbito(super.getCommonService().getAmbitoDescr(dto));
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
