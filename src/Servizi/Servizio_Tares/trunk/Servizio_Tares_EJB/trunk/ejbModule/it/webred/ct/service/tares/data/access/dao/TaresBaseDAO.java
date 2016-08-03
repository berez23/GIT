package it.webred.ct.service.tares.data.access.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

public class TaresBaseDAO 	implements Serializable {
		
		@PersistenceContext(unitName="Servizio_Tares_Data_Model")
		protected EntityManager manager;

		protected Logger logger = Logger.getLogger("tares.log");

		
}
