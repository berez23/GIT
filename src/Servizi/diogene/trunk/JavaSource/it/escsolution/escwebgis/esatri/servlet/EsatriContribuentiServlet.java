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
import it.escsolution.escwebgis.esatri.bean.Contribuente;
import it.escsolution.escwebgis.esatri.bean.ContribuenteFinder;
import it.escsolution.escwebgis.esatri.bean.TipoRiscossione;
import it.escsolution.escwebgis.esatri.logic.EsatriContribuentiLogic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class EsatriContribuentiServlet extends EscServlet implements EscService {

	private String recordScelto;
	private EsatriContribuentiLogic logic = null;
	private final String NOMEFINDER = "FINDER38";
	private ContribuenteFinder finder = null;
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

		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=","uguale"));

		Vector vct_tipo = new Vector();
		vct_tipo.add(new TipoRiscossione("","Tutti"));
		vct_tipo.add(new TipoRiscossione("CONTABILE","Contabile"));
		vct_tipo.add(new TipoRiscossione("VIOLAZIONE","Violazione"));



		// costruisce il vettore di elementi per la pagina di ricerca
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();

		//CF o PIVA
		elementoFiltro.setLabel("CodiceFiscale / Partita Iva (Es. P.IVA:00012345678)");
		elementoFiltro.setAttributeName("CF");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("CODICE_FISCALE_CONTRIBUENTE");
		listaElementiFiltro.add(elementoFiltro);

		//TIPO RISCOSSIONE
		/*elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Tipo Riscossione");
		elementoFiltro.setAttributeName("TIPORIS");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vct_tipo);
		elementoFiltro.setCampoFiltrato("TIPO");
		listaElementiFiltro.add(elementoFiltro);
		*/
		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		//chiamare la pagina di ricerca
		nav = new PulsantiNavigazione();
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"esatri/conFrame.jsp", nav);
	}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null)
		{
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new ContribuenteFinder().getClass())
			{
				finder = (ContribuenteFinder)sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (ContribuenteFinder)gestireMultiPagina(finder,request);

		EsatriContribuentiLogic logic = new EsatriContribuentiLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaContribuenti(vettoreRicerca,finder);

	    Vector vct_lista= (Vector)ht.get(EsatriContribuentiLogic.LISTA);
		finder = (ContribuenteFinder)ht.get(EsatriContribuentiLogic.FINDER);
		sessione.setAttribute(EsatriContribuentiLogic.LISTA,vct_lista);
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

		this.chiamaPagina(request,response,"esatri/conFrame.jsp",nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		// prendere dalla request il parametrio identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio
		String azione = "";
		HttpSession sessione = request.getSession();
		ContribuenteFinder finder = null;
		if (sessione.getAttribute(NOMEFINDER) !=null)
		{
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new ContribuenteFinder().getClass())
			{
				finder = (ContribuenteFinder)sessione.getAttribute(NOMEFINDER);
			}
		}

		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request,response,azione,finder,sessione,EsatriContribuentiLogic.LISTA,(Vector)sessione.getAttribute(EsatriContribuentiLogic.LISTA),NOMEFINDER);

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
		EsatriContribuentiLogic logic = new EsatriContribuentiLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglio(oggettoSel);

		Contribuente con = (Contribuente)ht.get(EsatriContribuentiLogic.ESATRI_CONTRIBUENTI);
		sessione.setAttribute(EsatriContribuentiLogic.ESATRI_CONTRIBUENTI,con);

		ArrayList listaRisCon = (ArrayList) ht.get(EsatriContribuentiLogic.LISTA_RIS_CON);
		sessione.setAttribute(EsatriContribuentiLogic.LISTA_RIS_CON, listaRisCon);
		ArrayList listaRisVio = (ArrayList) ht.get(EsatriContribuentiLogic.LISTA_RIS_VIO);
		sessione.setAttribute(EsatriContribuentiLogic.LISTA_RIS_VIO, listaRisVio);


		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		this.chiamaPagina(request,response,"esatri/conFrame.jsp", nav);
	}


	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (ContribuenteFinder)finder2;
		EsatriContribuentiLogic logic = new EsatriContribuentiLogic(this.getEnvUtente(request));
		return logic.mCaricareListaContribuenti(this.vettoreRicerca, finder);
	}


	public EscFinder getNewFinder()
	{
		return new ContribuenteFinder();
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo)
	{
		Contribuente def = (Contribuente)listaOggetti.get(recordSuccessivo);
		String key = def.getChiave();
		return key;
	}

	public String getTema()
	{
		return "Esatri";
	}

	public String getTabellaPerCrossLink()
	{
		return "MILANO.MI_ESATRI_RIS_CON";
	}

}



