/*
 * Created on 27-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.tributi.servlet;


import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.tributi.bean.Contribuente;
import it.escsolution.escwebgis.tributi.bean.ContribuentiFinder;
import it.escsolution.escwebgis.tributi.logic.TributiContribuentiLogic;
import it.escsolution.escwebgis.tributi.logic.TributiOggettiICILogic;
import it.escsolution.escwebgis.tributi.logic.TributiOggettiTARSULogic;
import it.webred.GlobalParameters;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 * @author administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TributiContribuentiServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private TributiContribuentiLogic logic = null;
	private ContribuentiFinder finder = null;
	private final String NOMEFINDER = "FINDER5";

		public void EseguiServizio(
		HttpServletRequest request,HttpServletResponse response)
				throws ServletException, IOException {
				/*
				 * ad ogni uc corrisponde una funzionalità diversa
				 *
				 */
			HttpSession sessione = request.getSession();		
			sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));	
			
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
					mCaricareLista(request,response);
					break;
				case 3:
					// ho cliccato su un elemento --> visualizzo il dettaglio
					mCaricareDettaglio(request,response);
					this.leggiCrossLink(request);
					break;
				case 33:
					// ho cliccato su un bottone visualizza dettaglio --> visualizzo il dettaglio su una PopUp
					mCaricareDettaglio(request,response);
					this.leggiCrossLink(request);
					break;
				case 4:
					// ho cliccato su un elemento --> visualizzo il dettaglio
					mCaricareListaIciTarsu(request,response);
					break;
				case 5:
					// ho cliccato su un elemento --> visualizzo il dettaglio
					mCaricareDettaglioDaNinc(request,response);
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

		/**
		 * @param request
		 * @param response
		 */
	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception{
	// prendere dalla request il parametro identificativo dell'oggetto cliccato
	// eseguire la query per caricare l'oggetto selezionato dal db
	// chiamare la pagina di dettaglio
		String azione = "";
		HttpSession sessione = request.getSession();
		ContribuentiFinder finder = null;
		if (sessione.getAttribute(NOMEFINDER) !=null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new ContribuentiFinder().getClass()){
				finder = (ContribuentiFinder)sessione.getAttribute(NOMEFINDER);
			}
			else{
				// il finder non corrisponde -->
				finder = null;
				 //sessione.removeAttribute(NOMEFINDER);
			}
		}

		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");

		gestioneMultiRecord(request,response,azione,finder,sessione,"LISTA_CONTRIBUENTI",(Vector)sessione.getAttribute("LISTA_CONTRIBUENTI"),NOMEFINDER);
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
		 * carica il dettaglio del contribuente
		 */
		TributiContribuentiLogic logic = new TributiContribuentiLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioContribuente(oggettoSel);

		Contribuente cont = (Contribuente)ht.get("CONTRIBUENTE");

		// LEGGE E METTE IN SESSIONE I SOGGETTI SCARTATI
		super.leggiScarti(request.getSession(), cont, request);

		sessione.setAttribute("CONTRIBUENTE",cont);

//TEST MB
		//leggo ici e tarsu collegati al contribuente
		mListaIciTarsuDetail(sessione, request);
//TEST FINE MB

		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		this.chiamaPagina(request,response,"tributi/conFrame.jsp", nav);

}

	private void mCaricareDettaglioDaNinc(HttpServletRequest request,HttpServletResponse response)throws Exception{
		// prendere dalla request il parametro identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio
			String azione = "";
			HttpSession sessione = request.getSession();
			ContribuentiFinder finder = null;
			if (sessione.getAttribute(NOMEFINDER) !=null){
				if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new ContribuentiFinder().getClass()){
					finder = (ContribuentiFinder)sessione.getAttribute(NOMEFINDER);
				}
				else{
					// il finder non corrisponde -->
					finder = null;
					 //sessione.removeAttribute(NOMEFINDER);
				}
			}

			if (request.getParameter("AZIONE")!= null)
				azione = request.getParameter("AZIONE");

			gestioneMultiRecord(request,response,azione,finder,sessione,"LISTA_CONTRIBUENTI",(Vector)sessione.getAttribute("LISTA_CONTRIBUENTI"),NOMEFINDER);
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
			 * carica il dettaglio del contribuente
			 */
			TributiContribuentiLogic logic = new TributiContribuentiLogic(this.getEnvUtente(request));
			Hashtable ht = logic.mCaricareDettaglioContribuenteDaNinc(oggettoSel);

			Contribuente cont = (Contribuente)ht.get("CONTRIBUENTE");
			sessione.setAttribute("CONTRIBUENTE",cont);

//			TEST MB
			//leggo ici e tarsu collegati al contribuente
			mListaIciTarsuDetail(sessione, request);
//	TEST FINE MB

			if(chiamataEsterna)
				nav.chiamataEsternaDettaglio();
			else
				nav.chiamataInternaDettaglio();

			this.chiamaPagina(request,response,"tributi/conFrame.jsp", nav);

	}

		/**
		 * @param request
		 * @param response
		 */
	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response)throws Exception{
	// prendere dalla request i parametri di ricerca impostati dall'utente
	HttpSession sessione = request.getSession();

	if (sessione.getAttribute(NOMEFINDER)!= null){
					finder = (ContribuentiFinder)sessione.getAttribute(NOMEFINDER);
	}

	else {
		finder = null;
	}

		finder = (ContribuentiFinder)gestireMultiPagina(finder,request);

		TributiContribuentiLogic logic = new TributiContribuentiLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaContribuenti(vettoreRicerca,finder);

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista


		Vector vct_lista_contribuenti= (Vector)ht.get("LISTA_CONTRIBUENTI");
		finder = (ContribuentiFinder)ht.get("FINDER");

//		TEST MB
		boolean saltaListaConUno = GlobalParameters.SALTA_LISTA_CON_UNO.get(this.getEnvUtente(request).getEnte()) == null ? GlobalParameters.SALTA_LISTA_CON_UNO_DEF : GlobalParameters.SALTA_LISTA_CON_UNO.get(this.getEnvUtente(request).getEnte()).booleanValue();
		if (saltaListaConUno && vct_lista_contribuenti.size() == 1){
			/*
			 * carica il dettaglio del contribuente
			 */
			logic = new TributiContribuentiLogic(this.getEnvUtente(request));
			Hashtable htd = logic.mCaricareDettaglioContribuente(((Contribuente)vct_lista_contribuenti.get(0)).getCodContribuente());

			Contribuente cont = (Contribuente)htd.get("CONTRIBUENTE");

			// LEGGE E METTE IN SESSIONE I SOGGETTI SCARTATI
			super.leggiScarti(request.getSession(), cont, request);

			sessione.setAttribute("CONTRIBUENTE",cont);

			//leggo ici e tarsu collegati al contribuente
			mListaIciTarsuDetail(sessione, request);

			if(chiamataEsterna)
				nav.chiamataEsternaDettaglio();
			else
				nav.chiamataInternaDettaglio();

			//dettaglio
			st=33;
			this.chiamaPagina(request,response,"tributi/conFrame.jsp", nav);

			//cross link
			//this.leggiCrossLink(request);


		}else {
//FINE TEST MB

		sessione.setAttribute("LISTA_CONTRIBUENTI",vct_lista_contribuenti);
		sessione.setAttribute(NOMEFINDER,finder);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();

		this.chiamaPagina(request,response,"tributi/conFrame.jsp", nav);

//		TEST MB
		}
//		FINE TEST MB

}

		/**
		 * @param request
		 * @param response
		 */



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

		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=","uguale"));

		Vector operatoriNumerici = new Vector();
		operatoriNumerici.add(new EscOperatoreFiltro("=","="));
		operatoriNumerici.add(new EscOperatoreFiltro("<>","<>"));
		operatoriNumerici.add(new EscOperatoreFiltro(">",">"));
		operatoriNumerici.add(new EscOperatoreFiltro("<","<"));


		// costruisce il vettore di elementi per la pagina di ricerca
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Comune");
		elementoFiltro.setAttributeName("COMUNE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vctComuni);
		elementoFiltro.setCampoFiltrato("tri_contribuenti.FK_COMUNI");
		listaElementiFiltro.add(elementoFiltro);

	    elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Codice Fiscale");
		elementoFiltro.setAttributeName("CODICE_FISCALE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("CODICE_FISCALE");
		listaElementiFiltro.add(elementoFiltro);


		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cognome");
		elementoFiltro.setAttributeName("COGNOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("COGNOME");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Nome");
		elementoFiltro.setAttributeName("NOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("NOME");
		listaElementiFiltro.add(elementoFiltro);

/*		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Data Nascita");
		elementoFiltro.setAttributeName("DATA_NASCITA");
		elementoFiltro.setTipo("D");
		elementoFiltro.setCampoJS("controllaData");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("DATA_NASCITA");
		//elementoFiltro.setListaValori(vct_catego);
		listaElementiFiltro.add(elementoFiltro);
*/
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Denominazione");
		elementoFiltro.setAttributeName("DENOMINAZIONE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		//elementoFiltro.setListaValori(vct_comuni);
		elementoFiltro.setCampoFiltrato("DENOMINAZIONE");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Partita Iva");
		elementoFiltro.setAttributeName("PARTITA_IVA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		//elementoFiltro.setListaValori(vct_comuni);
		elementoFiltro.setCampoFiltrato("PARTITA_IVA");
		listaElementiFiltro.add(elementoFiltro);

		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		

		//chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"tributi/conFrame.jsp", nav);

	}

	private void mListaIciTarsuDetail(HttpSession sessione, HttpServletRequest request)throws Exception{
		/*
		 * carica la lista di terreni e fabbricati dell'intestatario
		 */
		// oggetti ICI
		 TributiOggettiICILogic logic = new TributiOggettiICILogic(this.getEnvUtente(request));
		 Contribuente cont = (Contribuente)sessione.getAttribute("CONTRIBUENTE");
		 Hashtable ht = logic.mCaricareListaOggettiICIPerTitolarita(cont.getCodContribuente());
		 Vector listaICI = (Vector)ht.get("LISTA_ICI");

		// oggetti TARSU
		TributiOggettiTARSULogic logic2 = new TributiOggettiTARSULogic(this.getEnvUtente(request));
		Hashtable ht2 = logic2.mCaricareListaOggettiTARSUPerTitolarita(cont.getCodContribuente());
		Vector listaTARSU = (Vector)ht2.get("LISTA_TARSU");

		sessione.setAttribute("LISTA_ICI_CONTRIBUENTI",listaICI);
		sessione.setAttribute("LISTA_TARSU_CONTRIBUENTI",listaTARSU);

	}

	private void mCaricareListaIciTarsu(HttpServletRequest request,HttpServletResponse response)throws Exception{
		/*
		 * carica la lista di terreni e fabbricati dell'intestatario
		 */
		HttpSession sessione = request.getSession();
		 // oggetti ICI
		 TributiOggettiICILogic logic = new TributiOggettiICILogic(this.getEnvUtente(request));
		 Contribuente cont = (Contribuente)sessione.getAttribute("CONTRIBUENTE");
		 Hashtable ht = logic.mCaricareListaOggettiICIPerTitolarita(cont.getCodContribuente());
		 Vector listaICI = (Vector)ht.get("LISTA_ICI");


		// oggetti TARSU
		TributiOggettiTARSULogic logic2 = new TributiOggettiTARSULogic(this.getEnvUtente(request));
		Hashtable ht2 = logic2.mCaricareListaOggettiTARSUPerTitolarita(cont.getCodContribuente());
		Vector listaTARSU = (Vector)ht2.get("LISTA_TARSU");

		sessione.setAttribute("LISTA_ICI_CONTRIBUENTI",listaICI);
		sessione.setAttribute("LISTA_TARSU_CONTRIBUENTI",listaTARSU);

		nav.chiamataPart();
		this.chiamaPagina(request,response,"tributi/conFrame.jsp", nav);

	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
		return ((Contribuente)listaOggetti.get(recordSuccessivo)).getCodContribuente();
	}

	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (ContribuentiFinder)finder2;
		TributiContribuentiLogic logic = new TributiContribuentiLogic(this.getEnvUtente(request));
		return logic.mCaricareListaContribuenti(this.vettoreRicerca, finder);
	}

	public EscFinder getNewFinder(){
			return new ContribuentiFinder();
	}

	public String getTema() {
		return "Tributi";
	}
	public String getTabellaPerCrossLink() {
		return "TRI_CONTRIBUENTI";
	}
}

