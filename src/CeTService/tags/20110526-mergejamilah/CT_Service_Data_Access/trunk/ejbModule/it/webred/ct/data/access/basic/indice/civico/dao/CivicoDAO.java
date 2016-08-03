package it.webred.ct.data.access.basic.indice.civico.dao;

import it.webred.ct.data.access.basic.indice.dao.IndiceDAOException;
import java.util.List;


public interface CivicoDAO {

	public List<Object[]> getListaCiviciByVia(String id, int start, int rowNumber) throws IndiceDAOException;
	
	public Long getListaCiviciByViaRecordCount(String id) throws IndiceDAOException;
}
