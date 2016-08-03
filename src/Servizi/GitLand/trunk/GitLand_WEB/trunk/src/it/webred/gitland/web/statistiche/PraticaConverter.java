package it.webred.gitland.web.statistiche;

import it.webred.gitland.data.model.Azienda;
import it.webred.gitland.data.model.Pratica;
import it.webred.gitland.web.bean.util.AziendeBean;
import it.webred.gitland.web.bean.util.StatisticheBean;
import it.webred.gitland.web.bean.util.UserBean;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("praticaConverter")
public class PraticaConverter implements Converter {
 
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
                UserBean service = (UserBean) fc.getExternalContext().getSessionMap().get("userBean");
                return service.getGitLandService().getItemById(Long.parseLong(value),Pratica.class);
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore Conversione", "Pratica  non valida."));
            }
        }
        else {
            return null;
        }
    }
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return String.valueOf(((Pratica) object).getPkPratica());
        }
        else {
            return null;
        }
    }   
}     