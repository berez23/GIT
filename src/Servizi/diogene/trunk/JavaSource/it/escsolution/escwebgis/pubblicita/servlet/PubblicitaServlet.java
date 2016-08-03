package it.escsolution.escwebgis.pubblicita.servlet;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.pubblicita.bean.PubblicitaDettaglio;
import it.escsolution.escwebgis.pubblicita.bean.PubblicitaFinder;
import it.escsolution.escwebgis.pubblicita.bean.PubblicitaTestata;
import it.escsolution.escwebgis.pubblicita.logic.PubblicitaLogic;
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

public class PubblicitaServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private PubblicitaFinder finder = null;

	private final String NOMEFINDER = "FINDER115";
	private final String COD_FONTE = "27";
	
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
				mListaVie(request, response, "SIT_PUBBLICITA_PRAT_DETTAGLIO", null, "VIA", "VIA");
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
		elementoFiltro.setLabel("DATI PRATICA");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		
/*		Vector<TipoSoggetto> vctTipiSoggetto = new Vector<TipoSoggetto>();
		//lista fissa
		vctTipiSoggetto.add(new TipoSoggetto("", "Tutti"));
		vctTipiSoggetto.add(new TipoSoggetto("F", "Persona Fisica"));
		vctTipiSoggetto.add(new TipoSoggetto("G", "Persona Giuridica"));
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Tipo Soggetto");
		elementoFiltro.setAttributeName("TIPO_PERSONA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vctTipiSoggetto);
		elementoFiltro.setCampoFiltrato("A.TIPO_PERSONA");
		listaElementiFiltro.add(elementoFiltro);*/
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Tipo Pratica");
		elementoFiltro.setAttributeName("TIPO_PRATICA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.TIPO_PRATICA");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Anno Pratica");
		elementoFiltro.setAttributeName("ANNO_PRATICA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.ANNO_PRATICA");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Num.Pratica");
		elementoFiltro.setAttributeName("NUM_PRATICA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.NUM_PRATICA");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Inizio");
		elementoFiltro.setAttributeName("DT_INIZIO");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("T.DT_INIZIO");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Decorrenza Termini");
		elementoFiltro.setAttributeName("DT_DEC_TERMINI");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("T.DT_DEC_TERMINI");
		listaElementiFiltro.add(elementoFiltro);
		
		
		//titolo gruppo
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("DATI OGGETTI TERRITORIALI COLLEGATI");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Via / Piazza");
		elementoFiltro.setAttributeName("VIA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("D.VIA");
		elementoFiltro.setExtraHTML("<span><input type=\"button\" class=\"TXTmainLabel\" value=\"Cerca\" onclick=\"javascript:danRequest('"+it.escsolution.escwebgis.common.EscServlet.URL_PATH+"/Pubblicita?listavie='+document.getElementById('VIA').value,'divListaVie')\"/></span><span id=\"divListaVie\" class=\"txtRow\"  ></span> \n");		
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Civico");
		elementoFiltro.setAttributeName("CIVICO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("D.CIVICO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Classe");
		elementoFiltro.setAttributeName("CLASSE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("D.DESC_CLS");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Inizio");
		elementoFiltro.setAttributeName("DT_INIZIO_D");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("D.DT_INIZIO");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Fine");
		elementoFiltro.setAttributeName("DT_FINE_D");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("D.DT_FINE");
		listaElementiFiltro.add(elementoFiltro);
		
/*		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Oggetto");
		elementoFiltro.setAttributeName("OGGETTO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("D.DESC_OGGETTO");
		listaElementiFiltro.add(elementoFiltro);*/

		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"pubblicita/PubblicitaFrame.jsp", nav);

	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//prendere dalla request i parametri di ricerca impostati dall'utente
		 HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null){
			finder = (PubblicitaFinder)sessione.getAttribute(NOMEFINDER);
		}
		else{
			finder = null;
		}

		 finder = (PubblicitaFinder)gestireMultiPagina(finder,request);

		 PubblicitaLogic logic = new PubblicitaLogic(this.getEnvUtente(request));
		 Hashtable ht = logic.mCaricareLista(vettoreRicerca, finder);

		 Vector vct_lista = (Vector)ht.get(PubblicitaLogic.LISTA_PUBBLICITA);
		 finder = (PubblicitaFinder)ht.get(PubblicitaLogic.FINDER);

		 sessione.setAttribute(PubblicitaLogic.LISTA_PUBBLICITA, vct_lista);
		 sessione.setAttribute(PubblicitaLogic.FINDER, finder);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();
		
		this.chiamaPagina(request,response,"pubblicita/PubblicitaFrame.jsp", nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response, int tipo)throws Exception{
			
		// prendere dalla request il parametro identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio
		
		String azione = "";
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
	
	
		if (sessione.getAttribute(NOMEFINDER) != null) {
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new PubblicitaFinder().getClass()) {
				finder = (PubblicitaFinder)sessione.getAttribute(NOMEFINDER);
			} else {
				finder = null;
			}
		}
		
		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		
		gestioneMultiRecord(request, response, azione, finder, sessione, PubblicitaLogic.LISTA_PUBBLICITA, (Vector)sessione.getAttribute(PubblicitaLogic.LISTA_PUBBLICITA), NOMEFINDER);
		
		if (!azione.equals("")) {
			Vector listaOggettiPubblicita = (Vector)sessione.getAttribute(PubblicitaLogic.LISTA_PUBBLICITA);
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
		PubblicitaLogic logic = new PubblicitaLogic(this.getEnvUtente(request));
		Hashtable ht = null;
		
		//nel caso in cui provengo da Correlazione Vie/Civici e il parametro non inizia con #, e nel caso della Correlaione Fabbricati , allora l'id si riferisce all'oggetto e non al soggetto. 
		//In tutti gli altri casi l'id si riferisce al soggetto
		log.debug("ricerca dettaglio pubblicità da ID: " + oggettoSel );
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
		
		PubblicitaTestata tes = (PubblicitaTestata)ht.get(PubblicitaLogic.PUBBLICITA);
		
		sessione.setAttribute(PubblicitaLogic.PUBBLICITA, tes);
		
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
	
		OggettoIndice oi = new OggettoIndice();	
		OggettoIndice oic = new OggettoIndice();	
		
		ArrayList<PubblicitaDettaglio> listaDettaglio= tes.getLstDettaglio();
		
		for (PubblicitaDettaglio det:listaDettaglio){
			
			oi = new OggettoIndice();
			oi.setIdOrig(det.getId());
			oi.setFonte("27");
			oi.setProgr("2");
			
			String desc = null;
			if ((det.getVia()!= null && !"".equals(det.getVia()) ) )
				desc = det.getVia();
				
			oi.setDescrizione(desc);
			
			
			oic = new OggettoIndice();
			oic.setIdOrig(det.getId());
			oic.setFonte("27");
			oic.setProgr("2");
			
			if(desc!=null && det.getCivico()!=null && !"".equals(det.getVia()))
					desc += " " + det.getCivico();
			
			oic.setDescrizione(desc);
			
			String nome= oi.getFonte()+oi.getProgr()+oi.getIdOrig();
			
			if (!listaIndirizzi.contains(nome)){
				vieInd.add(oi);
				civiciInd.add(oic);
				listaIndirizzi.add(nome);
			}
			
		}
		
		sessione.setAttribute("indice_soggetti", soggettiInd);
		sessione.setAttribute("indice_vie", vieInd);
		sessione.setAttribute("indice_civici", civiciInd);
		sessione.setAttribute("indice_fabbricati", fabbInd);
		
		this.chiamaPagina(request,response,"pubblicita/PubblicitaFrame.jsp", nav);
	}	


	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo){
		return ((PubblicitaTestata)listaOggetti.get(recordSuccessivo)).getChiave();
	}

	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (PubblicitaFinder)finder2;
		PubblicitaLogic logic = new PubblicitaLogic(this.getEnvUtente(request));
		return logic.mCaricareLista(this.vettoreRicerca,finder);
	}

	public EscFinder getNewFinder(){
		return new PubblicitaFinder();
	}

	public String getTema() {
		return "Pubblicita";
	}
	
	public String getTabellaPerCrossLink() {
		return "SIT_PUBBLICITA_PRAT_DETTAGLIO";
	}
	
	public String getLocalDataSource() {
		return localDataSource;
	}

}

