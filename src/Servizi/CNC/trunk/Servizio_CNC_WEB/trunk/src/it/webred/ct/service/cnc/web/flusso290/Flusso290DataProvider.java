package it.webred.ct.service.cnc.web.flusso290;

import java.util.List;

import it.webred.ct.data.access.basic.cnc.flusso290.dto.AnagraficaImpostaDTO;
import it.webred.ct.data.access.basic.cnc.flusso290.dto.Flusso290SearchCriteria;

public interface Flusso290DataProvider {

    public long getRecordCount(Flusso290SearchCriteria criteria);
    
    public List<AnagraficaImpostaDTO> getDataByRange(int start, int rowNumber, Flusso290SearchCriteria criteria);
    
    public void resetData();
 
}
