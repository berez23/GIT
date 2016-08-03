package it.escsolution.escwebgis.condono.bean;

import it.escsolution.escwebgis.common.EscObject;

import java.io.Serializable;

public class CondonoCoordinate  extends EscObject implements Serializable {

	private String codcondono;
	private String foglio;
	private String mappale;
	private String sub;	
	private boolean docfa;

	public String getCodcondono() {
		return codcondono;
	}

	public void setCodcondono(String codcondono) {
		this.codcondono = codcondono;
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

	public boolean isDocfa() {
		return docfa;
	}

	public void setDocfa(boolean docfa) {
		this.docfa = docfa;
	}
	
}
