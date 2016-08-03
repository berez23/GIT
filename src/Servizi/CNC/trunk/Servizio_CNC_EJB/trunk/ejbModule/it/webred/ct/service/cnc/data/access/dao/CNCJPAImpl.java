package it.webred.ct.service.cnc.data.access.dao;

import it.webred.ct.service.cnc.data.access.CNCCommonServiceException;

import javax.persistence.NoResultException;
import javax.persistence.Query;


public class CNCJPAImpl extends CNCBaseDAO implements CNCDAO {
	
	private static final long serialVersionUID = 1L;

	@Override
	public String getAmbitoDescr(Long codAmbito) {
		try {
			Query q = manager.createNamedQuery("Ambito.getAmbitoDescr");
			q.setParameter("codice", codAmbito);
			String name = (String) q.getSingleResult();
			return name;
		}
		catch(Throwable t) {
			throw new CNCCommonServiceException(t);
		}
	}

	@Override
	public String getCodiceEntrataDescr(String codEntrata) {
		try {
			System.out.println("Codice ["+codEntrata+"]");
			Query q = manager.createNamedQuery("CodiciEntrata.getCodiceDescr");
			q.setParameter("codice", codEntrata);
			String name = (String) q.getSingleResult();
			return name;
		}
		catch(Throwable t) {
			throw new CNCCommonServiceException(t);
		}
	}

	
}
