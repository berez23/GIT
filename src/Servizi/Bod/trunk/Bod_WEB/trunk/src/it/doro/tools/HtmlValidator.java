package it.doro.tools;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class HtmlValidator implements Validator{
	  public void validate(FacesContext context, UIComponent component, Object numero) throws ValidatorException {
	    Long n = null;
	    
	    //verifico se è un numero
	    try {
	      n = Long.parseLong((String)numero);
	    } catch (NumberFormatException e) {
	      FacesMessage message = new FacesMessage();
	      message.setSeverity(FacesMessage.SEVERITY_ERROR);
	      message.setSummary("Devi inserire un numero");
	      throw new ValidatorException(message);
	    }
	    
	  }
	}