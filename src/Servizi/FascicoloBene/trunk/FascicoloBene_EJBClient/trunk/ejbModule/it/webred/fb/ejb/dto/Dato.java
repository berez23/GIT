package it.webred.fb.ejb.dto;

import java.io.Serializable;


public  class Dato implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private DatoArricchito datoArricchito = new DatoArricchito();

	public Dato() {
		// TODO Auto-generated constructor stub
	}

	public DatoArricchito getDatoArricchito() {
		return datoArricchito;
	}


	public void setDatoArricchito(DatoArricchito datoArricchito) {
		this.datoArricchito = datoArricchito;
	}



}
