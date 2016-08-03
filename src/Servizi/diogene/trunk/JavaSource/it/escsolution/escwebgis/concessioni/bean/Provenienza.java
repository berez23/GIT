package it.escsolution.escwebgis.concessioni.bean;

import it.escsolution.escwebgis.common.EscComboInterface;
import it.escsolution.escwebgis.common.EscComboObject;

import java.io.Serializable;

public class Provenienza extends EscComboObject implements Serializable,EscComboInterface {
	private String provenienza;
	
	public String getCode(){
		return provenienza;
	}
	
	public String getDescrizione(){
		return provenienza;
	}
	
	public Provenienza() {
	}
	
	public Provenienza(String provenienza) {
		this.provenienza = provenienza;
	}
	
	public String toString() {
		return provenienza;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

}
