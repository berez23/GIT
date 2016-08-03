package it.webred.ct.service.spprof.data.access.dao.anag;


import java.util.List;

import it.webred.ct.service.spprof.data.access.dto.SoggettoSearchCriteria;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfDAOException;
import it.webred.ct.service.spprof.data.model.SSpSoggetto;

public interface SpProfAnagDAO {
	
	public Long createAnag(SpProfDTO soggDTO) throws SpProfDAOException;
		
	public boolean updateAnag(SpProfDTO soggDTO) throws SpProfDAOException;
	
	public void deleteAnag(SpProfDTO soggDTO) throws SpProfDAOException;
	
	
	public Long getRecordCount(SoggettoSearchCriteria criteria) throws SpProfDAOException;
	
	public List<SSpSoggetto> getSoggettiList(SoggettoSearchCriteria criteria, int startm, int numberRecord) throws SpProfDAOException;
}
