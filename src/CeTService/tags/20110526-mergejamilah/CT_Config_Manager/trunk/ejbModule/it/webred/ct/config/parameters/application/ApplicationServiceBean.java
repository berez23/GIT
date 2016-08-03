package it.webred.ct.config.parameters.application;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.Query;

import it.webred.ct.config.model.AmFonte;
import it.webred.ct.config.model.AmInstance;
import it.webred.ct.config.parameters.CommonServiceBean;
import it.webred.ct.config.parameters.comune.ComuneServiceException;
import it.webred.ct.config.parameters.fonte.FonteServiceException;

@Stateless
public class ApplicationServiceBean extends CommonServiceBean implements ApplicationService {

	public List<SelectItem> getListaApplication() {

		List<SelectItem> listSelect = new ArrayList<SelectItem>();

		try {
			logger.debug("LISTA APPLICATION");
			Query q = manager
					.createNativeQuery(new ApplicationQueryBuilder().createQueryListaApplication());
			List<Object[]> result = q.getResultList();
			for (Object[] rs : result) {
				
				/*MAPPATURA CAMPI
				* 0 value
				* 1 label
				* 2 è stato trovato un parametro non valorizzato*/
				String label = rs[2].equals('S')? (String) rs[1] + "*" : (String) rs[1];
				SelectItem item = new SelectItem((String) rs[0], label);
				listSelect.add(item);
				
			}

		} catch (Throwable t) {
			logger.error("", t);
			throw new ApplicationServiceException(t);
		}
		
		return listSelect;
		
	}
	
	
	public List<SelectItem> getListaInstanceByApplication(String application) {
		
		List<SelectItem> listSelect = new ArrayList<SelectItem>();

		try {
			logger.debug("LISTA INSTANCE");
			Query q = manager
					.createNativeQuery(new ApplicationQueryBuilder().createQueryListaInstance(application));
			List<Object[]> result = q.getResultList();
			for (Object[] rs : result) {
				
				String label = rs[2].equals('S')? (String) rs[1] + "*" : (String) rs[1];
				SelectItem item = new SelectItem((String) rs[0], label);
				listSelect.add(item);
				
			}

		} catch (Throwable t) {
			logger.error("", t);
			throw new ApplicationServiceException(t);
		}
		
		return listSelect;
	}
	
	
	public List<AmFonte> getListaFonte(String application) {

		try {
			logger.debug("LISTA FONTE");
			Query q = manager
					.createNamedQuery("AmFonte.getFonteByApplication");
			q.setParameter("application", application);
			return q.getResultList();

		} catch (Throwable t) {
			logger.error("", t);
			throw new FonteServiceException(t);
		}
		
	}

	public void saveInstance(AmInstance instance) {
		
		try {
			
			logger.debug("SAVE INSTANCE");
			manager.persist(instance);

		} catch (Throwable t) {
			logger.error("", t);
			throw new ComuneServiceException(t);
		}
		
	}

	
}
