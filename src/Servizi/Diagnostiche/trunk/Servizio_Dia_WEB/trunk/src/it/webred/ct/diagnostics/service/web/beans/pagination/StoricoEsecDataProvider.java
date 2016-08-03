package it.webred.ct.diagnostics.service.web.beans.pagination;

import it.webred.ct.diagnostics.service.data.dto.DiaTestataDTO;

import java.util.List;

public interface StoricoEsecDataProvider {
    
	public List<DiaTestataDTO> getVisualizzaByRange(int start, int rowNumber);
	
	public long getVisualizzaCount();
}
