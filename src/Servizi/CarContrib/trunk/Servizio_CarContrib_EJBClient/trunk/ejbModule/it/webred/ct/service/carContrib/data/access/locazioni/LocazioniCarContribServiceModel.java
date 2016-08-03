package it.webred.ct.service.carContrib.data.access.locazioni;

import it.webred.ct.data.access.basic.locazioni.dto.RicercaLocazioniDTO;
import it.webred.ct.data.model.locazioni.LocazioniB;
import it.webred.ct.service.carContrib.data.access.common.dto.LocazioniDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.RicercaDTO;

import java.util.List;

public interface LocazioniCarContribServiceModel {
	//per schede ici e tarsu
	public List<LocazioniDTO> searchLocazioniBySogg(RicercaDTO dati);
}
