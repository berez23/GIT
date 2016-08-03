package it.escsolution.escwebgis.fascicoloDoc.bean;

import java.util.List;

import it.webred.ct.data.access.basic.catasto.dto.PlanimetriaComma340DTO;
import it.webred.ct.data.model.docfa.DocfaDatiCensuari;
import it.webred.ct.data.model.docfa.DocfaPlanimetrie;
import it.webred.ct.support.datarouter.CeTBaseObject;

public class PlanimetriaComma340ExtDTO extends CeTBaseObject {
	
	private static final long serialVersionUID = 1L;

	PlanimetriaComma340DTO planimetriaComma340;
	private String categoria;
	
	public PlanimetriaComma340ExtDTO() {
		super();
	}
	
	public PlanimetriaComma340ExtDTO(PlanimetriaComma340DTO planimetriaComma340) {
		super();
		this.planimetriaComma340 = planimetriaComma340;
	}
	
	public PlanimetriaComma340ExtDTO(PlanimetriaComma340DTO planimetriaComma340, String categoria) {
		super();
		this.planimetriaComma340 = planimetriaComma340;
		this.categoria = categoria;
	}
	
	public PlanimetriaComma340DTO getPlanimetriaComma340() {
		return planimetriaComma340;
	}

	public void setPlanimetriaComma340(PlanimetriaComma340DTO planimetriaComma340) {
		this.planimetriaComma340 = planimetriaComma340;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

}
