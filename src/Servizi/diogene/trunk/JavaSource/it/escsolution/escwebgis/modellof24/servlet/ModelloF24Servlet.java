/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.modellof24.servlet;


import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.concessioni.bean.Tipo;
import it.escsolution.escwebgis.modellof24.bean.ModelloF24;
import it.escsolution.escwebgis.modellof24.bean.ModelloF24Finder;
import it.escsolution.escwebgis.modellof24.logic.ModelloF24Logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ModelloF24Servlet extends EscServlet implements EscService
{

	private String recordScelto;

	private ModelloF24Logic logic = null;

	// private AnagrafeFinder finder = null;
	public static final String NOMEFINDER = "FINDER33";

	private ModelloF24Finder finder = null;

	PulsantiNavigazione nav;

	public void EseguiServizio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		/*
		 * ad ogni uc corrisponde una funzionalità diversa
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
		ModelloF24Logic logic = new ModelloF24Logic(this.getEnvUtente(request));
		HttpSession sessione = request.getSession();
		Vector vct_sino = new Vector();
		vct_sino.add(new Tipo("",""));
		vct_sino.add(new Tipo("0","No"));
		vct_sino.add(new Tipo("1","Si"));
		request.setAttribute("LISTA_CATEGORIE",vct_sino);
		sessione.setAttribute("LISTA_CATEGORIE",vct_sino);


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

		//costruisce il vettore di elementi per la pagina di ricerca
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Fornitura");
		elementoFiltro.setAttributeName("G1_FORNITURA_DATA");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("G1_FORNITURA_DATA");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Ripartizione");
		elementoFiltro.setAttributeName("G1_RIPARTIZIONE_DATA");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("G1_RIPARTIZIONE_DATA");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Bonifico");
		elementoFiltro.setAttributeName("G1_BONIFICO_DATA");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("G1_BONIFICO_DATA");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cod.Ente");
		elementoFiltro.setAttributeName("G1_CD_ENTE_RENDICONTO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("G1_CD_ENTE_RENDICONTO");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Tipo.Ente");
		elementoFiltro.setAttributeName("G1_TP_ENTE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("G1_TP_ENTE");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("C.A.B.");
		elementoFiltro.setAttributeName("G1_CAB");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("G1_CAB");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cod.Fisc./Contribuente");
		elementoFiltro.setAttributeName("G1_CD_FISCALE_CONTRIBUENTE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("rtrim(G1_CD_FISCALE_CONTRIBUENTE)");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cod.Tributo");
		elementoFiltro.setAttributeName("G1_CD_TRIBUTO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("G1_CD_TRIBUTO");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Anno Rif.");
		elementoFiltro.setAttributeName("G1_ANNO_RIFERIMENTO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("G1_ANNO_RIFERIMENTO");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("ICI Acconto");
		elementoFiltro.setAttributeName("G1_ICI_ACCONTO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vct_sino);
		elementoFiltro.setCampoFiltrato("G1_ICI_ACCONTO");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("ICI Saldo");
		elementoFiltro.setAttributeName("G1_ICI_SALDO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vct_sino);
		elementoFiltro.setCampoFiltrato("G1_ICI_SALDO");
		listaElementiFiltro.add(elementoFiltro);

		sessione.setAttribute("LISTA_RICERCA", listaElementiFiltro);
		sessione.setAttribute("TITOLO", Tema.getNomeFiltro(uc));

		// chiamare la pagina di ricerca
		nav = new PulsantiNavigazione();
		nav.chiamataRicerca();
		this.chiamaPagina(request, response, "modellof24/modellof24Frame.jsp", nav);

	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER) != null)
		{
			if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new ModelloF24Finder().getClass())
			{
				finder = (ModelloF24Finder) sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (ModelloF24Finder) gestireMultiPagina(finder, request);

		ModelloF24Logic logic = new ModelloF24Logic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaModelliF24(vettoreRicerca, finder);

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista

		Vector vct_lista_modellif24 = (Vector) ht.get(ModelloF24Logic.LISTA_MODELLIF24);
		finder = (ModelloF24Finder) ht.get("FINDER");
		sessione.setAttribute(ModelloF24Logic.LISTA_MODELLIF24, vct_lista_modellif24);
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
		this.chiamaPagina(request, response, "modellof24/modellof24Frame.jsp", nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// prendere dalla request il parametrio identificativo dell'oggetto
		// cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio

		String azione = "";
		HttpSession sessione = request.getSession();
		ModelloF24Finder finder = null;
		// boolean chiamataEsterna = false;
		if (sessione.getAttribute(NOMEFINDER) != null)
		{
			if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new ModelloF24Finder().getClass())
			{
				finder = (ModelloF24Finder) sessione.getAttribute(NOMEFINDER);
			}
		}

		if (request.getParameter("AZIONE") != null)
			azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request, response, azione, finder, sessione, "LISTA_MODELLIF24", (Vector) sessione.getAttribute("LISTA_MODELLIF24"), NOMEFINDER);
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
		ModelloF24Logic logic = new ModelloF24Logic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioModelloF24(oggettoSel);
		ModelloF24 conc = (ModelloF24) ht.get(ModelloF24Logic.MODELLOF24);
		sessione.setAttribute(ModelloF24Logic.MODELLOF24, conc);
		ArrayList listaDettaglioModelli = (ArrayList) ht.get(ModelloF24Logic.LISTA_DETTAGLIO_MODELLOF24);
		sessione.setAttribute(ModelloF24Logic.LISTA_DETTAGLIO_MODELLOF24, listaDettaglioModelli);
		ArrayList listaDettaglioG2 = (ArrayList) ht.get(ModelloF24Logic.LISTA_DETTAGLIO_G2);
		sessione.setAttribute(ModelloF24Logic.LISTA_DETTAGLIO_G2, listaDettaglioG2);

		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		this.chiamaPagina(request, response, "modellof24/modellof24Frame.jsp", nav);

		// EscFinder escf = (EscFinder)sessione.getAttribute(NOMEFINDER);

	}


	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception
	{
		finder = (ModelloF24Finder) finder2;
		ModelloF24Logic logic = new ModelloF24Logic(this.getEnvUtente(request));
		return logic.mCaricareListaModelliF24(this.vettoreRicerca, finder);
	}

	public EscFinder getNewFinder()
	{
		return new ModelloF24Finder();
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo)
	{
		return ((ModelloF24) listaOggetti.get(recordSuccessivo)).getChiave();
	}

	public String getTema()
	{
		return "Tributi";
	}

	public String getTabellaPerCrossLink()
	{
		return "MI_F24_G1";
	}

}
