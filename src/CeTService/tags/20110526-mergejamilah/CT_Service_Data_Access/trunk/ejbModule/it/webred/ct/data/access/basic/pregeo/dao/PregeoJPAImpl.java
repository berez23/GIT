package it.webred.ct.data.access.basic.pregeo.dao;

import it.webred.ct.data.access.basic.pregeo.PregeoServiceException;
import it.webred.ct.data.access.basic.pregeo.dto.RicercaPregeoDTO;
import it.webred.ct.data.model.pregeo.PregeoInfo;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

public class PregeoJPAImpl extends PregeoBaseDAO implements PregeoDAO {

	@Override
	public List<PregeoInfo> getDatiPregeoByFabbricato(RicercaPregeoDTO rp)	throws PregeoServiceException {
		List<PregeoInfo> lista=null;
		logger.debug("getDatiPregeoByFabbricato-parms[FOGLIO: "+rp.getFoglio()+"];" + "[PARTICELLA: "+rp.getParticella()+"]" );
		try {
			Query q =manager_diogene.createNamedQuery("PregeoInfo.getInfoByFP");
			q.setParameter("foglio", rp.getFoglio());
			q.setParameter("particella", rp.getParticella());
			lista =(List<PregeoInfo>) q.getResultList();
			logger.warn("TROVATI DATI PREGO . N.ELE: "+ lista.size());
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new PregeoServiceException (t);
		}
		return lista;
	}

}
