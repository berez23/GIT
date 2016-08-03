package it.webred.ct.service.intTerritorio.web.bean;

import it.webred.ct.aggregator.ejb.dto.DatiCivicoDTO;
import it.webred.ct.aggregator.ejb.dto.RichiestaDatiCivicoDTO;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Session Bean implementation class 
 */
@WebService
@SOAPBinding
(
      style = SOAPBinding.Style.DOCUMENT,
      use = SOAPBinding.Use.LITERAL,
      parameterStyle = SOAPBinding.ParameterStyle.WRAPPED
 )
public class InterrogazioneTerritorioWebService{
	
	
	@WebMethod
	public DatiCivicoDTO getDatiCivico(
			@WebParam(name = "CodEnte")   String codEnte,
			@WebParam(name = "Toponimo")   String toponimo,
			@WebParam(name = "Indirizzo")  String indirizzo,
			@WebParam(name = "NumCivico") String civico) {
		
		RichiestaDatiCivicoDTO input = new RichiestaDatiCivicoDTO();
		input.setEnteId(codEnte);
		input.setToponimo(toponimo);
		input.setVia(indirizzo);
		input.setCivico(civico);
		
		System.out.println("CodEnte:"+codEnte+"; Toponimo:"+toponimo+"; Indirizzo:"+indirizzo+"; NumCivico:"+civico);
			
		InterrogazioneTerritorioBean bean = (InterrogazioneTerritorioBean)this.getBeanReference("interrogazioneBean");
		//InterrogazioneTerritorioBean bean = new InterrogazioneTerritorioBean();
		DatiCivicoDTO dati =  bean.getDatiCivico(input);
		System.out.println("Elaborazione completata");
		
		return dati;
	}
	
	public Object getBeanReference(String beanName) {
		FacesContext context = FacesContext.getCurrentInstance();
		Application a = context.getApplication();
		Object o = a.getVariableResolver().resolveVariable(context, beanName);
		return o;
	}


}
