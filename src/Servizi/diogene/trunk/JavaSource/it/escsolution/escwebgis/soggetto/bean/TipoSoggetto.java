/*
 * Created on 9-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.soggetto.bean;

import it.escsolution.escwebgis.common.EscComboInterface;
import it.escsolution.escwebgis.common.EscComboObject;

import java.io.Serializable;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TipoSoggetto extends EscComboObject implements Serializable,EscComboInterface {
	private String codTipoSoggetto;
	private String desTipoSoggetto;
	
	/**
	 * 
	 */
	
	public String getCode(){
		return codTipoSoggetto;
	}
	public String getDescrizione(){
		return desTipoSoggetto;
	}
	public TipoSoggetto() {
	}
	public TipoSoggetto(String cod,String  des) {
		codTipoSoggetto = cod;
		desTipoSoggetto = des;
	}
	public String toString() {
		return desTipoSoggetto;
	}
	/**
	 * @return
	 */
	public String getCodTipoSoggetto() {
		return codTipoSoggetto;
	}

	/**
	 * @return
	 */
	public String getDesTipoSoggetto() {
		return desTipoSoggetto;
	}

	/**
	 * @param string
	 */
	public void setCodTipoSoggetto(String string) {
		codTipoSoggetto = string;
	}

	/**
	 * @param string
	 */
	public void setDesTipoSoggetto(String string) {
		desTipoSoggetto = string;
	}

}
