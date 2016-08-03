package it.bod.application.beans;

import java.util.Date;

import it.bod.application.common.MasterItem;

public class DatiCensuariBean  extends MasterItem{

	private static final long serialVersionUID = 1179792899069804634L;
	
	private Date fornitura = null;
	private String protocollo_registrazione = "";
	
	public Date getFornitura() {
		return fornitura;
	}
	public void setFornitura(Date fornitura) {
		this.fornitura = fornitura;
	}
	public String getProtocollo_registrazione() {
		return protocollo_registrazione;
	}
	public void setProtocollo_registrazione(String protocollo_registrazione) {
		this.protocollo_registrazione = protocollo_registrazione;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	

}
