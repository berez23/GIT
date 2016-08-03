package it.webred.cs.csa.web.manbean.scheda.operatori;

import it.webred.cs.jsf.bean.ValiditaCompBaseBean;

public class OperatoriComp extends ValiditaCompBaseBean {
	
	private String settore;
	private Boolean responsabile;
	
	public String getSettore() {
		return settore;
	}
	public void setSettore(String settore) {
		this.settore = settore;
	}
	public Boolean getResponsabile() {
		return responsabile;
	}
	public void setResponsabile(Boolean responsabile) {
		this.responsabile = responsabile;
	}
	
}
