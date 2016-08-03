package it.webred.ct.service.ff.data.access.common.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class RicercaDTO extends CeTBaseObject  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object objEntity;
	private Object objFiltro;
	
	public Object getObjEntity() {
		return objEntity;
	}
	public void setObjEntity(Object objEntity) {
		this.objEntity = objEntity;
	}
	public Object getObjFiltro() {
		return objFiltro;
	}
	public void setObjFiltro(Object objFiltro) {
		this.objFiltro = objFiltro;
	}
	
	

}
