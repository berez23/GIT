package it.webred.ct.service.spprof.data.access.dao.qualifica;

import java.util.List;

import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfDAOException;
import it.webred.ct.service.spprof.data.model.SSpQualifica;

public interface SpProfQualificaDAO {
	
	public SSpQualifica getQualifica(SpProfDTO qualDTO) throws SpProfDAOException;
	
	public List<SSpQualifica> getQualificaList(SpProfDTO qualDTO) throws SpProfDAOException;
}
