package it.webred.ct.service.geospatial.data.access.dao.catalogo;

import it.webred.ct.data.model.pgt.PgtSqlLayer;
import it.webred.ct.service.geospatial.data.access.exception.GeospatialDAOException;

public interface GeospatialCatalogoDAO {
	
	public Long saveCatalogo(PgtSqlLayer pgt) throws GeospatialDAOException;
}
