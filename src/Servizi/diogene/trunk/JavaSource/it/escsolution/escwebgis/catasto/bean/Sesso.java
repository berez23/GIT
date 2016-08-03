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
public class Sesso extends EscComboObject implements Serializable,EscComboInterface {
	private String codSesso;
	private String desSesso;
	
	/**
	 * 
	 */
	
	public String getCode(){
		return codSesso;
	}
	public String getDescrizione(){
		return desSesso;
	}
	public Sesso() {
	}
	public Sesso(String cod,String  des) {
		codSesso = cod;
		desSesso = des;
	}
	public String toString() {
		return desSesso;
	}
	/**
	 * @return
	 */
	public String getCodSesso() {
		return codSesso;
	}

	/**
	 * @return
	 */
	public String getDesSesso() {
		return desSesso;
	}

	/**
	 * @param string
	 */
	public void setCodSesso(String string) {
		codSesso = string;
	}

	/**
	 * @param string
	 */
	public void setDesSesso(String string) {
		desSesso = string;
	}

}
