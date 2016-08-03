package it.webred.cs.jsf.interfaces;

import it.webred.cs.csa.ejb.dto.RelazioneDTO;
import it.webred.cs.data.model.CsCTipoIntervento;
import it.webred.cs.data.model.CsDRelazione;

import java.util.List;

import javax.faces.model.SelectItem;


public interface IRelazioni {
	
	public void initializeData();
	public List<RelazioneDTO> getListaRelazioni();
	public void nuovo();
	public void carica();
	public void salva();
	public List<SelectItem> getListaTipiIntervento();
	public void aggiungiTipoIntervento();
	public void eliminaTipoIntervento();
	public String getModalHeader();
	public CsDRelazione getRelazione();
	public int getIdxSelected();
	public Long getIdTipoInterventoSelezionato();
	public List<CsCTipoIntervento> getListaTipiInterventoAttivi();

}
