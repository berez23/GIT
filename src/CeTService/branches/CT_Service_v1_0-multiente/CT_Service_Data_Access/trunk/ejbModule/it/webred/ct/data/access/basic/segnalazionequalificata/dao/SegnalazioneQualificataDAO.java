package it.webred.ct.data.access.basic.segnalazionequalificata.dao;

import it.webred.ct.data.access.basic.segnalazionequalificata.SegnalazioneQualificataServiceException;
import it.webred.ct.data.access.basic.segnalazionequalificata.dto.PraticaSegnalazioneDTO;
import it.webred.ct.data.access.basic.segnalazionequalificata.dto.RicercaPraticaSegnalazioneDTO;
import it.webred.ct.data.model.segnalazionequalificata.SOfAmbitoAccertamento;
import it.webred.ct.data.model.segnalazionequalificata.SOfPratAllegato;
import it.webred.ct.data.model.segnalazionequalificata.SOfPratica;

import java.util.List;

public interface SegnalazioneQualificataDAO {
	
	public List<SOfAmbitoAccertamento> getAmbitiAccertamento() 
		throws SegnalazioneQualificataServiceException;

	public PraticaSegnalazioneDTO getPraticaById(String id) 
		throws SegnalazioneQualificataServiceException;

	public List<SOfPratica> search(RicercaPraticaSegnalazioneDTO rs)
		throws SegnalazioneQualificataServiceException;
	
	public Long searchCount(RicercaPraticaSegnalazioneDTO rs)
		throws SegnalazioneQualificataServiceException;
	
	public List<Object> getSuggestionCognomi(String value, String tipo)
		throws SegnalazioneQualificataServiceException;
	
	public List<Object> getSuggestionDenominazione(String value)
		throws SegnalazioneQualificataServiceException;

	public List<Object> getSuggestionNomi(String value, String tipo)
		throws SegnalazioneQualificataServiceException;
	
	public List<Object> getSuggestionCodFisc(String value, String tipo)
		throws SegnalazioneQualificataServiceException;
	
	public List<String> getListaOperatori()
		throws SegnalazioneQualificataServiceException;
	

}
