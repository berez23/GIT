package it.webred.cet.service.gestprep.web.beans.tariffa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.webred.cet.service.gestprep.web.GestPrepBaseBean;
import it.webred.ct.service.gestprep.data.access.GestTariffeService;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepTariffaVisura;

public class TariffaBean extends GestPrepBaseBean implements Serializable {
	
	private GestTariffeService tariffeService;
	private GestPrepTariffaVisura tariffa = new GestPrepTariffaVisura();
	private Long idVisura;
	
	public GestTariffeService getTariffeService() {
		return tariffeService;
	}

	public void setTariffeService(GestTariffeService tariffeService) {
		this.tariffeService = tariffeService;
	}
	
	public void doSave() {
		try {
			
			//System.out.println("DoSave ID vis ["+tariffa.getIdVisura()+"]");
			tariffa.setDataInizioVal(new Date());
			
			GestPrepDTO dto = new GestPrepDTO();
			dto.setEnteId(super.getEnte());
			dto.setUserId(super.getUsername());
			dto.setObj(tariffa);
			
			tariffeService.createTariffa(dto);
			this.idVisura = tariffa.getIdVisura();
			
			getTariffaList();
			super.addInfoMessage("tariffa.create.ok");
		}
		catch(Throwable t) {
			super.addErrorMessage("tariffa.error", null);
		}
	}

	public GestPrepTariffaVisura getTariffa() {
		return tariffa;
	}

	public void setTariffa(GestPrepTariffaVisura tariffa) {
		this.tariffa = tariffa;
	}

	public Long getIdVisura() {
		//System.out.println("Get ID Vis ["+idVisura+"]");
		return idVisura;
	}

	public void setIdVisura(Long idVisura) {
		//System.out.println("ID vis ["+idVisura+"]");
		this.idVisura = idVisura;
	}
	
	public List<GestPrepTariffaVisura> getTariffaList() {
		List<GestPrepTariffaVisura> result = new ArrayList<GestPrepTariffaVisura>();
		
		try {
			GestPrepDTO visDTO = new GestPrepDTO();
			visDTO.setEnteId(super.getEnte());
			visDTO.setObj(idVisura);
			
			return tariffeService.getTariffeList(visDTO);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		
		return result;
		
	}
	
	public void setCurrentVisura(String idVisura) {
		//System.out.println("Current ID vis ["+idVisura+"]");
		this.idVisura = Long.parseLong(idVisura);		
	}

}
