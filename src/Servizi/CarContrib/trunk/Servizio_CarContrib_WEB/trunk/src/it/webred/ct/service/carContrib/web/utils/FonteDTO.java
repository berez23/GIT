package it.webred.ct.service.carContrib.web.utils;

import java.io.Serializable;

import com.itextpdf.text.BaseColor;

public class FonteDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String descrizione;
	private boolean abilitata;    //Fonte abilitata per l'ente
	private String strDataAgg;
	private String nota;
	private boolean abilitataCC;  //Fonte abilitata in configurazione per la Cartella Contribuente (parametro: pdf.fonti.default)
	private String coloreJsp;
	
	public boolean isAbilitata() {
		return abilitata;
	}
	public void setAbilitata(boolean abilitata) {
		this.abilitata = abilitata;
	}
	public String getStrDataAgg() {
		return strDataAgg;
	}
	public void setStrDataAgg(String strDataAgg) {
		this.strDataAgg = strDataAgg;
	}
	public String getNota() {
		return nota;
	}
	public void setNota(String nota) {
		this.nota = nota;
	}
	public boolean isAbilitataCC() {
		return abilitataCC;
	}
	public void setAbilitataCC(boolean abilitataCC) {
		this.abilitataCC = abilitataCC;
	}
	public boolean mostraInPdf() {
		return abilitata && abilitataCC;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getColoreJsp() {
		coloreJsp = this.abilitataCC ? "blackColor" : "greyColor";
		return coloreJsp;
	}
	
	
}
