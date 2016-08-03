package it.webred.ct.service.geospatial.data.access;
import it.webred.ct.service.geospatial.data.access.dto.GeospatialDTO;
import it.webred.ct.service.geospatial.data.access.exception.GeospatialException;

import javax.ejb.Remote;

@Remote
public interface GeospatialCatalogoService {
	
	public Long saveCatalogo(GeospatialDTO dto) throws GeospatialException;
}
