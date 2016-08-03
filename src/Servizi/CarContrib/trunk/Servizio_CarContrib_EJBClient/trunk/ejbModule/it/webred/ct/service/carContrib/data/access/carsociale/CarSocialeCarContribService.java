package it.webred.ct.service.carContrib.data.access.carsociale;

import it.webred.ct.data.access.basic.cartellasociale.dto.InterventoDTO;
import it.webred.ct.data.access.basic.cartellasociale.dto.RicercaCarSocDTO;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface CarSocialeCarContribService {

	public List<InterventoDTO> getInterventiByCF(RicercaCarSocDTO rs);

}
