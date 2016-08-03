package it.escsolution.escwebgis.imu.servlet;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.imu.bean.ConsulenzaBean;
import it.escsolution.escwebgis.imu.bean.SaldoImuFinder;
import it.escsolution.escwebgis.imu.logic.ConsulenzaImuLogic;
import it.webred.ct.data.access.basic.common.dto.KeyValueDTO;
import it.webred.ct.data.access.basic.imu.dto.SaldoImuConsulenzaDTO;
import it.webred.ct.data.access.basic.imu.dto.SaldoImuElaborazioneDTO;
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

public class ConsulenzaImuServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private SaldoImuFinder finder = null;

	private final String NOMEFINDER = "FINDER122";
	private final String COD_FONTE = "34";
	
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
					case 33:
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
		 ConsulenzaImuLogic logic = new ConsulenzaImuLogic(this.getEnvUtente(request));
		
		Vector listaElementiFiltro = new Vector();
		
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=", "uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like", "contiene"));
		
		Vector operatoriNumerici = new Vector();
		operatoriNumerici.add(new EscOperatoreFiltro("=","="));
		operatoriNumerici.add(new EscOperatoreFiltro("<>","<>"));
		operatoriNumerici.add(new EscOperatoreFiltro(">",">"));
		operatoriNumerici.add(new EscOperatoreFiltro("<","<"));

		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=", "uguale"));
		
		// costruisce il vettore di elementi per la pagina di ricerca
		//titolo gruppo
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("DATI CONTRIBUENTE");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cod.Fiscale");
		elementoFiltro.setAttributeName("CODFISC");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("S.CODFISC");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cognome");
		elementoFiltro.setAttributeName("COGNOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("S.COGNOME");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Nome");
		elementoFiltro.setAttributeName("NOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("S.NOME");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Consulenza");
		elementoFiltro.setAttributeName("DT_INS");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("S.DT_INS");
		listaElementiFiltro.add(elementoFiltro);
	
	
		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"saldoImu/saldoImuFrame.jsp", nav);

	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//prendere dalla request i parametri di ricerca impostati dall'utente
		 HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null){
			finder = (SaldoImuFinder)sessione.getAttribute(NOMEFINDER);
		}
		else{
			finder = null;
		}

		 finder = (SaldoImuFinder)gestireMultiPagina(finder,request);

		 ConsulenzaImuLogic logic = new ConsulenzaImuLogic(this.getEnvUtente(request));
		 Hashtable ht = logic.mCaricareLista(vettoreRicerca, finder);

		 Vector vct_lista = (Vector)ht.get(ConsulenzaImuLogic.LISTA);
		 finder = (SaldoImuFinder)ht.get(ConsulenzaImuLogic.FINDER);

		 sessione.setAttribute(ConsulenzaImuLogic.LISTA, vct_lista);
		 sessione.setAttribute(ConsulenzaImuLogic.FINDER, finder);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();
		
		this.chiamaPagina(request,response,"saldoImu/saldoImuFrame.jsp", nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response, int tipo)throws Exception{
			
		// prendere dalla request il parametro identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio
		
		String azione = "";
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
	
	
		if (sessione.getAttribute(NOMEFINDER) != null) {
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new SaldoImuFinder().getClass()) {
				finder = (SaldoImuFinder)sessione.getAttribute(NOMEFINDER);
			} else {
				finder = null;
			}
		}
		
		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		
		gestioneMultiRecord(request, response, azione, finder, sessione, ConsulenzaImuLogic.LISTA, (Vector)sessione.getAttribute(ConsulenzaImuLogic.LISTA), NOMEFINDER);
		
		if (!azione.equals("")) {
			Vector listaOggetti = (Vector)sessione.getAttribute(ConsulenzaImuLogic.LISTA);
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
		ConsulenzaImuLogic logic = new ConsulenzaImuLogic(this.getEnvUtente(request));
		Hashtable ht = null;
		
		//nel caso in cui provengo da Correlazione Vie/Civici e il parametro non inizia con #, e nel caso della Correlaione Fabbricati , allora l'id si riferisce all'oggetto e non al soggetto. 
		//In tutti gli altri casi l'id si riferisce al soggetto
		log.debug("ricerca dettaglio pubblicita da ID: " + oggettoSel );
		if (tipo == 101)
			ht = logic.mCaricareDettaglio(oggettoSel);	
		else if (tipo== 102 || tipo == 103 ){
			if (oggettoSel.startsWith("#") == false)
				ht = logic.mCaricareDettaglioFromOggetto(oggettoSel);
			
			else{
				oggettoSel= oggettoSel.substring(1);
				ht = logic.mCaricareDettaglio(oggettoSel);	
			}
		}
		else if (tipo == 105)
			ht = logic.mCaricareDettaglioFromOggetto(oggettoSel);	
		else
			ht = logic.mCaricareDettaglio(oggettoSel);	
		
		SaldoImuConsulenzaDTO cons = (SaldoImuConsulenzaDTO)ht.get(ConsulenzaImuLogic.IMU_CONS);
		SaldoImuElaborazioneDTO elab = (SaldoImuElaborazioneDTO)ht.get(ConsulenzaImuLogic.IMU_ELAB);
		
		sessione.setAttribute(ConsulenzaImuLogic.IMU_CONS, cons);
		sessione.setAttribute(ConsulenzaImuLogic.IMU_ELAB, elab);
		
		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		
		Vector<OggettoIndice> soggettiInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> vieInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> civiciInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> fabbInd = new Vector<OggettoIndice>();
		
		ArrayList<String> listaIndirizzi= new ArrayList<String>();
		ArrayList<String> listaFabbricati= new ArrayList<String>();

		// Aggiungo Soggetti per correlazione
		OggettoIndice oi = new OggettoIndice();
		oi.setIdOrig(cons.getId());
		oi.setFonte("34");
		oi.setProgr("1");
		String descr = cons.getId().split("\\|")[0];
		oi.setDescrizione(descr.trim());
		
		soggettiInd.add(oi);
		
		sessione.setAttribute("indice_soggetti", soggettiInd);
		sessione.setAttribute("indice_vie", vieInd);
		sessione.setAttribute("indice_civici", civiciInd);
		sessione.setAttribute("indice_fabbricati", fabbInd);
		
		this.chiamaPagina(request,response,"saldoImu/saldoImuFrame.jsp", nav);
	}	


	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo){
		return ((ConsulenzaBean)listaOggetti.get(recordSuccessivo)).getChiave();
	}

	
	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (SaldoImuFinder)finder2;
		ConsulenzaImuLogic logic = new ConsulenzaImuLogic(this.getEnvUtente(request));
		return logic.mCaricareLista(this.vettoreRicerca,finder);
	}

	public EscFinder getNewFinder(){
		return new SaldoImuFinder();
	}

	public String getTema() {
		return "IMU";
	}
	
	public String getTabellaPerCrossLink() {
		return "SALDO_IMU_STORICO";
	}
	
	public String getLocalDataSource() {
		return localDataSource;
	}

}

