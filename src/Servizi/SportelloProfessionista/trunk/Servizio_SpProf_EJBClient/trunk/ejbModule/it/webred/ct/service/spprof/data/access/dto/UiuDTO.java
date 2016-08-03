package it.webred.ct.service.spprof.data.access.dto;

import it.webred.ct.service.spprof.data.model.SSpUiu;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class UiuDTO extends CeTBaseObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SSpUiu uiu;
	
	private String categoriaDescr;

	public SSpUiu getUiu() {
		return uiu;
	}

	public void setUiu(SSpUiu uiu) {
		this.uiu = uiu;
	}

	public String getCategoriaDescr() {
		return categoriaDescr;
	}

	public void setCategoriaDescr(String categoriaDescr) {
		this.categoriaDescr = categoriaDescr;
	}

}
