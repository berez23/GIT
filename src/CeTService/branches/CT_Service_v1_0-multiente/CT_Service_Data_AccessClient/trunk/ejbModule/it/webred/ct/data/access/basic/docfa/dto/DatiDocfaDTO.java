package it.webred.ct.data.access.basic.docfa.dto;

import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class DatiDocfaDTO extends CeTBaseObject implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String protocolloReg;
	private String dataRegistrazione;
	private String desTipoOperazione;
	private String indirizzoUiuCompleto;
	public String getProtocolloReg() {
		return protocolloReg;
	}
	public void setProtocolloReg(String protocolloReg) {
		this.protocolloReg = protocolloReg;
	}
	public String getDataRegistrazione() {
		return dataRegistrazione;
	}
	public void setDataRegistrazione(String dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}
	public String getDesTipoOperazione() {
		return desTipoOperazione;
	}
	public void setDesTipoOperazione(String desTipoOperazione) {
		this.desTipoOperazione = desTipoOperazione;
	}
	public String getIndirizzoUiuCompleto() {
		return indirizzoUiuCompleto;
	}
	public void setIndirizzoUiuCompleto(String indirizzoUiuCompleto) {
		this.indirizzoUiuCompleto = indirizzoUiuCompleto;
	}
	
	
}
