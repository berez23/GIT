package it.webred.ct.service.tsSoggiorno.data.access.dto;

import java.io.Serializable;
import java.util.List;

import it.webred.ct.service.tsSoggiorno.data.model.IsClassiStruttura;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocieta;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocietaSogg;
import it.webred.ct.service.tsSoggiorno.data.model.IsStruttura;

public class SocietaDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private IsSocieta societa;
	private IsSocietaSogg societaSogg;
	
	public IsSocieta getSocieta() {
		return societa;
	}
	public void setSocieta(IsSocieta societa) {
		this.societa = societa;
	}
	public IsSocietaSogg getSocietaSogg() {
		return societaSogg;
	}
	public void setSocietaSogg(IsSocietaSogg societaSogg) {
		this.societaSogg = societaSogg;
	}
	
}
