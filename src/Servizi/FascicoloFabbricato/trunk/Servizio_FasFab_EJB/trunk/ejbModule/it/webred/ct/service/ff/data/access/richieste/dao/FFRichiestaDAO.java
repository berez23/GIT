package it.webred.ct.service.ff.data.access.richieste.dao;

import it.webred.ct.service.ff.data.access.dao.FFDAOException;
import it.webred.ct.service.ff.data.access.filtro.dto.FFFiltroRichiesteSearchCriteria;
import it.webred.ct.service.ff.data.access.filtro.dto.FiltroRichiesteDTO;
import it.webred.ct.service.ff.data.access.richieste.dto.RichiestaDTO;
import it.webred.ct.service.ff.data.model.FFRichieste;
import it.webred.ct.service.ff.data.model.FFSoggetti;

import java.util.List;

public interface FFRichiestaDAO {
	
	public Long ricercaSoggetto(RichiestaDTO richiesta) throws FFDAOException ;

	public Long createSoggetto(RichiestaDTO richiesta) throws FFDAOException ;
	
	public FFRichieste createRichiesta(RichiestaDTO richiesta) throws FFDAOException;
	
	public FFRichieste getRichiesta(RichiestaDTO richDTO) throws FFDAOException;
	
	public FFSoggetti getSoggetto(RichiestaDTO richiesta) throws FFDAOException;
	
	public List<FiltroRichiesteDTO> filtraRichieste(FFFiltroRichiesteSearchCriteria filtro,int start, int numberRecord) throws FFDAOException;

	public Long getRecordCount(FFFiltroRichiesteSearchCriteria filtro) throws FFDAOException;
	
	public void updateFilePdfRichiesta(RichiestaDTO richiesta) throws FFDAOException;
}
