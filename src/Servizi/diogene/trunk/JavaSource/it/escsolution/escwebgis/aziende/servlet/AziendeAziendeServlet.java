package it.escsolution.escwebgis.aziende.servlet;

import it.escsolution.escwebgis.aziende.bean.Azienda;
import it.escsolution.escwebgis.aziende.bean.AziendaFinder;
import it.escsolution.escwebgis.aziende.logic.AziendeAziendeLogic;
import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscObject;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Giulio Quaresima - WebRed
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AziendeAziendeServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private AziendeAziendeLogic logic = null;
	public static final String NOMEFINDER = "FINDER16";
	private AziendaFinder finder = null;

	public void EseguiServizio(
		HttpServletRequest request,HttpServletResponse response)
				throws ServletException, IOException {
		/*
		 * ad ogni uc corrisponde una funzionalit diversa
		 *
		 */
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		super.EseguiServizio(request,response);
		try {
			 switch (st) {
				 case 1:
					 // carico la form di ricerca
					 pulireSessione(request);
					 mCaricareFormRicerca(request,response);
					 break;
				 case 2:
					 // vengo dalla pagina di ricerca --> predispongo la lista e la passo alla pagina di lista
					 //pulireSessione(request);
					 mCaricareLista(request,response,false);
					 break;
				 case 3:
					 // ho cliccato su un elemento --> visualizzo il dettaglio
					 mCaricareDettaglio(request,response);
					 //this.leggiCrossLink(request);
					 break;
				 case 4:
					 mCaricareListaUnitaLocali(request,response);
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
		catch(Exception ex){
			log.error(ex.getMessage(),ex);
		}
	}

	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		HttpSession sessione = request.getSession();

		Vector listaElementiFiltro = new Vector();
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=","uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like","contiene"));
		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=","uguale"));
		Vector operatoriNumerici = new Vector();
		operatoriNumerici.add(new EscOperatoreFiltro("=","="));

		//Lista comuni
		Vector vctComuni = new ComuniLogic(this.getEnvUtente(request)).getListaComuniUtente(request.getUserPrincipal().getName());
		//
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Comune");
		elementoFiltro.setAttributeName("CODENTE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vctComuni);
		elementoFiltro.setCampoFiltrato("CODENTE");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Codice Fiscale");
		elementoFiltro.setAttributeName("CODICEFISCALE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("CODICEFISCALE");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Ragione Sociale");
		elementoFiltro.setAttributeName("RAGIONESOCIALE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("RAGIONESOCIALE");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("NREA");
		elementoFiltro.setAttributeName("NREA");
		elementoFiltro.setTipo("N");
		elementoFiltro.setCampoJS("numeroIntero");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("NREA");
		listaElementiFiltro.add(elementoFiltro);

		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		//chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"azienda/azFrame.jsp", nav);
	}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response,boolean notListaPrincipale)throws Exception{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new AziendaFinder().getClass()){
				finder = (AziendaFinder)sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}


		finder = (AziendaFinder) gestireMultiPagina(finder,request);

		AziendeAziendeLogic logic = new AziendeAziendeLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaAziende(vettoreRicerca, finder);

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista

		Vector vct_lista_aziende = (Vector) ht.get(AziendeAziendeLogic.LISTA_AZIENDE);
		finder = (AziendaFinder)ht.get(AziendeAziendeLogic.FINDER);

		sessione.setAttribute(AziendeAziendeLogic.LISTA_AZIENDE, vct_lista_aziende);
		sessione.setAttribute(NOMEFINDER, finder);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();

		this.chiamaPagina(request,response,"azienda/azFrame.jsp", nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception{
		// prendere dalla request il parametrio identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio

		String azione = "";
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
		
		AziendaFinder finder = null;
		//boolean chiamataEsterna = false;
		if (sessione.getAttribute(NOMEFINDER) !=null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new AziendaFinder().getClass()){
				finder = (AziendaFinder)sessione.getAttribute(NOMEFINDER);
			}
		}
		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request, response, azione, finder, sessione, AziendeAziendeLogic.LISTA_AZIENDE, (Vector) sessione.getAttribute(AziendeAziendeLogic.LISTA_AZIENDE), NOMEFINDER);
		if (azione.equals("")){
			oggettoSel="";recordScelto="";
			sessione.removeAttribute("BACK_JS_COUNT");
			sessione.removeAttribute("BACK_RECORD_ENABLE");

			if (request.getParameter("OGGETTO_SEL")!= null){
				oggettoSel = request.getParameter("OGGETTO_SEL");
			}
			if (request.getParameter("RECORD_SEL")!= null){
				recordScelto = request.getParameter("RECORD_SEL");
				if (finder!=null)
					finder.setRecordAttuale(new Long(recordScelto).longValue());
				}
			}

		// carica il dettaglio
		AziendeAziendeLogic logic = new AziendeAziendeLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioAzienda(oggettoSel);

		Azienda az = (Azienda)ht.get(AziendeAziendeLogic.DETTAGLIO_AZIENDA);
		sessione.setAttribute(AziendeAziendeLogic.DETTAGLIO_AZIENDA, az);

		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		this.chiamaPagina(request,response,"azienda/azFrame.jsp", nav);
	}

	private void mCaricareListaUnitaLocali(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		HttpSession sessione = request.getSession();

		if (request.getParameter("OGGETTO_SEL")!= null)
		{
			if (sessione.getAttribute(NOMEFINDER)!= null){
				if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new AziendaFinder().getClass()){
					finder = (AziendaFinder)sessione.getAttribute(NOMEFINDER);
				}
				else
					finder = null;
			}

			finder = (AziendaFinder) gestireMultiPagina(finder,request);

			AziendeAziendeLogic logic = new AziendeAziendeLogic(this.getEnvUtente(request));
			String chiave = (String) request.getParameter("OGGETTO_SEL");
			Hashtable ht = logic.mCaricareListaUnitaLocali(chiave);

			// eseguire la query e caricare il vettore con i risultati
			// chiamare la pagina di lista
			ArrayList listaUL = (ArrayList) ht.get(AziendeAziendeLogic.LISTA_UNITA_LOCALI);

			sessione.setAttribute(AziendeAziendeLogic.LISTA_UNITA_LOCALI, listaUL);
			sessione.setAttribute(NOMEFINDER, finder);

			nav.chiamataInternaLista();

			this.chiamaPagina(request,response,"azienda/azFrame.jsp", nav);
		}
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
		return ((EscObject) listaOggetti.get(recordSuccessivo)).getChiave();
	}
	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception {

		AziendeAziendeLogic logic = new AziendeAziendeLogic(this.getEnvUtente(request));
		return logic.mCaricareListaAziende(this.vettoreRicerca, (AziendaFinder) finder2);
	}
	public String getTabellaPerCrossLink() {
		return "SIT_MI_AZIENDE";
	}
	public EscFinder getNewFinder() {
		return new AziendaFinder();
	}
	public String getTema() {
		return "Camera di Commercio";
	}
}

