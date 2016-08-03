
package it.webred.ct.service.comma340.data.access;

import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.service.comma340.data.access.dto.DettaglioC340ImmobileDTO;


import javax.ejb.Remote;

@Remote
public interface C340CommonService {
	
	public DettaglioC340ImmobileDTO getDettaglioC340Immobile(RicercaOggettoCatDTO ro);
	
}
