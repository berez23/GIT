package it.webred.ct.service.gestprep.data.access.dao.visure;

import java.util.List;

import it.webred.ct.service.gestprep.data.access.dao.GestPrepDAOException;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepAnagVisura;

public interface GestVisureDAO {
	
	public Long createVisura(GestPrepDTO visuraDTO) throws GestPrepDAOException ;
	
	public boolean updateVisura(GestPrepDTO visuraDTO) throws GestPrepDAOException ;
	
	public List<GestPrepAnagVisura> getList(GestPrepDTO visuraDTO) throws GestPrepDAOException ;
	
	public GestPrepAnagVisura getVisura(GestPrepDTO visuraDTO) throws GestPrepDAOException;
}
