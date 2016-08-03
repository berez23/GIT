package it.webred.cs.csa.ejb.dto;

import java.util.List;

import it.webred.cs.data.model.CsDPai;
import it.webred.cs.data.model.CsDRelazione;
import it.webred.ct.support.datarouter.CeTBaseObject;

@SuppressWarnings("serial")
public class RelazioneDTO extends CeTBaseObject {

	private CsDRelazione relazione;
	
	private List<CsDPai> listaPai;

	public CsDRelazione getRelazione() {
		return relazione;
	}

	public void setRelazione(CsDRelazione relazione) {
		this.relazione = relazione;
	}

	public List<CsDPai> getListaPai() {
		return listaPai;
	}

	public void setListaPai(List<CsDPai> listaPai) {
		this.listaPai = listaPai;
	}


	
}
