package it.webred.ct.diagnostics.service.web.beans.pagination;

import it.webred.ct.diagnostics.service.data.model.DiaLogAccesso;

import java.util.List;

public interface LogDataProvider {
    
	public List<DiaLogAccesso> getDettaglioByRange(int start, int rowNumber);
	
	public long getDettaglioCount();
}
