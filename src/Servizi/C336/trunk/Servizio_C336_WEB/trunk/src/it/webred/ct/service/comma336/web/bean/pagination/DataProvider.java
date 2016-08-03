package it.webred.ct.service.comma336.web.bean.pagination;


import it.webred.ct.service.comma336.data.access.dto.C336SearchResultDTO;

import java.util.List;

public interface DataProvider {
    
    public Long getReportCount();
    
    public List<C336SearchResultDTO> getReportByRange(int start, int rowNumber);
    
    public void resetData();
}
