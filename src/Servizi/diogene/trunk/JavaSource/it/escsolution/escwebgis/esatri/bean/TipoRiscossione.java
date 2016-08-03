/*
 * Created on 9-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.esatri.bean;

import it.escsolution.escwebgis.common.EscComboInterface;
import it.escsolution.escwebgis.common.EscComboObject;

import java.io.Serializable;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TipoRiscossione extends EscComboObject implements Serializable,EscComboInterface {
	private String codTipo;
	private String desTipo;
	
	/**
	 * 
	 */
	
	public String getCode(){
		return codTipo;
	}
	public String getDescrizione(){
		return desTipo;
	}
	public TipoRiscossione() {
	}
	public TipoRiscossione(String cod,String  des) {
		codTipo = cod;
		desTipo = des;
	}
	public String toString() {
		return desTipo;
	}
	/**
	 * @return
	 */
	public String getCodSesso() {
		return codTipo;
	}

	/**
	 * @return
	 */
	public String getDesSesso() {
		return desTipo;
	}

	/**
	 * @param string
	 */
	public void setCodSesso(String string) {
		codTipo = string;
	}

	/**
	 * @param string
	 */
	public void setDesSesso(String string) {
		desTipo = string;
	}

}
