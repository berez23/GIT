package it.webred.ct.service.spprof.web.admin.beans.qualifica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import it.webred.ct.service.spprof.data.access.SpProfQualificaService;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.model.SSpQualifica;
import it.webred.ct.service.spprof.web.admin.SpProfBaseBean;

public class QualificaBean extends SpProfBaseBean implements Serializable {
	
	private SpProfQualificaService qualService;

	
	public List<SelectItem> getQualificaItemList() {
		List<SelectItem> result = new ArrayList<SelectItem>();
		
		try {
			SpProfDTO dto = new SpProfDTO();
			dto.setEnteId(super.getEnte());
			
			List<SSpQualifica> qualList = qualService.getQualificaList(dto);
			for(SSpQualifica qual : qualList) {
				SelectItem item = new SelectItem();
				item.setValue(qual.getIdQual());
				item.setLabel(qual.getDescr());
				
				result.add(item);
			}
			
		}catch(Throwable t) {
			super.getLogger().error("Eccezione: "+t.getMessage(),t);
		}
		
		return result;
	}


	public SpProfQualificaService getQualService() {
		return qualService;
	}


	public void setQualService(SpProfQualificaService qualService) {
		this.qualService = qualService;
	}
	
	
	
	
}
