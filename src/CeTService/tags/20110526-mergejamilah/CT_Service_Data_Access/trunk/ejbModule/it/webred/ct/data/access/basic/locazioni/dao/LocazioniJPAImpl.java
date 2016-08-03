package it.webred.ct.data.access.basic.locazioni.dao;

import it.webred.ct.data.access.basic.catasto.CatastoServiceException;
import it.webred.ct.data.access.basic.locazioni.LocazioniServiceException;
import it.webred.ct.data.access.basic.locazioni.dto.RicercaLocazioniDTO;
import it.webred.ct.data.model.anagrafe.SitDPersona;
import it.webred.ct.data.model.locazioni.LocazioneBPK;
import it.webred.ct.data.model.locazioni.LocazioniA;
import it.webred.ct.data.model.locazioni.LocazioniB;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

public class LocazioniJPAImpl extends LocazioniBaseDAO implements LocazioniDAO {
	
	public List<LocazioniA> getLocazioniByCF(RicercaLocazioniDTO rl) throws LocazioniServiceException {
		List<LocazioniA> lista=null;
		logger.debug("LOCAZIONI PER CF[CF "+rl.getCodFis()+"];" + "[DATA_RIF(OPZ): "+rl.getDtRif()+"]" );
		try {
			Query q = null;
			if (rl.getDtRif()==null)
				q = manager_diogene.createNamedQuery("Locazioni_A.getLocazioniByCF");
			else
				q =	manager_diogene.createNamedQuery("Locazioni_A.getLocazioniByCFAllaData");
			q.setParameter("codFis", rl.getCodFis());
			if (rl.getDtRif()!= null)
				q.setParameter("dtRif", rl.getDtRif());
			lista =(List<LocazioniA>) q.getResultList();
			logger.warn("TROVATO. N.ELE: "+ lista.size());
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new LocazioniServiceException(t);
		}
		return lista;
	}
	
	public LocazioniB getLocazioneSoggByKey(RicercaLocazioniDTO rl) throws LocazioniServiceException {
		LocazioniB loc=null;
		logger.debug("LOCAZIONI PER KEY[Ufficio "+rl.getKey().getUfficio()+"];[anno "+rl.getKey().getAnno()+"];[Serie "+rl.getKey().getSerie()+"];[Numero "+rl.getKey().getNumero()+"];[Prog_Sogg "+rl.getKey().getProgSoggetto()+"];" +"[DATA_RIF(OPZ): "+rl.getDtRif()+"]" );
		try {
			Query q = null;
			q = manager_diogene.createNamedQuery("Locazioni_B.getSoggByKey");
			q.setParameter("ufficio", rl.getKey().getUfficio());
			q.setParameter("anno", rl.getKey().getAnno());
			q.setParameter("serie", rl.getKey().getSerie());
			q.setParameter("numero", rl.getKey().getNumero());
			q.setParameter("progSogg", rl.getKey().getProgSoggetto());
			if (rl.getDtRif()!= null)
				q.setParameter("dtRif", rl.getDtRif());
			loc =(LocazioniB) q.getSingleResult();
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new LocazioniServiceException(t);
		}
		return loc;
	}
	public List<LocazioniA> getLocazioniOggByKey(RicercaLocazioniDTO rl) throws LocazioniServiceException {
		List<LocazioniA> lista=null;
		logger.debug("LOCAZIONI PER KEY[Ufficio "+rl.getKey().getUfficio()+"];[anno "+rl.getKey().getAnno()+"];[Serie "+rl.getKey().getSerie()+"];[Numero "+rl.getKey().getNumero()+"];" +"[DATA_RIF(OPZ): "+rl.getDtRif()+"]" );
		try {
			Query q = null;
			if (rl.getDtRif()==null)
				q = manager_diogene.createNamedQuery("Locazioni_A.getOggByKey");
			else
				q =	manager_diogene.createNamedQuery("Locazioni_A.getOggByKeyAllaData");
			q.setParameter("ufficio", rl.getKey().getUfficio());
			q.setParameter("anno", rl.getKey().getAnno());
			q.setParameter("serie", rl.getKey().getSerie());
			q.setParameter("numero", rl.getKey().getNumero());
			if (rl.getDtRif()!= null)
				q.setParameter("dtRif", rl.getDtRif());
			lista =(List<LocazioniA>) q.getResultList();
		}catch (NoResultException nre) {
			logger.warn("No Result " + nre.getMessage());
		} catch (Throwable t) {
			logger.error("", t);
			throw new LocazioniServiceException(t);
		}
		return lista;
	}

	
}	
