package it.escsolution.escwebgis.redditiAnnuali.servlet;

import it.escsolution.escwebgis.anagrafe.logic.AnagrafeDwhLogic;
import it.escsolution.escwebgis.common.DiogeneException;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.redditiAnnuali.bean.DecoRedditiDichiarati;
import it.escsolution.escwebgis.redditiAnnuali.bean.RedditiAnnuali;
import it.escsolution.escwebgis.redditiAnnuali.bean.RedditiAnnualiFinder;
import it.escsolution.escwebgis.redditiAnnuali.logic.RedditiAnnualiLogic;
import it.webred.DecodificaPermessi;
import it.webred.indice.OggettoIndice;
import it.webred.cet.permission.CeTUser;
import it.webred.cet.permission.GestionePermessi;
import it.webred.ct.data.access.basic.redditianalitici.dto.RigaQuadroRbDTO;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.iterators.EntrySetMapIterator;

public class RedditiAnnualiServlet extends EscServlet implements EscService {

	private String recordScelto;
	public static final String NOMEFINDER = "FINDER52";
	private RedditiAnnualiFinder finder = null;
	PulsantiNavigazione nav;
	
	public static String AUTORIZZAZIONE_NEGATA = "AUTORIZZAZIONE NEGATA";
	
	private String localDataSource = "jdbc/Diogene_DS";
	
	public void EseguiServizio(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		super.EseguiServizio(request,response);
		
		String ext= request.getParameter("IND_EXT");
		
		if (ext!= null && ext.equals("1"))
			_EseguiServizioExt(request,response);
		else
			_EseguiServizio(request,response);
		
	}

	public void _EseguiServizio(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		super.EseguiServizio(request,response);
		try
		{
			switch (st){
				 case 1:
					 // carico la form di ricerca
					 pulireSessione(request);
					 mCaricareFormRicerca(request,response);
					 break;
				 case 2:
					 // vengo dalla pagina di ricerca --> predispongo la lista e la passo alla pagina di lista
				     //pulireSessione(request);
					 mCaricareLista(request,response);
					 break;
				 case 3:
					 // ho cliccato su un elemento --> visualizzo il dettaglio
					 mCaricareDettaglio(request,response,3);
					// this.leggiCrossLink(request);
					 break;
				 case 4:
					 //apertura di una popup
					 mAprirePopup(request,response);
					 break;
				 case 33:
					 // ho cliccato su un elemento --> visualizzo il dettaglio
					 mCaricareDettaglio(request,response,33);
					 //this.leggiCrossLink(request);
					 break;
			}
		}
		catch(it.escsolution.escwebgis.common.DiogeneException de)
		{
			throw de;
		}
		catch(Exception ex)
		{
			log.error(ex.getMessage(),ex);
		}
	}

	public void _EseguiServizioExt(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		super.EseguiServizio(request,response);
		try
		{
			
				switch (st){
					case 101:
						mCaricareDettaglio(request,response,101);			
						break;
					case 102:
						mCaricareDettaglio(request,response,102);							 
						break;
					case 103:
						mCaricareDettaglio(request,response,103);								 
						break;
					case 104:
						//mCaricareLista(request,response);						 
						break;
				}
				
		}
		catch(it.escsolution.escwebgis.common.DiogeneException de)
		{
			throw de;
		}
		catch(Exception ex)
		{
			throw new it.escsolution.escwebgis.common.DiogeneException(ex);
		}
	}
	
	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//caricare dati che servono nella pagina di ricerca
		HttpSession sessione = request.getSession();

		Vector listaElementiFiltro = new Vector();

		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=", "uguale"));
		operatoriStringaRid.add(new EscOperatoreFiltro("like", "contiene"));

		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=", "uguale"));
		
		Vector operatoriNumericiRid = new Vector();
		operatoriNumericiRid.add(new EscOperatoreFiltro("=","="));

		// costruisce il vettore di elementi per la pagina di ricerca
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		
		//anno					
		elementoFiltro.setLabel("Anno");
		elementoFiltro.setAttributeName("ANNO_IMPOSTA");
		elementoFiltro.setTipo("N");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriNumericiRid);
		elementoFiltro.setCampoFiltrato("RED_DAT_ANA.ANNO_IMPOSTA");
		listaElementiFiltro.add(elementoFiltro);

		//cf
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Codice Fiscale/P.Iva");
		elementoFiltro.setAttributeName("CODICE_FISCALE_DIC");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("RED_DAT_ANA.CODICE_FISCALE_DIC");
		listaElementiFiltro.add(elementoFiltro);

		//cognome
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cognome");
		elementoFiltro.setAttributeName("COGNOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("RED_DAT_ANA.COGNOME");
		listaElementiFiltro.add(elementoFiltro);

		//nome
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Nome");
		elementoFiltro.setAttributeName("NOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("RED_DAT_ANA.NOME");
		listaElementiFiltro.add(elementoFiltro);
		
		//denominazione
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Denominazione");
		elementoFiltro.setAttributeName("DENOMINAZIONE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("RED_DAT_ANA.DENOMINAZIONE");
		listaElementiFiltro.add(elementoFiltro);


		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		//chiamare la pagina di ricerca
		nav = new PulsantiNavigazione();
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"redditiAnnuali/redAnnualiFrame.jsp", nav);
	}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null)
		{
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new RedditiAnnualiFinder().getClass())
			{
				finder = (RedditiAnnualiFinder)sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (RedditiAnnualiFinder)gestireMultiPagina(finder,request);

		RedditiAnnualiLogic logic = new RedditiAnnualiLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareLista(vettoreRicerca,finder);

	    Vector vct_lista= (Vector)ht.get(RedditiAnnualiLogic.LISTA);
		finder = (RedditiAnnualiFinder)ht.get(RedditiAnnualiLogic.FINDER);
		sessione.setAttribute(RedditiAnnualiLogic.LISTA,vct_lista);
		sessione.setAttribute(NOMEFINDER,finder);

		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
		{
			nav.chiamataEsternaLista();
			nav.setExt("1");
			nav.setPrimo(true);
		}
		else
			nav.chiamataInternaLista();

		this.chiamaPagina(request,response,"redditiAnnuali/redAnnualiFrame.jsp",nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response, int tipo)throws Exception
	{
		String azione = "";
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
		 
		RedditiAnnualiFinder finder = null;
		if (sessione.getAttribute(NOMEFINDER) !=null)
		{
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new RedditiAnnualiFinder().getClass())
			{
				finder = (RedditiAnnualiFinder)sessione.getAttribute(NOMEFINDER);
			}
		}

		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request,response,azione,finder,sessione,RedditiAnnualiLogic.LISTA,(Vector)sessione.getAttribute(RedditiAnnualiLogic.LISTA),NOMEFINDER);

		if (azione.equals(""))
		{
			oggettoSel="";recordScelto="";
			sessione.removeAttribute("BACK_JS_COUNT");
			sessione.removeAttribute("BACK_RECORD_ENABLE");

			if (request.getParameter("OGGETTO_SEL")!= null)
			{
				oggettoSel = request.getParameter("OGGETTO_SEL");
			}
			if (request.getParameter("RECORD_SEL")!= null)
			{
				recordScelto = request.getParameter("RECORD_SEL");
				if (finder!=null)
					finder.setRecordAttuale(new Long(recordScelto).longValue());
			}
		}

		//carica il dettaglio
		RedditiAnnualiLogic logic = new RedditiAnnualiLogic(this.getEnvUtente(request));
		
		Hashtable ht =null;
		
		if (tipo == 101 || tipo == 102 || tipo == 103  )
			 ht = logic.mCaricareDettaglioFromSoggetto(oggettoSel,request);
		else
			ht = logic.mCaricareDettaglio(oggettoSel,request);
		
				
		LinkedHashMap<String, DecoRedditiDichiarati> redditi = ((RedditiAnnuali)ht.get(RedditiAnnualiLogic.REDDITI)).getRedditi();
		profilaVisualizzazioneRedditi(request, redditi );
		
		RedditiAnnuali red = (RedditiAnnuali)ht.get(RedditiAnnualiLogic.REDDITI);
		Vector v = (Vector)ht.get(RedditiAnnualiLogic.ALTRIREDDITI);
		List<RigaQuadroRbDTO> quadroRB = (List<RigaQuadroRbDTO>)ht.get(RedditiAnnualiLogic.QUADRO_RB);
		
		sessione.setAttribute(RedditiAnnualiLogic.REDDITI,red);
		sessione.setAttribute(RedditiAnnualiLogic.ALTRIREDDITI,v);
		sessione.setAttribute(RedditiAnnualiLogic.QUADRO_RB, quadroRB );

		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		
		
		// Aggiungo i valori per l'indice di correlazione
		Vector<OggettoIndice> sOggettiInd = new Vector<OggettoIndice>();
		
		OggettoIndice oi = new OggettoIndice();
		
		oi.setIdOrig(red.getIdeTelematico()+ "|"+ red.getCodiceFiscaleDic());
		oi.setFonte("11");
		oi.setProgr("1");
		
		if ((red.getCognome()!= null && !"".equals(red.getCognome())) || (red.getNome()!= null && !"".equals(red.getNome())))
			oi.setDescrizione(red.getCognome() + " " + red.getNome());
		else if (red.getDenominazione()!= null && !red.getDenominazione().equals(""))
			oi.setDescrizione(red.getDenominazione());
		
		sOggettiInd.add(oi);
		sessione.setAttribute("indice_soggetti", sOggettiInd);
		
		// Carico gli indici
		OggettoIndice oi1 = new OggettoIndice();	
		oi1.setIdOrig(red.getIdeTelematico()+ "|"+ red.getCodiceFiscaleDic());
		oi1.setFonte("11");
		oi1.setProgr("1");
		oi1.setDescrizione(red.getIndirizzoAttuale());
		
		Vector<OggettoIndice> sCivInd = new Vector<OggettoIndice>();
		sCivInd.add(oi1);
		sessione.setAttribute("indice_civici", sCivInd);
		
		OggettoIndice oi2 = new OggettoIndice();	
		oi2.setIdOrig(red.getIdeTelematico()+ "|"+ red.getCodiceFiscaleDic());
		oi2.setFonte("11");
		oi2.setProgr("1");
		oi2.setDescrizione(red.getIndirizzoAttuale());
		
		Vector<OggettoIndice> viaInd = new Vector<OggettoIndice>();
		viaInd.add(oi2);
		sessione.setAttribute("indice_vie", viaInd);
		
				
		if (request.getParameter("ME_POPUP") != null && new Boolean(request.getParameter("ME_POPUP")).booleanValue()) {
			this.chiamaPagina(request,response,"redditiAnnuali/redAnnualiDetail.jsp", nav);
		} else {
			this.chiamaPagina(request,response,"redditiAnnuali/redAnnualiFrame.jsp", nav);
		}
		
	}
	
	
	
	/**
	 * Metodo che applica il permesso di visualizzazione dei redditi da lavoro dipendente
	 */
	private void profilaVisualizzazioneRedditi(HttpServletRequest request, LinkedHashMap<String, DecoRedditiDichiarati> redditi) {
		
		if (redditi==null)
			return;
		
		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
   		if (!GestionePermessi.autorizzato(eu.getUtente(), eu.getNomeIstanza(), DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, DecodificaPermessi.PERMESSO_VISUALIZZA_REDDITO_LAVORO,true)) {
   			Set<Entry<String, DecoRedditiDichiarati>> s = redditi.entrySet();
   			MapIterator it = new EntrySetMapIterator(redditi);
   			while (it.hasNext()) {
   				  String key = (String) it.next();
   				  DecoRedditiDichiarati value = (DecoRedditiDichiarati) it.getValue();
   				  if(value.getDescrizioneQuadro()!=null) {
   					  // a causa del fatto che ogni anno cambiano i codici , vado sulle descizioni del quadro	
   					  // I REDDITI DA FONDO (FABBRICATI, DOMINICALI E AGRICOLI) VANNO BENE
   					  // GLI ALTRI NO
   					  if (value.getDescrizioneQuadro().toUpperCase().indexOf("FABBRICAT")==-1    
   						  &&	value.getDescrizioneQuadro().toUpperCase().indexOf("AGRICOL")==-1    
   						  &&	value.getDescrizioneQuadro().toUpperCase().indexOf("DOMINICAL")==-1    
   					  ) {
   						  value.setValoreContabile(AUTORIZZAZIONE_NEGATA);
   						  redditi.put(key, value);
   					  }
   					  
   				  }
   			} 
   			
   		}
		
	}
	
	private void mAprirePopup(HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		String nomePopup = request.getParameter("POPUP");
		if (nomePopup.equalsIgnoreCase("ANAGRAFE")) {
			mAprireAnagrafe(request, response);
		} else if (nomePopup.equalsIgnoreCase("FAMIGLIA")) {
			mAprireFamiglia(request, response);
		}
	}
	
	private void mAprireAnagrafe(HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		String codiceFiscaleDic =  request.getParameter("CODICE_FISCALE_DIC");		
		RedditiAnnualiLogic logic = new RedditiAnnualiLogic(this.getEnvUtente(request));
		oggettoSel = logic.leggiChiaveAnagrafe(codiceFiscaleDic);
		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		this.chiamaPagina(request, response, "/AnagrafeDwh?ST=3&POPUP=true&OGGETTO_SEL=" + oggettoSel, nav);
	}
	
	private void mAprireFamiglia(HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		String codiceFiscaleDic = request.getParameter("CODICE_FISCALE_DIC");
		RedditiAnnualiLogic logic = new RedditiAnnualiLogic(this.getEnvUtente(request));
		String codAnagrafe = logic.leggiCodAnagrafe(codiceFiscaleDic);
		int annoImposta = Integer.parseInt(request.getParameter("ANNO_IMPOSTA"));
		
		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date dtDa = sdf.parse("01/01/" + annoImposta);
		Date dtA = sdf.parse("31/12/" + annoImposta);
		String idExt = logic.leggiIdExtAnagrafe(codiceFiscaleDic);
		
		Hashtable ht = new Hashtable();
		new AnagrafeDwhLogic(this.getEnvUtente(request)).setStoricoFamiglia(ht, idExt, dtDa, dtA);
		
		String visStoFamKey = (String)ht.get(AnagrafeDwhLogic.VIS_STORICO_FAM_KEY);
		if (visStoFamKey == null)
			visStoFamKey = AnagrafeDwhLogic.VIS_STORICO_FAM_NO;
		if (visStoFamKey.equals(AnagrafeDwhLogic.VIS_STORICO_FAM_ONLY) || visStoFamKey.equals(AnagrafeDwhLogic.VIS_STORICO_FAM_BOTH)) {
			request.getSession().setAttribute(AnagrafeDwhLogic.VIS_STORICO_FAM_KEY, ht.get(AnagrafeDwhLogic.VIS_STORICO_FAM_KEY));
			request.getSession().setAttribute(AnagrafeDwhLogic.VIS_STORICO_DT_AGG, ht.get(AnagrafeDwhLogic.VIS_STORICO_DT_AGG));
			request.getSession().setAttribute(AnagrafeDwhLogic.VIS_STORICO_LISTA  + "_" + codAnagrafe, ht.get(AnagrafeDwhLogic.VIS_STORICO_LISTA));
			request.getSession().setAttribute(AnagrafeDwhLogic.LBL_STORICO_PERIODO, "Anno di imposta: " + annoImposta);
			
			this.chiamaPagina(request,response,"/anagrafe/popupSoggettoDaAnagrafe.jsp?codAnagrafe=" + codAnagrafe + "&STORICO=true", nav);
		} else {
			request.getSession().setAttribute(AnagrafeDwhLogic.ANAGRAFE_INDIRIZZI_POPUP, logic.caricaFamiglia(codAnagrafe, annoImposta));	
			this.chiamaPagina(request,response,"/anagrafe/popupFamigliaDaAnagrafe.jsp?pos=1&REDDITI=true", nav);
		}		
	}

	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (RedditiAnnualiFinder)finder2;
		RedditiAnnualiLogic logic = new RedditiAnnualiLogic(this.getEnvUtente(request));
		return logic.mCaricareLista(this.vettoreRicerca, finder);
	}


	public EscFinder getNewFinder()
	{
		return new RedditiAnnualiFinder();
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo)
	{
		RedditiAnnuali def = (RedditiAnnuali)listaOggetti.get(recordSuccessivo);
		String key = def.getChiave();
		return key;
	}

	public String getTema()
	{
		return "Redditi";
	}

	public String getTabellaPerCrossLink()
	{
		return "RED_DATI_ANAGRAFICI";
	}
	
	public String getLocalDataSource() {
		return localDataSource;
	}

}




