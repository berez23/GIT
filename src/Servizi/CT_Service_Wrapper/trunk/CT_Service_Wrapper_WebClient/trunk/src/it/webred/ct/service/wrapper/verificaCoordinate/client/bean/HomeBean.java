package it.webred.ct.service.wrapper.verificaCoordinate.client.bean;

import it.webred.ct.service.wrapper.base.bean.WrapperBaseBean;
import it.webred.ct.service.wrapper.verificaCoordinate.client.stub.WSVerificaCoordinateStub;
import it.webred.utils.FileSeeker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;


import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.llom.OMTextImpl;
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
	
	private List<SelectItem> requestXmls = new ArrayList<SelectItem>();
	private String requestXmlSelected;
	
	private String xml;
	
	@PostConstruct
	public void init() {
		try{
			log.info("Recupero xml files di richiesta");
			String confPathXml = "/conf/verificaCoordinate/xml/";
			
			java.net.URL url = this.getClass().getResource(confPathXml);
			//log.info("path: "+url.getPath());
			java.io.File fld = new java.io.File(url.getPath());
			java.io.File[] ff = fld.listFiles();
			
			requestXmls = new ArrayList<SelectItem>();
			for(java.io.File f: ff) {
				requestXmls.add(new SelectItem(confPathXml+f.getName(),f.getName()));
			}
			
		}catch(Exception e) {
			log.error("Eccezione: "+e.getMessage(),e);
			super.addErrorMessage("verifica_coordinate_error", e.getMessage());
		}
	}
	
	public String goVerificaCoordinate() {
		return "client.VC.home";
	}
	
	
	public String doServiceInvoke() {
		
		log.info("Xml selezionato:\n"+requestXmlSelected);
		
		try {
			java.io.InputStream is = this.getClass().getResourceAsStream(requestXmlSelected);
			
			byte[] bb = new byte[is.available()];
			is.read(bb);
			String reqXmlStream = new String(bb);
			log.info(reqXmlStream);
			
			WSVerificaCoordinateStub stub = new WSVerificaCoordinateStub();
			stub._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED,false);
			
			
			WSVerificaCoordinateStub.VerificaCoordinate request = 
										new WSVerificaCoordinateStub.VerificaCoordinate();
			
			OMFactory factory = OMAbstractFactory.getOMFactory();
	        OMNamespace ns = factory.createOMNamespace(super.getClientProperty("verifica.coordinate.ws.namespace"),"ns");
	        OMElement elem = factory.createOMElement(super.getClientProperty("verifica.coordinate.ws.method.name"), ns);
	        OMElement childElem = factory.createOMElement(super.getClientProperty("verifica.coordinate.ws.param.1.name"), null);
	        childElem.setText(reqXmlStream);
	        elem.addChild(childElem);
			
	        log.info("Invoke web service");
	        OMElement response = stub._getServiceClient().sendReceive(elem);
	        
	        OMElement e = response.getFirstElement();
	        java.util.Iterator it = e.getChildren();
	        while(it.hasNext()) {
	        	OMTextImpl o = (OMTextImpl)it.next();
	        	log.info(o.getText());
	        	xml = o.getText();
	        }
			
		}catch(Exception e) {
			log.error("Eccezione: "+e.getMessage(),e);
			super.addErrorMessage("verifica_coordinate_error", e.getMessage());
		}
		
		return "client.VC.home";
	}
	
	
	/*
	 * Metodo per la chiamata al ws con autenticazione
	 */
	public String doWSSServiceInvoke() {
		
		log.info("Xml selezionato:\n"+requestXmlSelected);
		
		try {
			String repo_path =  FileSeeker.fileName(".") + "/../../META-INF"; 
			
			
			FileSystemConfigurator fsc = new FileSystemConfigurator(repo_path,null);
			ConfigurationContext ctx = 
				ConfigurationContextFactory.createConfigurationContext(fsc);
			
			java.io.InputStream is = this.getClass().getResourceAsStream(requestXmlSelected);
			
			byte[] bb = new byte[is.available()];
			is.read(bb);
			String reqXmlStream = new String(bb);
			log.info(reqXmlStream);
			
			WSVerificaCoordinateStub stub = new WSVerificaCoordinateStub(ctx);
			ServiceClient client = stub._getServiceClient();
			
			Options options = new Options();
			options.setAction(super.getClientProperty("verifica.coordinate.ws.action.name"));
	        options.setTo(new EndpointReference(super.getClientProperty("verifica.coordinate.ws.endpoint.url")));
	        client.setOptions(options);
	        
	        OMFactory factory = OMAbstractFactory.getOMFactory();
	        OMNamespace ns = factory.createOMNamespace(super.getClientProperty("verifica.coordinate.ws.namespace"),"ns");
	        OMElement elem = factory.createOMElement(super.getClientProperty("verifica.coordinate.ws.method.name"), ns);
	        //param 1
	        OMElement childElem = factory.createOMElement(super.getClientProperty("verifica.coordinate.ws.param.1.name"), null);
	        childElem.setText(reqXmlStream);
	        elem.addChild(childElem);
	     
	        
	        OMElement response = client.sendReceive(elem);
	        
	        OMElement e = response.getFirstElement();
	        java.util.Iterator it = e.getChildren();
	        while(it.hasNext()) {
	        	OMTextImpl o = (OMTextImpl)it.next();
	        	log.info(o.getText());
	        	xml = o.getText();
	        }
	        
		}catch(Exception e) {
			log.error("Eccezione: "+e.getMessage(),e);
			super.addErrorMessage("verifica_coordinate_error", e.getMessage());
		}
		
		return "client.VC.home";
	}
	
	
	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}



	public String getRequestXmlSelected() {
		return requestXmlSelected;
	}


	public void setRequestXmlSelected(String requestXmlSelected) {
		this.requestXmlSelected = requestXmlSelected;
	}


	public List<SelectItem> getRequestXmls() {
		return requestXmls;
	}


	public void setRequestXmls(List<SelectItem> requestXmls) {
		this.requestXmls = requestXmls;
	}


}
