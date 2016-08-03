package it.webred.ct.service.gestprep.data.access.dao.credito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import it.webred.ct.service.gestprep.data.access.dao.GestPrepBaseDAO;
import it.webred.ct.service.gestprep.data.access.dao.GestPrepDAOException;
import it.webred.ct.service.gestprep.data.access.dto.GestPrepDTO;
import it.webred.ct.service.gestprep.data.model.GestPrepCredito;
import it.webred.ct.service.gestprep.data.model.GestPrepDeposito;



public class GestCreditoJPAImpl extends GestPrepBaseDAO implements GestCreditoDAO {

	public void updateCredito(GestPrepDTO creditoDTO, BigDecimal importo) throws GestPrepDAOException {
		
		try {
			Long idSogg = (Long) creditoDTO.getObj();
			Query q = manager.createQuery("SELECT cred From GestPrepCredito cred WHERE cred.idSoggetto=:idSogg");
			q.setParameter("idSogg", idSogg);
			
			try {
				GestPrepCredito tmpCred = (GestPrepCredito) q.getSingleResult();
				tmpCred.setDataOp(new Date());
				
				tmpCred.setCredito(tmpCred.getCredito().add(importo));
				manager.merge(tmpCred);
			}
			catch(NoResultException nre) {
				GestPrepCredito tmpCred = new GestPrepCredito();
				tmpCred.setIdSoggetto(idSogg);
				tmpCred.setCredito(importo);
				tmpCred.setDataOp(new Date());
				manager.persist(tmpCred);
			}
		}
		catch(Throwable t) {
			t.printStackTrace();
			throw new GestPrepDAOException(t);
		}
		
	}

	public GestPrepCredito getCredito(GestPrepDTO creditoDTO)
			throws GestPrepDAOException {
		try {
			Long idSogg = (Long) creditoDTO.getObj();
			Query q = manager.createQuery("SELECT cred From GestPrepCredito cred WHERE cred.idSoggetto=:idSogg");
			q.setParameter("idSogg", idSogg);
			
			GestPrepCredito credito = (GestPrepCredito) q.getSingleResult();
			
			return credito;
		}
		catch(Throwable t) {
			t.printStackTrace();
			throw new GestPrepDAOException(t);
		}
	}


	




}
