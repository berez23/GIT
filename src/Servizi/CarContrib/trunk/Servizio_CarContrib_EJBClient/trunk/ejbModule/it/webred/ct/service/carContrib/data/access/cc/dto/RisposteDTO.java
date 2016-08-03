package it.webred.ct.service.carContrib.data.access.cc.dto;

import it.webred.ct.service.carContrib.data.model.Risposte;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class RisposteDTO extends CeTBaseObject implements Serializable{

	private static final long serialVersionUID = 1L;
	private Risposte risp;
	
	public void setRisp(Risposte risp) {
		this.risp = risp;
	}
	public Risposte getRisp() {
		return risp;
	}
}
