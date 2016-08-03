package it.webred.cs.csa.ejb.dao;

import it.webred.cs.csa.ejb.CarSocialeBaseDAO;
import it.webred.cs.data.model.CsAlert;
import it.webred.cs.data.model.CsAlertConfig;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

/**
 * @author alessandro.feriani
 *
 */
@Named
public class AlertDAO extends CarSocialeBaseDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	public void createAlert(CsAlert newAlert) throws Exception {
			em.persist(newAlert);
	}

	public CsAlert findAlertById(long idAlert) throws Exception {
			CsAlert alert= em.find(CsAlert.class,  idAlert);
			return alert;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsAlert> findAlertByIdCasoTipo(long idCaso, String tipo) throws Exception {
		Query q = em.createNamedQuery("CsAlert.getAlertByIdCasoTipo")
				.setParameter("idCaso", idCaso)
				.setParameter("tipo", tipo);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<CsAlert> getAlerts(String query) throws Exception {
			Query q = em.createQuery(query);
			
			@SuppressWarnings("rawtypes")
			List retList = q.getResultList();
			return retList;
	}
	
	public void setAlertLetto(Long idAlert) throws Exception {
			CsAlert alert = em.find(CsAlert.class, idAlert);
			alert.setLetto(true);
			em.merge(alert);
	}

	public void setAlertVisibile(Long idAlert) throws Exception {
			CsAlert alert = em.find(CsAlert.class, idAlert);
			alert.setVisibile(false);
			em.merge(alert);
	}

	public void setAlertPulisciLista(List<Long> idAlertList) throws Exception {
			for(Long id : idAlertList )
				setAlertVisibile(id);		
	}

	public void setAlertLeggiAll(List<Long> idAlertList) throws Exception {
			for(Long id : idAlertList )
				setAlertLetto(id);	
	}
	
	public void updateAlert(CsAlert alert) {

		em.merge(alert);

	}
	
	@SuppressWarnings("unchecked")
	public CsAlertConfig getAlertConfigByTipo(String tipo) throws Exception {
		Query q = em.createNamedQuery("CsAlertConfig.getAlertConfigByTipo")
				.setParameter("tipo", tipo);
		List<CsAlertConfig> lista = q.getResultList();
		if(lista.size() > 0)
			return lista.get(0);
		return null;
	}

}
