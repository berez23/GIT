package it.escsolution.escwebgis.demanio.servlet;

import it.escsolution.escwebgis.common.DiogeneException;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.demanio.bean.DemanioFinder;
import it.escsolution.escwebgis.demanio.bean.DettaglioBene;
import it.escsolution.escwebgis.demanio.logic.DemanioLogic;
import it.webred.fb.data.model.DmBIndirizzo;
import it.webred.fb.data.model.DmBMappale;
import it.webred.fb.ejb.dto.KeyValueDTO;
import it.webred.fb.ejb.dto.MappaleDTO;
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

public class DemanioServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private DemanioFinder finder = null;

	private final String NOMEFINDER = "FINDER132";
	private final String COD_FONTE = "42";
	
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
				mListaVie(request, response, "DM_B_INDIRIZZO", null, "DESCR_VIA", "TIPO_VIA");
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
		DemanioLogic logic = new DemanioLogic(this.getEnvUtente(request));
		
		Vector lstCatInv = logic.getListaCatInventariali();
		
		Vector listaElementiFiltro = new Vector();
		
		Vector operatori = new Vector();
		operatori.add(new EscOperatoreFiltro("=", "uguale"));
		
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
		elementoFiltro.setLabel("INVENTARIO");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cod.Inventario");
		elementoFiltro.setAttributeName("DATI INVENTARIO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("I.COD_INVENTARIO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Categoria");
		elementoFiltro.setAttributeName("CAT_INV");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("I.COD_CAT_INVENTARIALE");
		elementoFiltro.setListaValori(lstCatInv);
		listaElementiFiltro.add(elementoFiltro);
		
		//titolo gruppo
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("DATI CATASTALI");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setOperatore("=");
		elementoFiltro.setLabel("Foglio");
		elementoFiltro.setAttributeName("FOGLIO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatori);
		elementoFiltro.setCampoFiltrato("m.foglio");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setOperatore("=");
		elementoFiltro.setLabel("Particella");
		elementoFiltro.setAttributeName("MAPPALE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatori);
		elementoFiltro.setCampoFiltrato("m.mappale");
		listaElementiFiltro.add(elementoFiltro);
		
		//titolo gruppo
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("INDIRIZZO");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Comune");
		elementoFiltro.setAttributeName("COM_INDIRIZZO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("IND.DES_COMUNE");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Via");
		elementoFiltro.setAttributeName("VIA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("TRIM((IND.TIPO_VIA || ' ' || IND.DESCR_VIA))");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("n° civico");
		elementoFiltro.setAttributeName("CIVICO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("IND.CIVICO");
		listaElementiFiltro.add(elementoFiltro);
				
		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"demanio/DemanioFrame.jsp", nav);

	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//prendere dalla request i parametri di ricerca impostati dall'utente
		 HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null){
			finder = (DemanioFinder)sessione.getAttribute(NOMEFINDER);
		}
		else{
			finder = null;
		}

		 finder = (DemanioFinder)gestireMultiPagina(finder,request);

		 DemanioLogic logic = new DemanioLogic(this.getEnvUtente(request));
		 Hashtable ht = logic.mCaricareLista(vettoreRicerca, finder);

		 Vector vct_lista = (Vector)ht.get(DemanioLogic.LISTA_BENI);
		 finder = (DemanioFinder)ht.get(DemanioLogic.FINDER);

		 sessione.setAttribute(DemanioLogic.LISTA_BENI, vct_lista);
		 sessione.setAttribute(DemanioLogic.FINDER, finder);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();
		
		this.chiamaPagina(request,response,"demanio/DemanioFrame.jsp", nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response, int tipo)throws Exception{
			
		// prendere dalla request il parametro identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio
		
		String azione = "";
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
	
	
		if (sessione.getAttribute(NOMEFINDER) != null) {
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new DemanioFinder().getClass()) {
				finder = (DemanioFinder)sessione.getAttribute(NOMEFINDER);
			} else {
				finder = null;
			}
		}
		
		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		
		gestioneMultiRecord(request, response, azione, finder, sessione, DemanioLogic.LISTA_BENI, (Vector)sessione.getAttribute(DemanioLogic.LISTA_BENI), NOMEFINDER);
		
		if (!azione.equals("")) {
			Vector listaOggetti = (Vector)sessione.getAttribute(DemanioLogic.LISTA_BENI);
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
		DemanioLogic logic = new DemanioLogic(this.getEnvUtente(request));
		Hashtable ht = null;
		
		//nel caso in cui provengo da Correlazione Vie/Civici e il parametro non inizia con #, e nel caso della Correlaione Fabbricati , allora l'id si riferisce all'oggetto e non al soggetto. 
		//In tutti gli altri casi l'id si riferisce al soggetto
		log.debug("ricerca dettaglio demanio comunale da ID: " + oggettoSel );
		if (tipo== 102 || tipo == 103 )
			ht = logic.mCaricareDettaglioFromIndirizzo(oggettoSel);	
		else if (tipo == 105 || tipo==104) //Immobile e Fabbricato
			ht = logic.mCaricareDettaglioFromMappale(oggettoSel);	
		else
			ht = logic.mCaricareDettaglio(oggettoSel);	
		
		DettaglioBene b = (DettaglioBene)ht.get(DemanioLogic.BENE);
		
		sessione.setAttribute(DemanioLogic.BENE, b);
		
		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		
		Vector<OggettoIndice> soggettiInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> vieInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> civiciInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> fabbInd = new Vector<OggettoIndice>();
		Vector<OggettoIndice> oggettiInd = new Vector<OggettoIndice>();
		
		OggettoIndice oi = new OggettoIndice();
		OggettoIndice oic = new OggettoIndice();
		//Aggiungo le vie/civic degli immobili a ruolo
		String belfiore = this.getEnvUtente(request).getEnte();
		List<DmBIndirizzo> lstInd = b.getIndirizzi();
		List<String> lstUsati = new ArrayList<String>();
		for(DmBIndirizzo r : lstInd){
			String indirizzo = (r.getTipoVia()!=null ? r.getTipoVia()+" " : "")+r.getDescrVia();
			if(indirizzo!=null && belfiore.equalsIgnoreCase(r.getCodComune())){
				indirizzo = indirizzo.trim().replace("°", "."); // simbolo del grado
				oi = new OggettoIndice();
				oi.setIdOrig(r.getId());
				oi.setFonte("42");
				oi.setProgr("1");
				oi.setDescrizione(indirizzo);
				
				if(!lstUsati.contains(indirizzo)){
					lstUsati.add(indirizzo);
					vieInd.add(oi);
				}
				
				if(r.getCivico()!=null){
					String descrizione = indirizzo+", "+r.getCivico();
					
					oic = new OggettoIndice();
					oic.setIdOrig(r.getId());
					oic.setFonte("42");
					oic.setProgr("1");
					oic.setDescrizione(descrizione);
					
					if(!lstUsati.contains(descrizione)){
						lstUsati.add(descrizione);
						civiciInd.add(oic);
					}
					
				}
			}
		}
		
		//Aggiungo i fabbricati 
		List<MappaleDTO> lstImm = b.getMappali();
		lstUsati = new ArrayList<String>();
		for(MappaleDTO imm : lstImm){
			String foglio = imm.getFoglio();
			String particella = imm.getMappale();
			if(!(foglio==null && particella==null) && belfiore.equalsIgnoreCase(imm.getCodComune())){
				foglio = foglio!=null ? foglio : "0";
				particella = particella!=null? particella : "0";
				String descrizione = "F:" + foglio + " P:" + particella;
				if(!lstUsati.contains(descrizione)){
					oi = new OggettoIndice();
					oi.setIdOrig(imm.getId());
					oi.setFonte("42");
					oi.setProgr("2");
					oi.setDescrizione(descrizione);
					fabbInd.add(oi);
					lstUsati.add(descrizione);
				}
			}
			
		}
		
		sessione.setAttribute("indice_soggetti", soggettiInd);
		sessione.setAttribute("indice_vie", vieInd);
		sessione.setAttribute("indice_civici", civiciInd);
		sessione.setAttribute("indice_fabbricati", fabbInd);
		sessione.setAttribute("indice_oggetti", oggettiInd);
		
		this.chiamaPagina(request,response,"demanio/DemanioFrame.jsp", nav);
	}	


	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo){
		return Long.toString(((DettaglioBene)listaOggetti.get(recordSuccessivo)).getBene().getId());
	}

	
	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (DemanioFinder)finder2;
		DemanioLogic logic = new DemanioLogic(this.getEnvUtente(request));
		return logic.mCaricareLista(this.vettoreRicerca,finder);
	}

	public EscFinder getNewFinder(){
		return new DemanioFinder();
	}

	public String getTema() {
		return "DEMANIO COMUNALE";
	}
	
	public String getTabellaPerCrossLink() {
		return "";
	}
	
	public String getLocalDataSource() {
		return localDataSource;
	}

}

