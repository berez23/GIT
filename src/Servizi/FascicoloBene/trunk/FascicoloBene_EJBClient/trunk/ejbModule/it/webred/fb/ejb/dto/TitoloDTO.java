package it.webred.fb.ejb.dto;

import it.webred.fb.data.model.DmBTitolo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TitoloDTO implements Serializable {

	
	private List<BeneInListaDTO> lstBeni;
	private DmBTitolo titolo;
	
	public TitoloDTO(){
		lstBeni = new ArrayList<BeneInListaDTO>();
	}
	
	public List<BeneInListaDTO> getLstBeni() {
		return lstBeni;
	}
	public void setLstBeni(List<BeneInListaDTO> lstBeni) {
		this.lstBeni = lstBeni;
	}
	public DmBTitolo getTitolo() {
		return titolo;
	}
	public void setTitolo(DmBTitolo titolo) {
		this.titolo = titolo;
	}

}
