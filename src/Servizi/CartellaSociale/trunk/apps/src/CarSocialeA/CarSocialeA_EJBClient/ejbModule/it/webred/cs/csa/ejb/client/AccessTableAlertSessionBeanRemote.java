package it.webred.cs.csa.ejb.client;

import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.IterDTO;
import it.webred.cs.data.model.CsAlert;
import it.webred.cs.data.model.CsAlertConfig;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface AccessTableAlertSessionBeanRemote {
	public void addAlert(IterDTO dto) throws Exception;
	
	public List<CsAlert> getNotificas(IterDTO dto)throws Exception;
	
	public CsAlert findAlertById(IterDTO dto)throws Exception;
	
	public List<CsAlert> findAlertByIdCasoTipo(IterDTO dto)throws Exception;

	public void updateLettoById(IterDTO dto)throws Exception;

	public void updatePulisciLista(IterDTO dto)throws Exception;

	public void updateLeggiAll(IterDTO dto)throws Exception;
	
	public void updateAlert(BaseDTO dto);

	public CsAlertConfig getAlertConfigByTipo(BaseDTO dto) throws Exception;
}
