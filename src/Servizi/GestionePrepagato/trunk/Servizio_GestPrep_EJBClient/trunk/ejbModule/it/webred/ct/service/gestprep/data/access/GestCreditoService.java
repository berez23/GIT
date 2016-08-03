package it.webred.ct.service.gestprep.data.access;
import java.math.BigDecimal;

import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepCredito;

import javax.ejb.Remote;

@Remote
public interface GestCreditoService {

	public void updateCredito(GestPrepDTO creditoDTO, BigDecimal importo);
	
	public GestPrepCredito getCredito(GestPrepDTO creditoDTO);
	
}
