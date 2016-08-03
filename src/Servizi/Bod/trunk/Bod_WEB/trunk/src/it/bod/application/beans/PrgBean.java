package it.bod.application.beans;

import it.bod.application.common.MasterItem;

public class PrgBean extends MasterItem{

	private static final long serialVersionUID = -8736124073973715754L;
	
	private String destFunz = "";
	private String legenda = "";
	private String areaPart = "";
	private String areaPrg = "";
	private String areaInt = "";
	
	public String getDestFunz() {
		return destFunz;
	}
	public void setDestFunz(String destFunz) {
		this.destFunz = destFunz;
	}
	public String getLegenda() {
		return legenda;
	}
	public void setLegenda(String legenda) {
		this.legenda = legenda;
	}
	public String getAreaPart() {
		return areaPart;
	}
	public void setAreaPart(String areaPart) {
		this.areaPart = areaPart;
	}
	public String getAreaPrg() {
		return areaPrg;
	}
	public void setAreaPrg(String areaPrg) {
		this.areaPrg = areaPrg;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getAreaInt() {
		return areaInt;
	}
	public void setAreaInt(String areaInt) {
		this.areaInt = areaInt;
	}
	
	

}
