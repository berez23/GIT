package it.webred.gitland.web;

import it.webred.gitland.data.model.Comune;
import it.webred.gitland.web.bean.GitLandBaseBean;

import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("comuneConverter")
@ViewScoped
public class ComuneConverter extends GitLandBaseBean implements Converter {
	
	public ComuneConverter() {
	}//-------------------------------------------------------------------------

    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
       System.out.println("submittedValue - " + submittedValue);
       Comune c = (Comune)gitLandService.getItemById( new Long(submittedValue) , Comune.class);
       
       return c;
    }//---------------------------------------------------------------------

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
    	String istatc = "";
    	if (value != null ){
    		if (value instanceof Long){
    			Long submittedValue = (Long)value;
            	System.out.println("submittedValue - " + submittedValue.longValue());
               	Comune c = (Comune)gitLandService.getItemById( (Long)value , Comune.class);
               	if (c!=null)
               		istatc = c.getIstatc() + "§" + c.getIstatp();        		
    		}else if (value instanceof Comune){
    			Comune c = (Comune)value;
    			if (c!=null)
               		istatc = c.getIstatc() + "§" + c.getIstatp();
    		}
    		
    	}

//       if (!(value instanceof Long)) {
//           throw new ClassCastException(
//                 "Partner converter got invalid class" + value.getClass().getCanonicalName());
//       }

        return istatc;
    }//-------------------------------------------------------------------------
	
    

}
