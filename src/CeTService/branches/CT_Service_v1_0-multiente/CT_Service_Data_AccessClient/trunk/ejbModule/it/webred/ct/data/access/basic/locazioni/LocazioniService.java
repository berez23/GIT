package it.webred.ct.data.access.basic.locazioni;

import it.webred.ct.data.access.basic.locazioni.dto.RicercaLocazioniDTO;
import it.webred.ct.data.model.locazioni.LocazioniA;
import it.webred.ct.data.model.locazioni.LocazioniB;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface LocazioniService {
	public List<LocazioniA> getLocazioniByCF(RicercaLocazioniDTO rl) ; 
	public LocazioniB getLocazioneSoggByKey(RicercaLocazioniDTO rl) ; 
	public List<LocazioniA> getLocazioniOggByKey(RicercaLocazioniDTO rl);
	
}
