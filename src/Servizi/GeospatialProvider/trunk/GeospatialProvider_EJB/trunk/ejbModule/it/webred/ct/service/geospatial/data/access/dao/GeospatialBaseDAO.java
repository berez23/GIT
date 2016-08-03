package it.webred.ct.service.geospatial.data.access.dao;


import java.io.Serializable;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class GeospatialBaseDAO implements Serializable {

	@PersistenceContext(unitName="Servizio_Geospatial_Data_Model")
	protected EntityManager manager;


	@Resource(name="GeospatialDataSourceRouter")
	protected DataSource dataSource;
	
	protected Logger logger = Logger.getLogger("geospatial.log");
	
}
