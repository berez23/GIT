package it.bod.application.beans;

import it.bod.application.common.MasterItem;

public class OperatoreBean extends MasterItem{

	private static final long serialVersionUID = 3948182239475556244L;
	
	private String anno = "";
	private String protocollo = "";
	
	public String getAnno() {
		return anno;
	}
	public void setAnno(String anno) {
		this.anno = anno;
	}
	public String getProtocollo() {
		return protocollo;
	}
	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	

}
