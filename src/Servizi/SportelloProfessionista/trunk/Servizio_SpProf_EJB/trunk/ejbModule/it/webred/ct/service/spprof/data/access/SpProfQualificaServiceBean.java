package it.webred.ct.service.spprof.data.access;


import it.webred.ct.service.spprof.data.access.dao.qualifica.SpProfQualificaDAO;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfException;
import it.webred.ct.service.spprof.data.model.SSpQualifica;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class SpProfQualificaServiceBean
 */
@Stateless
public class SpProfQualificaServiceBean implements SpProfQualificaService {

	@Autowired
	private SpProfQualificaDAO qualificaDAO;
	
    
    
	public List<SSpQualifica> getQualificaList(SpProfDTO qualDTO) {
		try {
			return qualificaDAO.getQualificaList(qualDTO);
			
		}catch(Throwable t) {
			throw new SpProfException(t);
		}
	}

}
