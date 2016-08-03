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
public class Categoria extends EscComboObject implements Serializable,EscComboInterface {
	private String codCategoria;
	private String desCategoria;
	public String toString() {
		return desCategoria;
	}
	/**
	 * 
	 */
	public Categoria() {
	}
	public String getCode(){
		return codCategoria;
	}
	public String getDescrizione(){
		return desCategoria;
	}
	
	public Categoria(String cod,String  des) {
		codCategoria = cod;
		desCategoria = des;
	}

	/**
	 * @return
	 */
	public String getCodCategoria() {
		return codCategoria;
	}

	/**
	 * @return
	 */
	public String getDesCategoria() {
		return desCategoria;
	}

	/**
	 * @param string
	 */
	public void setCodCategoria(String string) {
		codCategoria = string;
	}

	/**
	 * @param string
	 */
	public void setDesCategoria(String string) {
		desCategoria = string;
	}

}
