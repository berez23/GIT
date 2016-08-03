package it.webred.ct.service.gestprep.data.access.dao.anag;

import java.util.List;

import it.webred.ct.service.gestprep.data.access.dao.GestPrepDAOException;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.access.dto.SoggettoSearchCriteria;
import it.webred.ct.service.gestprep.data.model.GestPrepSoggetti;

public interface GestAnagDAO {
	
	public Long createAnag(GestPrepDTO soggDTO) throws GestPrepDAOException ;
	
	public boolean updateAnag(GestPrepDTO soggDTO) throws GestPrepDAOException ;
	
	public void deleteAnag(GestPrepDTO soggDTO) throws GestPrepDAOException ;

	
	public Long getRecordCount(SoggettoSearchCriteria criteria) throws GestPrepDAOException;

	public List<GestPrepSoggetti> getSoggettiList(SoggettoSearchCriteria criteria, int startm, int numberRecord) throws GestPrepDAOException ;

}
