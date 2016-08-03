package it.webred.cet.service.gestprep.web.beans.deposito;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.webred.cet.service.gestprep.web.GestPrepBaseBean;
import it.webred.ct.service.gestprep.data.access.GestDepositoService;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepDeposito;


public class DepositoBean extends GestPrepBaseBean implements Serializable {
	
	private GestDepositoService depositoService;
	private Long idSoggetto;
	private GestPrepDeposito deposito = new GestPrepDeposito();
	
	public void doSave() {
		try {
			
			deposito.setDataDep(new Date());
			
			GestPrepDTO dto = new GestPrepDTO();
			dto.setEnteId(super.getEnte());
			dto.setUserId(super.getUsername());
			dto.setObj(deposito);
			
			depositoService.createDeposito(dto);
			this.idSoggetto =  deposito.getIdSoggetto();
			
			getDepositoList();
			super.addInfoMessage("deposito.create.ok");
		}
		catch(Throwable t) {
			super.addErrorMessage("deposito.error", null);
		}

	}
	
	public List<GestPrepDeposito> getDepositoList() {
		List<GestPrepDeposito> result = new ArrayList<GestPrepDeposito>();
		
		try {
			GestPrepDTO depDTO = new GestPrepDTO();
			depDTO.setEnteId(super.getEnte());
			depDTO.setObj(idSoggetto);
			System.out.println("ID Sogg ["+idSoggetto+"]");
			return depositoService.getDepositoList(depDTO);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		
		return result;
		
	}
	
	public void setCurrentSoggetto(String idSoggetto) {
		this.idSoggetto = Long.parseLong(idSoggetto);		
	}
	
	
	public GestDepositoService getDepositoService() {
		return depositoService;
	}

	public void setDepositoService(GestDepositoService depositoService) {
		this.depositoService = depositoService;
	}

	public Long getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

	public GestPrepDeposito getDeposito() {
		return deposito;
	}

	public void setDeposito(GestPrepDeposito deposito) {
		this.deposito = deposito;
	}
	
	

}
