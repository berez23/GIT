package it.webred.ct.data.access.basic.pgt.dao;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class PgtBaseDAO implements Serializable {
	@PersistenceContext(unitName="CT_Diogene")
	protected EntityManager manager_diogene;
	
	//@Resource(mappedName="java:/jdbc/Diogene_DS")
	@Resource(name="ctDataSourceRouter")
	DataSource dataSource;
	protected Logger logger = Logger.getLogger("CTservice_log");
}
