package it.webred.ct.service.geospatial.data.access.dao.layer;

import java.util.List;

import it.webred.ct.data.model.pgt.PgtSqlLayer;
import it.webred.ct.service.geospatial.data.access.dto.SpatialLayerDTO;
import it.webred.ct.service.geospatial.data.access.exception.GeospatialDAOException;

public interface GeospatialAreaLayerDAO {
	
	
	public List<SpatialLayerDTO> getLayer(String ente,String foglio,String particella,String codiFiscLuna,List<PgtSqlLayer> sqlLayers) throws GeospatialDAOException;
	
	public boolean isProgettoOnParticelle(Long idIntervento) throws GeospatialDAOException;
}
