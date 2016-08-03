package it.webred.ct.service.carContrib.web.pages.filtro;

import it.webred.ct.service.carContrib.data.access.cc.dto.FiltroRichiesteSearchCriteria;
import it.webred.ct.service.carContrib.web.beans.FiltroRichieste;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.List;

import javax.faces.model.SelectItem;

public interface FiltroDataProvider {

    public Long getRecordCount(FiltroRichiesteSearchCriteria criteria);
    
    public List<FiltroRichieste> getDataByRange(FiltroRichiesteSearchCriteria criteria,int start, int numberRecord);
    
    public List<SelectItem> getDistinctUserName(CeTBaseObject cetObj);
 
}
