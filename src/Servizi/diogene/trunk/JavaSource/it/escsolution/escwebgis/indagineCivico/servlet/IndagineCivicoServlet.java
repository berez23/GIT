package it.escsolution.escwebgis.indagineCivico.servlet;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import it.escsolution.escwebgis.catasto.logic.CatastoPlanimetrieComma340Logic;
import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.docfa.logic.DocfaLogic;
import it.escsolution.escwebgis.indagineCivico.bean.DatiDocfa;
import it.escsolution.escwebgis.indagineCivico.bean.IndagineCivicoFinder;
import it.escsolution.escwebgis.indagineCivico.bean.Indirizzo;
import it.escsolution.escwebgis.indagineCivico.logic.IndagineCivicoLogic;
//import it.escsolution.escwebgis.tributi.bean.OggettiTARSU;


public class IndagineCivicoServlet extends EscServlet implements EscService {
	private String recordScelto;
	private IndagineCivicoFinder finder = null;  
	private IndagineCivicoLogic logic = null;
	private final String urlPage="indagineCivico/indagineCivicoFrame.jsp";
	PulsantiNavigazione nav = new PulsantiNavigazione();
	String pathPlanimetrieComma340 = "";

	public void init(ServletConfig config) throws ServletException {
        super.init(config);
        pathPlanimetrieComma340 = config.getInitParameter("pathPlanimetrieComma340");
    }
	public void EseguiServizio(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		super.EseguiServizio(request,response);
		try{
			
			
			if (request.getParameter("listavie") != null)
				//mListaVie(request, response, "SIT_T_VIE","VIADES","VIA");	
				mListaVie(request, response, "SITIDSTR", null, "NOME","VIA");	
			else  
			if (request.getParameter("popupCens") != null)
				mPopupDatiCensuari(request, response);
			else {
				switch (st){
				case 1:
					// carico la form di ricerca
					pulireSessione(request);
					mCaricareFormRicerca(request,response);
					break;
				case 2:
					// vengo dalla pagina di ricerca --> predispongo la lista e la passo alla pagina di lista
					mCaricareLista(request,response); 
					break;
				case 3:
					// ho cliccato su un elemento --> visualizzo il dettaglio
					if (request.getParameter("popup") == null || !request.getParameter("popup").equals("true")) {
						mCaricareDettaglio(request,response,false); 
						//this.leggiCrossLink(request);
					}
					else
						mExportXls(request,response);
					break;
				case 4:
					// vengo dalla servlet di autenticazione per app esterne --> cerco e visualizzo il dettaglio della coppia codice via + civico 
					mCaricareDettaglioByParams(request,response); 
					break;
				case 33:
					// ho cliccato su un elemento --> visualizzo il dettaglio
					if (request.getParameter("popup") == null || !request.getParameter("popup").equals("true")) {
						mCaricareDettaglio(request,response,false); 
						//this.leggiCrossLink(request);
					}
					else
						mExportXls(request,response);
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

	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception {
		finder = (IndagineCivicoFinder)finder2;
		IndagineCivicoLogic logic = new IndagineCivicoLogic(this.getEnvUtente(request));
		return logic.mCaricareListaIndCivico(this.vettoreRicerca, finder);
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo) {
		Indirizzo ind = (Indirizzo)listaOggetti.get(recordSuccessivo);
		String key = ind.getChiave();
		return key;
	}

	public EscFinder getNewFinder() {
		return new IndagineCivicoFinder();
	}

	public String getTabellaPerCrossLink() {
		//ALMENO PER IL MOMENTO NESSUN CROSSLINK
		return null;
	}

	public String getTema() {
		return "Fascicoli";
	}
	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//caricare dati che servono nella pagina di ricerca --> eventuali combo

		/** Comune */
		Vector vctComuni = new ComuniLogic(this.getEnvUtente(request)).getListaComuniUtente(request.getUserPrincipal().getName());
		/***/

		HttpSession sessione = request.getSession();


		 // chiamare la pagina di ricerca

		Vector listaElementiFiltro = new Vector();
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=","uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like","contiene"));


		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=","uguale"));
		
		Vector operatoriNumerici = new Vector();
		operatoriNumerici.add(new EscOperatoreFiltro("=","="));
		operatoriNumerici.add(new EscOperatoreFiltro("<>","<>"));
		operatoriNumerici.add(new EscOperatoreFiltro(">",">"));
		operatoriNumerici.add(new EscOperatoreFiltro("<","<"));

       	// costruisce il vettore di elementi per la pagina di ricerca
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Codice Strada");
		elementoFiltro.setAttributeName("CODICE_STRADA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("numeroIntero");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("V.PKID_STRA");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Via");
		elementoFiltro.setAttributeName("VIA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("V.NOME");
		elementoFiltro.setExtraHTML("<span><input type=\"button\" class=\"TXTmainLabel\" value=\"Cerca\" onclick=\"javascript:danRequest('"+it.escsolution.escwebgis.common.EscServlet.URL_PATH+"/IndagineCivico?listavie='+document.getElementById('VIA').value,'divListaVie')\"/></span><span id=\"divListaVie\" class=\"txtRow\"  ></span> \n");		
		listaElementiFiltro.add(elementoFiltro);

		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Numero Civico");
		elementoFiltro.setAttributeName("CIVICO");
		elementoFiltro.setTipo("S");
		//elementoFiltro.setCampoJS("numeroIntero");
		elementoFiltro.setCampoJS(""); 
		elementoFiltro.setListaOperatori(operatoriStringa); //todo decidere se prevedere solo uguale 
		elementoFiltro.setCampoFiltrato("C.CIVICO");
		listaElementiFiltro.add(elementoFiltro);


		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,urlPage, nav);
	}
	
	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();
		if (sessione.getAttribute(IndagineCivicoLogic.FINDER)!= null){
			finder = (IndagineCivicoFinder)sessione.getAttribute(IndagineCivicoLogic.FINDER);
		}
		else{
			finder = null;
		}

		finder = (IndagineCivicoFinder)gestireMultiPagina(finder,request);

		IndagineCivicoLogic logic = new IndagineCivicoLogic(this.getEnvUtente(request)); 
		Hashtable ht = logic.mCaricareListaIndCivico(vettoreRicerca,finder);  
		Vector vct_lista= (Vector)ht.get(IndagineCivicoLogic.LISTA_CIVICI);
		
		finder = (IndagineCivicoFinder)ht.get(IndagineCivicoLogic.FINDER);
		sessione.setAttribute(IndagineCivicoLogic.LISTA_CIVICI,vct_lista);
		sessione.setAttribute(IndagineCivicoLogic.FINDER,finder);
		/*
		if (vct_lista.size() == 1) {
			request.setAttribute("OGGETTO_SEL", ((Indirizzo)vct_lista.get(0)).getChiave());
			request.setAttribute("RECORD_SEL", "1");
			request.setAttribute("ST", "3"); // TODO: NON BASTA perché st di EscServlet viene impostato all'inizio con quello recuperato come pa della request  e poi in chiamaPagina messo in seesione con il valore recupearto all'inizio ..
			request.setAttribute("AZIONE", "");
			mCaricareDettaglio(request,response,false);
			return;
		}
		 */
		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();

		 this.chiamaPagina(request,response,urlPage, nav);

	}//-------------------------------------------------------------------------
	
	private void mCaricareDettaglioByParams(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();
//		if (sessione.getAttribute(IndagineCivicoLogic.FINDER)!= null){
//			finder = (IndagineCivicoFinder)sessione.getAttribute(IndagineCivicoLogic.FINDER);
//		}
//		else{
//			finder = null;
//		}

//		finder = (IndagineCivicoFinder)gestireMultiPagina(finder,request);

		IndagineCivicoLogic logic = new IndagineCivicoLogic(this.getEnvUtente(request)); 
		/*
		 * Carico gli elemnti filtro a runtime visto che non passiamo dalla JSP 
		 */
		String ente = request.getParameter("ente");
		String codVia = request.getParameter("cod_via");
		String civico = request.getParameter("civico");
		vettoreRicerca = new Vector<String>();
		vettoreRicerca.add(ente);
		vettoreRicerca.add(codVia);
		vettoreRicerca.add(civico);
		Hashtable ht = logic.mCaricareListaIndCivicoByParams(vettoreRicerca);  
		Vector<Indirizzo> vct_lista= (Vector)ht.get(IndagineCivicoLogic.LISTA_CIVICI);
		
		finder = (IndagineCivicoFinder)ht.get(IndagineCivicoLogic.FINDER);
		sessione.setAttribute(IndagineCivicoLogic.LISTA_CIVICI,vct_lista);
		sessione.setAttribute(IndagineCivicoLogic.FINDER,finder);
		if (vct_lista != null && vct_lista.size()>0){
			Indirizzo indirizzo = (Indirizzo)vct_lista.get(0);
			oggettoSel = new Integer(indirizzo.getPkidCivi()).toString();
			//TODO: recuperare dal file properties il parametro che indica se la codifica del viario SITI
			// e quella del viario anagrafico sono congruenti
			boolean isCodificaViarioCongruente=true;
			ht = logic.mCaricareDettaglio(oggettoSel, isCodificaViarioCongruente);
			if(ht.get(IndagineCivicoLogic.INDIRIZZO) != null)
				sessione.setAttribute(IndagineCivicoLogic.INDIRIZZO,ht.get(IndagineCivicoLogic.INDIRIZZO));
			if(ht.get(IndagineCivicoLogic.LISTA_RESIDENTI) != null)
				sessione.setAttribute(IndagineCivicoLogic.LISTA_RESIDENTI,ht.get(IndagineCivicoLogic.LISTA_RESIDENTI));
			if(ht.get(IndagineCivicoLogic.LISTA_UI_CON_TITOLARE) != null)
				sessione.setAttribute(IndagineCivicoLogic.LISTA_UI_CON_TITOLARE,ht.get(IndagineCivicoLogic.LISTA_UI_CON_TITOLARE));
			if(ht.get(IndagineCivicoLogic.LISTA_UI_CONSISTENZA) != null)
				sessione.setAttribute(IndagineCivicoLogic.LISTA_UI_CONSISTENZA,ht.get(IndagineCivicoLogic.LISTA_UI_CONSISTENZA));
			
		}else{
			/*
			 * Codice via + civico non ha prodotto risultati
			 */
		}
		
		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();

		 this.chiamaPagina(request,response,urlPage, nav);

	}//-------------------------------------------------------------------------
	
	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response,boolean soloDettaglio)throws Exception{
		// prendere dalla request il parametrio identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio
		String azione = "";
		HttpSession sessione = request.getSession();
		//boolean chiamataEsterna = false;
		if (sessione.getAttribute(IndagineCivicoLogic.FINDER) !=null){
			if (((Object)sessione.getAttribute(IndagineCivicoLogic.FINDER)).getClass() == new IndagineCivicoFinder().getClass()){
				finder = (IndagineCivicoFinder)sessione.getAttribute(IndagineCivicoLogic.FINDER);
			}
			else{
				// il finder non corrisponde -->
				//sessione.removeAttribute(IndagineCivicoLogic.FINDER);
				finder = null;
			}
		}
		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		else if (request.getAttribute("AZIONE")!= null)
			azione = (String) request.getAttribute("AZIONE");
		gestioneMultiRecord(request,response,azione,finder,sessione,IndagineCivicoLogic.LISTA_CIVICI,(Vector)sessione.getAttribute(IndagineCivicoLogic.LISTA_CIVICI),IndagineCivicoLogic.FINDER);
		if (!azione.equals("")){
			Vector listaCivici = (Vector)sessione.getAttribute(IndagineCivicoLogic.LISTA_CIVICI);
		}
		else{
			oggettoSel = "";recordScelto = "";
			sessione.removeAttribute("BACK_JS_COUNT");
			sessione.removeAttribute("BACK_RECORD_ENABLE");

			if (request.getParameter("OGGETTO_SEL")!= null){
				oggettoSel = request.getParameter("OGGETTO_SEL");
			}else if (request.getAttribute("OGGETTO_SEL")!= null){
				oggettoSel = (String) request.getAttribute("OGGETTO_SEL");
			} 
			
			if (request.getParameter("RECORD_SEL")!= null || request.getAttribute("RECORD_SEL")!= null ){
				if (request.getParameter("RECORD_SEL")!= null )
					recordScelto = request.getParameter("RECORD_SEL");
				else 
					recordScelto = (String) request.getAttribute("RECORD_SEL");
				if (finder!=null)
					finder.setRecordAttuale(new Long(recordScelto).longValue());
			}
					
		}
	  //carica il dettaglio
		IndagineCivicoLogic logic = new IndagineCivicoLogic(this.getEnvUtente(request));
		//TODO: recuperare dal file properties il parametro che indica se la codifica del viario SITI
		// e quella del viario anagrafico sono congruenti
		boolean isCodificaViarioCongruente=true;
		Hashtable ht = logic.mCaricareDettaglio(oggettoSel, isCodificaViarioCongruente);
		if(ht.get(IndagineCivicoLogic.INDIRIZZO) != null)
			sessione.setAttribute(IndagineCivicoLogic.INDIRIZZO,ht.get(IndagineCivicoLogic.INDIRIZZO));
		if(ht.get(IndagineCivicoLogic.LISTA_RESIDENTI) != null)
			sessione.setAttribute(IndagineCivicoLogic.LISTA_RESIDENTI,ht.get(IndagineCivicoLogic.LISTA_RESIDENTI));
		if(ht.get(IndagineCivicoLogic.LISTA_UI_CON_TITOLARE) != null)
			sessione.setAttribute(IndagineCivicoLogic.LISTA_UI_CON_TITOLARE,ht.get(IndagineCivicoLogic.LISTA_UI_CON_TITOLARE));
		if(ht.get(IndagineCivicoLogic.LISTA_UI_CONSISTENZA) != null)
			sessione.setAttribute(IndagineCivicoLogic.LISTA_UI_CONSISTENZA,ht.get(IndagineCivicoLogic.LISTA_UI_CONSISTENZA));
		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		this.chiamaPagina(request,response,urlPage, nav);


	}//-------------------------------------------------------------------------

	private void mExportXls(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//recupera la chiave
		String chiave = "";
		if (request.getParameter("OGGETTO_SEL")!= null)
			oggettoSel = request.getParameter("OGGETTO_SEL");
		//acquisizione dati
		IndagineCivicoLogic logic = new IndagineCivicoLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mExportToXls(oggettoSel, request.getSession().getId());
		HSSFWorkbook wb =(HSSFWorkbook)ht.get(IndagineCivicoLogic.FILE_EXCEL);
		Indirizzo indSel = (Indirizzo)ht.get(IndagineCivicoLogic.INDIRIZZO);
		//preparazione output
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition","attachment=\""  + indSel.getIndirizzoCompleto()+ "\"; filename=\"" +  "Indirizzo " + indSel.getIndirizzoCompleto() + ".xls" + "\"");
		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.flush();
		out.close();
   	}
	
	private void mPopupDatiCensuari(HttpServletRequest request, HttpServletResponse response)
	throws Exception 	{
		DocfaLogic logic = new DocfaLogic(this.getEnvUtente(request));
		String identificativo_immobile = request.getParameter("im");
		String codice = request.getParameter("codice");
		//...in aggiunta rispetto al metodo di DocfaServlet
		//(n.b. il par codice contiene la chiaveDocfa, cioè protocollo|fornitura (con fornitura AAAAMMGG),
		// se arriva qui, il parametro codice è senz'altro valorizzato, mentre id immobile non necessariamente)
		IndagineCivicoLogic logicIC = new IndagineCivicoLogic(this.getEnvUtente(request));
		DatiDocfa datiDocfa = logicIC.mRecuperaDocfa(codice);
		request.setAttribute(IndagineCivicoLogic.DOCFA, datiDocfa);
		if(identificativo_immobile == null || identificativo_immobile.trim().equals("")     
			//	(identificativo_immobile == null || identificativo_immobile.trim().equals("")) || 
			//	(codice == null || codice.trim().equals("")) 
		  ){
			//throw new Exception("dati mancanti");
			request.setAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_SOLOREQUEST_DATI_CENSUARI, null);
			request.getRequestDispatcher("indagineCivico/indCivicoPopupCens.jsp").forward(request,response);
			return;
		}	
		request.setAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_SOLOREQUEST_DATI_CENSUARI, logic.mDatiCensuari(identificativo_immobile,codice));
	    // ... fine aggiunta rispetto al metodo di DocfaServlet
		//Questa parte del codice è identico a quello in DocfaServlet 
		if(request.getParameter("f") != null && request.getParameter("m")!= null && request.getParameter("s") != null)
			request.setAttribute(DocfaLogic.LISTA_DETTAGLIO_DOCFA_SOLOREQUEST_340, logic.mDati340(request.getParameter("f"), request.getParameter("m"), request.getParameter("s")));
		String  protocollo = codice.substring(0,codice.indexOf("|"));
		String fornitura = codice.substring(codice.indexOf("|")+1);
		Integer numP = logic.getNumPlanimetrie(protocollo, fornitura,identificativo_immobile);
		request.setAttribute(DocfaLogic.NUMERO_DI_PLANIMETRIE, numP);
		int planimetrieOrfane = ((Integer)logic.presenzaPlanimetrieSenzaImm(protocollo, fornitura,identificativo_immobile)).intValue();
		
		if(planimetrieOrfane >0 )
			request.setAttribute(DocfaLogic.PLANIMETRIE_SENZA_IMM_SOLO_REQUEST, planimetrieOrfane);
		
		CatastoPlanimetrieComma340Logic logic340 = new CatastoPlanimetrieComma340Logic(this.getEnvUtente(request));
		ArrayList<ArrayList<String>> planimetrieComma340 = logic340.getPlanimetrieComma340(request.getParameter("f"), request.getParameter("m"), request.getParameter("s"), pathPlanimetrieComma340);
		request.setAttribute(CatastoPlanimetrieComma340Logic.PLANIMETRIE_COMMA_340_DOCFA_CENS, planimetrieComma340);
		
		
		
		request.getRequestDispatcher("indagineCivico/indCivicoPopupCens.jsp").forward(request,response);		
	}
}
