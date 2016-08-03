package it.webred.ct.service.gestprep.data.access;

import java.util.List;

import it.webred.ct.service.gestprep.data.access.dao.deposito.GestDepositoDAO;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepDeposito;


import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Session Bean implementation class GestDepositoServiceBean
 */
@Stateless
public class GestDepositoServiceBean implements GestDepositoService {

	@Autowired
	private GestDepositoDAO depositoDAO;
	
	@EJB
	private GestCreditoService creditoService;
	
	public boolean createDeposito(GestPrepDTO depositoDTO) {
		try {			
			depositoDAO.createDeposito(depositoDTO);
			
			GestPrepDeposito deposito = (GestPrepDeposito) depositoDTO.getObj();
			GestPrepDTO creditoDTO = new GestPrepDTO();
			creditoDTO.setEnteId(depositoDTO.getEnteId());
			creditoDTO.setUserId(depositoDTO.getUserId());
			creditoDTO.setObj(deposito.getIdSoggetto());
			
			creditoService.updateCredito(creditoDTO, deposito.getImportoDep());
			return true;
		}
		catch(Throwable t) {
			throw new GestPrepServiceException();
		}

	}
	
	public List<GestPrepDeposito> getDepositoList(GestPrepDTO depositoDTO) {
		try {
			return depositoDAO.getList(depositoDTO);
		}
		catch(Throwable t) {
			throw new GestPrepServiceException();
		}
	}



}
