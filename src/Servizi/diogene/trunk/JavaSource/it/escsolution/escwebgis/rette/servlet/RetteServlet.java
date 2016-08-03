package it.escsolution.escwebgis.rette.servlet;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.pratichePortale.bean.NomeStato;
import it.escsolution.escwebgis.pratichePortale.bean.TipiServizio;
import it.escsolution.escwebgis.pratichePortale.logic.PraticheLogic;
import it.escsolution.escwebgis.rette.bean.Anno;
import it.escsolution.escwebgis.rette.bean.RetteFinder;
import it.escsolution.escwebgis.rette.bean.RttBollette;
import it.escsolution.escwebgis.rette.bean.RttDettaglio;
import it.escsolution.escwebgis.rette.bean.RttRate;
import it.escsolution.escwebgis.rette.bean.RttSoggetto;
import it.escsolution.escwebgis.rette.logic.RetteLogic;
import it.webred.indice.OggettoIndice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RetteServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private RetteFinder finder = null;

	private final String NOMEFINDER = "FINDER119";
	private final String COD_FONTE = "18";
	
	private String localDataSource = "jdbc/Diogene_DS";
	
	
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
	
	public void EseguiServizio(
			HttpServletRequest request,HttpServletResponse response)
					throws ServletException, IOException {
		/*
		 * ad ogni uc corrisponde una funzionalità diversa
		 *
		 */
		super.EseguiServizio(request,response);
		
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		String ext= request.getParameter("IND_EXT");
		
		if (ext!= null && ext.equals("1"))
			EseguiServizioExt(request,response);
		else
			_EseguiServizio(request,response);
		
	}
    

	public void _EseguiServizio(
			HttpServletRequest request,HttpServletResponse response)
					throws ServletException, IOException {

		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		super.EseguiServizio(request,response);
		
		try {
			switch (st){
				case 1:
					// carico la form di ricerca
					pulireSessione(request);
					mCaricareFormRicerca(request, response);
					break;
				case 2:
					// vengo dalla pagina di ricerca --> predispongo la lista e la passo alla pagina di lista
					mCaricareLista(request, response);
					break;
				case 3:
					// ho cliccato su un elemento --> visualizzo il dettaglio
					mCaricareDettaglio(request, response,st);
				//	this.leggiCrossLink(request);
					break;
				case 33:
					 // ho cliccato su un elemento --> visualizzo il dettaglio
					 mCaricareDettaglio(request,response,st);
				//	this.leggiCrossLink(request);
					break;
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
	
	public void EseguiServizioExt(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		super.EseguiServizio(request,response);
		try
		{
			
				switch (st){
				case 101:
					pulireSessione(request);
					mCaricareListaExt(request,response);
					break;
				case 102:
					pulireSessione(request);
					mCaricareListaExt(request,response);
					break;
				case 103:
					pulireSessione(request);
					mCaricareListaExt(request,response);
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
	

	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession sessione = request.getSession();
		
		Vector listaElementiFiltro = new Vector();
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=","uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like","contiene"));



		Vector operatoriNumerici = new Vector();
		operatoriNumerici.add(new EscOperatoreFiltro(">",">"));
		operatoriNumerici.add(new EscOperatoreFiltro("<","<"));
		operatoriNumerici.add(new EscOperatoreFiltro("=","="));
		operatoriNumerici.add(new EscOperatoreFiltro("<>","<>"));

		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("like","contiene"));
		operatoriStringaRid.add(new EscOperatoreFiltro("=","uguale"));

		RetteLogic logic = new RetteLogic(this.getEnvUtente(request));
		List<String> listaAnno = logic.caricaAnno();
		
		// costruisce il vettore di elementi per la pagina di ricerca
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("DATI RETTA");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		
		Vector<Anno> vctTipo = new Vector<Anno>();
		vctTipo.add(new Anno("", "Tutti"));
		for(String tipi: listaAnno){
			vctTipo.add(new Anno(tipi, tipi));
		}
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Anno");
		elementoFiltro.setAttributeName("ANNO");
		elementoFiltro.setTipo("N");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vctTipo);
		elementoFiltro.setCampoFiltrato("ANNO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Codice bolletta");
		elementoFiltro.setAttributeName("COD_BOLLETTA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("COD_BOLLETTA");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data da");
		elementoFiltro.setAttributeName("DATA_BOLLETTA");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("DATA_BOLLETTA");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data a");
		elementoFiltro.setAttributeName("DATA_BOLLETTA");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("DATA_BOLLETTA");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Oggetto");
		elementoFiltro.setAttributeName("OGGETTO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("OGGETTO");
		listaElementiFiltro.add(elementoFiltro);
		
		//titolo gruppo
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("DATI SOGGETTO");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Intestatario");
		elementoFiltro.setAttributeName("DES_INTESTATARIO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("DES_INTESTATARIO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Codice fiscale");
		elementoFiltro.setAttributeName("CODICE_FISCALE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("CODICE_FISCALE");
		listaElementiFiltro.add(elementoFiltro);				
		
		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"rette/RetteFrame.jsp", nav);

	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//prendere dalla request i parametri di ricerca impostati dall'utente
		 HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null){
			finder = (RetteFinder)sessione.getAttribute(NOMEFINDER);
		}
		else{
			finder = null;
		}

		 finder = (RetteFinder)gestireMultiPagina(finder,request);

		 RetteLogic logic = new RetteLogic(this.getEnvUtente(request));
		 Hashtable ht = logic.mCaricareLista(vettoreRicerca, finder);

		 Vector vct_lista = (Vector)ht.get(RetteLogic.LISTA_RETTE);
		 finder = (RetteFinder)ht.get(RetteLogic.FINDER);

		 sessione.setAttribute(RetteLogic.LISTA_RETTE, vct_lista);
		 sessione.setAttribute(RetteLogic.FINDER, finder);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();
		
		this.chiamaPagina(request,response,"rette/RetteFrame.jsp", nav);
	}
	
	private void mCaricareListaExt(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();
		String progEs= request.getParameter("progEs");
		if (request.getParameter("OGGETTO_SEL")!= null){
			oggettoSel = request.getParameter("OGGETTO_SEL");
		}

		RetteLogic logic = new RetteLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaExt(oggettoSel, progEs);

		Vector vct_lista = (Vector)ht.get(RetteLogic.LISTA_RETTE);

		sessione.setAttribute(RetteLogic.LISTA_RETTE, vct_lista);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();
		
		this.chiamaPagina(request,response,"rette/RetteFrameExt.jsp", nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response, int tipo)throws Exception{
			
		// prendere dalla request il parametro identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio
		
		String azione = "";
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
	
	
		if (sessione.getAttribute(NOMEFINDER) != null) {
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new RetteFinder().getClass()) {
				finder = (RetteFinder)sessione.getAttribute(NOMEFINDER);
			} else {
				finder = null;
			}
		}
		
		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		
		gestioneMultiRecord(request, response, azione, finder, sessione, RetteLogic.LISTA_RETTE, (Vector)sessione.getAttribute(RetteLogic.LISTA_RETTE), NOMEFINDER);
		
		if (!azione.equals("")) {
			Vector listaOggettiRette = (Vector)sessione.getAttribute(RetteLogic.LISTA_RETTE);
		} else {
			oggettoSel = "";
			recordScelto = "";
			sessione.removeAttribute("BACK_JS_COUNT");
			sessione.removeAttribute("BACK_RECORD_ENABLE");

			if (request.getParameter("OGGETTO_SEL")!= null){
				oggettoSel = request.getParameter("OGGETTO_SEL");
			}
			if (request.getParameter("RECORD_SEL")!= null){
				recordScelto = request.getParameter("RECORD_SEL");
				if (finder!=null)
					finder.setRecordAttuale(new Long(recordScelto).longValue());
			}
		}

		/*
		 * carica il dettaglio
		 */
		RetteLogic logic = new RetteLogic(this.getEnvUtente(request));
		Hashtable ht = null;
		String progEs= request.getParameter("progEs");
		
		//nel caso in cui provengo da Correlazione Vie/Civici e il parametro non inizia con #, e nel caso della Correlaione Fabbricati , allora l'id si riferisce all'oggetto e non al soggetto. 
		//In tutti gli altri casi l'id si riferisce al soggetto
		log.debug("ricerca dettaglio multe da ID: " + oggettoSel );
		if (oggettoSel.startsWith("#"))
			oggettoSel= oggettoSel.substring(1);
			
		ht = logic.mCaricareDettaglio(oggettoSel, progEs);
		
		RttBollette tes = (RttBollette)ht.get(RetteLogic.RETTA);
		Vector<RttDettaglio> dettaglio = (Vector<RttDettaglio>) ht.get(RetteLogic.DETTAGLIO);
		Vector<RttRate> rate =  (Vector<RttRate>) ht.get(RetteLogic.RATE);
		RttSoggetto sogg = (RttSoggetto)ht.get(RetteLogic.SOGGETTO);
		
		sessione.setAttribute(RetteLogic.RETTA, tes);
		sessione.setAttribute(RetteLogic.DETTAGLIO, dettaglio);
		sessione.setAttribute(RetteLogic.RATE, rate);
		
		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		
		// Aggiungo Soggetti per correlazione
		OggettoIndice oi = new OggettoIndice();
		
		oi.setIdOrig(tes.getId());
		oi.setFonte("18");
		oi.setProgr("1");
		String descr = (!"-".equals(sogg.getCognome())?sogg.getCognome():"")
				+ " " + (!"-".equals(sogg.getNome())?sogg.getNome():"");
		oi.setDescrizione(descr.trim());
		
		Vector<OggettoIndice> soggettiInd = new Vector<OggettoIndice>();
		soggettiInd.add(oi);
		sessione.setAttribute("indice_soggetti", soggettiInd);
		
		Vector<OggettoIndice> vieInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> civiciInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> fabbInd = new Vector<OggettoIndice>();
		
		sessione.setAttribute("indice_vie", vieInd);
		sessione.setAttribute("indice_civici", civiciInd);
		sessione.setAttribute("indice_fabbricati", fabbInd);
		
		this.chiamaPagina(request,response,"rette/RetteFrame.jsp", nav);
	}	


	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo){
		return ((RttBollette)listaOggetti.get(recordSuccessivo)).getChiave();
	}

	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (RetteFinder)finder2;
		RetteLogic logic = new RetteLogic(this.getEnvUtente(request));
		return logic.mCaricareLista(this.vettoreRicerca,finder);
	}

	public EscFinder getNewFinder(){
		return new RetteFinder();
	}

	public String getTema() {
		return "Rette";
	}
	
	public String getTabellaPerCrossLink() {
		return "SIT_RTT_BOLLETTE";
	}
	
	public String getLocalDataSource() {
		return localDataSource;
	}

}

