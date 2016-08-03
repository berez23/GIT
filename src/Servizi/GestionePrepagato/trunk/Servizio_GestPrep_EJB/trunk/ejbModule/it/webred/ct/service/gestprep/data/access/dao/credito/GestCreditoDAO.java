package it.webred.ct.service.gestprep.data.access.dao.credito;

import it.webred.ct.service.gestprep.data.access.dao.GestPrepDAOException;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepCredito;
import it.webred.ct.service.gestprep.data.model.GestPrepDeposito;



import java.math.BigDecimal;
import java.util.List;

public interface GestCreditoDAO {

	public void updateCredito(GestPrepDTO creditoDTO, BigDecimal importo) throws GestPrepDAOException ;
	
 
	public GestPrepCredito getCredito(GestPrepDTO creditoDTO) throws GestPrepDAOException ;


}
