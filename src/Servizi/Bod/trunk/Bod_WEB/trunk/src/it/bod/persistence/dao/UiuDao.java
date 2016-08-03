package it.bod.persistence.dao;

import it.bod.application.beans.UiuBean;

import java.util.List;

public interface UiuDao extends MasterDao{
	
	public List<UiuBean> getListaIndirizzi();
	public List<UiuBean> getListaIndirizzi(String des);

}
