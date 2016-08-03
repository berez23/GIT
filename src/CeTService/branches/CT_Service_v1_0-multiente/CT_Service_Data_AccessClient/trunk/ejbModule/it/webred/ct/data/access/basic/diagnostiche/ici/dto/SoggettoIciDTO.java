package it.webred.ct.data.access.basic.diagnostiche.ici.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import it.webred.ct.data.model.diagnostiche.*;
import it.webred.ct.data.model.ici.SitTIciSogg;

public class SoggettoIciDTO implements Serializable{
	
	private String titolo;
	private SitTIciSogg sitTIciSogg;
	
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public SitTIciSogg getSitTIciSogg() {
		return sitTIciSogg;
	}
	public void setSitTIciSogg(SitTIciSogg sitTIciSogg) {
		this.sitTIciSogg = sitTIciSogg;
	}
	
}
