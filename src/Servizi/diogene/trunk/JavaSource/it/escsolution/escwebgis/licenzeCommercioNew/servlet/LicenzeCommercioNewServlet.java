package it.escsolution.escwebgis.licenzeCommercioNew.servlet;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.licenzeCommercioNew.bean.LicenzeCommercioAnagNew;
import it.escsolution.escwebgis.licenzeCommercioNew.bean.LicenzeCommercioNew;
import it.escsolution.escwebgis.licenzeCommercioNew.bean.LicenzeCommercioNewFinder;
import it.escsolution.escwebgis.licenzeCommercioNew.logic.LicenzeCommercioNewLogic;
import it.webred.indice.OggettoIndice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LicenzeCommercioNewServlet extends EscServlet implements EscService {

	private String recordScelto;
	public static final String NOMEFINDER = "FINDER55";
	private LicenzeCommercioNewFinder finder = null;
	PulsantiNavigazione nav;
	
	private String localDataSource = "jdbc/Diogene_DS";
	

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    
    public void EseguiServizio(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
    	HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
    	
    	super.EseguiServizio(request,response);
		
		String ext= request.getParameter("IND_EXT");
		
		if (ext!= null && ext.equals("1"))
			EseguiServizioExt(request,response);
		else
			_EseguiServizio(request,response);
		
	}
    
	public void _EseguiServizio(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		super.EseguiServizio(request,response);
		try
		{
			if (request.getParameter("listavie") != null) {
				mListaVie(request, response, "SIT_LICENZE_COMMERCIO_VIE", null, "INDIRIZZO", "INDIRIZZO");			
			} else {
				switch (st){
					case 1:
						// carico la form di ricerca
						pulireSessione(request);
						mCaricareFormRicerca(request,response);
						break;
					case 2:
						mCaricareLista(request,response);							 
						break;
					case 3:
						mCaricareDettaglio(request,response);
						//this.leggiCrossLink(request);						 
						break;
					case 33:
						mCaricareDettaglio(request,response);
						//this.leggiCrossLink(request);						 
						break;
				}
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
	
	public void EseguiServizioExt(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		super.EseguiServizio(request,response);
		try
		{
			
				switch (st){
					case 101:
						mCaricareListaFromSoggetto(request,response);			
						break;
					case 102:
						//mCaricareListaFromVia(request,response);
						mCaricareDettaglio(request,response);
						break;
					case 103:
						mCaricareDettaglio(request,response);
						//this.leggiCrossLink(request);						 
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
		
		Vector operatoriStringaRidDefLike = new Vector();
		operatoriStringaRidDefLike.add(new EscOperatoreFiltro("like", "contiene"));
		operatoriStringaRidDefLike.add(new EscOperatoreFiltro("=", "uguale"));	

		Vector operatoriStringaUguale = new Vector();
		operatoriStringaUguale.add(new EscOperatoreFiltro("=", "uguale"));
		
		Vector operatoriNumericiRid = new Vector();
		operatoriNumericiRid.add(new EscOperatoreFiltro("=","="));

		// costruisce il vettore di elementi per la pagina di ricerca
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Numero esercizio");
		elementoFiltro.setAttributeName("NUMERO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("NUMERO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Numero protocollo");
		elementoFiltro.setAttributeName("NUMERO_PROTOCOLLO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaUguale);
		elementoFiltro.setCampoFiltrato("NUMERO_PROTOCOLLO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Anno protocollo");
		elementoFiltro.setAttributeName("ANNO_PROTOCOLLO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaUguale);
		elementoFiltro.setCampoFiltrato("ANNO_PROTOCOLLO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Tipologia");
		elementoFiltro.setAttributeName("TIPOLOGIA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("TIPOLOGIA");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Indirizzo");
		elementoFiltro.setAttributeName("INDIRIZZO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRidDefLike);
		elementoFiltro.setCampoFiltrato("INDIRIZZO");
		elementoFiltro.setExtraHTML("<span><input type=\"button\" class=\"TXTmainLabel\" style=\"text-align:center\" value=\"Cerca\" onclick=\"javascript:danRequest('"+it.escsolution.escwebgis.common.EscServlet.URL_PATH+"/LicenzeCommercioNew?listavie='+document.getElementById('INDIRIZZO').value,'divListaVie')\"/></span><span id=\"divListaVie\" class=\"txtRow\"  ></span> \n");		
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Sezione catastale");
		elementoFiltro.setAttributeName("SEZIONE_CATASTALE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaUguale);
		elementoFiltro.setCampoFiltrato("SEZIONE_CATASTALE");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Foglio");
		elementoFiltro.setAttributeName("FOGLIO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaUguale);
		elementoFiltro.setCampoFiltrato("FOGLIO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Particella");
		elementoFiltro.setAttributeName("PARTICELLA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("PARTICELLA");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Subalterno");
		elementoFiltro.setAttributeName("SUBALTERNO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("SUBALTERNO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Codice fabbricato");
		elementoFiltro.setAttributeName("CODICE_FABBRICATO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("CODICE_FABBRICATO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Zona");
		elementoFiltro.setAttributeName("ZONA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("ZONA");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Ragione sociale");
		elementoFiltro.setAttributeName("RAG_SOC");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("RAG_SOC");
		listaElementiFiltro.add(elementoFiltro);	
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Riferimento atto");
		elementoFiltro.setAttributeName("RIFERIM_ATTO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("RIFERIM_ATTO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Soggetto");
		elementoFiltro.setAttributeName("SOGGETTO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("SOGGETTO");
		listaElementiFiltro.add(elementoFiltro);	
		
		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO", Tema.getNomeFiltro(uc));
		

		//chiamare la pagina di ricerca
		nav = new PulsantiNavigazione();
		nav.chiamataRicerca();
		this.chiamaPagina(request, response, "licenzeCommercioNew/licenzeCommercioNewFrame.jsp", nav);
	}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null)
		{
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new LicenzeCommercioNewFinder().getClass())
			{
				finder = (LicenzeCommercioNewFinder)sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (LicenzeCommercioNewFinder)gestireMultiPagina(finder, request);		
		LicenzeCommercioNewLogic logic = new LicenzeCommercioNewLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareLista(vettoreRicerca, finder);
		Vector vct_lista = (Vector)ht.get(LicenzeCommercioNewLogic.LISTA);
		finder = (LicenzeCommercioNewFinder)ht.get(LicenzeCommercioNewLogic.FINDER);
		sessione.setAttribute(LicenzeCommercioNewLogic.LISTA, vct_lista);
		sessione.setAttribute(NOMEFINDER, finder);

		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
		{
			nav.chiamataEsternaLista();
			nav.setExt("1");
			nav.setPrimo(true);
		}
		else
			nav.chiamataInternaLista();

		this.chiamaPagina(request,response,"licenzeCommercioNew/licenzeCommercioNewFrame.jsp",nav);
	}
	
	private void mCaricareListaFromSoggetto(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		
		String idSoggetto= request.getParameter("OGGETTO_SEL");
		HttpSession sessione = request.getSession();
		
		LicenzeCommercioNewLogic logic = new LicenzeCommercioNewLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaFromSoggetto(idSoggetto);
		Vector vct_lista = (Vector)ht.get(LicenzeCommercioNewLogic.LISTA);
		
		sessione.setAttribute(LicenzeCommercioNewLogic.LISTA, vct_lista);
		
		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
		{
			nav.chiamataEsternaLista();
			nav.setExt("1");
			nav.setPrimo(true);
		}
		else
			nav.chiamataInternaLista();

		this.chiamaPagina(request,response,"licenzeCommercioNew/licenzeCommercioNewFrame.jsp",nav);
	}
	
	private void mCaricareListaFromVia(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		
		String idVia= request.getParameter("OGGETTO_SEL");
		HttpSession sessione = request.getSession();
		
		LicenzeCommercioNewLogic logic = new LicenzeCommercioNewLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaFromVia(idVia);
		Vector vct_lista = (Vector)ht.get(LicenzeCommercioNewLogic.LISTA);
		
		sessione.setAttribute(LicenzeCommercioNewLogic.LISTA, vct_lista);
		
		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
		{
			nav.chiamataEsternaLista();
			nav.setExt("1");
			nav.setPrimo(true);
		}
		else
			nav.chiamataInternaLista();

		this.chiamaPagina(request,response,"licenzeCommercioNew/licenzeCommercioNewFrame.jsp",nav);
	}
	
	private void mCaricareListaFromCiv(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		
		String idSoggetto= request.getParameter("OGGETTO_SEL");
		HttpSession sessione = request.getSession();
		
		LicenzeCommercioNewLogic logic = new LicenzeCommercioNewLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaFromSoggetto(idSoggetto);
		Vector vct_lista = (Vector)ht.get(LicenzeCommercioNewLogic.LISTA);
		
		sessione.setAttribute(LicenzeCommercioNewLogic.LISTA, vct_lista);
		
		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
		{
			nav.chiamataEsternaLista();
			nav.setExt("1");
			nav.setPrimo(true);
		}
		else
			nav.chiamataInternaLista();

		this.chiamaPagina(request,response,"licenzeCommercioNew/licenzeCommercioNewFrame.jsp",nav);
	}	
	
	
	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		String azione = "";
		HttpSession sessione = request.getSession();
				
		removeOggettiIndiceDaSessione(sessione);
		 
		LicenzeCommercioNewFinder finder = null;
		if (sessione.getAttribute(NOMEFINDER) !=null)
		{
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new LicenzeCommercioNewFinder().getClass())
			{
				finder = (LicenzeCommercioNewFinder)sessione.getAttribute(NOMEFINDER);
			}
		}

		if (request.getParameter("AZIONE") != null)
			azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request, response, azione, finder, sessione, LicenzeCommercioNewLogic.LISTA, (Vector)sessione.getAttribute(LicenzeCommercioNewLogic.LISTA), NOMEFINDER);

		if (azione.equals(""))
		{
			oggettoSel = "";
			recordScelto = "";
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
		LicenzeCommercioNewLogic logic = new LicenzeCommercioNewLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglio(oggettoSel);
		
		LicenzeCommercioNew lic = (LicenzeCommercioNew)ht.get(LicenzeCommercioNewLogic.DATI_LICENZE_COMMERCIO_NEW);
		sessione.setAttribute(LicenzeCommercioNewLogic.DATI_LICENZE_COMMERCIO_NEW, lic);
		
		//carica gli id storici		
		// CARICO GLI ID E LA DATE DI STORICIZZAZIONE
		idStorici = logic.caricaIdStorici(oggettoSel);		
		
		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		
		
		ArrayList<LicenzeCommercioAnagNew> anags= lic.getAnags();
		
		if (anags != null && anags.size()>0){
		
			// Aggiungo i valori per l'indice di correlazione
			Vector<OggettoIndice> sOggettiInd = new Vector<OggettoIndice>();
			
			for (LicenzeCommercioAnagNew ana: anags){
			OggettoIndice oi = new OggettoIndice();
			
			oi.setIdOrig(ana.getId());
			oi.setFonte("13");
			oi.setProgr("1");
			
			if ((ana.getCognome()!= null && !"".equals(ana.getCognome())) || (ana.getNome()!= null && !"".equals(ana.getNome())))
				oi.setDescrizione(ana.getCognome() + " " + ana.getNome());
			else
				oi.setDescrizione(ana.getDenominazione());
			
			sOggettiInd.add(oi);
		}
			
			log.debug("lista soggetti: " + sOggettiInd.size());
			sessione.setAttribute("indice_soggetti", sOggettiInd);
		}
		
		OggettoIndice oi2 = new OggettoIndice();
		String civ = lic.getCivico();
		if (civ == null)
			civ = "";
		
		oi2.setDescrizione(lic.getIndirizzo() + "," + civ);
		oi2.setFonte("13");
		oi2.setProgr("1");
		oi2.setIdOrig(lic.getId());
		
		Vector cOggettiInd = new Vector<OggettoIndice>();
		
		cOggettiInd.add(oi2);
		
		sessione.setAttribute("indice_civici", cOggettiInd);
		
		OggettoIndice oi3 = new OggettoIndice();
		String idVia = lic.getIdVia();
		if (idVia == null)
			idVia = "";
		
		oi3.setDescrizione(lic.getIndirizzo() + "," + lic.getCivico());
		oi3.setFonte("13");
		oi3.setProgr("1");
		oi3.setIdOrig(idVia);
		
		Vector vieOggettiInd = new Vector<OggettoIndice>();
		
		vieOggettiInd.add(oi3);
		
		sessione.setAttribute("indice_vie", vieOggettiInd);
		
		this.chiamaPagina(request,response,"licenzeCommercioNew/licenzeCommercioNewFrame.jsp", nav);		
	}
	
	
	
	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception {
		finder = (LicenzeCommercioNewFinder)finder2;
		LicenzeCommercioNewLogic logic = new LicenzeCommercioNewLogic(this.getEnvUtente(request));
		return logic.mCaricareLista(this.vettoreRicerca, finder);
	}

	public EscFinder getNewFinder()
	{
		return new LicenzeCommercioNewFinder();
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo)
	{
		LicenzeCommercioNew lic = (LicenzeCommercioNew)listaOggetti.get(recordSuccessivo);
		String key = lic.getChiave();
		return key;
	}

	public String getTema()
	{
		return "Licenze Commercio";
	}

	public String getTabellaPerCrossLink()
	{
		return "SIT_LICENZE_COMMERCIO_DIOGENE";
	}
	
	public String getLocalDataSource() {
		return localDataSource;
	}

}
