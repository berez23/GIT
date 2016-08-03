package it.webred.ct.service.spprof.web.admin.beans.anag;

import it.webred.ct.service.spprof.data.access.SpProfAnagService;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.model.SSpSoggetto;
import it.webred.ct.service.spprof.web.admin.SpProfBaseBean;

import java.io.Serializable;

public class AnagraficaBean extends SpProfBaseBean implements Serializable {
	
	private SpProfAnagService anagService;
	
	private SSpSoggetto soggetto = new SSpSoggetto();
	private Long idSogg;
	
	
	public void doSave() {
		try {
			SpProfDTO dto = new SpProfDTO();
			dto.setEnteId(super.getEnte());
			dto.setUserId(super.getUsername());
			dto.setObj(soggetto);
			
			anagService.createAnag(dto);
			super.addInfoMessage("anag.create.ok");
			
		}catch(Throwable t) {
			super.getLogger().error("", t);
			super.addErrorMessage("anag.create.error", "");
		}
	}
	
	public void doElimina() {
		try {
			SpProfDTO dto = new SpProfDTO();
			dto.setEnteId(super.getEnte());
			dto.setUserId(super.getUsername());
			dto.setObj(idSogg);
			
			anagService.deleteAnag(dto);
			super.addInfoMessage("anag.delete.ok");
			
		}catch(Throwable t) {
			super.getLogger().error("", t);
			super.addErrorMessage("anag.delete.error", "");
		}
	}
	

	public SpProfAnagService getAnagService() {
		return anagService;
	}

	public void setAnagService(SpProfAnagService anagService) {
		this.anagService = anagService;
	}

	public SSpSoggetto getSoggetto() {
		return soggetto;
	}

	public void setSoggetto(SSpSoggetto soggetto) {
		this.soggetto = soggetto;
	}

	public Long getIdSogg() {
		return idSogg;
	}

	public void setIdSogg(Long idSogg) {
		this.idSogg = idSogg;
	}
	
}
