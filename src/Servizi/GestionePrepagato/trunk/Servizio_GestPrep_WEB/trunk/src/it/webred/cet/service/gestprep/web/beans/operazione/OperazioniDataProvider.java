package it.webred.cet.service.gestprep.web.beans.operazione;

import java.util.List;


import it.webred.ct.service.gestprep.data.access.dto.OperazioneDTO;
import it.webred.ct.service.gestprep.data.access.dto.OperazioneSearchCriteria;


public interface OperazioniDataProvider {

    public long getRecordCount(OperazioneSearchCriteria criteria);
    
    public List<OperazioneDTO> getDataByRange(int start, int rowNumber, OperazioneSearchCriteria criteria);
    
    public void resetData();
 
}
