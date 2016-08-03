package it.escsolution.escwebgis.pratichePortale.bean;

import it.escsolution.escwebgis.common.EscComboInterface;
import it.escsolution.escwebgis.common.EscComboObject;

import java.io.Serializable;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class TipiServizio extends EscComboObject implements Serializable, EscComboInterface {
	
	private String codServizio;
	private String desServizio;
	
	public String getCode(){
		return codServizio;
	}
	
	public String getDescrizione(){
		return desServizio;
	}
	
	public TipiServizio() {
	}
	
	public TipiServizio(String cod, String  des) {
		codServizio = cod;
		desServizio = des;
	}
	
	public String toString() {
		return desServizio;
	}

	public String getCodServizio() {
		return codServizio;
	}

	public void setCodServizio(String codServizio) {
		this.codServizio = codServizio;
	}

	public String getDesServizio() {
		return desServizio;
	}

	public void setDesServizio(String desServizio) {
		this.desServizio = desServizio;
	}

}

