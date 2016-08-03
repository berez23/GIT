package it.webred.ct.service.gestprep.data.access;

import it.webred.ct.service.gestprep.data.access.dao.qualifica.GestQualificaDAO;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepQualifica;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class GestQualificaServiceBean
 */
@Stateless
public class GestQualificaServiceBean implements it.webred.ct.service.gestprep.data.access.GestQualificaService {

	@Autowired
	private GestQualificaDAO qualificaDAO;
	
	public List<GestPrepQualifica> getQualificaList(GestPrepDTO qualDTO) {
		try {
			return qualificaDAO.getQualificaList(qualDTO);
		}
		catch(Throwable t) {
			throw new  GestPrepServiceException(t);
		}
		
	}

}
