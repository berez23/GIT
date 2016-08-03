package it.webred.ct.service.geospatial.data.access.dao.localizza;


import it.webred.ct.service.geospatial.data.access.dto.ParticellaDTO;
import it.webred.ct.service.geospatial.data.access.exception.GeospatialDAOException;

import java.util.List;


public interface GeospatialLocalizzaDAO {
	
	public List<ParticellaDTO> getListaParticelle(String codNazionale,Long civico, int start, int record) throws GeospatialDAOException;
	
	public Long getListaParticelleCount(String codNazionale,Long civico) throws GeospatialDAOException;
}
