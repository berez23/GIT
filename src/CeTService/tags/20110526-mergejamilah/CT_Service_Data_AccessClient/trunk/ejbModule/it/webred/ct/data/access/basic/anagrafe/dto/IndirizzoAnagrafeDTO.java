package it.webred.ct.data.access.basic.anagrafe.dto;

import it.webred.ct.data.model.anagrafe.SitDCivicoV;
import it.webred.ct.data.model.anagrafe.SitDVia;

import java.io.Serializable;
import java.util.Date;

public class IndirizzoAnagrafeDTO extends AnagrafeBaseDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sedimeVia;
	private String desVia;
	private Date dtIniVal;
	private Date dtFinVal;
	private String civico;
	private String civicoLiv2;
	private String civicoLiv3;
	public String getSedimeVia() {
		return sedimeVia;
	}
	public void setSedimeVia(String sedimeVia) {
		this.sedimeVia = sedimeVia;
	}
	public String getDesVia() {
		return desVia;
	}
	public void setDesVia(String desVia) {
		this.desVia = desVia;
	}
	public Date getDtIniVal() {
		return dtIniVal;
	}
	public void setDtIniVal(Date dtIniVal) {
		this.dtIniVal = dtIniVal;
	}
	public Date getDtFinVal() {
		return dtFinVal;
	}
	public void setDtFinVal(Date dtFinVal) {
		this.dtFinVal = dtFinVal;
	}
	public String getCivico() {
		return civico;
	}
	public void setCivico(String civico) {
		this.civico = civico;
	}
	public String getCivicoLiv2() {
		return civicoLiv2;
	}
	public void setCivicoLiv2(String civicoLiv2) {
		this.civicoLiv2 = civicoLiv2;
	}
	public String getCivicoLiv3() {
		return civicoLiv3;
	}
	public void setCivicoLiv3(String civicoLiv3) {
		this.civicoLiv3 = civicoLiv3;
	}
	public void  valDatiIndirizzo(SitDVia via, SitDCivicoV civ) {
		sedimeVia= via.getViasedime();
		desVia= via.getDescrizione();
		civico = civ.getCivLiv1();
		civicoLiv2 = civ.getCivLiv2();
		civicoLiv3 =civ.getCivLiv3();
	}
	public void  valIndirizzo(SitDVia via) {
		sedimeVia= via.getViasedime();
		desVia= via.getDescrizione();
	
	}
}
