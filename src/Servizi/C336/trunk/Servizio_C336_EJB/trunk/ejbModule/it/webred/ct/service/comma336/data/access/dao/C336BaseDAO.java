package it.webred.ct.service.comma336.data.access.dao;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

public class C336BaseDAO {
	@PersistenceContext(unitName="Servizio_C336_Data_Model")
	protected EntityManager manager;

	protected Logger logger = Logger.getLogger("C336service.log");
}
