package it.webred.ct.service.tsSoggiorno.data.access.dto;

import java.io.Serializable;
import java.util.List;

import it.webred.ct.service.tsSoggiorno.data.model.IsClassiStruttura;
import it.webred.ct.service.tsSoggiorno.data.model.IsDichiarazione;
import it.webred.ct.service.tsSoggiorno.data.model.IsImpostaDovuta;
import it.webred.ct.service.tsSoggiorno.data.model.IsImpostaIncassata;
import it.webred.ct.service.tsSoggiorno.data.model.IsPeriodo;
import it.webred.ct.service.tsSoggiorno.data.model.IsRimborso;
import it.webred.ct.service.tsSoggiorno.data.model.IsSanzione;
import it.webred.ct.service.tsSoggiorno.data.model.IsSocieta;
import it.webred.ct.service.tsSoggiorno.data.model.IsStruttura;
import it.webred.ct.service.tsSoggiorno.data.model.IsStrutturaSnap;

public class DichiarazioneDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private IsDichiarazione dichiarazione;
	private IsStrutturaSnap strutturaSnap;
	private IsPeriodo periodo;
	private IsImpostaDovuta impDovuta;
	private IsImpostaIncassata impIncassata;
	private List<IsRimborso> listaRimborsi;
	private List<IsSanzione> listaSanzioni;
	
	public List<IsRimborso> getListaRimborsi() {
		return listaRimborsi;
	}
	public void setListaRimborsi(List<IsRimborso> listaRimborsi) {
		this.listaRimborsi = listaRimborsi;
	}
	public List<IsSanzione> getListaSanzioni() {
		return listaSanzioni;
	}
	public void setListaSanzioni(List<IsSanzione> listaSanzioni) {
		this.listaSanzioni = listaSanzioni;
	}
	public IsDichiarazione getDichiarazione() {
		return dichiarazione;
	}
	public void setDichiarazione(IsDichiarazione dichiarazione) {
		this.dichiarazione = dichiarazione;
	}
	public IsStrutturaSnap getStrutturaSnap() {
		return strutturaSnap;
	}
	public void setStrutturaSnap(IsStrutturaSnap strutturaSnap) {
		this.strutturaSnap = strutturaSnap;
	}
	public IsPeriodo getPeriodo() {
		return periodo;
	}
	public void setPeriodo(IsPeriodo periodo) {
		this.periodo = periodo;
	}
	public IsImpostaDovuta getImpDovuta() {
		return impDovuta;
	}
	public void setImpDovuta(IsImpostaDovuta impDovuta) {
		this.impDovuta = impDovuta;
	}
	public IsImpostaIncassata getImpIncassata() {
		return impIncassata;
	}
	public void setImpIncassata(IsImpostaIncassata impIncassata) {
		this.impIncassata = impIncassata;
	}
	
}
