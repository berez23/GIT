package it.webred.cet.service.ff.web.beans.dettaglio.statoocc.dto;

import it.webred.ct.data.access.basic.anagrafe.dto.ComponenteFamigliaDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.IndirizzoAnagrafeDTO;

import java.io.Serializable;
import java.util.List;

public class DettResidentiDTO implements Serializable {
	
	private IndirizzoAnagrafeDTO indirizzoAng;
	private List<ComponenteFamigliaDTO> componentiFam;
	private int numTotaleResidenti;
	
	
	
	public IndirizzoAnagrafeDTO getIndirizzoAng() {
		return indirizzoAng;
	}
	public void setIndirizzoAng(IndirizzoAnagrafeDTO indirizzoAng) {
		this.indirizzoAng = indirizzoAng;
	}
	public List<ComponenteFamigliaDTO> getComponentiFam() {
		return componentiFam;
	}
	public void setComponentiFam(List<ComponenteFamigliaDTO> componentiFam) {
		this.componentiFam = componentiFam;
	}
	public int getNumTotaleResidenti() {
		return numTotaleResidenti;
	}
	public void setNumTotaleResidenti(int numTotaleResidenti) {
		this.numTotaleResidenti = numTotaleResidenti;
	}
	
	
	

}
