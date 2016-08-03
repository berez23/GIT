package it.webred.cs.jsf.interfaces;

import it.webred.cs.data.model.CsDIsee;

import java.util.List;

import javax.faces.model.SelectItem;


public interface IIsee {
		
	public void initializeData();
	public void nuovo();
	public void carica();
	public void salva();
	public void elimina();
	public List<CsDIsee> getListaIsee();
	public List<SelectItem> getListaTipologie();
	public CsDIsee getIsee();
	public int getIdxSelected();
}
