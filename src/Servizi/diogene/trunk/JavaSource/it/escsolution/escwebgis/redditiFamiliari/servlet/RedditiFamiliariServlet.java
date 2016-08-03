package it.escsolution.escwebgis.redditiFamiliari.servlet;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.redditiFamiliari.bean.RedditiFamiliari;
import it.escsolution.escwebgis.redditiFamiliari.bean.RedditiFamiliariFinder;
import it.escsolution.escwebgis.redditiFamiliari.logic.RedditiFamiliariLogic;


public class RedditiFamiliariServlet extends EscServlet implements EscService {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NOMEFINDER = "FINDER133";
	
	private RedditiFamiliariFinder finder = null;
	PulsantiNavigazione nav;
	
	private String localDataSource = "jdbc/Diogene_DS";
	
	public void EseguiServizio(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		super.EseguiServizio(request,response);
		
		String ext= request.getParameter("IND_EXT");
		
		if (ext!= null && ext.equals("1"))
			_EseguiServizioExt(request,response);
		else
			_EseguiServizio(request,response);		
	}

	public void _EseguiServizio(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		super.EseguiServizio(request,response);
		try {
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
					 //this.leggiCrossLink(request);
					 break;
				 case 33:
					 // ho cliccato su un elemento --> visualizzo il dettaglio
					 mCaricareDettaglio(request,response,33);
					 //this.leggiCrossLink(request);
					 break;
				 //TODO verificare se esistono ulteriori altri casi
			}
		} catch(it.escsolution.escwebgis.common.DiogeneException de) {
			throw de;
		} catch(Exception ex) {
			log.error(ex.getMessage(),ex);
		}
	}

	public void _EseguiServizioExt(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		super.EseguiServizio(request,response);
		try {			
			switch (st) {
				case 101:
					mCaricareDettaglio(request,response,101);			
					break;
				case 102:
					mCaricareDettaglio(request,response,102);							 
					break;
				case 103:
					mCaricareDettaglio(request,response,103);								 
					break;
				//TODO verificare se esistono ulteriori altri casi
			}				
		} catch(it.escsolution.escwebgis.common.DiogeneException de) {
			throw de;
		} catch(Exception ex) {
			throw new it.escsolution.escwebgis.common.DiogeneException(ex);
		}
	}
	
	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//caricare dati che servono nella pagina di ricerca
		HttpSession sessione = request.getSession();

		Vector listaElementiFiltro = new Vector();

		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=", "uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like", "contiene"));
		
		//TODO basta uguale?
		Vector operatoriData = new Vector();
		operatoriData.add(new EscOperatoreFiltro("=", "uguale"));

		// costruisce il vettore di elementi per la pagina di ricerca
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		
		//codice fiscale					
		elementoFiltro.setLabel("Codice fiscale");
		elementoFiltro.setAttributeName("CODFISC");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("SIT_D_FAM_STORICO.CODFISC");
		listaElementiFiltro.add(elementoFiltro);

		//cognome
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cognome");
		elementoFiltro.setAttributeName("COGNOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("SIT_D_FAM_STORICO.COGNOME");
		listaElementiFiltro.add(elementoFiltro);
		
		//nome
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Nome");
		elementoFiltro.setAttributeName("NOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("SIT_D_FAM_STORICO.NOME");
		listaElementiFiltro.add(elementoFiltro);
		
		//data di nascita
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data di nascita");
		elementoFiltro.setAttributeName("DATA_NASCITA");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriData);
		elementoFiltro.setCampoFiltrato("SIT_D_FAM_STORICO.DATA_NASCITA");
		listaElementiFiltro.add(elementoFiltro);
		
		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		//chiamare la pagina di ricerca
		nav = new PulsantiNavigazione();
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"redditiFamiliari/redFamiliariFrame.jsp", nav);
	}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response) throws Exception {
		HttpSession sessione = request.getSession();
		if (sessione.getAttribute(NOMEFINDER) != null) {
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new RedditiFamiliariFinder().getClass()) {
				finder = (RedditiFamiliariFinder)sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (RedditiFamiliariFinder)gestireMultiPagina(finder,request);

		RedditiFamiliariLogic logic = new RedditiFamiliariLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareLista(vettoreRicerca, finder);

	    Vector vct_lista = (Vector)ht.get(RedditiFamiliariLogic.LISTA);
		finder = (RedditiFamiliariFinder)ht.get(RedditiFamiliariLogic.FINDER);
		sessione.setAttribute(RedditiFamiliariLogic.LISTA, vct_lista);
		sessione.setAttribute(NOMEFINDER, finder);

		nav = new PulsantiNavigazione();
		if (chiamataEsterna) {
			nav.chiamataEsternaLista();
			nav.setExt("1");
			nav.setPrimo(true);
		}
		else
			nav.chiamataInternaLista();
		this.chiamaPagina(request,response,"redditiFamiliari/redFamiliariFrame.jsp",nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response, int tipo) throws Exception {		
		String azione = "";
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
		 
		RedditiFamiliariFinder finder = null;
		if (sessione.getAttribute(NOMEFINDER) != null) {
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new RedditiFamiliariFinder().getClass()) {
				finder = (RedditiFamiliariFinder)sessione.getAttribute(NOMEFINDER);
			}
		}

		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request,response,azione,finder,sessione,RedditiFamiliariLogic.LISTA,(Vector)sessione.getAttribute(RedditiFamiliariLogic.LISTA),NOMEFINDER);

		if (azione.equals("")) {
			oggettoSel = "";
			recordScelto = "";
			sessione.removeAttribute("BACK_JS_COUNT");
			sessione.removeAttribute("BACK_RECORD_ENABLE");

			if (request.getParameter("OGGETTO_SEL")!= null) {
				oggettoSel = request.getParameter("OGGETTO_SEL");
			}
			if (request.getParameter("RECORD_SEL")!= null) {
				recordScelto = request.getParameter("RECORD_SEL");
				if (finder != null)
					finder.setRecordAttuale(new Long(recordScelto).longValue());
			}
		}

		//carica il dettaglio
		RedditiFamiliariLogic logic = new RedditiFamiliariLogic(this.getEnvUtente(request));
		
		Hashtable ht =null;
		
		ht = logic.mCaricareDettaglio(oggettoSel, request);
		RedditiFamiliari red = (RedditiFamiliari)ht.get(RedditiFamiliariLogic.DETTAGLIO);
		sessione.setAttribute(RedditiFamiliariLogic.DETTAGLIO, red);

		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();		
		
		// Aggiungo i valori per l'indice di correlazione TODO anche qui???		
		
		if (request.getParameter("ME_POPUP") != null && new Boolean(request.getParameter("ME_POPUP")).booleanValue()) {
			this.chiamaPagina(request,response,"redditiFamiliari/redFamiliariDetail.jsp", nav);
		} else {
			this.chiamaPagina(request,response,"redditiFamiliari/redFamiliariFrame.jsp", nav);
		}		
	}

	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception {
		finder = (RedditiFamiliariFinder)finder2;
		RedditiFamiliariLogic logic = new RedditiFamiliariLogic(this.getEnvUtente(request));
		return logic.mCaricareLista(this.vettoreRicerca, finder);
	}


	public EscFinder getNewFinder() {
		return new RedditiFamiliariFinder();
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo) {
		RedditiFamiliari red = (RedditiFamiliari)listaOggetti.get(recordSuccessivo);
		String key = red.getChiave();
		return key;
	}

	public String getTema() {
		return "Redditi";
	}

	public String getTabellaPerCrossLink() {
		return "SIT_D_FAM_STORICO";
	}
	
	public String getLocalDataSource() {
		return localDataSource;
	}

}
