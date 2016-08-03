package it.webred.ct.service.comma336.data.access.dto;

import it.webred.ct.data.access.basic.catasto.dto.ParametriCatastaliDTO;
import it.webred.ct.data.access.basic.catasto.dto.SoggettoCatastoDTO;
import it.webred.ct.data.access.basic.concedilizie.dto.ConcessioneDTO;
import it.webred.ct.data.access.basic.docfa.dto.DatiGeneraliDocfaDTO;
import it.webred.ct.data.access.basic.ici.dto.IciDTO;
import it.webred.ct.data.access.basic.ici.dto.SoggOggIciDTO;
import it.webred.ct.data.model.catasto.Sitiuiu;
import it.webred.ct.data.model.ici.SitTIciOggetto;
import it.webred.ct.data.model.ici.VTIciSoggAll;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.List;

public class DettInfoGeneraliUiuDTO extends CeTBaseObject {
	
	private static final long serialVersionUID = 1L;
	
	private ParametriCatastaliDTO uiu;
	
	//CATASTO
	private List<Sitiuiu> listaSitiuiu;
	private List<SoggettoCatastoDTO> listaTitolariCatasto;
	
	//DOCFA
	private List<DatiGeneraliDocfaDTO> listaDocfa;
	
	//CONC.EDILIZIE
	private List<ConcessioneDTO> listaConcEdi;
	
	//ICI
	private List<SitTIciOggetto> listaOggettiIci;
	private List<SoggOggIciDTO> listaSoggettiIci;
	

	public List<SoggettoCatastoDTO> getListaTitolariCatasto() {
		return listaTitolariCatasto;
	}

	public void setListaTitolariCatasto(
			List<SoggettoCatastoDTO> listaTitolariCatasto) {
		this.listaTitolariCatasto = listaTitolariCatasto;
	}

	public List<ConcessioneDTO> getListaConcEdi() {
		return listaConcEdi;
	}

	public void setListaConcEdi(List<ConcessioneDTO> listaConcEdi) {
		this.listaConcEdi = listaConcEdi;
	}

	public void setListaSitiuiu(List<Sitiuiu> listaSitiuiu) {
		this.listaSitiuiu = listaSitiuiu;
	}

	public List<Sitiuiu> getListaSitiuiu() {
		return listaSitiuiu;
	}

	public void setListaDocfa(List<DatiGeneraliDocfaDTO> listaDocfa) {
		this.listaDocfa = listaDocfa;
	}

	public List<DatiGeneraliDocfaDTO> getListaDocfa() {
		return listaDocfa;
	}

	public List<SitTIciOggetto> getListaOggettiIci() {
		return listaOggettiIci;
	}

	public void setListaOggettiIci(List<SitTIciOggetto> listaOggettiIci) {
		this.listaOggettiIci = listaOggettiIci;
	}

	public List<SoggOggIciDTO> getListaSoggettiIci() {
		return listaSoggettiIci;
	}

	public void setListaSoggettiIci(List<SoggOggIciDTO> listaSoggettiIci) {
		this.listaSoggettiIci = listaSoggettiIci;
	}

		
}
