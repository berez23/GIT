 /*
  * Created on 3-mag-2004
  *
  * To change the template for this generated file go to
  * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
  */
 package it.escsolution.escwebgis.successioni.servlet;

 import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.successioni.bean.Defunto;
import it.escsolution.escwebgis.successioni.bean.Erede;
import it.escsolution.escwebgis.successioni.bean.EredeFinder;
import it.escsolution.escwebgis.successioni.logic.SuccessioniDefuntiLogic;
import it.escsolution.escwebgis.successioni.logic.SuccessioniErediLogic;
import it.webred.indice.OggettoIndice;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

 public class SuccessioniErediServlet extends EscServlet implements EscService {

 	private String recordScelto;
 	private SuccessioniErediLogic logic = null;
 	//private AnagrafeFinder finder = null;
 	private final String NOMEFINDER = "FINDER18";
 	private EredeFinder finder = null;
 	PulsantiNavigazione nav;
 	
 	public void EseguiServizio(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
 		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
 		
 		super.EseguiServizio(request,response);
		
		String ext= request.getParameter("IND_EXT");
		
		if (ext!= null && ext.equals("1"))
			_EseguiServizioExt(request,response);
		else
			_EseguiServizio(request,response);
		
	}
 	
 	public void _EseguiServizio(
 		HttpServletRequest request,HttpServletResponse response)
 				throws ServletException, IOException {
 				/*
 				 * ad ogni uc corrisponde una funzionalità diversa
 				 *
 				 */
 		//super.init(getServletConfig());
 		//String aaa = getServletConfig().getServletName();
 		super.EseguiServizio(request,response);
 		 try{
 			 switch (st){
 				 case 1:
 					 // carico la form di ricerca
 					 pulireSessione(request);
 					 mCaricareFormRicerca(request,response);
 					 break;
 				 case 2:
 					 // vengo dalla pagina di ricerca --> predispongo la lista e la passo alla pagina di lista
 				     //pulireSessione(request);
 					 mCaricareLista(request,response);
 					 break;
 				 case 3:
 					 // ho cliccato su un elemento --> visualizzo il dettaglio
 					 mCaricareDettaglio(request,response);
 					 //this.leggiCrossLink(request);
 					 break;
 				case 4:
 					 // chiamata da altre successioni --> visualizzo elenco
 					 mCaricareListaEredita(request,response);
 					 //this.leggiCrossLink(request);
 					 break;
 				 case 33:
					 // ho cliccato su un elemento --> visualizzo il dettaglio
					 mCaricareDettaglio(request,response);
					 //this.leggiCrossLink(request);
					 break;
 				/*case 5:
 					 // ho cliccato su un elemento --> visualizzo il dettaglio
 					 mCaricareLista(request,response,true);
 					 break;	*/
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
 	
 	
 	public void _EseguiServizioExt(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		super.EseguiServizio(request,response);
		try
		{
			
				switch (st){
					case 101:
						mCaricareDettaglioFromSoggetto(request,response);			
						break;
					case 102:
						//mCaricareLista(request,response);							 
						break;
					case 103:
						//mCaricareLista(request,response);						 
						break;
					case 104:
						//mCaricareLista(request,response);						 
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

 	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception{

 				//caricare dati che servono nella pagina di ricerca
				/** Comune */
				Vector vctComuni = new ComuniLogic(this.getEnvUtente(request)).getListaComuniUtente(request.getUserPrincipal().getName());
				/***/

 				HttpSession sessione = request.getSession();


 				Vector listaElementiFiltro = new Vector();
 				Vector operatoriStringa = new Vector();
 				operatoriStringa.add(new EscOperatoreFiltro("=","uguale"));
 				operatoriStringa.add(new EscOperatoreFiltro("like","contiene"));



 				Vector operatoriNumerici = new Vector();
 				operatoriNumerici.add(new EscOperatoreFiltro("=","="));
 				operatoriNumerici.add(new EscOperatoreFiltro("<>","<>"));
 				operatoriNumerici.add(new EscOperatoreFiltro(">",">"));
 				operatoriNumerici.add(new EscOperatoreFiltro("<","<"));

 				Vector operatoriStringaRid = new Vector();
 				operatoriStringaRid.add(new EscOperatoreFiltro("=","uguale"));

 				// costruisce il vettore di elementi per la pagina di ricerca

 				EscElementoFiltro elementoFiltro = new EscElementoFiltro();

 				//UFFICIO
 				elementoFiltro.setLabel("Ufficio");
 				elementoFiltro.setAttributeName("UFFICIO");
 				elementoFiltro.setTipo("S");
 				elementoFiltro.setCampoJS("");
 				elementoFiltro.setListaOperatori(operatoriStringaRid);
 				elementoFiltro.setCampoFiltrato("mi_successioni_b.UFFICIO");
 				listaElementiFiltro.add(elementoFiltro);

 				//ANNO
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Anno");
 				elementoFiltro.setAttributeName("ANNO");
 				elementoFiltro.setTipo("S");
 				elementoFiltro.setCampoJS("");
 				elementoFiltro.setListaOperatori(operatoriStringaRid);
 				elementoFiltro.setCampoFiltrato("mi_successioni_b.ANNO");
 				listaElementiFiltro.add(elementoFiltro);

 				//VOLUME
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Volume");
 				elementoFiltro.setAttributeName("VOLUME");
 				elementoFiltro.setTipo("N");
 				elementoFiltro.setCampoJS("numeroIntero");
 				elementoFiltro.setListaOperatori(operatoriNumerici);
 				elementoFiltro.setCampoFiltrato("mi_successioni_b.VOLUME");
 				listaElementiFiltro.add(elementoFiltro);

 				//NUMERO
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Numero");
 				elementoFiltro.setAttributeName("NUMERO");
 				elementoFiltro.setTipo("N");
 				elementoFiltro.setCampoJS("numeroIntero");
 				elementoFiltro.setListaOperatori(operatoriNumerici);
 				elementoFiltro.setCampoFiltrato("mi_successioni_b.NUMERO");
 				listaElementiFiltro.add(elementoFiltro);

 				//SOTTONUMERO
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Sottonumero");
 				elementoFiltro.setAttributeName("SOTTONUMERO");
 				elementoFiltro.setTipo("N");
 				elementoFiltro.setCampoJS("numeroIntero");
 				elementoFiltro.setListaOperatori(operatoriNumerici);
 				elementoFiltro.setCampoFiltrato("mi_successioni_b.SOTTONUMERO");
 				listaElementiFiltro.add(elementoFiltro);

 				//COMUNE
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Comune");
 				elementoFiltro.setAttributeName("COMUNE");
 				elementoFiltro.setTipo("S");
 				elementoFiltro.setCampoJS("");
 				elementoFiltro.setListaOperatori(operatoriStringaRid);
 				elementoFiltro.setListaValori(vctComuni);
 				elementoFiltro.setCampoFiltrato("mi_successioni_b.COMUNE");
 				listaElementiFiltro.add(elementoFiltro);

 				//PROGRESSIVO
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Progressivo");
 				elementoFiltro.setAttributeName("PROGRESSIVO");
 				elementoFiltro.setTipo("N");
 				elementoFiltro.setCampoJS("numeroIntero");
 				elementoFiltro.setListaOperatori(operatoriNumerici);
 				elementoFiltro.setCampoFiltrato("mi_successioni_b.PROGRESSIVO");
 				listaElementiFiltro.add(elementoFiltro);

 				//CF EREDE
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Codice Fiscale");
 				elementoFiltro.setAttributeName("CODICE_FISCALE");
 				elementoFiltro.setTipo("S");
 				elementoFiltro.setCampoJS("");
 				elementoFiltro.setListaOperatori(operatoriStringa);
 				elementoFiltro.setCampoFiltrato("mi_successioni_b.CF_EREDE");
 				listaElementiFiltro.add(elementoFiltro);

 				//DENOMINAZIONE
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Denominazione");
 				elementoFiltro.setAttributeName("DENOMINAZIONE");
 				elementoFiltro.setTipo("S");
 				elementoFiltro.setCampoJS("");
 				elementoFiltro.setListaOperatori(operatoriStringa);
 				elementoFiltro.setCampoFiltrato("mi_successioni_b.DENOMINAZIONE");
 				listaElementiFiltro.add(elementoFiltro);


 				sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
 				sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
 				

 				//chiamare la pagina di ricerca
 				nav = new PulsantiNavigazione();
 				nav.chiamataRicerca();
 				this.chiamaPagina(request,response,"successioni/ereFrame.jsp", nav);

 			}

 	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response)throws Exception{
 		// prendere dalla request i parametri di ricerca impostati dall'utente
 		HttpSession sessione = request.getSession();

 		if (sessione.getAttribute(NOMEFINDER)!= null){
 			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new EredeFinder().getClass()){
 				finder = (EredeFinder)sessione.getAttribute(NOMEFINDER);
 			}
 			else
 				finder = null;
 		}

 		finder = (EredeFinder)gestireMultiPagina(finder,request);
 		//eseguire la query e caricare il vettore con i risultati
 		SuccessioniErediLogic logic = new SuccessioniErediLogic(this.getEnvUtente(request));
 		Hashtable ht = logic.mCaricareListaEredi(vettoreRicerca,finder);

 		Vector vct_lista_defunti= (Vector)ht.get("LISTA_EREDI");
 		finder = (EredeFinder)ht.get("FINDER");
 		sessione.setAttribute("LISTA_EREDI",vct_lista_defunti);
 		sessione.setAttribute(NOMEFINDER,finder);

 		nav = new PulsantiNavigazione();
 		if (chiamataEsterna){

 			nav.chiamataEsternaLista();
 			nav.setExt("1");
 			nav.setPrimo(true);
 		}
 		else
 			nav.chiamataInternaLista();

 		this.chiamaPagina(request,response,"successioni/ereFrame.jsp",nav);
 	}

	private void mCaricareListaEredita(HttpServletRequest request,HttpServletResponse response)throws Exception{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new EredeFinder().getClass()){
				finder = (EredeFinder)sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (EredeFinder)gestireMultiPagina(finder,request);

		SuccessioniErediLogic logic = new SuccessioniErediLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaEredita(request.getParameter("OGGETTO_SEL"));

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista


	    Vector vct_lista= (Vector)ht.get("LISTA");
		finder = (EredeFinder)ht.get("FINDER");
		sessione.setAttribute("LISTA_EREDI_EREDITA",vct_lista);
		sessione.setAttribute(NOMEFINDER,finder);




		nav = new PulsantiNavigazione();
		if (chiamataEsterna){

			nav.chiamataEsternaLista();
			nav.setExt("1");
			nav.setPrimo(true);
		}
		else
			nav.chiamataInternaLista();
		this.chiamaPagina(request,response,"successioni/ereFrame.jsp",nav);
	}


 	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception{
 				// prendere dalla request il parametrio identificativo dell'oggetto cliccato
 				// eseguire la query per caricare l'oggetto selezionato dal db
 				// chiamare la pagina di dettaglio

 				String azione = "";
 				HttpSession sessione = request.getSession();
 				
 				removeOggettiIndiceDaSessione(sessione);
 				  				
 				EredeFinder finder = null;
 				//boolean chiamataEsterna = false;
 				if (sessione.getAttribute(NOMEFINDER) !=null){
 					if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new EredeFinder().getClass()){
 						finder = (EredeFinder)sessione.getAttribute(NOMEFINDER);
 					}
 				}
 				if (request.getParameter("AZIONE")!= null)
 					azione = request.getParameter("AZIONE");
 				gestioneMultiRecord(request,response,azione,finder,sessione,"LISTA_EREDI",(Vector)sessione.getAttribute("LISTA_EREDI"),NOMEFINDER);
 				if (azione.equals("")){
 					oggettoSel="";recordScelto="";
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
 				SuccessioniErediLogic logic = new SuccessioniErediLogic(this.getEnvUtente(request));
 				Hashtable ht = logic.mCaricareDettaglioErede(oggettoSel);

 				Erede ere = (Erede)ht.get("EREDE");
 				sessione.setAttribute("EREDE",ere);


 				nav = new PulsantiNavigazione();
 				if (chiamataEsterna)
 					nav.chiamataEsternaDettaglio();
 				else
 					nav.chiamataInternaDettaglio();
 				
 			// Aggiungo i valori per l'indice di correlazione
 				Vector<OggettoIndice> sOggettiInd = new Vector<OggettoIndice>();
 				
 				OggettoIndice oi = new OggettoIndice();
 				
 				oi.setIdOrig(ere.getId());
 				oi.setFonte("6");
 				oi.setProgr("2");
 				
 				if ((ere.getDenominazione()!= null && !"".equals(ere.getDenominazione())) )
 					oi.setDescrizione(ere.getDenominazione() );
 				
 				sOggettiInd.add(oi);
 				
 				sessione.setAttribute("indice_soggetti", sOggettiInd);


 				this.chiamaPagina(request,response,"successioni/ereFrame.jsp", nav);

 				//EscFinder escf = (EscFinder)sessione.getAttribute(NOMEFINDER);


 			}
 	
 	private void mCaricareDettaglioFromSoggetto(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		
		String idSoggetto= request.getParameter("OGGETTO_SEL");
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
		 
		 SuccessioniErediLogic logic = new SuccessioniErediLogic(this.getEnvUtente(request));
			Hashtable ht = logic.mCaricareDettaglioFromSoggetto(idSoggetto);

			Erede ere = (Erede)ht.get("EREDE");
				sessione.setAttribute("EREDE",ere);


			nav = new PulsantiNavigazione();
			if (chiamataEsterna)
				nav.chiamataEsternaDettaglio();
			else
				nav.chiamataInternaDettaglio();
			
			// Aggiungo i valori per l'indice di correlazione
			Vector<OggettoIndice> sOggettiInd = new Vector<OggettoIndice>();
			
			OggettoIndice oi = new OggettoIndice();
			
			oi.setIdOrig(ere.getId());
			oi.setFonte("6");
			oi.setProgr("2");
			
			if ((ere.getDenominazione()!= null && !"".equals(ere.getDenominazione())) )
				oi.setDescrizione(ere.getDenominazione() );
			
			sOggettiInd.add(oi);
			
			sessione.setAttribute("indice_soggetti", sOggettiInd);

			this.chiamaPagina(request,response,"successioni/ereFrame.jsp", nav);
		 
		 
	}



 	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
 		finder = (EredeFinder)finder2;
 		SuccessioniErediLogic logic = new SuccessioniErediLogic(this.getEnvUtente(request));
 		return logic.mCaricareListaEredi(this.vettoreRicerca, finder);
 	}


 	public EscFinder getNewFinder(){
 		return new EredeFinder();
 	}
 	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){

 		Erede def = (Erede)listaOggetti.get(recordSuccessivo);
 		String key = def.getUfficio()+"|"+def.getAnno()+"|"+def.getVolume()+"|"+def.getNumero()+"|"+def.getSottonumero()+"|"+def.getComune()+"|"+def.getProgressivo()+"|"+def.getProgressivoErede();
 		return key;
 	}


 	public String getTema() {
 		return "Successioni";
 	}
 	public String getTabellaPerCrossLink() {
 		return "SIT_MI_EREDE";
 	}

 }



