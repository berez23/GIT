package it.webred.ct.service.spprof.data.access.dao.progetto;

import it.webred.ct.service.spprof.data.access.dto.ProgettoShapeDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfDAOException;

public interface SpProfProgettoDAO {
	
	public void saveProgetto(ProgettoShapeDTO dto) throws SpProfDAOException;
	
	public void deleteProgettoByIntervento(Long idIntervento) throws SpProfDAOException;
}
