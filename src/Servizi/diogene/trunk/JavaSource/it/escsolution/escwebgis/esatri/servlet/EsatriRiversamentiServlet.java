 /*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.esatri.servlet;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.esatri.bean.Riversamento;
import it.escsolution.escwebgis.esatri.bean.RiversamentoFinder;
import it.escsolution.escwebgis.esatri.logic.EsatriRiversamentiLogic;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class EsatriRiversamentiServlet extends EscServlet implements EscService {

	private String recordScelto;
	private EsatriRiversamentiLogic logic = null;
	private final String NOMEFINDER = "FINDER37";
	private RiversamentoFinder finder = null;
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
					 //this.leggiCrossLink(request);
					 break;
				 case 33:
					 // ho cliccato su un elemento --> visualizzo il dettaglio
					 mCaricareDettaglio(request,response);
					 //this.leggiCrossLink(request);
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

		Vector operatoriNumerici = new Vector();
		operatoriNumerici.add(new EscOperatoreFiltro("=","="));
		operatoriNumerici.add(new EscOperatoreFiltro("<>","<>"));
		operatoriNumerici.add(new EscOperatoreFiltro(">",">"));
		operatoriNumerici.add(new EscOperatoreFiltro("<","<"));

		// costruisce il vettore di elementi per la pagina di ricerca
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();

		//ANNO RISCOSSIONE
		elementoFiltro.setLabel("Anno Riscossione");
		elementoFiltro.setAttributeName("ANNO_RISC");
		elementoFiltro.setTipo("N");
		elementoFiltro.setCampoJS("numeroIntero");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("MI_ESATRI_ICI0.ANNO_RIF_RISCOSSIONI");
		listaElementiFiltro.add(elementoFiltro);

		//DATA SCADENZA
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Scadenza");
		elementoFiltro.setAttributeName("DATA_SCADENZA");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("to_date(MI_ESATRI_ICI0.DATA_SCADENZA,'ddmmyyyy')");
		listaElementiFiltro.add(elementoFiltro);

		//PROGRESSIVO
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Progressivo Invio");
		elementoFiltro.setAttributeName("PROGRESSIVO_INVIO");
		elementoFiltro.setTipo("N");
		elementoFiltro.setCampoJS("numeroIntero");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("MI_ESATRI_ICI0.PROGRESSIVO_INVIO");
		listaElementiFiltro.add(elementoFiltro);

		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		

		//chiamare la pagina di ricerca
		nav = new PulsantiNavigazione();
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"esatri/rivFrame.jsp", nav);
	}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null)
		{
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new RiversamentoFinder().getClass())
			{
				finder = (RiversamentoFinder)sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (RiversamentoFinder)gestireMultiPagina(finder,request);

		EsatriRiversamentiLogic logic = new EsatriRiversamentiLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaRiversamenti(vettoreRicerca,finder);

	    Vector vct_lista= (Vector)ht.get(EsatriRiversamentiLogic.LISTA);
		finder = (RiversamentoFinder)ht.get(EsatriRiversamentiLogic.FINDER);
		sessione.setAttribute(EsatriRiversamentiLogic.LISTA,vct_lista);
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

		this.chiamaPagina(request,response,"esatri/rivFrame.jsp",nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		// prendere dalla request il parametrio identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio
		String azione = "";
		HttpSession sessione = request.getSession();
		RiversamentoFinder finder = null;
		if (sessione.getAttribute(NOMEFINDER) !=null)
		{
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new RiversamentoFinder().getClass())
			{
				finder = (RiversamentoFinder)sessione.getAttribute(NOMEFINDER);
			}
		}

		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request,response,azione,finder,sessione,EsatriRiversamentiLogic.LISTA,(Vector)sessione.getAttribute(EsatriRiversamentiLogic.LISTA),NOMEFINDER);

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
		EsatriRiversamentiLogic logic = new EsatriRiversamentiLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglio(oggettoSel);

		Riversamento riv = (Riversamento)ht.get(EsatriRiversamentiLogic.ESATRI_RIVERSAMENTO);
		sessione.setAttribute(EsatriRiversamentiLogic.ESATRI_RIVERSAMENTO,riv);


		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		this.chiamaPagina(request,response,"esatri/rivFrame.jsp", nav);
	}


	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (RiversamentoFinder)finder2;
		EsatriRiversamentiLogic logic = new EsatriRiversamentiLogic(this.getEnvUtente(request));
		return logic.mCaricareListaRiversamenti(this.vettoreRicerca, finder);
	}


	public EscFinder getNewFinder()
	{
		return new RiversamentoFinder();
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo)
	{
		Riversamento def = (Riversamento)listaOggetti.get(recordSuccessivo);
		String key = def.getChiave();
		return key;
	}

	public String getTema()
	{
		return "Esatri";
	}

	public String getTabellaPerCrossLink()
	{
		return "MILANO.MI_ESATRI_RIV";
	}

}



