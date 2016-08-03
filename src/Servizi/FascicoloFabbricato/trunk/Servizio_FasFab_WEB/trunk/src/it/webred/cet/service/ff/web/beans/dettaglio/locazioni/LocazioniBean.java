package it.webred.cet.service.ff.web.beans.dettaglio.locazioni;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import it.webred.cet.service.ff.web.beans.dettaglio.DatiDettaglio;
import it.webred.cet.service.ff.web.beans.dettaglio.statoocc.dto.DettLocazioniDTO;
import it.webred.ct.data.access.basic.indice.ricerca.IndiceCorrelazioneService;
import it.webred.ct.data.access.basic.indice.ricerca.KeyFabbricatoDTO;
import it.webred.ct.data.access.basic.indice.ricerca.RicercaIndiceDTO;
import it.webred.ct.data.access.basic.locazioni.LocazioniService;
import it.webred.ct.data.access.basic.locazioni.dto.RicercaLocazioniDTO;
import it.webred.ct.data.model.locazioni.LocazioneBPK;
import it.webred.ct.data.model.locazioni.LocazioniA;
import it.webred.ct.data.model.locazioni.LocazioniB;

public class LocazioniBean extends DatiDettaglio implements Serializable {

	private IndiceCorrelazioneService indService;
	private LocazioniService locService;
	private List<DettLocazioniDTO> dettLocazioni;
	private String anagNome;
	private String anagCogn;
	private String anagCF;

	@Override
	public void doSwitch() {
	}
	
	
	public void doFindLocazioni() {
		
		RicercaIndiceDTO ricInd = new RicercaIndiceDTO();
		ricInd.setDestFonte("1");
		ricInd.setDestProgressivoEs("1");
		ricInd.setEnteId(super.getEnte());
		ricInd.setUserId(super.getUsername());
		
		KeyFabbricatoDTO fabDTO = new KeyFabbricatoDTO();
		
		fabDTO.setCodNazionale(super.getCodNazionale());
		fabDTO.setFoglio(super.getFoglio());
		fabDTO.setParticella(super.getParticella());
		fabDTO.setSezione(super.getSezione());
		fabDTO.setDtVal(super.getDataRif());
		
		ricInd.setObj(fabDTO);
		
		
		ricInd.setDestFonte("5");
		ricInd.setDestProgressivoEs("2");
		
		List<Object> objListLoc =  indService.getCiviciCorrelatiFromFabbricato(ricInd);

		logger.debug("Locazioni ["+objListLoc+"]");
		dettLocazioni = new ArrayList<DettLocazioniDTO>();

		for (Object o : objListLoc) {
			
			LocazioniB locB = (LocazioniB) o;
			
			RicercaLocazioniDTO rl = new RicercaLocazioniDTO();
			rl.setEnteId(super.getEnte());
			rl.setUserId(super.getUsername());			
			rl.setDtRif(super.getDataRif());						
			
			LocazioneBPK pk = new LocazioneBPK();
			
			pk.setAnno(locB.getAnno());
			pk.setNumero(locB.getNumero());
			pk.setProgSoggetto(locB.getProgSoggetto());
			pk.setSerie(locB.getSerie());
			pk.setUfficio(locB.getUfficio());
			rl.setKey(pk);
			
			List<LocazioniA> locAList = locService.getLocazioniOggByKey(rl);
			
			DettLocazioniDTO locDTO = new DettLocazioniDTO();
			
			if (locAList != null && locAList.size() > 0) 
				locDTO.setOggetto(locAList.get(0));				
			
			
			
			LocazioniB locBFull = locService.getLocazioneSoggByKey(rl);
			locDTO.setSoggetto(locBFull);
			locDTO.setTipoSoggLocazione("");
    		if (locBFull.getTipoSoggetto().equals("D"))
    			locDTO.setTipoSoggLocazione("Proprietario");
    		if (locBFull.getTipoSoggetto().equals("A"))
    			locDTO.setTipoSoggLocazione("Inquilino");
			
			//logger.debug("Tipo sogg ["+locBFull.getTipoSoggetto()+"]");
			
			if (locBFull.getCodicefiscale().equalsIgnoreCase(anagCF))
				dettLocazioni.add(locDTO);

			
		}
	}


	public String getAnagNome() {
		return anagNome;
	}


	public void setAnagNome(String anagNome) {
		this.anagNome = anagNome;
	}


	public String getAnagCogn() {
		return anagCogn;
	}


	public void setAnagCogn(String anagCogn) {
		this.anagCogn = anagCogn;
	}


	public String getAnagCF() {
		return anagCF;
	}


	public void setAnagCF(String anagCF) {
		this.anagCF = anagCF;
	}


	public IndiceCorrelazioneService getIndService() {
		return indService;
	}


	public void setIndService(IndiceCorrelazioneService indService) {
		this.indService = indService;
	}


	public LocazioniService getLocService() {
		return locService;
	}


	public void setLocService(LocazioniService locService) {
		this.locService = locService;
	}


	public List<DettLocazioniDTO> getDettLocazioni() {
		return dettLocazioni;
	}


	public void setDettLocazioni(List<DettLocazioniDTO> dettLocazioni) {
		this.dettLocazioni = dettLocazioni;
	}



	

}
