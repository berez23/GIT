package it.bod.persistence.dao;

import it.bod.application.beans.DichiaranteBean;

import java.util.List;

public interface DichiaranteDao extends MasterDao{

	public List<DichiaranteBean> getList();
	public List<DichiaranteBean> getListDicCognomi(String cog);
	public List<DichiaranteBean> getListProCognomi(String cog);
	
}
