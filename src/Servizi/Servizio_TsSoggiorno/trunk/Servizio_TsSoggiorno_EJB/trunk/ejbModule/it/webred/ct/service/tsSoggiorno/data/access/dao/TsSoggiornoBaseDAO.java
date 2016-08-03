package it.webred.ct.service.tsSoggiorno.data.access.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

public class TsSoggiornoBaseDAO 	implements Serializable {
		
		@PersistenceContext(unitName="Servizio_TsSoggiorno_Data_Model")
		protected EntityManager manager;

		protected Logger logger = Logger.getLogger("tssoggiorno_log");
		
}
