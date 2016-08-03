package it.webred.cs.jsf.interfaces;

import it.webred.cs.data.model.CsASoggetto;
import it.webred.cs.jsf.bean.ValiditaCompBaseBean;
import it.webred.dto.utility.KeyValuePairBean;

import java.util.List;

public interface ISoggCatSociale {
	
	public void carica(CsASoggetto soggetto);
		
	public void aggiungiSelezionato();
	
	public void chiudiSelezionato();
	
	public void eliminaSelezionato();
	
	public void salva();
	
	public void reset();
	
	public List<ValiditaCompBaseBean> getLstComponents();
	
	public ValiditaCompBaseBean getCompSelezionato();
	
	@SuppressWarnings("rawtypes")
	public List<KeyValuePairBean> getLstItems();
	
	public String getItemSelezionato();
	
	public Integer getIndexSelezionato();
	
	public List<ValiditaCompBaseBean> getLstComponentsActive();
	
	public Integer getMaxActiveComponents();
	
	public String getModalWidgetVar();
	
	public boolean isDisableEsci();
	
	public String getUfficioSchedaSegr();

}
