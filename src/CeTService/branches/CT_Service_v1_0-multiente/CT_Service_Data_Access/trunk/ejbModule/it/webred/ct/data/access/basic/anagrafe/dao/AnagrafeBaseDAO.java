package it.webred.ct.data.access.basic.anagrafe.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

public class AnagrafeBaseDAO implements Serializable {
		
	@PersistenceContext(unitName="CT_Diogene")
	protected EntityManager manager_diogene;
	
	protected Logger logger = Logger.getLogger("CTservice_log");
}
