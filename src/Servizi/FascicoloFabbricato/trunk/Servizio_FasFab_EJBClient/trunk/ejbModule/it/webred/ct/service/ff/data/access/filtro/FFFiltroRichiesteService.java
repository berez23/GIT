package it.webred.ct.service.ff.data.access.filtro;

import it.webred.ct.service.ff.data.access.filtro.dto.FFFiltroRichiesteSearchCriteria;
import it.webred.ct.service.ff.data.access.filtro.dto.FiltroRichiesteDTO;
import it.webred.ct.service.ff.data.model.FFSoggetti;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Remote;

@Remote
public interface FFFiltroRichiesteService extends Serializable{
	
	public List<FiltroRichiesteDTO> FiltraRichieste(FFFiltroRichiesteSearchCriteria filtro);
	
}
