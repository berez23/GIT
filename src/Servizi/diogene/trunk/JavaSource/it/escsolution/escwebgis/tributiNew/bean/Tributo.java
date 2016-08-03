package it.escsolution.escwebgis.tributiNew.bean;

import it.escsolution.escwebgis.common.EscComboInterface;
import it.escsolution.escwebgis.common.EscComboObject;

import java.io.Serializable;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class Tributo extends EscComboObject implements Serializable, EscComboInterface {
	
	private String codTributo;
	private String desTributo;
	
	public String getCode(){
		return codTributo;
	}
	
	public String getDescrizione(){
		return desTributo;
	}
	
	public Tributo() {
	}
	
	public Tributo(String cod, String  des) {
		codTributo = cod;
		desTributo = des;
	}
	
	public String toString() {
		return desTributo;
	}

	public String getCodTributo() {
		return codTributo;
	}

	public void setCodTributo(String codTributo) {
		this.codTributo = codTributo;
	}

	public String getDesTributo() {
		return desTributo;
	}

	public void setDesTributo(String desTributo) {
		this.desTributo = desTributo;
	}

}

