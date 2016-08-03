package it.webred.ct.config.parameters.fonte;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import it.webred.ct.config.model.AmFonte;
import it.webred.ct.config.model.AmFonteTipoinfo;
import it.webred.ct.config.parameters.CommonServiceBean;


@Stateless
public class FonteServiceBean extends CommonServiceBean implements FonteService {

	
	public List<AmFonte> getListaFonte() {

		try {
			logger.debug("LISTA FONTE");
			Query q = manager
					.createNamedQuery("AmFonte.getFonteBySection");
			return q.getResultList();

		} catch (Throwable t) {
			logger.error("", t);
			throw new FonteServiceException(t);
		}
		
	}
	
	
	public List<AmFonteTipoinfo> getListaFonteTipoinfoByFonte(String fonte) {
		
		try {
			logger.debug("LISTA FONTETIPOINFO");
			
			Query q = manager
					.createNamedQuery("AmFonteTipoinfo.getFonteTipoinfoByFonte");
			q.setParameter("fonte", new Integer(fonte));
			return q.getResultList();

		} catch (Throwable t) {
			logger.error("", t);
			throw new FonteServiceException(t);
		}
	}


	public AmFonte getFonte(Long idFonte) {
		
		try {
			logger.debug("AMFONTE BY ID");

			Query q = manager.createNamedQuery("AmFonte.getFonteById");
			q.setParameter("idFonte", idFonte.intValue());
			return (AmFonte) q.getSingleResult();

		} catch (Throwable t) {
			logger.error("", t);
			throw new FonteServiceException(t);
		}
	}


	public List<AmFonte> getListaFonteAll() {
		try {
			logger.debug("LISTA FONTI");
			Query q = manager.createNamedQuery("AmFonte.getFonte");
			return q.getResultList();

		} catch (Throwable t) {
			logger.error("", t);
			throw new FonteServiceException(t);
		}
	}
	
}
