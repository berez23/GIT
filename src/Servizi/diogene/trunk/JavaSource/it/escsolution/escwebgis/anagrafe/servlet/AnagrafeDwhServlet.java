 /*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.anagrafe.servlet;
import it.escsolution.escwebgis.anagrafe.bean.Anagrafe;
import it.escsolution.escwebgis.anagrafe.bean.AnagrafeDwhFinder;
import it.escsolution.escwebgis.anagrafe.bean.Famiglia;
import it.escsolution.escwebgis.anagrafe.logic.AnagrafeAnagrafeLogic;
import it.escsolution.escwebgis.anagrafe.logic.AnagrafeDwhLogic;
import it.escsolution.escwebgis.anagrafe.logic.AnagrafeFamigliaLogic;
import it.escsolution.escwebgis.catasto.bean.Sesso;
import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.webred.DecodificaPermessi;
import it.webred.GlobalParameters;
import it.webred.cet.permission.GestionePermessi;
import it.webred.indice.OggettoIndice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class AnagrafeDwhServlet extends EscServlet implements EscService {

	private String localDataSource = "jdbc/Diogene_DS";

	private String recordScelto;
	private AnagrafeAnagrafeLogic logic = null;
	//private AnagrafeDwhFinder finder = null;
	public static final String NOMEFINDER = "FINDER45";
	private AnagrafeDwhFinder finder = null;
	PulsantiNavigazione nav;
	
	public void EseguiServizio(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		super.EseguiServizio(request,response);
		
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		String ext= request.getParameter("IND_EXT");
		
		if (ext!= null && ext.equals("1"))
			_EseguiServizioExt(request,response);
		else
			_EseguiServizio(request,response);
		
	}
	
	
	public void _EseguiServizio(
		HttpServletRequest request,HttpServletResponse response)
				throws ServletException, IOException {
				/*
				 * ad ogni uc corrisponde una funzionalit diversa
				 *
				 */
		//super.init(getServletConfig());
		//String aaa = getServletConfig().getServletName();
		super.EseguiServizio(request,response);
		 try{
				if (request.getParameter("listavie") != null)
					mListaVie(request, response, "SIT_D_VIA", null, "DECODE(VIASEDIME, NULL, '', '', '', VIASEDIME || ' ') || DESCRIZIONE", "VIA");
				else{
				 switch (st){
					 case 1:
						 // carico la form di ricerca
						 pulireSessione(request);
						 mCaricareFormRicerca(request,response);
						 break;
					 case 2:
						 // vengo dalla pagina di ricerca --> predispongo la lista e la passo alla pagina di lista
					     //pulireSessione(request);
						 mCaricareLista(request,response, 2);
						 break;
					 case 3:
						 // ho cliccato su un elemento --> visualizzo il dettaglio
						 mCaricareDettaglio(request,response);
						 //this.leggiCrossLink(request);
						 break;
					 case 33:
						 // ho cliccato su un elemento --> visualizzo il dettaglio
						 mCaricareDettaglio(request,response);
						 //this.leggiCrossLink(request);
						 break;
					/*case 4:
						 // ho cliccato su un elemento --> visualizzo il dettaglio
						 mCaricareDettaglio(request,response);
						 this.leggiCrossLink(request);
						 break;
					case 5:
						 // ho cliccato su un elemento --> visualizzo il dettaglio
						 mCaricareLista(request,response,true);
						 break;	*/
					 case ST_ORDER_FILTER:
						 mCaricareLista(request,response, ST_ORDER_FILTER);
						 break;
				 }
			 }
		 }
		 catch(it.escsolution.escwebgis.common.DiogeneException de)
		 {
		 	throw de;
		 }
		catch(Exception ex){
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
						mCaricareDettaglio(request,response);			
						break;
					case 102:
						//mCaricareLista(request,response,102);		
						mCaricareDettaglio(request,response);	
						break;
					case 103:
						//mCaricareLista(request,response,103);	
						mCaricareDettaglio(request,response);	
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

	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception{

				EnvUtente eu = this.getEnvUtente(request);
		
				//caricare dati che servono nella pagina di ricerca
				/** Comune */
				Vector vctComuni = new ComuniLogic(eu).getListaComuniUtente(request.getUserPrincipal().getName());
				/***/
				HttpSession sessione = request.getSession();
				Vector vct_sesso = new Vector();
				//da 1 maschio 2 femmina a M,F
				vct_sesso.add(new Sesso("","Tutti"));
				vct_sesso.add(new Sesso("M","Maschio"));
				vct_sesso.add(new Sesso("F","Femmina"));


				Vector listaElementiFiltro = new Vector();
				Vector operatoriStringa = new Vector();
				operatoriStringa.add(new EscOperatoreFiltro("=","uguale"));
				operatoriStringa.add(new EscOperatoreFiltro("like","contiene"));

				Vector operatoriNumerici = new Vector();
				operatoriNumerici.add(new EscOperatoreFiltro("=","="));
				operatoriNumerici.add(new EscOperatoreFiltro("<>","<>"));
				operatoriNumerici.add(new EscOperatoreFiltro(">",">"));
				operatoriNumerici.add(new EscOperatoreFiltro("<","<"));

				Vector operatoriStringaRid = new Vector();
				operatoriStringaRid.add(new EscOperatoreFiltro("=","uguale"));

				// costruisce il vettore di elementi per la pagina di ricerca

				EscElementoFiltro elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Comune");
				elementoFiltro.setAttributeName("COMUNE");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriStringaRid);
				elementoFiltro.setListaValori(vctComuni);
				elementoFiltro.setCampoFiltrato("CODENT");
				listaElementiFiltro.add(elementoFiltro);
				
				if (GestionePermessi.autorizzato(eu.getUtente(), eu.getNomeIstanza(), DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, "Fonte:DEMOGRAFIA", true)) {
					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Via");
					elementoFiltro.setAttributeName("VIA");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("UPPER((DECODE(PERSONA_CIVICI_V.VIASEDIME, NULL, '', '', '', PERSONA_CIVICI_V.VIASEDIME || ' ') || PERSONA_CIVICI_V.DESCRIZIONE_VIA))");
					elementoFiltro.setExtraHTML("<span><input type=\"button\" class=\"TXTmainLabel\" value=\"Cerca\" onclick=\"javascript:danRequest('"+it.escsolution.escwebgis.common.EscServlet.URL_PATH+"/AnagrafeDwh?listavie='+document.getElementById('VIA').value,'divListaVie')\"/></span><span id=\"divListaVie\" class=\"txtRow\"  ></span> \n");		
					listaElementiFiltro.add(elementoFiltro);
					
					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Numero Civico");
					elementoFiltro.setAttributeName("NUM_CIV");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("PERSONA_CIVICI_V.CIV_COMPOSTO");
					listaElementiFiltro.add(elementoFiltro);
				}

				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Codice Anagrafe");
				elementoFiltro.setAttributeName("ID_ORIG");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriNumerici);
				elementoFiltro.setCampoFiltrato("SIT_D_PERSONA.ID_ORIG");
				listaElementiFiltro.add(elementoFiltro);


				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Codice Fiscale");
				elementoFiltro.setAttributeName("CODICE_FISCALE");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriStringa);
				elementoFiltro.setCampoFiltrato("SIT_D_PERSONA.CODFISC");
				listaElementiFiltro.add(elementoFiltro);

				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Cognome");
				elementoFiltro.setAttributeName("COGNOME");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriStringa);
				elementoFiltro.setCampoFiltrato("SIT_D_PERSONA.COGNOME");
				listaElementiFiltro.add(elementoFiltro);

				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Nome");
				elementoFiltro.setAttributeName("NOME");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriStringa);
				elementoFiltro.setCampoFiltrato("SIT_D_PERSONA.NOME");
				listaElementiFiltro.add(elementoFiltro);

				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Data di nascita");
				elementoFiltro.setAttributeName("DATA_NASCITA");
				elementoFiltro.setTipo("D");
				elementoFiltro.setCampoJS("controllaData");
				elementoFiltro.setListaOperatori(operatoriNumerici);
				elementoFiltro.setCampoFiltrato("SIT_D_PERSONA.DATA_NASCITA");
				listaElementiFiltro.add(elementoFiltro);


				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Sesso");
				elementoFiltro.setAttributeName("SESSO");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriStringaRid);
				elementoFiltro.setListaValori(vct_sesso);
				elementoFiltro.setCampoFiltrato("SIT_D_PERSONA.SESSO");
				listaElementiFiltro.add(elementoFiltro);

				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Codice Famiglia");
				elementoFiltro.setAttributeName("ID_ORIG");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriNumerici);
				elementoFiltro.setCampoFiltrato("LTRIM(SIT_D_FAMIGLIA.ID_ORIG,'0')");
				listaElementiFiltro.add(elementoFiltro);

				sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
				sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
				

				//chiamare la pagina di ricerca
				nav = new PulsantiNavigazione();
				nav.chiamataRicerca();
				this.chiamaPagina(request,response,"anagrafe/anaDwhFrame.jsp", nav);

			}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response, int tipo)throws Exception{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		/*
		 * Questo parametro passato dalla InfoCivico.jsp serve a capire se la richiesta
		 * arriva dalla mappa (=dopo che si +è cliccato sul civico) o dal filtro 
		 * della popolazione
		 */
		String keystr= request.getParameter("KEYSTR");
		String queryForKeyStr= request.getParameter("queryForKeyStr");
		String origine = request.getParameter("origine");
		String lastColor = request.getParameter("LAST_COLOR");
		String lastFamily = request.getParameter("LAST_FAMILY");
		if (origine != null && !origine.trim().equalsIgnoreCase("")){
			if (vettoreRicerca == null)
				vettoreRicerca = new Vector();
			vettoreRicerca.add(origine);
		}
		if (sessione.getAttribute(NOMEFINDER)!= null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new AnagrafeDwhFinder().getClass()){
				finder = (AnagrafeDwhFinder)sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}
		

		finder = (AnagrafeDwhFinder)gestireMultiPagina(finder,request);

		AnagrafeDwhLogic logic = new AnagrafeDwhLogic(this.getEnvUtente(request));
		Hashtable ht = null;
		
		if (tipo==102)
			ht = logic.mCaricareListaAnagrafeVia(request.getParameter("OGGETTO_SEL"), finder);
		else if (tipo == 103)
			ht = logic.mCaricareListaAnagrafeCiv(request.getParameter("OGGETTO_SEL"),finder);
		else {
			if (tipo == ST_ORDER_FILTER) {
				addOrderByToVector(request, null);
			}
			ht = logic.mCaricareListaAnagrafe(vettoreRicerca,finder);
		}

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista


	    Vector vct_lista_anagrafe= (Vector)ht.get(AnagrafeDwhLogic.LISTA_ANAGRAFEDWH);
		finder = (AnagrafeDwhFinder)ht.get(AnagrafeDwhLogic.FINDER);

		sessione.setAttribute(AnagrafeDwhLogic.LISTA_ANAGRAFEDWH, vct_lista_anagrafe);
		sessione.setAttribute(NOMEFINDER, finder);

		sessione.setAttribute("origine", origine);
		if (origine != null && origine.trim().equalsIgnoreCase("MAPPA")){
			sessione.setAttribute("KEYSTR", keystr);
			sessione.setAttribute("queryForKeyStr", queryForKeyStr);
		}else
		{
			sessione.setAttribute("KEYSTR", null);
			sessione.setAttribute("queryForKeyStr", null);
		}
		sessione.setAttribute("LAST_COLOR", lastColor);
		sessione.setAttribute("LAST_FAMILY", lastFamily);

//TEST MB
		boolean saltaListaConUno = GlobalParameters.SALTA_LISTA_CON_UNO.get(this.getEnvUtente(request).getEnte()) == null ? GlobalParameters.SALTA_LISTA_CON_UNO_DEF : GlobalParameters.SALTA_LISTA_CON_UNO.get(this.getEnvUtente(request).getEnte()).booleanValue();
		if (saltaListaConUno && vct_lista_anagrafe.size() == 1){
			st=33;
			EseguiServizio(request, response);
			return;
			
			// LA VERSIONE SOTTOSTANTE DEL CODICE REMMATO CON TRE ASTERISCHI VIENE SOSTITUITA DA QUESTO SOPRA
			/*
			 * carica il dettaglio
			 */
			// *** Hashtable htd = logic.mCaricareDettaglioAnagrafe(((Anagrafe)vct_lista_anagrafe.get(0)).getId());

			// *** Anagrafe ana = (Anagrafe)htd.get(AnagrafeDwhLogic.ANAGRAFEDWH);

			// LEGGE E METTE IN SESSIONE I SOGGETTI SCARTATI
			//***super.leggiScarti(request.getSession(), ana);

			//***sessione.setAttribute(AnagrafeDwhLogic.ANAGRAFEDWH, ana);


			//**nav = new PulsantiNavigazione();
			//**if (chiamataEsterna)
			//**	nav.chiamataEsternaDettaglio();
			//**else
			//**	nav.chiamataInternaDettaglio();

			//**st = 3;

			//**this.chiamaPagina(request,response,"anagrafe/anaDwhFrame.jsp", nav);

			//cross link
			//**this.leggiCrossLink(request);

		}else {
//FINE TEST MB
			sessione.setAttribute(AnagrafeDwhLogic.LISTA_ANAGRAFEDWH, vct_lista_anagrafe);
			sessione.setAttribute(NOMEFINDER, finder);

			nav = new PulsantiNavigazione();
			if (chiamataEsterna){

				nav.chiamataEsternaLista();
				nav.setExt("1");
				nav.setPrimo(true);
			}
			else
				nav.chiamataInternaLista();
			
			if (st == ST_ORDER_FILTER)
				st = 2;
			
			this.chiamaPagina(request,response,"anagrafe/anaDwhFrame.jsp",nav);

//		TEST MB
		}
//		FINE TEST MB

	}




	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception{
				// prendere dalla request il parametrio identificativo dell'oggetto cliccato
				// eseguire la query per caricare l'oggetto selezionato dal db
				// chiamare la pagina di dettaglio

				String azione = "";
				HttpSession sessione = request.getSession();
				
				removeOggettiIndiceDaSessione(sessione);
				 
				AnagrafeDwhFinder finder = null;
				//boolean chiamataEsterna = false;
				if (sessione.getAttribute(NOMEFINDER) !=null){
					if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new AnagrafeDwhFinder().getClass()){
						finder = (AnagrafeDwhFinder)sessione.getAttribute(NOMEFINDER);
					}
				}


				if (request.getParameter("AZIONE")!= null)
					azione = request.getParameter("AZIONE");
				gestioneMultiRecord(request,response,azione,finder,sessione,"LISTA_ANAGRAFEDWH",(Vector)sessione.getAttribute("LISTA_ANAGRAFEDWH"),NOMEFINDER);
				if (azione.equals("")){
					oggettoSel="";recordScelto="";
					sessione.removeAttribute("BACK_JS_COUNT");
					sessione.removeAttribute("BACK_RECORD_ENABLE");

					if (request.getParameter("OGGETTO_SEL")!= null){
						oggettoSel = request.getParameter("OGGETTO_SEL");
					} else {
						// nel caso sia saltato dalla lista direttamente qui in dettaglio (chiamato da mCaricareLista)
						Vector ladwh = (Vector)sessione.getAttribute("LISTA_ANAGRAFEDWH");
						ladwh.elementAt(0);
						oggettoSel = ((Anagrafe) ladwh.elementAt(0)).getId();
						
						
					}
					if (request.getParameter("RECORD_SEL")!= null){
						recordScelto = request.getParameter("RECORD_SEL");
						if (finder!=null)
							finder.setRecordAttuale(new Long(recordScelto).longValue());
					} else {
						if (finder!=null)
							finder.setRecordAttuale(1);
					}


				
				/*
				 * carica il dettaglio
				 */
				AnagrafeDwhLogic logic = new AnagrafeDwhLogic(this.getEnvUtente(request));

				// CARICO GLI ID E LA DATE DI STORICIZZAZIONE
				idStorici = logic.caricaIdStorici(oggettoSel);	

				Hashtable ht = logic.mCaricareDettaglioAnagrafe(oggettoSel);

				Anagrafe ana = (Anagrafe)ht.get(AnagrafeDwhLogic.ANAGRAFEDWH);
				
				// Aggiungo i valori per la corr
				OggettoIndice oi = new OggettoIndice();
				
				oi.setIdOrig(ana.getChiave());
				oi.setFonte("1");
				oi.setProgr("1");
				oi.setDescrizione(ana.getCognome() + " " + ana.getNome());
				
				Vector<OggettoIndice> sOggettiInd = new Vector<OggettoIndice>();
				sOggettiInd.add(oi);
				sessione.setAttribute("indice_soggetti", sOggettiInd);

				OggettoIndice oi1 = new OggettoIndice();
				
				oi1.setIdOrig(ana.getIdCiv());
				oi1.setFonte("1");
				oi1.setProgr("1");
				oi1.setDescrizione(ana.getResidenza());
				
				Vector<OggettoIndice> cOggettiInd = new Vector<OggettoIndice>();
				cOggettiInd.add(oi1);
				sessione.setAttribute("indice_civici", cOggettiInd);
				
				Vector<OggettoIndice> vieOggettiInd = new Vector<OggettoIndice>();
				
				OggettoIndice oi2 = new OggettoIndice();
				
				oi2.setIdOrig(ana.getIdVia());
				oi2.setFonte("1");
				oi2.setProgr("1");
				oi2.setDescrizione(ana.getDescPrefissoVia());
					
				vieOggettiInd.add(oi2);
				sessione.setAttribute("indice_vie", vieOggettiInd);
				
				sessione.setAttribute(AnagrafeDwhLogic.VIS_STORICO_FAM_KEY, ht.get(AnagrafeDwhLogic.VIS_STORICO_FAM_KEY));
				sessione.setAttribute(AnagrafeDwhLogic.VIS_STORICO_DT_AGG, ht.get(AnagrafeDwhLogic.VIS_STORICO_DT_AGG));
				sessione.setAttribute(AnagrafeDwhLogic.VIS_STORICO_LISTA  + "_" + ana.getCodAnagrafe(), ht.get(AnagrafeDwhLogic.VIS_STORICO_LISTA));
				if (sessione.getAttribute(AnagrafeDwhLogic.LBL_STORICO_PERIODO) != null) {
					sessione.removeAttribute(AnagrafeDwhLogic.LBL_STORICO_PERIODO);
				}
				
				// ----------------------------------------------------

				// LEGGE E METTE IN SESSIONE I SOGGETTI SCARTATI
				super.leggiScarti(request.getSession(), ana, request);

				if (request.getParameter("POPUP") != null && new Boolean(request.getParameter("POPUP")).booleanValue()) {
					sessione.setAttribute(AnagrafeDwhLogic.ANAGRAFEDWH_POPUP, ana);
				} else {
					sessione.setAttribute(AnagrafeDwhLogic.ANAGRAFEDWH, ana);
				}				

				Vector listaIndirizzi = (Vector) ht.get(AnagrafeDwhLogic.ANAGRAFE_INDIRIZZI);
				if (request.getParameter("POPUP") != null && new Boolean(request.getParameter("POPUP")).booleanValue()) {
					sessione.setAttribute(AnagrafeDwhLogic.ANAGRAFE_INDIRIZZI_POPUP, listaIndirizzi);
				} else {
					sessione.setAttribute(AnagrafeDwhLogic.ANAGRAFE_INDIRIZZI + "_" + ana.getCodAnagrafe(), listaIndirizzi);
				}	

				nav = new PulsantiNavigazione();
				if (chiamataEsterna)
					nav.chiamataEsternaDettaglio();
				else
					nav.chiamataInternaDettaglio();

				if (request.getParameter("POPUP") != null && new Boolean(request.getParameter("POPUP")).booleanValue()) {
					this.chiamaPagina(request,response,"anagrafe/anaDwhPopupSoggetto.jsp", nav);
				}else{
					this.chiamaPagina(request,response,"anagrafe/anaDwhFrame.jsp", nav);
				}
				
			}
	}




	private void mCaricareFamiglia(HttpServletRequest request,HttpServletResponse response, String chiave) throws Exception{
				/*
				 * carico la famiglia
				 */
				AnagrafeFamigliaLogic logic = new AnagrafeFamigliaLogic(this.getEnvUtente(request));
				Famiglia Fam = (Famiglia)((Hashtable)logic.mCaricareFamigliaPerAnagrafe(chiave)).get("FAMIGLIA");


				HttpSession sessione = request.getSession();
				sessione.setAttribute("FAMIGLIA",Fam);


			}

	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (AnagrafeDwhFinder)finder2;
		AnagrafeDwhLogic logic = new AnagrafeDwhLogic(this.getEnvUtente(request));
		return logic.mCaricareListaAnagrafe(this.vettoreRicerca, finder);
	}


	public EscFinder getNewFinder(){
		return new AnagrafeDwhFinder();
	}
	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
		return ((Anagrafe)listaOggetti.get(recordSuccessivo)).getCodAnagrafe();
	}


	public String getTema() {
		return "Popolazione";
	}
	public String getTabellaPerCrossLink() {
		return "SIT_D_PERSONA";
	}

	public String getLocalDataSource() {
		return localDataSource;
	}

}



