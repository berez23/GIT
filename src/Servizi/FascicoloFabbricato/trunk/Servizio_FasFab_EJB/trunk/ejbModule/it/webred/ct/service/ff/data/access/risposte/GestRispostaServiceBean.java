package it.webred.ct.service.ff.data.access.risposte;

import it.webred.ct.service.ff.data.access.FFServiceBaseBean;
import it.webred.ct.service.ff.data.access.FFServiceException;
import it.webred.ct.service.ff.data.access.risposte.dao.FFRispostaDAO;
import it.webred.ct.service.ff.data.access.risposte.dto.RispostaDTO;
import it.webred.ct.service.ff.data.model.FFRisposte;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

@Stateless
public class GestRispostaServiceBean extends FFServiceBaseBean implements GestRispostaService{

	@Autowired
	private FFRispostaDAO rispostaDAO;
	
	public FFRisposte getRisposta(RispostaDTO rispDTO) throws FFServiceException
	{
		try {
			return rispostaDAO.getRisposta(rispDTO);
		} 
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new FFServiceException(t);	
		}
	}
	
	public FFRisposte insertRisposta(RispostaDTO rispDTO)throws FFServiceException
	{
		try {
			// Aggiorno la data sulla gest Rich.
			rispostaDAO.evadiRichiesta(rispDTO);
			return rispostaDAO.insertRisposta(rispDTO);
		} 
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new FFServiceException(t);	
		}
	}
	
	public FFRisposte getRispostaByIdRichiesta(RispostaDTO rispDTO) throws FFServiceException
	{
		try {
			return rispostaDAO.getRispostaByIdRichiesta(rispDTO);
		} 
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new FFServiceException(t);	
		}
	}
}
