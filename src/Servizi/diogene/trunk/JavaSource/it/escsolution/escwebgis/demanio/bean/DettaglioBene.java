package it.escsolution.escwebgis.demanio.bean;

import it.webred.fb.data.model.DmBBene;
import it.webred.fb.data.model.DmBFascicolo;
import it.webred.fb.data.model.DmBIndirizzo;
import it.webred.fb.data.model.DmBInfo;
import it.webred.fb.data.model.DmBTerreno;
import it.webred.fb.data.model.DmBTipoUso;
import it.webred.fb.data.model.DmBTitolo;
import it.webred.fb.data.model.DmDDoc;
import it.webred.fb.ejb.dto.EventoDTO;
import it.webred.fb.ejb.dto.MappaleDTO;
import it.webred.fb.ejb.dto.TitoloDTO;

import java.util.List;

public class DettaglioBene {

	private DmBBene bene;
	private DmBInfo info;
	private DmBTipoUso uso;
	private DmBFascicolo fascicolo;
	private List<DmBIndirizzo> indirizzi;
	private List<MappaleDTO> mappali;
	private List<EventoDTO> eventi;
	private List<DmBTerreno> terreni;
	private List<TitoloDTO> titoli;
	private List<DmDDoc> documenti;
	
	public DmBBene getBene() {
		return bene;
	}
	public DmBFascicolo getFascicolo() {
		return fascicolo;
	}
	public void setFascicolo(DmBFascicolo fascicolo) {
		this.fascicolo = fascicolo;
	}
	public void setBene(DmBBene bene) {
		this.bene = bene;
	}
	public List<DmBIndirizzo> getIndirizzi() {
		return indirizzi;
	}
	public void setIndirizzi(List<DmBIndirizzo> indirizzi) {
		this.indirizzi = indirizzi;
	}
	public DmBInfo getInfo() {
		return info;
	}
	public void setInfo(DmBInfo info) {
		this.info = info;
	}
	public DmBTipoUso getUso() {
		return uso;
	}
	public void setUso(DmBTipoUso uso) {
		this.uso = uso;
	}
	public List<EventoDTO> getEventi() {
		return eventi;
	}
	public void setEventi(List<EventoDTO> eventi) {
		this.eventi = eventi;
	}
	public List<DmBTerreno> getTerreni() {
		return terreni;
	}
	public void setTerreni(List<DmBTerreno> terreni) {
		this.terreni = terreni;
	}
	public List<TitoloDTO> getTitoli() {
		return titoli;
	}
	public void setTitoli(List<TitoloDTO> titoli) {
		this.titoli = titoli;
	}
	public List<DmDDoc> getDocumenti() {
		return documenti;
	}
	public void setDocumenti(List<DmDDoc> documenti) {
		this.documenti = documenti;
	}
	public List<MappaleDTO> getMappali() {
		return mappali;
	}
	public void setMappali(List<MappaleDTO> mappali) {
		this.mappali = mappali;
	}
	
}
