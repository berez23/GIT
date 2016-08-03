package it.webred.ct.service.gestprep.data.access.dao.tariffe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import it.webred.ct.service.gestprep.data.access.dao.GestPrepBaseDAO;
import it.webred.ct.service.gestprep.data.access.dao.GestPrepDAOException;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepTariffaVisura;


public class GestTariffeJPAImpl extends GestPrepBaseDAO implements GestTariffeDAO {

	public Long createTariffa(GestPrepDTO tariffaDTO) throws GestPrepDAOException {
		
		GestPrepTariffaVisura tariffa;
		
		try {
			tariffa = (GestPrepTariffaVisura) tariffaDTO.getObj();
			chiudiTariffa(tariffa.getIdVisura());
			manager.persist(tariffa);
			return tariffa.getIdTar();
		}
		catch(Throwable t) {
			t.printStackTrace();
			throw new GestPrepDAOException(t);
		}
		
	}


	public void updateTariffa(GestPrepDTO tariffaDTO) throws GestPrepDAOException {
		try {
			manager.merge(tariffaDTO.getObj());			
		}
		catch(Throwable t) {
			throw new GestPrepDAOException(t);
		}
	}
	
	public List<GestPrepTariffaVisura> getList(GestPrepDTO tariffaDTO) throws GestPrepDAOException {
		List<GestPrepTariffaVisura> result = new ArrayList<GestPrepTariffaVisura>();
		try {
			String sql = "SELECT tar FROM GestPrepTariffaVisura tar " ;
			
			Long idVis = (Long) tariffaDTO.getObj();
			
			
			sql += " WHERE tar.idVisura=:idVis ORDER BY tar.dataFineVal";
			
			Query q = manager.createQuery(sql);
			q.setParameter("idVis", idVis);
			
			result = q.getResultList();
		}
		catch(Throwable t) {
			t.printStackTrace();
			throw new GestPrepDAOException(t);
		}		
		
		return result;
		
	}


	public GestPrepTariffaVisura getTariffa(GestPrepDTO tariffaDTO)
			throws GestPrepDAOException {
		
		try {
			Long idtar = Long.parseLong((String) tariffaDTO.getObj());
			
			return manager.find(GestPrepTariffaVisura.class, idtar);
		}
		catch(Throwable t) {
			t.printStackTrace();
			throw new GestPrepDAOException(t);
		}	
	}
	
	
	private void chiudiTariffa(Long idVis) throws GestPrepDAOException {
		Query q = manager.createQuery("UPDATE GestPrepTariffaVisura tar SET tar.dataFineVal=:dtVal WHERE tar.idVisura=:idVis and tar.dataFineVal is null");
		q.setParameter("dtVal", new Date());
		q.setParameter("idVis", idVis);
		q.executeUpdate();
	}
}
