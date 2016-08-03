package it.webred.ct.service.spprof.data.access.dao;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class SpProfBaseDAO {
	
	@PersistenceContext(unitName="Servizio_SpProf_Data_Model")
	protected EntityManager manager;

	@Resource(name="SpProfDataSourceRouter")
	protected DataSource dataSource;
	
	protected Logger logger = Logger.getLogger("spprof.log");
}
