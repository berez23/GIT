package it.webred.ct.data.access.basic.segnalazionequalificata;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import it.webred.ct.data.access.basic.CTServiceBaseBean;
import it.webred.ct.data.access.basic.segnalazionequalificata.*;
import it.webred.ct.data.access.basic.segnalazionequalificata.dao.SegnalazioneQualificataDAO;
import it.webred.ct.data.access.basic.segnalazionequalificata.dto.*;
import it.webred.ct.data.model.segnalazionequalificata.*;

@Stateless
public class SegnalazioneQualificataServiceBean extends CTServiceBaseBean 
	implements SegnalazioneQualificataService {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private SegnalazioneQualificataDAO segnalazioneQualificataDAO;
	
	@Override
	public List<SOfAmbitoAccertamento> getAmbitiAccertamento() throws SegnalazioneQualificataServiceException{
		return segnalazioneQualificataDAO.getAmbitiAccertamento();
	}

	
	@Override
	public PraticaSegnalazioneDTO getPraticaById(SegnalazioniDataIn dataIn) throws SegnalazioneQualificataServiceException{
		String id = (String) dataIn.getObj1();
		return segnalazioneQualificataDAO.getPraticaById(id);
	}
	
	@Override
	public List<SOfPratica> search(SegnalazioniDataIn dataIn)throws SegnalazioneQualificataServiceException{
		RicercaPraticaSegnalazioneDTO rs = dataIn.getRicercaPratica();
		return segnalazioneQualificataDAO.search(rs);
	}
	
	@Override
	public List<String> getListaOperatori() throws SegnalazioneQualificataServiceException{
		return segnalazioneQualificataDAO.getListaOperatori();
	}
	
	@Override
	public Long searchCount(SegnalazioniDataIn dataIn) throws SegnalazioneQualificataServiceException{
		RicercaPraticaSegnalazioneDTO rs = dataIn.getRicercaPratica();
		return segnalazioneQualificataDAO.searchCount(rs);
	}
		
	@Override
	public List<Object> getSuggestionCognomi(SegnalazioniDataIn dataIn)
		throws SegnalazioneQualificataServiceException{
		String cognome = (String) dataIn.getObj1();
		String tipo = (String)dataIn.getObj2();
		return segnalazioneQualificataDAO.getSuggestionCognomi(cognome, tipo);
	}
		
	@Override
	public List<Object> getSuggestionDenominazione(SegnalazioniDataIn dataIn)
		throws SegnalazioneQualificataServiceException{
		String denom = (String) dataIn.getObj1();
		return segnalazioneQualificataDAO.getSuggestionDenominazione(denom);
	}
		
	@Override
	public List<Object> getSuggestionNomi(SegnalazioniDataIn dataIn)
		throws SegnalazioneQualificataServiceException{
		String nome = (String) dataIn.getObj1();
		String tipo = (String)dataIn.getObj2();
		return segnalazioneQualificataDAO.getSuggestionNomi(nome, tipo);
	}
		
	@Override
	public List<Object> getSuggestionCodFisc(SegnalazioniDataIn dataIn)
		throws SegnalazioneQualificataServiceException{
		String codFisc = (String) dataIn.getObj1();
		String tipo = (String)dataIn.getObj2();
		return segnalazioneQualificataDAO.getSuggestionCodFisc(codFisc, tipo);
	}
	
	


}
