package it.webred.cet.service.gestprep.web.beans.anag;

import it.webred.cet.service.gestprep.web.GestPrepBaseBean;
import it.webred.ct.service.gestprep.data.access.GestAnagService;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepSoggetti;

import java.io.Serializable;

public class AnagraficaBean extends GestPrepBaseBean  implements Serializable {
	
	private GestPrepSoggetti soggetto = new GestPrepSoggetti();
	private GestAnagService anagService;
	
	public GestPrepSoggetti getSoggetto() {
		return soggetto;
	}

	public void setSoggetto(GestPrepSoggetti soggetto) {
		this.soggetto = soggetto;
	}

	public GestAnagService getAnagService() {
		return anagService;
	}

	public void setAnagService(GestAnagService anagService) {
		this.anagService = anagService;
	}
	
	
	public void doSave() {
		try {
			GestPrepDTO dto = new GestPrepDTO();
			//dto.setEnteId(super.getEnte());
			dto.setEnteId(super.getEnte());
			dto.setUserId(super.getUsername());
			dto.setObj(soggetto);
			
			anagService.createAnag(dto);
			super.addInfoMessage("anag.create.ok");
		}
		catch(Throwable t) {
			t.printStackTrace();
			super.getLogger().error("", t);
			super.addErrorMessage("anag.create.error", "");
		}
	}
}
