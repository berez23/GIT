package it.webred.ct.diagnostics.service.web.beans.docfa.pagination;

import it.webred.ct.diagnostics.service.data.model.DocfaNonResidenziale;

import java.util.List;

public interface DocfaNoResDataProvider {
    
	public List<DocfaNonResidenziale> getVisualizzaByRange(int start, int rowNumber);
	
	public long getVisualizzaCount();
}
