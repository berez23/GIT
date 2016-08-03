package it.escsolution.escwebgis.multe.bean;

import it.escsolution.escwebgis.common.EscComboInterface;
import it.escsolution.escwebgis.common.EscComboObject;

import java.io.Serializable;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class Pagato extends EscComboObject implements Serializable, EscComboInterface {
	
	private String codPagato;
	private String desPagato;
	
	public String getCode(){
		return codPagato;
	}
	
	public String getDescrizione(){
		return desPagato;
	}
	
	public Pagato() {
	}
	
	public Pagato(String cod, String  des) {
		codPagato = cod;
		desPagato = des;
	}
	
	public String toString() {
		return desPagato;
	}

	public String getCodPagato() {
		return codPagato;
	}

	public void setCodPagato(String codPagato) {
		this.codPagato = codPagato;
	}

	public String getDesPagato() {
		return desPagato;
	}

	public void setDesPagato(String desPagato) {
		this.desPagato = desPagato;
	}

}

