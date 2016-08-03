package it.bod.persistence.dao;

import it.bod.application.beans.DestinazioneFunzionaleBean;

import java.util.List;

public interface DestinazioneFunzionaleDao extends MasterDao{
	
	public List<DestinazioneFunzionaleBean> getListaDestinazioniFunzionali();

}
