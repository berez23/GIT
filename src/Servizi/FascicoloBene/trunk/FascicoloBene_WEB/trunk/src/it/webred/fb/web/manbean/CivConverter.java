package it.webred.fb.web.manbean;

import it.webred.ejb.utility.ClientUtility;
import it.webred.fb.ejb.client.DettaglioBeneSessionBeanRemote;
import it.webred.fb.ejb.dto.KeyValueDTO;
import it.webred.fb.ejb.dto.RicercaBeneDTO;
import it.webred.fb.web.bean.FascicoloBeneBaseBean;


import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("civConverter")
@ViewScoped
public class CivConverter extends FascicoloBeneBaseBean implements Converter {
	
	private KeyValueDTO corrente;
	
	    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
	    	RicercaBeneDTO b = new RicercaBeneDTO();
			this.fillUserData(b);
	    	try{
	    	if(value != null && !"null".equalsIgnoreCase(value) &&value.trim().length() > 0) {
	    	
	    		if(corrente==null || !value.equals(corrente.getCodice())){
		    		corrente = new KeyValueDTO();
		    		corrente.setCodice(value);
		    		corrente.setDescrizione(value);
	    		}
	    		return corrente;
	        
	        }
	        else {
	            return null;
	        }
	        }catch(Exception e){}
	        return null;
	    }
	 
	    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
	        if(object != null) {
	            return String.valueOf(((KeyValueDTO) object).getCodice());
	        }
	        else {
	            return null;
	        }
	    }   
	} 