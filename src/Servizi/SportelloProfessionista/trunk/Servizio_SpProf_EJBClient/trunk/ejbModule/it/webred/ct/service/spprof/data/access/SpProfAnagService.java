package it.webred.ct.service.spprof.data.access;

import it.webred.ct.service.spprof.data.access.dto.SoggettoSearchCriteria;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.model.SSpSoggetto;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface SpProfAnagService {

public Long createAnag(SpProfDTO soggetto);
	
	public boolean updateAnag(SpProfDTO soggetto);
	
	public void deleteAnag(SpProfDTO soggetto);
	
	public List<SSpSoggetto> getSoggettiList(SoggettoSearchCriteria criteria, int startm, int numberRecord);

    public Long getSoggettiCount(SoggettoSearchCriteria criteria);
    
}
