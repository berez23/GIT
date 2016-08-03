package it.escsolution.escwebgis.common;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

public class EnvBase {

	protected static Logger log = Logger.getLogger("diogene.log");
	
	public static Object getEjb(String ear, String module, String ejbName){
		Context cont;
		try {
			cont = new InitialContext();
			return cont.lookup("java:global/" + ear + "/" + module + "/" + ejbName);
		} catch (NamingException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
}
