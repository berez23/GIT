package it.webred.ct.service.geospatial.data.access;

import it.webred.ct.data.access.basic.pgt.PGTService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class GeospatialBaseServiceBean {
	protected Logger logger = Logger.getLogger("geospatial.log");
	
	protected PGTService pgtService = (PGTService) getEjb("CT_Service", "CT_Service_Data_Access", "PGTServiceBean");
	
	public Object getEjb(String ear, String module, String ejbName){
		Context cont;
		try {
			cont = new InitialContext();
			return cont.lookup("java:global/" + ear + "/" + module + "/" + ejbName);
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
