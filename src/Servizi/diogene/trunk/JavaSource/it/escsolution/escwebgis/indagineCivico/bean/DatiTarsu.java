package it.escsolution.escwebgis.indagineCivico.bean;

import java.io.Serializable;

public class DatiTarsu implements Serializable {
	private String codFisPIva;
	private double supTot;
	private String denominazione;
	private String supNonPres;
	private String desClsRsu;
	private String desTipOgg;
	
	private static final String DEF_LABEL_SUP_NON_PRES = "NON DICHIARATA";
	
	public DatiTarsu()  {
		codFisPIva="";
		supTot=0;
		denominazione="";
		supNonPres=DEF_LABEL_SUP_NON_PRES;
		desClsRsu="";
		desTipOgg="";
	}
	
	public String getCodFisPIva() {
		return codFisPIva;
	}
	public void setCodFisPIva(String codFisPIva) {
		this.codFisPIva = codFisPIva;
	}
	public double getSupTot() {
		return supTot;
	}
	public void setSupTot(double supTot) {
		this.supTot = supTot;
	}	
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public String getSupNonPres() {
		return supNonPres;
	}
	public void setSupNonPres(String supNonPres) {
		this.supNonPres = supNonPres;
	}
	public String getDesClsRsu() {
		return desClsRsu;
	}
	public void setDesClsRsu(String desClsRsu) {
		this.desClsRsu = desClsRsu;
	}
	public String getDesTipOgg() {
		return desTipOgg;
	}
	public void setDesTipOgg(String desTipOgg) {
		this.desTipOgg = desTipOgg;
	}

}
