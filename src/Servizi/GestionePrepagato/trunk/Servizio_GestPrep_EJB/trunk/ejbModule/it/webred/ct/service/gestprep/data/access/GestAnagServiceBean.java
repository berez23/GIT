package it.webred.ct.service.gestprep.data.access;

import java.util.List;

import it.webred.ct.service.gestprep.data.access.dao.anag.GestAnagDAO;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.access.dto.SoggettoSearchCriteria;
import it.webred.ct.service.gestprep.data.model.GestPrepSoggetti;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class GestAnagServiceBean
 */
@Stateless
public class GestAnagServiceBean extends GestPrepBaseServiceBean implements GestAnagService {

	@Autowired
	private GestAnagDAO anagDAO;
	
	public Long createAnag(GestPrepDTO soggetto) {
		try {			
			return anagDAO.createAnag(soggetto);
		}
		catch(Throwable t) {
			throw new GestPrepServiceException();
		}
	}

	public void deleteAnag(GestPrepDTO soggetto) {
		try {
			anagDAO.deleteAnag(soggetto);
		}
		catch(Throwable t) {
			throw new GestPrepServiceException();
		}		
	}

	public boolean updateAnag(GestPrepDTO soggetto) {
		try {
			return anagDAO.updateAnag(soggetto);
		}
		catch(Throwable t) {
			throw new GestPrepServiceException();
		}		
	}

   public List<GestPrepSoggetti> getSoggettiList(SoggettoSearchCriteria criteria, int startm, int numberRecord) {
	   try {
		   return anagDAO.getSoggettiList(criteria, startm, numberRecord);
	   }
	   catch(Throwable t) {
			throw new GestPrepServiceException();
		}
   }
   
   public Long getSoggettiCount(SoggettoSearchCriteria criteria) {
	   try {
		   return anagDAO.getRecordCount(criteria);
	   }
	   catch(Throwable t) {
			throw new GestPrepServiceException();
		}
   
   }

}
