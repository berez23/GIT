package it.webred.indice.fastsearch.civico.bean;

import java.util.List;

import it.webred.indice.fastsearch.bean.TotaleBean;

public class CivicoTotaleBean extends TotaleBean {
	
	private static final long serialVersionUID = 1L;
	
	private String fkVia;
	private String sedime;
	private String indirizzo;
	private String civico;
	
	private List<List<CivicoTotaleBean>> civiciAssociati;
	
	public String getFkVia() {
		return fkVia;
	}
	public void setFkVia(String fkVia) {
		this.fkVia = fkVia;
	}
	public String getSedime() {
		return sedime;
	}
	public void setSedime(String sedime) {
		this.sedime = sedime;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getCivico() {
		return civico;
	}
	public void setCivico(String civico) {
		this.civico = civico;
	}
	public List<List<CivicoTotaleBean>> getCiviciAssociati() {
		return civiciAssociati;
	}
	public void setCiviciAssociati(List<List<CivicoTotaleBean>> civiciAssociati) {
		this.civiciAssociati = civiciAssociati;
	}
	
	
}
