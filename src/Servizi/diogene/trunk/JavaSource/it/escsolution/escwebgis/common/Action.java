/*
 * Created on 8-apr-2004
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
public class Action implements Serializable {
	private String UC;
	//private String SC;
	private int ST;
	



	/**
	 * @return
	 */
	public int getST() {
		return ST;
	}

	/**
	 * @return
	 */
	public String getUC() {
		return UC;
	}


	/**
	 * @param i
	 */
	public void setST(int i) {
		ST = i;
	}

	/**
	 * @param string
	 */
	public void setUC(String string) {
		UC = string;
	}

}
