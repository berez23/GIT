package it.webred.ct.service.gestprep.data.access.dao.deposito;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import it.webred.ct.service.gestprep.data.access.dao.GestPrepBaseDAO;
import it.webred.ct.service.gestprep.data.access.dao.GestPrepDAOException;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepDeposito;



public class GestDepositoJPAImpl extends GestPrepBaseDAO implements GestDepositoDAO {

	public Long createDeposito(GestPrepDTO depositoDTO) throws GestPrepDAOException {
		
		GestPrepDeposito deposito;
		
		try {
			deposito = (GestPrepDeposito) depositoDTO.getObj();			
			manager.persist(deposito);
			return deposito.getIdDep();
		}
		catch(Throwable t) {
			t.printStackTrace();
			throw new GestPrepDAOException(t);
		}
		
	}


	
	public List<GestPrepDeposito> getList(GestPrepDTO depositoDTO) throws GestPrepDAOException {
		List<GestPrepDeposito> result = new ArrayList<GestPrepDeposito>();
		try {
			String sql = "SELECT dep FROM GestPrepDeposito dep " ;
			
			Long idSogg = (Long) depositoDTO.getObj();
			
			
			sql += " WHERE dep.idSoggetto=:idSogg ORDER BY dep.dataDep";
			
			Query q = manager.createQuery(sql);
			q.setParameter("idSogg", idSogg);
			//System.out.println("ID Sogg ["+idSogg+"]");
			
			result = q.getResultList();
			
			//System.out.println("Result ["+result+"]");
		}
		catch(Throwable t) {
			t.printStackTrace();
			throw new GestPrepDAOException(t);
		}		
		
		return result;
		
	}



}
