package it.webred.ct.service.spprof.data.access.dao.qualifica;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import it.webred.ct.service.spprof.data.access.dao.SpProfBaseDAO;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfDAOException;
import it.webred.ct.service.spprof.data.model.SSpQualifica;

public class SpProfQualificaDAOImpl extends SpProfBaseDAO implements SpProfQualificaDAO {

	public SSpQualifica getQualifica(SpProfDTO qualDTO) throws SpProfDAOException {
		SSpQualifica qual;
		
		try {
			qual = (SSpQualifica)manager.find(SSpQualifica.class,( (SSpQualifica) qualDTO.getObj()).getIdQual());
			
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
		
		return qual;
	}

	public List<SSpQualifica> getQualificaList(SpProfDTO qualDTO) throws SpProfDAOException {
		
		List<SSpQualifica> result = new ArrayList<SSpQualifica>();
		
		try {
			Query q = manager.createQuery("SELECT q FROM SSpQualifica q ORDER BY q.descr" );
			result = q.getResultList();
			
		}catch(Throwable t) {
			throw new SpProfDAOException(t);
		}
		
		return result;
	}

}
