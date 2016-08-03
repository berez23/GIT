package it.webred.ct.service.tares.web.bean;

import it.webred.ct.service.tares.data.model.SetarTariffa;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;

@Entity
public class SetarTariffeElabUteNonDom extends SetarTariffa implements Serializable {

	private static final long serialVersionUID = 4813031828892229755L;
	
	private BigDecimal quoteAtt = null;
	private BigDecimal superfMedLoc = null;
	
	private BigDecimal coeffKc = null;
	private BigDecimal supTotCatPerKc = null;
	private BigDecimal quf = null;
	private BigDecimal gettitoQF = null;
	private BigDecimal qufPerKc = null;
	private String selKc = "";
	
	private BigDecimal coeffKd = null;
	private BigDecimal supTotCatPerKd = null;
	private BigDecimal quv = null;
	private BigDecimal gettitoQV = null;
	private BigDecimal quvPerKd = null;
	private String selKd = "";
	
	private BigDecimal tariffaTot = null;
	private BigDecimal gettitoTot = null;
	
	
	public SetarTariffeElabUteNonDom() {

	}//-------------------------------------------------------------------------


	public BigDecimal getQuoteAtt() {
		return quoteAtt;
	}


	public void setQuoteAtt(BigDecimal quoteAtt) {
		this.quoteAtt = quoteAtt;
	}


	public BigDecimal getSuperfMedLoc() {
		return superfMedLoc;
	}


	public void setSuperfMedLoc(BigDecimal superfMedLoc) {
		this.superfMedLoc = superfMedLoc;
	}


	public BigDecimal getCoeffKc() {
		return coeffKc;
	}


	public void setCoeffKc(BigDecimal coeffKc) {
		this.coeffKc = coeffKc;
	}


	public BigDecimal getSupTotCatPerKc() {
		return supTotCatPerKc;
	}


	public void setSupTotCatPerKc(BigDecimal supTotCatPerKc) {
		this.supTotCatPerKc = supTotCatPerKc;
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


	public BigDecimal getQufPerKc() {
		return qufPerKc;
	}


	public void setQufPerKc(BigDecimal qufPerKc) {
		this.qufPerKc = qufPerKc;
	}


	public BigDecimal getCoeffKd() {
		return coeffKd;
	}


	public void setCoeffKd(BigDecimal coeffKd) {
		this.coeffKd = coeffKd;
	}


	public BigDecimal getSupTotCatPerKd() {
		return supTotCatPerKd;
	}


	public void setSupTotCatPerKd(BigDecimal supTotCatPerKd) {
		this.supTotCatPerKd = supTotCatPerKd;
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


	public BigDecimal getQuvPerKd() {
		return quvPerKd;
	}


	public void setQuvPerKd(BigDecimal quvPerKd) {
		this.quvPerKd = quvPerKd;
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


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getSelKc() {
		return selKc;
	}


	public void setSelKc(String setKc) {
		this.selKc = setKc;
	}


	public String getSelKd() {
		return selKd;
	}


	public void setSelKd(String selKd) {
		this.selKd = selKd;
	}

	
	

}
