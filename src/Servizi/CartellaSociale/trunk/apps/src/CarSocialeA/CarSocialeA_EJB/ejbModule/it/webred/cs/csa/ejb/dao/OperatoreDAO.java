package it.webred.cs.csa.ejb.dao;

import it.webred.cs.csa.ejb.CarSocialeBaseDAO;
import it.webred.cs.data.model.CsOOperatore;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.data.model.CsOOperatoreTipoOperatore;
import it.webred.cs.data.model.CsOOrganizzazione;
import it.webred.cs.data.model.CsOSettore;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

/**
 * @author Andrea
 *
 */
@Named
public class OperatoreDAO extends CarSocialeBaseDAO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public CsOOrganizzazione findOrganizzazioneById( long id ) throws Exception	{
		CsOOrganizzazione organizzazione = em.find(CsOOrganizzazione.class,  id);
		return organizzazione;
	}
	
	public CsOSettore findSettoreById(long id) throws Exception {
			CsOSettore settore = em.find(CsOSettore.class, id);
			return settore;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CsOOperatoreTipoOperatore> getOperatoriByTipoId(Long id) {
			
		Query q = em.createNamedQuery("CsOOperatore.findOperatoriByTipoId");
		q.setParameter("id", id);
		return q.getResultList();
			
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CsOOperatore> getOperatoriByTipoDescrizione(String descrizione) {
			
		Query q = em.createNamedQuery("CsOOperatore.findOperatoriByTipoDescrizione");
		q.setParameter("descrizione", descrizione);
		List result = q.getResultList();
		if( result != null && result.size() > 0 )
			return result;
		
		return null;

	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CsOOperatore> getOperatoriAll() {
			
		Query q = em.createNamedQuery("CsOOperatore.findAll");
		return q.getResultList();

	}
	
	@SuppressWarnings("rawtypes")
	public CsOOperatoreTipoOperatore getTipoByOperatoreSettore(Long idOpSet, Date datFinApp) {
			
		Query q = em.createNamedQuery("CsOOperatore.findTipoByOperatoreSettore");
		q.setParameter("idOpSet", idOpSet);
		q.setParameter("dataFineApp",  datFinApp);
		List result = q.getResultList();
		if( result != null && result.size() > 0 )
			return (CsOOperatoreTipoOperatore)result.get(0);
		
		return null;

	}	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CsOOperatore> getOperatoriByCatSociale(Long idCatSociale) {
			
		Query q = em.createNamedQuery("CsOOperatore.findOperatoriByCatSociale");
		q.setParameter("idCatSociale", idCatSociale);
		List result = q.getResultList();
		if( result != null && result.size() > 0 )
			return result;
		
		return null;

	}
	
	@SuppressWarnings("rawtypes")
	public CsOOperatoreSettore findOperatoreSettoreById(long idOp, long idSettore, Date date) throws Exception {
		Query q = em.createNamedQuery("CsOOperatoreSettore.getOperatoreSettoreById").setParameter("idOp", idOp).setParameter("date", date).setParameter("idSettore", idSettore);
		List result = q.getResultList();
		
		if( result != null && result.size() > 0 )
			return (CsOOperatoreSettore)result.get(0);
		
		return null;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public List<CsOOperatoreSettore> findOperatoreSettoreByOperatore(long idOp, Date date) throws Exception {
		Query q = em.createNamedQuery("CsOOperatoreSettore.findOperatoreSettoreByOperatore").setParameter("idOp", idOp).setParameter("date", date);
		List results = q.getResultList();
		return results;
	}

	@SuppressWarnings("rawtypes")
	public CsOOperatore findOperatoreByUsername(String username) throws Exception {
		if( StringUtils.isEmpty( username ) )
			return null;
		
		Query q = em.createNamedQuery("CsOOperatore.getOperatoreByUsername")
				.setParameter("username", username);
		List result = q.getResultList();
		
		if( result != null && result.size() > 0 )
			return (CsOOperatore)result.get(0);
			
		return null;
	}
	
	public CsOOperatore findOperatoreById(long idOp) throws Exception {
		Query op = em.createNamedQuery("CsOOperatore.getOperatoreById")
				.setParameter("idOp", idOp);

		return (CsOOperatore) op.getSingleResult();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<CsOOperatoreSettore> findOperatoreSettori() throws Exception {
		Query q = em.createNamedQuery("CsOOperatoreSettore.findAll");
		List results = q.getResultList();
		return results;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CsOOperatoreSettore> findOperatoreSettoreBySettore(long idSettore) throws Exception {
		Query q = em.createNamedQuery("CsOOperatoreSettore.findOperatoreSettoreBySettore").setParameter("idSettore", idSettore);
		List results = q.getResultList();
		return results;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CsOSettore> findSettori() throws Exception {
		Query q = em.createNamedQuery("CsOSettore.findAll");
		List results = q.getResultList();
		return results;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CsOSettore> findSettoreByOrganizzazione(long idOrganizzazione) throws Exception {
		Query q = em.createNamedQuery("CsOSettore.findSettoreByOrganizzazione").setParameter("idOrganizzazione", idOrganizzazione);
		List results = q.getResultList();
		return results;
	}
	
	public CsOOperatoreTipoOperatore findCsOOperatoreTipoOpById(Long obj) {
		return em.find(CsOOperatoreTipoOperatore.class, obj);
	}
	
	public CsOOperatore insertOrUpdateOperatore(CsOOperatore op, boolean update) throws Exception {
		if (update) {
			em.merge(op);
		} else {
			em.persist(op);
		}
		return op;
	}
	
	public CsOOperatoreTipoOperatore insertOrUpdateTipoOperatore(CsOOperatoreTipoOperatore tipoOp, boolean update) throws Exception {
		if (update) {
			em.merge(tipoOp);
		} else {
			em.persist(tipoOp);
		}
		return tipoOp;
	}
	
	public CsOOperatoreSettore insertOrUpdateOperatoreSettore(CsOOperatoreSettore opSet, boolean update) throws Exception {
		if (update) {
			em.merge(opSet);
		} else {
			em.persist(opSet);
		}
		return opSet;
	}
	
	public void deleteTipoOperatore(CsOOperatoreTipoOperatore tipoOp) throws Exception {
		Query q = em.createNamedQuery("CsOOperatoreTipoOperatore.eliminaTipoOperatoreById");
		q.setParameter("id", tipoOp.getId());
		q.executeUpdate();
	}
	
	public void deleteOperatoreSettore(CsOOperatoreSettore opSet) throws Exception {
		Query q = em.createNamedQuery("CsACasoOpeTipoOpe.eliminaCasoOpeTipoOpeByIdOperatoreSettore");
		q.setParameter("idOpSet", opSet.getId());
		q.executeUpdate();
		
		q = em.createNamedQuery("CsOOperatoreTipoOperatore.eliminaTipoOperatoreByIdOperatoreSettore");
		q.setParameter("idOpSet", opSet.getId());
		q.executeUpdate();
		
		q = em.createNamedQuery("CsOOperatoreSettore.eliminaOperatoreSettoreById");
		q.setParameter("id", opSet.getId());
		q.executeUpdate();
	}
	
}
