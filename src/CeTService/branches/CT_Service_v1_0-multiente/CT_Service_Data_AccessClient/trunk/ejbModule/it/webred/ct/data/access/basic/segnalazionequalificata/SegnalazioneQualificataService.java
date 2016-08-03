package it.webred.ct.data.access.basic.segnalazionequalificata;

import it.webred.ct.data.access.basic.segnalazionequalificata.dto.PraticaSegnalazioneDTO;
import it.webred.ct.data.access.basic.segnalazionequalificata.dto.RicercaPraticaSegnalazioneDTO;
import it.webred.ct.data.model.segnalazionequalificata.SOfAmbitoAccertamento;
import it.webred.ct.data.model.segnalazionequalificata.SOfPratAllegato;
import it.webred.ct.data.model.segnalazionequalificata.SOfPratica;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface SegnalazioneQualificataService {

	public List<SOfAmbitoAccertamento> getAmbitiAccertamento() 
		throws SegnalazioneQualificataServiceException;
	
	public List<SOfPratica> search(SegnalazioniDataIn dataIn) 
		throws SegnalazioneQualificataServiceException;
	
	public Long searchCount(SegnalazioniDataIn dataIn)
		throws SegnalazioneQualificataServiceException;
		
	public List<Object> getSuggestionCognomi(SegnalazioniDataIn dataIn)
		throws SegnalazioneQualificataServiceException;
		
	public List<Object> getSuggestionDenominazione(SegnalazioniDataIn dataIn)
		throws SegnalazioneQualificataServiceException;
	
	public List<Object> getSuggestionNomi(SegnalazioniDataIn dataIn)
		throws SegnalazioneQualificataServiceException;
		
	public List<Object> getSuggestionCodFisc(SegnalazioniDataIn dataIn)
		throws SegnalazioneQualificataServiceException;
	
	public PraticaSegnalazioneDTO getPraticaById(SegnalazioniDataIn dataIn) 
		throws SegnalazioneQualificataServiceException;
	
	public List<String> getListaOperatori()
		throws SegnalazioneQualificataServiceException;
	

}
