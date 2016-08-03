package it.webred.ct.service.comma340.web.catasto;

import java.util.List;

import it.webred.ct.data.access.basic.catasto.*;
import it.webred.ct.data.access.basic.catasto.dto.*;
import it.webred.ct.data.model.catasto.Siticivi;
import it.webred.ct.data.model.catasto.Sitidstr;


public class CatastoDataProviderImpl extends CatastoBaseBean implements CatastoDataProvider {

	
	public List<SintesiImmobileDTO> getDataByRange(int start, int rowNumber,
			CatastoSearchCriteria criteria) {
		
		List<SintesiImmobileDTO> result = getCatastoService().getListaImmobili(criteria, start, rowNumber);
		return result;
	}

	public long getRecordCount(CatastoSearchCriteria criteria) {
		Long result = getCatastoService().getCatastoRecordCount(criteria);
		return result;
	}

	public void resetData() {
		
	}
	
}
