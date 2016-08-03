package it.webred.ct.service.ff.data.access.risposte.dao;

import it.webred.ct.service.ff.data.access.dao.FFDAOException;
import it.webred.ct.service.ff.data.access.risposte.dto.RispostaDTO;
import it.webred.ct.service.ff.data.model.FFRisposte;

public interface FFRispostaDAO {
	
	public FFRisposte getRisposta(RispostaDTO rispDTO) throws FFDAOException;
	
	public FFRisposte insertRisposta(RispostaDTO rispDTO)throws FFDAOException;
	
	public FFRisposte getRispostaByIdRichiesta(RispostaDTO rispDTO) throws FFDAOException;

	public void evadiRichiesta(RispostaDTO rispDTO) throws FFDAOException;
}
