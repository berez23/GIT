package it.escsolution.escwebgis.cened.servlet;

import it.escsolution.escwebgis.cened.bean.CertificazioniEnergeticheFinder;
import it.escsolution.escwebgis.cened.logic.CertificazioniEnergeticheLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.toponomastica.bean.Civico;
import it.webred.ct.data.model.datitecnicifabbricato.CertificazioneEnergetica;
import it.webred.indice.OggettoIndice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CertificazioniEnergeticheServlet extends EscServlet implements EscService {

	private static final long serialVersionUID = 1333046214903114621L;
	
	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private CertificazioniEnergeticheFinder finder = null;

	private final String NOMEFINDER = "FINDER135";
	
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
				mListaVie(request, response, "DATI_TEC_FABBR_CERT_ENER", null, "INDIRIZZO", "VIA");
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
		elementoFiltro.setLabel("DATI CERTIFICAZIONI ENERGETICHE");
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
		elementoFiltro.setLabel("Foglio");
		elementoFiltro.setAttributeName("FOGLIO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.FOGLIO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Particella");
		elementoFiltro.setAttributeName("PARTICELLA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.PARTICELLA");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Subalterno");
		elementoFiltro.setAttributeName("SUB_TUTTI");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.SUB_TUTTI");
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
		elementoFiltro.setExtraHTML("<span><input type=\"button\" class=\"TXTmainLabel\" value=\"Cerca\" onclick=\"javascript:danRequest('"+it.escsolution.escwebgis.common.EscServlet.URL_PATH+"/CertificazioniEnergetiche?listavie='+document.getElementById('VIA').value,'divListaVie')\"/></span><span id=\"divListaVie\" class=\"txtRow\"  ></span> \n");		
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
		this.chiamaPagina(request,response,"cened/CenedFrame.jsp", nav);

	}//-------------------------------------------------------------------------

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//prendere dalla request i parametri di ricerca impostati dall'utente
		 HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null){
			finder = (CertificazioniEnergeticheFinder)sessione.getAttribute(NOMEFINDER);
		}
		else{
			finder = null;
		}

		 finder = (CertificazioniEnergeticheFinder)gestireMultiPagina(finder,request);

		 CertificazioniEnergeticheLogic logic = new CertificazioniEnergeticheLogic(this.getEnvUtente(request));
		 Hashtable ht = logic.mCaricareLista(vettoreRicerca, finder);

		 Vector vct_lista = (Vector)ht.get(CertificazioniEnergeticheLogic.LISTA_CENED);
		 finder = (CertificazioniEnergeticheFinder)ht.get(CertificazioniEnergeticheLogic.FINDER);

		 sessione.setAttribute(CertificazioniEnergeticheLogic.LISTA_CENED, vct_lista);
		 sessione.setAttribute(CertificazioniEnergeticheLogic.FINDER, finder);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();
		
		this.chiamaPagina(request,response,"cened/CenedFrame.jsp", nav);
	}//-------------------------------------------------------------------------

	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response, int tipo)throws Exception{
			
		// prendere dalla request il parametro identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio
		
		String azione = "";
		HttpSession sessione = request.getSession();

		removeOggettiIndiceDaSessione(sessione);
	
		if (sessione.getAttribute(NOMEFINDER) != null) {
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new CertificazioniEnergeticheFinder().getClass()) {
				finder = (CertificazioniEnergeticheFinder)sessione.getAttribute(NOMEFINDER);
			} else {
				finder = null;
			}
		}
		
		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		
		gestioneMultiRecord(request, response, azione, finder, sessione, CertificazioniEnergeticheLogic.LISTA_CENED, (Vector)sessione.getAttribute(CertificazioniEnergeticheLogic.LISTA_CENED), NOMEFINDER);
		
		if (!azione.equals("")) {
			Vector listaOggettiCened = (Vector)sessione.getAttribute(CertificazioniEnergeticheLogic.LISTA_CENED);
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
		CertificazioniEnergeticheLogic logic = new CertificazioniEnergeticheLogic(this.getEnvUtente(request));
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
		
		CertificazioneEnergetica tes = (CertificazioneEnergetica)ht.get(CertificazioniEnergeticheLogic.CENED);
		
		sessione.setAttribute(CertificazioniEnergeticheLogic.CENED, tes);
		
		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		
		Vector<OggettoIndice> soggettiInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> vieInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> civiciInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> fabbInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> oggettiInd = new Vector<OggettoIndice>();
		
		if (tes != null && tes.getIndirizzo() != null && !tes.getIndirizzo().equalsIgnoreCase("")){
			OggettoIndice oi = new OggettoIndice();
			oi.setIdOrig(Long.toString(tes.getId()));
			oi.setFonte("45");
			oi.setProgr("1");
			oi.setDescrizione(tes.getIndirizzo());
			civiciInd.add(oi);
			vieInd.add(oi);
		}
		
		if (tes != null && ( (tes.getDenomCertificatore() != null && !tes.getDenomCertificatore().equalsIgnoreCase("")) || (tes.getCfPivaCertificatore() != null && !tes.getCfPivaCertificatore().equalsIgnoreCase("")) ) ){
			OggettoIndice oiSoggCert = new OggettoIndice();
			oiSoggCert.setIdOrig(Long.toString(tes.getId()));
			oiSoggCert.setFonte("45");
			oiSoggCert.setProgr("3");
			String testo = "";
			if (tes.getDenomCertificatore() != null && !tes.getDenomCertificatore().equalsIgnoreCase(""))
				testo += tes.getDenomCertificatore();
			if (tes.getCfPivaCertificatore() != null && !tes.getCfPivaCertificatore().equalsIgnoreCase(""))
				testo += " CF/PIVA: " + tes.getCfPivaCertificatore() + " ";
			oiSoggCert.setDescrizione( testo );
			soggettiInd.add(oiSoggCert);
		}

		if (tes != null && ( (tes.getDenomProprietario() != null && !tes.getDenomProprietario().equalsIgnoreCase("")) || (tes.getCfPivaProprietario() != null && !tes.getCfPivaProprietario().equalsIgnoreCase("")) ) ){
			OggettoIndice oiSoggProp = new OggettoIndice();
			oiSoggProp.setIdOrig(  new Long(tes.getId()).toString() );
			oiSoggProp.setFonte("45");
			oiSoggProp.setProgr("2");
			String testo = "";
			if (tes.getDenomProprietario() != null && !tes.getDenomProprietario().equalsIgnoreCase(""))
				testo += tes.getDenomCertificatore();
			if (tes.getCfPivaProprietario() != null && !tes.getCfPivaProprietario().equalsIgnoreCase(""))
				testo += " CF/PIVA: " + tes.getCfPivaProprietario() + " ";
			oiSoggProp.setDescrizione( testo );
			soggettiInd.add(oiSoggProp);			
		}

		if (tes != null && ( (tes.getFoglio() != null && !tes.getFoglio().equalsIgnoreCase("")) && (tes.getParticella() != null && !tes.getParticella().equalsIgnoreCase("")) && (tes.getSubTutti() != null && !tes.getSubTutti().equalsIgnoreCase("")) ) ){
			OggettoIndice oiOgg = new OggettoIndice();
			oiOgg.setIdOrig(  new Long(tes.getId()).toString() );
			oiOgg.setFonte("45");
			oiOgg.setProgr("1");
			oiOgg.setDescrizione("F:" + tes.getFoglio() + " P:" + tes.getParticella() + " S:" + tes.getSubTutti());
			oggettiInd.add(oiOgg);			
		}

		if (tes != null && ( (tes.getFoglio() != null && !tes.getFoglio().equalsIgnoreCase("")) && (tes.getParticella() != null && !tes.getParticella().equalsIgnoreCase("")) ) ){
			OggettoIndice oiFabb = new OggettoIndice();
			oiFabb.setIdOrig( new Long(tes.getId()).toString() );
			oiFabb.setFonte("45");
			oiFabb.setProgr("1");
			oiFabb.setDescrizione("F:" + tes.getFoglio() + " P:" + tes.getParticella() );
			fabbInd.add(oiFabb);			
		}

		
		sessione.setAttribute("indice_soggetti", soggettiInd);
		sessione.setAttribute("indice_vie", vieInd);
		sessione.setAttribute("indice_civici", civiciInd);
		sessione.setAttribute("indice_fabbricati", fabbInd);
		sessione.setAttribute("indice_oggetti", oggettiInd);
		
		this.chiamaPagina(request,response,"cened/CenedFrame.jsp", nav);
	}//-------------------------------------------------------------------------	


	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo){
		return ((CertificazioneEnergetica)listaOggetti.get(recordSuccessivo)).getChiave();
	}//-------------------------------------------------------------------------

	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (CertificazioniEnergeticheFinder)finder2;
		CertificazioniEnergeticheLogic logic = new CertificazioniEnergeticheLogic(this.getEnvUtente(request));
		return logic.mCaricareLista(this.vettoreRicerca,finder);
	}//-------------------------------------------------------------------------

	public EscFinder getNewFinder(){
		return new CertificazioniEnergeticheFinder();
	}//-------------------------------------------------------------------------

	public String getTema() {
		return "CENED";
	}//-------------------------------------------------------------------------
	
	public String getTabellaPerCrossLink() {
		return "DATI_TEC_FABBR_CERT_ENER";
	}//-------------------------------------------------------------------------
	
	public String getLocalDataSource() {
		return localDataSource;
	}//-------------------------------------------------------------------------

}

