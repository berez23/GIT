package it.webred.ct.service.tares.web.tarsu;

import javax.persistence.Entity;

@Entity
public class SupTotAbit {
	
	private Boolean sel = false;
	private String numComp = "";
	private String numNuclei = "";
	private String supTarsuTot = "";
	private String supTarsuMed = "";

	public SupTotAbit() {
		
	}//-------------------------------------------------------------------------

	public Boolean getSel() {
		return sel;
	}

	public void setSel(Boolean sel) {
		this.sel = sel;
	}

	public String getNumComp() {
		return numComp;
	}

	public void setNumComp(String numComp) {
		this.numComp = numComp;
	}

	public String getNumNuclei() {
		return numNuclei;
	}

	public void setNumNuclei(String numNuclei) {
		this.numNuclei = numNuclei;
	}

	public String getSupTarsuTot() {
		return supTarsuTot;
	}

	public void setSupTarsuTot(String supTarsuTot) {
		this.supTarsuTot = supTarsuTot;
	}

	public String getSupTarsuMed() {
		return supTarsuMed;
	}

	public void setSupTarsuMed(String supTarsuMed) {
		this.supTarsuMed = supTarsuMed;
	}

	
	
	

}
