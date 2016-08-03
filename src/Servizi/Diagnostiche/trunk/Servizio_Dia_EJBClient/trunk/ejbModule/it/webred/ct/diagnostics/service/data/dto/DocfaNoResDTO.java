package it.webred.ct.diagnostics.service.data.dto;


import it.webred.ct.diagnostics.service.data.model.DocfaNonResidenziale;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class DocfaNoResDTO extends CeTBaseObject implements Serializable {
	
	private static final long serialVersionUID = 1L; 	
	private DocfaNonResidenziale objDocfa;
	
	public DocfaNoResDTO(DocfaNonResidenziale _objDocfa,String enteId, String userId){
		this.objDocfa = _objDocfa;
				
		super.setEnteId(enteId);
		super.setUserId(userId);
	}

	public DocfaNonResidenziale getObjDocfa() {
		return objDocfa;
	}

	public void setObjDocfa(DocfaNonResidenziale objDocfa) {
		this.objDocfa = objDocfa;
	}
	
	
	
}
