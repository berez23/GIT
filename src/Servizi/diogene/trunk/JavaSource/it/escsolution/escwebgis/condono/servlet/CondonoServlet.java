 /*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.condono.servlet;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.condono.bean.Condono;
import it.escsolution.escwebgis.condono.bean.CondonoFinder;
import it.escsolution.escwebgis.condono.logic.CondonoLogic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CondonoServlet extends EscServlet implements EscService {

	private String recordScelto;
	private CondonoLogic logic = null;
	private final String NOMEFINDER = "FINDER39";
	private CondonoFinder finder = null;
	PulsantiNavigazione nav;

	public void EseguiServizio(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		super.EseguiServizio(request,response);
		try
		{
			if (request.getParameter("popupACondono") != null) {
				mPopupACondono(request, response);
			} else {
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
					// this.leggiCrossLink(request);
					 break;
				 case 33:
					 // ho cliccato su un elemento --> visualizzo il dettaglio
					 mCaricareDettaglio(request,response);
					// this.leggiCrossLink(request);
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

		//ESIBENTE
		elementoFiltro.setLabel("Esibente");
		elementoFiltro.setAttributeName("ESIBENTE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("DECODE(UPPER(A.ESIBENTE), 'NULL', NULL, A.ESIBENTE)");
		listaElementiFiltro.add(elementoFiltro);

		//CF o PIVA ESIBENTE
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Codice Fiscale/P.Iva Esibente");
		elementoFiltro.setAttributeName("CFPIESIBENTE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("DECODE(UPPER(A.CFPIESIBENTE), 'NULL', NULL, A.CFPIESIBENTE)");
		listaElementiFiltro.add(elementoFiltro);

		//INDIRIZZO CONDONO
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Indirizzo Condono");
		elementoFiltro.setAttributeName("DESCR_VIA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("NVL(B.DESCRIZIONE, C.PREFISSO || C.NOME)");
		listaElementiFiltro.add(elementoFiltro);

		//CIVICO CONDONO
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Civico Condono");
		elementoFiltro.setAttributeName("NCIVICO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("DECODE(UPPER(A.NCIVICO), 'NULL', NULL, '0', NULL, A.NCIVICO)");
		listaElementiFiltro.add(elementoFiltro);

		//BARRATO CONDONO
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Barrato Condono");
		elementoFiltro.setAttributeName("BARRANUMCIVICO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("DECODE(UPPER(A.BARRANUMCIVICO), 'NULL', NULL, A.BARRANUMCIVICO)");
		listaElementiFiltro.add(elementoFiltro);

		//DESCR DATI CATASTALI
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Descr. Dati Catasto");
		elementoFiltro.setAttributeName("UTE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("DECODE(UPPER(A.UTE), 'NULL', NULL, A.UTE)");
		listaElementiFiltro.add(elementoFiltro);


		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		

		//chiamare la pagina di ricerca
		nav = new PulsantiNavigazione();
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"condono/conFrame.jsp", nav);
	}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null)
		{
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new CondonoFinder().getClass())
			{
				finder = (CondonoFinder)sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (CondonoFinder)gestireMultiPagina(finder,request);

		CondonoLogic logic = new CondonoLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareLista(vettoreRicerca,finder);

	    Vector vct_lista= (Vector)ht.get(CondonoLogic.LISTA);
		finder = (CondonoFinder)ht.get(CondonoLogic.FINDER);
		sessione.setAttribute(CondonoLogic.LISTA,vct_lista);
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

		this.chiamaPagina(request,response,"condono/conFrame.jsp",nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		String azione = "";
		HttpSession sessione = request.getSession();
		CondonoFinder finder = null;
		if (sessione.getAttribute(NOMEFINDER) !=null)
		{
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new CondonoFinder().getClass())
			{
				finder = (CondonoFinder)sessione.getAttribute(NOMEFINDER);
			}
		}

		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request,response,azione,finder,sessione,CondonoLogic.LISTA,(Vector)sessione.getAttribute(CondonoLogic.LISTA),NOMEFINDER);

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
		CondonoLogic logic = new CondonoLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglio(oggettoSel);

		Condono con = (Condono)ht.get(CondonoLogic.CONDONO);
		ArrayList stralci = (ArrayList)ht.get(CondonoLogic.STRALCI);
		ArrayList uius = (ArrayList)ht.get(CondonoLogic.UIU);
		sessione.setAttribute(CondonoLogic.CONDONO, con);
		sessione.setAttribute(CondonoLogic.STRALCI, stralci);
		sessione.setAttribute(CondonoLogic.UIU, uius);

		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		if (request.getParameter("popupACondonoDett") != null) {
			String foglio = request.getParameter("foglio");
			String mappale = request.getParameter("mappale");
			String sub = request.getParameter("sub");
			this.chiamaPagina(request, response, "condono/conDetail.jsp?popupACondonoDett=true&foglio=" + foglio +
												"&mappale=" + mappale + "&sub=" + sub, nav);
		} else {
			this.chiamaPagina(request,response,"condono/conFrame.jsp", nav);
		}
		
	}
	
	private void mPopupACondono(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		HttpSession sessione = request.getSession();
		
		String foglio = request.getParameter("foglio");
		String mappale = request.getParameter("mappale");
		String sub = request.getParameter("sub");
		
		finder = new CondonoFinder();
		
		CondonoLogic logic = new CondonoLogic(this.getEnvUtente(request));
		
		//in questo caso fa una ricerca particolare e crea una versione ridotta di vettoreRicerca, 
		//contenente solo i valori dei parametri foglio, mappale e sub
		vettoreRicerca = new Vector();
		vettoreRicerca.add("popupACondono");
		vettoreRicerca.add(foglio);
		vettoreRicerca.add(mappale);
		vettoreRicerca.add(sub);
		
		Hashtable ht = logic.mCaricareLista(vettoreRicerca,finder);

	    Vector vct_lista= (Vector)ht.get(CondonoLogic.LISTA);
		finder = (CondonoFinder)ht.get(CondonoLogic.FINDER);
		sessione.setAttribute(CondonoLogic.LISTA, vct_lista);
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

		this.chiamaPagina(request,response,"condono/conList.jsp?popupACondono=true&foglio=" + foglio +
							"&mappale=" + mappale + "&sub=" + sub, nav);

	}


	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (CondonoFinder)finder2;
		CondonoLogic logic = new CondonoLogic(this.getEnvUtente(request));
		return logic.mCaricareLista(this.vettoreRicerca, finder);
	}


	public EscFinder getNewFinder()
	{
		return new CondonoFinder();
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo)
	{
		Condono def = (Condono)listaOggetti.get(recordSuccessivo);
		String key = def.getChiave();
		return key;
	}

	public String getTema()
	{
		return "Condono";
	}

	public String getTabellaPerCrossLink()
	{
		return "MI_CONDONO";
	}

}



