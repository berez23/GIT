 /*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.redditi2004.servlet;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.redditi2004.bean.Redditi;
import it.escsolution.escwebgis.redditi2004.bean.RedditiFinder;
import it.escsolution.escwebgis.redditi2004.logic.Redditi2004Logic;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Redditi2004Servlet extends EscServlet implements EscService {

	private String recordScelto;
	private Redditi2004Logic logic = null;
	private final String NOMEFINDER = "FINDER48";
	private RedditiFinder finder = null;
	PulsantiNavigazione nav;

	public void EseguiServizio(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		super.EseguiServizio(request,response);
		try
		{
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
					 mCaricareDettaglio(request,response);
					 this.leggiCrossLink(request);
					 break;
				 case 33:
					 // ho cliccato su un elemento --> visualizzo il dettaglio
					 mCaricareDettaglio(request,response);
					 this.leggiCrossLink(request);
					 break;
			}
		}
		catch(it.escsolution.escwebgis.common.DiogeneException de)
		{
			throw de;
		}
		catch(Exception ex)
		{
			log.error(ex.getMessage(),ex);
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

		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=", "uguale"));

		// costruisce il vettore di elementi per la pagina di ricerca
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();

		//cf
		elementoFiltro.setLabel("Codice Fiscale");
		elementoFiltro.setAttributeName("dic_cf");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("dic_cf");
		listaElementiFiltro.add(elementoFiltro);

		//cognome
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cognome");
		elementoFiltro.setAttributeName("dic_cogn");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("dic_cogn");
		listaElementiFiltro.add(elementoFiltro);

		//Nome
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Nome");
		elementoFiltro.setAttributeName("dic_nome");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("dic_nome");
		listaElementiFiltro.add(elementoFiltro);



		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		

		//chiamare la pagina di ricerca
		nav = new PulsantiNavigazione();
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"redditi/red2004Frame.jsp", nav);
	}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null)
		{
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new RedditiFinder().getClass())
			{
				finder = (RedditiFinder)sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (RedditiFinder)gestireMultiPagina(finder,request);

		Redditi2004Logic logic = new Redditi2004Logic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareLista(vettoreRicerca,finder);

	    Vector vct_lista= (Vector)ht.get(Redditi2004Logic.LISTA);
		finder = (RedditiFinder)ht.get(Redditi2004Logic.FINDER);
		sessione.setAttribute(Redditi2004Logic.LISTA,vct_lista);
		sessione.setAttribute(NOMEFINDER,finder);

		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
		{
			nav.chiamataEsternaLista();
			nav.setExt("1");
			nav.setPrimo(true);
		}
		else
			nav.chiamataInternaLista();

		this.chiamaPagina(request,response,"redditi/red2004Frame.jsp",nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		String azione = "";
		HttpSession sessione = request.getSession();
		RedditiFinder finder = null;
		if (sessione.getAttribute(NOMEFINDER) !=null)
		{
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new RedditiFinder().getClass())
			{
				finder = (RedditiFinder)sessione.getAttribute(NOMEFINDER);
			}
		}

		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request,response,azione,finder,sessione,Redditi2004Logic.LISTA,(Vector)sessione.getAttribute(Redditi2004Logic.LISTA),NOMEFINDER);

		if (azione.equals(""))
		{
			oggettoSel="";recordScelto="";
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
		Redditi2004Logic logic = new Redditi2004Logic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglio(oggettoSel);

		Redditi red = (Redditi)ht.get(Redditi2004Logic.REDDITI);
		Vector v = (Vector)ht.get(Redditi2004Logic.ALTRIREDDITI);
		sessione.setAttribute(Redditi2004Logic.REDDITI,red);
		sessione.setAttribute(Redditi2004Logic.ALTRIREDDITI,v);

		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		this.chiamaPagina(request,response,"redditi/red2004Frame.jsp", nav);
	}


	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (RedditiFinder)finder2;
		Redditi2004Logic logic = new Redditi2004Logic(this.getEnvUtente(request));
		return logic.mCaricareLista(this.vettoreRicerca, finder);
	}


	public EscFinder getNewFinder()
	{
		return new RedditiFinder();
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo)
	{
		Redditi def = (Redditi)listaOggetti.get(recordSuccessivo);
		String key = def.getChiave();
		return key;
	}

	public String getTema()
	{
		return "Redditi";
	}

	public String getTabellaPerCrossLink()
	{
		return "RED_DICHSINT_A2004M3_P0021";
	}

}



