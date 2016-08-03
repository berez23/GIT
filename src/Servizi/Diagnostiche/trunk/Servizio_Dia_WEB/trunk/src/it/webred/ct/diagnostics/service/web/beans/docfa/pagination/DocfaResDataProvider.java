package it.webred.ct.diagnostics.service.web.beans.docfa.pagination;

import it.webred.ct.diagnostics.service.data.model.DocfaResidenziale;

import java.util.List;

public interface DocfaResDataProvider {
    
	public List<DocfaResidenziale> getVisualizzaByRange(int start, int rowNumber);
	
	public long getVisualizzaCount();
}
