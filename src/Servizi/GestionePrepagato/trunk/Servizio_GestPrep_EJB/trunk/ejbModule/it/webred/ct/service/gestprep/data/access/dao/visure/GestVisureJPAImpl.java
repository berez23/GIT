package it.webred.ct.service.gestprep.data.access.dao.visure;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import it.webred.ct.service.gestprep.data.access.dao.GestPrepBaseDAO;
import it.webred.ct.service.gestprep.data.access.dao.GestPrepDAOException;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepAnagVisura;


public class GestVisureJPAImpl extends GestPrepBaseDAO implements GestVisureDAO {

	public Long createVisura(GestPrepDTO visuraDTO) throws GestPrepDAOException {
		
		GestPrepAnagVisura visura;
		
		try {
			visura = (GestPrepAnagVisura) visuraDTO.getObj();
			manager.persist(visura);
			return visura.getIdVis();
		}
		catch(Throwable t) {
			throw new GestPrepDAOException(t);
		}
		
	}


	public boolean updateVisura(GestPrepDTO visuraDTO) throws GestPrepDAOException {
		try {
			manager.merge(visuraDTO.getObj());
			return true;
		}
		catch(Throwable t) {
			throw new GestPrepDAOException(t);
		}
	}
	
	public List<GestPrepAnagVisura> getList(GestPrepDTO visuraDTO) throws GestPrepDAOException {
		List<GestPrepAnagVisura> result = new ArrayList<GestPrepAnagVisura>();
		try {
			String sql = "SELECT vis FROM GestPrepAnagVisura vis " ;
			
			String namePat = (String) visuraDTO.getObj();
			
			if (namePat != null && !namePat.equals(""))
				sql += " WHERE vis.descr LIKE '" + namePat + "'";
			
			sql += " ORDER BY vis.descr";
			
			Query q = manager.createQuery(sql);
			
			result = q.getResultList();
		}
		catch(Throwable t) {
			throw new GestPrepDAOException(t);
		}		
		
		return result;
		
	}


	public GestPrepAnagVisura getVisura(GestPrepDTO visuraDTO)
			throws GestPrepDAOException {
		
		try {
			Long idVis = Long.parseLong((String) visuraDTO.getObj());
			System.out.println("Id Vis ["+idVis+"]");
			return manager.find(GestPrepAnagVisura.class, idVis);
		}
		catch(Throwable t) {
			t.printStackTrace();
			throw new GestPrepDAOException(t);
		}	
	}

}
