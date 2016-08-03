package it.webred.ct.service.gestprep.data.access;

import java.util.ArrayList;
import java.util.List;

import it.webred.ct.service.gestprep.data.access.dao.GestPrepBaseDAO;
import it.webred.ct.service.gestprep.data.access.dao.visure.GestVisureDAO;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepAnagVisura;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;



/**
 * Session Bean implementation class GestVisureServiceBean
 */
@Stateless
public class GestVisureServiceBean  implements GestVisureService {

	@Autowired
	private GestVisureDAO visureDAO;
	
	public Long createVisura(GestPrepDTO visuraDTO) {
		try {			
			return visureDAO.createVisura(visuraDTO);
		}
		catch(Throwable t) {
			throw new GestPrepServiceException();
		}
	}

	public boolean updateVisura(GestPrepDTO visuraDTO) {
		try {
			return visureDAO.updateVisura(visuraDTO);
		}
		catch(Throwable t) {
			throw new GestPrepServiceException();
		}
	}

	public List<GestPrepAnagVisura> getVisureList(GestPrepDTO obj) {
		
		try {
			return visureDAO.getList(obj);
		}
		catch(Throwable t) {
			throw new GestPrepServiceException();
		}
	}

	public GestPrepAnagVisura getVisura(GestPrepDTO visuraDTO) {
		try {
			return visureDAO.getVisura(visuraDTO);
		}
		catch(Throwable t) {
			throw new GestPrepServiceException();
		}
	}

  

}
