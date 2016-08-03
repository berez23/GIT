package it.escsolution.escwebgis.rette.bean;

import it.escsolution.escwebgis.common.EscComboInterface;
import it.escsolution.escwebgis.common.EscComboObject;

import java.io.Serializable;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class Anno extends EscComboObject implements Serializable, EscComboInterface {
	
	private String codAnno;
	private String desAnno;
	
	public String getCode(){
		return codAnno;
	}
	
	public String getDescrizione(){
		return desAnno;
	}
	
	public Anno() {
	}
	
	public Anno(String cod, String  des) {
		codAnno = cod;
		desAnno = des;
	}
	
	public String toString() {
		return desAnno;
	}

	public String getCodAnno() {
		return codAnno;
	}

	public void setCodAnno(String codAnno) {
		this.codAnno = codAnno;
	}

	public String getDesAnno() {
		return desAnno;
	}

	public void setDesAnno(String desAnno) {
		this.desAnno = desAnno;
	}

}

