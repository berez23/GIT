package it.webred.ct.diagnostics.service.web.beans.pagination;



import it.webred.ct.diagnostics.service.data.dto.DiaEsecuzioneDTO;

import java.util.List;

public interface ViewDataProvider {
    
	public List<DiaEsecuzioneDTO> getVisualizzaByRange(int start, int rowNumber);
	
	public long getVisualizzaCount();
}
