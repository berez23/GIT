package it.webred.cs.csa.ejb.dao;

import it.webred.cs.csa.ejb.CarSocialeBaseDAO;
import it.webred.cs.data.model.CsACaso;
import it.webred.cs.data.model.CsACasoOpeTipoOpe;
import it.webred.cs.data.model.CsOOperatoreTipoOperatore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

/**
 * @author Andrea
 *
 */
@Named
public class CasoDAO extends CarSocialeBaseDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	public CsACaso findCasoById(Long idCaso) throws Exception {
		CsACaso caso = em.find(CsACaso.class, idCaso);
		return caso;
	}

	public void createCaso(CsACaso newCaso) throws Exception {
		em.persist(newCaso);
	}

	public void updateCaso(CsACaso caso) throws Exception {
		em.merge(caso);
	}
	
	@SuppressWarnings("unchecked")
	public List<CsACaso> findAll(){
		Query q = em.createNamedQuery("CsACaso.findAll");
		return q.getResultList();
	}

	public CsOOperatoreTipoOperatore findOperatoreTipoOperatoreByOpSettore(long idOperatore, long idSettore) throws Exception {
		Query q = em.createNamedQuery("CsOOperatoreTipoOperatore.findOpTipoOpByOpSettore");
		q.setParameter("operatoreId", idOperatore);
		q.setParameter("settoreId", idSettore);
		List<CsOOperatoreTipoOperatore> list = q.getResultList();
		if(!list.isEmpty())
			return list.get(0);
		return null;
	}
	
	public CsACasoOpeTipoOpe findResponsabile(long idCaso) throws Exception {
		Query q = em.createNamedQuery("CsACasoOpeTipoOpe.findResponsabileByCaso");
		q.setParameter("casoId", idCaso);
		List<CsACasoOpeTipoOpe> list = q.getResultList();
		if(!list.isEmpty())
			return list.get(0);
		return null;
	}

	public void setDataModifica(long idCaso) throws Exception {
		CsACaso caso = findCasoById( idCaso );
		caso.setDtMod(new Date());
		em.merge(caso);
	}
	

	public void salvaOperatoreTipoOpCaso(CsACasoOpeTipoOpe oper) throws Exception {
		em.persist(oper);
	}
	
	public void updateOperatoreTipoOpCaso(CsACasoOpeTipoOpe oper) throws Exception {
		em.merge(oper);
	}

	public void eliminaOperatoreTipoOpByCasoId(Long obj) {
		Query q = em.createNamedQuery("CsACasoOpeTipoOpe.deleteByCasoId");
		q.setParameter("casoId", obj);
		q.executeUpdate();
		
	}

	public List<CsACasoOpeTipoOpe> getListaOperatoreTipoOpByCasoId(Object obj) {
		Query q = em.createNamedQuery("CsACasoOpeTipoOpe.findByCasoId");
		q.setParameter("casoId", obj);
		return q.getResultList();
	}
	
	public CsACasoOpeTipoOpe findCasoOpeTipoOpeByOpSettore(long idOperatore, long idSettore) throws Exception {
		Query q = em.createNamedQuery("CsACasoOpeTipoOpe.findCasoOpeTipoOpeByOpSettore");
		q.setParameter("operatoreId", idOperatore);
		q.setParameter("settoreId", idSettore);
		List<CsACasoOpeTipoOpe> list = q.getResultList();
		if(!list.isEmpty())
			return list.get(0);
		return null;
	}
	
	public Integer countCasiByResponsabileCatSociale(long idOperatore, long idCatSociale) throws Exception {
		Query q = em.createNamedQuery("CsACasoOpeTipoOpe.countCasiByResponsabileCatSociale");
		q.setParameter("operatoreId", idOperatore);
		q.setParameter("catSocialeId", idCatSociale);
		Long c = (Long) q.getSingleResult();
		return c.intValue();
	}
	
}
