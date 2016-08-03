package it.webred.ct.service.spprof.web.admin.beans.anag;



import it.webred.ct.service.spprof.data.access.dto.SoggettoSearchCriteria;
import it.webred.ct.service.spprof.data.model.SSpSoggetto;

import java.util.List;

public interface AnagDataProvider {
	
public long getRecordCount(SoggettoSearchCriteria criteria);
    
    public List<SSpSoggetto> getDataByRange(int start, int rowNumber, SoggettoSearchCriteria criteria);
    
    public void resetData();
}
