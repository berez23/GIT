package it.webred.ct.service.tares.data.access;

import it.webred.ct.service.tares.data.access.dto.DataInDTO;

import it.webred.ct.service.tares.data.model.SetarElab;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface ValutazioniService {

	public List<SetarElab> getElaborazioni(DataInDTO dataIn);
	public Number getElaborazioniCount(DataInDTO dataIn);
	public SetarElab getElaborazione(DataInDTO dataIn);
	
}
