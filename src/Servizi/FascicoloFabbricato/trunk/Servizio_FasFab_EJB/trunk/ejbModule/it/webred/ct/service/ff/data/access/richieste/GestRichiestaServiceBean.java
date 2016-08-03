package it.webred.ct.service.ff.data.access.richieste;

import it.webred.ct.service.ff.data.access.FFServiceBaseBean;
import it.webred.ct.service.ff.data.access.FFServiceException;
import it.webred.ct.service.ff.data.access.dao.FFDAOException;
import it.webred.ct.service.ff.data.access.filtro.dto.FFFiltroRichiesteSearchCriteria;
import it.webred.ct.service.ff.data.access.filtro.dto.FiltroRichiesteDTO;
import it.webred.ct.service.ff.data.access.richieste.dao.FFRichiestaDAO;
import it.webred.ct.service.ff.data.access.richieste.dto.RichiestaDTO;
import it.webred.ct.service.ff.data.model.FFRichieste;
import it.webred.ct.service.ff.data.model.FFSoggetti;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class GestRichiestaServiceBean
 */
@Stateless
public class GestRichiestaServiceBean extends FFServiceBaseBean implements GestRichiestaService {

	@Autowired
	private FFRichiestaDAO richiestaDAO;

	public FFRichieste createRichiesta(RichiestaDTO richiestaDTO) throws FFServiceException {
		System.out.println("Richiesta DAO ["+richiestaDAO+"]");
		//FFRichieste richiesta = richiestaDTO.getRichiesta();
		FFSoggetti soggetto = richiestaDTO.getSoggetto();
		Long soggID = null;
		
		if (soggetto != null) {
			if (soggetto.getNome() != null && !soggetto.getNome().equals("") && 
				soggetto.getCognome() != null && !soggetto.getCognome().equals("")) {
				
				// Soggetto inserito. Verifico se esiste in archivio
				try {
					soggID = richiestaDAO.ricercaSoggetto(richiestaDTO);
				}
				catch(FFDAOException fde) {
					logger.error(fde.getMessage(), fde);
					throw new FFServiceException(fde);
				}
				
				
				// Verifico se esiste il soggetto
				if (soggID == null) {
					try {
						richiestaDAO.createSoggetto(richiestaDTO);
						soggID = richiestaDTO.getSoggetto().getIdSogg();
						logger.debug("New SoggID ["+soggID+"]");
					}
					catch(FFDAOException fde) {
						logger.error(fde.getMessage(), fde);
						throw new FFServiceException(fde);
					}
				}
			}
		}
		
		// 
		if (soggID != null) 
			richiestaDTO.getRichiesta().setIdSoggRic(soggID);		
		else 
			richiestaDTO.getRichiesta().setUserNameRic(richiestaDTO.getUserId());
			
		// Creo il model gestRichiesta
		/*
		FFGestioneRichieste gestRic = new FFGestioneRichieste();
		FFGestioneRichiestePK pk = new FFGestioneRichiestePK();
		
		pk.setDtIniGes(new Date());
		pk.setUserName(richiestaDTO.getUserId());
		
		gestRic.setId(pk);
		
		richiestaDTO.setGestRichiesta(gestRic);
		*/
		
		try {
			/*
			Long idRich = richiestaDAO.createRichiesta(richiestaDTO);
			return idRich;
			*/
			return richiestaDAO.createRichiesta(richiestaDTO);
		}
		catch(FFDAOException fde) {
			logger.error(fde.getMessage(), fde);
			throw new FFServiceException(fde);
		}
		
	}	
	
	public FFRichieste getRichiesta(RichiestaDTO richDTO) throws FFServiceException
	{
		try{	
			return richiestaDAO.getRichiesta(richDTO);
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new FFServiceException(t);	
		}
	}
	
	public FFSoggetti getSoggetto(RichiestaDTO richDTO) throws FFServiceException
	{
		try{	
			return richiestaDAO.getSoggetto(richDTO);
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new FFServiceException(t);	
		}
	}
	
	public List<FiltroRichiesteDTO> filtraRichieste(FFFiltroRichiesteSearchCriteria filtro,int startm, int numberRecord) throws FFServiceException
	{
		try{	
			return richiestaDAO.filtraRichieste(filtro,startm,numberRecord);
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new FFServiceException(t);	
		}
	}
	
	public Long getRecordCount(FFFiltroRichiesteSearchCriteria filtro) throws FFServiceException
	{
		try{	
			return richiestaDAO.getRecordCount(filtro);
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new FFServiceException(t);	
		}
	}
	
	public void updateFilePdfRichiesta(RichiestaDTO richiestaDTO) throws FFServiceException
	{
		try {
			//Long idRich = richiestaDAO.createRichiesta(richiestaDTO).getIdRic();
			richiestaDAO.updateFilePdfRichiesta(richiestaDTO);
		}
		catch(FFDAOException fde) {
			logger.error(fde.getMessage(), fde);
			throw new FFServiceException(fde);
		}
	}
	
}
