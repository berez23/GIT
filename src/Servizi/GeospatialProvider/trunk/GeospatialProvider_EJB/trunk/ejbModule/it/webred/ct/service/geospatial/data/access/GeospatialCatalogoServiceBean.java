package it.webred.ct.service.geospatial.data.access;

import it.webred.ct.data.model.pgt.PgtSqlLayer;
import it.webred.ct.service.geospatial.data.access.dao.catalogo.GeospatialCatalogoDAO;
import it.webred.ct.service.geospatial.data.access.dto.GeospatialDTO;
import it.webred.ct.service.geospatial.data.access.exception.GeospatialException;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class GeospatialCatalogoServiceBean
 */
@Stateless
public class GeospatialCatalogoServiceBean extends GeospatialBaseServiceBean implements GeospatialCatalogoService {
	
	@Autowired
	private GeospatialCatalogoDAO catalogoDAO;
	
	public Long saveCatalogo(GeospatialDTO dto) throws GeospatialException {
		
		try {
			PgtSqlLayer pgt = (PgtSqlLayer)dto.getObj();
			return catalogoDAO.saveCatalogo(pgt);
		}catch(Throwable t) {
			throw new GeospatialException(t);
		}
	}

    

}
