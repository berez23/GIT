package it.webred.ct.data.access.basic.rette.dao;

import it.webred.ct.data.access.basic.rette.RetteBaseService;
import it.webred.ct.data.access.basic.rette.RetteServiceException;
import it.webred.ct.data.model.anagrafe.SitDPersona;
import it.webred.ct.data.model.rette.SitRttBollette;
import it.webred.ct.data.model.rette.SitRttDettBollette;
import it.webred.ct.data.model.rette.SitRttRateBollette;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class RetteJPAImpl extends RetteBaseService implements RetteDAO {

	@Override
	public List<SitRttBollette> getListaBollettePagateByCodFis(String codFiscale)
			throws RetteServiceException {

		List<SitRttBollette> listaBollette = new ArrayList<SitRttBollette>();
		
		try {
			//anche bollette familiari
			Query q = manager_diogene.createNamedQuery("SitDPersona.getFamigliaByCF");
			q.setParameter("codFisc", codFiscale.toUpperCase());
			List<SitDPersona> listaFamiliari = q.getResultList();
			
			for(SitDPersona persona: listaFamiliari){
				System.out.println("***********" + persona.getCodfisc() + "**********");
			Query q2 = manager_diogene.createNamedQuery(
					"SitRttBollette.getBollettePagByCF").setParameter(
					"codFisc", persona.getCodfisc().toUpperCase());
			listaBollette.addAll(q2.getResultList());
			}

		} catch (Throwable t) {
			throw new RetteServiceException(t);
		}

		return listaBollette;
		
	}
	
	@Override
	public List<SitRttBollette> getListaBolletteNonPagateByCodFis(String codFiscale)
			throws RetteServiceException {

		List<SitRttBollette> listaBollette = new ArrayList<SitRttBollette>();
		
		try {
			//anche bollette familiari
			Query q = manager_diogene.createNamedQuery("SitDPersona.getFamigliaByCF");
			q.setParameter("codFisc", codFiscale.toUpperCase());
			List<SitDPersona> listaFamiliari = q.getResultList();
			
			for(SitDPersona persona: listaFamiliari){
				System.out.println("***********" + persona.getCodfisc() + "**********");
			Query q2 = manager_diogene.createNamedQuery(
					"SitRttBollette.getBolletteNonPagByCF").setParameter(
					"codFisc", persona.getCodfisc().toUpperCase());
			listaBollette.addAll(q2.getResultList());
			}

		} catch (Throwable t) {
			throw new RetteServiceException(t);
		}

		return listaBollette;
		
	}
	
	@Override
	public List<SitRttDettBollette> getListaDettaglioBolletteByCodBoll(String codBolletta)
			throws RetteServiceException {

		try {

			Query q = manager_diogene.createNamedQuery(
					"SitRttDettBollette.getBolletteDettByCodBoll").setParameter(
					"codBolletta", codBolletta);
			return q.getResultList();

		} catch (Throwable t) {
			throw new RetteServiceException(t);
		}

	}
	
	@Override
	public List<SitRttRateBollette> getListaRateBolletteByCodBoll(String codBolletta)
			throws RetteServiceException {

		try {

			Query q = manager_diogene.createNamedQuery(
					"SitRttRateBollette.getBolletteRateByCodBoll").setParameter(
					"codBolletta", codBolletta);
			return q.getResultList();

		} catch (Throwable t) {
			throw new RetteServiceException(t);
		}

	}

}
