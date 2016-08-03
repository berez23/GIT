package it.webred.indice.fastsearch.bean;

import java.io.Serializable;

public class IndiceBean implements Serializable {
	
	private String fonteId;
	private String progEs;
	private String id_Dwh;
	private String fonteDescr;
	private String idUnico;
	
	private int numero;
	
	public String getFonteId() {
		return fonteId;
	}
	public void setFonteId(String fonteId) {
		this.fonteId = fonteId;
	}
	public String getProgEs() {
		return progEs;
	}
	public void setProgEs(String progEs) {
		this.progEs = progEs;
	}
	public String getId_Dwh() {
		return id_Dwh;
	}
	public void setId_Dwh(String idDwh) {
		id_Dwh = idDwh;
	}
	public String getFonteDescr() {
		return fonteDescr;
	}
	public void setFonteDescr(String fonteDescr) {
		this.fonteDescr = fonteDescr;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public String getIdUnico() {
		return idUnico;
	}
	public void setIdUnico(String idUnico) {
		this.idUnico = idUnico;
	}
	
	
	

}
