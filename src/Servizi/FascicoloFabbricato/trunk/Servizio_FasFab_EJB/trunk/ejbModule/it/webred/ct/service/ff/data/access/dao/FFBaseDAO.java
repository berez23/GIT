package it.webred.ct.service.ff.data.access.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

public class FFBaseDAO {
	
	
	@PersistenceContext(unitName="Servizio_FasFab_Data_Model")
	protected EntityManager manager;

	protected Logger logger = Logger.getLogger("fasfab.log");

}
