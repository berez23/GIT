package it.escsolution.escwebgis.indagineCivico.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;

public class DatiDocfa implements Serializable{
	private String chiaveDocfa;
	private String fornitura;
	private java.sql.Date dataFornitura;
	private String protocollo;
	private String idImmobile;
	
	public DatiDocfa() {
		fornitura="";
		protocollo="";
		idImmobile="";
	}
	
	public DatiDocfa(DatiDocfa datiDocfa) {
		dataFornitura=datiDocfa.getDataFornitura();
		fornitura = datiDocfa.getFornitura();
		protocollo=datiDocfa.getProtocollo();
		idImmobile=datiDocfa.getIdImmobile();
	}
	
	public String getProtocollo() {
		return protocollo;
	}
	public void setProtocollo(String protocollo) {
		this.protocollo = protocollo;
	}
	public String getIdImmobile() {
		return idImmobile;
	}
	public void setIdImmobile(String idImmobile) {
		this.idImmobile = idImmobile;
	}

	public String getFornitura() {
		return fornitura;
	}

	public void setFornitura(String fornitura) {
		this.fornitura = fornitura;
	}

	public java.sql.Date getDataFornitura() {
		return dataFornitura;
	}

	public void setDataFornitura(java.sql.Date dataFornitura) {
		this.dataFornitura = dataFornitura;
	}

	public String getChiaveDocfa() {
		return chiaveDocfa;
	}

	public void setChiaveDocfa(String chiaveDocfa) {
		this.chiaveDocfa = chiaveDocfa;
	}
}
