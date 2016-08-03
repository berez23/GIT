package it.escsolution.escwebgis.indagineCivico.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Catasto extends EscObject implements Serializable {
	
	private String foglio;
	private String mappale;
	private String sub;
	private String categoria;
	private String classe;
	private double rendita;
	private String renditaStrFmt;
	
	private Titolare titolare;
  	private Indirizzo indirizzo;
  	
  	private String listaCodFiscTitolari;
  	private String listaDenomTitolari;
  	private String listaTipoTitoloTitolari;
  	

	
	public Catasto(){
		foglio ="";
		mappale="";
		sub="";
		categoria="";
		classe="";
		rendita=0;
		titolare = new Titolare();
		indirizzo = new Indirizzo();
   
	}
	public Catasto(Catasto cat ) {
		foglio =cat.getFoglio();
		mappale=cat.getMappale();
		sub=cat.getSub();
		categoria=cat.getCategoria();
		classe = cat.getClasse();
		rendita= cat.getRendita();
		titolare= new Titolare(cat.getTitolare());
		//indirizzo da catasto
	    indirizzo = new Indirizzo(cat.getIndirizzo());
	}
	
	public String getFoglio() {
		return foglio;
	}
	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}
	public String getMappale() {
		return mappale;
	}
	public void setMappale(String mappale) {
		this.mappale = mappale;
	}
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	public Titolare getTitolare() {
		return titolare;
	}
	public void setTitolare(Titolare titolare) {
		this.titolare = titolare;
	}
	public Indirizzo getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(Indirizzo indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getClasse() {
		return classe;
	}
	public void setClasse(String classe) {
		this.classe = classe;
	}
	public double getRendita() {
		return rendita;
	}
	public void setRendita(double rendita) {
		this.rendita = rendita;
	}
	public String getRenditaStrFmt() {
		return renditaStrFmt;
	}
	public void setRenditaStrFmt(String renditaStrFmt) {
		this.renditaStrFmt = renditaStrFmt;
	}
	public String getListaCodFiscTitolari() {
		return listaCodFiscTitolari;
	}
	public void setListaCodFiscTitolari(String listaCodFiscTitolari) {
		this.listaCodFiscTitolari = listaCodFiscTitolari;
	}
	public String getListaDenomTitolari() {
		return listaDenomTitolari;
	}
	public void setListaDenomTitolari(String listaDenomTitolari) {
		this.listaDenomTitolari = listaDenomTitolari;
	}
	public String getListaTipoTitoloTitolari() {
		return listaTipoTitoloTitolari;
	}
	public void setListaTipoTitoloTitolari(String listaTipoTitoloTitolari) {
		this.listaTipoTitoloTitolari = listaTipoTitoloTitolari;
	}
	
	
	
	
	
}
