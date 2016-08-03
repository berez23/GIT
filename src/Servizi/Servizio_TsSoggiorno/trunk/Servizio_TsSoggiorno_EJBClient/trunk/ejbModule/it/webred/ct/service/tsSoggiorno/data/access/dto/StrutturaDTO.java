package it.webred.ct.service.tsSoggiorno.data.access.dto;

import java.io.Serializable;
import java.util.List;

import it.webred.ct.service.tsSoggiorno.data.model.IsClassiStruttura;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocieta;
import it.webred.ct.service.tsSoggiorno.data.model.IsStruttura;

public class StrutturaDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private IsStruttura struttura;
	private IsSocieta societa;
	private List<IsClassiStruttura> classi;
	
	public IsStruttura getStruttura() {
		return struttura;
	}
	public void setStruttura(IsStruttura struttura) {
		this.struttura = struttura;
	}
	public IsSocieta getSocieta() {
		return societa;
	}
	public void setSocieta(IsSocieta societa) {
		this.societa = societa;
	}
	public List<IsClassiStruttura> getClassi() {
		return classi;
	}
	public void setClassi(List<IsClassiStruttura> classi) {
		this.classi = classi;
	}
	
}
