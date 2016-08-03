package it.webred.cs.csa.ejb.dto;

import it.webred.cs.data.model.CsDPai;
import it.webred.ct.support.datarouter.CeTBaseObject;

@SuppressWarnings("serial")
public class PaiDTO extends CeTBaseObject {

	private CsDPai pai;
	
	private Long idRelazione;
	
	private Long casoId;

	public CsDPai getPai() {
		return pai;
	}

	public void setPai(CsDPai pai) {
		this.pai = pai;
	}

	public Long getIdRelazione() {
		return idRelazione;
	}

	public void setIdRelazione(Long idRelazione) {
		this.idRelazione = idRelazione;
	}

	public Long getCasoId() {
		return casoId;
	}

	public void setCasoId(Long casoId) {
		this.casoId = casoId;
	}
	
}
