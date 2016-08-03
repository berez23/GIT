package it.webred.ct.service.tares.web.bean.paging;

import it.webred.ct.service.tares.data.model.SetarElab;

import java.util.List;

public interface ValutazioniDataProvider {
    
	public Long doCountElaborazioni();

	public List<SetarElab> doCercaElaborazioni(int offset, int limit);

}
