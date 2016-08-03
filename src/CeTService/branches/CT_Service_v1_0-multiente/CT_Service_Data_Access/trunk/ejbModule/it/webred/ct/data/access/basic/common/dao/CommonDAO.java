package it.webred.ct.data.access.basic.common.dao;

import java.util.List;

import it.webred.ct.data.model.common.SitEnte;

public interface CommonDAO {
	
	public SitEnte getEnte();

	public  List<Object[]> executeNativeQuery(String query);
}
