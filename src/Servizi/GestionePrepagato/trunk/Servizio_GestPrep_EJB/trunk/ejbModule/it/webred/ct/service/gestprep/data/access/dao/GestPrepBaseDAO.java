package it.webred.ct.service.gestprep.data.access.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

public class GestPrepBaseDAO {

	@PersistenceContext(unitName="Servizio_GestPrepagato_Data_Model")
	protected EntityManager manager;

	protected Logger logger = Logger.getLogger("gestprep.log");

}
