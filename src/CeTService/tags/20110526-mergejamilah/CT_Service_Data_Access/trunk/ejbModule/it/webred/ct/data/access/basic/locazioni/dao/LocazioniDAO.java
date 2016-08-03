package it.webred.ct.data.access.basic.locazioni.dao;

import it.webred.ct.data.access.basic.locazioni.LocazioniServiceException;
import it.webred.ct.data.access.basic.locazioni.dto.RicercaLocazioniDTO;
import it.webred.ct.data.model.locazioni.LocazioniA;
import it.webred.ct.data.model.locazioni.LocazioniB;

import java.util.List;

public interface LocazioniDAO {
	public List<LocazioniA> getLocazioniByCF(RicercaLocazioniDTO rl) throws LocazioniServiceException;
	public List<LocazioniA> getLocazioniOggByKey(RicercaLocazioniDTO rl) throws LocazioniServiceException;
	public LocazioniB getLocazioneSoggByKey(RicercaLocazioniDTO rl) throws LocazioniServiceException;
}
