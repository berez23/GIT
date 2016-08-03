package it.webred.fb.ejb.dto;

import java.math.BigDecimal;


public class DatoSpec extends Dato {

	private static final long serialVersionUID = 1L;
	
	protected String dato = new String();
	
	public String getDato() {
		return dato;
	}

	public void setDato(String dato) {
		this.dato = dato;
	}

	public DatoSpec(String dato) {
		this.dato = dato;
	}

	public DatoSpec(BigDecimal dato) {
		this.dato = dato==null?null:dato.toString();
	}

	public DatoSpec() {
	}
}
