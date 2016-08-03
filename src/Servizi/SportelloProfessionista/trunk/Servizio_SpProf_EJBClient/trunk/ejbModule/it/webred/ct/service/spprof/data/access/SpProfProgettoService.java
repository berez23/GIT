package it.webred.ct.service.spprof.data.access;


import it.webred.ct.service.spprof.data.access.dto.ProgettoShapeDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfException;

import javax.ejb.Remote;

@Remote
public interface SpProfProgettoService {

	public void saveProgetto(ProgettoShapeDTO dto) throws SpProfException;
	
}
