package it.webred.ct.service.gestprep.data.access;
import java.util.List;

import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepDeposito;

import javax.ejb.Remote;

@Remote
public interface GestDepositoService {

	public boolean createDeposito(GestPrepDTO depositoDTO) ;
	
	public List<GestPrepDeposito> getDepositoList(GestPrepDTO depositoDTO) ;

}
