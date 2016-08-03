import it.webred.cet.permission.CeTUser;
import it.webred.ct.data.access.basic.anagrafe.AnagrafeService;
import it.webred.ct.data.access.basic.anagrafe.dto.ComponenteFamigliaDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.DatiPersonaDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.IndirizzoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaIndirizzoAnagrafeDTO;
import it.webred.ct.data.access.basic.anagrafe.dto.RicercaSoggettoAnagrafeDTO;
import it.webred.ct.data.access.basic.catasto.CatastoService;
import it.webred.ct.data.access.basic.catasto.dto.CatastoSearchCriteria;
import it.webred.ct.data.access.basic.catasto.dto.IndirizzoDTO;
import it.webred.ct.data.access.basic.catasto.dto.ParticellaKeyDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaOggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.RicercaSoggettoCatDTO;
import it.webred.ct.data.access.basic.catasto.dto.SintesiImmobileDTO;
import it.webred.ct.data.access.basic.catasto.dto.TerrenoPerSoggDTO;
import it.webred.ct.data.access.basic.common.dto.RicercaOggettoDTO;
import it.webred.ct.data.access.basic.common.dto.RicercaSoggettoDTO;
import it.webred.ct.data.access.basic.compravendite.CompravenditeMUIService;
import it.webred.ct.data.access.basic.compravendite.dto.RicercaCompravenditeDTO;
import it.webred.ct.data.access.basic.compravendite.dto.SoggettoCompravenditeDTO;
import it.webred.ct.data.access.basic.concedilizie.ConcessioniEdilizieService;
import it.webred.ct.data.access.basic.concedilizie.dto.ConcessioneDTO;
import it.webred.ct.data.access.basic.cosap.dto.RicercaSoggettoCosapDTO;
import it.webred.ct.data.access.basic.docfa.DocfaService;
import it.webred.ct.data.access.basic.docfa.dto.DatiDocfaDTO;
import it.webred.ct.data.access.basic.docfa.dto.RicercaOggettoDocfaDTO;
import it.webred.ct.data.access.basic.ici.IciService;
import it.webred.ct.data.access.basic.ici.dto.OggettoIciDTO;
import it.webred.ct.data.access.basic.ici.dto.RicercaOggettoIciDTO;
import it.webred.ct.data.access.basic.ici.dto.RicercaSoggettoIciDTO;
import it.webred.ct.data.access.basic.indice.ricerca.IndiceCorrelazioneService;
import it.webred.ct.data.access.basic.indice.ricerca.KeyFabbricatoDTO;
import it.webred.ct.data.access.basic.indice.ricerca.KeyUIDTO;
import it.webred.ct.data.access.basic.indice.ricerca.RicercaIndiceDTO;
import it.webred.ct.data.access.basic.locazioni.dto.RicercaLocazioniDTO;
import it.webred.ct.data.access.basic.pregeo.PregeoService;
import it.webred.ct.data.access.basic.pregeo.dto.RicercaPregeoDTO;
import it.webred.ct.data.access.basic.redditi.RedditiService;
import it.webred.ct.data.access.basic.redditi.dto.KeySoggDTO;
import it.webred.ct.data.access.basic.redditi.dto.RedditiDicDTO;
import it.webred.ct.data.access.basic.tarsu.TarsuService;
import it.webred.ct.data.access.basic.tarsu.dto.RicercaOggettoTarsuDTO;
import it.webred.ct.data.access.basic.tarsu.dto.RicercaSoggettoTarsuDTO;
import it.webred.ct.data.model.anagrafe.SitDCivico;
import it.webred.ct.data.model.anagrafe.SitDPersona;
import it.webred.ct.data.model.catasto.SiticonduzImmAll;
import it.webred.ct.data.model.catasto.Sititrkc;
import it.webred.ct.data.model.catasto.Sitiuiu;
import it.webred.ct.data.model.cnc.flusso750.VAnagrafica;
import it.webred.ct.data.model.compravendite.MuiFabbricatiIdentifica;
import it.webred.ct.data.model.compravendite.MuiNotaTras;
import it.webred.ct.data.model.cosap.SitTCosapContrib;
import it.webred.ct.data.model.cosap.SitTCosapTassa;
import it.webred.ct.data.model.ici.SitTIciOggetto;
import it.webred.ct.data.model.ici.SitTIciSogg;
import it.webred.ct.data.model.ici.SitTIciVia;
import it.webred.ct.data.model.ici.VTIciCiviciAll;
import it.webred.ct.data.model.ici.VTIciSoggAll;
import it.webred.ct.data.model.locazioni.LocazioniA;
import it.webred.ct.data.model.locazioni.LocazioniB;
import it.webred.ct.data.model.pregeo.PregeoInfo;
import it.webred.ct.data.model.redditi.RedDatiAnagrafici;
import it.webred.ct.data.model.redditi.RedDomicilioFiscale;
import it.webred.ct.data.model.redditi.RedRedditiDichiarati;
import it.webred.ct.data.model.tarsu.SitTTarOggetto;
import it.webred.ct.data.model.tarsu.SitTTarSogg;
import it.webred.ct.data.model.tarsu.VTTarCiviciAll;
import it.webred.ct.service.carContrib.data.access.anagrafe.AnagrafeCarContribService;
import it.webred.ct.service.carContrib.data.access.catasto.CatastoCarContribService;
import it.webred.ct.service.carContrib.data.access.cc.CarContribService;
import it.webred.ct.service.carContrib.data.access.cc.IndiceNonAllineatoException;
import it.webred.ct.service.carContrib.data.access.cc.dto.RichiesteDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.SoggettiCarContribDTO;
import it.webred.ct.service.carContrib.data.access.cc.dto.TipDocDTO;
import it.webred.ct.service.carContrib.data.access.cnc.CncCarContribService;
import it.webred.ct.service.carContrib.data.access.cnc.dto.DatiImportiCncDTO;
import it.webred.ct.service.carContrib.data.access.cnc.dto.RicercaCncDTO;
import it.webred.ct.service.carContrib.data.access.common.GeneralService;
import it.webred.ct.service.carContrib.data.access.ici.IciServiceCarContrib;
import it.webred.ct.service.carContrib.data.access.ici.dto.DatiIciDTO;
import it.webred.ct.service.carContrib.data.access.locazioni.LocazioniCarContribService;
import it.webred.ct.service.carContrib.data.access.redditi.RedditiCarContribService;
import it.webred.ct.service.carContrib.data.access.tarsu.TarsuCarContribService;
import it.webred.ct.service.carContrib.data.access.tarsu.dto.DatiTarsuDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.IndiciSoggettoDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.IndirizzoIciTarsuDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.LocazioniDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.ParamAccessoDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.RicercaDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.SitPatrimImmobileDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.SitPatrimTerrenoDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.SoggettoDTO;
import it.webred.ct.service.carContrib.data.access.common.dto.VersamentoDTO;
import it.webred.ct.service.carContrib.data.access.cosap.CosapCarContribService;
import it.webred.ct.service.carContrib.data.model.DecodTipDoc;
import it.webred.ct.service.carContrib.data.model.Richieste;
import it.webred.ct.service.carContrib.data.model.SoggettiCarContrib;
import it.webred.ct.service.carContrib.web.utils.Permessi;
import it.webred.ct.config.parameters.ParameterService;
import it.webred.ct.config.parameters.dto.AmKeyValueDTO;
import it.webred.ct.config.parameters.dto.ParameterSearchCriteria;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet implements Servlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
		esegui(req,resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
		esegui(req,resp);
	}
	private void esegui(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("TestServlet - Esegui. Servizio: "+ req.getParameter("servizio"));
		
		ServiceLocator locator =ServiceLocator.getInstance() ;
		String enteID = req.getParameter("ente");
		String servizio = req.getParameter("servizio");
		String param = req.getParameter("param");
		String section = req.getParameter("section");
		String foglio = req.getParameter("foglio");
		String particella = req.getParameter("particella");
		String str="";
		if (servizio != null ) {
			//verifica abilitazione
			if (servizio.equals("parms"))
				testParam(locator, enteID,section, param);
			//verifica abilitazione
			if (servizio.equals("abil"))
				testVisDatiAbilitazione((CeTUser)(req.getSession().getAttribute("user")));
			//chiamate metodi di test fascicolo fabbricato
			if (servizio.equals("part"))
				testListaParticelle(locator, enteID);
			if (servizio.equals("listInd"))
				testListaIndirizziParticella(locator, enteID);
			if (servizio.equals("listUI"))
				testListaImmobili(locator, enteID);
			if (servizio.equals("listTe"))
				testListaTerreni(locator, enteID);
			if (servizio.equals("mui"))
				testMuiServ(locator, enteID);
			if (servizio.equals("listTe"))
				testListaSoggTerreno(locator, enteID);
			if (servizio.equals("docfa"))
				testListaDocfaUI(locator, enteID);
			if (servizio.equals("conc"))
				testListaConc(locator, enteID) ;
			if (servizio.equals("resid"))
				testListaResid(locator, enteID) ;
			if (servizio.equals("pregeo"))
				testPregeo(locator, enteID, foglio, particella) ;
			//CHIAMATE METODI INDICE
			if (servizio.equals("indCorrT"))
				testaIndCorrTarsu(locator, enteID);
			if (servizio.equals("indCorrI"))
				testaIndCorrIci(locator, enteID);
			if (servizio.equals("indCorrTutte"))
				testaIndCorrTutte(locator, enteID);
			//indice correlazione su redditi
			if (servizio.equals("indCorrR"))
				testaIndCorrRed(locator, enteID);
			//indice correlazione indiretto 
			if (servizio.equals("indCorrInd"))
				testaIndCorrIndiretto(locator, enteID);
			//chiamate metodi di test cartella contribuente
			//FULLPG: TEST COMPLETO PG
			if (servizio.equals("fullPG"))
				testCompletoPG(locator, enteID);
			//1=versamenti per scheda ici
			if (servizio.equals("1"))
				testVersIci(locator, enteID);
			//2=sit patrimon 
			if (servizio.equals("2"))
				test2(locator, enteID);
			//3=LOCAZIONI 
			if (servizio.equals("3"))
				testLocazioni(locator, enteID);
			//FULLIci: DATI DENUNCE U.I. PER SCEHDA ICI
			if (servizio.equals("fullIci"))
				testCompletoIci(locator, enteID);
			
			//cosap: 
			if (servizio.equals("cosap"))
				testCosap(locator, enteID);
			//FULLTarsu: DATI DENUNCE U.I. PER SCEHDA ICI
			if (servizio.equals("fullTarsu"))
				testTarsu(locator, enteID);
			//soggTarsu: DATI SOGGETTO TARSU
			if (servizio.equals("soggTarsu"))
				testTarsuSogg(locator, enteID);
			//soggTarsu: DATI SOGGETTO TARSU
			if (servizio.equals("immPossTarsu"))
				immobiliPossTarsu(locator, enteID);
			//cnc: testCnc
			if (servizio.equals("cnc"))
				testCnc(locator, enteID);
			//cnc: test versamenti ICI + cnc con i 2 metodi 
			if (servizio.equals("ici-cnc"))
				testIciCnc(locator, enteID);
			//redditi
			if (servizio.equals("redd"))
				testRedditi(locator, enteID);
			//redditi
			if (servizio.equals("compFam"))
				testCompFamiglia(locator, enteID);
			//CarContrib DataModel
			if (servizio.equals("cc-dm"))
				testCCDataModel(locator, enteID);
			//CarContrib DataModel: inserimento Richiesta
			if (servizio.equals("insR"))
				testInsertRichiesta(locator, enteID);
			//test dei metodi del ct-service
			if (servizio.equals("ct"))
				testaCtService(locator, enteID);
			if (servizio.equals("catTit"))
				testaCtServiceTitolari(locator, enteID, req.getParameter("idUiu"));
			if (servizio.equals("ct-ici"))
				testaCtServiceICI(locator, enteID);
			if (servizio.equals("ct-anag"))
				testaCtServiceAnag(locator, enteID);
			//DATIaNAG COMPLETI
			if (servizio.equals("anaComp"))
				testaDatiAnag(locator, enteID);
			
			
		}	
		
	}
	private void testParam(ServiceLocator locator,String enteID, String section, String param) {
		ParameterService parmSer = (ParameterService)locator.getReference("CT_Service/ParameterBaseService/remote");
		ParameterSearchCriteria cri = new ParameterSearchCriteria();
		cri.setComune(enteID);
		cri.setSection(section);
		cri.setKey(param);
		if (section.equals("param.globali")){
			cri.setType("0");
			cri.setComune(null);
		}
			
		List<AmKeyValueDTO> lista = parmSer.getListaKeyValue(cri);
		if (lista !=null) {
			stampa(lista);
		}
	}
	/* METODI PER TEST S13-FASCICOLO FABBRICATO
	 * 
	 */
	private void testVisDatiAbilitazione(CeTUser user) {
		
		System.out.println("USERNAME: " +user.getUsername() );
		List<String> listaEnti = user.getEnteList();
		System.out.println("-- ENTI --");
		for (String ente: listaEnti ) {
			System.out.println("Enti: " + ente);
		}
		HashMap<String,String> listaPermessi = user.getPermList();
		Collection<String> listaPermessiC = listaPermessi.values();
		Iterator it =listaPermessiC.iterator();
		System.out.println("-- PERMESSI --");
		while(it.hasNext() )		{
			System.out.println("Permesso: " + it.next().toString());
		}
		System.out.println("PERMESSO_SUPERVISIONE_RICHIESTE_CARTELLA: AUTORIZZATO ?: " + Permessi.controlla(user, Permessi.PERMESSO_SUPERVISIONE_RICHIESTE_CARTELLA));	
		System.out.println("PERMESSO_GESTIONE_RICHIESTE_CARTELLA ?: " + Permessi.controlla(user, Permessi.PERMESSO_GESTIONE_RICHIESTE_CARTELLA));
	}
	
	/* METODI PER TEST S13-FASCICOLO FABBRICATO
    */
	private void testListaParticelle(ServiceLocator locator, String enteID) {
		System.out.println("Inizio metodo di ricerca PARTICELLE PER INDIRIZZO" ) ;
		CatastoService catSer = (CatastoService)locator.getReference("CT_Service/CatastoServiceBean/remote");
		String idCivico= "37978";
		CatastoSearchCriteria sc=new CatastoSearchCriteria(); 
		sc.setCodNazionale("F704");
		sc.setIdCivico(idCivico);
		RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO(sc);
		ro.setEnteId(enteID);
		System.out.println("PARTICELLE PER INDIRIZZO PKID_CIVICO " + idCivico) ;
		List<ParticellaKeyDTO> listaPart = catSer.getListaParticelle(ro);
		stampa(listaPart);
	}
	private void testListaImmobili(ServiceLocator locator, String enteID) {
		System.out.println("Inizio metodo di ricerca IMMOBILI PER FOGLIO-PARTICELLA" ) ;
		CatastoService catSer = (CatastoService)locator.getReference("CT_Service/CatastoServiceBean/remote");
		RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
		String sezione= null;
		String foglio= "88";
		String particella= "00266";
		ro.setCodEnte("F704");
		ro.setSezione(sezione);
		ro.setFoglio(foglio);
		ro.setParticella(particella);
		ro.setEnteId(enteID);
		System.out.println("U.I. PER FOGLIO-PARTICELLA" ) ;
		List<Sitiuiu>  listaUI = catSer.getListaUiAllaData(ro);
		stampa(listaUI);
	}
	private void testListaTerreni(ServiceLocator locator, String enteID) {
		System.out.println("Inizio metodo di ricerca TERRENI PER FOGLIO-PARTICELLA" ) ;
		CatastoService catSer = (CatastoService)locator.getReference("CT_Service/CatastoServiceBean/remote");
		RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
		String sezione= null;
		String foglio= "88";
		String particella= "00266";
		ro.setCodEnte("F704");
		ro.setSezione(sezione);
		ro.setFoglio(foglio);
		ro.setParticella(particella);
		ro.setEnteId(enteID);
		System.out.println("TERRENI PER FOGLIO-PARTICELLA" ) ;
		List<Sititrkc>  listaUI = catSer.getListaTerreniByFP(ro);
		stampa(listaUI);
	}
	private void testListaSoggTerreno(ServiceLocator locator, String enteID) {
		CatastoService catSer = (CatastoService)locator.getReference("CT_Service/CatastoServiceBean/remote");
		RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
		String sezione= null;
		String foglio= "88";
		String particella= "00004";
		ro.setCodEnte("F704");
		ro.setSezione(sezione);
		ro.setFoglio(foglio);
		ro.setParticella(particella);
		ro.setEnteId(enteID);
		System.out.println("Inizio metodo di ricerca soggetti terreno PER FOGLIO-PARTICELLA. FOGLIO/PART: " + foglio +"/" + particella) ;
		List<it.webred.ct.data.access.basic.catasto.dto.SoggettoDTO>  listaSoggAtt= catSer.getListaSoggettiAttualiTerreno(ro);
		System.out.println("TITOLARI ATTUALI TERRENO " ) ;
		stampa(listaSoggAtt);
		List<it.webred.ct.data.access.basic.catasto.dto.SoggettoDTO>  listaSoggSto= catSer.getListaSoggettiStoriciTerreno(ro);
		System.out.println("TITOLARI STORICI TERRENO " ) ;
		stampa(listaSoggSto);
	}
	
	private void testListaIndirizziParticella(ServiceLocator locator, String enteID) {
		System.out.println("Inizio metodo di ricerca INDIRIZZI PER FOGLIO-PARTICELLA" ) ;
		CatastoService catSer = (CatastoService)locator.getReference("CT_Service/CatastoServiceBean/remote");
		RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
		String sezione= null;
		String foglio= "88";
		String particella= "00266";
		ro.setCodEnte("F704");
		ro.setSezione(sezione);
		ro.setFoglio(foglio);
		ro.setParticella(particella);
		ro.setEnteId(enteID);
		System.out.println("INDIRIZZI PER FOGLIO-PARTICELLA" ) ;
		List<IndirizzoDTO>  lista = catSer.getListaIndirizziPartAllaData(ro);
		stampa(lista);
	}
	private void testMuiServ(ServiceLocator locator, String enteID) {
		CompravenditeMUIService ser = (CompravenditeMUIService)locator.getReference("CT_Service/CompravenditeMUIServiceBean/remote");
		RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
		String sezione= null;
		String foglio= "115";
		String particella= "109";
		String sub= "734";
		//ro.setCodEnte("F704");
		ro.setSezione(sezione);
		ro.setFoglio(foglio);
		ro.setParticella(particella);
		ro.setUnimm(sub);
		ro.setEnteId(enteID);
		System.out.println("ACQUISIZIOEN NOTE COMPRAVENDITA PER U.I.: " + foglio + "/"  + particella+ "/" +sub)  ;
		List<MuiNotaTras>  lista = ser.getListaNoteByFPS(ro); 
		RicercaCompravenditeDTO rc = null;
		if (lista!=null) {
			for(MuiNotaTras nota:lista) {
				 rc = new RicercaCompravenditeDTO();
				 rc.setEnteId(enteID);
				 rc.setIdNota(nota.getIdNota());
				List<SoggettoCompravenditeDTO> listaSoggNota =ser.getListaSoggettiNota(rc);
				stampaOgg(nota);
				stampa(listaSoggNota);
			}
		}
		ro = new RicercaOggettoCatDTO();
		foglio= "34";
		particella= "185";
		ro.setFoglio(foglio);
		ro.setParticella(particella);
		ro.setEnteId(enteID);
		System.out.println("ACQUISIZIONE NOTE COMPRAVENDITA PER TERRENO: " + foglio + "/"  + particella)  ;
		lista = ser.getListaNoteTerrenoByFP(ro);
		if (lista!=null) {
			for(MuiNotaTras nota:lista) {
				 rc = new RicercaCompravenditeDTO();
				 rc.setEnteId(enteID);
				 rc.setIdNota(nota.getIdNota());
				List<SoggettoCompravenditeDTO> listaSoggNota =ser.getListaSoggettiNota(rc);
				stampaOgg(nota);
				stampa(listaSoggNota);
			}
		}
	}
	private void testListaDocfaUI(ServiceLocator locator, String enteID) {
		
		/*
		DocfaService ser = (DocfaService)locator.getReference("CT_Service/DocfaServiceBean/remote");
		RicercaOggettoDocfaDTO ro = new RicercaOggettoDocfaDTO();
		String sezione= null;
		String foglio= "85";
		String particella= "339";
		String sub= "46";
		//ro.setCodEnte("F704");
		ro.setSezione(sezione);
		ro.setFoglio(foglio);
		ro.setParticella(particella);
		ro.setUnimm(sub);
		ro.setEnteId(enteID);
		System.out.println("ACQUISIZIONE DOCFA PER U.I.: " + foglio + "/"  + particella+ "/" +sub)  ;
		List<DatiDocfaDTO>  lista = ser.getListaDatiDocfaFabbricato(ro);
		if (lista!=null) {
			stampa(lista);
			
		}
		System.out.println("ACQUISIZIONE DOCFA PER FABBR CON SUB NON VALORIZZATO: " + foglio + "/"  + particella+ "/" +sub)  ;
		ro.setUnimm(null);
		lista = ser.getListaDatiDocfaFabbricato(ro);
		if (lista!=null) {
			stampa(lista);
			
		}
		*/
	}
	private void testListaConc(ServiceLocator locator, String enteID) {
		ConcessioniEdilizieService ser = (ConcessioniEdilizieService)locator.getReference("CT_Service/ConcessioniEdilizieServiceBean/remote");
		RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
		String sezione= null;
		String foglio= "8";
		String particella= "25";
		String sub= null;
		//ro.setCodEnte("F704");
		ro.setSezione(sezione);
		ro.setFoglio(foglio);
		ro.setParticella(particella);
		ro.setUnimm(sub);
		ro.setEnteId(enteID);
		System.out.println("ACQUISIZIONE CONCESSIONI EDILIZIE PER FABBRICATO: " + foglio + "/"  + particella+ "/" +sub)  ;
		List<ConcessioneDTO>  lista = ser.getDatiConcessioniByFabbricato(ro);
		if (lista!=null) {
			stampa(lista);
			
		}
			
	}
	
	
	/* FINE METODI PER TEST S13-FASCICOLO FABBRICATO
	 */
	//METODO PRINCIPALE PER IL TEST--- SIMULA LA CREAZIONE DELLA LISTA DA PAR DI FILTRO-->SEL. ELEMENTO--> VAL. DATI SCHEDA ICI 
	//INPUT DATI TEST PER enteID='F205'; F704
	private String testCompletoIci(ServiceLocator locator, String enteID) {
		String codEnte ="F704";//"F205";
		GeneralService serv = (GeneralService)locator.getReference("Servizio_CarContrib/GeneralServiceBean/remote");
		String cognome = "AMBROGI";
		ParamAccessoDTO par = new ParamAccessoDTO();
		par.setProvenienzaDatiIci("T");
		par.setProvenienzaDatiTarsu("T");
		par.setCognome(cognome);
		par.setEnteId(enteID);
		par.setTipoSogg("F");
		System.out.println("RICERCA PERSONE PER COGNOME: " + cognome ) ;
		Set<SoggettoDTO> listaPersone=serv.searchSoggetto(par);
		stampa(listaPersone);
		//dalla lista simulo la selzione del primo elemento e cerco i dati 
		
		//SoggettoDTO sogg = listaPersone.iterator().next();
		SoggettoDTO sogg= new SoggettoDTO();
		int ind =0;
		//IND=4 alfredo: non aggancia i dati ici
		Iterator iterator =  listaPersone.iterator();
		while (ind <14) {
			sogg=(SoggettoDTO)iterator.next();
			ind++;
		}
		System.out.println("SIMULO SELEZIONE ELEMENTO N.  " + ind + ": "+ sogg.getCognome() + " " + sogg.getNome() + " " + sogg.getDtNas().toString() + " - "  + sogg.getDesComNas() ) ;
		RicercaSoggettoIciDTO rsi = new RicercaSoggettoIciDTO();
		rsi.setEnteId(enteID);
		rsi.setProvenienza("T");
		rsi.setTipoSogg("F");
		rsi.setNome(sogg.getNome());
		rsi.setCognome(sogg.getCognome());
		rsi.setDtNas(sogg.getDtNas());
		rsi.setCodFis(sogg.getCodFis());
		rsi.setDesComNas(sogg.getDesComNas());
		String codComNas="";
		if (sogg.getDesTblProv().equals("SIT_D_PERSONA")) {
			if (sogg.getCodComNas()!=null)
				codComNas = sogg.getCodComNas().substring(0,5);
		}
		else
			codComNas = sogg.getCodComNas();
		rsi.setCodComNas(codComNas);
		rsi.setUpperCase();
		IciServiceCarContrib servIci = (IciServiceCarContrib)locator.getReference("Servizio_CarContrib/IciServiceCarContribBean/remote");
		RicercaDTO ric = new RicercaDTO();
		ric.setEnteId(enteID);
		ric.setUserId("user");
		ric.setObjEntity(null);
		ric.setObjFiltro(rsi);
		try{
			List<SitTIciSogg> listaSoggIci = servIci.searchSoggettiCorrelatiIci(ric);
			//scheda ICI del primo della lista
			if (listaSoggIci != null && listaSoggIci.size() >0 ) {
				SitTIciSogg soggIci = listaSoggIci.get(0);
				System.out.println("PREPARO SCHEDA ICI DEL SOGGETTO: " + soggIci.getCogDenom() + " " + soggIci.getNome() + " " + sogg.getDtNas().toString() + " - "  + sogg.getDesComNas() ) ;		
				System.out.println("-->ID : " + soggIci.getId());
				rsi=new RicercaSoggettoIciDTO();
				rsi.setEnteId(enteID);
				rsi.setIdSoggIci(soggIci.getId());
				rsi.setCodFis(sogg.getCodFis());
				rsi.setCognome(sogg.getCognome() );
				rsi.setNome(sogg.getNome());
				rsi.setDtNas(sogg.getDtNas());
				rsi.setCodComNas(sogg.getCodComNas());
				rsi.setDesComNas(sogg.getDesComNas());
				//
				sogg.setCodEnte(codEnte);
				SitTIciSogg datiSoggetto = servIci.getDatiIciSoggetto(rsi);
				System.out.println("DATI SOGGETTO ICI");
				stampaOgg(datiSoggetto);
				ric = new RicercaDTO();
				ric.setEnteId(enteID);
				ric.setUserId("user");
				ric.setObjEntity(null);
				ric.setObjFiltro(sogg);
				IndiciSoggettoDTO indSogg = serv.getIndiciSoggetto(ric, null);
				List<DatiIciDTO> listaIci = servIci.getDatiIci(rsi, indSogg);
				System.out.println("DATI OGGETTI ICI");
				stampa(listaIci);
			}
		}catch(IndiceNonAllineatoException ie){
			System.out.println( ie.getMessage());
		}
		
		return null;
		
	}
	private void testCompletoPG(ServiceLocator locator, String enteID) {
		GeneralService serv = (GeneralService)locator.getReference("Servizio_CarContrib/GeneralServiceBean/remote");
		//---- PERSONE GIURIDICHE
		String denom ="CONSULT";
		ParamAccessoDTO par = new ParamAccessoDTO();
		par.setProvenienzaDatiIci("T");
		par.setProvenienzaDatiTarsu("T");
		par.setEnteId(enteID);
		par.setTipoSogg("G");
		par.setDenom(denom);
		System.out.println("RICERCA P.GIURIDICHE PER DENOMINAZ.: " + denom) ;
		Set<SoggettoDTO> listaPersone=serv.searchSoggetto(par);
		stampa(listaPersone);
		//dalla lista simulo la selezione  e cerco i dati 
		SoggettoDTO sogg= new SoggettoDTO();
		int ind =0;
		Iterator iterator =  listaPersone.iterator();
		while (ind <5) {
			sogg=(SoggettoDTO)iterator.next();
			ind++;
		}
		//recupeo indici (anagrafe-catasto) corrispondenti all'elemento selezionato
		RicercaDTO ricObjInd= new RicercaDTO();
		ricObjInd.setEnteId(enteID);
		ricObjInd.setUserId("user");
		ricObjInd.setObjEntity(null);
		ricObjInd.setObjFiltro(sogg);
		IndiciSoggettoDTO indSogg = serv.getIndiciSoggetto(ricObjInd, null);
		//DATI ICI
		RicercaSoggettoIciDTO rsi = new RicercaSoggettoIciDTO();
		rsi.setEnteId(enteID);
		rsi.setProvenienza("T");
		rsi.setTipoSogg("G");
		rsi.setDenom(sogg.getDenom());
		rsi.setParIva(sogg.getParIva());
		rsi.setUpperCase();
		IciServiceCarContrib servIci = (IciServiceCarContrib)locator.getReference("Servizio_CarContrib/IciServiceCarContribBean/remote");
		RicercaDTO ricObj= new RicercaDTO();
		ricObj.setEnteId(enteID);
		ricObj.setUserId("user");
		ricObj.setObjEntity(null);
		ricObj.setObjFiltro(rsi);
		try{
			List<SitTIciSogg> listaSoggIci = servIci.searchSoggettiCorrelatiIci(ricObj);
			stampa(listaSoggIci);
			//scheda ICI del primo della lista
			if (listaSoggIci != null && listaSoggIci.size() >0 ) {
				SitTIciSogg soggIci = listaSoggIci.get(0);
				System.out.println("PREPARO SCHEDA ICI DEL SOGGETTO: " + soggIci.getCogDenom() + " " + soggIci.getPartIva() ) ;		
				System.out.println("-->ID : " + soggIci.getId());
				rsi=new RicercaSoggettoIciDTO();
				rsi.setEnteId(enteID);
				rsi.setIdSoggIci(soggIci.getId());
				SitTIciSogg datiSoggetto = servIci.getDatiIciSoggetto(rsi);
				System.out.println("DATI SOGGETTO ICI");
				stampaOgg(datiSoggetto);
				RicercaDTO ric = new RicercaDTO();
				ric.setEnteId(enteID);
				ric.setUserId("user");
				ric.setObjEntity(null);
				ric.setObjFiltro(sogg);
				List<DatiIciDTO> listaIci = servIci.getDatiIci(rsi, indSogg);
				System.out.println("DATI OGGETTI ICI");
				stampa(listaIci);
				//VERSAMENTI
				System.out.println("DATI VERSAMENTI");
				List<DatiImportiCncDTO> listaVer = servIci.getVersamenti(rsi, 2011) ;
				stampa(listaVer);
				String dtRifStr="02/02/2011";
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				Date dtRif=new Date();
				try {dtRif= df.parse(dtRifStr);}catch(java.text.ParseException pe){}
				//SITUAZIONE PATRIMONIALE
				CatastoCarContribService servCatCC = (CatastoCarContribService)locator.getReference("Servizio_CarContrib/CatastoCarContribServiceBean/remote");
				if (indSogg != null) {
					List<BigDecimal> indiciAnagCat = indSogg.getListaIdSoggAnagCat();
					if (indiciAnagCat != null && indiciAnagCat.size() == 1 ) {
						BigDecimal idSoggCat = indiciAnagCat.get(0);
						System.out.println("recuperato idSogg Cat: " + idSoggCat);
						RicercaSoggettoCatDTO rs = new RicercaSoggettoCatDTO();
						rs.setEnteId(enteID);
						rs.setCodEnte("F704");
						rs.setIdSogg(idSoggCat);
						rs.setDtVal(null);
						List<SitPatrimImmobileDTO> listaImm = servCatCC.getImmobiliPosseduti(rs, "ICI");
						if (listaImm != null){
							System.out.println("STAMPA IMMOBILI POSSEDUTI AL: " + dtRif.toString());
							stampa(listaImm);
						}
						
						String dtRifAStr="31/12/2010";
						String dtRifDaStr="01/01/2010";
						Date dtRifA=new Date();
						Date dtRifDa=new Date();
						try {
							dtRifA= df.parse(dtRifAStr);
							dtRifDa= df.parse(dtRifDaStr);
						}catch(java.text.ParseException pe){}
						rs.setDtRifDa(dtRifDa);
						rs.setDtRifA(dtRifA);
						rs.setDtVal(null);
						List<SitPatrimImmobileDTO> listaImmCeduti= servCatCC.getImmobiliCeduti(rs);
						if (listaImmCeduti != null){
							System.out.println("STAMPA IMMOBILI CEDUTI DAL " + dtRifDa.toString() + " AL: " + dtRifA.toString());
							stampa(listaImmCeduti);
						}
						System.out.println("TERRENI PER SOGGETTO " );
						ric = new RicercaDTO();
						ric.setEnteId(enteID);
						ric.setUserId("user");
						ric.setObjEntity(null);
						ric.setObjFiltro(sogg);
						System.out.println("recuperato idSogg Cat: " + idSoggCat);
	    				rs = new RicercaSoggettoCatDTO();
	    				rs.setEnteId(enteID);
	    				rs.setCodEnte("F704");
	    				rs.setIdSogg(idSoggCat);
	    				rs.setDtVal(dtRif);
	    				//metto try perché le tabelle non esistono
	    				try {
	    					List<SitPatrimTerrenoDTO>  listaTerr = servCatCC.getTerreniPosseduti(rs);
	    					if (listaTerr != null){
	        					System.out.println("STAMPA TERRENI POSSEDUTI AL: " + dtRif.toString());
	        					stampa(listaTerr);
	        				}
	        				
	    				}catch(Exception e) {
	    					System.out.println("ECCEZIONE IN ACQUISIZIONE TERRENI");
	    					e.printStackTrace();
	    				}
	    				
	    				dtRifAStr="31/12/2010";
	    				dtRifDaStr="01/01/2001";
	    				dtRifA=new Date();
	    				dtRifDa=new Date();
	    				try {
	    					dtRifA= df.parse(dtRifAStr);
	    					dtRifDa= df.parse(dtRifDaStr);
	    				}catch(java.text.ParseException pe){}
	    				rs.setDtRifDa(dtRifDa);
	    				rs.setDtRifA(dtRifA);
	    				rs.setDtVal(null);
	    				//metto try perché le tabelle non esistono
	    				try {
		    				List<SitPatrimTerrenoDTO> listaTerrCeduti= servCatCC.getTerreniCeduti(rs);
		    				if (listaTerrCeduti != null){
		    					System.out.println("STAMPA TERRENI CEDUTI DAL " + dtRifDa.toString() + " AL: " + dtRifA.toString());
		    					stampa(listaTerrCeduti);
		    				}
	    				}	catch(Exception e) {
	    					System.out.println("ECCEZIONE IN ACQUISIZIONE TERRENI");
	    					e.printStackTrace();
	    				}
					}
				}//FINE SITUAZIONE PATRIMONIALE
			}//FINE ICI
			
		}catch(IndiceNonAllineatoException ie){
			System.out.println( ie.getMessage());
		}
		
		// DATI TARSU
		System.out.println("<------RICERCA DEI DATI TARSU------>");
		TarsuCarContribService servTar = (TarsuCarContribService)locator.getReference("Servizio_CarContrib/TarsuCarContribServiceBean/remote");
		RicercaSoggettoTarsuDTO rst = new RicercaSoggettoTarsuDTO();
		rst.setEnteId(enteID);
		rst.setProvenienza("T");
		rst.setTipoSogg("G");
		rst.setDenom(sogg.getDenom());
		rst.setParIva(sogg.getParIva());
		rst.setUpperCase();
		ricObj= new RicercaDTO();
		ricObj.setEnteId(enteID);
		ricObj.setUserId("user");
		ricObj.setObjEntity(null);
		ricObj.setObjFiltro(rst);
		
		try{
			List<SitTTarSogg> listaSoggTar = servTar.searchSoggettiCorrelatiTarsu(ricObj );
			stampa(listaSoggTar);
			//scheda TARSU del primo della lista
			if (listaSoggTar != null && listaSoggTar.size() >0 ) {
				SitTTarSogg soggTar = (SitTTarSogg)listaSoggTar.get(0);
				String idSoggTarsu = soggTar.getId();
				System.out.println("PREPARO SCHEDA TARSU DEL SOGGETTO: " + soggTar.getCogDenom() + " " + soggTar.getPartIva()) ;		
				System.out.println("-->ID : " + idSoggTarsu);
				rst=new RicercaSoggettoTarsuDTO();
				rst.setEnteId(enteID);
				rst.setIdSoggTarsu(idSoggTarsu);
				SitTTarSogg datiSoggettoT = servTar.getDatiTarsuSoggetto(rst);
				System.out.println("DATI ANAGRAFICI SOGGETTO TARSU");
				stampaOgg(datiSoggettoT);
				List<DatiTarsuDTO> listaTarsu = servTar.getDatiTarsu(rst, indSogg);
				System.out.println("DATI OGGETTI TARSU");
				stampa(listaTarsu);
				//VERSAMENTI
				System.out.println("DATI VERSAMENTI TARSU");
				 String[] codiciEntrata =  {"116","117", "127","113"};
				List<DatiImportiCncDTO> listaVerT = servTar.getVersamenti(rst, 2011, codiciEntrata);
				stampa(listaVerT);
			}
		}catch(IndiceNonAllineatoException ie){
			System.out.println( ie.getMessage());
		}
		
	}
	//inpu dati test per enteID=roma
	private void testTarsu(ServiceLocator locator, String enteID) {
		String codEnte="F704";
		RicercaSoggettoTarsuDTO rst = new RicercaSoggettoTarsuDTO(); 
		rst.setEnteId(enteID);
		rst.setUserId("user");
		rst.setProvenienza("T");
		rst.setTipoSogg("F");
		String cf="";
		// politi giuliano inizio
		/*
		cf="PLTVTR44E21G965U";  
		rst.setNome("GIULIANO");
		rst.setCognome("POLITI");
		//INDIRIZZO PERSONA PER DATA
		String dtNasStr="10/07/1964";
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date dtNas=new Date();
		try {dtNas= df.parse(dtNasStr);}catch(java.text.ParseException pe){}
		rst.setDtNas(dtNas);
		rst.setCodFis(cf);
		rst.setDesComNas("MILANO");
		String codComNas="";
		codComNas = "015146";
		*/
		//politi giuliano fine
		//politi vittorio inizio
		cf="PLTVTR44E21G965U";
		rst.setNome("VITTORIO");
		rst.setCognome("POLITI");
		//INDIRIZZO PERSONA PER DATA
		String dtNasStr="21/05/1944";
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date dtNas=new Date();
		try {dtNas= df.parse(dtNasStr);}catch(java.text.ParseException pe){}
		rst.setDtNas(dtNas);
		rst.setCodFis(cf);
		rst.setDesComNas("POZZUOLO MARTESANA");
		String codComNas="";
		codComNas = "015178"; //??? DA QUI....
		//politi vittorio fine
		rst.setCodComNas(codComNas);
		rst.setUpperCase();
		//INDICI DEL SOGGETTO
		GeneralService serv = (GeneralService)locator.getReference("Servizio_CarContrib/GeneralServiceBean/remote");
		SoggettoDTO sogg = new SoggettoDTO();
		sogg.setEnteId(enteID);
		sogg.setUserId("user");
		sogg.setCodEnte(codEnte);
		sogg.setCognome(rst.getCognome());
		sogg.setNome(rst.getNome());
		sogg.setDtNas(rst.getDtNas());
		sogg.setCodComNas(rst.getCodComNas());
		sogg.setTipoSogg("F");
		RicercaDTO ric = new RicercaDTO();
		ric.setEnteId(enteID);
		ric.setUserId("user");
		ric.setObjEntity(null);
		ric.setObjFiltro(sogg);
		IndiciSoggettoDTO indSogg = serv.getIndiciSoggetto(ric, null);
		//TARSU
		TarsuCarContribService servTar = (TarsuCarContribService)locator.getReference("Servizio_CarContrib/TarsuCarContribServiceBean/remote");
		ric = new RicercaDTO();
		ric.setEnteId(enteID);
		ric.setUserId("user");
		ric.setObjEntity(null);
		ric.setObjFiltro(rst);
		try{
		List<SitTTarSogg> listaSoggTar = servTar.searchSoggettiCorrelatiTarsu(ric);
			if (listaSoggTar != null && listaSoggTar.size()> 0) {
				SitTTarSogg soggTar = listaSoggTar.get(0);
				System.out.println("PREPARO SCHEDA TARSU DEL SOGGETTO: " + soggTar.getCogDenom() + " " + soggTar.getNome() + " " + soggTar.getDtNsc().toString() + " - "  +soggTar.getDesComNsc()  ) ;		
				System.out.println("-->ID : " + soggTar.getId());
				rst.setEnteId(enteID);
				rst.setUserId("user");
				rst.setIdSoggTarsu( soggTar.getId());
				rst.setCodFis(soggTar.getCodFisc());
				rst.setCognome(soggTar.getCogDenom() );
				rst.setNome(soggTar.getNome());
				rst.setDtNas(soggTar.getDtNsc());
				rst.setCodComNas(soggTar.getCodCmnNsc());
				rst.setDesComNas(soggTar.getDesComNsc());
				System.out.print("-----DATI TARSU PER SOGGETTO----");
				SitTTarSogg datiTarSoggetto = servTar.getDatiTarsuSoggetto(rst);
				stampaOgg(datiTarSoggetto);
				List<DatiTarsuDTO> listaTarsu = servTar.getDatiTarsu(rst, indSogg);
				System.out.print("-----LISTA OGGETTI TARSU PER SOGGETTO----");
				stampa(listaTarsu);
			}
		}catch(IndiceNonAllineatoException ie){
			System.out.println(ie.getMessage());
		}
		
		//SITUAZIONE PATRIMONIALE
		String dtRifAStr="31/12/2010";
		String dtRifDaStr="01/01/2003"; //per il test va bene ma in realtà l'anno sarebbe 2010-4=2006. Ma non ci sono dati dopo il 2006, quinddi non vedrei nulla 
		df = new SimpleDateFormat("dd/MM/yyyy");
		Date dtRifA=new Date();
		Date dtRifDa=new Date();
		try {
			dtRifA= df.parse(dtRifAStr);
			dtRifDa= df.parse(dtRifDaStr);
		}catch(java.text.ParseException pe){}
		System.out.print("-----SITUAZIONE PATRIMONIALE DAL " + dtRifDaStr + " AL " + dtRifAStr +"----");
		RicercaSoggettoCatDTO rsCat = new RicercaSoggettoCatDTO();
		rsCat.setEnteId(enteID) ;
		rsCat.setUserId("user");
		CatastoCarContribService servCatCC = (CatastoCarContribService)locator.getReference("Servizio_CarContrib/CatastoCarContribServiceBean/remote");
		if (indSogg != null) {
			List<BigDecimal> indiciAnagCat = indSogg.getListaIdSoggAnagCat();
			if (indiciAnagCat != null && indiciAnagCat.size() == 1 ) {
				BigDecimal idSoggCat = indiciAnagCat.get(0);
				System.out.println("recuperato idSogg Cat: " + idSoggCat);
				RicercaSoggettoCatDTO rs = new RicercaSoggettoCatDTO();
				rs.setEnteId(enteID);
				rs.setCodEnte(codEnte);
				rs.setIdSogg(idSoggCat);
				rs.setDtRifDa(dtRifDa);
				rs.setDtRifA(dtRifA);
				List<SitPatrimImmobileDTO> listaImm = servCatCC.getImmobiliPosseduti(rs, "TARSU");
				if (listaImm != null){
					System.out.println("IMMOBILI TROVATI");
					stampa(listaImm);
				}
			
			}	
		}
		System.out.print("---- LOCAZIONI DAL " + dtRifDaStr + "----");
		
		RicercaLocazioniDTO rl = new RicercaLocazioniDTO();
		rl.setEnteId(enteID);
		rl.setUserId("user");
		cf="BBISLV57R64B354Y";
		rl.setCodFis(cf);
		ric = new RicercaDTO();
		ric.setEnteId(enteID);
		ric.setUserId("user");
		ric.setObjFiltro(rl);
		ric.setObjEntity(null);
		//rl.setDtRif(dtRifDa);//N.B. NON RICERCARE PER DATA perché NON SONO SEMPRE VAORIZZATE. ALL'OCCORRENZA SCORRERE LE RIGHE E SCARTARE.... 
		LocazioniCarContribService servLoc = (LocazioniCarContribService)locator.getReference("Servizio_CarContrib/LocazioniCarContribServiceBean/remote");
		List<LocazioniDTO> listaLocazioni = servLoc.searchLocazioniBySogg(ric);
	   if (listaLocazioni!=null) {
		   System.out.println("--- LOCAZIONI ---" );
		   stampa(listaLocazioni);
	   }
	   //VERSAMENTI TARSU(DA CNC)
	   rst=new RicercaSoggettoTarsuDTO();
	   rst.setEnteId(enteID);
	   rst.setUserId("user");
	   rst.setCodFis("BCCNNA24L53E079U");
	   String[] codiciEntrata =  {"116","117", "127","113"};
	   //List<VersamentoDTO> listaVer = servTar.getVersamentiOLD(rst, 2005,codiciEntrata) ;
	   //System.out.print("---- VERSAMENTI TARSU (DA CNC) ----");
	   //if (listaVer!=null) {
	   //   stampa(listaVer);
	   //}
	   List<DatiImportiCncDTO> listaVer1 = servTar.getVersamenti(rst, 2005, codiciEntrata);
	   System.out.print("---- VERSAMENTI TARSU  -NEW-   (DA CNC) ----");
	   if (listaVer1!=null) {
		   stampa(listaVer1);
	   }
	}
	//inpu dati test per enteID=roma(dati di Monza)
	private void testTarsuSogg(ServiceLocator locator, String enteID) {
		String id="20100921 000000                                       T@000886682    2";
		RicercaSoggettoTarsuDTO rst = new RicercaSoggettoTarsuDTO(); 
		rst.setEnteId(enteID);
		rst.setUserId("user");
		rst.setIdSoggTarsu( id);
		System.out.print("-----DATI TARSU PER SOGGETTO----");
		TarsuCarContribService servTar = (TarsuCarContribService)locator.getReference("Servizio_CarContrib/TarsuCarContribServiceBean/remote");
		SitTTarSogg datiTarSoggetto = servTar.getDatiTarsuSoggetto(rst);
		stampaOgg(datiTarSoggetto);
		//dati
		//INDICI DEL SOGGETTO
		GeneralService serv = (GeneralService)locator.getReference("Servizio_CarContrib/GeneralServiceBean/remote");
		SoggettoDTO sogg = new SoggettoDTO();
		sogg.setEnteId(enteID);
		sogg.setUserId("user");
		sogg.setCodEnte(enteID);
		sogg.setCognome("BOUFTILA");
		sogg.setNome("BADR");
		String dtNasStr="06/09/1975";
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date dtNas=new Date();
		try {
			dtNas= df.parse(dtNasStr);
		}catch(java.text.ParseException pe){}
		sogg.setDtNas(dtNas);
		sogg.setCodComNas("360108");
		sogg.setTipoSogg("F");
		sogg.setDesTblProv("SIT_T_TAR_SOGG");
		RicercaDTO ric = new RicercaDTO();
		ric.setEnteId(enteID);
		ric.setUserId("user");
		ric.setObjEntity(null);
		ric.setObjFiltro(sogg);
		IndiciSoggettoDTO indSogg = serv.getIndiciSoggetto(ric, null);
		List<DatiTarsuDTO> datiT = servTar.getDatiTarsu(rst, indSogg);
		stampa(datiT);
		
	}
	//
	private void testCosap(ServiceLocator locator, String enteID) {
		String cf="BAIPTR22T11L157Q";String cognome="";	String nome=""; Date dtNas =null; String codComNas=""; String desComNas=""; 
		//RicercaSoggettoDTO rs = new RicercaSoggettoDTO();
		RicercaSoggettoCosapDTO rs = new RicercaSoggettoCosapDTO();
		rs.setEnteId(enteID);
		rs.setProvenienza("COSAP");
		rs.setTipoSogg("F");
		rs.setCodFis(cf);
		/*
		rs.setNome(nome);
		rs.setCognome(cognome);
		rs.setDtNas(dtNas);
		rs.setDesComNas(desComNas);
		rs.setCodComNas(codComNas);
		*/
		rs.setUpperCase();
		CosapCarContribService  serv = (CosapCarContribService)locator.getReference("Servizio_CarContrib/CosapCarContribServiceBean/remote");
		List<SitTCosapContrib> listaSogg = serv.searchSoggettoCosap(rs);
		System.out.print("----- SOGGETTI COSAP CON CF: " + cf + "----");
		stampa(listaSogg);
		//PER COGNOME.NOME-DATA/LUOGO NASCITA
		cognome="IBBA";	 nome="RAFFAELE"; 
		String  dtNasStr ="17/04/1971"; 
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			dtNas= df.parse(dtNasStr);
			
		}catch(java.text.ParseException pe){}
		codComNas="L157"; desComNas="THIENE"; 
		rs = new RicercaSoggettoCosapDTO();
		rs.setEnteId(enteID);
		rs.setProvenienza("COSAP");
		rs.setTipoSogg("F");
		rs.setNome(nome);
		rs.setCognome(cognome);
		rs.setDtNas(dtNas);
		rs.setDesComNas(desComNas);
		rs.setCodComNas(codComNas);
		rs.setUpperCase();
		listaSogg = serv.searchSoggettoCosap(rs);
		System.out.print("----- SOGGETTI COSAP ---");
		stampa(listaSogg);
		//PG--dati completi
		String pIva="00598410249";
		rs = new RicercaSoggettoCosapDTO();
		rs.setEnteId(enteID);
		rs.setProvenienza("COSAP");
		rs.setTipoSogg("G");
		rs.setParIva(pIva);
		rs.setUpperCase();
		listaSogg = serv.searchSoggettoCosap(rs);
		Date dtRif = new Date();
		if (listaSogg!=null && listaSogg.size() > 0) {
			SitTCosapContrib ele = listaSogg.get(0);
			String idSogg = ele.getId();
			rs.setIdSoggCosap(idSogg);
			SitTCosapContrib sogg = serv.getDatiCosapSoggetto(rs);
			System.out.print("----- DATI COSAP SOGGETTO CON PIVA: " + pIva+ "----");
			stampaOgg(sogg);
			List<SitTCosapTassa> listaOgg = serv.getDatiCosap(rs,dtRif);
			System.out.print("----- DATI OGGETTI COSAP ----"); 
			if (listaOgg!=null && listaOgg.size() >0){
				stampa(listaOgg);	
			}
			
		}
		String denom="TRAPUNTIFICIO AMERICA";
		rs = new RicercaSoggettoCosapDTO();
		rs.setEnteId(enteID);
		rs.setProvenienza("COSAP");
		rs.setTipoSogg("G");
		rs.setDenom(denom);
		rs.setUpperCase();
		listaSogg = serv.searchSoggettoCosap(rs);
		dtRif = new Date();
		if (listaSogg!=null && listaSogg.size() > 0) {
			SitTCosapContrib ele = listaSogg.get(0);
			String idSogg = ele.getId();
			rs.setIdSoggCosap(idSogg);
			SitTCosapContrib sogg = serv.getDatiCosapSoggetto(rs);
			System.out.print("----- DATI COSAP SOGGETTO CON DENOMINAZIONE: " + denom+ "----");
			stampaOgg(sogg);
			List<SitTCosapTassa> listaOgg = serv.getDatiCosap(rs,dtRif);
			System.out.print("----- DATI OGGETTI COSAP ----"); 
			if (listaOgg!=null && listaOgg.size() >0){
				stampa(listaOgg);	
			}
			
		}
		
	}	
	//inpu dati test per enteID=roma
	private void testCnc(ServiceLocator locator, String enteID) {
	   String cf ="BCCNNA24L53E079U";
	   int annoRif=2003;
	   CncCarContribService cncService =(CncCarContribService)locator.getReference("Servizio_CarContrib/CncCarContribServiceBean/remote");
	   RicercaCncDTO rc = new RicercaCncDTO();
	   rc.setEnteId(enteID);
	   rc.setUserId("user");
	   rc.setCodEnteCreditore("F205");//AL MOMENTO CI SONO DATI SOLO PER F205
	   rc.setCodFis(cf);
	  
	   List<VAnagrafica> listaAna = cncService.getAnagraficaDebitore(rc);
	   System.out.print("---- RECORD ANAGRAFICI FLUSSO 750 CNC. CF: " + cf +"----");
	   if (listaAna!=null) {
		   stampa(listaAna); 
	   }
	   System.out.print("TestServlet. Chiama getDatiCNC()");
	   List<DatiImportiCncDTO> listaCnc = cncService.getDatiCNC(rc, annoRif);
	   System.out.print("---- DATI IMPORTI CNC.ANNO RIF. " + annoRif +"----");
	   if (listaCnc!=null) {
		   stampa(listaCnc);
	   }
	 	//altro soggetto
	   cf ="MRSRLN45T51D279U";
	   annoRif=2010;
	   rc = new RicercaCncDTO();
	   rc.setEnteId(enteID);
	   rc.setUserId("user");
	   rc.setCodEnteCreditore("F205");//AL MOMENTO CI SONO DATI SOLO PER F205
	   rc.setCodFis(cf);
	  
	   listaAna = cncService.getAnagraficaDebitore(rc);
	   System.out.print("---- RECORD ANAGRAFICI FLUSSO 750 CNC. CF: " + cf +"----");
	   if (listaAna!=null) {
		   stampa(listaAna); 
	   }
	   System.out.print("TestServlet. Chiama getDatiCNC()");
	   listaCnc = cncService.getDatiCNC(rc, annoRif);
	   System.out.print("---- DATI IMPORTI CNC.ANNO RIF. " + annoRif +"----");
	   if (listaCnc!=null) {
		   stampa(listaCnc);
	   }
	 
	}
	
	//inpu dati test per enteID=roma
	private void testIciCnc(ServiceLocator locator, String enteID) {
	   //soggetto per test cnc-750
		String cf ="BCCNNA24L53E079U";
	   int annoRif=2003;
	   CncCarContribService cncService =(CncCarContribService)locator.getReference("Servizio_CarContrib/CncCarContribServiceBean/remote");
	   RicercaCncDTO rc = new RicercaCncDTO();
	   rc.setEnteId(enteID);
	   rc.setUserId("user");
	   rc.setCodEnteCreditore("F205");//AL MOMENTO CI SONO DATI SOLO PER F205
	   rc.setCodFis(cf);
	    List<DatiImportiCncDTO> listaCnc = cncService.getDatiCNC(rc, annoRif);
	   System.out.print("---- DATI IMPORTI CNC  CF:  " +cf + " ANNO RIF. " + annoRif +"----");
	   if (listaCnc!=null) {
		   stampa(listaCnc);
	   }
	 //ICI-CNC
	   IciServiceCarContrib servIci = (IciServiceCarContrib)locator.getReference("Servizio_CarContrib/IciServiceCarContribBean/remote");
	    String idSoggIci="";
		RicercaSoggettoIciDTO rs = new RicercaSoggettoIciDTO();
		rs.setCodFis(cf);
		rs.setEnteId(enteID);
		rs.setIdSoggIci(idSoggIci);
		//List<VersamentoDTO> lista = servIci.getVersamentiOLDrs, annoRif) ;
		//NUOVO METODO ACQUISIZIONE VERSAMENBTI
		//System.out.print("ICI VERSAMENTI -METODO OLD");
		//stampa(lista);
		//NUOVO METODO ACQUISIZIONE VERSAMENBTI
		System.out.print("ICI VERSAMENTI -METODO NEW");
		List<DatiImportiCncDTO> lista1 = servIci.getVersamenti(rs, annoRif) ;
		stampa(lista1);
		
		//altro soggetto PER TEST RISCOSSIONI
	   cf ="MRSRLN45T51D279U";
	   annoRif=2010;
	   rc = new RicercaCncDTO();
	   rc.setEnteId(enteID);
	   rc.setUserId("user");
	   rc.setCodEnteCreditore("F205");//AL MOMENTO CI SONO DATI SOLO PER F205
	   rc.setCodFis(cf);
	   listaCnc = cncService.getDatiCNC(rc, annoRif);
	   System.out.print("---- DATI IMPORTI CNC  CF:  " +cf + " ANNO RIF. " + annoRif +"----");
	   if (listaCnc!=null) {
		   stampa(listaCnc);
	   }
	   //ICI-CNC
	    idSoggIci="";
		rs = new RicercaSoggettoIciDTO();
		rs.setCodFis(cf);
		rs.setEnteId(enteID);
		rs.setIdSoggIci(idSoggIci);
		//lista = servIci.getVersamentiOLD(rs, annoRif) ;
		//NUOVO METODO ACQUISIZIONE VERSAMENBTI
		//System.out.print("ICI VERSAMENTI -METODO OLD");
		//stampa(lista);
		//NUOVO METODO ACQUISIZIONE VERSAMENBTI
		System.out.print("ICI VERSAMENTI -METODO NEW");
		lista1 = servIci.getVersamenti(rs, annoRif) ;
		stampa(lista1);
	}
	private void testRedditi(ServiceLocator locator, String enteID) {
	   //
	    RicercaSoggettoDTO rs = new RicercaSoggettoDTO();
	    rs.setEnteId(enteID);
	    String cf ="VJNCST64H55F205T";
	    rs.setCodFis(cf);
	    RicercaDTO ric = new RicercaDTO();
	    ric.setEnteId(enteID);
	    ric.setObjEntity(null);
	    ric.setObjFiltro(rs);
	    RedditiCarContribService redSer = (RedditiCarContribService)locator.getReference("Servizio_CarContrib/RedditiCarContribServiceBean/remote");
	    //RedditiService redCTSer = (RedditiService)locator.getReference("CT_Service/RedditiServiceBean/remote");
	    List<RedDatiAnagrafici> listaSogg = redSer.searchSoggettiCorrelatiRedditi(ric);
	    if (listaSogg==null)
	    	return;
	    RedDatiAnagrafici sogg = listaSogg.get(0);
	    KeySoggDTO keySogg = new KeySoggDTO();
		keySogg.setCodFis(cf);
		keySogg.setEnteId(enteID);
		keySogg.setIdeTelematico(sogg.getIdeTelematico());
		RedDatiAnagrafici sogg1 = redSer.getSoggettoByKey(keySogg);
		System.out.println("REDDITI DATI ANAGRAFICI" ) ;
		stampaOgg(sogg1);
		RedDomicilioFiscale dom = redSer.getDomicilioByKey(keySogg);
		System.out.println("REDDITI DOMICILIO" ) ;
		stampaOgg(dom);
		List<RedditiDicDTO>  listaRed = redSer.getRedditiByKey(keySogg);
		System.out.println("REDDITI" ) ;
        stampa(listaRed);
		
		
		
	   //TODO
	}		   
	
	//Per effettuare il test dei metodi di AnagrafeService
	private void testaCtServiceAnag(ServiceLocator locator, String codEnte) {
		AnagrafeService anaSer = (AnagrafeService)locator.getReference("CT_Service/AnagrafeServiceBean/remote");
		String cf = "BRTNRN20H60B395U";
		RicercaSoggettoAnagrafeDTO rs = new RicercaSoggettoAnagrafeDTO();
		rs.setCodFis(cf);
		rs.setEnteId(codEnte);
		String idSoggAna = anaSer.getIdPersonaByCF(rs);
		if (idSoggAna!=null) {
			System.out.println("IDSOGGETTO IN ANAGRAFE: " + idSoggAna ) ;
			rs.setIdSogg(idSoggAna);
			List<SitDPersona> listaPersone=anaSer.getVariazioniPersonaByIdExt(rs);
			System.out.println("VARIAZIONI PERSONA" ) ;
			stampa(listaPersone);
			System.out.println("RICERCA INDIRIZZO PERSONA SENZA DATA " ) ;
			IndirizzoAnagrafeDTO  indPers = anaSer.getIndirizzoPersona(rs);
			stampaOgg(indPers);
			//INDIRIZZO PERSONA PER DATA
			String dtRifStr="31/12/2009";
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			Date dtRif=new Date();
			try {dtRif= df.parse(dtRifStr);}catch(java.text.ParseException pe){}
			System.out.println("RICERCA INDIRIZZO PERSONA PER DATA: " + dtRifStr ) ;
			indPers = anaSer.getIndirizzoPersona(rs);
			stampaOgg(indPers);
		}
	}
	//Per effettuare il test dei metodi di AnagrafeService
	private void testaDatiAnag(ServiceLocator locator, String codEnte) {
		AnagrafeService anaSer = (AnagrafeService)locator.getReference("CT_Service/AnagrafeServiceBean/remote");
		String id = "20090610 155646                                        0000251323    1";
		id = "20100308 104312                                        0000170541    1";
		RicercaSoggettoAnagrafeDTO rs = new RicercaSoggettoAnagrafeDTO();
		rs.setIdVarSogg(id);
		rs.setEnteId(codEnte);
		String dtRifStr="31/12/2009";
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date dtRif=new Date();
		try {
			dtRif= df.parse(dtRifStr);
			
		}catch(java.text.ParseException pe){}
		dtRif=null;//i dati relativi a comune e provincia per data validità non sono attendibili. Vedi ad es; select * from sit_provincia where id_ext='001    1'; in diogene_f704
		rs.setDtRif(dtRif);
		DatiPersonaDTO dati = anaSer.getDatiPersonaById(rs);
		if (dati!=null) {
			System.out.println("SOGGETTO TROVATO IN ANAGRAFE") ;
			stampaOgg(dati.getPersona());
			System.out.println("COMUNE NASCITA - PROVINCIA NASCITA: "+ dati.getDesComNas() + " - " + dati.getDesProvNas() + "(" + dati.getSiglaProvNas()+ ")") ;
			System.out.println("INDIRIZZO: "+ dati.getIndirizzoResidenza()) ;
			String idExt = dati.getPersona().getIdExt();
			rs.setIdSogg(idExt);
			List<ComponenteFamigliaDTO> listaCompFam= anaSer.getListaCompFamiglia(rs);
			stampa(listaCompFam);
		}
	}
	//Per effettuare il test dei metodi di CatastoService che utilizzo per l'app Cartella Contribuente
	private void testaCtService(ServiceLocator locator, String codEnte) {
		// TEST CT_Service
		CatastoService catSer = (CatastoService)locator.getReference("CT_Service/CatastoServiceBean/remote");
		String cf = "CNTGNN13A01M088L";//"BRTNRN20H60B395U";
		String dtRifStr="31/12/2009";
		String dtRifDaStr="01/01/2009";
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date dtRif=new Date();
		Date dtRifDa=new Date();
		try {
			dtRif= df.parse(dtRifStr);
			dtRifDa= df.parse(dtRifDaStr);
		}catch(java.text.ParseException pe){}
		RicercaSoggettoCatDTO rs = new RicercaSoggettoCatDTO();
		rs.setCodFis(cf);
		rs.setDtVal(dtRif);
		rs.setEnteId(codEnte);
		rs.setCodEnte(codEnte);
		List<BigDecimal> listaIdSogg = catSer.getIdSoggByCF(rs);
		BigDecimal idSogg =null;
		if (listaIdSogg !=null) 
			idSogg = listaIdSogg.get(0);
		//-->getIdSoggByCF
		System.out.println("TEST getIdSoggByCF(). ID_SOGG: " + idSogg);
		String dtRifOggStr="31/12/2005";
		Date dtRifOgg=new Date();
		try {
			dtRifOgg= df.parse(dtRifOggStr);
		}catch(java.text.ParseException pe){}
		//-->getDatiUiAllaData
		System.out.println("TEST getDatiUiAllaData().1");
		RicercaOggettoCatDTO roCat = new RicercaOggettoCatDTO(codEnte,"62","00276","13",dtRifOgg);
		Sitiuiu ui = catSer.getDatiUiAllaData(roCat);
		stampaOgg(ui);
		System.out.println("TEST getDatiUiAllaData().2");
		RicercaOggettoCatDTO roCat1 = new RicercaOggettoCatDTO(codEnte,"255","00043","13",dtRifOgg);
		ui = catSer.getDatiUiAllaData(roCat1);
		stampaOgg(ui);
		//--->getDatiBySoggUiAllaData
		System.out.println("TEST getDatiBySoggUiAllaData()1");
		rs.setIdSogg(idSogg);
		SiticonduzImmAll datiTitImm= catSer.getDatiBySoggUiAllaData(roCat, rs);
		stampaOgg(datiTitImm);
		//--->getDatiBySoggUiAllaData2
		datiTitImm= catSer.getDatiBySoggUiAllaData(roCat1, rs);
		stampaOgg(datiTitImm);
		//--->getLocalizzazioneCatastale 1
		System.out.println("TEST getLocalizzazioneCatastale()-1");
		List<IndirizzoDTO> listaInd = catSer.getLocalizzazioneCatastale(roCat);
		stampa(listaInd);
		//--->getLocalizzazioneCatastale 2
		System.out.println("TEST getLocalizzazioneCatastale()-2");
		listaInd = catSer.getLocalizzazioneCatastale(roCat1);
		stampa(listaInd);
		//-->getImmobiliByIdSogg
		System.out.println("TEST getImmobiliByIdSogg()");
		List<SiticonduzImmAll> listaImm = catSer.getImmobiliByIdSogg(rs);
		stampa(listaImm);
		//-->getImmobiliByIdSogg
		System.out.println("TEST getImmobiliByIdSoggCedutiInRangeDate()");
		rs.setDtRifA(dtRif);
		rs.setDtRifDa(dtRifDa);
		listaImm = catSer.getImmobiliByIdSoggCedutiInRangeDate(rs);
		stampa(listaImm);
		//Immobili e terreni per id sogg= 460101 alla data 
		rs.setIdSogg(new BigDecimal(460101)); 
		rs.setDtVal(dtRif);
		//-->getImmobiliByIdSogg
		System.out.println("TEST getImmobiliByIdSogg(). Data valorizzata.");
		listaImm = catSer.getImmobiliByIdSogg(rs);
		stampa(listaImm);
		//-->getImmobiliByIdSoggCedutiInRangeDate
		System.out.println("TEST getImmobiliByIdSoggCedutiInRangeDate()");
		rs.setDtRifA(dtRif);
		rs.setDtRifDa(dtRifDa);
		listaImm = catSer.getImmobiliByIdSoggCedutiInRangeDate(rs);
		stampa(listaImm);
		//-->getTerreniByIdSogg
		System.out.println("TEST getTerreniByIdSogg()");
		List<TerrenoPerSoggDTO> listaTerr = catSer.getTerreniByIdSogg(rs);
		stampa(listaTerr);
		//-->getTerreniByIdSoggCedutiInRangeDate
		System.out.println("TEST getTerreniByIdSoggCedutiInRangeDate()");
		listaTerr = catSer.getTerreniByIdSogg(rs);
		stampa(listaTerr);
		//Immobili e terreni per id sogg= 460101 senza la data  
		rs.setIdSogg(new BigDecimal(460101)); 
		rs.setDtVal(null);
		//-->getImmobiliByIdSogg
		System.out.println("TEST getImmobiliByIdSogg(). Data  NON valorizzata.");
		listaImm = catSer.getImmobiliByIdSogg(rs);
		stampa(listaImm);
		//-->getTerreniByIdSogg
		System.out.println("TEST getTerreniByIdSogg()");
		listaTerr = catSer.getTerreniByIdSogg(rs);
		stampa(listaTerr);
		
	}
	private void testaCtServiceTitolari(ServiceLocator locator, String codEnte, String idUiu) {
		// TEST CT_Service
		CatastoService catSer = (CatastoService)locator.getReference("CT_Service/CatastoServiceBean/remote");
		RicercaOggettoCatDTO ro = new RicercaOggettoCatDTO();
		ro.setEnteId(codEnte);
		ro.setCodEnte(codEnte);
		if (idUiu ==null || idUiu.equals(""))
			ro.setIdUiu("43467");
		else
			ro.setIdUiu(idUiu);
		//-->Attuali
		System.out.println("Titolari Attuali UI");
		List<it.webred.ct.data.access.basic.catasto.dto.SoggettoDTO> listaAtt =  catSer.getListaSoggettiAttualiImmobile(ro);
		stampa(listaAtt);
		//-->Storici
		System.out.println("Titolari Storici UI");
		List<it.webred.ct.data.access.basic.catasto.dto.SoggettoDTO> listaSto =  catSer.getListaSoggettiStoriciImmobile(ro);
		stampa(listaSto);
	}
	private void testaCtServiceICI(ServiceLocator locator, String codEnte) {
		// TEST CT_Service
		IciService iciSer = (IciService)locator.getReference("CT_Service/IciServiceBean/remote");
		String cf = "LLGTTR69R11Z133S";
		String idIci="20101103 000000                                          T@151227    2";
		//String codEnte="F205";
		RicercaSoggettoIciDTO rs = new RicercaSoggettoIciDTO();
		rs.setCodFis(cf);
		rs.setEnteId(codEnte);
		
		List<SitTIciSogg> lista = iciSer.getListaSogg(rs); 
		if (lista!= null)
			System.out.println("n. ele" + lista.size() + ". PRIMO: " + lista.get(0).getCogDenom());
		else
			System.out.println("NON TROVATO");
	}
	//METODO getVersamenti PER SCHEDA ICI
	private void testVersIci(ServiceLocator locator, String enteID) {
		// TEST CT_Service
		IciServiceCarContrib servIci = (IciServiceCarContrib)locator.getReference("Servizio_CarContrib/IciServiceCarContribBean/remote");
		String idSoggIci="20100921 000000                                       T@000881534    2";
		int annoRif = 2011;
		RicercaSoggettoIciDTO rs = new RicercaSoggettoIciDTO();
		rs.setEnteId(enteID);
		rs.setIdSoggIci(idSoggIci);
		//lista = servIci.getVersamentiOLD(rs, annoRif) ;
		//stampa(lista);
		//NUOVO METODO ACQUISIZIONE VERSAMENBTI
		List<DatiImportiCncDTO>  lista1 = servIci.getVersamenti(rs, annoRif) ;
		stampa(lista1);
	}
	//SITUAZION. PATRIM PER SCHEDA  ICI
	private void test2(ServiceLocator locator, String codEnte) {
		// TEST CT_Service
		IciServiceCarContrib servIci = (IciServiceCarContrib)locator.getReference("Servizio_CarContrib/IciServiceCarContribBean/remote");
		GeneralService genServ = (GeneralService)locator.getReference("Servizio_CarContrib/GeneralServiceBean/remote");
		/*
		 * NESSUN DATO
		String cf = "LLGTTR69R11Z133S";
		String idSoggIci="20101103 000000                                          T@151227    2";
		int annoRif = 2009;
		*/
		//DATI CNC-750 PRESENTI
		/*
		String cf = "STFBRN57E14F205Y";
		String idSoggIci="";
		int annoRif = 2001;
		*/
		//IMMOBILI
		String cf = "BLLSVN31M46F205U";
		System.out.println("IMMOBILI PER SOGGETTO: " + cf);
		Date dtRif =new Date();
		SoggettoDTO sogg = new SoggettoDTO();
		sogg.setTipoSogg("F");
		sogg.setCodFis(cf);
		sogg.setEnteId(codEnte);
		RicercaDTO ric = new RicercaDTO();
		ric.setEnteId(codEnte);
		ric.setUserId("user");
		ric.setObjEntity(null);
		ric.setObjFiltro(sogg);
		IndiciSoggettoDTO indici = genServ.getIndiciSoggetto(ric, dtRif) ;
		CatastoCarContribService servCatCC = (CatastoCarContribService)locator.getReference("Servizio_CarContrib/CatastoCarContribServiceBean/remote");
		if (indici != null) {
			List<BigDecimal> indiciAnagCat = indici.getListaIdSoggAnagCat();
			if (indiciAnagCat != null && indiciAnagCat.size() == 1 ) {
				BigDecimal idSoggCat = indiciAnagCat.get(0);
				System.out.println("recuperato idSogg Cat: " + idSoggCat);
				RicercaSoggettoCatDTO rs = new RicercaSoggettoCatDTO();
				rs.setEnteId(codEnte);
				rs.setCodEnte(codEnte);
				rs.setIdSogg(idSoggCat);
				rs.setDtVal(dtRif);
				List<SitPatrimImmobileDTO> listaImm = servCatCC.getImmobiliPosseduti(rs, "ICI");
				if (listaImm != null){
					System.out.println("STAMPA IMMOBILI POSSEDUTI AL: " + dtRif.toString());
					stampa(listaImm);
				}
				
				String dtRifAStr="31/12/2009";
				String dtRifDaStr="01/01/2009";
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				Date dtRifA=new Date();
				Date dtRifDa=new Date();
				try {
					dtRifA= df.parse(dtRifAStr);
					dtRifDa= df.parse(dtRifDaStr);
				}catch(java.text.ParseException pe){}
				rs.setDtRifDa(dtRifDa);
				rs.setDtRifA(dtRifA);
				rs.setDtVal(null);
				List<SitPatrimImmobileDTO> listaImmCeduti= servCatCC.getImmobiliCeduti(rs);
				if (listaImmCeduti != null){
					System.out.println("STAMPA IMMOBILI CEDUTI DAL " + dtRifDa.toString() + " AL: " + dtRifA.toString());
					stampa(listaImmCeduti);
				}
			}
			//terreni
			System.out.println("TERRENI PER SOGGETTO: " + cf);
			cf="CLZRRT28H22F205N";
			sogg.setCodFis(cf);
			ric = new RicercaDTO();
			ric.setEnteId(codEnte);
			ric.setUserId("user");
			ric.setObjEntity(null);
			ric.setObjFiltro(sogg);
    	    indici = genServ.getIndiciSoggetto(ric, dtRif) ;
    	    if (indici != null) {
    			indiciAnagCat = indici.getListaIdSoggAnagCat();
    			if (indiciAnagCat != null && indiciAnagCat.size() == 1 ) {
    				BigDecimal idSoggCat = indiciAnagCat.get(0);
    				System.out.println("recuperato idSogg Cat: " + idSoggCat);
    				RicercaSoggettoCatDTO rs = new RicercaSoggettoCatDTO();
    				rs.setEnteId(codEnte);
    				rs.setCodEnte(codEnte);
    				rs.setIdSogg(idSoggCat);
    				rs.setDtVal(dtRif);
    				List<SitPatrimTerrenoDTO>  listaTerr = servCatCC.getTerreniPosseduti(rs);
    				if (listaTerr != null){
    					System.out.println("STAMPA TERRENI POSSEDUTI AL: " + dtRif.toString());
    					stampa(listaTerr);
    				}
    				
    				String dtRifAStr="31/12/2005";
    				String dtRifDaStr="01/01/2001";
    				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    				Date dtRifA=new Date();
    				Date dtRifDa=new Date();
    				try {
    					dtRifA= df.parse(dtRifAStr);
    					dtRifDa= df.parse(dtRifDaStr);
    				}catch(java.text.ParseException pe){}
    				rs.setDtRifDa(dtRifDa);
    				rs.setDtRifA(dtRifA);
    				rs.setDtVal(null);
    				List<SitPatrimTerrenoDTO> listaTerrCeduti= servCatCC.getTerreniCeduti(rs);
    				if (listaTerrCeduti != null){
    					System.out.println("STAMPA TERRENI CEDUTI DAL " + dtRifDa.toString() + " AL: " + dtRifA.toString());
    					stampa(listaTerrCeduti);
    				}
    			}
	   	    }
		}
	}
	
	//LOCAZIONI  
	private void testLocazioni(ServiceLocator locator, String codEnte) {
		System.out.print("test locazioni");
		String dtRifDaStr="01/01/2003"; //per il test va bene ma in rea
		GeneralService serv = (GeneralService)locator.getReference("Servizio_CarContrib/GeneralServiceBean/remote");
		System.out.print("---- LOCAZIONI DAL " + dtRifDaStr + "----");
		RicercaLocazioniDTO rl = new RicercaLocazioniDTO();
		rl.setEnteId(codEnte);
		rl.setUserId("user");
		String cf="BBISLV57R64B354Y";
		rl.setCodFis(cf);
		//rl.setDtRif(dtRifDa);//N.B. NON RICERCARE PER DATA perché NON SONO SEMPRE VAORIZZATE. ALL'OCCORRENZA SCORRERE LE RIGHE E SCARTARE.... 
		LocazioniCarContribService servLoc = (LocazioniCarContribService)locator.getReference("Servizio_CarContrib/LocazioniCarContribServiceBean/remote");
		RicercaDTO ric = new RicercaDTO();
		ric.setEnteId(codEnte);
		ric.setUserId("user");
		ric.setObjFiltro(rl);
		ric.setObjEntity(null);
		List<LocazioniDTO> listaLocazioni = servLoc.searchLocazioniBySogg(ric);
	   if (listaLocazioni!=null) {
		   System.out.println("--- LOCAZIONI ---" );
		   stampa(listaLocazioni);
	   }
	}
	
	//SCHEDA COMPONENTI NUCELO FAMILIARE
	
	private void testCompFamiglia(ServiceLocator locator, String codEnte) {
		AnagrafeService anaSer = (AnagrafeService)locator.getReference("CT_Service/AnagrafeServiceBean/remote");
		String id = "20090610 155646                                        0000019676    1";
		RicercaSoggettoAnagrafeDTO rs = new RicercaSoggettoAnagrafeDTO();
		rs.setIdVarSogg(id);
		rs.setEnteId(codEnte);
		SitDPersona pers=anaSer.getPersonaById(rs);
		rs.setIdSogg(pers.getIdExt());
		System.out.println("DATI PERSONA ANAGRAFE" ) ;
		stampaOgg(pers);
		IndirizzoAnagrafeDTO  indPers = anaSer.getIndirizzoPersona(rs);
		System.out.println("INDIRIZZO PERSONA -- SENZA DATA " ) ;
		stampaOgg(indPers);
		//INDIRIZZO PERSONA PER DATA
		String dtRifStr="31/12/2010";
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date dtRif=new Date();
		try {dtRif= df.parse(dtRifStr);}catch(java.text.ParseException pe){}
		rs.setDtRif(dtRif);
		indPers = anaSer.getIndirizzoPersona(rs);
		System.out.println("INDIRIZZO PERSONA PER DATA: " + dtRifStr ) ;
		stampaOgg(indPers);
		//COMPONENTI NUCLE0
		rs.setIdSogg(pers.getIdExt());
		List<ComponenteFamigliaDTO> listaComp = anaSer.getListaCompFamiglia(rs);
		System.out.println("COMPONENTI NUCELO FAMILIARE ALLA DATA " + dtRifStr ) ;
		stampa(listaComp);
		
	}
	
	private void testCCDataModel(ServiceLocator locator, String codEnte) {
		CarContribService ser = (CarContribService)locator.getReference("Servizio_CarContrib/CarContribServiceBean/remote");
		long codTipDoc= 2;
		TipDocDTO tdDTO  = new TipDocDTO();
		tdDTO.setEnteId(codEnte);
		DecodTipDoc dc = new DecodTipDoc();
		dc.setCodTipDoc(codTipDoc);
		tdDTO.setDecodTipDoc(dc);
		DecodTipDoc dec = ser.getTipDoc(tdDTO);
		System.out.println("DECODIFICA TIPO DOC: " + codTipDoc);
		stampaOgg(dec);
	}
	private void testInsertRichiesta(ServiceLocator locator, String codEnte) {
		CarContribService ser = (CarContribService)locator.getReference("Servizio_CarContrib/CarContribServiceBean/remote");
		SoggettiCarContribDTO soggRicDTO=null;
		SoggettiCarContribDTO soggCarDTO= null; 
		RichiesteDTO richDTO = new RichiesteDTO();
		String codTipSogg, cf, nome, cognome, codComNas;
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String dtNasStr="21/07/1954";
		Date dtNas=null;
		codTipSogg="F";
		cf="BRTMNE54S52F205D";
		nome="BERTA";
		cognome="EMANUELA";
		codComNas="F205";
		try {dtNas= df.parse(dtNasStr);}catch(java.text.ParseException pe){}
		SoggettiCarContrib sogg=new SoggettiCarContrib();
		soggRicDTO=new SoggettiCarContribDTO(sogg);
		soggRicDTO.getSogg().setCodTipSogg(codTipSogg);
		soggRicDTO.getSogg().setCodFis(cf);
		soggRicDTO.getSogg().setCognome(cognome);
		soggRicDTO.getSogg().setNome(nome);
		soggRicDTO.getSogg().setCodComNas(codComNas);
		soggRicDTO.getSogg().setDtNas(dtNas);
		soggRicDTO.setEnteId(codEnte);
		soggRicDTO.setUserId("test");
		//
		richDTO.setEnteId(codEnte);
		richDTO.setUserId("test");
		Richieste rich = new Richieste();
		rich.setNumDocRicon("MI123456");
		rich.setCodTipDocRicon("1");
		rich.setCodTipRic("C");
		String dtEmiStr="01/12/2007";
		Date dtEmi=null;
		try {dtEmi= df.parse(dtEmiStr);}catch(java.text.ParseException pe){}
		rich.setDtEmiDocRicon(dtEmi);
		rich.setDtRic(new Date());
		rich.setCodTipProven("A");
		richDTO.setRich(rich);
		Long id = ser.insertRichiesta(soggRicDTO, soggCarDTO, richDTO);
		System.out.println("INSERITA RICHIESTA ID: " +  id + " PER SOGGETTO: " + cognome + " " + nome);
		//
		codTipSogg="G";
		String pIva = "00665544332";
		String denom="AZIENDA PROVA";
		sogg=new SoggettiCarContrib();
		soggRicDTO=new SoggettiCarContribDTO(sogg);
		soggRicDTO.getSogg().setCodTipSogg(codTipSogg);
		soggRicDTO.getSogg().setParIva(pIva);
		soggRicDTO.getSogg().setDenomSoc(denom);
		soggRicDTO.setEnteId(codEnte);
		soggRicDTO.setEnteId(codEnte);
		soggRicDTO.setUserId("test");
		richDTO= new RichiesteDTO();
		richDTO.setEnteId(codEnte);
		richDTO.setUserId("test");
		rich = new Richieste();
		rich.setNumDocRicon("MI765432");
		rich.setCodTipDocRicon("1");
		rich.setCodTipRic("C");
		dtEmiStr="11/05/2006";
		dtEmi=null;
		try {dtEmi= df.parse(dtEmiStr);}catch(java.text.ParseException pe){}
		rich.setDtEmiDocRicon(dtEmi);
		rich.setDtRic(new Date());
		rich.setCodTipProven("A");
		richDTO.setRich(rich);
		id = ser.insertRichiesta(soggRicDTO, soggCarDTO, richDTO);
		System.out.println("INSERITA RICHIESTA ID: " +  id + " PER SOGGETTO: " + pIva + " - " + denom);
		//
		soggRicDTO=new SoggettiCarContribDTO(sogg);
		soggRicDTO.setSogg(null);
		soggRicDTO.setEnteId(codEnte);
		soggRicDTO.setUserId("test");
		dtNasStr="23/02/1970";
		codTipSogg="F";
		cf="MBRSTF70S52D653J";
		nome="AMBROGI";
		cognome="STEFANIA";
		codComNas="D653";
		sogg=new SoggettiCarContrib();
		soggCarDTO=new SoggettiCarContribDTO(sogg);
		soggCarDTO.getSogg().setCodTipSogg(codTipSogg);
		soggCarDTO.getSogg().setCodFis(cf);
		soggCarDTO.getSogg().setCognome(cognome);
		soggCarDTO.getSogg().setNome(nome);
		soggCarDTO.getSogg().setCodComNas(codComNas);
		soggCarDTO.getSogg().setDtNas(dtNas);
		soggCarDTO.setEnteId(codEnte);
		soggCarDTO.setUserId("test");
		rich = new Richieste();
		richDTO.setEnteId(codEnte);
		richDTO.setUserId("test");
		rich.setNumDocRicon("PG675412");
		rich.setCodTipDocRicon("1");
		rich.setCodTipRic("C");
		dtEmiStr="01/12/2003";
		dtEmi=null;
		try {dtEmi= df.parse(dtEmiStr);}catch(java.text.ParseException pe){}
		rich.setDtEmiDocRicon(dtEmi);
		rich.setDtRic(new Date());
		rich.setCodTipProven("A");
		rich.setUserNameRic("test");
		richDTO.setRich(rich);
		id = ser.insertRichiesta(soggRicDTO, soggCarDTO, richDTO);
		System.out.println("INSERITA RICHIESTA ID: " +  id + " PER SOGGETTO: " + cognome + " " + nome);
	}
	
	private void testaIndCorrTarsu(ServiceLocator locator, String enteID) {
		String cognome="DINOLFO";
		String nome="ANDREA";
		String cf="DNLNDR72S20F704W";
		String codComNas="015149";
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String dtNasStr="20/11/1972";
		Date dtNas=null;
		try {dtNas= df.parse(dtNasStr);}catch(java.text.ParseException pe){}
		RicercaSoggettoTarsuDTO sogg = new RicercaSoggettoTarsuDTO();
		sogg.setEnteId(enteID);
		sogg.setUserId("user");
		sogg.setCognome(cognome);
		sogg.setNome(nome);
		sogg.setDtNas(dtNas);
		sogg.setCodComNas(codComNas);
		sogg.setCodFis(cf);
		sogg.setTipoSogg("F");
		sogg.setProvenienza("T");
		String desTblProv="SIT_T_TAR_SOGG";
		GeneralService servGen = (GeneralService)locator.getReference("Servizio_CarContrib/GeneralServiceBean/remote");
		Object entityBean = servGen.getEntityBean(sogg, desTblProv);
		TarsuCarContribService servTar = (TarsuCarContribService)locator.getReference("Servizio_CarContrib/TarsuCarContribServiceBean/remote");
		RicercaDTO dati = new RicercaDTO();
		dati.setEnteId(enteID);
		dati.setUserId("user");
		dati.setObjEntity(entityBean);
		dati.setObjFiltro(sogg);
		try{
			List<SitTTarSogg> listaSogg =servTar.searchSoggettiCorrelatiTarsu(dati) ;
			System.out.println("--LISTA SOGGETTI CORRELATI TARSU--");
			if (listaSogg != null) {
				for (SitTTarSogg ele:listaSogg) {
					System.out.println("ID SOGG TARSU: " + ele.getId());
				}
			}
		}catch(IndiceNonAllineatoException ie){
			System.out.println( ie.getMessage());
		}
	
	}	 
	private void testaIndCorrIci(ServiceLocator locator, String enteID) {
		String cognome="DINOLFO";
		String nome="ANDREA";
		String cf="DNLNDR72S20F704W";
		String codComNas="015149";
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String dtNasStr="20/11/1972";
		Date dtNas=null;
		cognome="VERDICCHIA"; nome="COSIMA"; cf="VRDCSM69C51D305N"; codComNas="075027"; dtNasStr="11/03/1969";
		try {dtNas= df.parse(dtNasStr);}catch(java.text.ParseException pe){}
		RicercaSoggettoIciDTO sogg = new RicercaSoggettoIciDTO();
		sogg.setEnteId(enteID);
		sogg.setUserId("user");
		sogg.setCognome(cognome);
		sogg.setNome(nome);
		sogg.setDtNas(dtNas);
		sogg.setCodComNas(codComNas);
		sogg.setCodFis(cf);
		sogg.setTipoSogg("F");
		sogg.setProvenienza("T");
		//String desTblProv="SIT_T_TAR_SOGG";//--> signifca il sogg è stato identificato in tarsu
		String desTblProv="SIT_T_ICI_SOGG";//--> signifca il sogg è stato identificato in tarsu
		GeneralService servGen = (GeneralService)locator.getReference("Servizio_CarContrib/GeneralServiceBean/remote");
		Object entityBean = servGen.getEntityBean(sogg, desTblProv);
		IciServiceCarContrib servIci = (IciServiceCarContrib)locator.getReference("Servizio_CarContrib/IciServiceCarContribBean/remote");
		RicercaDTO dati = new RicercaDTO();
		dati.setEnteId(enteID);
		dati.setUserId("user");
		dati.setObjEntity(entityBean);
		dati.setObjFiltro(sogg);
		try{
			List<SitTIciSogg> listaSogg =servIci.searchSoggettiCorrelatiIci(dati) ;
			System.out.println("--LISTA SOGGETTI CORRELATI ICI--");
			if (listaSogg != null) {
				for (SitTIciSogg ele:listaSogg) {
					System.out.println("ID SOGG ICI: " + ele.getId());
				}
			}
		}catch(IndiceNonAllineatoException ie){
			System.out.println( ie.getMessage());
		}
		
		//TEST INDICI 
		String dtRifStr="28/01/2011";
		Date dtRif=null;
		try {dtRif= df.parse(dtRifStr);}catch(java.text.ParseException pe){}
		SoggettoDTO soggD = new SoggettoDTO();
		soggD.setEnteId(enteID);
		soggD.setUserId("user");
		soggD.setCognome(cognome);
		soggD.setNome(nome);
		soggD.setDtNas(dtNas);
		soggD.setCodComNas(codComNas);
		soggD.setCodFis(cf);
		soggD.setTipoSogg("F");
		dati = new RicercaDTO();
		dati.setEnteId(enteID);
		dati.setUserId("user");
		dati.setObjEntity(entityBean);
		dati.setObjFiltro(soggD);
		IndiciSoggettoDTO indici= servGen.getIndiciSoggetto(dati, null);
		List<String> listaIndAna = indici.getListaIdSoggAnagGen();
		List<BigDecimal> listaIndCat = indici.getListaIdSoggAnagCat();
		if (listaIndAna != null) {
			for (String ele:listaIndAna) {
				System.out.println("ID SOGG IN ANAGRAFE: " + ele);
			}
		}
		if (listaIndCat != null) {
			for (BigDecimal ele:listaIndCat) {
				System.out.println("ID SOGG IN CATASTO: " + ele);
			}		
		}
	}
	private void testaIndCorrRed(ServiceLocator locator, String enteID) {
		String cognome="DINOLFO";
		String nome="ANDREA";
		String cf="DNLNDR72S20F704W";
		String codComNas="015149";
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String dtNasStr="20/11/1972";
		Date dtNas=null;
		try {dtNas= df.parse(dtNasStr);}catch(java.text.ParseException pe){}
		RicercaSoggettoDTO sogg = new RicercaSoggettoDTO();
		sogg.setEnteId(enteID);
		sogg.setUserId("user");
		sogg.setCognome(cognome);
		sogg.setNome(nome);
		sogg.setDtNas(dtNas);
		sogg.setCodComNas(codComNas);
		sogg.setCodFis(cf);
		sogg.setTipoSogg("F");
		String desTblProv="SIT_T_ICI_SOGG";
		GeneralService servGen = (GeneralService)locator.getReference("Servizio_CarContrib/GeneralServiceBean/remote");
		Object entityBean = servGen.getEntityBean(sogg, desTblProv);
		RedditiCarContribService servRed = (RedditiCarContribService)locator.getReference("Servizio_CarContrib/RedditiCarContribServiceBean/remote");
		RicercaDTO dati = new RicercaDTO();
		dati.setEnteId(enteID);
		dati.setUserId("user");
		dati.setObjEntity(entityBean);
		dati.setObjFiltro(sogg);
		List<RedDatiAnagrafici> listaSogg =servRed.searchSoggettiCorrelatiRedditi(dati);
		System.out.println("--LISTA SOGGETTI CORRELATI REDDITI--");
		if (listaSogg != null) {
			for (RedDatiAnagrafici ele:listaSogg) {
				System.out.println("ID SOGG RED (ide-tel/cf): " + ele.getIdeTelematico() + "/" + ele.getCodiceFiscaleDic());
			}
		}
	
	}	 
	private void testaIndCorrIndiretto(ServiceLocator locator, String enteID) {
		String codNazionale="F704";
		String sezione ="";
		String foglio="57";
		String particella="00005";
		Date data=null;
		IndiceCorrelazioneService serv = (IndiceCorrelazioneService)locator.getReference("CT_Service/IndiceCorrelazioneServiceBean/remote");
		String keyStr = codNazionale + "-" + sezione + "-" + foglio+ "-" + particella + "-" + data;
		RicercaIndiceDTO ri= new RicercaIndiceDTO();
		ri.setEnteId(enteID);
		ri.setUserId("user");
		KeyFabbricatoDTO keyFabbr = new KeyFabbricatoDTO(null, codNazionale, sezione, foglio, particella, data);
		ri.setObj(keyFabbr);
		ri.setProgressivoEs(null);
		ri.setDestFonte("2"); //
		//ri.setDestProgressivoEs("2");//per test correlqazione ici
		ri.setDestProgressivoEs("3");//per test correlqazione tarsu
		//List<Object> lista = serv.getCiviciCorrelatiFromFabbricato(ri);
		//
		List lista = serv.getCiviciCorrelatiFromFabbricato(ri);
		System.out.println("ACQUISIZIONE CORRELAZIONI INDIRETTE TARSU FABBRICATO-CIVICI. KEY: " + keyStr);
		System.out.println("NUMERO CORRELAZIONI CIVICI: "+ lista.size() );
		stampa(lista);
		//oggetti tarsu ai civici deol fabbricato
		List<VTTarCiviciAll> listaT =(List<VTTarCiviciAll>)lista;
		TarsuService servT = (TarsuService)locator.getReference("CT_Service/TarsuServiceBean/remote");
		RicercaOggettoTarsuDTO rt = new RicercaOggettoTarsuDTO();
		rt.setEnteId(enteID);
		rt.setListaCivTarsu(listaT);
		List<SitTTarOggetto> listaO = servT.getListaOggettiAiCiviciTarsu(rt);
		stampa(listaO);
		//controprova
		System.out.println("ACQUISIZIONE OGGETTI AI CIVICI TARSU");
		TarsuCarContribService servTCC = (TarsuCarContribService)locator.getReference("Servizio_CarContrib/TarsuCarContribServiceBean/remote");
		RicercaOggettoDTO rot = new RicercaOggettoDTO();
		rot.setEnteId(enteID);
		rot.setFoglio(foglio);
		rot.setParticella(particella);
		rot.setProvenienza("T");
		/*
		List<DatiTarsuDTO> listaDatiT = servTCC.getDatiTarsuCiviciDelFabbricato(rot);
		if(listaDatiT !=null) {
			System.out.println("NUMERO ELE.:"+ listaDatiT.size() );
			stampa(listaDatiT);		
		}
		*/
	/*
		String sub= "11";
		keyStr= codNazionale + "-" + sezione + "-" + foglio+ "-" + particella + "-" + sub+ "-" + data;
		KeyUIDTO keyUi = new KeyUIDTO(codNazionale, sezione, foglio, particella, data, sub);
		ri.setEnteId(enteID);
		ri.setUserId("user");
		ri.setObj(keyUi);
		lista = serv.getCiviciCorrelatiFromUI(ri);
		System.out.println("ACQUISIZIONE CORRELAZIONI INDIRETTE TARSU UI-CIVICI. KEY: " + keyStr);
		System.out.println("NUMERO CORRELAZIONI CIVICI: "+ lista.size() );
		for (Object ele:lista) {
			System.out.println("TIPO OGGETTO: " + ele.getClass().getName()  );
		}
		stampa(lista);
*/		
		//ricerca delle locazioni
/*		
		System.out.println("ACQUISIZIONE CORRELAZIONI INDIRETTE LOCAZIONI FABBRICATO-CIVICI (SOGGETTO). KEY: " + keyStr);
		foglio="98"; particella="147";sub=null;
		ri.setDestFonte("5"); //
		ri.setDestProgressivoEs("2");
		keyFabbr = new KeyFabbricatoDTO(codNazionale, sezione, foglio, particella, data);
		ri.setObj(keyFabbr);
		lista = serv.getCiviciCorrelatiFromFabbricato(ri);
		stampa(lista);
		System.out.println("ACQUISIZIONE CORRELAZIONI INDIRETTE LOCAZIONI FABBRICATO-CIVICI(FABBRICATO). KEY: " + keyStr);
		foglio="98"; particella="147";sub=null;
		ri.setDestFonte("5"); //
		ri.setDestProgressivoEs("1");
		keyFabbr = new KeyFabbricatoDTO(codNazionale, sezione, foglio, particella, data);
		ri.setObj(keyFabbr);
		lista = serv.getCiviciCorrelatiFromFabbricato(ri);
		stampa(lista);
*/		
	}
	
	private void testaIndCorrTutte(ServiceLocator locator, String enteID) {
		//34-181-24: CI SONO LOCAZIONI SU F,P,S, NO SUL CIVICO
		//34/291/1:LOCAZIONI AL CIVICO CORRISPONDENTE, no su coordinate 
		//10-262-27:OGGETTI ICI
		String codNazionale="F704";	String sezione ="";	String foglio="34";	String particella="00181";	Date data=null;String sub= "24";
		IndiceCorrelazioneService serv = (IndiceCorrelazioneService)locator.getReference("CT_Service/IndiceCorrelazioneServiceBean/remote");
		String keyStr = codNazionale + "-" + sezione + "-" + foglio+ "-" + particella + "-" + data;
		RicercaIndiceDTO ri= new RicercaIndiceDTO();
		ri.setEnteId(enteID);
		ri.setUserId("user");
		KeyFabbricatoDTO keyFabbr = new KeyFabbricatoDTO(null, codNazionale, sezione, foglio, particella, data);
		ri.setObj(keyFabbr);
		ri.setProgressivoEs(null);
		ri.setDestFonte("7"); //
		ri.setDestProgressivoEs("2");//--> note di compravendita
		//List<Object> lista = serv.getCiviciCorrelatiFromFabbricato(ri);
		//System.out.println("ACQUISIZIONE di TUTTE LE CORRELAZIONI PER FABBRICATO KEY: " + keyStr);//DA FARE......
		//stampa(lista);
		foglio="34"; particella="291"; sub="1";
		keyStr= codNazionale + "-" + sezione + "-" + foglio+ "-" + particella + "-" + sub+ "-" + data;
		KeyUIDTO keyUi = new KeyUIDTO(null, codNazionale, sezione, foglio, particella, data, sub);
		ri.setObj(keyUi);
		List<Object>  lista = serv.getCorrelazioniOggettiFromUI(ri);
		System.out.println("ACQUISIZIONE DI TUTTE LE COMPRAVENDITE CORRELATE - U.I. KEY: " + keyStr );
		System.out.println("NUMERO CORRELAZIONI : "+ lista.size() );
		stampa(lista);
		//
		ri.setDestFonte("2"); //
		ri.setDestProgressivoEs("2");//--> OGGETTI ICI
		foglio="10"; particella="262"; sub="27";
		keyStr= codNazionale + "-" + sezione + "-" + foglio+ "-" + particella + "-" + sub+ "-" + data;
		keyUi = new KeyUIDTO(null, codNazionale, sezione, foglio, particella, data, sub);
		ri.setObj(keyUi);
		lista = serv.getCorrelazioniOggettiFromUI(ri);
		System.out.println("ACQUISIZIONE DI TUTTEI GLI OGGETTI ICI CORRELATI - U.I. KEY: " + keyStr );
		System.out.println("NUMERO CORRELAZIONI : "+ lista.size() );
		stampa(lista);
		//
		ri.setDestFonte("3"); //
		ri.setDestProgressivoEs("2");//--> CONCESSIONI EDILIZIE PRATICHE
		foglio="11";particella="276";sub= "5";
		keyStr= codNazionale + "-" + sezione + "-" + foglio+ "-" + particella + "-" + sub+ "-" + data;
		keyUi = new KeyUIDTO(null, codNazionale, sezione, foglio, particella, data, sub);
		ri.setObj(keyUi);
		lista = serv.getCorrelazioniOggettiFromUI(ri);
		System.out.println("ACQUISIZIONE DI TUTTE LE CONCESSIONI CORRELATE - U.I. KEY: " + keyStr );
		System.out.println("NUMERO CORRELAZIONI : "+ lista.size() );
		stampa(lista);
	}
	private void testaIndCorrTutteBis(ServiceLocator locator, String enteID) {
		//34-181-24: CI SONO LOCAZIONI SU F,P,S, NO SUL CIVICO
		//34/291/1:LOCAZIONI AL CIVICO CORRISPONDENTE, no su coordinate 
		//10-262-27:OGGETTI ICI
		String codNazionale="F704";	String sezione ="";	String foglio="3";	String particella="0095";	Date data=null;String sub= "45";
		IndiceCorrelazioneService serv = (IndiceCorrelazioneService)locator.getReference("CT_Service/IndiceCorrelazioneServiceBean/remote");
		String keyStr = codNazionale + "-" + sezione + "-" + foglio+ "-" + particella + "-" + data;
		RicercaIndiceDTO ri= new RicercaIndiceDTO();
		ri.setEnteId(enteID);
		ri.setUserId("user");
		KeyFabbricatoDTO keyFabbr = new KeyFabbricatoDTO(null, codNazionale, sezione, foglio, particella, data);
		ri.setObj(keyFabbr);
		ri.setProgressivoEs(null);
		ri.setDestFonte("3"); //
		ri.setDestProgressivoEs("2");//
		//List<Object> lista = serv.getCiviciCorrelatiFromFabbricato(ri);
		//System.out.println("ACQUISIZIONE di TUTTE LE CORRELAZIONI PER FABBRICATO KEY: " + keyStr);//DA FARE......
		//stampa(lista);
		
		//foglio="34"; particella="291"; sub="1";
		keyStr= codNazionale + "-" + sezione + "-" + foglio+ "-" + particella + "-" + sub+ "-" + data;
		KeyUIDTO keyUi = new KeyUIDTO(null, codNazionale, sezione, foglio, particella, data, sub);
		ri.setObj(keyUi);
		List<Object>  lista = serv.getCorrelazioniOggettiFromUI(ri);
		System.out.println("ACQUISIZIONE DI TUTTE LE CONCESSIONI CORRELATE - U.I. KEY: " + keyStr );
		System.out.println("NUMERO CORRELAZIONI : "+ lista.size() );
		stampa(lista);
		//
		
	}
	
	private void testListaResid(ServiceLocator locator, String enteID) {
		String codNazionale="F704";	String sezione ="";	String foglio="57";	String particella="5";	Date data=null;String sub= null;
		IndiceCorrelazioneService serv = (IndiceCorrelazioneService)locator.getReference("CT_Service/IndiceCorrelazioneServiceBean/remote");
		String keyStr = codNazionale + "-" + sezione + "-" + foglio+ "-" + particella + "-" + data;
		RicercaIndiceDTO ri= new RicercaIndiceDTO();
		ri.setEnteId(enteID);
		ri.setUserId("user");
		KeyFabbricatoDTO keyFabbr = new KeyFabbricatoDTO(null, codNazionale, sezione, foglio, particella, data);
		ri.setObj(keyFabbr);
		ri.setProgressivoEs(null);
		ri.setDestFonte("1"); //
		ri.setDestProgressivoEs("1");//--> demografia
		List<Object> listaCivici = serv.getCiviciCorrelatiFromFabbricato(ri);
		System.out.println("ACQUISIZIONE DEI CIVICI CORRELATI - U.I. KEY: " + keyStr );
		if (listaCivici == null)  
			System.out.println("NESSUN CIVICO CORRELATO ");
		else
			System.out.println("Civici correlati: " + listaCivici.size());
		AnagrafeService anaSer = (AnagrafeService)locator.getReference("CT_Service/AnagrafeServiceBean/remote");
		for (Object ele: listaCivici)  {
			SitDCivico civ = (SitDCivico)ele;
			System.out.println("Civico correlato id: " + civ.getId());
		}
		for (Object ele: listaCivici)  {
			SitDCivico civ = (SitDCivico)ele;
			RicercaIndirizzoAnagrafeDTO ra = new RicercaIndirizzoAnagrafeDTO();
			ra.setEnteId(enteID);
			ra.setUserId("user");
			ra.setSitDCivicoId(civ.getId());
			IndirizzoAnagrafeDTO ind = anaSer.getIndirizzo(ra);
			stampaOgg(ind);
			List<ComponenteFamigliaDTO> listaComp = anaSer.getResidentiAlCivico(ra);
			stampa(listaComp);
			//per ogni componente acquisire il soggetto in catasto 
		}
			
	}
	private void testPregeo(ServiceLocator locator, String enteID, String foglio, String particella) {
		if (foglio==null || foglio.equals(""))
			foglio = "4030";
		if (particella==null || particella.equals(""))
			particella = "20";
		PregeoService serv = (PregeoService)locator.getReference("CT_Service/PregeoServiceBean/remote");
		RicercaPregeoDTO rp = new RicercaPregeoDTO();
		rp.setEnteId(enteID);
		rp.setFoglio(foglio);
		rp.setParticella(particella);
		List<PregeoInfo> lista = serv.getDatiPregeo(rp);
		stampa(lista);
		
	}
		
	private void immobiliPossTarsu (ServiceLocator locator, String enteID) {
		String dtRifAStr="31/12/2011";
		String dtRifDaStr="01/01/2007";
		BigDecimal codSogg = new BigDecimal(39381);
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date dtRifA=new Date();
		Date dtRifDa=new Date();
		try {
			dtRifA= df.parse(dtRifAStr);
			dtRifDa= df.parse(dtRifDaStr);
		}catch(java.text.ParseException pe){}
		
		RicercaSoggettoCatDTO rs = new RicercaSoggettoCatDTO();
		rs.setEnteId(enteID);
		rs.setIdSogg(codSogg);
		rs.setDtRifDa(dtRifDa);
		rs.setDtRifA(dtRifA);
		rs.setDtRifDa(dtRifDa);
		rs.setDtRifA(dtRifA);
		rs.setDtVal(null);
		CatastoCarContribService servCatCC = (CatastoCarContribService)locator.getReference("Servizio_CarContrib/CatastoCarContribServiceBean/remote");
		List<SitPatrimImmobileDTO> listaImm = servCatCC.getImmobiliPosseduti(rs, "TARSU");
		if (listaImm != null){
			System.out.println("IMMOBILI TROVATI");
			stampa(listaImm);
		}	
	}
	
	private void stampa(List listaOggetti ) {
		if (listaOggetti==null){
			System.out.println("LISTA VUOTA");
			return;
		}
		System.out.println("NUMERO ELEMENTI: " + listaOggetti.size());			
		for (int i =0; i <listaOggetti.size(); i++) {
			Object ele = listaOggetti.get(i);
			stampaOgg(ele);
		}
	}
	private void stampa(Set listaOggetti ) {
		if (listaOggetti==null){
			System.out.println("LISTA VUOTA");
			return;
		}
		System.out.println("NUMERO ELEMENTI: " + listaOggetti.size());  	
		for (Object ele : listaOggetti) {
			stampaOgg(ele);
		}
	}
	
	private void stampaOgg(Object ele){
		if (ele == null) return;
		boolean ok =false;
		
		if (ele instanceof DatiIciDTO ) {
			DatiIciDTO ogg = (DatiIciDTO) ele;
			System.out.println("***  RIGA DATI ICI **** ");
			System.out.println("*** F/P/S: " + ogg.getFoglio() + "/" +ogg.getParticella() + "/" + ogg.getSub() );  
			System.out.println("    Denuncia: " + ogg.getNumeroDenuncia()+ "-" +ogg.getAnnoDenuncia()+ ". ANNO-RIF: " + ogg.getAnnoRif() + " INDIRIZZO: " + ogg.getIndirizzo().getDesVia() + ", "  + ogg.getIndirizzo().getCivico());
			//System.out.println("CONFRONTI CATASTO (ANNO RIF.CONFRONTI: " + ogg.getAnnoRifConfr()+ ")  CLASSE:" + ogg.getClasseDaCatasto() + "; CATEGORIA: " + ogg.getCategoriaDaCatasto() + ": TIPOLOGIA: " +ogg.getTipologiaCatastale() + ". DATA INIZIO/FINE: " + ogg.getDtFinPossDaCatasto() + " / " + ogg.getDtFinPossDaCatasto() );
			String stampaInd="INDIRIZZI DA CATASTO: " ; 
			for (IndirizzoIciTarsuDTO ind: ogg.getListaIndirizziDaCatasto()) {
				stampaInd+=ind.getDesVia() + ", " + ind.getCivico() ;
			}
			System.out.println(stampaInd);
			ok=true;
		}
		if (ele instanceof OggettoIciDTO ) {
			OggettoIciDTO oggG = (OggettoIciDTO) ele;
			SitTIciOggetto ogg = oggG.getOggettoIci();
			System.out.println("*** F/P/S: " + ogg.getFoglio() + "/" +ogg.getNumero()+ "/" + ogg.getSub() );  
			System.out.println("    Denuncia: " + ogg.getNumDen()+ "-" +ogg.getYeaDen()+ ". ANNO-RIF: " + ogg.getYeaRif() );
			ok=true;
		}
		if (ele instanceof VTIciSoggAll ) {
			VTIciSoggAll ogg = (VTIciSoggAll) ele;
			System.out.println("*** SOGGETTO COINVOLTO: " + ogg.getCogDenom() + "/" +ogg.getNome()  );
			ok=true;
		}
		if (ele instanceof Sitiuiu  ) {
			Sitiuiu ogg = (Sitiuiu) ele;
			System.out.println("*** UI. F/P/S-VALIDITA': " + ogg.getId().getFoglio()+ "/" +ogg.getId().getParticella() + "/" + ogg.getId().getUnimm() + "; VALIDITA': " + ogg.getDataInizioVal() + "-" + ogg.getId().getDataFineVal() );  
			System.out.println("***--> Categoria: " + ogg.getCategoria() + ".Classe: " +ogg.getClasse() + ".Rendita: " + ogg.getRendita() );
			ok=true;
		}
		if (ele instanceof SiticonduzImmAll  ) {
			SiticonduzImmAll ogg = (SiticonduzImmAll) ele;
			System.out.println("*** UI. F/P/S: " + ogg.getId().getFoglio()+ "/" +ogg.getId().getParticella() + "/" + ogg.getId().getUnimm()  );  
			System.out.println("***--> Perc. Poss:: " + ogg.getPercPoss() );
			ok=true;
		}
		
		if (ele instanceof IndirizzoDTO ) {
			IndirizzoDTO ogg = (IndirizzoDTO) ele;
			System.out.println("*** INDIRIZZO CATASTALE: " + ogg.getStrada() + ", " +ogg.getNumCivico()  );  
			ok=true;
		}
		if (ele instanceof TerrenoPerSoggDTO ) {
			TerrenoPerSoggDTO ogg = (TerrenoPerSoggDTO) ele;
			System.out.println("*** TERRENO-F/P/S: " + ogg.getFoglio()+ "/" +ogg.getParticella() + "/" + ogg.getSubalterno() );   
			System.out.println("*** -->Superficie: " + ogg.getSuperficie()+ "; Titolo:" +ogg.getTitolo() + "; Inizio/Fine Poss.:" + ogg.getDtIniPos() + "/" + ogg.getDtFinPos());
			ok=true;
		}
		if (ele instanceof SitDPersona ) {
			SitDPersona ogg = (SitDPersona) ele;
			System.out.println("*** DATI PERSONA ANAGRAFE - ID" + ogg.getId()+ "/ COGNOME-NOME-CF: " + ogg.getCognome() + " " + ogg.getNome()+ "-" + ogg.getCodfisc() + "; POS. ANAG: " + ogg.getPosizAna() + "; DATA INIZIO/FINE VAL: " + ogg.getDtInizioVal() + "/" +ogg.getDtFineVal() + "; DATA INIZIO RES: : " + ogg.getDataInizioResidenza() );
			ok=true;
		}
		if (ele instanceof ComponenteFamigliaDTO ) {
			ComponenteFamigliaDTO ogg = (ComponenteFamigliaDTO) ele;
			System.out.println("*** COMPONENTE FAMIGLIA - DATIANAG: " + ogg.getDatiAnagComponente() + " ID: " + ogg.getPersona().getId()+ "/ COGNOME-NOME-CF: " + ogg.getPersona().getCognome() + " " +ogg.getPersona().getNome()+ "-" + ogg.getPersona().getCodfisc() + "; RELAZ. PAR: " + ogg.getRelazPar() + "; POS. ANAG: " + ogg.getPersona().getPosizAna() + "; DATA INIZIO/FINE VAL: " + ogg.getPersona().getDtInizioVal() + "/" +ogg.getPersona().getDtFineVal() + "; DATA INIZIO RES: : " + ogg.getPersona().getDataInizioResidenza() );
			ok=true;
		}
		if (ele instanceof IndirizzoAnagrafeDTO ) {
			IndirizzoAnagrafeDTO ogg = (IndirizzoAnagrafeDTO) ele;
			System.out.println("***INDIRIZZO ANAGRAFE " + ogg.getSedimeVia() + " " + ogg.getDesVia()+ " N." + ogg.getCivico() + " " + ogg.getCivicoLiv2() + " " + ogg.getCivicoLiv3());
			ok=true;
		}
		if (ele instanceof SoggettoDTO ) {
			SoggettoDTO ogg = (SoggettoDTO) ele;
			if (ogg.getTipoSogg().equals("F"))
				System.out.println("***SOGGETTO: " + ogg.getCognome() + " " + ogg.getNome()+ " " + ogg.getDtNas() + " " + ogg.getDesComNas());
			else
				System.out.println("***SOGGETTO: " + ogg.getDenom()+ " " + ogg.getParIva());
			ok=true;
		}
		if (ele instanceof it.webred.ct.data.access.basic.catasto.dto.SoggettoDTO ) {
			it.webred.ct.data.access.basic.catasto.dto.SoggettoDTO ogg = (it.webred.ct.data.access.basic.catasto.dto.SoggettoDTO) ele;
			System.out.println("***SOGGETTO COGNOME: " + ogg.getDenominazioneSoggetto() + " , NOME: " + ogg.getNomeSoggetto()+ " TIPO: " + ogg.getTipo()+ ";INIZIO/FINEPOSS:  " + ogg.getDataInizioPossStr() + "-" + ogg.getDataFinePossStr()+ "; QUOTA: " + ogg.getQuota());
			System.out.println("***SOGGETTO DENOMINAZIONE: " + ogg.getDenominazioneSoggetto() +", PIVA: " + ogg.getPivaSoggetto());
			System.out.println("***INIZIO-FINE POSS:  " + ogg.getDataInizioPossStr() + "-" + ogg.getDataFinePossStr()+ "; QUOTA: " + ogg.getQuota());
			ok=true;
		}
		if (ele instanceof VersamentoDTO ) {
			VersamentoDTO ogg = (VersamentoDTO) ele;
			System.out.println("***DATI VERSAMENTO **** ANNO: " + ogg.getAnnoRif()+ "; IMP.VER: " + ogg.getImpTotVer()+ " IMP.ISCR.:" + ogg.getImpTotRuolo() + " IMP.RISCOSSO:" + ogg.getImpTotRiscosso());
			
		}
		if (ele instanceof SitPatrimImmobileDTO ) {
			SitPatrimImmobileDTO ogg = (SitPatrimImmobileDTO) ele;
			System.out.println("***DATI IMMOBILE  **** F,P,S: " + ogg.getDatiTitImmobile().getId().getFoglio()+ "," + ogg.getDatiTitImmobile().getId().getParticella()+ "," + ogg.getDatiTitImmobile().getId().getUnimm()+ " INIZIO POSSESSO: " + ogg.getDatiTitImmobile().getDataInizio());
			ok=true;	
		}
		
		if (ele instanceof SitPatrimTerrenoDTO ) {
			SitPatrimTerrenoDTO ogg = (SitPatrimTerrenoDTO) ele;
			System.out.println("***DATI TERRENO  **** F,P: " + ogg.getDatiTitTerreno().getFoglio()+ "," + ogg.getDatiTitTerreno().getParticella()+ " INIZIO POSSESSO: " + ogg.getDatiTitTerreno().getDtIniPos());
			ok=true;	
		}
		if (ele instanceof DatiTarsuDTO ) {
			DatiTarsuDTO ogg = (DatiTarsuDTO) ele;
			System.out.println("***  RIGA DATI TARSU **** ");
			System.out.println("*** F/P/S: " + ogg.getFoglio() + "/" +ogg.getParticella() + "/" + ogg.getSub() );  
			System.out.println("CLASSE: " + ogg.getDesClasse() + "; TIPO: " + ogg.getDesTipOgg() + ";  SUP.TOT: " +ogg.getSupTot()+ "; DATA INIZIO/FINE: " + ogg.getDtIniPos() + " / " + ogg.getDtFinPos() + "; INDIRIZZO: " +ogg.getIndirizzo().getDesVia() + ", "  + ogg.getIndirizzo().getCivico());
		
			System.out.println("CONFRONTI CATASTO - CLASSE:" + ogg.getClasseDaCatasto() + "; TIPO: " + ogg.getDesTipOggDaCatasto() + ": SUP.TOT: " +ogg.getSuperficieDaCatasto()+ ". DATA INIZIO/FINE: " + ogg.getDtFinPossDaCatasto() + " / " + ogg.getDtFinPossDaCatasto() );
			String stampaInd="INDIRIZZI DA CATASTO: " ; 
			if (ogg.getListaIndirizziDaCatasto() != null ) {
				for (IndirizzoIciTarsuDTO ind: ogg.getListaIndirizziDaCatasto()) {
					stampaInd+=ind.getDesVia() + ", " + ind.getCivico() ;
				}
				System.out.println(stampaInd);
				stampaInd="INDIRIZZO DA ANAGRAFE: " + ogg.getIndirizzoDaAnagrafe(); 	
			}
			
			ok=true;
		}
		if (ele instanceof LocazioniDTO ) {
			LocazioniDTO ogg = (LocazioniDTO) ele;
			System.out.println("***DATI OGGETTO LOCAZIONE  ****CHIAVE: " + ogg.getDatiOggLocazione().getUfficio() + "-" + ogg.getDatiOggLocazione().getAnno()+"-" +ogg.getDatiOggLocazione().getSerie()+"-" +ogg.getDatiOggLocazione().getNumero() +  "; INIZIO: " + ogg.getDatiOggLocazione().getIndirizzo()+"; INIZIO: " + ogg.getDatiOggLocazione().getDataInizio() + "; FINE: " + ogg.getDatiOggLocazione().getDataFine() + "; STIPULA"  + ogg.getDatiOggLocazione().getDataStipula());  
			ok=true;
		}
		if (ele instanceof SitTTarSogg ) {
			SitTTarSogg ogg = (SitTTarSogg) ele;
			if (ogg.getTipSogg().equals("F"))
				System.out.println("***DATI SOGGETTO TARSU PF ****: " + ogg.getCogDenom()+ " " + ogg.getNome()+"-" +ogg.getCodFisc()+"-" +ogg.getDtNsc().toString() +  "; COMUNE NASCITA: " + ogg.getDesComNsc()+"; INDIRIZZO RESID: " + ogg.getDesIndRes() + "; COMUNE RESID." + ogg.getDesComRes());
			else
				System.out.println("***DATI SOGGETTO TARSU PG ****: " + ogg.getCogDenom()+ " " + ogg.getPartIva());  
			ok=true;
		}
		if (ele instanceof SitTIciSogg ) {
			SitTIciSogg ogg = (SitTIciSogg) ele;
			if (ogg.getTipSogg().equals("F"))
				System.out.println("***DATI SOGGETTO ICI PF ****: " + ogg.getCogDenom()+ " " + ogg.getNome()+"-" +ogg.getCodFisc()+"-" +ogg.getDtNsc().toString() +  "; COMUNE NASCITA: " + ogg.getDesComNsc()+"; INDIRIZZO RESID: " + ogg.getDesIndRes() + "; COMUNE RESID." + ogg.getDesComRes());
			else
				System.out.println("***DATI SOGGETTO ICI PG ****: " + ogg.getCogDenom()+ " " + ogg.getPartIva());  
			ok=true;
		}
		if (ele instanceof SitTCosapContrib ) {
			SitTCosapContrib ogg = (SitTCosapContrib) ele;
			System.out.println("***DATI SOGGETTO COSAP  ****: " + ogg.getCogDenom()+ " " + ogg.getNome()+"-" +ogg.getCodiceFiscale()+"; DATA NASCITA: " +ogg.getDtNascita().toString() +  "; COMUNE NASCITA: " + ogg.getDescComuneNascita()+"; INDIRIZZO RESID: " + ogg.getIndirizzo() + " " + ogg.getCivico() + "; COMUNE RESID." + ogg.getDescComuneResidenza()   );
			ok=true;
		}
		if (ele instanceof SitTCosapTassa ) {
			SitTCosapTassa ogg = (SitTCosapTassa) ele;
			System.out.println("***DATI OGGETTO COSAP  ****: " + ogg.getDescrizione()+ " " + ogg.getFoglio()+"/" + ogg.getParticella()+"/" + ogg.getSubalterno()+"; INDIRIZZO:"+ ogg.getIndirizzo()+", " + ogg.getCivico()+ "; QTA: " +ogg.getQuantita() +  "; U.M.: " +ogg.getUnitaMisuraQuantita()+ "; TAR.X UNITA': " + ogg.getTariffaPerUnita());
			ok=true;
		}
		if (ele instanceof VAnagrafica ) {
			VAnagrafica ogg = (VAnagrafica) ele;
			System.out.println("***ANAGRAFICA DEBITORE CNC  ****: DENOM:" + ogg.getDenominazione()+ "; COGNOME: " + ogg.getCognome()+ "; NOME" + ogg.getNome()+"; DATA NASCITA: " + ogg.getDtNascita()+"; COMUNE NASCITA: : " + ogg.getCodBelfioreNascita()+"; INDIRIZZO:"+ ogg.getIndirizzoDeb()+"  " + ogg.getCodBelfioreDeb()+ "; CF: " +ogg.getCodFiscale() );  
			ok=true;
		}
		if (ele instanceof DatiImportiCncDTO ) {
			DatiImportiCncDTO ogg = (DatiImportiCncDTO) ele;
			System.out.println("*** dati CNC  ****: ANNO:" + ogg.getAnnoRif()+ "VERSATO: " + ogg.getImpTotVer() + "; TRIBUTO: " + ogg.getDesTipoTributo()+ "; IMPORTO RUOLO: " + ogg.getImpTotRuolo()+"; RISCOSSO: " + ogg.getImpTotRiscosso());  
			ok=true;
		}
		if (ele instanceof RedDatiAnagrafici ) {
			RedDatiAnagrafici ogg = (RedDatiAnagrafici) ele;
			System.out.println("*** SOGGETTO ****: " + ogg.getCognome()+ " " + ogg.getNome()+"-" +ogg.getCodiceFiscaleDic()+"-" +ogg.getDataNascita()+  "; COMUNE NASCITA: " + ogg.getComuneNascita()+ "-" + ogg.getDesComuneNascita() +"; ANNO IMPOSTA: " + ogg.getAnnoImposta());  
			ok=true;
		}
		if (ele instanceof RedDomicilioFiscale ) {
			RedDomicilioFiscale ogg = (RedDomicilioFiscale) ele;
			System.out.println("*** DOMICILIO ****: " + ogg.getIndirizzoAttuale()+ "; CODICE CAT DOMICILIO FISCALE DIC: " + ogg.getCodiceCatDomFiscaleDic()+";  CODICE CAT DOMICILIO FISCALE ATTUALE: " + ogg.getCodiceCatDomFiscaleAttuale());   
			ok=true;
		}
		if (ele instanceof RedditiDicDTO ) {
			RedditiDicDTO ogg = (RedditiDicDTO) ele;
			System.out.println("*** REDDITO ****: QUADRO " + ogg.getId().getCodiceQuadro()+ "-" + ogg.getDesQuadro() + "; IMPORTO: " + ogg.getValoreContabile()   );
			ok=true;
		}
		if (ele instanceof DecodTipDoc ) {
			DecodTipDoc ogg = (DecodTipDoc) ele;
			System.out.println("*** TIPO DOCUMENTO: " + ogg.getCodTipDoc() + "-" + ogg.getDesTipDoc());
			ok=true;
		}
		if (ele instanceof ParticellaKeyDTO ) {
			ParticellaKeyDTO ogg = (ParticellaKeyDTO) ele;
			System.out.println("*** SEZIONE/FOGLIO/PARTICELLA: " + ogg.getIdSezc() + "/" + ogg.getFoglio() + "/" + ogg.getParticella());
			ok=true;
		}
		if (ele instanceof Sititrkc ) {
			Sititrkc  ogg = (Sititrkc ) ele;
			System.out.println("*** TERRENO: " + ogg.getId().getFoglio() + "/" + ogg.getId().getParticella() +  "; CLASSE: " + ogg.getClasseTerreno() + " SUPERFICIE:" + ogg.getAreaPart()+ ";FINE VALIDITA' " + ogg.getId().getDataFine());
			
		}
		if (ele instanceof MuiFabbricatiIdentifica ) {
			MuiFabbricatiIdentifica  ogg = (MuiFabbricatiIdentifica ) ele;
			System.out.println("*** MUI FABBR: " + ogg.getFoglio() + "/" + ogg.getNumero() +  "; SUB: " + ogg.getSubalterno()); 
			ok=true;
		}
		if (ele instanceof MuiNotaTras ) {
			MuiNotaTras  ogg = (MuiNotaTras ) ele;
			System.out.println("*** MUI NOTA: DATA_REG_IN_ATTI:" + ogg.getDataRegInAtti() + "; N.REP:" + ogg.getNumeroRepertorio()+  "; ROGITO EFFETTUATO DA DA: " + ogg.getCognomeNomeRog() ); 
			ok=true;	
		}
		if (ele instanceof VTIciCiviciAll ) {
			VTIciCiviciAll  ogg = (VTIciCiviciAll ) ele;
			System.out.println("*** CIVICO ICI. ID_VIA/NUM_CIV/ESP_CIV : " + ogg.getId()+ "/" + ogg.getNumCiv()+  "/" + ogg.getEspCiv()); 
			ok=true;
		}
		if (ele instanceof VTTarCiviciAll ) {
			VTTarCiviciAll  ogg = (VTTarCiviciAll ) ele;
			System.out.println("*** CIVICO TARSU. ID_VIA/NUM_CIV/ESP_CIV : " + ogg.getId()+ "/" + ogg.getNumCiv()+  "/" + ogg.getEspCiv()); 
			ok=true;
		}
		if (ele instanceof DatiDocfaDTO ) {
			DatiDocfaDTO ogg = (DatiDocfaDTO) ele;
			System.out.println("*** PROTOCOLLO REG: " + ogg.getProtocolloReg() + "; DATA REG.:" + ogg.getDataRegistrazione() + "; DATA OPER.:" + ogg.getDesTipoOperazione() +  "; INDIRIZZO: " + ogg.getIndirizzoUiuCompleto()); 
			ok=true;	
		}
		if (ele instanceof ConcessioneDTO ) {
			ConcessioneDTO ogg = (ConcessioneDTO) ele;
			System.out.println("*** CONCESSIONE: " + ogg.getConcNumero() + "; OGG: " + ogg.getOggetto() + "; SOGGETTI; " + ogg.getListaSoggettiHtml() + "; UI:" + ogg.getStringaImmobili()); 
			ok=true;
		}
		if (ele instanceof LocazioniA ) {
			LocazioniA ogg = (LocazioniA) ele;
			System.out.println("*** LOCAZIONI_A (OGGETTO): " + ogg.getUfficio() + "/" + ogg.getAnno() + "/" + ogg.getSerie() + "/" + ogg.getNumero() + "; INDIRIZZO: " + ogg.getIndirizzo()+ "; DATA STIPULA; " + ogg.getDataStipula() + "; CANONE:" + ogg.getImportoCanone()); 
			ok=true;
		}
		if (ele instanceof LocazioniB ) {
			LocazioniB ogg = (LocazioniB) ele;
			System.out.println("*** LOCAZIONI_B (SOGGETTO): " + ogg.getUfficio() + "/" + ogg.getAnno() + "/" + ogg.getSerie() + "/" + ogg.getNumero() + "; CF: " + ogg.getCodicefiscale()+ "; INDIRIZZO RESID.; " + ogg.getIndirizzoResidenza() + ", " + ogg.getCivicoResidenza()); 
			ok=true;
		}
		if (ele instanceof AmKeyValueDTO ) {
			AmKeyValueDTO ogg = (AmKeyValueDTO) ele;
			System.out.println("*** PARAMETRO: " + ogg.getDescrizione() + " VALORE:" + ogg.getAmKeyValueExt().getValueConf() ); 
			ok=true;
		}
		if (ele instanceof PregeoInfo ) {
			PregeoInfo ogg = (PregeoInfo) ele;
			System.out.println("*** CODICE PREGEO: " + ogg.getCodicePregeo() + " DATA:" + ogg.getDataPregeo() + " NOME PDF: " + ogg.getNomeFilePdf() + " DENOMINAZIONE: " + ogg.getDenominazione() + " TECNICO: "  + ogg. getTipoTecnico() + " " + ogg.getTecnico() ); 
			ok=true;
		}
		if(!ok)
			System.out.println("ELEMENTO DI TIPO: " + ele.getClass().getName());
	}	
	
	
}
