package it.webred.cet.service.gestprep.web.beans.credito;

import it.webred.cet.service.gestprep.web.GestPrepBaseBean;
import it.webred.ct.service.gestprep.data.access.GestCreditoService;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepCredito;

import java.io.Serializable;
import java.math.BigDecimal;

public class CreditoBean extends GestPrepBaseBean implements Serializable {
	
	private GestCreditoService creditoService;
	private GestPrepCredito credito = new GestPrepCredito();

	
	
	public void setCurrentSoggetto(Long idSoggetto) {
		
		try {
			GestPrepDTO dto = new GestPrepDTO();
			dto.setEnteId(super.getEnte());
			dto.setUserId(super.getUsername());
			dto.setObj(idSoggetto);
			
			credito = creditoService.getCredito(dto);
			
		}
		catch(Throwable t) {}
		
	}
	
	public GestCreditoService getCreditoService() {		
		return creditoService;
	}
	
	public void setCreditoService(GestCreditoService creditoService) {
		this.creditoService = creditoService;
	}
	
	
	public GestPrepCredito getCredito() {
		
		return credito;

	}
	

}
