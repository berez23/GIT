package it.webred.ct.service.gestprep.data.access.dao.anag;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import it.webred.ct.service.gestprep.data.access.dao.GestPrepBaseDAO;
import it.webred.ct.service.gestprep.data.access.dao.GestPrepDAOException;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.access.dto.SoggettoSearchCriteria;
import it.webred.ct.service.gestprep.data.model.GestPrepQualifica;
import it.webred.ct.service.gestprep.data.model.GestPrepSoggetti;

public class GestAnagJPAImpl extends GestPrepBaseDAO implements GestAnagDAO {

	public Long createAnag(GestPrepDTO soggDTO) throws GestPrepDAOException {
		
		GestPrepSoggetti sogg;
		
		try {
			sogg = (GestPrepSoggetti) soggDTO.getObj();
			manager.persist(sogg);
			return sogg.getIdSogg();
		}
		catch(Throwable t) {
			t.printStackTrace();
			throw new GestPrepDAOException(t);
		}
		
	}

	public void deleteAnag(GestPrepDTO soggDTO) throws GestPrepDAOException {
		try {
			Query q = manager.createQuery("DELETE FROM GestPrepSoggetti s WHERE s.idSogg=:idSogg");
			q.setParameter("idSogg", ( (GestPrepSoggetti) soggDTO.getObj()).getIdSogg() );
			q.executeUpdate();
		}
		catch(Throwable t) {
			throw new GestPrepDAOException(t);
		}
		
	}

	public boolean updateAnag(GestPrepDTO soggDTO) throws GestPrepDAOException {
		try {
			manager.merge(soggDTO.getObj());
			return true;
		}
		catch(Throwable t) {
			throw new GestPrepDAOException(t);
		}
	}

	public Long getRecordCount(SoggettoSearchCriteria criteria)
			throws GestPrepDAOException {
		
		try {
			String sql = (new QueryBuilder(criteria)).createQuery(true);
			
			if (sql == null)
				return 0L;
			
			Query q = manager.createQuery(sql);
			Object o = q.getSingleResult();
			
			return (Long) o;

		}
		catch(Throwable t) {
			throw new GestPrepDAOException(t);
		}	
	}

	public List<GestPrepSoggetti> getSoggettiList(
			SoggettoSearchCriteria criteria, int startm, int numberRecord)
			throws GestPrepDAOException {

		List<GestPrepSoggetti> soggList = new ArrayList<GestPrepSoggetti>();
		
		try {
			String sql = (new QueryBuilder(criteria)).createQuery(false);
			
			if (sql == null)
				return new ArrayList<GestPrepSoggetti>();
			
			Query q = manager.createQuery(sql);
			List<Object[]> result = q.getResultList();
			
			for (Object[] obj : result) {
				GestPrepSoggetti sogg = (GestPrepSoggetti) obj[0];
				GestPrepQualifica qual = (GestPrepQualifica) obj[1];
				sogg.setDescrQualifica(qual.getDescr());
				
				soggList.add(sogg);
			}
			
			return soggList;

		}
		catch(Throwable t) {
			t.printStackTrace();
			throw new GestPrepDAOException(t);
		}		
	}

}
