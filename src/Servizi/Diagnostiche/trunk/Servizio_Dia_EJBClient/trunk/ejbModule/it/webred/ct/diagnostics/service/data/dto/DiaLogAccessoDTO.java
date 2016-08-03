package it.webred.ct.diagnostics.service.data.dto;

import it.webred.ct.diagnostics.service.data.model.DiaLogAccesso;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class DiaLogAccessoDTO extends CeTBaseObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
		
	private DiaLogAccesso objDiaLogAccessi;
	
	public DiaLogAccessoDTO(DiaLogAccesso objCatalogo,String enteId, String userId) {
		super();
		this.objDiaLogAccessi = objCatalogo;
		super.setEnteId(enteId);
		super.setUserId(userId);
	}


	public DiaLogAccesso getObjDiaLogAccessi() {
		return objDiaLogAccessi;
	}


	public void setObjDiaLogAccessi(DiaLogAccesso objDiaLogAccessi) {
		this.objDiaLogAccessi = objDiaLogAccessi;
	}
	

}
