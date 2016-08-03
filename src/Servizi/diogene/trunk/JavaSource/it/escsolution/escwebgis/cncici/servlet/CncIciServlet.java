
package it.escsolution.escwebgis.cncici.servlet;


import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.cncici.bean.CncIci;
import it.escsolution.escwebgis.cncici.bean.CncIciFinder;
import it.escsolution.escwebgis.cncici.logic.CncIciLogic;




import java.io.IOException;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class CncIciServlet extends EscServlet implements EscService
{

	private String recordScelto;

	private CncIciLogic logic = null;

	private String localDataSource = "jdbc/Diogene_DS";
	public static final String NOMEFINDER = "FINDER50";

	private CncIciFinder finder = null;

	PulsantiNavigazione nav =  new PulsantiNavigazione();

	public void EseguiServizio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		/*
		 * ad ogni uc corrisponde una funzionalit diversa
		 */
		// super.init(getServletConfig());
		// String aaa = getServletConfig().getServletName();
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
				this.leggiCrossLink(request);
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
		
		/*
		 *
		CONT.CODICE_FISCALE,
        CONT.COGNOME_DENOM COGNOME_CONTRIBUENTE,
        CONT.NOME,
        CONT.NAS_DATA,
		 * */
		elementoFiltro.setLabel("Contribuente");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Codice Fiscale");
		elementoFiltro.setAttributeName("CONT.CODICE_FISCALE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("CONT.CODICE_FISCALE");
		listaElementiFiltro.add(elementoFiltro);
				
		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Cognome");
		elementoFiltro.setAttributeName("CONT.COGNOME_DENOM");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("CONT.COGNOME_DENOM");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Nome");
		elementoFiltro.setAttributeName("CONT.NOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("CONT.NOME");
		listaElementiFiltro.add(elementoFiltro);
		
		// se si vuole la data di nascita
		//mettere a posto la data in formato data quando prende re
		/*elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Nascita");
		elementoFiltro.setAttributeName("CONT.NAS_DATA");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("CONT.NAS_DATA");
		listaElementiFiltro.add(elementoFiltro);*/		
		
		                                                                                                                                                                                                         

		/*-----------------------------------------------------------------

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Denunciante ");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		

		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Cognome");
		elementoFiltro.setAttributeName("DEN.COGNOME_DENOM");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("DEN.COGNOME_DENOM");
		listaElementiFiltro.add(elementoFiltro);
		elementoFiltro = new EscElementoFiltro();
		
		elementoFiltro.setLabel("Codice Fiscale");
		elementoFiltro.setAttributeName("den.CODICE_FISCALE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("den.CODICE_FISCALE");
		listaElementiFiltro.add(elementoFiltro);		
		
		
		-----------------------------------------------------------------*/
		/*
		 * IMM.FOGLIO,
	   IMM.NUMERO,
	   IMM.SUBALTERNO,
	   IMM.INDIRIZZO
		 * */
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Denunciante ");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		

		elementoFiltro = new EscElementoFiltro();				
		elementoFiltro.setLabel("Foglio");
		elementoFiltro.setAttributeName("IMM.FOGLIO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("IMM.FOGLIO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();		
		elementoFiltro.setLabel("Particella");
		elementoFiltro.setAttributeName("IMM.NUMERO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("IMM.NUMERO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();	
		elementoFiltro.setLabel("Sub.");
		elementoFiltro.setAttributeName("IMM.SUBALTERNO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("IMM.SUBALTERNO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();	
		elementoFiltro.setLabel("Indirizzo");
		elementoFiltro.setAttributeName("IMM.INDIRIZZO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("IMM.INDIRIZZO");
		listaElementiFiltro.add(elementoFiltro);			
		
		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		//chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request, response, "cncici/cnciciFrame.jsp", nav);

	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER) != null)
		{
			if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new CncIciFinder().getClass())
			{
				finder = (CncIciFinder) sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (CncIciFinder) gestireMultiPagina(finder, request);

		CncIciLogic logic = new CncIciLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaCncIci(vettoreRicerca, finder);

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista

		Vector vct_lista = (Vector) ht.get(CncIciLogic.LISTA_CNCICI);
		finder = (CncIciFinder) ht.get("FINDER");
		sessione.setAttribute(CncIciLogic.LISTA_CNCICI, vct_lista);
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
		this.chiamaPagina(request, response, "cncici/cnciciFrame.jsp", nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		String azione = "";
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
		
		CncIciFinder finder = null;
		// boolean chiamataEsterna = false;
		if (sessione.getAttribute(NOMEFINDER) != null)
		{
			if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new CncIciFinder().getClass())
			{
				finder = (CncIciFinder) sessione.getAttribute(NOMEFINDER);
			}
		}

		if (request.getParameter("AZIONE") != null)
			azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request, response, azione, finder, sessione, CncIciLogic.LISTA_CNCICI, (Vector) sessione.getAttribute(CncIciLogic.LISTA_CNCICI), NOMEFINDER);
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
		CncIciLogic logic = new CncIciLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioCncIci(oggettoSel);
		CncIci cncici = (CncIci) ht.get(CncIciLogic.CNCICI);
		sessione.setAttribute(CncIciLogic.CNCICI, cncici);
		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		

		this.chiamaPagina(request, response, "cncici/cnciciFrame.jsp", nav);		
	}


	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception
	{
		finder = (CncIciFinder) finder2;
		CncIciLogic logic = new CncIciLogic(this.getEnvUtente(request));
		return logic.mCaricareListaCncIci(this.vettoreRicerca, finder);
	}

	public EscFinder getNewFinder()
	{
		return new CncIciFinder();
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo)
	{
		return ((CncIci) listaOggetti.get(recordSuccessivo)).getChiave();
	}

	public String getTema()
	{
		return "Tributi";
	}

	public String getTabellaPerCrossLink()
	{
		return null;
	}
	public String getLocalDataSource() {
		return localDataSource;
	}

}
