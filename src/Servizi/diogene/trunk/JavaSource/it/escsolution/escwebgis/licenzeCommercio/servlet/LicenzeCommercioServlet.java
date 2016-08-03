 /*
  * Created on 3-mag-2004
  *
  * To change the template for this generated file go to
  * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
  */
 package it.escsolution.escwebgis.licenzeCommercio.servlet;

 import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.licenzeCommercio.bean.LicenzaCommercio;
import it.escsolution.escwebgis.licenzeCommercio.bean.LicenzaCommercioFinder;
import it.escsolution.escwebgis.licenzeCommercio.logic.LicenzeCommercioLogic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

 public class LicenzeCommercioServlet extends EscServlet implements EscService {

 	private String recordScelto;
 	private LicenzeCommercioLogic logic = null;
 	public static String NOMEFINDER = "FINDER29";
 	private LicenzaCommercioFinder finder = null;
 	PulsantiNavigazione nav;
 	public void EseguiServizio(
 		HttpServletRequest request,HttpServletResponse response)
 				throws ServletException, IOException {

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
 					 this.leggiCrossLink(request);
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

 				EscElementoFiltro elementoFiltro = new EscElementoFiltro();


 				//COMUNE
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Comune");
 				elementoFiltro.setAttributeName("COMUNE");
 				elementoFiltro.setTipo("S");
 				elementoFiltro.setCampoJS("");
 				elementoFiltro.setListaOperatori(operatoriStringaRid);
 				elementoFiltro.setListaValori(vctComuni);
 				elementoFiltro.setCampoFiltrato("sit_licenze_commercio.CODENTE");
 				listaElementiFiltro.add(elementoFiltro);


 				//NUM_ESERCIZIO
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Numero Esercizio");
 				elementoFiltro.setAttributeName("NUM_ESERCIZIO");
 				elementoFiltro.setTipo("N");
 				elementoFiltro.setCampoJS("numeroIntero");
 				elementoFiltro.setListaOperatori(operatoriNumerici);
 				elementoFiltro.setCampoFiltrato("sit_licenze_commercio.NUM_ESERCIZIO");
 				listaElementiFiltro.add(elementoFiltro);

 				//TIPOLOGIA
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Tipologia");
 				elementoFiltro.setAttributeName("TIPOLOGIA");
 				elementoFiltro.setTipo("S");
 				elementoFiltro.setCampoJS("");
 				elementoFiltro.setListaOperatori(operatoriStringaRid);
 				elementoFiltro.setCampoFiltrato("sit_licenze_commercio.TIPOLOGIA");
 				listaElementiFiltro.add(elementoFiltro);

 				//COGNOME
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Cognome");
 				elementoFiltro.setAttributeName("COGNOME");
 				elementoFiltro.setTipo("S");
 				elementoFiltro.setCampoJS("");
 				elementoFiltro.setListaOperatori(operatoriStringa);
 				elementoFiltro.setCampoFiltrato("sit_licenze_commercio_a.COGNOME");
 				listaElementiFiltro.add(elementoFiltro);

 				//NOME
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Nome");
 				elementoFiltro.setAttributeName("NOME");
 				elementoFiltro.setTipo("S");
 				elementoFiltro.setCampoJS("");
 				elementoFiltro.setListaOperatori(operatoriStringa);
 				elementoFiltro.setCampoFiltrato("sit_licenze_commercio_a.NOME");
 				listaElementiFiltro.add(elementoFiltro);

 				//CF
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Codice Fiscale");
 				elementoFiltro.setAttributeName("CODFISC");
 				elementoFiltro.setTipo("S");
 				elementoFiltro.setCampoJS("");
 				elementoFiltro.setListaOperatori(operatoriStringa);
 				elementoFiltro.setCampoFiltrato("sit_licenze_commercio_a.CODFISC");
 				listaElementiFiltro.add(elementoFiltro);

 				//VIA
 				elementoFiltro = new EscElementoFiltro();
 				elementoFiltro.setLabel("Nome Via");
 				elementoFiltro.setAttributeName("DESCRVIA");
 				elementoFiltro.setTipo("S");
 				elementoFiltro.setCampoJS("");
 				elementoFiltro.setListaOperatori(operatoriStringa);
 				elementoFiltro.setCampoFiltrato("sit_licenze_commercio_vie.DESCRIZIONE");
 				listaElementiFiltro.add(elementoFiltro);

 				sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
 				sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));


 				//chiamare la pagina di ricerca
 				nav = new PulsantiNavigazione();
 				nav.chiamataRicerca();
 				this.chiamaPagina(request,response,"licenzeCommercio/lcFrame.jsp", nav);

 			}

 	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response)throws Exception{
 		// prendere dalla request i parametri di ricerca impostati dall'utente
 		HttpSession sessione = request.getSession();

 		if (sessione.getAttribute(NOMEFINDER)!= null){
 			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new LicenzaCommercioFinder().getClass()){
 				finder = (LicenzaCommercioFinder)sessione.getAttribute(NOMEFINDER);
 			}
 			else
 				finder = null;
 		}

 		finder = (LicenzaCommercioFinder)gestireMultiPagina(finder,request);
 		//eseguire la query e caricare il vettore con i risultati
 		LicenzeCommercioLogic logic = new LicenzeCommercioLogic(this.getEnvUtente(request));
 		Hashtable ht = logic.mCaricareLista(vettoreRicerca,finder);

 		Vector vct_lista= (Vector)ht.get("LISTA");
 		finder = (LicenzaCommercioFinder)ht.get("FINDER");
 		sessione.setAttribute("LISTA_LICENZE",vct_lista);
 		sessione.setAttribute(NOMEFINDER,finder);

 		nav = new PulsantiNavigazione();
 		if (chiamataEsterna){

 			nav.chiamataEsternaLista();
 			nav.setExt("1");
 			nav.setPrimo(true);
 		}
 		else
 			nav.chiamataInternaLista();

 		this.chiamaPagina(request,response,"licenzeCommercio/lcFrame.jsp",nav);
 	}



 	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception{
 				String azione = "";
 				HttpSession sessione = request.getSession();
 				LicenzaCommercioFinder finder = null;
 				//boolean chiamataEsterna = false;
 				if (sessione.getAttribute(NOMEFINDER) !=null){
 					if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new LicenzaCommercioFinder().getClass()){
 						finder = (LicenzaCommercioFinder)sessione.getAttribute(NOMEFINDER);
 					}
 				}
 				if (request.getParameter("AZIONE")!= null)
 					azione = request.getParameter("AZIONE");
 				gestioneMultiRecord(request,response,azione,finder,sessione,"LISTA_LICENZE",(Vector)sessione.getAttribute("LISTA_LICENZE"),NOMEFINDER);
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
 				LicenzeCommercioLogic logic = new LicenzeCommercioLogic(this.getEnvUtente(request));
 				Hashtable ht = logic.mCaricareDettaglio(oggettoSel);

 				LicenzaCommercio det = (LicenzaCommercio)ht.get("DETTAGLIO");
 				ArrayList lista_sogg = (ArrayList)ht.get("LISTA_SOGGETTI_LC");
 				sessione.setAttribute("LICENZA",det);
 				sessione.setAttribute("LISTA_SOGGETTI_LC",lista_sogg);


 				nav = new PulsantiNavigazione();
 				if (chiamataEsterna)
 					nav.chiamataEsternaDettaglio();
 				else
 					nav.chiamataInternaDettaglio();

 				this.chiamaPagina(request,response,"licenzeCommercio/lcFrame.jsp", nav);

 			}



 	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
 		finder = (LicenzaCommercioFinder)finder2;
 		LicenzeCommercioLogic logic = new LicenzeCommercioLogic(this.getEnvUtente(request));
 		return logic.mCaricareLista(this.vettoreRicerca, finder);
 	}


 	public EscFinder getNewFinder(){
 		return new LicenzaCommercioFinder();
 	}
 	public String getChiaveOggetto(EscFinder finder, Vector lista,int recordSuccessivo){

 		LicenzaCommercio ogg = (LicenzaCommercio)lista.get(recordSuccessivo);
 		String key = ogg.getChiave();
 		return key;
 	}


 	public String getTema() {
 		return "Licenze Commercio";
 	}
 	public String getTabellaPerCrossLink() {
 		return "SIT_LICENZE_COMMERCIO";
 	}

 }



