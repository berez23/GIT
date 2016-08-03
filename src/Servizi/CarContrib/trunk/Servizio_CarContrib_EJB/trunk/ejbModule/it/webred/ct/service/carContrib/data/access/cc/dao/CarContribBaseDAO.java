package it.webred.ct.service.carContrib.data.access.cc.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

public class CarContribBaseDAO 	implements Serializable {
		
		@PersistenceContext(unitName="Servizio_CarContrib_Data_Model")
		protected EntityManager manager;

		protected Logger logger = Logger.getLogger("carcontrib.log");

		
}
