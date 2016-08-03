package it.webred.fb.ejb.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.List;

public class InfoCarProvenienzaDTO extends CeTBaseObject {

	private String provenienza;
	private List<InfoCaricamentoDTO> lstInfo;
	private boolean aggiornamentiDisponibili;
	
	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public List<InfoCaricamentoDTO> getLstInfo() {
		return lstInfo;
	}

	public void setLstInfo(List<InfoCaricamentoDTO> lstInfo) {
		this.lstInfo = lstInfo;
	}

	public boolean isAggiornamentiDisponibili() {
	
		aggiornamentiDisponibili=false;
		int i = 0;
		while(i<lstInfo.size() && !aggiornamentiDisponibili){
			
			InfoCaricamentoDTO dto = lstInfo.get(i);
			aggiornamentiDisponibili = dto.getNumModificati().intValue()>0 || dto.getNumNuovi().intValue()>0 || dto.getNumRimossi().intValue()>0;
			
			i++;
		}
		
		return aggiornamentiDisponibili;
	}

	
	
}
