/*
 * Created on 9-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.catasto.bean;

import it.escsolution.escwebgis.common.EscComboInterface;
import it.escsolution.escwebgis.common.EscComboObject;

import java.io.Serializable;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Qualita extends EscComboObject implements Serializable,EscComboInterface {
	private String codComune;
	private String desComune;
	
	/**
	 * 
	 */
	
	public String getCode(){
		return codComune;
	}
	public String getDescrizione(){
		return desComune;
	}
	public Qualita() {
	}
	public Qualita(String cod,String  des) {
		codComune = cod;
		desComune = des;
	}
	public String toString() {
		return desComune;
	}
	/**
	 * @return
	 */
	public String getCodComune() {
		return codComune;
	}

	/**
	 * @return
	 */
	public String getDesComune() {
		return desComune;
	}

	/**
	 * @param string
	 */
	public void setCodComune(String string) {
		codComune = string;
	}

	/**
	 * @param string
	 */
	public void setDesComune(String string) {
		desComune = string;
	}

}
