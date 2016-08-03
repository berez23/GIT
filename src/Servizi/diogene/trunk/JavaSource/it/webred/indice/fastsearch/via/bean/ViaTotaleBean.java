package it.webred.indice.fastsearch.via.bean;

import it.webred.indice.fastsearch.bean.TotaleBean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class ViaTotaleBean extends TotaleBean {
	
	private static final long serialVersionUID = 1L;
	
	private String fkVia;
	private String sedime;
	private String indirizzo;
	
	private List<List<ViaTotaleBean>> vieAssociate;
	
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
	
	public List<List<ViaTotaleBean>> getVieAssociate() {
		return vieAssociate;
	}
	public void setVieAssociate(List<List<ViaTotaleBean>> vieAssociate) {
		this.vieAssociate = vieAssociate;
	}
	
}
