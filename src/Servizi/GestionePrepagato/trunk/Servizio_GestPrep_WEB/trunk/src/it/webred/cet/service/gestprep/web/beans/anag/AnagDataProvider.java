package it.webred.cet.service.gestprep.web.beans.anag;

import java.util.List;

import it.webred.ct.service.gestprep.data.access.dto.SoggettoSearchCriteria;
import it.webred.ct.service.gestprep.data.model.GestPrepSoggetti;

public interface AnagDataProvider {

    public long getRecordCount(SoggettoSearchCriteria criteria);
    
    public List<GestPrepSoggetti> getDataByRange(int start, int rowNumber, SoggettoSearchCriteria criteria);
    
    public void resetData();
 
}
