package it.webred.ct.service.comma340.web.common;

import java.util.HashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class ServiceLocator {

	protected static Logger logger = Logger.getLogger("C340service_log");
	private HashMap<String, Object> service = new HashMap();

	private static ServiceLocator me;
	private InitialContext ctx;

	private ServiceLocator() {
		// Set Context Reference
		try {
			ctx = new InitialContext();
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}

	}

	public static ServiceLocator getInstance() {
		if (me == null)
			me = new ServiceLocator();
		return me;
	}

	public Object getReference(String name) {
		/*
		 * Object o = service.get( name); if (o != null) return o;
		 */
		Object o = null;

		try {
			o = ctx.lookup(name);
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
			return null;
		}

		// service.put(name, o);
		return o;
	}
}
