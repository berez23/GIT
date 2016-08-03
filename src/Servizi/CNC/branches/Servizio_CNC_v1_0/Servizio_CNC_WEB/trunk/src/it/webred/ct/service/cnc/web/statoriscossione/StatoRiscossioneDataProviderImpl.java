package it.webred.ct.service.cnc.web.statoriscossione;



import it.webred.ct.data.access.basic.cnc.statoriscossione.StatoRiscossioneService;
import it.webred.ct.data.access.basic.cnc.statoriscossione.dto.StatoRiscossioneSearchCriteria;
import it.webred.ct.data.model.cnc.statoriscossione.SRiscossioni;

import java.util.List;

import javax.ejb.EJB;



public class StatoRiscossioneDataProviderImpl extends StatoRiscossioneBaseBean implements StatoRiscossioneDataProvider {

	
	@Override
	public List<SRiscossioni> getDataByRange(int start, int rowNumber,
			StatoRiscossioneSearchCriteria criteria) {
		
		List<SRiscossioni> result = super.getStatoRiscossioneService().getRiscossioni(criteria, start, rowNumber);
		return result;
	}

	@Override
	public long getRecordCount(StatoRiscossioneSearchCriteria criteria) {
		Long result = super.getStatoRiscossioneService().getRecordCountRiscossioni(criteria);
		return result;
	}

	@Override
	public void resetData() {
		
	}



}
