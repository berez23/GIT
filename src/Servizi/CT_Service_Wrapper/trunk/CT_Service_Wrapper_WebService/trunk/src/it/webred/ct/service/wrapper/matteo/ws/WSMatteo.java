package it.webred.ct.service.wrapper.matteo.ws;


import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class WSMatteo {
	private Logger log = Logger.getLogger(this.getClass().getName());	
	
	
	public String execMatteo(String xml) {
		String ret = null;
		
		try {
			log.info("[Servizio WEB] WSMatteo::execMatteo");
			
			log.info("Xml request:\n---------\n"+xml+"\n----------------");
			
			
			ClassPathXmlApplicationContext  ctx = new
		      ClassPathXmlApplicationContext(new String[] {"/applicationContext.xml"}, false);
			
			ctx.refresh();
			
			com.mchange.v2.c3p0.ComboPooledDataSource ds = 
				(com.mchange.v2.c3p0.ComboPooledDataSource)ctx.getBean("diogene_f704");
			
			java.sql.Connection conn = ds.getConnection();
			
			ret = conn.getClass().getName();
			
			conn.close();
			ds.close();
			
			log.info("Xml response:\n---------\n"+ret+"\n----------------");
			
		}catch (Exception e) {
			log.error("Eccezione Web Service: "+e.getMessage(),e);
		}
		
		return ret;
	}
}
