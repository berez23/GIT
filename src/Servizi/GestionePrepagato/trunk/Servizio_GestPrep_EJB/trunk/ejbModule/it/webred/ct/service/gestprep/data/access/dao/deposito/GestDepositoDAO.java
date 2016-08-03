package it.webred.ct.service.gestprep.data.access.dao.deposito;

import it.webred.ct.service.gestprep.data.access.dao.GestPrepDAOException;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepDeposito;



import java.util.List;

public interface GestDepositoDAO {

	public Long createDeposito(GestPrepDTO depositoDTO) throws GestPrepDAOException ;
	

	public List<GestPrepDeposito> getList(GestPrepDTO depositoDTO) throws GestPrepDAOException ;
	

}
