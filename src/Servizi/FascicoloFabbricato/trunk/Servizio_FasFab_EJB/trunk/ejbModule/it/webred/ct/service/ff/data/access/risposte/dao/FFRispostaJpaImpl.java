package it.webred.ct.service.ff.data.access.risposte.dao;

import it.webred.ct.service.ff.data.access.dao.FFBaseDAO;
import it.webred.ct.service.ff.data.access.dao.FFDAOException;
import it.webred.ct.service.ff.data.access.risposte.dto.RispostaDTO;
import it.webred.ct.service.ff.data.model.FFRisposte;

import java.util.Date;

import javax.persistence.Query;

public class FFRispostaJpaImpl extends FFBaseDAO implements FFRispostaDAO {

	
	public FFRisposte getRisposta(RispostaDTO rispDTO) throws FFDAOException
	{
		try{	
			return manager.find(FFRisposte.class, rispDTO.getRisposta().getIdRis());
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new FFDAOException(t);	
		}
	}
	
	public FFRisposte insertRisposta(RispostaDTO rispDTO)throws FFDAOException
	{
		try{	
			
			logger.debug("Insert risposta");
			FFRisposte risposta = rispDTO.getRisposta();
			manager.persist(risposta);

			return risposta;
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new FFDAOException(t);	
		}		
	}
	
	public FFRisposte getRispostaByIdRichiesta(RispostaDTO rispDTO) throws FFDAOException
	{
		try{	
			Query q = manager.createNamedQuery("FF.getRispostaByIdRichiesta");
			q.setParameter("idRichiesta", rispDTO.getRisposta().getIdRic());
			
			return (FFRisposte) q.getSingleResult();
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new FFDAOException(t);	
		}
	}

	public void evadiRichiesta(RispostaDTO rispDTO) throws FFDAOException {
		try {
			//FFRisposte risposta = rispDTO.getRisposta();
			//BigDecimal idRich =  risposta.getIdRic();
			/*
			FFGestioneRichiestePK pk = new FFGestioneRichiestePK();
			pk.setIdRic(idRich.longValue());
			pk.setDtIniGes(risposta.);
			pk.setUserName(rispDTO.getUserId());
			*/
			Query q = manager.createNamedQuery("FF.evadiRichiesta");
			q.setParameter("dtFine", new Date());
			q.setParameter("idRic", rispDTO.getRisposta().getIdRic().longValue());
			q.executeUpdate();
			
		}
		catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new FFDAOException(t);	
		}
		
		
	}
}
