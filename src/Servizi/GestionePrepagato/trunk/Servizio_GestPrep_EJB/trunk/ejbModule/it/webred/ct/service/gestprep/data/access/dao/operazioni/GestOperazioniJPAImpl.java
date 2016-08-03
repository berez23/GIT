package it.webred.ct.service.gestprep.data.access.dao.operazioni;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import it.webred.ct.service.gestprep.data.access.dao.GestPrepBaseDAO;
import it.webred.ct.service.gestprep.data.access.dao.GestPrepDAOException;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.access.dto.OperazioneDTO;
import it.webred.ct.service.gestprep.data.access.dto.OperazioneSearchCriteria;

import it.webred.ct.service.gestprep.data.model.GestPrepAnagVisura;
import it.webred.ct.service.gestprep.data.model.GestPrepOperazVisure;
import it.webred.ct.service.gestprep.data.model.GestPrepSoggetti;
import it.webred.ct.service.gestprep.data.model.GestPrepTariffaVisura;

public class GestOperazioniJPAImpl extends GestPrepBaseDAO implements GestOperazioniDAO {



	
	public Long createOperazione(GestPrepDTO operazioneDTO)
			throws GestPrepDAOException {
		
		try {
			GestPrepOperazVisure operaz = (GestPrepOperazVisure) operazioneDTO.getObj();
			manager.persist(operaz);
			return operaz.getIdOperaz();
		}
		catch(Throwable t) {
			throw new GestPrepDAOException(t);
		}
	}

	public Long getRecordCount(OperazioneSearchCriteria criteria)
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

	public List<OperazioneDTO> getOperazioniList(
			OperazioneSearchCriteria criteria, int startm, int numberRecord)
			throws GestPrepDAOException {
		
		List<OperazioneDTO> operList = new ArrayList<OperazioneDTO>();
		
		try {
			String sql = (new QueryBuilder(criteria)).createQuery(false);
			
			if (sql == null)
				return new ArrayList<OperazioneDTO>();
			
			Query q = manager.createQuery(sql);
			List<Object[]> result = q.getResultList();
			
			for (Object[] obj : result) {
				GestPrepOperazVisure oper  = (GestPrepOperazVisure) obj[0];
				GestPrepSoggetti sogg = (GestPrepSoggetti) obj[1];
				GestPrepTariffaVisura tar = (GestPrepTariffaVisura) obj[2];
				GestPrepAnagVisura vis = (GestPrepAnagVisura) obj[3];
				
				OperazioneDTO dto = new OperazioneDTO(tar, vis, sogg, oper);
				operList.add(dto);
			}
			
			return operList;

		}
		catch(Throwable t) {
			t.printStackTrace();
			throw new GestPrepDAOException(t);
		}	
	}

}
