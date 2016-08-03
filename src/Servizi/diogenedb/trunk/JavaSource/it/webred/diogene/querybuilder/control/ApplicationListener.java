package it.webred.diogene.querybuilder.control;

import it.webred.utils.RunAntBuild;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

/**
 * La classe predispone e rimuove gli attributi nel Context dell'applicazione
 * recuperando le informazioni dal file di configurazione web.xml
 * 
 * @author Monica
 * @version $Revision: 1.2 $ $Date: 2012/01/18 07:53:49 $
 */
public class ApplicationListener implements ServletContextListener
{
	private static final Logger log = Logger.getLogger(ApplicationListener.class.getName());

	/**
	 * Vengono impostati nel Context i dati necessari per la connessione al DB
	 * 
	 * @param event
	 *            ServletContextEvent da cui si recupera il context
	 *            dell'applicazione
	 */
	public void contextInitialized(ServletContextEvent event)
	{
		try 
		{

			log.info("Effettuo il deploy dei web services");
					/* 
		TODO: NIENTE PIU' WEB SERVICES ???? PERCHE' ???
		*/
			
		//	deployWS dws = new deployWS(event.getServletContext());
			
		//	new Thread(dws).start();
			
		} catch (Exception e) {
			//log.error("Errore in contextInitialized",e);
		}
 
		
		
		
	}

	/**
	 * Vengono rimossi dal Context gli attributi relativi la connessione al DB
	 * 
	 * @param event
	 *            ServletContextEvent da cui si recupera il context
	 *            dell'applicazione
	 */
	public void contextDestroyed(ServletContextEvent event)
	{
		ServletContext servletContext = event.getServletContext();

	}

	private class deployWS implements Runnable{

		ServletContext sc;
		public deployWS(ServletContext sc) {
			this.sc = sc;
			
		}
		
		public void run()
		{
			
			try 
			{
				log.info("Lancio il build in thread");
				ServletContext context = sc;
				String dir = context.getRealPath( "/WEB-INF/");
				
				File f = new File(dir, "build.xml");
				
				RunAntBuild runAnt = new RunAntBuild(f,"axis-deploy");
				runAnt.execute();
				
			} catch (Exception e) {
				log.error("Errore nella chiamata a RunAntBuild",e);
			}			
			
		}
		
	}

}



