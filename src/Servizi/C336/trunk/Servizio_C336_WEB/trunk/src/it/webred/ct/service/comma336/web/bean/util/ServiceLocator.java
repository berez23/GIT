package it.webred.ct.service.comma336.web.bean.util;

import java.util.HashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class ServiceLocator {

	protected static final Logger logger = Logger.getLogger("C336service.log");
	
	private HashMap<String, Object> service = new HashMap<String, Object>();

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
