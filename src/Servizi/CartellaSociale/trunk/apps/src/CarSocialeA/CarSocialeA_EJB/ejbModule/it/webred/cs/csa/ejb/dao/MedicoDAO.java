package it.webred.cs.csa.ejb.dao;

import it.webred.cs.csa.ejb.CarSocialeBaseDAO;
import it.webred.cs.data.model.CsCMedico;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

@Named
public class MedicoDAO extends CarSocialeBaseDAO implements Serializable {
	
	private static final long serialVersionUID = 1L;
		
	@SuppressWarnings("unchecked")
	public List<CsCMedico> getMedici() {
		
		try{
			
			Query q = em.createNamedQuery("CsCMedico.findAllAbil");
			return q.getResultList();
			
		} catch (Exception e) {	
			logger.error("Errore getMedici "+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public CsCMedico findMedicoById(long id) throws Exception {
		CsCMedico medico = em.find(CsCMedico.class, id);
		return medico;
}
}
