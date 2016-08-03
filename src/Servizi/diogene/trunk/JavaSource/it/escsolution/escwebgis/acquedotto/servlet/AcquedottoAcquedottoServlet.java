package it.escsolution.escwebgis.acquedotto.servlet;

import it.escsolution.escwebgis.acquedotto.bean.AcquedottoFinder;
import it.escsolution.escwebgis.acquedotto.logic.AcquedottoAcquedottoLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscObject;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;

import java.io.IOException;
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
public class AcquedottoAcquedottoServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private AcquedottoAcquedottoLogic logic = null;
	public static final String NOMEFINDER = "FINDER20";
	public static final String URL_FRAME = "acquedotto/acqFrame.jsp";
	private AcquedottoFinder finder = null;

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
					 //mCaricareListaUnitaLocali(request,response);
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
		AcquedottoAcquedottoLogic logic = new AcquedottoAcquedottoLogic(this.getEnvUtente(request));
		HttpSession sessione = request.getSession();

		Vector listaSedime = (Vector) ((Hashtable) logic.mCaricareDatiFormRicerca()).get(AcquedottoAcquedottoLogic.LISTA_SEDIME);
		request.setAttribute(AcquedottoAcquedottoLogic.LISTA_SEDIME, listaSedime);
		sessione.setAttribute(AcquedottoAcquedottoLogic.LISTA_SEDIME, listaSedime);

		Vector listaElementiFiltro = new Vector();
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=","uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like","contiene"));
		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=","uguale"));
		Vector operatoriNumerici = new Vector();
		operatoriNumerici.add(new EscOperatoreFiltro("=","="));

		EscElementoFiltro elementoFiltro = null;

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Nominativo");
		elementoFiltro.setAttributeName("NOMINATIVO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("NOMINATIVO");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Codice Fiscale / Partita Iva");
		elementoFiltro.setAttributeName("CF_PI");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("CF_PI");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Sedime");
		elementoFiltro.setAttributeName("SEDIME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(listaSedime);
		elementoFiltro.setCampoFiltrato("TIPOLOGIA");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Indirizzo");
		elementoFiltro.setAttributeName("INDIRIZZOFORNITURA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("INDIRIZZOFORNITURA");
		listaElementiFiltro.add(elementoFiltro);

		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		//chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request,response, URL_FRAME, nav);
	}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response,boolean notListaPrincipale)throws Exception{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == AcquedottoFinder.class){
				finder = (AcquedottoFinder) sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}


		finder = (AcquedottoFinder) gestireMultiPagina(finder,request);

		AcquedottoAcquedottoLogic logic = new AcquedottoAcquedottoLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaAcquedotto(vettoreRicerca, finder);

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista

		Vector vct_lista_aziende = (Vector) ht.get(AcquedottoAcquedottoLogic.LISTA_ACQUEDOTTO);
		finder = (AcquedottoFinder) ht.get(AcquedottoAcquedottoLogic.FINDER);

		sessione.setAttribute(AcquedottoAcquedottoLogic.LISTA_ACQUEDOTTO, vct_lista_aziende);
		sessione.setAttribute(NOMEFINDER, finder);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();

		this.chiamaPagina(request, response, URL_FRAME, nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception{
		String azione = "";
		HttpSession sessione = request.getSession();
		AcquedottoFinder finder = null;
		//boolean chiamataEsterna = false;
		if (sessione.getAttribute(NOMEFINDER) !=null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new AcquedottoFinder().getClass()){
				finder = (AcquedottoFinder)sessione.getAttribute(NOMEFINDER);
			}
		}
		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request, response, azione, finder, sessione, AcquedottoAcquedottoLogic.LISTA_ACQUEDOTTO, (Vector) sessione.getAttribute(AcquedottoAcquedottoLogic.LISTA_ACQUEDOTTO), NOMEFINDER);
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
		AcquedottoAcquedottoLogic logic = new AcquedottoAcquedottoLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioAzienda(oggettoSel);

		sessione.setAttribute(AcquedottoAcquedottoLogic.DETTAGLIO_ACQUEDOTTO, ht.get(AcquedottoAcquedottoLogic.DETTAGLIO_ACQUEDOTTO));

		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		this.chiamaPagina(request,response, URL_FRAME, nav);
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo){
		return ((EscObject) listaOggetti.get(recordSuccessivo)).getChiave();
	}
	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception {

		AcquedottoAcquedottoLogic logic = new AcquedottoAcquedottoLogic(this.getEnvUtente(request));
		return logic.mCaricareListaAcquedotto(this.vettoreRicerca, (AcquedottoFinder) finder2);
	}
	public String getTabellaPerCrossLink() {
		return "SIT_MI_ACQUEDOTTO";
	}
	public EscFinder getNewFinder() {
		return new AcquedottoFinder();
	}
	public String getTema() {
		return "Acquedotto e Imp.Termici";
	}
}

