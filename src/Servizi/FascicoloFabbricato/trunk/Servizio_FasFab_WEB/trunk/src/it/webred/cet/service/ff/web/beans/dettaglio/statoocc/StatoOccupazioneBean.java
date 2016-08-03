package it.webred.cet.service.ff.web.beans.dettaglio.statoocc;

import it.webred.cet.service.ff.web.beans.dettaglio.DatiDettaglio;
import it.webred.cet.service.ff.web.beans.dettaglio.statoocc.dto.DettLicenzeCommercioDTO;
import it.webred.cet.service.ff.web.beans.dettaglio.statoocc.dto.DettLocazioniDTO;
import it.webred.cet.service.ff.web.beans.dettaglio.statoocc.dto.DettResidentiDTO;
import it.webred.cet.service.ff.web.beans.fonti.PermessiFonteBean;
import it.webred.ct.config.parameters.comune.ComuneService;
import it.webred.ct.data.access.basic.anagrafe.AnagrafeService;
import it.webred.ct.data.access.basic.anagrafe.dto.ComponenteFamigliaDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.IndirizzoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaIndirizzoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaSoggettoAnagrafeDTO;
import it.webred.ct.data.access.basic.indice.ricerca.IndiceCorrelazioneService;
import it.webred.ct.data.access.basic.indice.ricerca.KeyFabbricatoDTO;
import it.webred.ct.data.access.basic.indice.ricerca.RicercaIndiceDTO;
import it.webred.ct.data.access.basic.licenzecommercio.LicenzeCommercioService;
import it.webred.ct.data.access.basic.licenzecommercio.dto.RicercaLicenzeCommercioDTO;
import it.webred.ct.data.access.basic.locazioni.LocazioniService;
import it.webred.ct.data.access.basic.locazioni.dto.RicercaLocazioniDTO;
import it.webred.ct.data.model.anagrafe.SitDCivico;
import it.webred.ct.data.model.anagrafe.SitDPersona;
import it.webred.ct.data.model.licenzecommercio.SitLicenzeCommercio;
import it.webred.ct.data.model.locazioni.LocazioneBPK;
import it.webred.ct.data.model.locazioni.LocazioniA;
import it.webred.ct.data.model.locazioni.LocazioniB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

public class StatoOccupazioneBean extends DatiDettaglio implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private IndiceCorrelazioneService indService;
	private AnagrafeService anagService;
	private List<DettResidentiDTO> dettResidenti;
	private List<DettLocazioniDTO> dettLocazioni;
	private List<DettLicenzeCommercioDTO> dettLicenzeCommercio;
	
	private LocazioniService locService;
	private LicenzeCommercioService licCommService;
	private ComuneService comuneService;
	
	
	
	public void doSwitch() {
		logger.debug("STATO OCC DATA RIF ["+super.getDataRif()+"]");
		
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
		fabDTO.setDtVal(null);//LA DATA DI RIF PER LO STATO OCCUPAZIONE E' SEMPRE LA DATA DI SISTEMA
	
		ricInd.setObj(fabDTO);
		
		
			List<Object> objList =  indService.getCiviciCorrelatiFromFabbricato(ricInd);
		
			logger.debug("Size ["+objList.size()+"]");
			
			dettResidenti = new ArrayList<DettResidentiDTO>();
			
			for (Object o : objList) {
			
				SitDCivico civ = (SitDCivico) o;
				
				//super.getLogger().debug("-- Analisi civico ["+civ.getId()+"] --");
				logger.debug("-- Analisi civico ["+civ.getId()+"] --");
				
				RicercaIndirizzoAnagrafeDTO rAnagDTO = new RicercaIndirizzoAnagrafeDTO();
				
				rAnagDTO.setEnteId(super.getEnte());
				rAnagDTO.setUserId(super.getUsername());
				rAnagDTO.setSitDCivicoId(civ.getId());
				
				IndirizzoAnagrafeDTO indAnagDTO = anagService.getIndirizzo(rAnagDTO);
				
				//super.getLogger().debug("\tIndirizzo ["+indAnagDTO+"]");
				
				logger.debug("\tIndirizzo ["+indAnagDTO+"]");
				
				List<ComponenteFamigliaDTO> comFamList = anagService.getResidentiAlCivico(rAnagDTO);
				for (ComponenteFamigliaDTO comp: comFamList) {
					logger.debug("FF--->PERSONA: "+ comp.getPersona().getCognome() + " " + comp.getPersona().getNome() + "-"+ comp.getPersona().getId());
					String id = (comp.getPersona().getId()).replace(" ", "&");
					comp.getPersona().setId(id);
					logger.debug("ID PERSONA DA PASSARE ALLA PAGINA: " + id);
				}
				
				DettResidentiDTO res = new DettResidentiDTO();
				res.setIndirizzoAng(indAnagDTO);
				res.setComponentiFam(comFamList);
				
				dettResidenti.add(res);
		
		}
			
			
			//distinguo la colorazione delle righe in base al codice famiglia e determino il numero dei residenti
			
			
			for (DettResidentiDTO dettRes: dettResidenti){
				
				List<ComponenteFamigliaDTO> compFam= dettRes.getComponentiFam();
			
				boolean backgroundColor= false;
				String codiceFamigliaOld="";
				
				int numResidenti=0;
				for (ComponenteFamigliaDTO compFamItem: compFam){
					
					String codiceFamigliaNew=compFamItem.getIdExtDFamiglia();
					if (codiceFamigliaNew!= null && !codiceFamigliaNew.equals(codiceFamigliaOld)){
						compFamItem.setBackgroundColor(!backgroundColor);
						backgroundColor= !backgroundColor;
					}
					else{
						compFamItem.setBackgroundColor(backgroundColor);
					}
					codiceFamigliaOld= codiceFamigliaNew;
					
					numResidenti= numResidenti + 1;
				}
			
				
				
				dettRes.setNumTotaleResidenti(numResidenti);
			}
			
			
		
		
		
			dettLocazioni = new ArrayList<DettLocazioniDTO>();
			
			ricInd.setDestFonte("5");
			ricInd.setDestProgressivoEs("1");
			
			List<Object> objListLoc =  indService.getCiviciCorrelatiFromFabbricato(ricInd);
	
			logger.debug("Loc List da Indice ["+objListLoc+"]");
					
			for (Object o : objListLoc) {
				LocazioniA locA = (LocazioniA) o;
				RicercaLocazioniDTO rl = new RicercaLocazioniDTO();
				rl.setEnteId(super.getEnte());
				rl.setUserId(super.getUsername());			
				//rl.setDtRif(super.getDataRif());						
				
				LocazioneBPK pk = new LocazioneBPK();
				
				pk.setAnno(locA.getAnno());
				pk.setNumero(locA.getNumero());
				pk.setSerie(locA.getSerie());
				pk.setUfficio(locA.getUfficio());
				rl.setKey(pk);
				
				List<LocazioniA> locAList = locService.getLocazioniOggByKey(rl);
				LocazioniA ogg =null;
				if (locAList != null && locAList.size() > 0) {
					ogg = locAList.get(0);
					//(PER IL MOMENTO) FACCIAMO VEDERE TUTTO
					/*if (super.getDataRif() !=null  &&  ogg.getDataFine() != null && 
						ogg.getDataFine().compareTo(super.getDataRif()) >0 )
						continue;
					*/
				}else
					continue;
				logger.debug("Ogg Locazione da Locazioni_A ["+ogg+"]");
				List<LocazioniB> listaSogg = locService.getInquiliniByOgg(rl);
				DettLocazioniDTO locDTO=null;
			    if (listaSogg != null && listaSogg.size() >0 ){
			    	logger.debug("Inquilini n. ["+listaSogg.size()+"]");
			    	for (LocazioniB sogg: listaSogg ) {
			    		locDTO = new DettLocazioniDTO();
			    		locDTO.setOggetto(ogg);
			    		locDTO.setSoggetto(sogg);
			    		if (sogg.getCodicefiscale() != null ){
			    			String posizAnag="";
			    			try {
			    				Long.parseLong(sogg.getCodicefiscale());
			    				posizAnag="-"; // una partita iva
			    			} 
			    			catch (NumberFormatException nfe){
			       				RicercaSoggettoAnagrafeDTO rsa = new RicercaSoggettoAnagrafeDTO();
								rsa.setEnteId(super.getEnte());
								rsa.setUserId(super.getUsername());
								Date dtRifAnag = super.getDataRif();
								if (dtRifAnag == null)
									dtRifAnag = new Date();
								rsa.setDtRif(dtRifAnag);	
								rsa.setCodFis(sogg.getCodicefiscale());
								List<SitDPersona> listaP = anagService.getListaPersoneByCF(rsa) ;
								if (listaP != null && listaP.size() > 0) {
									logger.debug("ITEM SOGG IN ANAGRAFE["+listaP.size()+"]");
									for(SitDPersona p: listaP) {
										if (p.getPosizAna()!=null) {
											logger.debug("POSIZ.ANAG["+p.getPosizAna()+"]");
											if (!posizAnag.equals("") && !posizAnag.equals(p.getPosizAna())) {
												posizAnag="NON DETERMINATA";
												break;
											}else
												posizAnag=p.getPosizAna();
										}
									}
								}else
									posizAnag= "NON PRESENTE";
			    				}
							locDTO.setPosizAnagSoggetto(posizAnag);
						}
			    	}
			    }else{
			    	locDTO = new DettLocazioniDTO();
		    		locDTO.setOggetto(ogg);
			    }
	    		dettLocazioni.add(locDTO);	
			}
		
		
			dettLicenzeCommercio = new ArrayList<DettLicenzeCommercioDTO>();
		
			ricInd.setDestFonte("13");
			ricInd.setDestProgressivoEs("1");
			
			Hashtable<String, List<Object>> ht=indService.getCorrelazioniFabbricatoFromFabbricato(ricInd, false);
			List<Object> objListLc = ht.get(IndiceCorrelazioneService.LISTA_CORRELAZIONI_FABBRICATO);
			if (objListLc != null) {
				for (Object o : objListLc) {
					SitLicenzeCommercio lic = (SitLicenzeCommercio)o;
					//logger.debug("ID:  ["+lic.getId()+"]");
					RicercaLicenzeCommercioDTO rlc = new RicercaLicenzeCommercioDTO();
					rlc.setEnteId(super.getEnte());
					rlc.setUserId(super.getUsername());
					rlc.setId(lic.getId());
					SitLicenzeCommercio datiLic = licCommService.getLicenzaCommercioByID(rlc);
					/*
					if (super.getDataRif() != null)
						if (datiLic.getDataRilascio().compareTo(super.getDataRif())> 0)
							continue;
							*/
					DettLicenzeCommercioDTO dl = new DettLicenzeCommercioDTO();
					dl.setLicenzaCommercio(datiLic);
					String ind="";
					if (datiLic.getIdExtVie() !=null) {
						rlc.setId(null);
						rlc.setIdExtVia(datiLic.getIdExtVie());
						ind = licCommService.getViaLicenza(rlc);
						if (datiLic.getCivico() !=null ){
							ind += ", " + datiLic.getCivico();
						}
						if (datiLic.getCivico2() !=null ){
							ind += " " + datiLic.getCivico2();
						}
						if (datiLic.getCivico3() !=null ){
							ind += " " +datiLic.getCivico3();
						}
						dl.setIndirizzoLicenza(ind);
					}
					dettLicenzeCommercio.add(dl);
				}
				
			}
			
			logger.debug("Licenze comm ["+dettLicenzeCommercio.size()+"]");
		
	}


	public IndiceCorrelazioneService getIndService() {
		return indService;
	}


	public void setIndService(IndiceCorrelazioneService indService) {
		this.indService = indService;
	}


	public AnagrafeService getAnagService() {
		return anagService;
	}


	public void setAnagService(AnagrafeService anagService) {
		this.anagService = anagService;
	}


	public List<DettResidentiDTO> getDettResidenti() {
		return dettResidenti;
	}


	public void setDettResidenti(List<DettResidentiDTO> dettResidenti) {
		this.dettResidenti = dettResidenti;
	}


	public LocazioniService getLocService() {
		return locService;
	}


	public void setLocService(LocazioniService locService) {
		this.locService = locService;
	}


	public ComuneService getComuneService() {
		return comuneService;
	}


	public void setComuneService(ComuneService comuneService) {
		this.comuneService = comuneService;
	}


	public List<DettLocazioniDTO> getDettLocazioni() {
		return dettLocazioni;
	}


	public void setDettLocazioni(List<DettLocazioniDTO> dettLocazioni) {
		this.dettLocazioni = dettLocazioni;
	}


	public List<DettLicenzeCommercioDTO> getDettLicenzeCommercio() {
		return dettLicenzeCommercio;
	}


	public void setDettLicenzeCommercio(
			List<DettLicenzeCommercioDTO> dettLicenzeCommercio) {
		this.dettLicenzeCommercio = dettLicenzeCommercio;
	}


	public LicenzeCommercioService getLicCommService() {
		return licCommService;
	}


	public void setLicCommService(LicenzeCommercioService licCommService) {
		this.licCommService = licCommService;
	}



	

}
