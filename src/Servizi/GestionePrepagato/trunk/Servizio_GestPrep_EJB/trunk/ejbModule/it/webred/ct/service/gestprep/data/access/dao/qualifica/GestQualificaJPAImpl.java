package it.webred.ct.service.gestprep.data.access.dao.qualifica;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import it.webred.ct.service.gestprep.data.access.dao.GestPrepBaseDAO;
import it.webred.ct.service.gestprep.data.access.dao.GestPrepDAOException;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepQualifica;


public class GestQualificaJPAImpl extends GestPrepBaseDAO implements GestQualificaDAO {

	public GestPrepQualifica getQualifica(GestPrepDTO qualDTO) throws GestPrepDAOException {		
		GestPrepQualifica qual;
		
		try {
			qual = (GestPrepQualifica) manager.find(GestPrepQualifica.class, ( (GestPrepQualifica) qualDTO.getObj()).getIdQual());
			return qual;
		}
		catch(Throwable t) {
			throw new GestPrepDAOException(t);
		}
		
	}

	public List<GestPrepQualifica> getQualificaList(GestPrepDTO qualDTO)  throws GestPrepDAOException {
		
		List<GestPrepQualifica> result = new ArrayList<GestPrepQualifica>();
		
		try {
			Query q = manager.createQuery("SELECT q FROM GestPrepQualifica q ORDER BY q.descr" );
			result = q.getResultList();
			return result;
		}
		catch(Throwable t) {
			throw new GestPrepDAOException(t);
		}
	}



}
