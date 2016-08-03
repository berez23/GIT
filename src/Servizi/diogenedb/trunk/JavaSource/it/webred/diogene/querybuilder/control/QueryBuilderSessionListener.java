package it.webred.diogene.querybuilder.control;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

public class QueryBuilderSessionListener implements HttpSessionListener
{
	private final Logger log = Logger.getLogger(this.getClass());

	public void sessionCreated(HttpSessionEvent e)
	{
		// TODO
		HttpSession session = e.getSession();
		log.debug("La sessione " + session.getId() + " \u00E8 nata, thread: " + Thread.currentThread().getName());
	}

	public void sessionDestroyed(HttpSessionEvent e)
	{
		// TODO
		HttpSession session = e.getSession();
		log.debug("La sessione " + session.getId() + " \u00E8 morta, thread: " + Thread.currentThread().getName());
		Enumeration enumer = session.getAttributeNames();
		while (enumer.hasMoreElements())
		{
			Object value = session.getAttribute((String) enumer.nextElement());
			if (value instanceof MetadataController)
				((MetadataController) value).endSesssion();
		}
		System.gc(); // TODO ?
	}
}
