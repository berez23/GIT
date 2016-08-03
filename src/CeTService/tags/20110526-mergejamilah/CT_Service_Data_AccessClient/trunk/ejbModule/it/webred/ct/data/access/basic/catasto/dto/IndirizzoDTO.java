package it.webred.ct.data.access.basic.catasto.dto;

import it.webred.ct.data.model.catasto.*;

import java.io.Serializable;

public class IndirizzoDTO implements Serializable{
	
	private String numCivico;
	private String strada;
	
	public String getNumCivico() {
		return numCivico;
	}
	public void setNumCivico(String numCivico) {
		this.numCivico = numCivico;
	}
	public String getStrada() {
		return strada;
	}
	public void setStrada(String strada) {
		this.strada = strada;
	}
	

	


}
