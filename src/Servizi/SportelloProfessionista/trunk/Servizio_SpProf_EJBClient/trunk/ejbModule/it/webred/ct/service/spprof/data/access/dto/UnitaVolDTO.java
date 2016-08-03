package it.webred.ct.service.spprof.data.access.dto;

import it.webred.ct.service.spprof.data.model.SSpUnitaVol;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class UnitaVolDTO extends CeTBaseObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SSpUnitaVol unitaVol;
	
	private Integer numUiuCollegate;

	public SSpUnitaVol getUnitaVol() {
		return unitaVol;
	}

	public void setUnitaVol(SSpUnitaVol unitaVol) {
		this.unitaVol = unitaVol;
	}

	public Integer getNumUiuCollegate() {
		return numUiuCollegate;
	}

	public void setNumUiuCollegate(Integer numUiuCollegate) {
		this.numUiuCollegate = numUiuCollegate;
	}

}
