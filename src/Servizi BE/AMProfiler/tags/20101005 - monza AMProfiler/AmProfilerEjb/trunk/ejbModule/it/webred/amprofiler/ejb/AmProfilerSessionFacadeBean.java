package it.webred.amprofiler.ejb;

import it.webred.amprofiler.model.AmApplication;
import it.webred.amprofiler.model.AmComune;
import it.webred.amprofiler.model.AmSection;
import it.webred.amprofiler.model.Anagrafica;

import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class AmProfilerConfigSessionFacadeBean
 */
@Stateless
public class AmProfilerSessionFacadeBean implements AmProfilerSessionFacadeBeanRemote, AmProfilerSessionFacadeBeanLocal {

	@PersistenceContext(unitName = "AmProfilerDataModel")
	EntityManager em;

	
	/**
     * Default constructor. 
     */
    public AmProfilerSessionFacadeBean() {
        // TODO Auto-generated constructor stub
    }
	@SuppressWarnings("unchecked")
	@Override
	public List<AmSection> findAmSections(String amApplicationName,String amComuneName) {
		if (amApplicationName != null && !"".equals(amApplicationName)) {
			return em
					.createQuery( 
							"select s from AmSection s where s.amComune is null and s.amApplication.name = :amApplicationName order by s.id")
					.setParameter("amApplicationName", amApplicationName).getResultList();
		} else if (amComuneName != null && !"".equals(amComuneName))  {
			return em
					.createQuery( 
							"select s from AmSection s where s.amComune.name = :amComuneName and s.amApplication is null order by s.id")
					.setParameter("amComuneName", amComuneName).getResultList();
		} else { 
			return em
					.createQuery( 
							"select s from AmSection s where s.amComune is null and s.amApplication is null order by s.id")
					.setParameter("amComuneName", amComuneName).getResultList();
		}
	}
	@Override
	public AmSection findAmSectionById(Integer id) {
		AmSection amSection =(AmSection)em.find(AmSection.class, id);
		if (amSection != null) {
			//Carico la relazione figlia Lazy.
			amSection.getAmKeyValues().size();
		}		
		return amSection;
	}

	@Override
	public boolean createAmSection(AmSection amSection, String amApplicationName, String amComuneName ) {
		AmApplication amApplication=null;
		AmComune amComune=null;
		if (amApplicationName!=null && !"".equals(amApplicationName)){
			amApplication =(AmApplication)em.find(AmApplication.class, amApplicationName);
			if (amApplication==null)return false;
		}else{
			if(amComuneName!=null && !"".equals(amComuneName)){
				amComune =(AmComune)em.find(AmComune.class, amComuneName);
				if (amComune==null)return false;
			}
		}
		amSection.setAmApplication(amApplication);
		amSection.setAmComune(amComune);
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
		AmSection amSection = em.find(AmSection.class,id);
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
	public List<Anagrafica> findAnagraficaByCodiceFiscale(String codiceFiscale) {
		// TODO Implementare il metodo per ritornare i dati corretti
		
		return new LinkedList<Anagrafica>(); 
	}

}
