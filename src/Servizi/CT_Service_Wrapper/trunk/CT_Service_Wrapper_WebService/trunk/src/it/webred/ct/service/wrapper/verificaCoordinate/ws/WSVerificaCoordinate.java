package it.webred.ct.service.wrapper.verificaCoordinate.ws;

import it.webred.ct.service.wrapper.verificaCoordinate.exception.VerificaCoordinateException;
import it.webred.ct.service.wrapper.verificaCoordinate.util.DTO2Jaxb;
import it.webred.ct.service.wrapper.verificaCoordinate.util.Jaxb2DTO;

import java.io.ByteArrayInputStream;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class WSVerificaCoordinate {
	
	private Logger log = Logger.getLogger(this.getClass().getName());	
	
	
	public String verificaCoordinate(String xml) {
		String ret = null;
		java.sql.Connection conn = null;
		com.mchange.v2.c3p0.ComboPooledDataSource dataSource = null;
		
		try {
			log.info("[Servizio WEB] WSVerificaCoordinate::verificaCoordinate");
			
			log.info("Xml request:\n---------\n"+xml+"\n----------------");
			
			it.webred.ct.service.jaxb.verificaCoordinate.request.VerificaCoordinate vcRequestJaxb = this.validateRequest(xml);
			
			//recupero codice ente
			String ente = vcRequestJaxb.getInfoUtente().getEnteId();
			log.info("Ente: "+ente);
			
			//jaxb request to DTO	request
			it.webred.verificaCoordinate.dto.request.VerificaCoordinateRequestDTO vcRequestDTO = 
												Jaxb2DTO.getInstance().jaxb2DTOObject(vcRequestJaxb);
			
			//invoke classe main di elaborazione
			it.webred.verificaCoordinate.entrypoint.VerificaCoordinateEntrypoint entry = 
						new it.webred.verificaCoordinate.entrypoint.VerificaCoordinateEntrypoint();
			
			log.info("Recupero connessione DIOGENE da Spring context in base all'ente");
			ClassPathXmlApplicationContext  ctx = new
		      	ClassPathXmlApplicationContext(new String[] {"/applicationContext.xml"}, false);
			
			ctx.refresh();
			
			dataSource = (com.mchange.v2.c3p0.ComboPooledDataSource)ctx.getBean("diogene_"+ente.toLowerCase());
			
			conn = dataSource.getConnection();
			log.info("Connessione recuperata ["+conn.getClass().getName()+"]");
			
			log.info("Invoke del metodo 'verifica' ["+entry.getClass().getName()+"]");
			it.webred.verificaCoordinate.dto.VerificaCoordinateBaseDTO vcBaseDTO = entry.verifica(vcRequestDTO,conn);
			
			if(vcBaseDTO != null) {
				it.webred.verificaCoordinate.dto.response.VerificaCoordinateResponseDTO vcResponseDTO = 
						(it.webred.verificaCoordinate.dto.response.VerificaCoordinateResponseDTO)vcBaseDTO;
				
				//DTO response to jaxb response
				it.webred.ct.service.jaxb.verificaCoordinate.response.VerificaCoordinate vcResponseJaxb = 
														DTO2Jaxb.getInstance().dto2JaxbObject(vcResponseDTO);
				
				//validazione response per xml
				ret = this.validaResponse(vcResponseJaxb);
			}
			else {
				throw new VerificaCoordinateException("Entrypoint ha restituito un oggetto null");
			}
			
			log.info("Xml response:\n---------\n"+ret+"\n----------------");
			
		}catch(VerificaCoordinateException vce) {
			log.error("Eccezione validazione: "+vce.getMessage());
			ret = this.buildERRMessage(vce.getMessage());
		}catch (Exception e) {
			log.error("Eccezione Web Service: "+e.getMessage(),e);
			ret = this.buildERRMessage("Eccezione Web Service: "+e.getMessage());
		}finally {
			try {
				if(conn != null) {
					if(!conn.isClosed()){
						conn.close();
					}
				}
				
				if(dataSource != null) {
					dataSource.close();
				}
				
			}catch(Exception ee) {
				log.error("Eccezione chiusura datasource e/o connessione");
				ret = this.buildERRMessage("Eccezione Web Service (chiusura datasource e/o connessione): "+ee.getMessage());
			}
		}
		
		return ret;
	}
	
	
	
	private String buildERRMessage(String descr) {
		String errXml = null;
		
		log.info("---- Generazione messaggio di errore [ERR-Ack] ---- ");
		
		try {
			it.webred.ct.service.jaxb.verificaCoordinate.response.ObjectFactory of = 
							new  it.webred.ct.service.jaxb.verificaCoordinate.response.ObjectFactory();
			
			it.webred.ct.service.jaxb.verificaCoordinate.response.VerificaCoordinate vc = of.createVerificaCoordinate();
			vc.setTipo("ERR-Ack");
			
			it.webred.ct.service.jaxb.verificaCoordinate.response.Error error = of.createError();
			error.setDesc(descr);
			vc.getError().add(error);
			
			errXml = this.validaResponse(vc);
			
		}catch(Exception e) {
			log.error("Eccezione generazione messaggio di errore: "+e.getMessage(),e);
		}
		
		return errXml;
	}
	
	
 	private it.webred.ct.service.jaxb.verificaCoordinate.request.VerificaCoordinate validateRequest(String xml) throws VerificaCoordinateException {
		it.webred.ct.service.jaxb.verificaCoordinate.request.VerificaCoordinate vc = null;
		
		try {
			log.info("Validazione xml di richiesta");
			javax.xml.bind.JAXBContext jc = javax.xml.bind.JAXBContext
					.newInstance("it.webred.ct.service.jaxb.verificaCoordinate.request");

			javax.xml.bind.Unmarshaller u = jc.createUnmarshaller();
			vc = (it.webred.ct.service.jaxb.verificaCoordinate.request.VerificaCoordinate)u.unmarshal(new ByteArrayInputStream(xml.getBytes()));
			log.info("Xml corretto");
			
		}catch(Exception e) {
			log.error("Xml di richiesta non è valido");
			throw new VerificaCoordinateException("Formato xml di request non corretto: "+e.getMessage());
		}
		
		return vc;
	}
 	
 	
 	
	
	
	private String validaResponse(it.webred.ct.service.jaxb.verificaCoordinate.response.VerificaCoordinate vcResponseJaxb)throws VerificaCoordinateException {
		String ret = null;
		
		try {
			log.info("Validazione oggetto Jaxb di response");
			javax.xml.bind.JAXBContext jc = javax.xml.bind.JAXBContext.newInstance(vcResponseJaxb.getClass().getPackage().getName());
			
			javax.xml.bind.Marshaller m = jc.createMarshaller();
			m.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
			m.marshal(vcResponseJaxb, baos);
			ret = new String(baos.toByteArray());
			log.info("Oggetto jaxb corretto");

		}catch(Exception e) {
			log.error("Oggetto response non valido");
			throw new VerificaCoordinateException(e.getMessage());
		}
		
		return ret;
	}
}
