package it.webred.ct.service.gestprep.data.access;
import java.util.List;

import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.access.dto.SoggettoSearchCriteria;
import it.webred.ct.service.gestprep.data.model.GestPrepSoggetti;

import javax.ejb.Remote;

@Remote
public interface GestAnagService {

	public Long createAnag(GestPrepDTO soggetto);
	
	public boolean updateAnag(GestPrepDTO soggetto);
	
	public void deleteAnag(GestPrepDTO soggetto);
	
	public List<GestPrepSoggetti> getSoggettiList(SoggettoSearchCriteria criteria, int startm, int numberRecord);

    public Long getSoggettiCount(SoggettoSearchCriteria criteria);

}
