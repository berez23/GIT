package it.bod.application.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import it.bod.application.common.MasterItem;

public class DocfaValoriBean  extends MasterItem{

	
	
	private static final long serialVersionUID = -5255065677490206696L;
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private String zona = "";
	private String microzona = "";
	private String tipologiaEdilizia = "";
	private String stato = "";
	private BigDecimal valMed=null;
	
	public String getZona() {
		return zona;
	}
	public void setZona(String zona) {
		this.zona = zona;
	}
	public String getMicrozona() {
		return microzona;
	}
	public void setMicrozona(String microzona) {
		this.microzona = microzona;
	}
	public String getTipologiaEdilizia() {
		return tipologiaEdilizia;
	}
	public void setTipologiaEdilizia(String tipologiaEdilizia) {
		this.tipologiaEdilizia = tipologiaEdilizia;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public BigDecimal getValMed() {
		return valMed;
	}
	public void setValMed(BigDecimal valMed) {
		this.valMed = valMed;
	}

	

	
	
	

	

}
