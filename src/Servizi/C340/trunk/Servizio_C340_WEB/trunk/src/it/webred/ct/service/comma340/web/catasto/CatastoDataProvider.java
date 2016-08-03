package it.webred.ct.service.comma340.web.catasto;

import java.util.List;

import it.webred.ct.data.access.basic.catasto.dto.*;

public interface CatastoDataProvider {

    public Long getRecordCount();
    
    public List<SintesiImmobileDTO> getDataByRange(int start, int rowNumber);
    
    public void resetData();
 
}
