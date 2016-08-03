package it.webred.ct.service.carContrib.data.access.cnc.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class RicercaCncDTO extends CeTBaseObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String codEnteCreditore;
	private String codiceTipoTributo;
	private String codiceTributo;
	private String codFis;
	public String getCodEnteCreditore() {
		return codEnteCreditore;
	}
	public void setCodEnteCreditore(String codEnteCreditore) {
		this.codEnteCreditore = codEnteCreditore;
	}
	public String getCodiceTipoTributo() {
		return codiceTipoTributo;
	}
	public void setCodiceTipoTributo(String codiceTipoTributo) {
		this.codiceTipoTributo = codiceTipoTributo;
	}
	public String getCodiceTributo() {
		return codiceTributo;
	}
	public void setCodiceTributo(String codiceTributo) {
		this.codiceTributo = codiceTributo;
	}
	public String getCodFis() {
		return codFis;
	}
	public void setCodFis(String codFis) {
		this.codFis = codFis;
	}

}
