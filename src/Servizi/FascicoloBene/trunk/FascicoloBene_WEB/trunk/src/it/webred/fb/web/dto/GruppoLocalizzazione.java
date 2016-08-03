package it.webred.fb.web.dto;

import it.webred.fb.ejb.dto.locazione.DatiLocalizzazione;

import java.util.List;

public class GruppoLocalizzazione {
	protected String provenienza;
	protected List<DatiLocalizzazione> datiLocs;
	
	public GruppoLocalizzazione(String provenienza, List<DatiLocalizzazione> datiLocs){
		this.provenienza = provenienza;
		this.datiLocs = datiLocs;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public List<DatiLocalizzazione> getDatiLocs() {
		return datiLocs;
	}

	public void setDatiLoc(List<DatiLocalizzazione> datiLocs) {
		this.datiLocs = datiLocs;
	}	
}
