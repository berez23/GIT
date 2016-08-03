package it.webred.ct.service.gestprep.data.access;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.access.dto.OperazioneDTO;
import it.webred.ct.service.gestprep.data.access.dto.OperazioneSearchCriteria;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface GestOperazioniService {

	public List<OperazioneDTO> getListOperazioni(OperazioneSearchCriteria criteria, int startm, int numberRecord);
	
	public Long getOperazioniCount(OperazioneSearchCriteria criteria);
	
	public Long saveOperazione(GestPrepDTO operazioneDTO) ;

	
}
