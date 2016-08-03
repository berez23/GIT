package it.webred.cs.csa.ejb.dao;

import it.webred.cs.csa.ejb.CarSocialeBaseDAO;
import it.webred.cs.data.model.CsSsSchedaSegr;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

/**
 * @author Andrea
 *
 */
@Named
public class SchedaSegrDAO extends CarSocialeBaseDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	public CsSsSchedaSegr findSchedaSegrById(Long id) {
		CsSsSchedaSegr scheda = em.find(CsSsSchedaSegr.class, id);
		return scheda;
	}

	public CsSsSchedaSegr findSchedaSegrByIdAnagrafica(Long idAnagrafica) {
		Query q = em.createNamedQuery("CsSsSchedaSegr.findByIdAnagrafica")
				.setParameter("idAnagrafica", idAnagrafica);
		List<CsSsSchedaSegr> lista = q.getResultList();
		if(lista != null && lista.size() > 0)
			return lista.get(0);
			
		return null;
	}
	
	public void saveSchedaSegr(CsSsSchedaSegr newScheda) {
		em.persist(newScheda);
	}
	
	public void updateSchedaSegr(CsSsSchedaSegr scheda) {
		em.merge(scheda);
	}

	@SuppressWarnings("unchecked")
	public List<CsSsSchedaSegr> getSchedeSegr(Integer first, Integer pagesize, boolean onlyNew, Long idSettore, Long idAnagrafica, String enteId) {
		String sql = getQuery(false, onlyNew, idSettore, idAnagrafica);
		logger.debug("ListaSchedeSegretariato - SQL["+sql+"]");
		Query q = em.createQuery(sql);
		if(first != null) {
			q.setFirstResult(first);
			q.setMaxResults(pagesize);
		}
		return q.getResultList();
	}
	
	public Integer getSchedeSegrCount(boolean onlyNew, Long idSettore) {
		
		Query q = em.createQuery(getQuery(true, onlyNew, idSettore, null));
		Long o = (Long) q.getSingleResult();
		return new Integer(o.intValue());
	}
	
	private String getQuery(boolean count, boolean onlyNew, Long idSettore, Long idAnagrafica) {
		
		String sql = "";
		
		if(count)
			sql += "SELECT COUNT(ss) ";
		else sql += "SELECT DISTINCT ss ";
		
		sql += " FROM CsSsSchedaSegr ss, CsRelSettoreCatsoc cs " +
				" WHERE ss.csCCategoriaSociale.id = cs.id.categoriaSocialeId "+
				" AND ss.codEnte=cs.csOSettore.csOOrganizzazione.belfiore ";
		
		if(onlyNew)
			sql += " AND ss.csASoggetto is null ";
		if(idSettore != null)
			sql += " AND cs.id.settoreId = " + idSettore;
		if(idAnagrafica != null)
			sql += " AND ss.csASoggetto.anagraficaId = " + idAnagrafica;
		if(!count)
			sql += " ORDER BY (CASE WHEN ss.csASoggetto IS NULL THEN 0 ELSE 1 END), ss.dtIns DESC";
		
		return sql;
	}

}
