package it.webred.ct.service.geospatial.data.access;

import it.webred.ct.service.geospatial.data.access.dao.localizza.GeospatialLocalizzaDAO;
import it.webred.ct.service.geospatial.data.access.dto.ParticellaDTO;
import it.webred.ct.service.geospatial.data.access.exception.GeospatialException;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class GeospatialLocalizzaServiceBean
 */
@Stateless
public class GeospatialLocalizzaServiceBean extends GeospatialBaseServiceBean
		implements GeospatialLocalizzaService {

	@Autowired
	private GeospatialLocalizzaDAO localizzaDAO;

	public List<ParticellaDTO> getListaParticelle(ParticellaDTO particella)
			throws GeospatialException {

		try {
			return localizzaDAO.getListaParticelle(
					particella.getCodNazionale(), particella.getPkidCivi(),
					particella.getStartRecord(), particella.getNumRecord());
		} catch (Throwable t) {
			throw new GeospatialException(t);
		}

	}

	public Long getListaParticelleCount(ParticellaDTO particella)
			throws GeospatialException {

		try {
			return localizzaDAO.getListaParticelleCount(
					particella.getCodNazionale(), particella.getPkidCivi());
		} catch (Throwable t) {
			throw new GeospatialException(t);
		}

	}

}
