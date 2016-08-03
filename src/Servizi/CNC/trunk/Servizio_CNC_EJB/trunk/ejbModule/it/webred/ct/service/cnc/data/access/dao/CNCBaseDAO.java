package it.webred.ct.service.cnc.data.access.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CNCBaseDAO implements Serializable {
		
		@PersistenceContext(unitName="Servizio_CNC_Model")
		protected EntityManager manager;
		
}
