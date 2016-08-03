package it.webred.ct.service.cnc.web.flusso750;

import java.util.List;


import it.webred.ct.data.access.basic.cnc.flusso750.dto.Flusso750SearchCriteria;
import it.webred.ct.data.access.basic.cnc.flusso750.dto.InfoRuoloDTO;



public interface Flusso750RuoloDataProvider {

    public long getRecordCount(Flusso750SearchCriteria criteria);
    
    public List<InfoRuoloDTO> getDataByRange(int start, int rowNumber, Flusso750SearchCriteria criteria);
    
    public void resetData();
 
}
