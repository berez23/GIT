package it.webred.cs.jsf.bean;

import it.webred.cs.data.model.CsCCategoriaSociale;
import it.webred.cs.data.model.CsOSettore;

public class CasiCatSocialeBean {
	
	private CsCCategoriaSociale catSociale;
	private CsOSettore settore;
	private Integer casi;
	private Integer casiInCarico;
	private Integer casiChiusi;
	
	public CsCCategoriaSociale getCatSociale() {
		return catSociale;
	}
	public void setCatSociale(CsCCategoriaSociale catSociale) {
		this.catSociale = catSociale;
	}
	public Integer getCasi() {
		return casi;
	}
	public void setCasi(Integer casi) {
		this.casi = casi;
	}
	public Integer getCasiInCarico() {
		return casiInCarico;
	}
	public void setCasiInCarico(Integer casiInCarico) {
		this.casiInCarico = casiInCarico;
	}
	public Integer getCasiChiusi() {
		return casiChiusi;
	}
	public void setCasiChiusi(Integer casiChiusi) {
		this.casiChiusi = casiChiusi;
	}
	public CsOSettore getSettore() {
		return settore;
	}
	public void setSettore(CsOSettore settore) {
		this.settore = settore;
	}
}
