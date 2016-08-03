package it.escsolution.escwebgis.ruolo.servlet;

import it.escsolution.escwebgis.common.DiogeneException;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.ruolo.bean.RuoloComboObject;
import it.escsolution.escwebgis.ruolo.bean.RuoloFinder;
import it.escsolution.escwebgis.ruolo.logic.RuoloTarsuLogic;
import it.webred.ct.data.access.basic.ruolo.tarsu.dto.RuoloTarsuDTO;
import it.webred.ct.data.model.ruolo.tarsu.SitRuoloTarsu;
import it.webred.ct.data.model.ruolo.tarsu.SitRuoloTarsuImm;
import it.webred.indice.OggettoIndice;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RuoloTarsuServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private RuoloFinder finder = null;

	private final String NOMEFINDER = "FINDER128";
	private final String COD_FONTE = "39";
	
	private String localDataSource = "jdbc/Diogene_DS";
	
	
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
	
	public void EseguiServizio(
			HttpServletRequest request,HttpServletResponse response)
					throws ServletException, IOException {

		
		//Gestione Download File
		String strFilee = (String) request.getParameter("strFilee");
		
		if(strFilee!=null){
			File image = null;
			int formato=0;
			InputStream is = null;
			InputStream isByte = null;
			ByteArrayOutputStream baos=null;
			try{
			
			//Apertura file PDF Generico (dato il PATH completo)
				
				image = new File(strFilee);
				String nomeFile = image.getName();
				isByte = new FileInputStream(image);
				OutputStream out = response.getOutputStream();
				response.setHeader("Content-Disposition","inline; attachment; filename=\"" + nomeFile + ".pdf" + "\"");
				response.setContentType("application/pdf");
				
				byte[] b = new byte[1024];
	            while ( isByte.read( b ) != -1 )
	            {
	                out.write( b );
	            }
			
			} catch (FileNotFoundException e) {
				log.error("File richiesto non trovato " + e.getMessage(),e);
				throw new DiogeneException("File richiesto non trovato");
			}finally{
				isByte.close();
			}
		}else{
			
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
		
	}
    

	public void _EseguiServizio(
			HttpServletRequest request,HttpServletResponse response)
					throws ServletException, IOException {

		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		super.EseguiServizio(request,response);
		
		try {
			if (request.getParameter("listavie") != null)
				mListaVie(request, response, "SIT_RUOLO_TARSU", null, "INDIRIZZO", "VIA");
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
					case 39:
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
		RuoloTarsuLogic logic = new RuoloTarsuLogic(this.getEnvUtente(request));
		
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
		elementoFiltro.setLabel("DATI RUOLO TARSU");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		
		Vector<RuoloComboObject> vctFlag = new Vector<RuoloComboObject>();
		//lista fissa
		vctFlag.add(new RuoloComboObject("", "Tutti"));
		vctFlag.add(new RuoloComboObject("1", "Si"));
		vctFlag.add(new RuoloComboObject("0", "No"));
		
		Vector<RuoloComboObject> vctTipo = new Vector<RuoloComboObject>();
		//lista fissa
		vctTipo.add(new RuoloComboObject("", "Tutti"));
		vctTipo.add(new RuoloComboObject("A", "Principale"));
		vctTipo.add(new RuoloComboObject("S", "Conguaglio"));
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cod.Utente");
		elementoFiltro.setAttributeName("CU");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("T.CU");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cod.Fiscale");
		elementoFiltro.setAttributeName("CF");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.CODFISC");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Nominativo");
		elementoFiltro.setAttributeName("NOMINATIVO_CONTRIB");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.NOMINATIVO_CONTRIB");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Anno Riferimento");
		elementoFiltro.setAttributeName("ANNO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("T.ANNO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Tipo");
		elementoFiltro.setAttributeName("TIPO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vctTipo);
		elementoFiltro.setCampoFiltrato("T.TIPO");
		listaElementiFiltro.add(elementoFiltro);
			
		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"ruolo/RuoloTarsuFrame.jsp", nav);

	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//prendere dalla request i parametri di ricerca impostati dall'utente
		 HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null){
			finder = (RuoloFinder)sessione.getAttribute(NOMEFINDER);
		}
		else{
			finder = null;
		}

		 finder = (RuoloFinder)gestireMultiPagina(finder,request);

		 RuoloTarsuLogic logic = new RuoloTarsuLogic(this.getEnvUtente(request));
		 Hashtable ht = logic.mCaricareLista(vettoreRicerca, finder);

		 Vector vct_lista = (Vector)ht.get(RuoloTarsuLogic.LISTA_RUOLI);
		 finder = (RuoloFinder)ht.get(RuoloTarsuLogic.FINDER);

		 sessione.setAttribute(RuoloTarsuLogic.LISTA_RUOLI, vct_lista);
		 sessione.setAttribute(RuoloTarsuLogic.FINDER, finder);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();
		
		this.chiamaPagina(request,response,"ruolo/RuoloTarsuFrame.jsp", nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response, int tipo)throws Exception{
			
		// prendere dalla request il parametro identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio
		
		String azione = "";
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
	
	
		if (sessione.getAttribute(NOMEFINDER) != null) {
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new RuoloFinder().getClass()) {
				finder = (RuoloFinder)sessione.getAttribute(NOMEFINDER);
			} else {
				finder = null;
			}
		}
		
		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		
		gestioneMultiRecord(request, response, azione, finder, sessione, RuoloTarsuLogic.LISTA_RUOLI, (Vector)sessione.getAttribute(RuoloTarsuLogic.LISTA_RUOLI), NOMEFINDER);
		
		if (!azione.equals("")) {
			Vector listaOggetti = (Vector)sessione.getAttribute(RuoloTarsuLogic.LISTA_RUOLI);
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
		RuoloTarsuLogic logic = new RuoloTarsuLogic(this.getEnvUtente(request));
		Hashtable ht = null;
		
		//nel caso in cui provengo da Correlazione Vie/Civici e il parametro non inizia con #, e nel caso della Correlaione Fabbricati , allora l'id si riferisce all'oggetto e non al soggetto. 
		//In tutti gli altri casi l'id si riferisce al soggetto
		log.debug("ricerca dettaglio ruolo tarsu da ID: " + oggettoSel );
		if (tipo== 102 || tipo == 103 ){
			if (oggettoSel.contains("|"))
				ht = logic.mCaricareDettaglioFromImm(oggettoSel);
			else
				ht = logic.mCaricareDettaglio(oggettoSel);	
		}
		else if (tipo == 105 || tipo==104)
			ht = logic.mCaricareDettaglioFromImm(oggettoSel);	
		else
			ht = logic.mCaricareDettaglio(oggettoSel);	
		
		RuoloTarsuDTO tes = (RuoloTarsuDTO)ht.get(RuoloTarsuLogic.RUOLO);
		
		sessione.setAttribute(RuoloTarsuLogic.RUOLO, tes);
		
		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		
		Vector<OggettoIndice> soggettiInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> vieInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> civiciInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> fabbInd = new Vector<OggettoIndice>();
		
		// Aggiungo Soggetti per correlazione
		SitRuoloTarsu r = tes.getRuolo();
		OggettoIndice oi = new OggettoIndice();
		oi.setIdOrig(r.getId());
		oi.setFonte("39");
		oi.setProgr("1");
		String descr = (r.getNominativoContrib()!=null ? r.getNominativoContrib(): "")+" (c.f. "+r.getCodfisc()+")";
		oi.setDescrizione(descr.trim());
		soggettiInd.add(oi);
		
		List<String> lstUsati = new ArrayList<String>();
		//Aggiungo la via di residenza
		if(r.getIndirizzo()!=null){
			String indirizzo = r.getIndirizzo().replace("°", "."); // simbolo del grado
			oi = new OggettoIndice();
			oi.setIdOrig(r.getId());
			oi.setFonte("39");
			oi.setProgr("1");
			oi.setDescrizione(indirizzo);
			lstUsati.add(indirizzo);
			vieInd.add(oi);
			civiciInd.add(oi);
		}
		//Aggiungo le vie/civic degli immobili a ruolo
		List<SitRuoloTarsuImm> lstImm = tes.getImmobili();
		for(SitRuoloTarsuImm imm : lstImm){
			String indirizzo = imm.getIndirizzo();
			if(indirizzo!=null && !lstUsati.contains(indirizzo)){
				oi = new OggettoIndice();
				oi.setIdOrig(r.getId());
				oi.setFonte("39");
				oi.setProgr("2");
				oi.setDescrizione(indirizzo);
				vieInd.add(oi);
				civiciInd.add(oi);
				lstUsati.add(indirizzo);
			}
		}
		
		sessione.setAttribute("indice_soggetti", soggettiInd);
		sessione.setAttribute("indice_vie", vieInd);
		sessione.setAttribute("indice_civici", civiciInd);
		sessione.setAttribute("indice_fabbricati", fabbInd);
		
		this.chiamaPagina(request,response,"ruolo/RuoloTarsuFrame.jsp", nav);
	}	


	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo){
		return ((RuoloTarsuDTO)listaOggetti.get(recordSuccessivo)).getRuolo().getId();
	}

	
	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (RuoloFinder)finder2;
		RuoloTarsuLogic logic = new RuoloTarsuLogic(this.getEnvUtente(request));
		return logic.mCaricareLista(this.vettoreRicerca,finder);
	}

	public EscFinder getNewFinder(){
		return new RuoloFinder();
	}

	public String getTema() {
		return "RUOLO TARSU";
	}
	
	public String getTabellaPerCrossLink() {
		return "SIT_RUOLO_TARSU";
	}
	
	public String getLocalDataSource() {
		return localDataSource;
	}

}

