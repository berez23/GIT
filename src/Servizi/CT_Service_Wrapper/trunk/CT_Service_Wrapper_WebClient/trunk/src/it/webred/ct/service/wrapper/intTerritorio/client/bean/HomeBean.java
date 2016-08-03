package it.webred.ct.service.wrapper.intTerritorio.client.bean;

import it.webred.ct.service.jaxb.intTerritorio.datiCivico.request.DatiCivico;
import it.webred.ct.service.jaxb.intTerritorio.datiCivico.request.Richiesta;
import it.webred.ct.service.jaxb.intTerritorio.datiCivico.request.UserInfo;
import it.webred.ct.service.wrapper.base.bean.WrapperBaseBean;
import it.webred.ct.service.wrapper.intTerritorio.client.stub.WSInterrogazioniTerritorioStub;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.llom.OMTextImpl;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.deployment.FileSystemConfigurator;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.log4j.Logger;

public class HomeBean extends WrapperBaseBean implements Serializable {
	
	private Logger log = Logger.getLogger(HomeBean.class.getName());
	
	private String enteId;
	private String userId;
	private String civico;
	private String toponimo;
	private String via;
	
	
	private String xml;
	private it.webred.ct.service.jaxb.intTerritorio.datiCivico.response.DatiCivico datiCivico;
	
	public String goInterrogazioniTerritorio() {
		return "client.IT.home";
	}
	
	
	/**
	 * Metodo per la chiamata al ws senza autenticazione
	 * @return
	 */
	public String doServiceInvoke() {
		
		try {
			WSInterrogazioniTerritorioStub stub = new WSInterrogazioniTerritorioStub();
			stub._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
			
			WSInterrogazioniTerritorioStub.InterrogazioneTerritorio request = 
				new WSInterrogazioniTerritorioStub.InterrogazioneTerritorio();
			
			OMFactory factory = OMAbstractFactory.getOMFactory();
	        OMNamespace ns = factory.createOMNamespace("http://ws.intTerritorio.wrapper.service.ct.webred.it","ns");
	        OMElement elem = factory.createOMElement("interrogazioneTerritorio", ns);
	        OMElement childElem = factory.createOMElement("args0", null);
	        childElem.setText(this.buildXml());
	        elem.addChild(childElem);
			
	        OMElement response = stub._getServiceClient().sendReceive(elem);
	        
	        OMElement e = response.getFirstElement();
	        java.util.Iterator it = e.getChildren();
	        while(it.hasNext()) {
	        	OMTextImpl o = (OMTextImpl)it.next();
	        	log.debug(o.getText());
	        	xml = o.getText();
	        }
	        
	        this.datiCivico = xml2Jaxb();
	        
	        
	        super.addInfoMessage("interrogazioni_territorio_ok");
		}catch(Exception e) {
			log.error("Eccezione: "+e.getMessage(),e);
			super.addErrorMessage("interrogazioni_territorio_error", e.getMessage());
		}
		
		return "client.IT.home";
	}
	
	
	/**
	 * Metodo per la chiamata al ws con autenticazione
	 * @return
	 */
	public String doWSSServiceInvoke() {
		try {
			
			//String repo_path = super.getRequest().getContextPath()+"/META-INF";
			String repo_path = System.getProperty("catalina.home")+"/webapps/WrapperWebClient/META-INF";
			
			FileSystemConfigurator fsc = new FileSystemConfigurator(repo_path,null);
			ConfigurationContext ctx = 
				ConfigurationContextFactory.createConfigurationContext(fsc);
			
			WSInterrogazioniTerritorioStub stub = new WSInterrogazioniTerritorioStub(ctx);
			ServiceClient client = stub._getServiceClient();
			
			Options options = new Options();
			options.setAction(super.getClientProperty("interrogazioni.territorio.ws.action.name"));
	        options.setTo(new EndpointReference(super.getClientProperty("interrogazioni.territorio.ws.endpoint.url")));
	        client.setOptions(options);
	        
	        OMFactory factory = OMAbstractFactory.getOMFactory();
	        OMNamespace ns = factory.createOMNamespace(super.getClientProperty("interrogazioni.territorio.ws.namespace"),"ns");
	        OMElement elem = factory.createOMElement(super.getClientProperty("interrogazioni.territorio.ws.method.name"), ns);
	        //param 1
	        OMElement childElem = factory.createOMElement(super.getClientProperty("interrogazioni.territorio.ws.param.1.name"), null);
	        childElem.setText(this.buildXml());
	        elem.addChild(childElem);
	        
	        OMElement response = client.sendReceive(elem);
	        
	        OMElement e = response.getFirstElement();
	        java.util.Iterator it = e.getChildren();
	        while(it.hasNext()) {
	        	OMTextImpl o = (OMTextImpl)it.next();
	        	log.debug("\n"+o.getText());
	        	xml = o.getText();
	        }
	        
	        this.datiCivico = xml2Jaxb();
	        
	        
	        super.addInfoMessage("interrogazioni_territorio_ok");
		}catch(Exception e) {
			log.error("Eccezione: "+e.getMessage(),e);
			super.addErrorMessage("interrogazioni_territorio_error", e.getMessage());
		}
		
		return "client.IT.home";
	}
	

	
	private String buildXml() throws Exception{
		String xmlrequest = null;
		
		try {
			it.webred.ct.service.jaxb.intTerritorio.datiCivico.request.ObjectFactory of = 
				new it.webred.ct.service.jaxb.intTerritorio.datiCivico.request.ObjectFactory();
			
			DatiCivico dc = of.createDatiCivico();
			UserInfo uf = of.createUserInfo();
			dc.setInfoUtente(uf);
			
			uf.setEnteId(this.enteId);
			uf.setUserId(this.userId);
			
			Richiesta r = of.createRichiesta();
			dc.setRichiesta(r);
			
			r.setCivico(this.civico);
			r.setToponimo(this.toponimo);
			r.setVia(this.via);
			
			log.info(dc.getClass().getPackage().getName());
			
			javax.xml.bind.JAXBContext jc = 
					javax.xml.bind.JAXBContext.newInstance("it.webred.ct.service.jaxb.intTerritorio.datiCivico.request");
			
			javax.xml.bind.Marshaller m = jc.createMarshaller();
			m.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
			m.marshal(dc, baos);
			
			xmlrequest = new String(baos.toByteArray());
			log.debug("\n"+xmlrequest);
			
		}catch(Exception e) {
			log.error("Eccezione: "+e.getMessage());
			throw new Exception(super.getMessage("interrogazioni_territorio_error_buildxml"));
		}
		
		return xmlrequest;
	}
	
	
	private it.webred.ct.service.jaxb.intTerritorio.datiCivico.response.DatiCivico xml2Jaxb() throws Exception {
		it.webred.ct.service.jaxb.intTerritorio.datiCivico.response.DatiCivico dc = null;
		
		try {
			log.info("Trasformazione xml in oggetto Jaxb [it.webred.ct.service.jaxb.intTerritorio.datiCivico.response]");
			javax.xml.bind.JAXBContext jc = javax.xml.bind.JAXBContext.newInstance("it.webred.ct.service.jaxb.intTerritorio.datiCivico.response");
			
			javax.xml.bind.Unmarshaller u = jc.createUnmarshaller();
			Object o = u.unmarshal(new ByteArrayInputStream(this.xml.getBytes()));
			dc = (it.webred.ct.service.jaxb.intTerritorio.datiCivico.response.DatiCivico)o;
			log.info("Trasformazione eseguita con successo ["+dc.getClass().getName()+"]");
			
		}catch(Exception e) {
			log.error("Eccezione: "+e.getMessage());
			throw new Exception(super.getMessage("interrogazioni_territorio_error_jaxbobj"));
		}
		
		return dc;
	}
	
	
	public String getXml() {
		return xml;
	}


	public void setXml(String xml) {
		this.xml = xml;
	}


	public String getEnteId() {
		return enteId;
	}


	public void setEnteId(String enteId) {
		this.enteId = enteId;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getCivico() {
		return civico;
	}


	public void setCivico(String civico) {
		this.civico = civico;
	}


	public String getToponimo() {
		return toponimo;
	}


	public void setToponimo(String toponimo) {
		this.toponimo = toponimo;
	}


	public String getVia() {
		return via;
	}


	public void setVia(String via) {
		this.via = via;
	}


	public it.webred.ct.service.jaxb.intTerritorio.datiCivico.response.DatiCivico getDatiCivico() {
		return datiCivico;
	}


	public void setDatiCivico(
			it.webred.ct.service.jaxb.intTerritorio.datiCivico.response.DatiCivico datiCivico) {
		this.datiCivico = datiCivico;
	}
}
