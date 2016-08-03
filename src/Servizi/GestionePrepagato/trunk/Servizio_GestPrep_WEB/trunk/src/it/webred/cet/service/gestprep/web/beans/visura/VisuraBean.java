package it.webred.cet.service.gestprep.web.beans.visura;

import java.util.ArrayList;
import java.util.List;

import it.webred.cet.service.gestprep.web.GestPrepBaseBean;
import it.webred.ct.service.gestprep.data.access.GestAnagService;
import it.webred.ct.service.gestprep.data.access.GestVisureService;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepAnagVisura;

public class VisuraBean extends GestPrepBaseBean {

	private String namePat = new String();
	private GestPrepAnagVisura visura = new GestPrepAnagVisura();
	
	private GestVisureService visureService;
	
	public List<GestPrepAnagVisura> getVisuaraList() {
		List<GestPrepAnagVisura> result = new ArrayList<GestPrepAnagVisura>();
		
		try {
			GestPrepDTO visDTO = new GestPrepDTO();
			visDTO.setEnteId(super.getEnte());
			visDTO.setObj(namePat);
			
			return visureService.getVisureList(visDTO);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		
		return result;
	}
	
	

	public String getNamePat() {
		return namePat;
	}



	public void setNamePat(String namePat) {
		this.namePat = namePat;
	}



	public GestVisureService getVisureService() {
		return visureService;
	}

	public void setVisureService(GestVisureService visureService) {
		this.visureService = visureService;
	}




	
	public GestPrepAnagVisura getVisura() {
		return visura;
	}



	public void setVisura(GestPrepAnagVisura visura) {
		this.visura = visura;
	}


	public void setCurrentVisura(String idVisura) {
		GestPrepDTO dto = new GestPrepDTO();
		dto.setEnteId(super.getEnte());
		dto.setUserId(super.getUsername());
		
		dto.setObj(idVisura);
		System.out.println("ID Visura ["+idVisura+"]");
		
		visura = visureService.getVisura(dto);
	}
	
	public void doSave() {
		try {
			GestPrepDTO dto = new GestPrepDTO();
						
			dto.setEnteId(super.getEnte());
			dto.setUserId(super.getUsername());
			dto.setObj(visura);
			
			visureService.createVisura(dto);
			this.getVisuaraList();
			
			super.addInfoMessage("visura.create.ok");
		}
		catch(Throwable t) {
			t.printStackTrace();
			super.addErrorMessage("visura.error", null);
		}
	}
	
	public void doUpdate() {
		try {
			GestPrepDTO dto = new GestPrepDTO();
						
			dto.setEnteId(super.getEnte());
			dto.setUserId(super.getUsername());
			dto.setObj(visura);
			
			visureService.updateVisura(dto);
			
			this.getVisuaraList();
			super.addInfoMessage("visura.update.ok");
		}
		catch(Throwable t) {
			t.printStackTrace();
			super.addErrorMessage("visura.error", null);
		}
	}	
	
	
}
