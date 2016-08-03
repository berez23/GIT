package it.webred.ct.data.access.basic.segnalazionequalificata.dao;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

public class SegnalazioneQualificataBaseDAO implements Serializable {
	
	@PersistenceContext(unitName="CT_Diogene")
	protected EntityManager manager_diogene;
	
	protected Logger logger = Logger.getLogger("CTservice_log");
	
	

}
