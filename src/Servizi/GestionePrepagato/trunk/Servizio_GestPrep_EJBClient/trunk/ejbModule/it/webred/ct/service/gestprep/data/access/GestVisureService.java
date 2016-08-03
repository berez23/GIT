package it.webred.ct.service.gestprep.data.access;
import java.util.List;

import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepAnagVisura;

import javax.ejb.Remote;

@Remote
public interface GestVisureService {

	public Long createVisura(GestPrepDTO visura);
	
	public boolean updateVisura(GestPrepDTO visura);
	
	public List<GestPrepAnagVisura> getVisureList(GestPrepDTO obj);
	
	public GestPrepAnagVisura getVisura(GestPrepDTO visura);
	
}
