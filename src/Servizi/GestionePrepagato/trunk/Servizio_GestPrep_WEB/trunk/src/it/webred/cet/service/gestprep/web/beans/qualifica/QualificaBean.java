package it.webred.cet.service.gestprep.web.beans.qualifica;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import it.webred.cet.service.gestprep.web.GestPrepBaseBean;
import it.webred.ct.service.gestprep.data.access.GestQualificaService;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepQualifica;
import it.webred.ct.support.datarouter.CeTBaseObject;

public class QualificaBean extends GestPrepBaseBean {
	
	private GestQualificaService qualService;
	
	
	
	public List<SelectItem> getQualificaItemList() {
		List<SelectItem> result = new ArrayList<SelectItem>();
		
		try {
			
			GestPrepDTO dto = new GestPrepDTO();
			//dto.setEnteId(super.getEnte());
			dto.setEnteId("F704");
			
			List<GestPrepQualifica> qualList = qualService.getQualificaList(dto);
			
			for (GestPrepQualifica qual : qualList) {
				SelectItem item = new SelectItem();
				item.setValue(qual.getIdQual());
				item.setLabel(qual.getDescr());
				
				result.add(item);
			}
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		
		return result;
	}

	public GestQualificaService getQualService() {
		return qualService;
	}

	public void setQualService(GestQualificaService qualService) {
		this.qualService = qualService;
	}
	
	

}
