/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.enel.servlet;


import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.enel.bean.Enel;
import it.escsolution.escwebgis.enel.bean.EnelFinder;
import it.escsolution.escwebgis.enel.logic.EnelLogic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class EnelServlet extends EscServlet implements EscService
{

	private String recordScelto;

	private EnelLogic logic = null;

	// private AnagrafeFinder finder = null;
	public static final String NOMEFINDER = "FINDER32";

	private EnelFinder finder = null;

	PulsantiNavigazione nav;

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
				this.leggiCrossLink(request);
				break;
			case 4:
				// ho cliccato su un elemento --> visualizzo il dettaglio
				mCaricareListaSemUtenzeEnel(request,response);
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
		EnelLogic logic = new EnelLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDatiFormRicerca(request.getUserPrincipal().getName());
		HttpSession sessione = request.getSession();
		Vector vct_comuni = (Vector) ht.get("LISTA_COMUNI");

		Vector listaElementiFiltro = new Vector();
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=", "uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like", "contiene"));

		Vector listaElementiFiltroOrder = new Vector();
		Vector operatoriStringaOrder = new Vector();
		operatoriStringaOrder.add(new EscOperatoreFiltro("like", "contiene"));
		operatoriStringaOrder.add(new EscOperatoreFiltro("=", "uguale"));

		Vector operatoriNumerici = new Vector();
		operatoriNumerici.add(new EscOperatoreFiltro("=", "="));
		operatoriNumerici.add(new EscOperatoreFiltro("<>", "<>"));
		operatoriNumerici.add(new EscOperatoreFiltro(">", ">"));
		operatoriNumerici.add(new EscOperatoreFiltro("<", "<"));

		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=", "uguale"));

		// costruisce il vettore di elementi per la pagina di ricerca
		//costruisce il vettore di elementi per la pagina di ricerca
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Comune");
		elementoFiltro.setAttributeName("Comune");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setListaValori(vct_comuni);
		elementoFiltro.setCampoFiltrato("SIT_E_UTENTI.CODENT");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Nominativo");
		elementoFiltro.setAttributeName("NOMINATIVO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaOrder);
		elementoFiltro.setCampoFiltrato("UPPER(SIT_E_UTENTI.NOMINATIVO)");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cod.Fisc/P.Iva");
		elementoFiltro.setAttributeName("PIVA_CODFISC");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("UPPER(SIT_E_UTENTI.PIVA_CODFISC)");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Indirizzo");
		elementoFiltro.setAttributeName("INDIRIZZO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaOrder);
		elementoFiltro.setCampoFiltrato("UPPER(SIT_E_UTENZE.INDIRIZZO)");
		listaElementiFiltro.add(elementoFiltro);

		sessione.setAttribute("LISTA_RICERCA", listaElementiFiltro);
		sessione.setAttribute("TITOLO", Tema.getNomeFiltro(uc));
		

		// chiamare la pagina di ricerca
		nav = new PulsantiNavigazione();
		nav.chiamataRicerca();
		this.chiamaPagina(request, response, "enel/enelFrame.jsp", nav);

	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER) != null)
		{
			if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new EnelFinder().getClass())
			{
				finder = (EnelFinder) sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (EnelFinder) gestireMultiPagina(finder, request);

		EnelLogic logic = new EnelLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaEnel(vettoreRicerca, finder);

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista

		Vector vct_lista_enel = (Vector) ht.get(EnelLogic.LISTA_ENEL);
		finder = (EnelFinder) ht.get("FINDER");
		sessione.setAttribute(EnelLogic.LISTA_ENEL, vct_lista_enel);
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
		this.chiamaPagina(request, response, "enel/enelFrame.jsp", nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// prendere dalla request il parametrio identificativo dell'oggetto
		// cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio

		String azione = "";
		HttpSession sessione = request.getSession();
		EnelFinder finder = null;
		// boolean chiamataEsterna = false;
		if (sessione.getAttribute(NOMEFINDER) != null)
		{
			if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new EnelFinder().getClass())
			{
				finder = (EnelFinder) sessione.getAttribute(NOMEFINDER);
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
		EnelLogic logic = new EnelLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioEnel(oggettoSel);
		Enel enel = (Enel) ht.get(EnelLogic.ENEL);
		sessione.setAttribute(EnelLogic.ENEL, enel);
		ArrayList listaDettaglioEnel = (ArrayList) ht.get(EnelLogic.LISTA_DETTAGLIO_ENEL);
		sessione.setAttribute(EnelLogic.LISTA_DETTAGLIO_ENEL, listaDettaglioEnel);
		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		this.chiamaPagina(request, response, "enel/enelFrame.jsp", nav);

		// EscFinder escf = (EscFinder)sessione.getAttribute(NOMEFINDER);

	}
	private void mCaricareListaSemUtenzeEnel(HttpServletRequest request,HttpServletResponse response)
		throws Exception
	{
		/*
		 * carica la delle semetralità delle utenze
		 */
		HttpSession sessione = request.getSession();
		String chiave = request.getParameter("OGGETTO_SEL");
		EnelLogic logic = new EnelLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaSemestralitaUtenze(chiave);
		ArrayList listaSemestralita = (ArrayList) ht.get(EnelLogic.LISTA_SEMESTRALITA_UTENZE);
		sessione.setAttribute(EnelLogic.LISTA_SEMESTRALITA_UTENZE, listaSemestralita);
		nav.chiamataPart();
		this.chiamaPagina(request, response, "enel/enelFrame.jsp", nav);

	}
	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception
	{
		finder = (EnelFinder) finder2;
		EnelLogic logic = new EnelLogic(this.getEnvUtente(request));
		return logic.mCaricareListaEnel(this.vettoreRicerca, finder);
	}

	public EscFinder getNewFinder()
	{
		return new EnelFinder();
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo)
	{
		return ((Enel) listaOggetti.get(recordSuccessivo)).getChiave();
	}

	public String getTema()
	{
		return "Forniture Elettriche";
	}

	public String getTabellaPerCrossLink()
	{
		return "SIT_E_UTENTI";
	}

}
