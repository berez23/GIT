package it.webred.ct.service.carContrib.data.access.cc.dto;

import it.webred.ct.service.carContrib.data.model.CfgFonteNote;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.io.Serializable;

public class FonteNoteDTO extends CeTBaseObject implements Serializable{

	private static final long serialVersionUID = 1L;
	private CfgFonteNote cfgFonteNote;
	public CfgFonteNote getCfgFonteNote() {
		return cfgFonteNote;
	}
	public void setCfgFonteNote(CfgFonteNote cfgFonteNote) {
		this.cfgFonteNote = cfgFonteNote;
	}
	

}
