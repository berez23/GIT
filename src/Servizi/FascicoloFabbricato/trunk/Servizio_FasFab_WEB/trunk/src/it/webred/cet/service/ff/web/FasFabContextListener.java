package it.webred.cet.service.ff.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class FasFabContextListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent evt) {
		// TODO Auto-generated method stub
		
	}

	public void contextInitialized(ServletContextEvent evt) {
		Enumeration<String> parNames = evt.getServletContext().getInitParameterNames();
		Map<String, String> paramConfig = new HashMap<String, String>();
		while (parNames.hasMoreElements()) {
			String key = parNames.nextElement();
			String val = evt.getServletContext().getInitParameter(key);
			System.out.println("Param ["+key+" - "+val+"]");
			paramConfig.put(key, val);
		}
		
		evt.getServletContext().setAttribute("ff.paramConfig", paramConfig);
		
	}

}
