package it.webred.ct.service.ff.data.access.richieste;

import it.webred.ct.service.ff.data.access.FFServiceException;
import it.webred.ct.service.ff.data.access.filtro.dto.FFFiltroRichiesteSearchCriteria;
import it.webred.ct.service.ff.data.access.filtro.dto.FiltroRichiesteDTO;
import it.webred.ct.service.ff.data.access.richieste.dto.RichiestaDTO;
import it.webred.ct.service.ff.data.model.FFRichieste;
import it.webred.ct.service.ff.data.model.FFSoggetti;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface GestRichiestaService {

	public FFRichieste createRichiesta(RichiestaDTO richiesta) throws FFServiceException;
	
	public List<FiltroRichiesteDTO> filtraRichieste(FFFiltroRichiesteSearchCriteria filtro,int startm, int numberRecord);
	
	public Long getRecordCount(FFFiltroRichiesteSearchCriteria filtro);

	public FFSoggetti getSoggetto(RichiestaDTO richDTO);
	
	public FFRichieste getRichiesta(RichiestaDTO richDTO);
	
	public void updateFilePdfRichiesta(RichiestaDTO richiesta) throws FFServiceException;
	
	

}
