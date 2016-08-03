package it.webred.ct.service.comma340.data.access.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

public class C340BaseDAO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName="Servizio_C340_Data_Model")
	protected EntityManager manager;
	
	protected static Logger logger = Logger.getLogger("C340service_log");

}
