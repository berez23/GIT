package it.webred.ct.service.cnc.web.statoriscossione;

import it.webred.ct.data.access.basic.cnc.statoriscossione.dto.StatoRiscossioneSearchCriteria;
import it.webred.ct.data.model.cnc.statoriscossione.SRiscossioni;

import java.util.List;



public interface StatoRiscossioneDataProvider {

    public long getRecordCount(StatoRiscossioneSearchCriteria criteria);
    
    public List<SRiscossioni> getDataByRange(int start, int rowNumber, StatoRiscossioneSearchCriteria criteria);
    
    public void resetData();
 
}
