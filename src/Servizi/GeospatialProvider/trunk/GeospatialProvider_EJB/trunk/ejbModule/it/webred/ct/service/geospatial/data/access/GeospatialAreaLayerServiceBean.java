package it.webred.ct.service.geospatial.data.access;

import java.util.ArrayList;
import java.util.List;

import it.webred.ct.data.access.basic.pgt.dto.RicercaPgtDTO;
import it.webred.ct.data.model.pgt.PgtSqlLayer;
import it.webred.ct.service.geospatial.data.access.dao.layer.GeospatialAreaLayerDAO;
import it.webred.ct.service.geospatial.data.access.dto.GeospatialDTO;
import it.webred.ct.service.geospatial.data.access.dto.ParticellaDTO;
import it.webred.ct.service.geospatial.data.access.dto.SpatialLayerDTO;
import it.webred.ct.service.geospatial.data.access.exception.GeospatialDAOException;
import it.webred.ct.service.geospatial.data.access.exception.GeospatialException;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class GeospatialAreaLayerServiceBean
 */
@Stateless
public class GeospatialAreaLayerServiceBean extends GeospatialBaseServiceBean implements GeospatialAreaLayerService {

	@Autowired
	private GeospatialAreaLayerDAO layerDAO;

	public List<SpatialLayerDTO> getLayer(ParticellaDTO dto)
			throws GeospatialException {
		
		List<SpatialLayerDTO> res = new ArrayList<SpatialLayerDTO>();
		
		try {
			logger.info("Recupero sql layers da scaricare");
			RicercaPgtDTO r = new RicercaPgtDTO();
			r.setEnteId(dto.getEnteId());
			r.setUserId(dto.getUserId());
			
			//List<PgtSqlLayer> sqlLayers = super.pgtService.getListaLayersDownloadable(r);
			List<PgtSqlLayer> sqlLayers = (List<PgtSqlLayer>)dto.getLista();
			
			logger.info("Recupero info layers e shape");
			return layerDAO.getLayer(dto.getEnteId(),dto.getFoglio(), dto.getParticella(), dto.getEnteId(), sqlLayers);
			
		}catch(Throwable t) {
			throw new GeospatialException(t);
		}
	}
	
	public boolean isProgettoOnParticelle(GeospatialDTO dto) throws GeospatialDAOException {
	
		try {
			
			logger.info("Verifica intersezione layer progetto/particelle");
			return layerDAO.isProgettoOnParticelle((Long) dto.getObj());
			
		}catch(Throwable t) {
			throw new GeospatialException(t);
		}	
	}
}
