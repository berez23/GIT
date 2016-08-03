package it.webred.amprofiler.ejb;

import it.webred.amprofiler.model.Anagrafica;
import it.webred.ct.config.model.AmApplication;
import it.webred.ct.config.model.AmFonte;
import it.webred.ct.config.model.AmInstance;
import it.webred.ct.config.model.AmKeyValue;
import it.webred.ct.config.model.AmKeyValueExt;
import it.webred.ct.config.model.AmSection;
import it.webred.ct.config.parameters.ParameterService;

import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Session Bean implementation class AmProfilerConfigSessionFacadeBean
 */
@Stateless
public class AmProfilerBaseService implements AmProfilerService {

	@PersistenceContext(unitName = "AmProfilerDataModel")
	EntityManager em;
	
	@EJB(mappedName = "CT_Service/ParameterBaseService/remote")
	protected ParameterService parameterService;

	/**
	 * Default constructor.
	 */
	public AmProfilerBaseService() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AmSection> findAmSections(String applicationName,
			Integer idFonte) {

		// ricerca per nome applicazione
		if (applicationName != null && !"".equals(applicationName)) {
			// senza id Fonte
			if (idFonte == null) {
				return em
						.createQuery(
								"select s from AmSection s where s.amApplication.name = :applicationName order by s.id")
						.setParameter("applicationName", applicationName)
						.getResultList();
			} else { // and id Fonte
				return em
						.createQuery(
								"select s from AmSection s where s.amApplication.name = :applicationName and s.amFonte.id = :idFonte order by s.id")
						.setParameter("applicationName", applicationName)
						.setParameter("idFonte", idFonte).getResultList();
			}
		} else {
			// ricerca per id fonte.
			if (idFonte != null && applicationName != null
					&& !"".equals(applicationName)) {
				return em
						.createQuery(
								"select s from AmSection s where s.amFonte.id = :idFonte order by s.id")
						.setParameter("idFonte", idFonte).getResultList();

			}
		}
		// sezioni Globali
		return em
				.createQuery(
						"select s from AmSection s where s.amApplication is null and s.amInstance is null and s.amComune is null order by s.id")
				.getResultList();

	}

	@Override
	public List<AmSection> findAmSections(String applicationName) {
		return findAmSections(applicationName, null);
	}

	@Override
	public List<AmSection> findAmSections(Integer idFonte) {
		return findAmSections(null, idFonte);
	}

	@Override
	public List<AmSection> findGlobalAmSections() {
		return findAmSections(null, null);
	}

	@Override
	public AmSection findAmSectionById(Integer id) {
		AmSection amSection = (AmSection) em.find(AmSection.class, id);
		if (amSection != null) {
			// Carico la relazione figlia Lazy.
			amSection.getAmKeyValues().size();
		}
		return amSection;
	}

	@Override
	public boolean createAmSection(AmSection amSection,
			String amApplicationName, Integer amIdFonte) {
		AmApplication amApplication = null;
		AmFonte amFonte = null;
		// ricerca applicazione
		if (amApplicationName != null && !"".equals(amApplicationName)) {
			amApplication = (AmApplication) em.find(AmApplication.class,
					amApplicationName);
			// errore se applicazione non esiste
			if (amApplication == null)
				return false;
		}
		// ricerca fonte
		if (amIdFonte != null && !"".equals(amIdFonte)) {
			amFonte = (AmFonte) em.find(AmFonte.class, amIdFonte);
			// errore se istanza non esiste
			if (amFonte == null)
				return false;
		}
		amSection.setAmApplication(amApplication);
		amSection.setAmFonte(amFonte);
		em.persist(amSection);

		return true;
	}

	@Override
	public void updateAmSection(AmSection amSection) {
		if (em.find(AmSection.class, amSection.getId()) != null) {
			em.merge(amSection);
		}
	}

	@Override
	public void deleteAmSection(Integer id) {
		AmSection amSection = em.find(AmSection.class, id);
		if (amSection != null) {
			em.remove(amSection);
		}
	}

	@Override
	public Anagrafica findAnagraficaByUserName(String userName) {
		// TODO Implementare il metodo per ritornare i dati corretti
		return new Anagrafica();
	}

	@Override
	public Anagrafica findAnagraficaById(Integer id) {
		// TODO Implementare il metodo per ritornare i dati corretti
		return new Anagrafica();
	}

	@Override
	public List<Anagrafica> findAnagraficaByCodiceFiscale(String userName,
			String codiceFiscale) {
		// TODO Implementare il metodo per ritornare i dati corretti

		return new LinkedList<Anagrafica>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AmKeyValueExt> findAmKeyValueExtByComune(String comune) {
		List<AmKeyValueExt> result = em.createNamedQuery(
				"AmKeyValueExt.getAmKeyValueExtByComune").setParameter(
				"comune", comune).getResultList();
		for (AmKeyValueExt kve : result) {
			kve.setAmSection(parameterService.getAmSection(kve.getSectionName(), kve.getFkAmApplication()));
		}
		List<AmKeyValue> lista = em.createNamedQuery(
				"AmKeyValue.getAmKeyValue99ByComune").setParameter("comune",
				comune).getResultList();
		for (AmKeyValue kv : lista) {
			AmKeyValueExt kve = new AmKeyValueExt();
			kve.setKeyConf(kv.getKey());
			kve.setValueConf(kv.getValue());
			kve.setAmSection(parameterService.getAmSection(kve.getSectionName(), kve.getFkAmApplication()));
			kve.setFkAmFonte(kv.getAmSection().getAmFonte().getId());
			result.add(kve);
		}
		return result;
	}

	@Override
	public List<AmKeyValueExt> findAmKeyValueExtByFonte(Integer idFonte) {
		return parameterService.getListaKeyValueExt99(idFonte, null,
				em);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AmKeyValueExt> findAmKeyValueExtByFonteComune(String comune,
			Integer idFonte) {
		List<AmKeyValueExt> result = em.createNamedQuery(
				"AmKeyValueExt.getAmKeyValueExtByFonteComune").setParameter(
				"comune", comune).setParameter("fonte", idFonte)
				.getResultList();
		for (AmKeyValueExt kve : result) {
			kve.setAmSection(parameterService.getAmSection(kve.getSectionName(), kve.getFkAmApplication()));
		}
		result.addAll(findAmKeyValueExtByFonte(idFonte));
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AmKeyValueExt> findAmKeyValueExtByInstance(String instanceName) {
		List<AmKeyValueExt> result = em.createNamedQuery(
				"AmKeyValueExt.getAmKeyValueExtByInstance").setParameter(
				"instance", instanceName).getResultList();
		AmInstance i = (AmInstance) em.createNamedQuery(
				"AmInstance.getInstanceById").setParameter("id", instanceName)
				.getSingleResult();
		for (AmKeyValueExt kve : result) {
			kve.setAmSection(parameterService.getAmSection(kve.getSectionName(), kve.getFkAmApplication()));
		}
		result.addAll(parameterService.getListaKeyValueExt99(null, i
				.getFkAmApplication(), em));
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AmKeyValueExt> findAmKeyValueExtByInstanceComune(String comune,
			String instanceName) {
		List<AmKeyValueExt> result = em.createNamedQuery(
				"AmKeyValueExt.getAmKeyValueExtByInstanceComune").setParameter(
				"comune", comune).setParameter("instance", instanceName)
				.getResultList();
		for (AmKeyValueExt kve : result) {
			kve.setAmSection(parameterService.getAmSection(kve.getSectionName(), kve.getFkAmApplication()));
		}
		result.addAll(findAmKeyValueExtByInstance(instanceName));
		return result;
	}

	@Override
	public AmKeyValueExt getAmKeyValueExtByKeyFonteComune(String key,
			String comune, String fonte) {

		return parameterService.getAmKeyValueExtByKeyFonteComune(key, comune, fonte);
		
	}

}
