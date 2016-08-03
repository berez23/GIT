package it.webred.ct.data.access.basic.indice.civico.dao;

import it.webred.ct.data.access.basic.cnc.dao.CNCDAOException;
import it.webred.ct.data.access.basic.indice.dao.IndiceBaseDAO;
import it.webred.ct.data.access.basic.indice.dao.IndiceDAOException;
import javax.persistence.Query;
import java.util.List;

public class CivicoJPAImpl extends IndiceBaseDAO implements CivicoDAO{

	
	public List<Object[]> getListaCiviciByVia(String id, int start, int rowNumber) throws IndiceDAOException {
	
		List<Object[]> result = null;
		
		try {
			
			String sql = (new CivicoQueryBuilder()).createQueryCiviciByViaUnico(id, false);
			
			Query q = manager.createQuery(sql);
			if(start != 0 || rowNumber != 0){
				q.setFirstResult(start);
				q.setMaxResults(rowNumber);
			}
			
			result = q.getResultList();
			
			
		}catch(Throwable t) {
			throw new IndiceDAOException(t);
		}		
		
		return result;
	}
			
	
	public Long getListaCiviciByViaRecordCount(String id) throws IndiceDAOException {
		
		Long ol = 0L;
		
		try{
			
			String sql = (new CivicoQueryBuilder()).createQueryCiviciByViaUnico(id, true);
			
			logger.debug("COUNT LISTA CIVICI - SQL["+sql+"]");
			
			if (sql != null) {
				
				Query q = manager.createQuery(sql);
				Object o = q.getSingleResult();
				ol = (Long) o;
				
				logger.debug("COUNT LISTA CIVICI ["+ol+"]");
				
			}
		}catch(Throwable t) {
			throw new IndiceDAOException(t);
		}	
		
		return ol;
	}
}
