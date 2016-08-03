package it.webred.gitland.web.statistiche;

import it.webred.gitland.data.model.Azienda;
import it.webred.gitland.data.model.IntParametro;
import it.webred.gitland.data.model.Pratica;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

public class FormField implements Serializable {  
	  
	public static String TIPO_INPUT_UTENTE="InputUtente";
	public static String TIPO_ENTE_CORRENTE="EnteCorrente";
	public static String TIPO_UTENTE_CORRENTE="UtenteCorrente";
	
	public static String DATA_TYPE_STRING="String";
	public static String DATA_TYPE_DATE="Date";
	public static String DATA_TYPE_LONG="Long";
	public static String DATA_TYPE_DOUBLE="Double";
	public static String DATA_TYPE_BIGDECIMAL="BigDecimal";
	
	public static String MATCH_TYPE_UGUALE="Uguale";
	public static String MATCH_TYPE_CONTIENE="Contiene";
	public static String MATCH_TYPE_INIZIA="Inizia";
	public static String MATCH_TYPE_FINISCE="Finisce";

	
	private static final long serialVersionUID = 3449320501999010651L;
	
	private Object value;  
	
    private boolean required;  
    
    private boolean readOnly;  
    
    private boolean visible;  
    
    private List<SelectItem> selectItems;  

    private Azienda aziendaSelezionata=null;
    
    private Pratica praticaSelezionata=null;
    
    private String requiredMessage;
    
    private IntParametro parametro;
    
    public FormField(Object value) {  
        this.value = value;  
    }  

    public FormField(IntParametro parametro) {  
        this.parametro=parametro;
        this.required = "S".equals(parametro.getObbligatorio());  
        this.visible = "S".equals(parametro.getVisibile());  
        this.readOnly = !"S".equals(parametro.getEditabile());  
        this.requiredMessage=parametro.getDescrizione()+" Obbligatorio! Inserire un valore";
        this.value = "";
        if("Date".equals(parametro.getDataType())){
        	this.value= new Date();
        }
        if("Double".equals(parametro.getDataType())){
        	this.value= new Double(0);
        }
        if("Long".equals(parametro.getDataType())){
        	this.value= new Long(0);
        }
  
    }  
  
    public FormField(boolean required) {  
        this.required = required;  
    }  
  
    public FormField(Object value, boolean required) {  
        this.value = value;  
        this.required = required;  
    }  
  
    public FormField(Object value, boolean required, List<SelectItem> selectItems) {  
        this.value = value;  
        this.required = required;  
        this.selectItems = selectItems;  
    }  
  
    public Object getValue() {  
        return value;  
    }  
  
    public void setValue(Object value) {  
        this.value = value;  
    }  
  
    public boolean isRequired() {  
        return required;  
    }  
  
    public void setRequired(boolean required) {  
        this.required = required;  
    }  
  
    public List<SelectItem> getSelectItems() {  
        return selectItems;  
    }

	public String getRequiredMessage() {
		return requiredMessage;
	}

	public void setRequiredMessage(String requiredMessage) {
		this.requiredMessage = requiredMessage;
	}

	public IntParametro getParametro() {
		return parametro;
	}

	public void setParametro(IntParametro parametro) {
		this.parametro = parametro;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Azienda getAziendaSelezionata() {
		return aziendaSelezionata;
	}

	public void setAziendaSelezionata(Azienda aziendaSelezionata) {
		this.aziendaSelezionata = aziendaSelezionata;
	}

	public Pratica getPraticaSelezionata() {
		return praticaSelezionata;
	}

	public void setPraticaSelezionata(Pratica praticaSelezionata) {
		this.praticaSelezionata = praticaSelezionata;
	}
}  