 /*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.anagrafe.servlet;
import it.escsolution.escwebgis.anagrafe.bean.Anagrafe;
import it.escsolution.escwebgis.anagrafe.bean.AnagrafeFinder;
import it.escsolution.escwebgis.anagrafe.bean.Famiglia;
import it.escsolution.escwebgis.anagrafe.logic.AnagrafeAnagrafeLogic;
import it.escsolution.escwebgis.anagrafe.logic.AnagrafeFamigliaLogic;
import it.escsolution.escwebgis.catasto.bean.Sesso;
import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.webred.GlobalParameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class AnagrafeAnagrafeServlet extends EscServlet implements EscService {

	private String recordScelto;
	private AnagrafeAnagrafeLogic logic = null;
	//private AnagrafeFinder finder = null;
	public static final String NOMEFINDER = "FINDER8";
	private AnagrafeFinder finder = null;
	PulsantiNavigazione nav;
	public void EseguiServizio(
		HttpServletRequest request,HttpServletResponse response)
				throws ServletException, IOException {
				/*
				 * ad ogni uc corrisponde una funzionalit diversa
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
					 this.leggiCrossLink(request);
					 break;
				/*case 4:
					 // ho cliccato su un elemento --> visualizzo il dettaglio
					 mCaricareDettaglio(request,response);
					 this.leggiCrossLink(request);
					 break;
				case 5:
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

	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception{

				//caricare dati che servono nella pagina di ricerca
				/** Comune */
				Vector vctComuni = new ComuniLogic(this.getEnvUtente(request)).getListaComuniUtente(request.getUserPrincipal().getName());
				/***/
				HttpSession sessione = request.getSession();
				Vector vct_sesso = new Vector();
				//da 1 maschio 2 femmina a M,F
				vct_sesso.add(new Sesso("","Tutti"));
				vct_sesso.add(new Sesso("M","Maschio"));
				vct_sesso.add(new Sesso("F","Femmina"));


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
				elementoFiltro.setLabel("Comune");
				elementoFiltro.setAttributeName("COMUNE");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriStringaRid);
				elementoFiltro.setListaValori(vctComuni);
				elementoFiltro.setCampoFiltrato("pop_anagrafe.FK_COMUNI");
				listaElementiFiltro.add(elementoFiltro);


				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Codice Anagrafe");
				elementoFiltro.setAttributeName("COD_ANAGRAFE");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriNumerici);
				elementoFiltro.setCampoFiltrato("PK_CODI_ANAGRAFE");
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

				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Data di nascita");
				elementoFiltro.setAttributeName("DATA_NASCITA");
				elementoFiltro.setTipo("D");
				elementoFiltro.setCampoJS("controllaData");
				elementoFiltro.setListaOperatori(operatoriNumerici);
				elementoFiltro.setCampoFiltrato("DATA_NASCITA");
				listaElementiFiltro.add(elementoFiltro);


				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Sesso");
				elementoFiltro.setAttributeName("SESSO");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriStringaRid);
				elementoFiltro.setListaValori(vct_sesso);
				elementoFiltro.setCampoFiltrato("SESSO");
				listaElementiFiltro.add(elementoFiltro);

				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Tipo Soggetto");
				elementoFiltro.setAttributeName("TIPO_SOGGETTO");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriStringa);
				elementoFiltro.setCampoFiltrato("TIPO_SOGGETTO");
				//elementoFiltro.setListaValori(vct_catego);
				listaElementiFiltro.add(elementoFiltro);



				sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
				sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));


				//chiamare la pagina di ricerca
				nav = new PulsantiNavigazione();
				nav.chiamataRicerca();
				this.chiamaPagina(request,response,"anagrafe/anaFrame.jsp", nav);

			}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response)throws Exception{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();



		if (sessione.getAttribute(NOMEFINDER)!= null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new AnagrafeFinder().getClass()){
				finder = (AnagrafeFinder)sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}


		finder = (AnagrafeFinder)gestireMultiPagina(finder,request);

		AnagrafeAnagrafeLogic logic = new AnagrafeAnagrafeLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaAnagrafe(vettoreRicerca,finder);

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista


	    Vector vct_lista_anagrafe= (Vector)ht.get(AnagrafeAnagrafeLogic.LISTA_ANAGRAFE);
		finder = (AnagrafeFinder)ht.get("FINDER");

//TEST MB
		boolean saltaListaConUno = GlobalParameters.SALTA_LISTA_CON_UNO.get(this.getEnvUtente(request).getEnte()) == null ? GlobalParameters.SALTA_LISTA_CON_UNO_DEF : GlobalParameters.SALTA_LISTA_CON_UNO.get(this.getEnvUtente(request).getEnte()).booleanValue();
		if (saltaListaConUno && vct_lista_anagrafe.size() == 1){
			/*
			 * carica il dettaglio
			 */
			Hashtable htd = logic.mCaricareDettaglioAnagrafe(((Anagrafe)vct_lista_anagrafe.get(0)).getCodAnagrafe());

			Anagrafe ana = (Anagrafe)htd.get(AnagrafeAnagrafeLogic.ANAGRAFE);

			// LEGGE E METTE IN SESSIONE I SOGGETTI SCARTATI
			super.leggiScarti(request.getSession(), ana, request);

			sessione.setAttribute(AnagrafeAnagrafeLogic.ANAGRAFE, ana);

			ArrayList listaComponentiFamiglia = (ArrayList) htd.get(AnagrafeAnagrafeLogic.LISTA_COMPONENTI_FAMIGLIA);
			sessione.setAttribute(AnagrafeAnagrafeLogic.LISTA_COMPONENTI_FAMIGLIA, listaComponentiFamiglia);

			nav = new PulsantiNavigazione();
			if (chiamataEsterna)
				nav.chiamataEsternaDettaglio();
			else
				nav.chiamataInternaDettaglio();

			st = 33;

			this.chiamaPagina(request,response,"anagrafe/anaFrame.jsp", nav);

			//cross link
			this.leggiCrossLink(request);

		}else {
//FINE TEST MB
			sessione.setAttribute(AnagrafeAnagrafeLogic.LISTA_ANAGRAFE, vct_lista_anagrafe);
			sessione.setAttribute(NOMEFINDER, finder);

			nav = new PulsantiNavigazione();
			if (chiamataEsterna){

				nav.chiamataEsternaLista();
				nav.setExt("1");
				nav.setPrimo(true);
			}
			else
				nav.chiamataInternaLista();
			this.chiamaPagina(request,response,"anagrafe/anaFrame.jsp",nav);

//		TEST MB
		}
//		FINE TEST MB

	}




	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception{
				// prendere dalla request il parametrio identificativo dell'oggetto cliccato
				// eseguire la query per caricare l'oggetto selezionato dal db
				// chiamare la pagina di dettaglio

				String azione = "";
				HttpSession sessione = request.getSession();
				
				removeOggettiIndiceDaSessione(sessione);
				
				AnagrafeFinder finder = null;
				//boolean chiamataEsterna = false;
				if (sessione.getAttribute(NOMEFINDER) !=null){
					if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new AnagrafeFinder().getClass()){
						finder = (AnagrafeFinder)sessione.getAttribute(NOMEFINDER);
					}
				}

				if (request.getParameter("AZIONE")!= null)
					azione = request.getParameter("AZIONE");
				gestioneMultiRecord(request,response,azione,finder,sessione,"LISTA_ANAGRAFE",(Vector)sessione.getAttribute("LISTA_ANAGRAFE"),NOMEFINDER);
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
				AnagrafeAnagrafeLogic logic = new AnagrafeAnagrafeLogic(this.getEnvUtente(request));
				Hashtable ht = logic.mCaricareDettaglioAnagrafe(oggettoSel);

				Anagrafe ana = (Anagrafe)ht.get(AnagrafeAnagrafeLogic.ANAGRAFE);

				// LEGGE E METTE IN SESSIONE I SOGGETTI SCARTATI
				super.leggiScarti(request.getSession(), ana, request);

				sessione.setAttribute(AnagrafeAnagrafeLogic.ANAGRAFE, ana);

				ArrayList listaComponentiFamiglia = (ArrayList) ht.get(AnagrafeAnagrafeLogic.LISTA_COMPONENTI_FAMIGLIA);
				sessione.setAttribute(AnagrafeAnagrafeLogic.LISTA_COMPONENTI_FAMIGLIA, listaComponentiFamiglia);

				nav = new PulsantiNavigazione();
				if (chiamataEsterna)
					nav.chiamataEsternaDettaglio();
				else
					nav.chiamataInternaDettaglio();

				this.chiamaPagina(request,response,"anagrafe/anaFrame.jsp", nav);

				//EscFinder escf = (EscFinder)sessione.getAttribute(NOMEFINDER);


			}




	private void mCaricareFamiglia(HttpServletRequest request,HttpServletResponse response, String chiave) throws Exception{
				/*
				 * carico la famiglia
				 */
				AnagrafeFamigliaLogic logic = new AnagrafeFamigliaLogic(this.getEnvUtente(request));
				Famiglia Fam = (Famiglia)((Hashtable)logic.mCaricareFamigliaPerAnagrafe(chiave)).get("FAMIGLIA");


				HttpSession sessione = request.getSession();
				sessione.setAttribute("FAMIGLIA",Fam);


			}

	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (AnagrafeFinder)finder2;
		AnagrafeAnagrafeLogic logic = new AnagrafeAnagrafeLogic(this.getEnvUtente(request));
		return logic.mCaricareListaAnagrafe(this.vettoreRicerca, finder);
	}


	public EscFinder getNewFinder(){
		return new AnagrafeFinder();
	}
	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
		return ((Anagrafe)listaOggetti.get(recordSuccessivo)).getCodAnagrafe();
	}


	public String getTema() {
		return "Popolazione";
	}
	public String getTabellaPerCrossLink() {
		return "POP_ANAGRAFE";
	}

}



