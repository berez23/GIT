package it.bod.application.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import it.bod.application.common.MasterItem;

public class DocfaFogliMicrozonaBean  extends MasterItem{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1457319990433800337L;
	
	private String zc = "";
	private String foglio = "";
	private String old_microzona = "";
	private String new_microzona = "";
	
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
	
	

	

}
