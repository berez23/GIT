/*
 * Created on 9-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.ecog.bean;

import it.escsolution.escwebgis.common.EscComboInterface;
import it.escsolution.escwebgis.common.EscComboObject;

import java.io.Serializable;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Destinazione extends EscComboObject implements Serializable,EscComboInterface {
	private String codDestinazione;
	private String desDestinazione;
	
	/**
	 * 
	 */
	
	public String getCode(){
		return codDestinazione;
	}
	public String getDescrizione(){
		return desDestinazione;
	}
	public Destinazione() {
	}
	public Destinazione(String cod,String  des) {
		codDestinazione = cod;
		desDestinazione = des;
	}
	public String toString() {
		return desDestinazione;
	}
	/**
	 * @return
	 */
	public String getCodDestinazione() {
		return codDestinazione;
	}

	/**
	 * @return
	 */
	public String getDesDestinazione() {
		return desDestinazione;
	}

	/**
	 * @param string
	 */
	public void setCodDestinazione(String string) {
		codDestinazione = string;
	}

	/**
	 * @param string
	 */
	public void setDesDestinazione(String string) {
		desDestinazione = string;
	}

}

