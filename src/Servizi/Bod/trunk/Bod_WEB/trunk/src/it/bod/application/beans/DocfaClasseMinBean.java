package it.bod.application.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import it.bod.application.common.MasterItem;

public class DocfaClasseMinBean  extends MasterItem{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2377658368418460436L;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private String zc = "";
	private String foglio = "";
	private String categoria = "";
	private String old_microzona = "";
	private String new_microzona="";
	private String classe_min="";

	public String getZc() {
		return zc;
	}
	public void setZc(String zc) {
		this.zc = zc;
	}
	public String getFoglio() {
		return foglio;
	}
	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getOld_microzona() {
		return old_microzona;
	}
	public void setOld_microzona(String oldMicrozona) {
		old_microzona = oldMicrozona;
	}
	public String getNew_microzona() {
		return new_microzona;
	}
	public void setNew_microzona(String newMicrozona) {
		new_microzona = newMicrozona;
	}
	public String getClasse_min() {
		return classe_min;
	}
	public void setClasse_min(String classeMin) {
		classe_min = classeMin;
	}
	
	
	
	
	

	
	
	

	

}
