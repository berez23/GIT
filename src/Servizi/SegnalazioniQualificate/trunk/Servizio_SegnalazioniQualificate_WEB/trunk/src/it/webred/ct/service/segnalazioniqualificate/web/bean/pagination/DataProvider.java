package it.webred.ct.service.segnalazioniqualificate.web.bean.pagination;

import it.webred.ct.data.access.basic.segnalazionequalificata.dto.PraticaSegnalazioneDTO;

import java.util.List;

public interface DataProvider {
    
    public Long getReportCount();
    
    public List<PraticaSegnalazioneDTO> getReportByRange(int start, int rowNumber);
    
    public void resetData();
}
