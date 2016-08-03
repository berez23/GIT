package it.webred.ct.service.cnc.data.access;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Session Bean implementation class CNCCommonServiceBean
 */
@Stateless
public class CNCCommonServiceBean implements CNCCommonService {

	@PersistenceContext(unitName="Servizio_CNC_Model")
	protected EntityManager manager;

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

	@Override
	public String getCodiceTipoEntrataDescr(String codTipoEntrata) {
		try {
			//System.out.println("Codice ["+codTipoEntrata+"]");
			Query q = manager.createNamedQuery("CodiciTipoEntrata.getCodiceDescr");
			q.setParameter("codice", codTipoEntrata);
			String name = (String) q.getSingleResult();
			return name;
		}
		catch(Throwable t) {
			throw new CNCCommonServiceException(t);
		}
	}

	@Override
	public String getCodiceUfficioDescr(String codUfficio, String codiceEnte,
			String codiceTipoUfficio) {
		try {
			//System.out.println("Codice ["+codTipoEntrata+"]");
			Query q = manager.createNamedQuery("CodiceEnteCredBenef.getCodiceUfficioDescr");
			q.setParameter("codEnte", codiceEnte);
			q.setParameter("codTipoUfficio", codiceTipoUfficio);
			q.setParameter("codUfficio", codUfficio);
			String name = (String) q.getSingleResult();
			return name;
		}
		catch(Throwable t) {
			throw new CNCCommonServiceException(t);
		}
	}

	@Override
	public String getCodiceUfficioDescrDaPartita(String codiceEnte,
			String codicePartita) {
		try {
			//System.out.println("Codice ["+codTipoEntrata+"]");
			Query q = manager.createNamedQuery("CodiceEnteCredBenef.getCodiceUfficioDescrTipo");
			q.setParameter("codEnte", codiceEnte);
			q.setParameter("codTipoUfficio", codicePartita.substring(0,1));
			
			String name = (String) q.getSingleResult();
			return name;
		}
		catch(Throwable t) {
			throw new CNCCommonServiceException(t);
		}	
	}

	@Override
	public String getCodiceUfficioDescrFull(String codiceEnte,
			String codTipoUfficio, String codUfficio) {
		
		try {
			//System.out.println("Codice ["+codTipoEntrata+"]");
			Query q = manager.createNamedQuery("CodiceEnteCredBenef.getCodiceUfficioDescrFullParam");
			q.setParameter("codEnte", codiceEnte);
			q.setParameter("codTipoUfficio", codTipoUfficio);
			q.setParameter("codUfficio", codUfficio);
			
			String name = (String) q.getSingleResult();
			return name;
		}
		catch(Throwable t) {
			throw new CNCCommonServiceException(t);
		}	
	}

    

}
