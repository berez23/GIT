package it.webred.ct.service.gestprep.data.access;

import it.webred.ct.service.gestprep.data.access.dao.tariffe.GestTariffeDAO;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepTariffaVisura;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class GestTariffeServiceBean
 */
@Stateless
public class GestTariffeServiceBean implements GestTariffeService {

	@Autowired
	private GestTariffeDAO tariffeDAO;
	
	public Long createTariffa(GestPrepDTO tariffaDTO) {
		try {			
			return tariffeDAO.createTariffa(tariffaDTO);
		}
		catch(Throwable t) {
			throw new GestPrepServiceException();
		}
	}

	public GestPrepTariffaVisura getTariffa(GestPrepDTO tariffaDTO) {
		try {
			return tariffeDAO.getTariffa(tariffaDTO);
		}
		catch(Throwable t) {
			throw new GestPrepServiceException();
		}
	}

	public List<GestPrepTariffaVisura> getTariffeList(GestPrepDTO tariffa) {
		try {
			return tariffeDAO.getList(tariffa);
		}
		catch(Throwable t) {
			throw new GestPrepServiceException();
		}
	}

	public boolean updateTariffa(GestPrepDTO tariffaDTO) {
		try {
			tariffeDAO.updateTariffa(tariffaDTO);
			return true;
		}
		catch(Throwable t) {
			throw new GestPrepServiceException();
		}
	}

  
}
