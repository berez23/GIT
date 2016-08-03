package it.webred.cet.service.gestprep.web.beans.operazione;

import java.util.Date;

import it.webred.cet.service.gestprep.web.GestPrepBaseBean;
import it.webred.ct.service.gestprep.data.access.GestOperazioniService;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.access.dto.OperazioneDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepOperazVisure;

public class OperazioneBean extends GestPrepBaseBean {
	
	private GestPrepOperazVisure operazione = new GestPrepOperazVisure();
	
	private GestOperazioniService operazioneService ;
	
	
	public void doSave() {
		try {
			GestPrepDTO dto = new GestPrepDTO();
			dto.setEnteId(super.getEnte());
			dto.setObj(operazione);
			operazione.setDataOperaz(new Date());
			operazioneService.saveOperazione(dto);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}


	public GestPrepOperazVisure getOperazione() {
		return operazione;
	}


	public void setOperazione(GestPrepOperazVisure operazione) {
		this.operazione = operazione;
	}


	public GestOperazioniService getOperazioneService() {
		return operazioneService;
	}


	public void setOperazioneService(GestOperazioniService operazioneService) {
		this.operazioneService = operazioneService;
	}
	
	
	

}
