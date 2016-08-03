/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.concessioni.servlet;


import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.concessioni.bean.Concessioni;
import it.escsolution.escwebgis.concessioni.bean.ConcessioniFinder;
import it.escsolution.escwebgis.concessioni.logic.ConcessioniLogic;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ConcessioniServlet extends EscServlet implements EscService
{

	private String recordScelto;

	private ConcessioniLogic logic = null;

	// private AnagrafeFinder finder = null;
	public static final String NOMEFINDER = "FINDER31";

	private ConcessioniFinder finder = null;

	PulsantiNavigazione nav;

	public void EseguiServizio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		/*
		 * ad ogni uc corrisponde una funzionalit diversa
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
				this.leggiCrossLink(request);
				break;
			/*
			 * case 4: // ho cliccato su un elemento --> visualizzo il dettaglio
			 * mCaricareDettaglio(request,response);
			 * this.leggiCrossLink(request); break; case 5: // ho cliccato su un
			 * elemento --> visualizzo il dettaglio
			 * mCaricareLista(request,response,true); break;
			 */
			case 33:
				 // ho cliccato su un elemento --> visualizzo il dettaglio						 
					 mCaricareDettaglio(request,response);
					 this.leggiCrossLink(request);						 
				 	 break;
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
		ConcessioniLogic logic = new ConcessioniLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDatiFormRicerca(request.getUserPrincipal().getName());
		HttpSession sessione = request.getSession();
		Vector vct_comuni = (Vector) ht.get("LISTA_COMUNI");
		Vector vct_tipo = (Vector) ht.get("LISTA_TIPI_CONC");
		request.setAttribute("LISTA_CATEGORIE",vct_tipo);
		sessione.setAttribute("LISTA_CATEGORIE",vct_tipo);


		Vector listaElementiFiltro = new Vector();
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=", "uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like", "contiene"));

		Vector operatoriNumerici = new Vector();
		operatoriNumerici.add(new EscOperatoreFiltro("=", "="));
		operatoriNumerici.add(new EscOperatoreFiltro("<>", "<>"));
		operatoriNumerici.add(new EscOperatoreFiltro(">", ">"));
		operatoriNumerici.add(new EscOperatoreFiltro("<", "<"));

		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=", "uguale"));

		Vector operatoriStringaRid2 = new Vector();
		operatoriStringaRid2.add(new EscOperatoreFiltro("like", "contiene"));
		operatoriStringaRid2.add(new EscOperatoreFiltro("=", "uguale"));

		// costruisce il vettore di elementi per la pagina di ricerca
		//costruisce il vettore di elementi per la pagina di ricerca
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Comune");
		elementoFiltro.setAttributeName("Comune");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setListaValori(vct_comuni);
		elementoFiltro.setCampoFiltrato("CODENTE");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Numero");
		elementoFiltro.setAttributeName("NUMERO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("NUMERO");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Tipo");
		elementoFiltro.setAttributeName("UPPER(TIPO)");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vct_tipo);
		elementoFiltro.setCampoFiltrato("UPPER(TIPO)");
		elementoFiltro.setMaxSizeCombo(new Integer(500));
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Richiedente");
		elementoFiltro.setAttributeName("RICHIEDENTE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");//todo
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("UPPER(RICHIEDENTE)");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Codice Fiscale");
		elementoFiltro.setAttributeName("CODFIS");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("UPPER(CODFIS)");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Indirizzo");
		elementoFiltro.setAttributeName("Indirizzo");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid2);
		elementoFiltro.setCampoFiltrato("INDIRIZZO_CONC");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Civico");
		elementoFiltro.setAttributeName("Civico");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("CIVICO");
		listaElementiFiltro.add(elementoFiltro);
		

		sessione.setAttribute("LISTA_RICERCA", listaElementiFiltro);
		sessione.setAttribute("TITOLO", Tema.getNomeFiltro(uc));
		

		// chiamare la pagina di ricerca
		nav = new PulsantiNavigazione();
		nav.chiamataRicerca();
		this.chiamaPagina(request, response, "concessioni/concessioniFrame.jsp", nav);

	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER) != null)
		{
			if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new ConcessioniFinder().getClass())
			{
				finder = (ConcessioniFinder) sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (ConcessioniFinder) gestireMultiPagina(finder, request);

		ConcessioniLogic logic = new ConcessioniLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaConcessioni(vettoreRicerca, finder);

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista

		Vector vct_lista_concessioni = (Vector) ht.get(ConcessioniLogic.LISTA_CONCESSIONI);
		finder = (ConcessioniFinder) ht.get("FINDER");
		sessione.setAttribute(ConcessioniLogic.LISTA_CONCESSIONI, vct_lista_concessioni);
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
		this.chiamaPagina(request, response, "concessioni/concessioniFrame.jsp", nav);
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
		
		ConcessioniFinder finder = null;
		// boolean chiamataEsterna = false;
		if (sessione.getAttribute(NOMEFINDER) != null)
		{
			if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new ConcessioniFinder().getClass())
			{
				finder = (ConcessioniFinder) sessione.getAttribute(NOMEFINDER);
			}
		}

		if (request.getParameter("AZIONE") != null)
			azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request, response, azione, finder, sessione, "LISTA_CONCESSIONI", (Vector) sessione.getAttribute("LISTA_CONCESSIONI"), NOMEFINDER);
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
		ConcessioniLogic logic = new ConcessioniLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioConcessioni(oggettoSel);
		Concessioni conc = (Concessioni) ht.get(ConcessioniLogic.CONCESSIONI);
		sessione.setAttribute(ConcessioniLogic.CONCESSIONI, conc);
		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		this.chiamaPagina(request, response, "concessioni/concessioniFrame.jsp", nav);

		// EscFinder escf = (EscFinder)sessione.getAttribute(NOMEFINDER);

	}


	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception
	{
		finder = (ConcessioniFinder) finder2;
		ConcessioniLogic logic = new ConcessioniLogic(this.getEnvUtente(request));
		return logic.mCaricareListaConcessioni(this.vettoreRicerca, finder);
	}

	public EscFinder getNewFinder()
	{
		return new ConcessioniFinder();
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo)
	{
		return ((Concessioni) listaOggetti.get(recordSuccessivo)).getChiave();
	}

	public String getTema()
	{
		return "Concessioni Edilizie";
	}

	public String getTabellaPerCrossLink()
	{
		return "SIT_CONCESSIONI_EDILIZIE";
	}

}
