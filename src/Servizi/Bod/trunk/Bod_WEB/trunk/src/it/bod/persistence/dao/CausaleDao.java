package it.bod.persistence.dao;

import it.bod.application.beans.CausaleBean;

import java.util.List;

public interface CausaleDao extends MasterDao{
	
	public List<CausaleBean> getList();
	public List<CausaleBean> getListDescrizioni();
	

}
