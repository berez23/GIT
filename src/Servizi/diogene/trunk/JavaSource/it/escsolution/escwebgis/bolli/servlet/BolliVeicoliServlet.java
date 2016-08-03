package it.escsolution.escwebgis.bolli.servlet;


import it.escsolution.escwebgis.bolli.bean.BolliVeicoliFinder;
import it.escsolution.escwebgis.bolli.logic.BolliVeicoliLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.webred.ct.data.model.bolliVeicoli.BolloVeicolo;
import it.webred.indice.OggettoIndice;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BolliVeicoliServlet extends EscServlet implements EscService {

	private static final long serialVersionUID = -4076207891787225299L;
	
	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private BolliVeicoliFinder finder = null;

	private final String NOMEFINDER = "FINDER137";
	
	private String localDataSource = "jdbc/Diogene_DS";
	
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }//-------------------------------------------------------------------------
	
	public void EseguiServizio(	HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
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
		
	}//-------------------------------------------------------------------------
    
	public void _EseguiServizio(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		super.EseguiServizio(request,response);
		
		try {
			if (request.getParameter("listavie") != null)
				mListaVie(request, response, "BOLLI_VEICOLI", null, "INDIRIZZO", "VIA");
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
	}//-------------------------------------------------------------------------
	
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
			ex.printStackTrace();
			throw new it.escsolution.escwebgis.common.DiogeneException(ex);
		}
	}//-------------------------------------------------------------------------	

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
		elementoFiltro.setLabel("DATI BOLLI VEICOLI");
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
		listaElementiFiltro.add(elementoFiltro);
*/
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Targa");
		elementoFiltro.setAttributeName("TARGA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.TARGA");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cognome");
		elementoFiltro.setAttributeName("COGNOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.COGNOME");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Nome");
		elementoFiltro.setAttributeName("NOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.NOME");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Ragione Sociale");
		elementoFiltro.setAttributeName("RAGIONESOCIALE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.RAGIONESOCIALE");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Codice Fiscale/P. Iva");
		elementoFiltro.setAttributeName("CODICEFISCALE_PIVA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.CODICEFISCALE_PIVA");
		listaElementiFiltro.add(elementoFiltro);
		
//		elementoFiltro = new EscElementoFiltro();
//		elementoFiltro.setLabel("Data Inizio");
//		elementoFiltro.setAttributeName("DT_INIZIO");
//		elementoFiltro.setTipo("D");
//		elementoFiltro.setCampoJS("controllaData");
//		elementoFiltro.setListaOperatori(operatoriNumerici);
//		elementoFiltro.setCampoFiltrato("T.DT_INIZIO");
//		listaElementiFiltro.add(elementoFiltro);

		
		//titolo gruppo
//		elementoFiltro = new EscElementoFiltro();
//		elementoFiltro.setLabel("DATI OGGETTI TERRITORIALI COLLEGATI");
//		elementoFiltro.setSoloLabel(true);
//		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Via / Piazza");
		elementoFiltro.setAttributeName("VIA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.INDIRIZZO");
		elementoFiltro.setExtraHTML("<span><input type=\"button\" class=\"TXTmainLabel\" value=\"Cerca\" onclick=\"javascript:danRequest('"+it.escsolution.escwebgis.common.EscServlet.URL_PATH+"/BolliVeicoli?listavie='+document.getElementById('VIA').value,'divListaVie')\"/></span><span id=\"divListaVie\" class=\"txtRow\"  ></span> \n");		
		listaElementiFiltro.add(elementoFiltro);
		
//		elementoFiltro = new EscElementoFiltro();
//		elementoFiltro.setLabel("Civico");
//		elementoFiltro.setAttributeName("CIVICO");
//		elementoFiltro.setTipo("S");
//		elementoFiltro.setCampoJS("");
//		elementoFiltro.setListaOperatori(operatoriStringaRid);
//		elementoFiltro.setCampoFiltrato("D.CIVICO");
//		listaElementiFiltro.add(elementoFiltro);
		
		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"bolli/BolliFrame.jsp", nav);

	}//-------------------------------------------------------------------------

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//prendere dalla request i parametri di ricerca impostati dall'utente
		 HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null){
			finder = (BolliVeicoliFinder)sessione.getAttribute(NOMEFINDER);
		}
		else{
			finder = null;
		}

		 finder = (BolliVeicoliFinder)gestireMultiPagina(finder,request);

		 BolliVeicoliLogic logic = new BolliVeicoliLogic(this.getEnvUtente(request));
		 Hashtable ht = logic.mCaricareLista(vettoreRicerca, finder);

		 Vector vct_lista = (Vector)ht.get(BolliVeicoliLogic.LISTA_BOLLI_VEICOLI);
		 finder = (BolliVeicoliFinder)ht.get(BolliVeicoliLogic.FINDER);

		 sessione.setAttribute(BolliVeicoliLogic.LISTA_BOLLI_VEICOLI, vct_lista);
		 sessione.setAttribute(BolliVeicoliLogic.FINDER, finder);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();
		
		this.chiamaPagina(request,response,"bolli/BolliFrame.jsp", nav);
	}//-------------------------------------------------------------------------

	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response, int tipo)throws Exception{
			
		// prendere dalla request il parametro identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio
		
		String azione = "";
		HttpSession sessione = request.getSession();

		removeOggettiIndiceDaSessione(sessione);
	
		if (sessione.getAttribute(NOMEFINDER) != null) {
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new BolliVeicoliFinder().getClass()) {
				finder = (BolliVeicoliFinder)sessione.getAttribute(NOMEFINDER);
			} else {
				finder = null;
			}
		}
		
		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		
		gestioneMultiRecord(request, response, azione, finder, sessione, BolliVeicoliLogic.LISTA_BOLLI_VEICOLI, (Vector)sessione.getAttribute(BolliVeicoliLogic.LISTA_BOLLI_VEICOLI), NOMEFINDER);
		
		if (!azione.equals("")) {
			Vector listaOggettiBolliVeicoli = (Vector)sessione.getAttribute(BolliVeicoliLogic.LISTA_BOLLI_VEICOLI);
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
		BolliVeicoliLogic logic = new BolliVeicoliLogic(this.getEnvUtente(request));
		Hashtable ht = null;
		
		//nel caso in cui provengo da Correlazione Vie/Civici e il parametro non inizia con #, e nel caso della Correlaione Fabbricati , allora l'id si riferisce all'oggetto e non al soggetto. 
		//In tutti gli altri casi l'id si riferisce al soggetto
		log.debug("ricerca dettaglio certificazioni da ID: " + oggettoSel );
		if (tipo == 101)
			ht = logic.mCaricareDettaglio(oggettoSel);	
		else if (tipo== 102 || tipo == 103 ){
			if (oggettoSel.startsWith("#") == false){
				//ht = logic.mCaricareDettaglioFromOggetto(oggettoSel);
				ht = logic.mCaricareDettaglio(oggettoSel);
			}
			else{
				oggettoSel= oggettoSel.substring(1);
				ht = logic.mCaricareDettaglio(oggettoSel);	
			}
		}
		else if (tipo == 105){
			//ht = logic.mCaricareDettaglioFromOggetto(oggettoSel);
		}
		else
			ht = logic.mCaricareDettaglio(oggettoSel);	
		
		BolloVeicolo bv = (BolloVeicolo)ht.get(BolliVeicoliLogic.BOLLI_VEICOLI);
		
		sessione.setAttribute(BolliVeicoliLogic.BOLLI_VEICOLI, bv);
		
		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		
		Vector<OggettoIndice> soggettiInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> vieInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> civiciInd = new Vector<OggettoIndice>();
		//Vector<OggettoIndice> fabbInd = new Vector<OggettoIndice>();
		//Vector<OggettoIndice> oggettiInd = new Vector<OggettoIndice>();
		
		if (bv != null && bv.getIndirizzo() != null && !bv.getIndirizzo().equalsIgnoreCase("")){
			OggettoIndice oi = new OggettoIndice();
			oi.setIdOrig(Long.toString(bv.getId()));
			oi.setFonte("46");
			oi.setProgr("1");
			oi.setDescrizione(bv.getIndirizzo());
			//civiciInd.add(oi);
			vieInd.add(oi);
		}
		
		if (bv != null && bv.getNumeroCivico() != null && !bv.getNumeroCivico().equalsIgnoreCase("")){
			OggettoIndice oi = new OggettoIndice();
			oi.setIdOrig(Long.toString(bv.getId()));
			oi.setFonte("46");
			oi.setProgr("1");
			oi.setDescrizione(bv.getIndirizzo() + " " + bv.getNumeroCivico() );
			civiciInd.add(oi);
			//vieInd.add(oi);
		}
		
		if (bv != null && ( 
				( bv.getCognome() != null && !bv.getCognome().equalsIgnoreCase("") ) || 
				( bv.getNome() != null && !bv.getNome().equalsIgnoreCase("") ) ||
				( bv.getRagioneSociale() != null && !bv.getRagioneSociale().equalsIgnoreCase("") ) ||
				( bv.getCodiceFiscalePiva() != null && !bv.getCodiceFiscalePiva().equalsIgnoreCase("") )
				) ){
			OggettoIndice oiSoggCert = new OggettoIndice();
			oiSoggCert.setIdOrig(Long.toString(bv.getId()));
			oiSoggCert.setFonte("46");
			oiSoggCert.setProgr("1");
			String testo = "";
			if (bv.getCognome() != null && !bv.getCognome().equalsIgnoreCase(""))
				testo += bv.getCognome();
			if (bv.getNome() != null && !bv.getNome().equalsIgnoreCase(""))
				testo += bv.getNome();
			if (bv.getRagioneSociale() != null && !bv.getRagioneSociale().equalsIgnoreCase(""))
				testo += bv.getRagioneSociale();
			if (bv.getCodiceFiscalePiva() != null && !bv.getCodiceFiscalePiva().equalsIgnoreCase(""))
				testo += " CF/PIVA: " + bv.getCodiceFiscalePiva() + " ";
			oiSoggCert.setDescrizione( testo );
			soggettiInd.add(oiSoggCert);
		}

//		if (tes != null && ( (tes.getFoglio() != null && !tes.getFoglio().equalsIgnoreCase("")) && (tes.getParticella() != null && !tes.getParticella().equalsIgnoreCase("")) && (tes.getSubTutti() != null && !tes.getSubTutti().equalsIgnoreCase("")) ) ){
//			OggettoIndice oiOgg = new OggettoIndice();
//			oiOgg.setIdOrig(  new Long(tes.getId()).toString() );
//			oiOgg.setFonte("45");
//			oiOgg.setProgr("1");
//			oiOgg.setDescrizione("F:" + tes.getFoglio() + " P:" + tes.getParticella() + " S:" + tes.getSubTutti());
//			oggettiInd.add(oiOgg);			
//		}
//
//		if (tes != null && ( (tes.getFoglio() != null && !tes.getFoglio().equalsIgnoreCase("")) && (tes.getParticella() != null && !tes.getParticella().equalsIgnoreCase("")) ) ){
//			OggettoIndice oiFabb = new OggettoIndice();
//			oiFabb.setIdOrig( new Long(tes.getId()).toString() );
//			oiFabb.setFonte("45");
//			oiFabb.setProgr("1");
//			oiFabb.setDescrizione("F:" + tes.getFoglio() + " P:" + tes.getParticella() );
//			fabbInd.add(oiFabb);			
//		}

		sessione.setAttribute("indice_soggetti", soggettiInd);
		sessione.setAttribute("indice_vie", vieInd);
		sessione.setAttribute("indice_civici", civiciInd);
//		sessione.setAttribute("indice_fabbricati", fabbInd);
//		sessione.setAttribute("indice_oggetti", oggettiInd);
		
		this.chiamaPagina(request,response,"bolli/BolliFrame.jsp", nav);
	}//-------------------------------------------------------------------------	

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo){
		return ((BolloVeicolo)listaOggetti.get(recordSuccessivo)).getChiave();
	}//-------------------------------------------------------------------------

	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (BolliVeicoliFinder)finder2;
		BolliVeicoliLogic logic = new BolliVeicoliLogic(this.getEnvUtente(request));
		return logic.mCaricareLista(this.vettoreRicerca,finder);
	}//-------------------------------------------------------------------------

	public EscFinder getNewFinder(){
		return new BolliVeicoliFinder();
	}//-------------------------------------------------------------------------

	public String getTema() {
		return "BOLLI_VEICOLI";
	}//-------------------------------------------------------------------------
	
	public String getTabellaPerCrossLink() {
		return "BOLLI_VEICOLI";
	}//-------------------------------------------------------------------------
	
	public String getLocalDataSource() {
		return localDataSource;
	}//-------------------------------------------------------------------------

}

