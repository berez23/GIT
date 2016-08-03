package it.webred.ct.service.comma336.web.bean.util;

import javax.faces.component.UIComponent;  
import javax.faces.context.FacesContext;  
import javax.faces.convert.Converter;  
  
public class StringAsUpper implements Converter {  
	  
	 public Object getAsObject(FacesContext facesContext  
	                           , UIComponent component, String value) {  
	  return value.toString().toUpperCase();  
	 }  
	  
	 public String getAsString(FacesContext facesContext  
	                          , UIComponent component, Object value) {  
	  return value.toString().toUpperCase();  
	 }  
}
