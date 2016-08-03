package it.webred.ct.service.spprof.data.access;


import it.webred.ct.service.spprof.data.access.dao.anag.SpProfAnagDAO;
import it.webred.ct.service.spprof.data.access.dto.SoggettoSearchCriteria;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfException;
import it.webred.ct.service.spprof.data.model.SSpSoggetto;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class SpProfAnagServiceBean
 */
@Stateless
public class SpProfAnagServiceBean extends SpProfBaseServiceBean implements SpProfAnagService {

	@Autowired
	private SpProfAnagDAO anagDAO;
	
	public Long createAnag(SpProfDTO soggetto) {
		try {			
			return anagDAO.createAnag(soggetto);
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}

	public void deleteAnag(SpProfDTO soggetto) {
		try {			
			anagDAO.deleteAnag(soggetto);
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}

	public Long getSoggettiCount(SoggettoSearchCriteria criteria) {
		try {			
			return anagDAO.getRecordCount(criteria);
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}

	public List<SSpSoggetto> getSoggettiList(SoggettoSearchCriteria criteria,
			int startm, int numberRecord) {
		
		try {			
			return anagDAO.getSoggettiList(criteria, startm, numberRecord);
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
		
	}

	public boolean updateAnag(SpProfDTO soggetto) {
		try {			
			return anagDAO.updateAnag(soggetto);
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}

}
