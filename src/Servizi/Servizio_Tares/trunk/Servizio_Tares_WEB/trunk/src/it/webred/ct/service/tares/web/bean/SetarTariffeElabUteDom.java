package it.webred.ct.service.tares.web.bean;

import it.webred.ct.service.tares.data.model.SetarTariffa;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;

@Entity
public class SetarTariffeElabUteDom extends SetarTariffa implements Serializable {

	private static final long serialVersionUID = -5961209214275421197L;
	
	private BigDecimal quoteFam = null;
	private BigDecimal superfMedAbit = null;
	
	private BigDecimal coeffKa = null;
	private BigDecimal supTotAbitPerKa = null;
	private BigDecimal quf = null;
	private BigDecimal gettitoQF = null;
	private BigDecimal qufPerKa = null;
	private BigDecimal quotaFissaMedia = null;
	private String selKa = "";
	
	private BigDecimal coeffKb = null;
	private BigDecimal numNucleiFamPerKb = null;
	private BigDecimal quv = null;
	private BigDecimal gettitoQV = null;
	private BigDecimal quvPerKb = null;
	private BigDecimal quotaVariabilePerPersona = null;
	private String selKb = "";
	
	private BigDecimal tariffaTot = null;
	private BigDecimal gettitoTot = null;
	private BigDecimal tariffaMed = null;
	
	
	public SetarTariffeElabUteDom() {

	}//-------------------------------------------------------------------------


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public BigDecimal getQuoteFam() {
		return quoteFam;
	}


	public void setQuoteFam(BigDecimal quoteFam) {
		this.quoteFam = quoteFam;
	}


	public BigDecimal getSuperfMedAbit() {
		return superfMedAbit;
	}


	public void setSuperfMedAbit(BigDecimal superfMedAbit) {
		this.superfMedAbit = superfMedAbit;
	}


	public BigDecimal getCoeffKa() {
		return coeffKa;
	}


	public void setCoeffKa(BigDecimal coeffKa) {
		this.coeffKa = coeffKa;
	}


	public BigDecimal getSupTotAbitPerKa() {
		return supTotAbitPerKa;
	}


	public void setSupTotAbitPerKa(BigDecimal supTotAbitPerKa) {
		this.supTotAbitPerKa = supTotAbitPerKa;
	}


	public BigDecimal getQuf() {
		return quf;
	}


	public void setQuf(BigDecimal quf) {
		this.quf = quf;
	}


	public BigDecimal getGettitoQF() {
		return gettitoQF;
	}


	public void setGettitoQF(BigDecimal gettitoQF) {
		this.gettitoQF = gettitoQF;
	}


	public BigDecimal getQufPerKa() {
		return qufPerKa;
	}


	public void setQufPerKa(BigDecimal qufPerKa) {
		this.qufPerKa = qufPerKa;
	}


	public BigDecimal getCoeffKb() {
		return coeffKb;
	}


	public void setCoeffKb(BigDecimal coeffKb) {
		this.coeffKb = coeffKb;
	}


	public BigDecimal getNumNucleiFamPerKb() {
		return numNucleiFamPerKb;
	}


	public void setNumNucleiFamPerKb(BigDecimal numNucleiFamPerKb) {
		this.numNucleiFamPerKb = numNucleiFamPerKb;
	}


	public BigDecimal getQuv() {
		return quv;
	}


	public void setQuv(BigDecimal quv) {
		this.quv = quv;
	}


	public BigDecimal getGettitoQV() {
		return gettitoQV;
	}


	public void setGettitoQV(BigDecimal gettitoQV) {
		this.gettitoQV = gettitoQV;
	}


	public BigDecimal getQuvPerKb() {
		return quvPerKb;
	}


	public void setQuvPerKb(BigDecimal quvPerKb) {
		this.quvPerKb = quvPerKb;
	}


	public BigDecimal getTariffaTot() {
		return tariffaTot;
	}


	public void setTariffaTot(BigDecimal tariffaTot) {
		this.tariffaTot = tariffaTot;
	}


	public BigDecimal getGettitoTot() {
		return gettitoTot;
	}


	public void setGettitoTot(BigDecimal gettitoTot) {
		this.gettitoTot = gettitoTot;
	}


	public BigDecimal getQuotaFissaMedia() {
		return quotaFissaMedia;
	}


	public void setQuotaFissaMedia(BigDecimal quotaFissaMedia) {
		this.quotaFissaMedia = quotaFissaMedia;
	}


	public BigDecimal getQuotaVariabilePerPersona() {
		return quotaVariabilePerPersona;
	}


	public void setQuotaVariabilePerPersona(BigDecimal quotaVariabilePerPersona) {
		this.quotaVariabilePerPersona = quotaVariabilePerPersona;
	}


	public BigDecimal getTariffaMed() {
		return tariffaMed;
	}


	public void setTariffaMed(BigDecimal tariffaMed) {
		this.tariffaMed = tariffaMed;
	}


	public String getSelKa() {
		return selKa;
	}


	public void setSelKa(String selKa) {
		this.selKa = selKa;
	}


	public String getSelKb() {
		return selKb;
	}


	public void setSelKb(String selKb) {
		this.selKb = selKb;
	}
	
	
	

}
