package it.webred.cet.service.ff.web.beans.filtro;

import it.webred.ct.service.ff.data.access.filtro.dto.FFFiltroRichiesteSearchCriteria;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.List;

import javax.faces.model.SelectItem;

public interface FiltroDataProvider {

    public Long getRecordCount(FFFiltroRichiesteSearchCriteria criteria);
    
    public List<FiltroRichieste> getDataByRange(FFFiltroRichiesteSearchCriteria criteria,int start, int numberRecord);
    
    public void resetData();
    
    public List<SelectItem> getDistinctUserName(CeTBaseObject cet);
 
}
