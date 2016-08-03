package it.escsolution.escwebgis.concessioni.servlet;


import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.concessioni.bean.FornituraDia;
import it.escsolution.escwebgis.concessioni.bean.FornituraDiaFinder;
import it.escsolution.escwebgis.concessioni.logic.FornituraDiaLogic;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;




public class FornituraDiaServlet extends EscServlet implements EscService
{

	private String recordScelto;

	private FornituraDiaLogic logic = null;


	public static final String NOMEFINDER = "FINDER51";

	private FornituraDiaFinder finder = null;

	PulsantiNavigazione nav =  new PulsantiNavigazione();
	

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }



	public void EseguiServizio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		/*
		 * ad ogni uc corrisponde una funzionalità diversa
		 */
		// super.init(getServletConfig());
		// String aaa = getServletConfig().getServletName();
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		super.EseguiServizio(request, response);
		try
		{
			switch (st)
			{
			case 1:
				// carico la form di ricerca
				pulireSessione(request);
				mCaricareFormRicerca(request, response);
				break;
			case 2:
				// vengo dalla pagina di ricerca --> predispongo la lista e la
				// passo alla pagina di lista
				// pulireSessione(request);
				mCaricareLista(request, response);
				break;
			case 3:
				// ho cliccato su un elemento --> visualizzo il dettaglio
				mCaricareDettaglio(request, response);
				//this.leggiCrossLink(request);
				break;		
			case 33:
				 // ho cliccato su un elemento --> visualizzo il dettaglio						 
					 mCaricareDettaglio(request,response);
					 //this.leggiCrossLink(request);						 
				 	 break;
			/*
			 * case 4: // ho cliccato su un elemento --> visualizzo il dettaglio
			 * mCaricareDettaglio(request,response);
			 * this.leggiCrossLink(request); break; case 5: // ho cliccato su un
			 * elemento --> visualizzo il dettaglio
			 * mCaricareLista(request,response,true); break;
			 */
			}
		}
		catch (it.escsolution.escwebgis.common.DiogeneException de)
		{
			throw de;
		}
		catch (Exception ex)
		{
			log.error(ex.getMessage(),ex);
		}

	}


	
	
	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		// caricare dati che servono nella pagina di ricerca
		HttpSession sessione = request.getSession();

		
		Vector listaElementiFiltro = new Vector();
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=","uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like","contiene"));
		Vector operatoriLike = new Vector();
		operatoriLike.add(new EscOperatoreFiltro("like","contiene"));
	

		Vector operatoriNumerici = new Vector();
		operatoriNumerici.add(new EscOperatoreFiltro("=","="));
		operatoriNumerici.add(new EscOperatoreFiltro("<>","<>"));
		operatoriNumerici.add(new EscOperatoreFiltro(">",">"));
		operatoriNumerici.add(new EscOperatoreFiltro("<","<")); 
		
		Vector operatoriNumericiSoloMaggiorneUguale = new Vector();
		operatoriNumericiSoloMaggiorneUguale.add(new EscOperatoreFiltro(">=",">="));
 
		Vector operatoriNumericiSoloMinoreUguale = new Vector();
		operatoriNumericiSoloMinoreUguale.add(new EscOperatoreFiltro("<=","<="));
		
		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=","uguale"));

		// costruisce il vettore di elementi per la pagina di ricerca
	
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();				

		elementoFiltro.setLabel("Fornitura Dia");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Protocollo");
		elementoFiltro.setAttributeName("GEN.PROTOCOLLO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("GEN.PROTOCOLLO");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Fornitura");
		elementoFiltro.setAttributeName("GEN.FK_FORNITURA");
		elementoFiltro.setTipo("N");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("GEN.FK_FORNITURA");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Richiedente");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Codice Fiscale");
		elementoFiltro.setAttributeName("GEN.RIC_CODICE_FISCALE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("GEN.RIC_CODICE_FISCALE");
		listaElementiFiltro.add(elementoFiltro);
		
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cognome");
		elementoFiltro.setAttributeName("GEN.RIC_COGNOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("GEN.RIC_COGNOME");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Nome");
		elementoFiltro.setAttributeName("GEN.RIC_NOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("GEN.RIC_NOME");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Immobile");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Foglio");
		elementoFiltro.setAttributeName("CAT.FOGLIO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("CAT.FOGLIO");
		listaElementiFiltro.add(elementoFiltro);
		
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Particella");
		elementoFiltro.setAttributeName("CAT.PARTICELLA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("CAT.PARTICELLA");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Subalterno");
		elementoFiltro.setAttributeName("CAT.SUBALTERNO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("CAT.SUBALTERNO");
		listaElementiFiltro.add(elementoFiltro);;

		
		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		//chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request, response, "concessioni/fornituraDiaFrame.jsp", nav);

	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER) != null)
		{
			if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new FornituraDiaFinder().getClass())
			{
				finder = (FornituraDiaFinder) sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (FornituraDiaFinder) gestireMultiPagina(finder, request);

		FornituraDiaLogic logic = new FornituraDiaLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaDia(vettoreRicerca, finder);

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista

		Vector vct_lista_DIA = (Vector) ht.get(FornituraDiaLogic.LISTA_DIA);
		finder = (FornituraDiaFinder) ht.get("FINDER");
		sessione.setAttribute(FornituraDiaLogic.LISTA_DIA, vct_lista_DIA);
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
		this.chiamaPagina(request, response, "concessioni/fornituraDiaFrame.jsp", nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// prendere dalla request il parametrio identificativo dell'oggetto
		// cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio

		String azione = "";
		HttpSession sessione = request.getSession();
		removeOggettiIndiceDaSessione(sessione);
		
		FornituraDiaFinder finder = null;
		// boolean chiamataEsterna = false;
		if (sessione.getAttribute(NOMEFINDER) != null)
		{
			if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new FornituraDiaFinder().getClass())
			{
				finder = (FornituraDiaFinder) sessione.getAttribute(NOMEFINDER);
			}
		}

		if (request.getParameter("AZIONE") != null)
			azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request, response, azione, finder, sessione, FornituraDiaLogic.LISTA_DIA, (Vector) sessione.getAttribute(FornituraDiaLogic.LISTA_DIA), NOMEFINDER);
		if (azione.equals(""))
		{
			oggettoSel = "";
			recordScelto = "";
			sessione.removeAttribute("BACK_JS_COUNT");
			sessione.removeAttribute("BACK_RECORD_ENABLE");

			if (request.getParameter("OGGETTO_SEL") != null)
			{
				oggettoSel = request.getParameter("OGGETTO_SEL");
			}
			if (request.getParameter("RECORD_SEL") != null)
			{
				recordScelto = request.getParameter("RECORD_SEL");
				if (finder != null)
					finder.setRecordAttuale(new Long(recordScelto).longValue());
			}
		}

		/*
		 * carica il dettaglio
		 */
		FornituraDiaLogic logic = new FornituraDiaLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioDia(oggettoSel);
		FornituraDia dia = (FornituraDia) ht.get(FornituraDiaLogic.FORNITURADIA);
		sessione.setAttribute(FornituraDiaLogic.FORNITURADIA, dia);

		
		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		

		this.chiamaPagina(request, response, "concessioni/fornituraDiaFrame.jsp", nav);

		// EscFinder escf = (EscFinder)sessione.getAttribute(NOMEFINDER);

	}


	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception
	{
		finder = (FornituraDiaFinder) finder2;
		FornituraDiaLogic logic = new FornituraDiaLogic(this.getEnvUtente(request));
		return logic.mCaricareListaDia(this.vettoreRicerca, finder);
	}

	public EscFinder getNewFinder()
	{
		return new FornituraDiaFinder();
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo)
	{
		return ((FornituraDia) listaOggetti.get(recordSuccessivo)).getChiave();
	}

	public String getTema()
	{
		return "Concessioni Edilizie";
	}



}
