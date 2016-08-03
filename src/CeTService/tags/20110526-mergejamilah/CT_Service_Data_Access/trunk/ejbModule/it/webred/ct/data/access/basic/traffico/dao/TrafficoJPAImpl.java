package it.webred.ct.data.access.basic.traffico.dao;

import it.webred.ct.data.access.basic.traffico.TrafficoBaseService;
import it.webred.ct.data.access.basic.traffico.TrafficoServiceException;
import it.webred.ct.data.access.basic.traffico.dto.TrafficoSearchCriteria;
import it.webred.ct.data.model.traffico.SitTrffMulte;

import java.util.List;

import javax.persistence.Query;

public class TrafficoJPAImpl extends TrafficoBaseService implements TrafficoDAO {

	@Override
	public List<SitTrffMulte> getListaMulteByCriteria(TrafficoSearchCriteria criteria)
			throws TrafficoServiceException {

		try {

			String sql = (new TrafficoQueryBuilder(criteria)).createQuery();

			Query q = manager_diogene.createQuery(sql);
			return q.getResultList();

		} catch (Throwable t) {
			throw new TrafficoServiceException(t);
		}

	}

}
