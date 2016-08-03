package it.webred.ct.service.wrapper.intTerritorio.ws;


import it.webred.ct.aggregator.ejb.DatiCivicoService;
import it.webred.ct.aggregator.ejb.dto.DatiCivicoDTO;
import it.webred.ct.aggregator.ejb.dto.RichiestaDatiCivicoDTO;

import java.io.ByteArrayInputStream;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

/**
 * Classe Web Service per servizio di Interrogazione Territorio
 * 
 * @author webred
 *
 */
public class WSInterrogazioniTerritorio {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	
	/**
	 * 
	 * @param xml
	 * @return
	 */
	public String interrogazioneTerritorio(String xml) {
		String ret = null;
		
		try {
			log.info("[Servizio WEB] WSInterrogazioniTerritorio::interrogazioneTerritorio");
			
			log.info("Xml request:\n---------\n"+xml+"\n----------------");
			
			//validazione del xml
			validateRequest(xml);
			
			//chiamata ejb passando la stringa xml
			ret = callRemoteEJB4InterrogazioneTerritorioService(xml);
			
			log.info("Xml response:\n---------\n"+ret+"\n----------------");
			
		}catch (Exception e) {
			log.error("Eccezione Web Service: "+e.getMessage(),e);
		}
		
		return ret;
	}
	
	
	
	private void validateRequest(String xml) throws Exception {

		try {
			log.info("Validazione xml di richiesta");
			javax.xml.bind.JAXBContext jc = javax.xml.bind.JAXBContext
					.newInstance("it.webred.ct.service.jaxb.intTerritorio.datiCivico.request");

			javax.xml.bind.Unmarshaller u = jc.createUnmarshaller();
			u.unmarshal(new ByteArrayInputStream(xml.getBytes()));
			log.info("Xml corretto");
			
		}catch(Exception e) {
			log.error("Xml di richiesta non è valido");
			throw e;
		}
	}


	
	private String callRemoteEJB4InterrogazioneTerritorioService(String xml) throws Exception {
		String result = "";
		
		log.info("Chiamata EJB remoto");
		
		try {
			java.io.InputStream is = this.getClass().getResourceAsStream("/service.properties");
			Properties p = new Properties();
			p.load(is);
			log.info("Parametri di connessione remota acquisiti");
			
			Context ctx = new InitialContext(p);
			String ejbname = p.getProperty("remote.ejb.interrogazioni.territorio.name");
			log.info("Invocazione EJB remoto: "+ejbname);

			DatiCivicoService obj = (DatiCivicoService)ctx.lookup(ejbname);
			result = obj.getDatiCivico(xml);

			log.info("EJB chiamato con successo");
			
		} catch (Exception e) {
			log.error("Eccezione chiamata EJB: "+e.getMessage());
			throw e;
		}
		
		return result;
	}
}
