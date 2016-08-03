package it.escsolution.escwebgis.f24.servlet;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.f24.bean.F24ComboObject;
import it.escsolution.escwebgis.f24.bean.F24Finder;
import it.escsolution.escwebgis.f24.logic.F24AnnullamentoLogic;
import it.webred.ct.data.access.basic.f24.dto.F24AnnullamentoDTO;
import it.webred.ct.data.access.basic.f24.dto.F24VersamentoDTO;
import it.webred.indice.OggettoIndice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class F24AnnullamentoServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private F24Finder finder = null;

	private final String NOMEFINDER = "FINDER123";
	private final String COD_FONTE = "33";
	
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
			if (request.getParameter("listavie") != null)
				mListaVie(request, response, "SIT_T_24_ANNULLAMENTO", null, "VIA", "VIA");
			else  {
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
		 F24AnnullamentoLogic logic = new F24AnnullamentoLogic(this.getEnvUtente(request));
		
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
		
		Vector operatoriNumericiMag = new Vector();
		operatoriNumericiMag.add(new EscOperatoreFiltro(">",">"));
		operatoriNumericiMag.add(new EscOperatoreFiltro(">=",">="));
		
		Vector operatoriNumericiMin = new Vector();
		operatoriNumericiMin.add(new EscOperatoreFiltro("<","<"));
		operatoriNumericiMin.add(new EscOperatoreFiltro("<=","<="));
		
		// costruisce il vettore di elementi per la pagina di ricerca
		//titolo gruppo
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("DATI ANNULLAMENTO F24");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		
		Vector<F24ComboObject> vctTipiImposta = new Vector<F24ComboObject>();
		//lista fissa
		vctTipiImposta.add(new F24ComboObject("", "Tutti"));
		vctTipiImposta.add(new F24ComboObject("R", "R - Contributo/Imposta di Soggiorno"));
		vctTipiImposta.add(new F24ComboObject("I", "I - ICI/IMU"));
		vctTipiImposta.add(new F24ComboObject("A", "A - TARES"));
		vctTipiImposta.add(new F24ComboObject("T", "T - TARSU/Tariffa"));
		vctTipiImposta.add(new F24ComboObject("S", "S - Tassa di Scopo"));
		vctTipiImposta.add(new F24ComboObject("O", "O - TOSAP/COSAP"));
		vctTipiImposta.add(new F24ComboObject("U", "U - TASI"));
		
		Vector<F24ComboObject> vctFlag = new Vector<F24ComboObject>();
		//lista fissa
		vctFlag.add(new F24ComboObject("", "Tutti"));
		vctFlag.add(new F24ComboObject("1", "Si"));
		vctFlag.add(new F24ComboObject("0", "No"));
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Fornitura");
		elementoFiltro.setAttributeName("DT_FORNITURA");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("T.DT_FORNITURA");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Prog.Fornitura");
		elementoFiltro.setAttributeName("PROG_FORNITURA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.PROG_FORNITURA");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Ripartizione");
		elementoFiltro.setAttributeName("DT_RIPARTIZIONE");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("T.DT_RIPART_ORIG");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Prog.Fornitura");
		elementoFiltro.setAttributeName("PROG_RIPARTIZIONE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.PROG_RIPART_ORIG");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Bonifico");
		elementoFiltro.setAttributeName("DT_BONIFICO");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("T.DT_BONIFICO_ORIG");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Versamento da");
		elementoFiltro.setAttributeName("DT_RISCOSSIONE");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumericiMag);
		elementoFiltro.setCampoFiltrato("T.DT_RISCOSSIONE");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Versamento a");
		elementoFiltro.setAttributeName("DT_RISCOSSIONE");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumericiMin);
		elementoFiltro.setCampoFiltrato("T.DT_RISCOSSIONE");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cod.Fiscale Contribuente");
		elementoFiltro.setAttributeName("CF");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.CF");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Anno Riferimento");
		elementoFiltro.setAttributeName("ANNO_RIF");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.ANNO_RIF");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Tipo Imposta");
		elementoFiltro.setAttributeName("TIPO_IMPOSTA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vctTipiImposta);
		elementoFiltro.setCampoFiltrato("T.TIPO_IMPOSTA");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Tipo Tributo");
		elementoFiltro.setAttributeName("TIPO_TRIBUTO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(logic.getListaTipoTributo());
		elementoFiltro.setCampoFiltrato("COD.DESCRIZIONE");
		listaElementiFiltro.add(elementoFiltro);
		
		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"f24/F24AnnullamentoFrame.jsp", nav);

	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//prendere dalla request i parametri di ricerca impostati dall'utente
		 HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null){
			finder = (F24Finder)sessione.getAttribute(NOMEFINDER);
		}
		else{
			finder = null;
		}

		 finder = (F24Finder)gestireMultiPagina(finder,request);

		 F24AnnullamentoLogic logic = new F24AnnullamentoLogic(this.getEnvUtente(request));
		 Hashtable ht = logic.mCaricareLista(vettoreRicerca, finder);

		 Vector vct_lista = (Vector)ht.get(F24AnnullamentoLogic.LISTA);
		 finder = (F24Finder)ht.get(F24AnnullamentoLogic.FINDER);

		 sessione.setAttribute(F24AnnullamentoLogic.LISTA, vct_lista);
		 sessione.setAttribute(F24AnnullamentoLogic.FINDER, finder);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();
		
		this.chiamaPagina(request,response,"f24/F24AnnullamentoFrame.jsp", nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response, int tipo)throws Exception{
			
		// prendere dalla request il parametro identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio
		
		String azione = "";
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
	
	
		if (sessione.getAttribute(NOMEFINDER) != null) {
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new F24Finder().getClass()) {
				finder = (F24Finder)sessione.getAttribute(NOMEFINDER);
			} else {
				finder = null;
			}
		}
		
		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		
		gestioneMultiRecord(request, response, azione, finder, sessione, F24AnnullamentoLogic.LISTA, (Vector)sessione.getAttribute(F24AnnullamentoLogic.LISTA), NOMEFINDER);
		
		if (!azione.equals("")) {
			Vector listaOggettiPubblicita = (Vector)sessione.getAttribute(F24AnnullamentoLogic.LISTA);
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
		F24AnnullamentoLogic logic = new F24AnnullamentoLogic(this.getEnvUtente(request));
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
		
		F24AnnullamentoDTO tes = (F24AnnullamentoDTO)ht.get(F24AnnullamentoLogic.ANNULLAMENTO);
		List<F24VersamentoDTO> listaVers = (List<F24VersamentoDTO>)ht.get(F24AnnullamentoLogic.LISTA_VERSAMENTI);
		
		sessione.setAttribute(F24AnnullamentoLogic.ANNULLAMENTO, tes);
		sessione.setAttribute(F24AnnullamentoLogic.LISTA_VERSAMENTI,listaVers);
		
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
		oi.setIdOrig(tes.getId());
		oi.setFonte("33");
		oi.setProgr("3");
		String descr = tes.getCf();
		oi.setDescrizione(descr.trim());
		
		soggettiInd.add(oi);
		
		sessione.setAttribute("indice_soggetti", soggettiInd);
		
		
		sessione.setAttribute("indice_soggetti", soggettiInd);
		sessione.setAttribute("indice_vie", vieInd);
		sessione.setAttribute("indice_civici", civiciInd);
		sessione.setAttribute("indice_fabbricati", fabbInd);
		
		this.chiamaPagina(request,response,"f24/F24AnnullamentoFrame.jsp", nav);
	}	


	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo){
		return ((F24AnnullamentoDTO)listaOggetti.get(recordSuccessivo)).getChiave();
	}

	
	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (F24Finder)finder2;
		F24AnnullamentoLogic logic = new F24AnnullamentoLogic(this.getEnvUtente(request));
		return logic.mCaricareLista(this.vettoreRicerca,finder);
	}

	public EscFinder getNewFinder(){
		return new F24Finder();
	}

	public String getTema() {
		return "F24";
	}
	
	public String getTabellaPerCrossLink() {
		return "SIT_T_24_ANNULLAMENTO";
	}
	
	public String getLocalDataSource() {
		return localDataSource;
	}

}

