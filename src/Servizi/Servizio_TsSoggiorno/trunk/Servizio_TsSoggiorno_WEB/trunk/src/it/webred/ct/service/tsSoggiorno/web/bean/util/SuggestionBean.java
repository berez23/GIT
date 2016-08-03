package it.webred.ct.service.tsSoggiorno.web.bean.util;


import it.webred.ct.service.tsSoggiorno.data.access.dto.DataInDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.SocietaDTO;
import it.webred.ct.service.tsSoggiorno.data.access.dto.StrutturaDTO;
import it.webred.ct.service.tsSoggiorno.web.bean.TsSoggiornoBaseBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.model.SelectItem;


public class SuggestionBean extends TsSoggiornoBaseBean{
	
	private String descSocieta;
	
	/**
	 * Restituisce la lista di vie il cui nome contiene la stringa passata come parametro
	 * 
	 * @param viaObj Nome completo o parziale della via/strada/piazza/etc. 
	 * @return Lista di oggetti Sitidstr che soddisfano la ricerca
	 */
	public List<SocietaDTO> getListaSocieta(Object socObj){
		String msg = "suggestion.listaSocieta";
		String soc = (String)socObj;
		List<SocietaDTO> result = new ArrayList<SocietaDTO>();
		DataInDTO dataIn = new DataInDTO();
		fillEnte(dataIn);
		try{
			if(!soc.trim().equalsIgnoreCase("NULL")){
				dataIn.setObj(socObj);
				result = super.getAnagraficaService().getSuggestionSocieta(dataIn);
			}else{
				result = super.getAnagraficaService().getSocieta(dataIn);
			}
		}catch(Throwable t) {
			super.addErrorMessage(msg+".error", t.getMessage());
			logger.error(t.getMessage(), t);
		}
		
		return result;
		
	}
	
	



}
