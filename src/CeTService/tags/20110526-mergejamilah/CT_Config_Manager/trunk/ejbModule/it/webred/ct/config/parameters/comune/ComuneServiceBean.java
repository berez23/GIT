package it.webred.ct.config.parameters.comune;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.Query;

import it.webred.ct.config.model.*;
import it.webred.ct.config.parameters.CommonServiceBean;

@Stateless
public class ComuneServiceBean extends CommonServiceBean implements
		ComuneService {

	public List<AmComune> getListaComuneByUsername(String user) {

		try {
			logger.debug("LISTA COMUNE BY USER");
			return manager.createNamedQuery("AmComune.getComuneByUser")
					.setParameter("user", user).getResultList();

		} catch (Throwable t) {
			logger.error("", t);
			throw new ComuneServiceException(t);
		}

	}

	public List<AmComune> getListaComune() {

		try {
			logger.debug("LISTA COMUNE");
			return manager.createNamedQuery("AmComune.getComuni")
					.getResultList();

		} catch (Throwable t) {
			logger.error("", t);
			throw new ComuneServiceException(t);
		}

	}

	public List<SelectItem> getListaComune(boolean visMissingParam) {

		List<SelectItem> listSelect = new ArrayList<SelectItem>();

		try {
			logger.debug("LISTA COMUNE");
			Query q = manager.createNativeQuery(new ComuneQueryBuilder()
					.createQueryListaComune());
			List<Object[]> result = q.getResultList();
			for (Object[] rs : result) {

				/*
				 * MAPPATURA CAMPI 0 value 1 label 2 è stato trovato un
				 * parametro non valorizzato
				 */
				String label = rs[2].equals('S') ? (String) rs[1]
						+ (visMissingParam ? "*" : "") : (String) rs[1];
				SelectItem item = new SelectItem((String) rs[0], label);
				listSelect.add(item);

			}

		} catch (Throwable t) {
			logger.error("", t);
			throw new ComuneServiceException(t);
		}

		return listSelect;

	}

	public List<SelectItem> getListaInstanceByComune(String comune,
			boolean visMissingParam) {

		List<SelectItem> listSelect = new ArrayList<SelectItem>();

		try {
			logger.debug("LISTA INSTANCE COMUNE");
			Query q = manager.createNativeQuery(new ComuneQueryBuilder()
					.createQueryListaInstanceComune(comune));
			List<Object[]> result = q.getResultList();
			for (Object[] rs : result) {

				String label = rs[2].equals('S') ? (String) rs[1]
						+ (visMissingParam ? "*" : "") : (String) rs[1];
				SelectItem item = new SelectItem((String) rs[0], label);
				listSelect.add(item);

			}

		} catch (Throwable t) {
			logger.error("", t);
			throw new ComuneServiceException(t);
		}

		return listSelect;
	}

	public List<SelectItem> getListaFonteByComune(String comune,
			boolean visMissingParam) {

		List<SelectItem> listSelect = new ArrayList<SelectItem>();

		try {
			logger.debug("LISTA FONTE COMUNE");
			Query q = manager.createNativeQuery(new ComuneQueryBuilder()
					.createQueryListaFonteComune(comune));
			List<Object[]> result = q.getResultList();
			for (Object[] rs : result) {

				String label = rs[2].equals('S') ? (String) rs[1]
						+ (visMissingParam ? "*" : "") : (String) rs[1];
				SelectItem item = new SelectItem(((BigDecimal) rs[0])
						.toString(), label);
				listSelect.add(item);

			}

		} catch (Throwable t) {
			logger.error("", t);
			throw new ComuneServiceException(t);
		}

		return listSelect;

	}

	public List<AmFonteComune> getListaFonteByComune(String comune) {

		try {

			logger.debug("LISTA FONTE COMUNE");
			Query q = manager
					.createNamedQuery("AmFonteComune.getFonteByComune");
			q.setParameter("comune", comune);
			return q.getResultList();

		} catch (Throwable t) {
			logger.error("", t);
			throw new ComuneServiceException(t);
		}

	}

	public List<AmInstance> getListaInstance() {

		try {
			logger.debug("LISTA INSTANCE");
			Query q = manager.createNamedQuery("AmInstance.getInstance");
			return q.getResultList();

		} catch (Throwable t) {
			logger.error("", t);
			throw new ComuneServiceException(t);
		}

	}

	public AmComune getComune(String belfiore) {

		try {
			logger.debug("COMUNE BY BELFIORE");
			Query q = manager.createNamedQuery("AmComune.getComuneById");
			q.setParameter("id", belfiore);
			return (AmComune) q.getSingleResult();

		} catch (Throwable t) {
			logger.error("", t);
			throw new ComuneServiceException(t);
		}

	}

	public AmInstance getInstanceByname(String instance) {

		try {
			logger.debug("INSTANCE BY NAME");
			Query q = manager.createNamedQuery("AmInstance.getInstanceById");
			return (AmInstance) q.setParameter("id", instance)
					.getSingleResult();

		} catch (Throwable t) {
			logger.error("", t);
			throw new ComuneServiceException(t);
		}

	}

	public AmFonteComune getFonteComuneByComuneFonte(String comune,
			Integer fonte) {

		try {
			logger.debug("FONTECOMUNE BY COMUNE AND FONTE");
			Query q = manager
					.createNamedQuery("AmFonteComune.getFonteByComuneFonte");
			q.setParameter("comune", comune);
			q.setParameter("fonte", fonte.toString());
			List<AmFonteComune> lista = q.getResultList();
			if (lista.size() > 0)
				return lista.get(0);
			else
				return null;

		} catch (Throwable t) {
			logger.error("", t);
			throw new ComuneServiceException(t);
		}

	}

	public AmInstanceComune getInstanceComuneByComuneInstance(String comune,
			String instance) {

		try {
			logger.debug("INSTANCECOMUNE BY COMUNE AND INSTANCE");
			Query q = manager
					.createNamedQuery("AmInstanceComune.getInstanceByComuneInstance");
			q.setParameter("comune", comune);
			q.setParameter("instance", instance);
			List<AmInstanceComune> lista = q.getResultList();
			if (lista.size() > 0)
				return lista.get(0);
			else
				return null;

		} catch (Throwable t) {
			logger.error("", t);
			throw new ComuneServiceException(t);
		}

	}

	public List<AmFonte> getListaFonte() {

		try {
			logger.debug("LISTA FONTE");
			Query q = manager.createNamedQuery("AmFonte.getFonte");
			return q.getResultList();

		} catch (Throwable t) {
			logger.error("", t);
			throw new ComuneServiceException(t);
		}

	}

	public void saveComune(AmComune comune) {

		try {

			logger.debug("SAVE COMUNE");
			manager.persist(comune);

		} catch (Throwable t) {
			logger.error("", t);
			throw new ComuneServiceException(t);
		}

	}

	public void saveFonteComune(AmFonteComune fonteComune) {

		try {

			logger.debug("SAVE FONTE COMUNE");
			manager.persist(fonteComune);

		} catch (Throwable t) {
			logger.error("", t);
			throw new ComuneServiceException(t);
		}

	}

	public void saveInstanceComune(AmInstanceComune instanceComune) {

		try {

			logger.debug("SAVE INSTANCE COMUNE");
			manager.persist(instanceComune);

		} catch (Throwable t) {
			logger.error("", t);
			throw new ComuneServiceException(t);
		}

	}

	public void deleteFonteComune(AmFonteComune fonteComune) {

		try {

			logger.debug("DELETE FONTE COMUNE");
			AmFonteComune afc = getFonteComuneByComuneFonte(fonteComune.getId()
					.getFkAmComune(), fonteComune.getAmFonte().getId());
			manager.remove(afc);

		} catch (Throwable t) {
			logger.error("", t);
			throw new ComuneServiceException(t);
		}

	}

	public void deleteInstanceComune(AmInstanceComune instanceComune) {

		try {

			logger.debug("DELETE INSTANCE COMUNE");
			AmInstanceComune aic = getInstanceComuneByComuneInstance(
					instanceComune.getId().getFkAmComune(), instanceComune
							.getId().getFkAmInstance());
			manager.remove(aic);

		} catch (Throwable t) {
			logger.error("", t);
			throw new ComuneServiceException(t);
		}

	}

}
