package it.webred.cs.csa.ejb.dao;

import it.webred.cs.csa.ejb.CarSocialeBaseDAO;
import it.webred.cs.csa.ejb.dto.CasoSearchCriteria;
import it.webred.cs.data.model.CsAAnagrafica;
import it.webred.cs.data.model.CsACaso;
import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.data.model.CsASoggettoCategoriaSoc;
import it.webred.cs.data.model.CsASoggettoMedico;
import it.webred.cs.data.model.CsASoggettoStatoCivile;
import it.webred.cs.data.model.CsASoggettoStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

@Named
public class SoggettoDAO extends CarSocialeBaseDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public CsASoggetto getSoggettoByCaso(CsACaso caso){
		
		Query q = em.createNamedQuery("CsASoggetto.findByCaso");
		q.setParameter("casoId", caso.getId());
		List<CsASoggetto> lst =  q.getResultList();
		if(lst.size()>0)
			return lst.get(0);
		else 
			return null;
		
	}
	
	public CsASoggetto getSoggettoById(Long id) {

		CsASoggetto cs = em.find(CsASoggetto.class, id);
		return cs;
		
	}
	
	@SuppressWarnings("unchecked")
	public CsASoggetto getSoggettiByCF(String cf) {

		Query q = em.createNamedQuery("CsASoggetto.findByCF")
				.setParameter("cf", cf);
		List<CsASoggetto> lista = q.getResultList();
		if(lista.size() > 0)
			return lista.get(0);
		
		return null;

	}
	
	@SuppressWarnings("unchecked")
	public List<CsASoggetto> getSoggettiByDenominazione(String denominazione) {

		Query q = em.createNamedQuery("CsASoggetto.findByDenominazione")
				.setParameter("denominazione", denominazione);
		return q.getResultList();

	}
	
	public void saveSoggetto(CsASoggetto soggetto) {

		em.persist(soggetto);

	}
	
	public void updateSoggetto(CsASoggetto soggetto) {

		em.merge(soggetto);

	}
	
	@SuppressWarnings("unchecked")
	public List<CsASoggettoCategoriaSoc> getSoggettoCategorieBySoggetto(long idSoggetto) {

		Query q = em.createNamedQuery("CsASoggettoCategoriaSoc.findBySoggetto")
				.setParameter("idSoggetto", idSoggetto);
		return q.getResultList();

	}
	
	public void saveSoggettoCategoria(CsASoggettoCategoriaSoc cs) {

		em.persist(cs);

	}
	
	public void eliminaSoggettoCategorieBySoggetto(Long soggettoId) {
		
		Query q = em.createNamedQuery("CsASoggettoCategoriaSoc.eliminaBySoggettoId");
		q.setParameter("id", soggettoId);
		q.executeUpdate();
		
	}

	@SuppressWarnings("unchecked")
	public List<CsASoggetto> getCasiSoggetto(Integer first, Integer pagesize, CasoSearchCriteria criteria) {
		List<CsASoggetto> listaSogg = new ArrayList<CsASoggetto>();
		
		String sql = new SoggettoQueryBuilder(criteria).createQueryListaCasi(false);
		logger.debug("SQL LISTA CASI: " + sql);
		
		Query q = em.createNativeQuery(sql);
		if(first != null)
			q.setFirstResult(first);
		if(pagesize != null)
			q.setMaxResults(pagesize);
		List<Object[]> lista = q.getResultList();
		for(Object[] objArr: lista) {
			BigDecimal idAnagrafica = (BigDecimal) objArr[0];
			listaSogg.add(getSoggettoById(idAnagrafica.longValue()));
		}
		return listaSogg;
	}
	
	public Integer getCasiSoggettoCount(CasoSearchCriteria criteria) {
		String sql = new SoggettoQueryBuilder(criteria).createQueryListaCasi(true);
		Query q = em.createNativeQuery(sql);
		BigDecimal o = (BigDecimal) q.getSingleResult();
		return new Integer(o.intValue());
	}
	
	public Integer getCasiPerCategoriaCount(CasoSearchCriteria criteria) {
		String sql = new SoggettoQueryBuilder(criteria).createQueryCasiPerCategoria(true);
		Query q = em.createNativeQuery(sql);
		BigDecimal o = (BigDecimal) q.getSingleResult();
		return new Integer(o.intValue());
	}
	
	public CsAAnagrafica saveAnagrafica(CsAAnagrafica anagrafica) {

		em.persist(anagrafica);
		return anagrafica;

	}
	
	public CsAAnagrafica updateAnagrafica(CsAAnagrafica anagrafica) {

		em.merge(anagrafica);
		return anagrafica;

	}
	
	//Medici
	@SuppressWarnings("unchecked")
	public List<CsASoggettoMedico> getSoggettoMedicoBySoggetto(long idSoggetto) {

		Query q = em.createNamedQuery("CsASoggettoMedico.findBySoggetto")
				.setParameter("idSoggetto", idSoggetto);
		return q.getResultList();

	}
	
	public void saveSoggettoMedico(CsASoggettoMedico cs) {

		em.persist(cs);

	}
	
	public void updateSoggettoMedico(CsASoggettoMedico cs) {

		em.merge(cs);

	}
	
	public void eliminaSoggettoMedicoBySoggetto(Long soggettoId) {
		
		Query q = em.createNamedQuery("CsASoggettoMedico.eliminaBySoggettoId");
		q.setParameter("id", soggettoId);
		q.executeUpdate();
		
	}
	
	//Stato Civile
	@SuppressWarnings("unchecked")
	public List<CsASoggettoStatoCivile> getSoggettoStatoCivileBySoggetto(long idSoggetto) {

		Query q = em.createNamedQuery("CsASoggettoStatoCivile.findBySoggetto")
				.setParameter("idSoggetto", idSoggetto);
		return q.getResultList();

	}
	
	public void saveSoggettoStatoCivile(CsASoggettoStatoCivile cs) {

		em.persist(cs);

	}
	
	public void updateSoggettoStatoCivile(CsASoggettoStatoCivile cs) {

		em.merge(cs);

	}
	
	public void eliminaSoggettoStatoCivileBySoggetto(Long soggettoId) {
		
		Query q = em.createNamedQuery("CsASoggettoStatoCivile.eliminaBySoggettoId");
		q.setParameter("id", soggettoId);
		q.executeUpdate();
		
	}
	
	//Status
	@SuppressWarnings("unchecked")
	public List<CsASoggettoStatus> getSoggettoStatusBySoggetto(long idSoggetto) {

		Query q = em.createNamedQuery("CsASoggettoStatus.findBySoggetto")
				.setParameter("idSoggetto", idSoggetto);
		return q.getResultList();

	}
	
	public void saveSoggettoStatus(CsASoggettoStatus cs) {

		em.persist(cs);

	}
	
	public void updateSoggettoStatus(CsASoggettoStatus cs) {

		em.merge(cs);

	}
	
	public void eliminaSoggettoStatusBySoggetto(Long soggettoId) {
		
		Query q = em.createNamedQuery("CsASoggettoStatus.eliminaBySoggettoId");
		q.setParameter("id", soggettoId);
		q.executeUpdate();
		
	}

	@SuppressWarnings("unchecked")
	public List<CsAAnagrafica> findAnagraficaFamigliaByIdSoggetto(Long soggettoId) {
		Query q = em.createNamedQuery("CsASoggettoStatus.findAnagraficaFamigliaByIdSoggetto")
				.setParameter("soggettoId", soggettoId);
		return q.getResultList();
	}
}
