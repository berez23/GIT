package it.webred.ct.service.geospatial.data.access;


import it.webred.ct.service.geospatial.data.access.dto.GeospatialDTO;
import it.webred.ct.service.geospatial.data.access.dto.ParticellaDTO;
import it.webred.ct.service.geospatial.data.access.dto.SpatialLayerDTO;
import it.webred.ct.service.geospatial.data.access.exception.GeospatialDAOException;
import it.webred.ct.service.geospatial.data.access.exception.GeospatialException;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface GeospatialAreaLayerService {
	public List<SpatialLayerDTO> getLayer(ParticellaDTO dto) throws GeospatialException;
	public boolean isProgettoOnParticelle(GeospatialDTO dto) throws GeospatialDAOException;
}
