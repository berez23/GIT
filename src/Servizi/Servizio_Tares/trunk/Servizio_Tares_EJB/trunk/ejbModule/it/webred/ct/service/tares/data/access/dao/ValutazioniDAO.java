package it.webred.ct.service.tares.data.access.dao;

import it.webred.ct.service.tares.data.access.dto.ValutazioniSearchCriteria;
import it.webred.ct.service.tares.data.model.SetarElab;

import java.util.List;

public interface ValutazioniDAO {

	public List<SetarElab> getElaborazioni(ValutazioniSearchCriteria criteria);
	public Number getElaborazioniCount(ValutazioniSearchCriteria criteria);
	public SetarElab getElaborazione(SetarElab elaborazione);
	
}
