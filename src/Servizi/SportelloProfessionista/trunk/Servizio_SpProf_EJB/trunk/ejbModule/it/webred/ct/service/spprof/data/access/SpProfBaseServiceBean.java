package it.webred.ct.service.spprof.data.access;

import it.webred.ct.service.geospatial.data.access.GeospatialCatalogoService;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class SpProfBaseServiceBean {
	protected Logger logger = Logger.getLogger("spprof.log");
	
	protected GeospatialCatalogoService geospatialCatalogoService = (GeospatialCatalogoService) getEjb("GeospatialProvider", "GeospatialProvider_EJB", "GeospatialCatalogoServiceBean");

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
