package it.webred.ct.service.ff.data.access.risposte;

import it.webred.ct.service.ff.data.access.FFServiceException;
import it.webred.ct.service.ff.data.access.risposte.dto.RispostaDTO;
import it.webred.ct.service.ff.data.model.FFRisposte;

import javax.ejb.Remote;

@Remote
public interface GestRispostaService {

	public FFRisposte getRisposta(RispostaDTO rispDTO)  throws FFServiceException;
	
	public FFRisposte insertRisposta(RispostaDTO rispDTO) throws FFServiceException;
	
	public FFRisposte getRispostaByIdRichiesta(RispostaDTO rispDTO) throws FFServiceException;
}
