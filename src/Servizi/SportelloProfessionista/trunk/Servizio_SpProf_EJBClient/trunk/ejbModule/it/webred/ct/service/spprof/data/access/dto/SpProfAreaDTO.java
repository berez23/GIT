package it.webred.ct.service.spprof.data.access.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;
import java.util.List;

public class SpProfAreaDTO extends CeTBaseObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Long fkSpIntervento;
	
	private List<?> listaAreaPart;
	
	private List<?> listaAreaFabb;
	
	private List<?> listaAreaLayer;
	
	
	

	public List<?> getListaAreaPart() {
		return listaAreaPart;
	}

	public void setListaAreaPart(List<?> listaAreaPart) {
		this.listaAreaPart = listaAreaPart;
	}

	public List<?> getListaAreaFabb() {
		return listaAreaFabb;
	}

	public void setListaAreaFabb(List<?> listaAreaFabb) {
		this.listaAreaFabb = listaAreaFabb;
	}

	public List<?> getListaAreaLayer() {
		return listaAreaLayer;
	}

	public void setListaAreaLayer(List<?> listaAreaLayer) {
		this.listaAreaLayer = listaAreaLayer;
	}

	public Long getFkSpIntervento() {
		return fkSpIntervento;
	}

	public void setFkSpIntervento(Long fkSpIntervento) {
		this.fkSpIntervento = fkSpIntervento;
	}
	
	

}
