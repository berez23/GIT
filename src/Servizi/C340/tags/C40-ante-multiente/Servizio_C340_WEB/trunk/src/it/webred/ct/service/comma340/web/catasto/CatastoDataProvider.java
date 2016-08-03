package it.webred.ct.service.comma340.web.catasto;

import java.util.List;

import it.webred.ct.data.access.basic.catasto.*;
import it.webred.ct.data.access.basic.catasto.dto.*;
import it.webred.ct.data.model.catasto.Siticivi;
import it.webred.ct.data.model.catasto.Sitidstr;

public interface CatastoDataProvider {

    public long getRecordCount(CatastoSearchCriteria criteria);
    
    public List<SintesiImmobileDTO> getDataByRange(int start, int rowNumber, CatastoSearchCriteria criteria);
    
    public void resetData();
 
}
