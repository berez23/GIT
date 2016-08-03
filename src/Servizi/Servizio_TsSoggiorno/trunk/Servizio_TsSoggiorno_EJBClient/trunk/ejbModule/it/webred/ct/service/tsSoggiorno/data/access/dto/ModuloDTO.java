package it.webred.ct.service.tsSoggiorno.data.access.dto;

import java.io.Serializable;
import java.util.List;

import it.webred.ct.service.tsSoggiorno.data.model.IsModuloDati;
import it.webred.ct.service.tsSoggiorno.data.model.IsModuloEventi;
import it.webred.ct.service.tsSoggiorno.data.model.IsModuloOspiti;
import it.webred.ct.service.tsSoggiorno.data.model.IsTabModuloField;

public class ModuloDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private IsModuloDati dati;
	private List<IsTabModuloField> listaField;
	private IsModuloOspiti ospiti;
	private List<IsModuloEventi> eventi;
	
	public IsModuloDati getDati() {
		return dati;
	}
	public void setDati(IsModuloDati dati) {
		this.dati = dati;
	}
	public List<IsTabModuloField> getListaField() {
		return listaField;
	}
	public void setListaField(List<IsTabModuloField> listaField) {
		this.listaField = listaField;
	}
	public IsModuloOspiti getOspiti() {
		return ospiti;
	}
	public void setOspiti(IsModuloOspiti ospiti) {
		this.ospiti = ospiti;
	}
	public List<IsModuloEventi> getEventi() {
		return eventi;
	}
	public void setEventi(List<IsModuloEventi> eventi) {
		this.eventi = eventi;
	}
	
}
