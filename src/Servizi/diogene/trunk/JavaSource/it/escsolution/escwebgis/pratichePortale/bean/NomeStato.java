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

public class NomeStato extends EscComboObject implements Serializable, EscComboInterface {
	
	private String codStato;
	private String desStato;
	
	public String getCode(){
		return codStato;
	}
	
	public String getDescrizione(){
		return desStato;
	}
	
	public NomeStato() {
	}
	
	public NomeStato(String cod, String  des) {
		codStato = cod;
		desStato = des;
	}
	
	public String toString() {
		return desStato;
	}

	public String getCodServizio() {
		return codStato;
	}

	public void setCodStato(String codStato) {
		this.codStato = codStato;
	}

	public String getDesStato() {
		return desStato;
	}

	public void setDesStato(String desStato) {
		this.desStato = desStato;
	}

}

