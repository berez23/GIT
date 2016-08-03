package it.bod.application.common;

import java.io.Serializable;

public class FilterItem implements Serializable{
	
	private static final long serialVersionUID = -9107811598889847147L;
	
	private String attributo ="";
	private String parametro = "";
	private String operatore = "";
	private Object valore = null;
	
	
	public Object getValore() {
		return valore;
	}
	public void setValore(Object valore) {
		this.valore = valore;
	}
	public String getAttributo() {
		return attributo;
	}
	public void setAttributo(String attributo) {
		this.attributo = attributo;
	}
	public String getParametro() {
		return parametro;
	}
	public void setParametro(String parametro) {
		this.parametro = parametro;
	}
	public String getOperatore() {
		return operatore;
	}
	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}

	
}