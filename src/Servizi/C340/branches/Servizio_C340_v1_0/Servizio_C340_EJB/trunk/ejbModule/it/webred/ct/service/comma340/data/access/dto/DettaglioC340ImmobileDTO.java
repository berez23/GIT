package it.webred.ct.service.comma340.data.access.dto;

import java.io.Serializable;
import java.util.List;

import it.webred.ct.data.access.basic.catasto.dto.*;
import it.webred.ct.data.access.basic.tarsu.dto.InformativaTarsuDTO;
import it.webred.ct.data.model.docfa.DocfaDatiCensuari;

public class DettaglioC340ImmobileDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;	
	
	private DettaglioCatastaleImmobileDTO catasto;
	
	private List<InformativaTarsuDTO> tarsu;
	
	private List<DocfaDatiCensuari> docfa;
	
	public DettaglioCatastaleImmobileDTO getCatasto() {
		return catasto;
	}

	public void setCatasto(DettaglioCatastaleImmobileDTO catasto) {
		this.catasto = catasto;
	}


	public List<InformativaTarsuDTO> getTarsu() {
		return tarsu;
	}

	public void setTarsu(List<InformativaTarsuDTO> tarsu) {
		this.tarsu = tarsu;
	}

	public List<DocfaDatiCensuari> getDocfa() {
		return docfa;
	}

	public void setDocfa(List<DocfaDatiCensuari> docfa) {
		this.docfa = docfa;
	}
	

}
