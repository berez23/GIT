package it.webred.ct.service.geospatial.data.access;
import it.webred.ct.service.geospatial.data.access.dto.ParticellaDTO;
import it.webred.ct.service.geospatial.data.access.exception.GeospatialException;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface GeospatialLocalizzaService {
	public List<ParticellaDTO> getListaParticelle(ParticellaDTO particella) throws GeospatialException;
	
	public Long getListaParticelleCount(ParticellaDTO particella) throws GeospatialException;
}
