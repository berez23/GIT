/*
 * Created on 14-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.common.interfacce;

import java.io.Serializable;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class InterfacciaObject implements Serializable {
	private String descrizione; 
	private String link; 

	/**
	 * @return
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param string
	 */
	public void setDescrizione(String string) {
		descrizione = string;
	}

	/**
	 * @param string
	 */
	public void setLink(String string) {
		link = string;
	}

}
