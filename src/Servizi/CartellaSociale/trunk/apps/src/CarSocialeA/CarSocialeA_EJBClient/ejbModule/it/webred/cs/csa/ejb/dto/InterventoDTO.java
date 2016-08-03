package it.webred.cs.csa.ejb.dto;

import it.webred.cs.data.model.CsFlgIntervento;
import it.webred.ct.support.datarouter.CeTBaseObject;

@SuppressWarnings("serial")
public class InterventoDTO extends CeTBaseObject {

	private CsFlgIntervento foglio;
	//Id relazione da collegare al foglio amministrativo
	private Long idRelazione;
	
	private Long casoId;
	private Long idTipoIntervento;
	
	private Long idSettore;
	private Long idCatsoc;
	
	public CsFlgIntervento getFoglio() {
		return foglio;
	}
	public void setFoglio(CsFlgIntervento foglio) {
		this.foglio = foglio;
	}
	public Long getCasoId() {
		return casoId;
	}
	public void setCasoId(Long casoId) {
		this.casoId = casoId;
	}
	
	public Long getIdSettore() {
		return idSettore;
	}
	public void setIdSettore(Long idSettore) {
		this.idSettore = idSettore;
	}
	public Long getIdCatsoc() {
		return idCatsoc;
	}
	public void setIdCatsoc(Long idCatsoc) {
		this.idCatsoc = idCatsoc;
	}
	public Long getIdTipoIntervento() {
		return idTipoIntervento;
	}
	public void setIdTipoIntervento(Long idTipoIntervento) {
		this.idTipoIntervento = idTipoIntervento;
	}
	public Long getIdRelazione() {
		return idRelazione;
	}
	public void setIdRelazione(Long idRelazione) {
		this.idRelazione = idRelazione;
	}
	
}
