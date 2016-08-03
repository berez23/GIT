/*
 * Created on 2-lug-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.tributi.servlet;

import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.tributi.bean.Versamenti;
import it.escsolution.escwebgis.tributi.bean.VersamentiFinder;
import it.escsolution.escwebgis.tributi.logic.VersamentiLogic;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Administrator To change the template for this generated type comment
 *         go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */
public class VersamentiServlet extends EscServlet implements EscService
{

	PulsantiNavigazione nav = new PulsantiNavigazione();

	private String recordScelto;

	private VersamentiLogic logic = null;

	private final String NOMEFINDER = "FINDER26";

	private VersamentiFinder finder = null;

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
				if (request.getParameter("POPUP") != null && new Boolean(request.getParameter("POPUP")).booleanValue()) {
					mCaricareListaPopup(request, response, false);
				}else{					
					mCaricareLista(request, response, false);
					this.leggiCrossLink(request);
				}				
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

		HttpSession sessione = request.getSession();

		Vector listaElementiFiltro = new Vector();
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=", "uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like", "contiene"));


		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=","uguale"));

		/** Comune */
		Vector vctComuni = new ComuniLogic(this.getEnvUtente(request)).getListaComuniUtente(request.getUserPrincipal().getName());
		/***/





		// costruisce il vettore di elementi per la pagina di ricerca

		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Comune");
		elementoFiltro.setAttributeName("COMUNE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vctComuni);
		elementoFiltro.setCampoFiltrato("ver.CODENT");
		listaElementiFiltro.add(elementoFiltro);


		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("CODICEFISCALE");
		elementoFiltro.setAttributeName("CODICEFISCALE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("CODFISC");
		listaElementiFiltro.add(elementoFiltro);

		sessione.setAttribute("LISTA_RICERCA", listaElementiFiltro);
		sessione.setAttribute("TITOLO", Tema.getNomeFiltro(uc));
		
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request, response, "versamenti/versFrame.jsp", nav);

	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response, boolean notListaPrincipale)
			throws Exception
	{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER) != null)
		{
			if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new VersamentiFinder().getClass())
			{
				finder = (VersamentiFinder) sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (VersamentiFinder) gestireMultiPagina(finder, request);

		VersamentiLogic logic = new VersamentiLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaVersamenti(vettoreRicerca, finder);

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista

		Vector vct_lista_Versamenti = (Vector) ht.get("LISTA_VERSAMENTI");
		finder = (VersamentiFinder) ht.get("FINDER");



		Versamenti vers = new Versamenti();
		if (vct_lista_Versamenti.size() > 0)
			vers=(Versamenti)vct_lista_Versamenti.get(0);

		sessione.setAttribute("VERSAMENTI",vers);





		sessione.setAttribute("LISTA_VERSAMENTI", vct_lista_Versamenti);

		if (!notListaPrincipale)
		{
			finder = (VersamentiFinder) ht.get("FINDER");
			sessione.setAttribute(NOMEFINDER, finder);
		}

		//if (chiamataEsterna)
			nav.chiamataEsternaLista();
		//else
		//	nav.chiamataInternaLista();

		this.chiamaPagina(request, response, "versamenti/versFrame.jsp", nav);
	}
	
	private void mCaricareListaPopup(HttpServletRequest request, HttpServletResponse response, boolean notListaPrincipale)
		throws Exception
	{
		HttpSession sessione = request.getSession();
		VersamentiLogic logic = new VersamentiLogic(this.getEnvUtente(request));	
		Vector vct_lista_Versamenti = logic.mCaricareListaVersamentiPopup(vettoreRicerca, finder);		
		sessione.setAttribute("LISTA_VERSAMENTI_POPUP", vct_lista_Versamenti);		
		nav.chiamataEsternaLista();
		this.chiamaPagina(request, response, "versamenti/versListPopup.jsp", nav);
	}

	public EscFinder getNewFinder()
	{
		return new VersamentiFinder();
	}

	public String getTema()
	{
		return "Tributi";
	}
	public String getTabellaPerCrossLink() {
		return "SIT_T_VERSAMENTI";
	}
}
