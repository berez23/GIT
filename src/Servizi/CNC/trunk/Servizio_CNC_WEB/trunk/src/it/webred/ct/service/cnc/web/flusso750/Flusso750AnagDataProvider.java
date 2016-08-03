package it.webred.ct.service.cnc.web.flusso750;

import java.util.List;


import it.webred.ct.data.access.basic.cnc.flusso750.dto.Flusso750SearchCriteria;
import it.webred.ct.data.model.cnc.flusso750.VAnagrafica;


public interface Flusso750AnagDataProvider {

    public long getRecordCount(Flusso750SearchCriteria criteria);
    
    public List<VAnagrafica> getDataByRange(int start, int rowNumber, Flusso750SearchCriteria criteria);
    
    public void resetData();
 
}
