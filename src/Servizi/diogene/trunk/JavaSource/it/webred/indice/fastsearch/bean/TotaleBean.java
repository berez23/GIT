package it.webred.indice.fastsearch.bean;

import java.io.Serializable;

public class TotaleBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String idDwh;
	private String fkEnteSorg;
	private String progEs;
	
	private String descrFonte;
	private String descrTipoInfo;
	
	public String getIdDwh() {
		return idDwh;
	}
	public void setIdDwh(String idDwh) {
		this.idDwh = idDwh;
	}
	public String getFkEnteSorg() {
		return fkEnteSorg;
	}
	public void setFkEnteSorg(String fkEnteSorg) {
		this.fkEnteSorg = fkEnteSorg;
	}
	public String getProgEs() {
		return progEs;
	}
	public void setProgEs(String progEs) {
		this.progEs = progEs;
	}
	public String getDescrFonte() {
		return descrFonte;
	}
	public void setDescrFonte(String descrFonte) {
		this.descrFonte = descrFonte;
	}
	public String getDescrTipoInfo() {
		return descrTipoInfo;
	}
	public void setDescrTipoInfo(String descrTipoInfo) {
		this.descrTipoInfo = descrTipoInfo;
	}
	
}
