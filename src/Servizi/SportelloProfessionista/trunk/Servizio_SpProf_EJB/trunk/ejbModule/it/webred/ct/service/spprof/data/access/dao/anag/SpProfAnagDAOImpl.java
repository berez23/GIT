package it.webred.ct.service.spprof.data.access.dao.anag;



import it.webred.ct.service.spprof.data.access.dao.SpProfBaseDAO;
import it.webred.ct.service.spprof.data.access.dto.SoggettoSearchCriteria;
import it.webred.ct.service.spprof.data.access.dto.SpProfDTO;
import it.webred.ct.service.spprof.data.access.exception.SpProfDAOException;
import it.webred.ct.service.spprof.data.model.SSpQualifica;
import it.webred.ct.service.spprof.data.model.SSpSoggetto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class SpProfAnagDAOImpl extends SpProfBaseDAO implements SpProfAnagDAO {

	public Long createAnag(SpProfDTO soggDTO) throws SpProfDAOException {
		
		SSpSoggetto sogg;
		
		try {
			sogg = (SSpSoggetto)soggDTO.getObj();
			manager.persist(sogg);
			return sogg.getIdSogg();
			
		}catch(Throwable t) {
			super.logger.error("Eccezione: "+t.getMessage());
			throw new SpProfDAOException(t);
		}
	}

	public void deleteAnag(SpProfDTO soggDTO) throws SpProfDAOException {
		try {
			Query q = manager.createQuery("DELETE FROM SSpSoggetto s WHERE s.idSogg=:idSogg");
			q.setParameter("idSogg", ((Long) soggDTO.getObj()));
			q.executeUpdate();
		}catch(Throwable t) {
			super.logger.error("Eccezione: "+t.getMessage());
			throw new SpProfDAOException(t);
		}
	}

	public Long getRecordCount(SoggettoSearchCriteria criteria)
			throws SpProfDAOException {

		try {
			String sql = (new QueryBuilder(criteria)).createQuery(true);
			
			if (sql == null)
				return 0L;
			
			Query q = manager.createQuery(sql);
			Object o = q.getSingleResult();
			
			return (Long) o;
		}catch(Throwable t) {
			super.logger.error("Eccezione: "+t.getMessage());
			throw new SpProfDAOException(t);
		}
	}

	
	
	public List<SSpSoggetto> getSoggettiList(SoggettoSearchCriteria criteria,int startm, int numberRecord)
			throws SpProfDAOException {
		
		List<SSpSoggetto> soggList = new ArrayList<SSpSoggetto>();
		
		try {
			String sql = (new QueryBuilder(criteria)).createQuery(false);
			
			if (sql == null)
				return new ArrayList<SSpSoggetto>();
			
			Query q = manager.createQuery(sql);
			List<Object[]> result = q.getResultList();
			
			for (Object[] obj : result) {
				SSpSoggetto sogg = (SSpSoggetto) obj[0];
				SSpQualifica qual = (SSpQualifica) obj[1];
				sogg.setDescrQualifica(qual.getDescr());
				
				soggList.add(sogg);
			}
			
			return soggList;
		}catch(Throwable t) {
			super.logger.error("Eccezione: "+t.getMessage());
			throw new SpProfDAOException(t);
		}
	}
	
	

	public boolean updateAnag(SpProfDTO soggDTO) throws SpProfDAOException {


		try {
			manager.merge(soggDTO.getObj());
			return true;
			
		}catch(Throwable t) {
			super.logger.error("Eccezione: "+t.getMessage());
			throw new SpProfDAOException(t);
		}
	}

}
