package it.webred.ct.diagnostics.service.web.beans.pagination;



import it.webred.ct.diagnostics.service.data.dto.DiaCommandDTO;


import java.util.List;

public interface ExecuteDataProvider {
    
	public List<DiaCommandDTO> getEsecuzioneByRange(int start, int rowNumber);
	
	public long getEsecuzioneCount();
}
