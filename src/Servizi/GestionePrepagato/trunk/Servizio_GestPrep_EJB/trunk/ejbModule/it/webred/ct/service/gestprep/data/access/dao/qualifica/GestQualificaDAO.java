package it.webred.ct.service.gestprep.data.access.dao.qualifica;

import java.util.List;

import it.webred.ct.service.gestprep.data.access.dao.GestPrepDAOException;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepQualifica;

public interface GestQualificaDAO {
	
	public GestPrepQualifica getQualifica(GestPrepDTO qualDTO) throws GestPrepDAOException ;
	
	public List<GestPrepQualifica> getQualificaList(GestPrepDTO qualDTO)  throws GestPrepDAOException ;

}
