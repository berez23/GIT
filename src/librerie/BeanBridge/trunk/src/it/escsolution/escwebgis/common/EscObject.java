/*
 * Created on 9-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.common;

import java.io.Serializable;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class EscObject implements Serializable {

	private String chiave;
	
	protected String diaKey;
	
	public EscObject() {
	}
	
	public String getChiave(){ 
		return "";
	}

	public String getIdFonte() {
		return "*";
	}

	public String getTipoFonte() {
		return "*";
	}

	public String getDiaKey() {
		return diaKey;
	}

	public void setDiaKey(String diaKey) {
		this.diaKey = diaKey;
	}
	
}
