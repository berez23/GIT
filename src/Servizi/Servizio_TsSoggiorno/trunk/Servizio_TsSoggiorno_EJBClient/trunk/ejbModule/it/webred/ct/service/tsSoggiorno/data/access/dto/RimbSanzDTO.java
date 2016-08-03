package it.webred.ct.service.tsSoggiorno.data.access.dto;

import java.io.Serializable;
import java.util.List;

import it.webred.ct.service.tsSoggiorno.data.model.IsClasse;
import it.webred.ct.service.tsSoggiorno.data.model.IsClassiStruttura;
import it.webred.ct.service.tsSoggiorno.data.model.IsPeriodo;
import it.webred.ct.service.tsSoggiorno.data.model.IsRimborso;
import it.webred.ct.service.tsSoggiorno.data.model.IsSanzione;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocieta;
import it.webred.ct.service.tsSoggiorno.data.model.IsStruttura;

public class RimbSanzDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private IsRimborso rimborso;
	private IsSanzione sanzione;
	private IsPeriodo periodo;
	private IsClassiStruttura classe;
	private IsSocieta societa;
	private IsStruttura struttura;
	
	public IsPeriodo getPeriodo() {
		return periodo;
	}
	public void setPeriodo(IsPeriodo periodo) {
		this.periodo = periodo;
	}
	public IsClassiStruttura getClasse() {
		return classe;
	}
	public void setClasse(IsClassiStruttura classe) {
		this.classe = classe;
	}
	public IsRimborso getRimborso() {
		return rimborso;
	}
	public void setRimborso(IsRimborso rimborso) {
		this.rimborso = rimborso;
	}
	public IsSanzione getSanzione() {
		return sanzione;
	}
	public void setSanzione(IsSanzione sanzione) {
		this.sanzione = sanzione;
	}
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
	
}
