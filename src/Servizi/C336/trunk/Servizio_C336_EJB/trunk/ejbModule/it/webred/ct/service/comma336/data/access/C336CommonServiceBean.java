package it.webred.ct.service.comma336.data.access;

import it.webred.ct.data.access.aggregator.catastoDocfaConcedilizie.CatastoDocfaConcedilizieService;
import it.webred.ct.data.access.aggregator.catastoDocfaConcedilizie.dto.SearchCriteriaDTO;
import it.webred.ct.data.access.basic.anagrafe.AnagrafeService;
import it.webred.ct.data.access.basic.anagrafe.dto.IndirizzoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaIndirizzoAnagrafeDTO;
import it.webred.ct.data.access.basic.c336.C336CommonServiceException;
import it.webred.ct.data.access.basic.c336.C336PraticaService;
import it.webred.ct.data.access.basic.c336.dto.C336CommonDTO;
import it.webred.ct.data.access.basic.c336.dto.C336PraticaDTO;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.ParametriCatastaliDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.SoggettoCatastoDTO;
import it.webred.ct.data.access.basic.common.dto.RicercaOggettoDTO;
import it.webred.ct.data.access.basic.concedilizie.ConcessioniEdilizieService;
import it.webred.ct.data.access.basic.concedilizie.dto.ConcessioneDTO;
import it.webred.ct.data.access.basic.concedilizie.dto.RicercaConcEdilizieDTO;
import it.webred.ct.data.access.basic.docfa.DocfaService;
import it.webred.ct.data.access.basic.docfa.dto.DatiGeneraliDocfaDTO;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.data.access.basic.fabbricato.FabbricatoService;
import it.webred.ct.data.access.basic.ici.IciService;
import it.webred.ct.data.access.basic.ici.dto.IciDTO;
import it.webred.ct.data.access.basic.ici.dto.RicercaOggettoIciDTO;
import it.webred.ct.data.access.basic.ici.dto.SoggOggIciDTO;
import it.webred.ct.data.access.basic.indice.ricerca.IndiceCorrelazioneService;
import it.webred.ct.data.access.basic.indice.ricerca.KeyFabbricatoDTO;
import it.webred.ct.data.access.basic.indice.ricerca.RicercaIndiceDTO;
import it.webred.ct.data.access.basic.tarsu.TarsuService;
import it.webred.ct.data.model.anagrafe.SitDCivico;
import it.webred.ct.data.model.c336.C336Allegato;
import it.webred.ct.data.model.c336.C336GesPratica;
import it.webred.ct.data.model.c336.C336GesPraticaPK;
import it.webred.ct.data.model.c336.C336GridAttribCatA2;
import it.webred.ct.data.model.c336.C336Pratica;
import it.webred.ct.data.model.c336.C336SkCarGenFabbricato;
import it.webred.ct.data.model.c336.C336SkCarGenUiu;
import it.webred.ct.data.model.c336.C336TabValIncrClsA4A3;
import it.webred.ct.data.model.c336.C336TabValIncrClsA5A6;
import it.webred.ct.data.model.catasto.Sitiuiu;
import it.webred.ct.data.model.concedilizie.SitCConcessioni;
import it.webred.ct.data.model.docfa.DocfaUiu;
import it.webred.ct.data.model.ici.SitTIciOggetto;
import it.webred.ct.data.model.ici.VTIciSoggAll;
import it.webred.ct.service.comma336.data.access.dto.C336SearchResultDTO;
import it.webred.ct.service.comma336.data.access.dto.DettGestionePraticaDTO;
import it.webred.ct.service.comma336.data.access.dto.DettInfoGeneraliPartDTO;
import it.webred.ct.service.comma336.data.access.dto.DettInfoGeneraliUiuDTO;
import it.webred.ct.service.comma336.data.access.dto.VerificheControlliPartDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Session Bean implementation class C336CommonServiceBean
 */
@Stateless
public class C336CommonServiceBean extends C336ServiceBaseBean implements C336CommonService {

	private static final long serialVersionUID = 1L;
	
	private C336PraticaService praticaService = (C336PraticaService) getEjb("CT_Service", "CT_Service_Data_Access", "C336PraticaServiceBean");
	
	private CatastoDocfaConcedilizieService catDcfConcediService = (CatastoDocfaConcedilizieService) getEjb("CT_Service", "CT_Service_Data_Access", "CatastoDocfaConcedilizieServiceBean");
	
	private CatastoService catastoService = (CatastoService) getEjb("CT_Service", "CT_Service_Data_Access", "CatastoServiceBean");

	private ConcessioniEdilizieService concessioniService = (ConcessioniEdilizieService) getEjb("CT_Service", "CT_Service_Data_Access", "ConcessioniEdilizieServiceBean");

	private DocfaService docfaService = (DocfaService) getEjb("CT_Service", "CT_Service_Data_Access", "DocfaServiceBean");
	
	private AnagrafeService anagrafeService = (AnagrafeService) getEjb("CT_Service", "CT_Service_Data_Access", "AnagrafeServiceBean");
	
	private IciService iciService = (IciService) getEjb("CT_Service", "CT_Service_Data_Access", "IciServiceBean");
	
	private TarsuService tarsuService = (TarsuService) getEjb("CT_Service", "CT_Service_Data_Access", "TarsuServiceBean");
	
	private IndiceCorrelazioneService indiceService = (IndiceCorrelazioneService) getEjb("CT_Service", "CT_Service_Data_Access", "IndiceCorrelazioneServiceBean");
	
	private FabbricatoService fabbricatoService = (FabbricatoService) getEjb("CT_Service", "CT_Service_Data_Access", "FabbricatoServiceBean");
	
	private void fillParametriCatastali(SearchCriteriaDTO ro){
		logger.debug("fillParametriCatastali()"); 
		String ente = ro.getEnteId();
		String foglio = ro.getUiu().getFoglio();
		String particella = ro.getUiu().getParticella();
		String unimm = ro.getUiu().getSubalterno();
		
		logger.debug("Ente ["+ ente+"]"); 
		logger.debug("Foglio ["+ foglio+"]"); 
		logger.debug("Particella ["+ particella+"]");
		logger.debug("Subalterno ["+ unimm +"]"); 
		
		//Imposto FPS sulla ricerca in catasto
		ro.getCatOggetto().setCodNazionale(ente);
		ro.getCatOggetto().setFoglio(foglio);
		ro.getCatOggetto().setParticella(particella);
		ro.getCatOggetto().setUnimm(unimm);
		
		//Imposto FPS sulla ricerca in docfa
		ro.getDcfOggetto().setFoglio(foglio);
		ro.getDcfOggetto().setParticella(particella);
		ro.getDcfOggetto().setUnimm(unimm);
		
		//Imposto FPS sulla ricerca in concessioni edilizie
		ro.getConcEdiOggetto().setFoglio(foglio);
		ro.getConcEdiOggetto().setParticella(particella);
		ro.getConcEdiOggetto().setSub(unimm);
		
		//Imposto FPS sulla ricerca in C336 Pratiche
		ro.getC336Pratica().setFoglio(foglio);
		ro.getC336Pratica().setParticella(particella);
		ro.getC336Pratica().setSub(unimm);
	}
	
	private void fillAmbitiDiRicerca(SearchCriteriaDTO ro){
		ro.setFaiRicercaInC336Pratica(false);
		if (ro.getC336Pratica() != null &&
		   (ro.getC336Pratica().getCodStato() != null && ! ro.getC336Pratica().getCodStato().equals(""))) {
			ro.setFaiRicercaInC336Pratica(true);
		}
		if (ro.getTipoRicerca() !=null  ) {
			/*In questo caso i flag: faiRicercaInCatastoUrbano e faiRicercaInCatastoTerreni sono impostati dall'operatore
			 * nella videata di filtro
			 */
			ro.setFaiRicercaInDocfa(false);
			ro.setFaiRicercaInConEdi(false);
			if (ro.getDcfOggetto() != null &&
				(ro.getDcfOggetto().getDataRegistrazioneDa()!= null && !ro.getDcfOggetto().getDataRegistrazioneA().equals("")) ||
				(ro.getDcfOggetto().getDataRegistrazioneA() !=null && !ro.getDcfOggetto().getDataRegistrazioneA().equals("") )||
	   			(ro.getDcfOggetto().getFornituraDa() != null && !ro.getDcfOggetto().getFornituraDa().equals("") )|| 
	   			(ro.getDcfOggetto().getFornituraA() !=null && !ro.getDcfOggetto().getFornituraA().equals("") ) ||
				(ro.getDcfOggetto().getProtocollo() != null && !ro.getDcfOggetto().getProtocollo().equals("")) ||
				(ro.getDcfOggetto().getFlgCommiFin2005() != null && !ro.getDcfOggetto().getFlgCommiFin2005().equals(""))) {
				ro.setFaiRicercaInDocfa(true);
			}
			if (ro.getTipoRicerca().equals("maiDic") ){
				//In questo caso il flag faiRicercaInFabbrMaiDich è impostato dall'operatore nella videata di filtro
				ro.setFaiRicercaInFabbrExRurali(false);
			} else {
				//In questo caso il flag faiRicercaInFabbrExRurali è impostato dall'operatore nella videata di filtro
				ro.setFaiRicercaInFabbrMaiDich(false);
			}
		}else {
			ro.getCatOggetto().setFaiRicercaInCatastoTerreni(false);
			ro.setFaiRicercaInFabbrExRurali(false);
			ro.setFaiRicercaInFabbrMaiDich(false);
		}
	
	}
	
	public Long searchCountImmobili(SearchCriteriaDTO ro){
		fillParametriCatastali(ro);
		fillAmbitiDiRicerca(ro);
		//ro.getConcEdiOggetto().setTipoCatasto("URBANO");
		return  catDcfConcediService.ricercaDisgiuntaSearchCount(ro);
	}
	
	public List<C336SearchResultDTO> search(SearchCriteriaDTO ro){
		List<C336SearchResultDTO> list = new ArrayList<C336SearchResultDTO>();
		if (ro.getTipoRicerca() !=null  ) {
			///si produce in ogni caso la lista delle particelle
			list= searchFabbricatiMaiDich(ro);
			/*
			if (ro.getTipoRicerca().equals("maiDic") ){
				list= searchFabbricatiMaiDich(ro);
			} else {
				list= searchFabbricatiExRurali(ro);
			}*/
		}else
			list= searchImmobili(ro);
		return list;
			
	}
	
	public List<C336SearchResultDTO> searchImmobili(SearchCriteriaDTO ro){
		logger.debug("searchImmobili()");
		List<C336SearchResultDTO> result = new ArrayList<C336SearchResultDTO>();
		
		fillParametriCatastali(ro);
		fillAmbitiDiRicerca(ro);
		//ro.getConcEdiOggetto().setTipoCatasto("URBANO");
		
		//Recupero la listona ottenuta unendo tutti i parametri di ricerca sulle tre banche dati
		List<ParametriCatastaliDTO> listauiu = catDcfConcediService.ricercaDisgiuntaListaUiu(ro);
		
		for(ParametriCatastaliDTO uiu : listauiu){
		
			C336SearchResultDTO res = new C336SearchResultDTO();
			//Per ciscuna uiu ottenuta verifico se esiste nelle tre banche dati
			
			res.setId(uiu.getFoglio()+"@"+uiu.getParticella()+"@"+uiu.getSubalterno());
			
			res.setUiu(uiu);
			
			//Impostare flgPresenzaCatasto
			RicercaOggettoCatDTO csc = new RicercaOggettoCatDTO();
			csc.setEnteId(ro.getEnteId());
			csc.setFoglio(uiu.getFoglio());
			csc.setParticella(uiu.getParticella());
			csc.setUnimm(uiu.getSubalterno());
			List<Sitiuiu> listaUiu = catastoService.getListaImmobiliByFPS(csc);
			res.setFlgPresCatasto(listaUiu.size()>0);
			
			//Impostare flgPresenzaDocfa
			RicercaOggettoDocfaDTO rod= new RicercaOggettoDocfaDTO();
			rod.setEnteId(ro.getEnteId());
			rod.setSezione(uiu.getSezione());
			rod.setFoglio(uiu.getFoglio());
			rod.setParticella(uiu.getParticella());
			rod.setUnimm(uiu.getSubalterno());
			List<DocfaUiu> listDocfa = docfaService.getListaDocfaUiuByUI(rod);
			res.setFlgPresDocfa(listDocfa.size()>0);
			
			//Impostare flgPresenzaConcEdilizie
			RicercaConcEdilizieDTO rce = new RicercaConcEdilizieDTO();
			rce.setEnteId(ro.getEnteId());
			rce.setFoglio(uiu.getFoglio());
			rce.setParticella(uiu.getParticella());
			rce.setSub(uiu.getSubalterno());
			List<SitCConcessioni> listaConc = concessioniService.getConcessioniByUiu(rce);
			res.setFlgPresConcEdilizie(listaConc.size()>0);
			
			//stato pratica
			RicercaOggettoDTO ricObj = new RicercaOggettoDTO();
			ricObj.setEnteId(ro.getEnteId());
			ricObj.setFoglio(uiu.getFoglio());
			ricObj.setParticella(uiu.getParticella());
			ricObj.setSub(uiu.getSubalterno());
			C336Pratica pra = praticaService.getPraticaApertaByOggetto(ricObj);
			if (pra==null)  
				pra = praticaService.getPraticaChiusaByOggetto(ricObj);
			if (pra!=null)
				res.setStatoPratica(C336PraticaDTO.STATI.get(pra.getCodStato()));
			else
				res.setStatoPratica(C336PraticaDTO.STATI.get(C336PraticaDTO.COD_STATO_NON_ESAMINATA));
			
			result.add(res);
		}
		
		return result;
	}
	
	public DettInfoGeneraliUiuDTO getDettInfoGeneraliImmobile(RicercaOggettoDTO ro) {
			
		DettInfoGeneraliUiuDTO infoGen = new DettInfoGeneraliUiuDTO();

		try {
			
			// Informazioni Catastali Immobile
			RicercaOggettoCatDTO csc = new RicercaOggettoCatDTO();
			csc.setEnteId(ro.getEnteId());
			csc.setFoglio(ro.getFoglio());
			csc.setParticella(ro.getParticella());
			csc.setUnimm(ro.getSub());
			List<Sitiuiu> listaUiu = catastoService.getListaImmobiliByFPS(csc);
			infoGen.setListaSitiuiu(listaUiu);
			
			// Lista Soggetti Catastali Immobile
			RicercaOggettoCatDTO roc = new RicercaOggettoCatDTO();
			roc.setEnteId(ro.getEnteId());
			roc.setFoglio(ro.getFoglio());
			roc.setParticella(ro.getParticella());
			roc.setUnimm(ro.getSub());
			List<SoggettoCatastoDTO> listaCatSogg = catastoService.getListaSoggettiUiuByFPS(roc);
			infoGen.setListaTitolariCatasto(listaCatSogg);
			
			// Pratiche DOCFA Immobile
			RicercaOggettoDocfaDTO roDocfa = new RicercaOggettoDocfaDTO();
			roDocfa.setEnteId(ro.getEnteId());  //Data-Routing
			roDocfa.setFoglio(ro.getFoglio());
			roDocfa.setParticella(ro.getParticella());
			roDocfa.setUnimm(ro.getSub());
			roDocfa.setEstraiTutteLeUI(true);
			List<DatiGeneraliDocfaDTO> docfaUiu = docfaService.getListaDatiDocfaImmobile(roDocfa);
			infoGen.setListaDocfa(docfaUiu);
			
			// Lista Conc.Edilizie 
			RicercaConcEdilizieDTO rce = new RicercaConcEdilizieDTO();
			rce.setEnteId(ro.getEnteId());
			rce.setFoglio(ro.getFoglio());
			rce.setParticella(ro.getParticella());
			rce.setSub(ro.getSub());
			List<ConcessioneDTO> concEdi = concessioniService.getDatiConcessioniByImmobile(rce);
			infoGen.setListaConcEdi(concEdi);
			
			//Informazioni Tributi ICI
			RicercaOggettoIciDTO roi = new RicercaOggettoIciDTO();
			roi.setEnteId(ro.getEnteId());
			List<String>provenienza = iciService.getListaProvenienzaIci(roi);
			List<IciDTO> listaIci = new ArrayList<IciDTO>();
			for(String p:provenienza){
				ro.setProvenienza(p);
				List<IciDTO> listaIciProv = iciService.getListaIciByFPSOgg(ro);
				listaIci.addAll(listaIciProv);
			}
			
			List<SitTIciOggetto> oggetti = new ArrayList<SitTIciOggetto>();
			List<SoggOggIciDTO> soggetti = new ArrayList<SoggOggIciDTO>();
			for(IciDTO ici : listaIci){
				for(VTIciSoggAll sogg : ici.getSoggetti()){
					SoggOggIciDTO soggetto = new SoggOggIciDTO();
					soggetto.setOggetto(ici.getOggetto());
					soggetto.setSoggetto(sogg);
					soggetti.add(soggetto);
				}
				oggetti.add(ici.getOggetto());
			}
			
			infoGen.setListaOggettiIci(oggetti);
			infoGen.setListaSoggettiIci(soggetti);
			
			

		} catch (Throwable t) {
			logger.error("",t);
			throw new C336CommonServiceException(t);
		}

		return infoGen;
	}
	
	public DettInfoGeneraliPartDTO getDettInfoGeneraliParticella(RicercaOggettoDTO ro) {
		
		DettInfoGeneraliPartDTO infoGen = new DettInfoGeneraliPartDTO();

		try {
			
			// Informazioni Catastali Terreni su Foglio e Particella
			
			RicercaOggettoCatDTO roc = new RicercaOggettoCatDTO();
			roc.setEnteId(ro.getEnteId());
			roc.setFoglio(ro.getFoglio());
			roc.setParticella(ro.getParticella());
			 
			infoGen.setListaTerreniAttuali(catastoService.getListaTerreniByFP(roc));
			infoGen.setListaTerreniStorico(catastoService.getListaStoricoTerreniByFP(roc));
			
			infoGen.setListaUiuAttuali(catastoService.getListaUiAllaData(roc));
			
			List<Sitiuiu> listaStorico = new ArrayList<Sitiuiu>();
			List<Sitiuiu> listaUiu = catastoService.getListaImmobiliByFP(roc);
			Date today = new Date();
			for(Sitiuiu uiu : listaUiu){
				Date dataFine  =  uiu.getId().getDataFineVal();
				if(dataFine.before(today))
					listaStorico.add(uiu);
				else
					logger.debug("DataFineUiu: " + dataFine);
			}
			infoGen.setListaUiuStorico(listaStorico);
			
			// Pratiche DOCFA Immobile
			RicercaOggettoDocfaDTO roDocfa = new RicercaOggettoDocfaDTO();
			roDocfa.setEnteId(ro.getEnteId());  //Data-Routing
			roDocfa.setFoglio(ro.getFoglio());
			roDocfa.setParticella(ro.getParticella());
			roDocfa.setEstraiTutteLeUI(true);
			List<DatiGeneraliDocfaDTO> docfaUiu = docfaService.getListaDatiDocfaFabbricato(roDocfa);
			infoGen.setListaDocfa(docfaUiu);
			infoGen.setListaExRurali(fabbricatoService.getListaFabbricatiExRurali(ro));
			infoGen.setListaMaiDichiarati(fabbricatoService.getListaFabbricatiMaiDichiarati(ro));
		

		} catch (Throwable t) {
			logger.error("",t);
			throw new C336CommonServiceException(t);
		}

		return infoGen;
	}


public List<VerificheControlliPartDTO> getVerificheControlliFabbricato(RicercaOggettoDTO ro){
		
		List<VerificheControlliPartDTO> lista = new ArrayList<VerificheControlliPartDTO>();
		
		//Ricerca nell'indice di correlazione
		KeyFabbricatoDTO keyF = new KeyFabbricatoDTO();
		keyF.setEnteId(ro.getEnteId());
		keyF.setCodNazionale(ro.getEnteId());
		keyF.setFoglio(ro.getFoglio());
		keyF.setParticella(ro.getParticella());
		keyF.setSezione(ro.getSezione());
		
		RicercaIndiceDTO ri = new RicercaIndiceDTO();
		ri.setEnteId(ro.getEnteId());
		ri.setObj(keyF);
		lista.add(this.getControlliFabbricatoIndice("Concessioni Edilizie", "3", "2", ri));
		lista.add(this.getControlliFabbricatoIndice("Dichiarazioni Ici", "2", "2", ri));
		lista.add(this.getControlliFabbricatoIndice("Dichiarazioni Tarsu", "2", "3", ri));
		lista.add(this.getControlliFabbricatoIndice("Utenze Enel", "10", "2", ri));
		//lista.add(this.getVerificheControlliFabbricato("Utenze Acqua",  ricUiu));
		
		//Ricerca in Anagrafe solo per CIVICO
		VerificheControlliPartDTO dtoAnag = new VerificheControlliPartDTO();
		dtoAnag.setArchivio("Residenti");
		
		ri.setDestFonte("1");
		ri.setDestProgressivoEs("1");
		
		List<Object> lstCivAnagrafe = indiceService.getCiviciCorrelatiFromFabbricato(ri);
		logger.debug("Numero Civici in Anagrafe: " + lstCivAnagrafe.size());
		int i = 0;
		boolean trovato = false;
		while (i<lstCivAnagrafe.size()&& !trovato) {
			Object o = lstCivAnagrafe.get(i);
			SitDCivico civ = (SitDCivico) o;
			
			RicercaIndirizzoAnagrafeDTO rAnagDTO = new RicercaIndirizzoAnagrafeDTO();
			
			rAnagDTO.setEnteId(ro.getEnteId());
			rAnagDTO.setUserId(ro.getUserId());
			rAnagDTO.setSitDCivicoId(civ.getId());
			
			IndirizzoAnagrafeDTO indAnagDTO = anagrafeService.getIndirizzo(rAnagDTO);
			
			trovato = anagrafeService.getResidentiAlCivico(rAnagDTO).size()>0;
			i++;
		}
		
		dtoAnag.setFlgPresCivico(trovato);
		lista.add(dtoAnag);
		
		return lista;
	}


	private VerificheControlliPartDTO getControlliFabbricatoIndice(String archivio, String destFonte, String destProgressivoEs, RicercaIndiceDTO ri){
		
		VerificheControlliPartDTO dto = new VerificheControlliPartDTO();
		dto.setArchivio(archivio);
		
		ri.setDestFonte(destFonte);
		ri.setDestProgressivoEs(destProgressivoEs);
		
		//Recuperare flag e impostare
		Hashtable<String,  List<Object>> result = indiceService.getCorrelazioniFabbricatoFromFabbricato(ri, true);
		List listaFabbricati = result.get("LISTA_CORRELAZIONI_FABBRICATO_DIRETTE@IndiceCorrelazioneService");
		List listaCivici = result.get("LISTA_CORRELAZIONI_FABBRICATO_INDIRETTE@IndiceCorrelazioneService");
	
		Boolean flgPresParticella = listaFabbricati.size()>0;
		Boolean flgPresCivico = listaCivici.size()>0;
		
		dto.setFlgPresParticella(flgPresParticella);
		dto.setFlgPresCivico(flgPresCivico);
		
  
 /* 		//Ricerca su particella
		RicercaOggettoCatDTO roc = new RicercaOggettoCatDTO();
		roc.setEnteId(ro.getEnteId());
		roc.setFoglio(ro.getFoglio());
		roc.setParticella(ro.getParticella());
		
		List<IndirizzoDTO> catIndirizzi = catastoService.getListaIndirizziPartAllaData(roc);
		String[] idsCivici = new String[catIndirizzi.size()];
		for(int i=0; i<catIndirizzi.size();i++)
			idsCivici[i]= catIndirizzi.get(i).getIdCivico();
		
		//Ricerca su civici
		RicercaCivicoDTO rc = new RicercaCivicoDTO();
		rc.setEnteId(ro.getEnteId());
		rc.setListaIdCivici(idsCivici);
		
		//Conc.Edilizie
		if(archivio.equals("Concessioni Edilizie")){
			
			//Ricerca per Particella
			List<SitCConcessioni> lista = concessioniService.getConcessioniByFabbricato(roc);
			flgPresParticella = lista.size()>0;
			
			//Ricerca per civico
			List<SitCConcessioni> listac = concessioniService.getConcessioniSuCiviciCatasto(rc);
			flgPresCivico = listac.size()>0;
		
			
		}
		
		//Residenti
		if(archivio.equals("Residenti")){
			
			//flgPresCivico = listac.size()>0
			
		}
		
		//Dich.Ici
		if(archivio.equals("Dichiarazioni Ici")){
			
			List<SitTIciOggetto> lista = iciService.getOggettiByFabbricato(ro);
			flgPresParticella = lista.size()>0;
			
			//Ricerca per civico
			List<SitTIciOggetto> listac = iciService.getDichiarazioniSuCiviciCatasto(rc);
			flgPresCivico = listac.size()>0;
		}
		
		//Dich.Tarsu
		if(archivio.equals("Dichiarazioni Tarsu")){
			
			
			List<SitTTarOggetto> lista = tarsuService.getListaOggettiByFabbricato(ro);
			flgPresParticella = lista.size()>0;
			
			//Ricerca per civico
			List<SitTTarOggetto> listac = tarsuService.getDichiarazioniSuCiviciCatasto(rc);
			flgPresCivico = listac.size()>0;
		}
		
		//Enel
		if(archivio.equals("Utenze Enel")){
			
		}
		
		//Acqua
		if(archivio.equals("Utenze Acqua")){}*/
	
		
		return dto;
	}

	public List<C336SearchResultDTO> searchFabbricatiMaiDich(SearchCriteriaDTO ro) {
		logger.debug("searchParticelle()");
		List<C336SearchResultDTO> result = new ArrayList<C336SearchResultDTO>();
		fillParametriCatastali(ro);
		fillAmbitiDiRicerca(ro);
		//
		List<ParametriCatastaliDTO> listaP = catDcfConcediService.ricercaDisgiuntaListaParticelle(ro);
		for(ParametriCatastaliDTO part : listaP){
			
			C336SearchResultDTO res = new C336SearchResultDTO();
			res.setId(part.getFoglio()+"@"+part.getParticella()+"@0000");
			
			res.setUiu(part);
			res.setFlgPresCatasto(false);
			
			res.setFlgPresCatasto(false);
			res.setFlgPresDocfa(false);
			res.setFlgPresConcEdilizie(false);
			//stato pratica
			RicercaOggettoDTO ricObj = new RicercaOggettoDTO();
			ricObj.setEnteId(ro.getEnteId());
			ricObj.setFoglio(part.getFoglio());
			ricObj.setParticella(part.getParticella());
			C336Pratica pra = praticaService.getPraticaApertaByOggetto(ricObj);
			if (pra==null)  
				pra = praticaService.getPraticaChiusaByOggetto(ricObj);
			if (pra!=null)
				res.setStatoPratica(C336PraticaDTO.STATI.get(pra.getCodStato()));
			else
				res.setStatoPratica(C336PraticaDTO.STATI.get(C336PraticaDTO.COD_STATO_NON_ESAMINATA));
			result.add(res);
		}
		
		return result;
		
	}


	public List<C336SearchResultDTO> searchFabbricatiExRurali(SearchCriteriaDTO ro) {
		logger.debug("searchFabbricatiExRurali()");
		List<C336SearchResultDTO> result = new ArrayList<C336SearchResultDTO>();
		fillParametriCatastali(ro);
		fillAmbitiDiRicerca(ro);		
		//Recupero la listona ottenuta unendo tutti i parametri di ricerca sulle tre banche dati
		List<ParametriCatastaliDTO> listauiu = catDcfConcediService.ricercaDisgiuntaListaUiu(ro);
		
		for(ParametriCatastaliDTO uiu : listauiu){
		
			C336SearchResultDTO res = new C336SearchResultDTO();
			res.setId(uiu.getFoglio()+"@"+uiu.getParticella()+"@"+uiu.getSubalterno());
			//
			//stato pratica
			RicercaOggettoDTO ricObj = new RicercaOggettoDTO();
			ricObj.setEnteId(ro.getEnteId());
			ricObj.setFoglio(uiu.getFoglio());
			ricObj.setParticella(uiu.getParticella());
			ricObj.setSub(uiu.getSubalterno());
			C336Pratica pra = praticaService.getPraticaApertaByOggetto(ricObj);
			if (pra==null)  
				pra = praticaService.getPraticaChiusaByOggetto(ricObj);
			if (pra!=null)
				res.setStatoPratica(C336PraticaDTO.STATI.get(pra.getCodStato()));
			else
				res.setStatoPratica(C336PraticaDTO.STATI.get(C336PraticaDTO.COD_STATO_NON_ESAMINATA));
			res.setUiu(uiu);
			result.add(res);
		}
		
		return result;
	}
	
	public DettGestionePraticaDTO getDatiGestionePratica(RicercaOggettoDTO ro) {
		DettGestionePraticaDTO dettGesPra = getDatiBasePratica(ro);
		C336PraticaDTO praDto = dettGesPra.getDatiPratica();
	//	logger.debug("getDatiGestionePratica-->idPra: " + praDto.getPratica().getIdPratica());
		//decodifica codice irregolarità
		C336CommonDTO dto = new C336CommonDTO();
		dto.setEnteId(ro.getEnteId());
		dto.setCodIrreg(praDto.getPratica().getCodIrregAccertata());
		praDto.setDesIrregolarita(praticaService.getDesIrregolarita(dto));
		
		//lista operatori
		praDto.setEnteId(ro.getEnteId());
		List<C336GesPratica> listaOperatori  = praticaService.getListaOperatoriPratica(praDto);
		dettGesPra.setListaOperatoriPratica(listaOperatori);
		//allegati
		List<C336Allegato> listaAllegato  = praticaService.getListaAllegatiPratica(praDto);
		dettGesPra.setListaAllegatiPratica(listaAllegato);
		//schede / tabelle / griglia di valutazione 
		C336SkCarGenFabbricato schedaFabbricato = praticaService.getSkGeneraleFabbricato(praDto);
		C336SkCarGenUiu schedaUiu = praticaService.getSkGeneraleUiu(praDto);
		
		C336TabValIncrClsA4A3 tabellaValutIncrA4A3 = praticaService.getTabValutIncrClasseA4A3(praDto);
		C336TabValIncrClsA5A6 tabellaValutIncrA5A6 = praticaService.getTabValutIncrClasseA5A6(praDto);
		C336GridAttribCatA2 grigliaAttribA2 = praticaService.getGridAttribCat2(praDto);
		dettGesPra.setSchedaFabbricato(schedaFabbricato);
		dettGesPra.setSchedaUiu(schedaUiu);
		dettGesPra.setTabellaValutIncrA4A3(tabellaValutIncrA4A3);
		dettGesPra.setTabellaValutIncrA5A6(tabellaValutIncrA5A6);
		dettGesPra.setGrigliaAttribA2(grigliaAttribA2);
		
		return dettGesPra;
	}
		
	public DettGestionePraticaDTO getDatiBasePratica(RicercaOggettoDTO ro) {
		logger.debug("getDatiTestataPratica");
		DettGestionePraticaDTO dettGesPra = new DettGestionePraticaDTO();
		C336Pratica pra = praticaService.getPraticaApertaByOggetto(ro);
		C336PraticaDTO praDto = new C336PraticaDTO();
		praDto.setEnteId(ro.getEnteId());
		praDto.setPratica(pra);
		if (pra== null) {
			pra = praticaService.getPraticaChiusaByOggetto(ro);
			// recupera pratica chiusa, se c'è
			if (pra==null)
				return dettGesPra;
			else {
				//si upera la "gestione attuale" che nel caso della pratica chiusa è l'ultima gestione
				praDto.setPratica(pra);
				C336GesPratica ges = new C336GesPratica ();
				C336GesPraticaPK pk = new C336GesPraticaPK();
				pk.setIdPratica(pra.getIdPratica());
				pk.setDtIniGes(pra.getDtIni());
				pk.setUserName(pra.getUserNameIni());
				ges.setId(pk);
				ges.setDtFinGes(pra.getDtFin());
				praDto.setGesAttualePratica(ges);
			}
		}
		else {
			//gestione attuale 
			C336GesPratica ges = praticaService.getGesAttualePratica(praDto);
			praDto.setGesAttualePratica(ges);
			//dettGesPra.getDatiPratica().setDesStatoIstruttoria(C336PraticaDTO.COD_STATO_ISTRUTTORIA_IN_CORSO);
		}
		logger.debug("getDatiTestataPratica-->idPra: " + praDto.getPratica().getIdPratica());
		dettGesPra.setDatiPratica(praDto);
		super.logger.debug("getDatiTestataPratica- fine ");
		return dettGesPra;
	}
		
	protected Object getEjb(String ear, String module, String ejbName){
		Context cont;
		try {
			cont = new InitialContext();
			return cont.lookup("java:global/" + ear + "/" + module + "/" + ejbName);
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
