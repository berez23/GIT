package it.webred.indice;

import java.io.Serializable;

public class Fonte implements Serializable {

	private String id;
	private String codice;
	private String descr;
	private String info;
	private String prog_old;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getProg_old() {
		return prog_old;
	}
	public void setProg_old(String progOld) {
		prog_old = progOld;
	}
	
	
	
	
	
}
