package it.webred.gitland.ejb.dto;

import java.io.Serializable;

public class FiltroDTO implements Serializable{

	private static final long serialVersionUID = 6297200877140452216L;
	
	private String attributo = "";
	private String operatore = "";
	private Object valore = null;
	private String parametro = "";

	public FiltroDTO() {
	}//-------------------------------------------------------------------------

	public String getAttributo() {
		return attributo;
	}

	public void setAttributo(String attributo) {
		this.attributo = attributo;
	}

	public String getOperatore() {
		return operatore;
	}

	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}

	public Object getValore() {
		return valore;
	}

	public void setValore(Object valore) {
		this.valore = valore;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getParametro() {
		return parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}
	
	

}
