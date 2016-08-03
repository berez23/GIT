/*
 * Created on 29-apr-2004
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
public class EscOperatoreFiltro implements Serializable {
	private String codiceOperatore;
	private String desOperatore;
	public EscOperatoreFiltro( String cod, String des){
		codiceOperatore = cod;
		desOperatore = des;
	}

	/**
	 * @return
	 */
	public String getCodiceOperatore() {
		return codiceOperatore;
	}

	/**
	 * @return
	 */
	public String getDesOperatore() {
		return desOperatore;
	}

	/**
	 * @param string
	 */
	public void setCodiceOperatore(String string) {
		codiceOperatore = string;
	}

	/**
	 * @param string
	 */
	public void setDesOperatore(String string) {
		desOperatore = string;
	}

}
