package it.escsolution.escwebgis.impiantitermici.servlet;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.impiantitermici.bean.ImpTerFinder;
import it.escsolution.escwebgis.impiantitermici.logic.ImpTerLogic;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author dan
 *
 */
public class ImpTerServlet extends EscServlet implements EscService
{

	PulsantiNavigazione nav = new PulsantiNavigazione();

	private String recordScelto;

	private ImpTerLogic logic = null;

	private final String NOMEFINDER = "FINDER27";

	private ImpTerFinder finder = null;

	public void EseguiServizio(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		/*
		 * ad ogni uc corrisponde una funzionalità diversa
		 */
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
				mCaricareLista(request, response, false);
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

	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{

		// caricare dati che servono nella pagina di ricerca
		ImpTerLogic logic = new ImpTerLogic(this.getEnvUtente(request));
		HttpSession sessione = request.getSession();

		Vector listaElementiFiltro = new Vector();
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=", "uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like", "contiene"));

		// costruisce il vettore di elementi per la pagina di ricerca

		EscElementoFiltro elementoFiltro = new EscElementoFiltro();

		elementoFiltro.setLabel("OCCUPANTE");
		elementoFiltro.setAttributeName("OCCUPANTE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("OCCUPANTE");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("NOME VIA");
		elementoFiltro.setAttributeName("NOME_VIA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("NOME_VIA");
		listaElementiFiltro.add(elementoFiltro);
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("CIVICO");
		elementoFiltro.setAttributeName("CIVICO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("CIVICO");

		listaElementiFiltro.add(elementoFiltro);

		sessione.setAttribute("LISTA_RICERCA", listaElementiFiltro);
		sessione.setAttribute("TITOLO", Tema.getNomeFiltro(uc));
		
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request, response, "impiantitermici/impterFrame.jsp", nav);

	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response, boolean notListaPrincipale)
			throws Exception
	{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER) != null)
		{
			if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new ImpTerFinder().getClass())
			{
				finder = (ImpTerFinder) sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (ImpTerFinder) gestireMultiPagina(finder, request);

		ImpTerLogic logic = new ImpTerLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaImpTer(vettoreRicerca, finder);

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista

		Vector vct_lista_ImpTer = (Vector) ht.get("LISTA_IMPTER");
		finder = (ImpTerFinder) ht.get("FINDER");

		sessione.setAttribute("LISTA_IMPTER", vct_lista_ImpTer);

		if (!notListaPrincipale)
		{
			finder = (ImpTerFinder) ht.get("FINDER");
			sessione.setAttribute(NOMEFINDER, finder);
		}

		//if (chiamataEsterna)
			nav.chiamataEsternaLista();
		//else
		//	nav.chiamataInternaLista();

		this.chiamaPagina(request, response, "impiantitermici/impterFrame.jsp", nav);
	}

	public EscFinder getNewFinder()
	{
		return new ImpTerFinder();
	}

	public String getTema()
	{
		return "Tributi";
	}
}
