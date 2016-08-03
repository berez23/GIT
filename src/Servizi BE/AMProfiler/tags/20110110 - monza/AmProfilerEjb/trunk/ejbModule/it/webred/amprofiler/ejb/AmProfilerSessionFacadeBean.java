package it.webred.amprofiler.ejb;

import it.webred.amprofiler.model.Anagrafica;
import it.webred.ct.config.model.AmKeyValueExt;
import it.webred.ct.config.model.AmSection;

import java.util.List;

import javax.ejb.Stateless;

/**
 * Session Bean implementation class AmProfilerConfigSessionFacadeBean
 */
@Stateless
public class AmProfilerSessionFacadeBean implements
		AmProfilerSessionFacadeBeanRemote, AmProfilerSessionFacadeBeanLocal {


	private AmProfilerBaseService amProfilerBaseService;

	/**
	 * Default constructor.
	 */
	public AmProfilerSessionFacadeBean() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AmSection> findAmSections(String applicationName,
			Integer idFonte) {

		return amProfilerBaseService.findAmSections(applicationName, idFonte);

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

		return amProfilerBaseService.findAmSectionById(id);
		
	}

	@Override
	public boolean createAmSection(AmSection amSection,
			String amApplicationName, Integer amIdFonte) {
		
		return amProfilerBaseService.createAmSection(amSection, amApplicationName, amIdFonte);
		
	}

	@Override
	public void updateAmSection(AmSection amSection) {
		
		amProfilerBaseService.updateAmSection(amSection);
		
	}

	@Override
	public void deleteAmSection(Integer id) {
		
		amProfilerBaseService.deleteAmSection(id);
		
	}

	@Override
	public Anagrafica findAnagraficaByUserName(String userName) {
		
		return amProfilerBaseService.findAnagraficaByUserName(userName);
	
	}

	@Override
	public Anagrafica findAnagraficaById(Integer id) {
		
		return amProfilerBaseService.findAnagraficaById(id);
	
	}

	@Override
	public List<Anagrafica> findAnagraficaByCodiceFiscale(String userName,
			String codiceFiscale) {
		
		return amProfilerBaseService.findAnagraficaByCodiceFiscale(userName, codiceFiscale);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AmKeyValueExt> findAmKeyValueExtByComune(String comune) {
		
		return amProfilerBaseService.findAmKeyValueExtByComune(comune);
		
	}

	@Override
	public List<AmKeyValueExt> findAmKeyValueExtByFonte(Integer idFonte) {

		return amProfilerBaseService.findAmKeyValueExtByFonte(idFonte);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AmKeyValueExt> findAmKeyValueExtByFonteComune(String comune,
			Integer idFonte) {
		
		return amProfilerBaseService.findAmKeyValueExtByFonteComune(comune, idFonte);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AmKeyValueExt> findAmKeyValueExtByInstance(String instanceName) {
				
		return amProfilerBaseService.findAmKeyValueExtByInstance(instanceName);
				
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AmKeyValueExt> findAmKeyValueExtByInstanceComune(String comune,
			String instanceName) {
		
		return amProfilerBaseService.findAmKeyValueExtByInstanceComune(comune, instanceName);
		
	}

	@Override
	public AmKeyValueExt getAmKeyValueExtByKeyFonteComune(String key, String comune,
			String fonte) {

		return amProfilerBaseService.getAmKeyValueExtByKeyFonteComune(key, comune, fonte);
		
	}

}
