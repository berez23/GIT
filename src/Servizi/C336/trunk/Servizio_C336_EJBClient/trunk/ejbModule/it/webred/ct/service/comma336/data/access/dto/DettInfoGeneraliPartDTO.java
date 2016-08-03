package it.webred.ct.service.comma336.data.access.dto;

import it.webred.ct.data.access.basic.docfa.dto.DatiGeneraliDocfaDTO;
import it.webred.ct.data.model.catasto.Sititrkc;
import it.webred.ct.data.model.catasto.Sitiuiu;
import it.webred.ct.data.model.fabbricato.FabbricatoExRurale;
import it.webred.ct.data.model.fabbricato.FabbricatoMaiDichiarato;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.List;

public class DettInfoGeneraliPartDTO extends CeTBaseObject {
	
	private static final long serialVersionUID = 1L;
	
	//CATASTO
	private List<Sititrkc> listaTerreniAttuali;
	private List<Sitiuiu>  listaUiuAttuali;
	
	private List<Sititrkc> listaTerreniStorico;
	private List<Sitiuiu>  listaUiuStorico;
	
	//DOCFA
	private List<DatiGeneraliDocfaDTO> listaDocfa;
	
	//FABBRICATI
	private List<FabbricatoExRurale> listaExRurali;
	private List<FabbricatoMaiDichiarato> listaMaiDichiarati;

	public void setListaDocfa(List<DatiGeneraliDocfaDTO> listaDocfa) {
		this.listaDocfa = listaDocfa;
	}

	public List<DatiGeneraliDocfaDTO> getListaDocfa() {
		return listaDocfa;
	}

	public List<Sititrkc> getListaTerreniAttuali() {
		return listaTerreniAttuali;
	}

	public void setListaTerreniAttuali(List<Sititrkc> listaTerreniAttuali) {
		this.listaTerreniAttuali = listaTerreniAttuali;
	}

	public List<Sitiuiu> getListaUiuAttuali() {
		return listaUiuAttuali;
	}

	public void setListaUiuAttuali(List<Sitiuiu> listaUiuAttuali) {
		this.listaUiuAttuali = listaUiuAttuali;
	}

	public void setListaExRurali(List<FabbricatoExRurale> listaExRurali) {
		this.listaExRurali = listaExRurali;
	}

	public List<FabbricatoExRurale> getListaExRurali() {
		return listaExRurali;
	}

	public void setListaMaiDichiarati(List<FabbricatoMaiDichiarato> listaMaiDichiarati) {
		this.listaMaiDichiarati = listaMaiDichiarati;
	}

	public List<FabbricatoMaiDichiarato> getListaMaiDichiarati() {
		return listaMaiDichiarati;
	}

	public List<Sititrkc> getListaTerreniStorico() {
		return listaTerreniStorico;
	}

	public void setListaTerreniStorico(List<Sititrkc> listaTerreniStorico) {
		this.listaTerreniStorico = listaTerreniStorico;
	}

	public List<Sitiuiu> getListaUiuStorico() {
		return listaUiuStorico;
	}

	public void setListaUiuStorico(List<Sitiuiu> listaUiuStorico) {
		this.listaUiuStorico = listaUiuStorico;
	}
	
}
