package it.escsolution.escwebgis.versamenti.iciDM.servlet;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.versamenti.bean.VersFinder;
import it.escsolution.escwebgis.versamenti.iciDM.logic.VersIciDmLogic;
import it.webred.ct.data.access.basic.common.dto.KeyValueDTO;
import it.webred.ct.data.access.basic.versamenti.iciDM.dto.IciDmDTO;
import it.webred.ct.data.access.basic.versamenti.iciDM.dto.VersamentoIciDmDTO;
import it.webred.ct.data.access.basic.versamenti.iciDM.dto.ViolazioneIciDmDTO;
import it.webred.indice.OggettoIndice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class VersIciDmServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private VersFinder finder = null;
	private String FRAME = "versamenti/VersIciDmFrame.jsp";
	
	
	private String localDataSource = "jdbc/Diogene_DS";
	
	
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
	
	public void EseguiServizio(
			HttpServletRequest request,HttpServletResponse response)
					throws ServletException, IOException {
		/*
		 * ad ogni uc corrisponde una funzionalità diversa
		 *
		 */
		super.EseguiServizio(request,response);
		
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		String ext= request.getParameter("IND_EXT");
		
		if (ext!= null && ext.equals("1"))
			EseguiServizioExt(request,response);
		else
			_EseguiServizio(request,response);
		
	}
    

	public void _EseguiServizio(
			HttpServletRequest request,HttpServletResponse response)
					throws ServletException, IOException {

		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		super.EseguiServizio(request,response);
		
		try {
			switch (st){
				case 1:
					// carico la form di ricerca
					pulireSessione(request);
					mCaricareFormRicerca(request, response);
					break;
				case 2:
					// vengo dalla pagina di ricerca --> predispongo la lista e la passo alla pagina di lista
					mCaricareLista(request, response);
					break;
				case 3:
					// ho cliccato su un elemento --> visualizzo il dettaglio
					mCaricareDettaglio(request, response,st);
				//	this.leggiCrossLink(request);
					break;
				case 37:
					 // ho cliccato su un elemento --> visualizzo il dettaglio
					 mCaricareDettaglio(request,response,st);
				//	this.leggiCrossLink(request);
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
	
	public void EseguiServizioExt(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		super.EseguiServizio(request,response);
		
		try
		{
			switch (st){
			case 101:
				pulireSessione(request);
				mCaricareDettaglio(request,response,st);
				break;
			case 102:
				// carico la form di ricerca
				pulireSessione(request);
				//mCaricareLista(request,response, 102);
				mCaricareDettaglio(request,response,st);
				break;
			case 103:
				// carico la form di ricerca
				pulireSessione(request);
				//mCaricareLista(request,response, 103);
				mCaricareDettaglio(request,response,st);
				break;				
			case 105:
				pulireSessione(request);
				mCaricareDettaglio(request,response,st);
				break;
			}
		}
		catch(it.escsolution.escwebgis.common.DiogeneException de)
		{
			throw de;
		}
		catch(Exception ex)
		{
			throw new it.escsolution.escwebgis.common.DiogeneException(ex);
		}
	}	
	

	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession sessione = request.getSession();
		VersIciDmLogic logic = new VersIciDmLogic(this.getEnvUtente(request));
		
		Vector listaElementiFiltro = new Vector();
		
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=", "uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like", "contiene"));
		
		Vector operatoriNumerici = new Vector();
		operatoriNumerici.add(new EscOperatoreFiltro("=","="));
		operatoriNumerici.add(new EscOperatoreFiltro("<>","<>"));
		operatoriNumerici.add(new EscOperatoreFiltro(">",">"));
		operatoriNumerici.add(new EscOperatoreFiltro("<","<"));
		
		Vector operatoriNumericiMag = new Vector();
		operatoriNumericiMag.add(new EscOperatoreFiltro(">",">"));
		operatoriNumericiMag.add(new EscOperatoreFiltro(">=",">="));
		
		Vector operatoriNumericiMin = new Vector();
		operatoriNumericiMin.add(new EscOperatoreFiltro("<","<"));
		operatoriNumericiMin.add(new EscOperatoreFiltro("<=","<="));

		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=", "uguale"));
		
		// costruisce il vettore di elementi per la pagina di ricerca
		//titolo gruppo
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("DATI VERSAMENTO ICI (D.M.)");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
				
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cod.Fiscale");
		elementoFiltro.setAttributeName("CF");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.CODFISC");
		listaElementiFiltro.add(elementoFiltro);
		
		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,this.FRAME, nav);

	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//prendere dalla request i parametri di ricerca impostati dall'utente
		 HttpSession sessione = request.getSession();

		if (sessione.getAttribute(VersIciDmLogic.FINDER)!= null){
			finder = (VersFinder)sessione.getAttribute(VersIciDmLogic.FINDER);
		}
		else{
			finder = null;
		}

		 finder = (VersFinder)gestireMultiPagina(finder,request);

		 VersIciDmLogic logic = new VersIciDmLogic(this.getEnvUtente(request));
		 Hashtable ht = logic.mCaricareLista(vettoreRicerca, finder);

		 Vector vct_lista = (Vector)ht.get(VersIciDmLogic.LISTA_VERSAMENTI);
		 finder = (VersFinder)ht.get(VersIciDmLogic.FINDER);

		 sessione.setAttribute(VersIciDmLogic.LISTA_VERSAMENTI, vct_lista);
		 sessione.setAttribute(VersIciDmLogic.FINDER, finder);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();
		
		this.chiamaPagina(request,response,this.FRAME, nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response, int tipo)throws Exception{
			
		// prendere dalla request il parametro identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio
		
		String azione = "";
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
	
	
		if (sessione.getAttribute(VersIciDmLogic.FINDER) != null) {
			if (((Object)sessione.getAttribute(VersIciDmLogic.FINDER)).getClass() == new VersFinder().getClass()) {
				finder = (VersFinder)sessione.getAttribute(VersIciDmLogic.FINDER);
			} else {
				finder = null;
			}
		}
		
		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		
		gestioneMultiRecord(request, response, azione, finder, sessione, VersIciDmLogic.LISTA_VERSAMENTI, (Vector)sessione.getAttribute(VersIciDmLogic.LISTA_VERSAMENTI), VersIciDmLogic.FINDER);
		
		if (!azione.equals("")) {
			Vector listaOggetti = (Vector)sessione.getAttribute(VersIciDmLogic.LISTA_VERSAMENTI);
		} else {
			oggettoSel = "";
			recordScelto = "";
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

		/*
		 * carica il dettaglio
		 */
		VersIciDmLogic logic = new VersIciDmLogic(this.getEnvUtente(request));
		Hashtable ht = null;
		
		ht = logic.mCaricareDettaglio(oggettoSel);	
		
		VersamentoIciDmDTO ver = (VersamentoIciDmDTO)ht.get(VersIciDmLogic.VERSAMENTO);
		ViolazioneIciDmDTO vio = (ViolazioneIciDmDTO)ht.get(VersIciDmLogic.VIOLAZIONE);
		
		sessione.setAttribute(VersIciDmLogic.VERSAMENTO, ver);
		sessione.setAttribute(VersIciDmLogic.VIOLAZIONE, vio);
		
		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		
		Vector<OggettoIndice> soggettiInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> vieInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> civiciInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> fabbInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> oggInd = new Vector<OggettoIndice>();
		
		// Aggiungo Soggetti per correlazione
		OggettoIndice oi = new OggettoIndice();
		if(ver!=null){
			oi.setIdOrig(ver.getId());
			oi.setFonte("37");
			oi.setProgr("1");
			oi.setDescrizione(ver.getCfVersante());
			soggettiInd.add(oi);
		}
		
		if(vio!=null){
			oi = new OggettoIndice();
			oi.setIdOrig(vio.getId());
			oi.setFonte("37");
			oi.setProgr("2");
			oi.setDescrizione(vio.getCfVersante());
			soggettiInd.add(oi);
		}
		
		sessione.setAttribute("indice_soggetti", soggettiInd);
		sessione.setAttribute("indice_vie", vieInd);
		sessione.setAttribute("indice_civici", civiciInd);
		sessione.setAttribute("indice_fabbricati", fabbInd);
		sessione.setAttribute("indice_oggetti", oggInd);
		
		this.chiamaPagina(request,response,this.FRAME, nav);
	}	


	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo){
		return ((IciDmDTO)listaOggetti.get(recordSuccessivo)).getId();
	}

	
	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (VersFinder)finder2;
		VersIciDmLogic logic = new VersIciDmLogic(this.getEnvUtente(request));
		return logic.mCaricareLista(this.vettoreRicerca,finder);
	}

	public EscFinder getNewFinder(){
		return new VersFinder();
	}

	public String getTema() {
		return "VERS ICI MISTERIALE";
	}
	
	public String getTabellaPerCrossLink() {
		return "SIT_T_ICI_DM_VERS";
	}
	
	public String getLocalDataSource() {
		return localDataSource;
	}

}

