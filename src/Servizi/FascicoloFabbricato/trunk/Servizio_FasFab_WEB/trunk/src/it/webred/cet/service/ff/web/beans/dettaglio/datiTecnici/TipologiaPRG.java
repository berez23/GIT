package it.webred.cet.service.ff.web.beans.dettaglio.datiTecnici;

import java.util.ArrayList;
import java.util.List;

public class TipologiaPRG {
	
	private String descrizione;
	private String descrizioneLayer;
	List<List<String>> listaDati = new ArrayList<List<String>>();
	List<String> listaColonne = new ArrayList<String>();
	
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public List<List<String>> getListaDati() {
		return listaDati;
	}
	public void setListaDati(List<List<String>> listaDati) {
		this.listaDati = listaDati;
	}
	public List<String> getListaColonne() {
		return listaColonne;
	}
	public void setListaColonne(List<String> listaColonne) {
		this.listaColonne = listaColonne;
	}
	public String getDescrizioneLayer() {
		return descrizioneLayer;
	}
	public void setDescrizioneLayer(String descrizioneLayer) {
		this.descrizioneLayer = descrizioneLayer;
	}
	
}
