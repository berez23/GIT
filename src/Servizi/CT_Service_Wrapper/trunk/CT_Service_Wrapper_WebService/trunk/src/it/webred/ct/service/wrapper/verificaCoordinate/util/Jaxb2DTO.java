package it.webred.ct.service.wrapper.verificaCoordinate.util;

import org.apache.log4j.Logger;

public class Jaxb2DTO {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	private static Jaxb2DTO me;
	
	public static Jaxb2DTO getInstance() {
		if(me==null)
			me = new Jaxb2DTO();
		
		return me;
	}
	
	
	public it.webred.verificaCoordinate.dto.request.VerificaCoordinateRequestDTO jaxb2DTOObject(
					it.webred.ct.service.jaxb.verificaCoordinate.request.VerificaCoordinate vcJaxb) 
																					throws Exception {
		
		it.webred.verificaCoordinate.dto.request.VerificaCoordinateRequestDTO vcDTO = null;
		
		log.info("Trasformazione da oggetto jaxb a DTO ["+vcJaxb.getClass().getName()+"]");
		
		try {
			vcDTO = new it.webred.verificaCoordinate.dto.request.VerificaCoordinateRequestDTO();
			
			//info utente
			it.webred.ct.service.jaxb.verificaCoordinate.request.UserInfo uiJaxb = vcJaxb.getInfoUtente();
			if(uiJaxb != null) {
				it.webred.verificaCoordinate.dto.request.UserInfoDTO uiDTO =
										new it.webred.verificaCoordinate.dto.request.UserInfoDTO();
				vcDTO.setInfoUtente(uiDTO);
				uiDTO.setEnteId(uiJaxb.getEnteId());
				uiDTO.setUserId(uiJaxb.getUserId());
				uiDTO.setPassword(uiJaxb.getPassword());
			}
			
			//info credenziali
			it.webred.ct.service.jaxb.verificaCoordinate.request.Credenziali cJaxb = vcJaxb.getCredenziali();
			if(cJaxb != null) {
				//TODO
			}

			//dati toponomastici
			it.webred.ct.service.jaxb.verificaCoordinate.request.DatiToponomastici dtJaxb = vcJaxb.getDatiToponomastici();
			if(dtJaxb != null) {
				it.webred.verificaCoordinate.dto.request.DatiToponomasticiDTO dtDTO = 
					                     new it.webred.verificaCoordinate.dto.request.DatiToponomasticiDTO();
				vcDTO.setDatiToponomastici(dtDTO);
				dtDTO.setTipoArea(dtJaxb.getTipoArea());
				dtDTO.setNomeVia(dtJaxb.getNomeVia());
				dtDTO.setCivico(dtJaxb.getCivico());
				dtDTO.setEsponente(dtJaxb.getEsponente());
				dtDTO.setCodiceVia(dtJaxb.getCodiceVia());
			}
			
			//dati catastali
			it.webred.ct.service.jaxb.verificaCoordinate.request.DatiCatastali dcJaxb = vcJaxb.getDatiCatastali();
			if(dcJaxb != null) {
				it.webred.verificaCoordinate.dto.request.DatiCatastaliDTO dcDTO = 
										new it.webred.verificaCoordinate.dto.request.DatiCatastaliDTO();
				vcDTO.setDatiCatastali(dcDTO);
				dcDTO.setFoglio(dcJaxb.getFoglio());
				dcDTO.setMappale(dcJaxb.getMappale());
				dcDTO.setSubalterno(dcJaxb.getSubalterno());
				dcDTO.setSezione(dcJaxb.getSezione());
				//dcDTO.setTipoCatasto(dcJaxb.getTipoCatasto());
				
				if(dcJaxb.isSetTipoCatasto()) {
					it.webred.ct.service.jaxb.verificaCoordinate.request.TipoCatasto tcJaxb = dcJaxb.getTipoCatasto();
					
					dcDTO.setTipoCatasto(
							it.webred.verificaCoordinate.dto.request.TipoCatastoDTO.fromValue(tcJaxb.value()));
				}
			}
			
			
			//residenziale
			it.webred.ct.service.jaxb.verificaCoordinate.request.Residenziale rJaxb = vcJaxb.getResidenziale();
			if(rJaxb != null) {
				it.webred.verificaCoordinate.dto.request.ResidenzialeDTO rDTO = 
					new it.webred.verificaCoordinate.dto.request.ResidenzialeDTO();
				
				org.apache.commons.beanutils.BeanUtils.copyProperties(rDTO, rJaxb);
				vcDTO.setResidenziale(rDTO);
			}
			
			//non residenziale
			it.webred.ct.service.jaxb.verificaCoordinate.request.NonResidenziale nrJaxb = vcJaxb.getNonResidenziale();
			if(nrJaxb != null) {
				it.webred.verificaCoordinate.dto.request.NonResidenzialeDTO nrDTO = 
					new it.webred.verificaCoordinate.dto.request.NonResidenzialeDTO();
				
				if(nrJaxb.isSetC6()) {
					it.webred.verificaCoordinate.dto.request.C6DTO c6DTO = 
						new it.webred.verificaCoordinate.dto.request.C6DTO();
					
					c6DTO.setPostoAutoCoperto(nrJaxb.getC6().getPostoAutoCoperto());
					nrDTO.setC6(c6DTO);
				}
				
				//org.apache.commons.beanutils.BeanUtils.copyProperties(nrDTO, nrJaxb);
				nrDTO.setCategoriaEdilizia(nrJaxb.getCategoriaEdilizia());
				
				vcDTO.setNonResidenziale(nrDTO);
			}
			
			
			log.info("Oggetto DTO generato: "+vcDTO.getClass().getName());
			
		}catch(Exception e) {
			log.error("Eccezione jaxb2DTOObject(): "+e.getMessage());
			throw e;
		}
		
		return vcDTO;
	}
}
