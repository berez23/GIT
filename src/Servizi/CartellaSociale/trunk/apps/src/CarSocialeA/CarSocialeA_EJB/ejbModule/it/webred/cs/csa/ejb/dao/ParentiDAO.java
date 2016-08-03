package it.webred.cs.csa.ejb.dao;

import it.webred.cs.csa.ejb.CarSocialeBaseDAO;
import it.webred.cs.data.model.CsAComponenteGit;
import it.webred.cs.data.model.CsAFamigliaGruppo;
import it.webred.cs.data.model.CsAFamigliaGruppoGit;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

/**
 * @author Andrea
 *
 */
@Named
public class ParentiDAO extends CarSocialeBaseDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public CsAFamigliaGruppoGit getFamigliaGruppoGit(Long idSoggetto) {
			
		Query q = em.createNamedQuery("CsAFamigliaGruppoGit.getFamigliaGruppoGitBySoggettoId")
				.setParameter("idSoggetto", idSoggetto);
		List<CsAFamigliaGruppoGit> lista = q.getResultList();
		if(lista != null && lista.size() > 0)
			return lista.get(0);
			
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public CsAFamigliaGruppo getFamigliaGruppo(Long idSoggetto) {
			
		Query q = em.createNamedQuery("CsAFamigliaGruppo.getFamigliaGruppoBySoggettoId")
				.setParameter("idSoggetto", idSoggetto);
		List<CsAFamigliaGruppo> lista = q.getResultList();
		if(lista != null && lista.size() > 0)
			return lista.get(0);
			
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsAFamigliaGruppoGit> getFamiglieGruppoGitAggiornate() {
		
		Query q = em.createNamedQuery("CsAFamigliaGruppoGit.getFamigliaGruppoGitAggiornate");
		return q.getResultList();
	
	}
	
	public void saveComponenteGit(CsAComponenteGit comp) {

		em.persist(comp);

	}
	
	public void updateComponenteGit(CsAComponenteGit comp) {

		em.merge(comp);

	}
	
	public CsAFamigliaGruppoGit saveFamigliaGruppoGit(CsAFamigliaGruppoGit fam) {

		em.persist(fam);
		return fam;

	}
	
	public void updateFamigliaGruppoGit(CsAFamigliaGruppoGit fam) {

		em.merge(fam);

	}
	
}
