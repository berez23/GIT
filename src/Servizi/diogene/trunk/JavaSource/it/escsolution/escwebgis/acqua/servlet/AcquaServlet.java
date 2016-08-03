package it.escsolution.escwebgis.acqua.servlet;

import it.escsolution.escwebgis.acqua.bean.AcquaCatasto;
import it.escsolution.escwebgis.acqua.bean.AcquaFinder;
import it.escsolution.escwebgis.acqua.bean.AcquaUtente;
import it.escsolution.escwebgis.acqua.bean.AcquaUtenze;
import it.escsolution.escwebgis.acqua.logic.AcquaLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.multe.bean.Pagato;
import it.escsolution.escwebgis.tributiNew.bean.Tributo;
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

public class AcquaServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private AcquaFinder finder = null;

	private final String NOMEFINDER = "FINDER121";
	private final String COD_FONTE = "30";
	
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
				mListaVie(request, response, "SIT_ACQUA_UTENZE", null, "VIA_UBICAZIONE", "VIA_UBICAZIONE");
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
				case 104:
					pulireSessione(request);
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
		operatoriStringa.add(new EscOperatoreFiltro("=","uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like","contiene"));



		Vector operatoriNumerici = new Vector();
		operatoriNumerici.add(new EscOperatoreFiltro(">",">"));
		operatoriNumerici.add(new EscOperatoreFiltro("<","<"));
		operatoriNumerici.add(new EscOperatoreFiltro("=","="));
		operatoriNumerici.add(new EscOperatoreFiltro("<>","<>"));

		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("like","contiene"));
		operatoriStringaRid.add(new EscOperatoreFiltro("=","uguale"));

		// costruisce il vettore di elementi per la pagina di ricerca
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Codice Fiscale Utente");
		elementoFiltro.setAttributeName("COD_FISCALE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("COD_FISCALE");
		listaElementiFiltro.add(elementoFiltro);


		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Denominazione");
		elementoFiltro.setAttributeName("DENOMINAZIONE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("TRIM(COGNOME || ' ' || NOME || ' ' || DENOMINAZIONE)");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Codice Utenza");
		elementoFiltro.setAttributeName("COD_SERVIZIO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("COD_SERVIZIO");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Consumo");
		elementoFiltro.setAttributeName("CONSUMO_MEDIO");
		elementoFiltro.setTipo("N");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("CONSUMO_MEDIO");
		listaElementiFiltro.add(elementoFiltro);	
		
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Indirizzo Utenza");
		elementoFiltro.setAttributeName("VIA_UBICAZIONE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setExtraHTML("<span><input type=\"button\" class=\"TXTmainLabel\" value=\"Cerca\" onclick=\"javascript:danRequest('"+it.escsolution.escwebgis.common.EscServlet.URL_PATH+"/EnelDwh?listavie='+document.getElementById('INDIRIZZO_UBICAZIONE').value,'divListaVie')\"/></span><span id=\"divListaVie\" class=\"txtRow\"  ></span> \n");		
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("VIA_UBICAZIONE");
		listaElementiFiltro.add(elementoFiltro);				
		
		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"acqua/AcquaFrame.jsp", nav);

	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//prendere dalla request i parametri di ricerca impostati dall'utente
		 HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null){
			finder = (AcquaFinder)sessione.getAttribute(NOMEFINDER);
		}
		else{
			finder = null;
		}

		 finder = (AcquaFinder)gestireMultiPagina(finder,request);

		 AcquaLogic logic = new AcquaLogic(this.getEnvUtente(request));
		 Hashtable ht = logic.mCaricareLista(vettoreRicerca, finder);

		 Vector vct_lista = (Vector)ht.get(AcquaLogic.LISTA_ACQUA);
		 finder = (AcquaFinder)ht.get(AcquaLogic.FINDER);

		 sessione.setAttribute(AcquaLogic.LISTA_ACQUA, vct_lista);
		 sessione.setAttribute(AcquaLogic.FINDER, finder);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();
		
		this.chiamaPagina(request,response,"acqua/AcquaFrame.jsp", nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response, int tipo)throws Exception{
			
		// prendere dalla request il parametro identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio
		
		String azione = "";
		HttpSession sessione = request.getSession();
		String progEs= request.getParameter("progEs");
		
		removeOggettiIndiceDaSessione(sessione);
	
	
		if (sessione.getAttribute(NOMEFINDER) != null) {
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new AcquaFinder().getClass()) {
				finder = (AcquaFinder)sessione.getAttribute(NOMEFINDER);
			} else {
				finder = null;
			}
		}
		
		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		
		gestioneMultiRecord(request, response, azione, finder, sessione, AcquaLogic.LISTA_ACQUA, (Vector)sessione.getAttribute(AcquaLogic.LISTA_ACQUA), NOMEFINDER);
		
		if (!azione.equals("")) {
			Vector listaOggettiAcqua = (Vector)sessione.getAttribute(AcquaLogic.LISTA_ACQUA);
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
		AcquaLogic logic = new AcquaLogic(this.getEnvUtente(request));
		Hashtable ht = null;
		
		//nel caso in cui provengo da Correlazione Vie/Civici e il parametro non inizia con #, e nel caso della Correlaione Fabbricati , allora l'id si riferisce all'oggetto e non al soggetto. 
		//In tutti gli altri casi l'id si riferisce al soggetto
		log.debug("ricerca dettaglio multe da ID: " + oggettoSel );
		if (oggettoSel.startsWith("#"))
			oggettoSel= oggettoSel.substring(1);
		ht = logic.mCaricareDettaglio(oggettoSel, progEs);
		
		
		AcquaUtenze tes = (AcquaUtenze)ht.get(AcquaLogic.ACQUA);
		AcquaUtente te = (AcquaUtente)ht.get(AcquaLogic.ACQUA_UTENTE);
		Vector<AcquaUtenze> altreUtenze = (Vector<AcquaUtenze>) ht.get(AcquaLogic.ALTRE_UTENZE);
		Vector<AcquaCatasto> catasto =  (Vector<AcquaCatasto>) ht.get(AcquaLogic.CATASTO);
		
		sessione.setAttribute(AcquaLogic.ACQUA, tes);
		sessione.setAttribute(AcquaLogic.ACQUA_UTENTE, te);
		sessione.setAttribute(AcquaLogic.ALTRE_UTENZE, altreUtenze);
		sessione.setAttribute(AcquaLogic.CATASTO, catasto);
		
		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		
		// Aggiungo Soggetti per correlazione
		OggettoIndice oi = new OggettoIndice();
		oi.setIdOrig(te.getId());
		oi.setFonte("30");
		oi.setProgr("1");
		String descr = (!"-".equals(te.getCognome())?te.getCognome():"")
				+ " " + (!"-".equals(te.getNome())?te.getNome():"")
				+ " " + (!"-".equals(te.getDenominazione())?te.getDenominazione():"");
		oi.setDescrizione(descr.trim());
		
		OggettoIndice oi1 = new OggettoIndice();
		oi1.setIdOrig(tes.getId());
		oi1.setFonte("30");
		oi1.setProgr("2");
		oi1.setDescrizione(!"-".equals(tes.getRagSocUbicazione())?tes.getRagSocUbicazione():"");
		
		Vector<OggettoIndice> soggettiInd = new Vector<OggettoIndice>();
		soggettiInd.add(oi);
		soggettiInd.add(oi1);
		sessione.setAttribute("indice_soggetti", soggettiInd);
		
		// Aggiungo Vie per correlazione
		OggettoIndice oi2 = new OggettoIndice();
		oi2.setIdOrig(te.getId());
		oi2.setFonte("30");
		oi2.setProgr("1");
		oi2.setDescrizione(te.getViaResidenza());
		
		OggettoIndice oi3 = new OggettoIndice();
		oi3.setIdOrig(tes.getId());
		oi3.setFonte("30");
		oi3.setProgr("2");
		oi3.setDescrizione(tes.getViaUbicazione());
			
		Vector<OggettoIndice> vieInd = new Vector<OggettoIndice>();
		vieInd.add(oi2);
		vieInd.add(oi3);
		sessione.setAttribute("indice_vie", vieInd);
		
		// Aggiungo Civici per correlazione
		OggettoIndice oi4 = new OggettoIndice();
		oi4.setIdOrig(te.getId());
		oi4.setFonte("30");
		oi4.setProgr("1");
		oi4.setDescrizione(te.getCivicoResidenza());
		
		OggettoIndice oi5 = new OggettoIndice();
		oi5.setIdOrig(tes.getId());
		oi5.setFonte("30");
		oi5.setProgr("2");
		oi5.setDescrizione(tes.getCivicoUbicazione());
			
		Vector<OggettoIndice> civiciInd = new Vector<OggettoIndice>();
		civiciInd.add(oi4);
		civiciInd.add(oi5);
		sessione.setAttribute("indice_civici", civiciInd);
		
		// Aggiungo Fabbricati per correlazione
		Vector<OggettoIndice> fabbInd = new Vector<OggettoIndice>();
		java.util.Enumeration en = catasto.elements();
		OggettoIndice oi6 = new OggettoIndice();
		while (en.hasMoreElements()) {
			AcquaCatasto ac = (AcquaCatasto)en.nextElement();
			oi6 = new OggettoIndice();
			oi6.setIdOrig(te.getId());
			oi6.setFonte("30");
			oi6.setProgr("3");
			oi6.setDescrizione("SEZ:-"+" F:" + ac.getFoglio() + " P:" + ac.getParticella() );
				
			fabbInd.add(oi6);
		}
		sessione.setAttribute("indice_fabbricati", fabbInd);
		
		this.chiamaPagina(request,response,"acqua/AcquaFrame.jsp", nav);
	}	


	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo){
		return ((AcquaUtenze)listaOggetti.get(recordSuccessivo)).getChiave();
	}

	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (AcquaFinder)finder2;
		AcquaLogic logic = new AcquaLogic(this.getEnvUtente(request));
		return logic.mCaricareLista(this.vettoreRicerca,finder);
	}

	public EscFinder getNewFinder(){
		return new AcquaFinder();
	}

	public String getTema() {
		return "Acqua";
	}
	
	public String getTabellaPerCrossLink() {
		return "SIT_ACQUA_UTENTE";
	}
	
	public String getLocalDataSource() {
		return localDataSource;
	}

}

