package it.webred.ct.service.gestprep.data.access;

import java.math.BigDecimal;

import it.webred.ct.service.gestprep.data.access.dao.credito.GestCreditoDAO;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepCredito;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class GestCreditoServiceBean
 */
@Stateless
public class GestCreditoServiceBean implements GestCreditoService {

	@Autowired
	private GestCreditoDAO creditoDAO;
	
	public void updateCredito(GestPrepDTO creditoDTO, BigDecimal importo) {
		try {
			creditoDAO.updateCredito(creditoDTO, importo);
		}
		catch(Throwable t) {
			throw new GestPrepServiceException(t);
		}
	}

	public GestPrepCredito getCredito(GestPrepDTO creditoDTO) {
		try {
			return creditoDAO.getCredito(creditoDTO);
		}
		catch(Throwable t) {
			throw new GestPrepServiceException(t);
		}
		
	}

   

}
