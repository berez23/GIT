package it.webred.ct.service.carContrib.data.access.cc.dto;

import it.webred.ct.service.carContrib.data.model.DecodTipDoc;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class TipDocDTO extends CeTBaseObject implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private DecodTipDoc decodTipDoc;
	
	public TipDocDTO() {
		super();
		decodTipDoc = new DecodTipDoc();
	}
	
	public TipDocDTO(DecodTipDoc dc) {
		super();
		decodTipDoc = new DecodTipDoc();
		decodTipDoc.setCodTipDoc(dc.getCodTipDoc());
		decodTipDoc.setDesTipDoc(dc.getDesTipDoc());
	}
	
	public DecodTipDoc getDecodTipDoc() {
		return decodTipDoc;
	}
	public void setDecodTipDoc(DecodTipDoc decodTipDoc) {
		this.decodTipDoc = decodTipDoc;
	}
	

}
