/*
 * Created on 2-lug-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.pregeo.servlet;


import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscObject;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.pregeo.bean.ElementoListaFinder;
import it.escsolution.escwebgis.pregeo.bean.PregeoFornitura;
import it.escsolution.escwebgis.pregeo.bean.PregeoInfo;
import it.escsolution.escwebgis.pregeo.logic.PregeoLogic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.webred.cet.permission.CeTUser;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PregeoServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private final String NOMEFINDER = "FINDER114";
	private ElementoListaFinder finder = null;
	
	private String pathDatiDiogene = "";
	private String codEnte = "";

	  public void init(ServletConfig config) throws ServletException {
	        super.init(config);
	        pathDatiDiogene = super.getPathDatiDiogene();
	    }
	
	public void EseguiServizio(
		HttpServletRequest request,HttpServletResponse response)
				throws ServletException, IOException {
				/*
				 * ad ogni uc corrisponde una funzionalità diversa
				 */
		
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
		codEnte = eu.getEnte();
		
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
					 mCaricareLista(request,response,false);
					 break;
				 case 3:
					 // ho cliccato su un elemento --> visualizzo il dettaglio
					 mCaricareDettaglio(request,response);
					//this.leggiCrossLink(request);
					 break;
				 case 33:
					 // ho cliccato su un elemento --> visualizzo il dettaglio
					 mCaricareDettaglio(request,response);
					//this.leggiCrossLink(request);
					 break;

			 }
			 
			 request.getSession().setAttribute("PATH_DATI_DIOGENE", pathDatiDiogene + "/" + codEnte + "/");
			 
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
					PregeoLogic logic = new PregeoLogic(this.getEnvUtente(request));
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
					operatoriStringaRid.add(new EscOperatoreFiltro("like","contiene"));

					// costruisce il vettore di elementi per la pagina di ricerca

					EscElementoFiltro elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("INFORMAZIONI");
					elementoFiltro.setSoloLabel(true);
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Codice");
					elementoFiltro.setAttributeName("CODICE_PREGEO");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("CODICE_PREGEO");
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Denominazione");
					elementoFiltro.setAttributeName("DENOMINAZIONE");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringaRid);
					elementoFiltro.setCampoFiltrato("DENOMINAZIONE");
					listaElementiFiltro.add(elementoFiltro);
					
					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Tecnico");
					elementoFiltro.setAttributeName("TECNICO");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringaRid);
					elementoFiltro.setCampoFiltrato("TECNICO");
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Foglio");
					elementoFiltro.setAttributeName("FOGLIO");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("lpad4_0");
					elementoFiltro.setListaOperatori(operatoriStringa);					
					elementoFiltro.setCampoFiltrato("LPAD(FOGLIO, 4, '0')");					
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Particella");
					elementoFiltro.setAttributeName("PARTICELLA");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("lpad5_0");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("LPAD(PARTICELLA, 5, '0')");
					listaElementiFiltro.add(elementoFiltro);
					

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Data Pregeo");
					elementoFiltro.setAttributeName("DATA_PREGEO");
					elementoFiltro.setTipo("D");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriNumerici);
					elementoFiltro.setCampoFiltrato("DATA_PREGEO");
					listaElementiFiltro.add(elementoFiltro);

					sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
					sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
					//chiamare la pagina di ricerca
					nav.chiamataRicerca();
					this.chiamaPagina(request,response,"pregeo/pregeoFrame.jsp", nav);

				}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response,boolean notListaPrincipale)throws Exception{
			// prendere dalla request i parametri di ricerca impostati dall'utente
			HttpSession sessione = request.getSession();

			if (sessione.getAttribute(NOMEFINDER)!= null){
				if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new ElementoListaFinder().getClass()){
					finder = (ElementoListaFinder)sessione.getAttribute(NOMEFINDER);
				}
				else
					finder = null;
			}


			finder = (ElementoListaFinder )gestireMultiPagina(finder,request);

			PregeoLogic logic = new PregeoLogic(this.getEnvUtente(request));
			Hashtable ht = logic.mCaricareLista(vettoreRicerca,finder);

			// eseguire la query e caricare il vettore con i risultati
			// chiamare la pagina di lista


			Vector vct_lista_note= (Vector)ht.get(PregeoLogic.LISTA_DATI_PREGEO);
			finder = (ElementoListaFinder)ht.get(PregeoLogic.FINDER);

			sessione.setAttribute(PregeoLogic.LISTA_DATI_PREGEO,vct_lista_note);

			if (!notListaPrincipale){
				finder = (ElementoListaFinder)ht.get(PregeoLogic.FINDER);
				sessione.setAttribute(NOMEFINDER,finder);
			}

			if(chiamataEsterna)
				nav.chiamataEsternaLista();
			else
				nav.chiamataInternaLista();

			this.chiamaPagina(request,response,"pregeo/pregeoFrame.jsp", nav);
		}

	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception{
				// prendere dalla request il parametrio identificativo dell'oggetto cliccato
				// eseguire la query per caricare l'oggetto selezionato dal db
				// chiamare la pagina di dettaglio

				String azione = "";
				HttpSession sessione = request.getSession();
				ElementoListaFinder finder = null;
				//boolean chiamataEsterna = false;
				if (sessione.getAttribute(NOMEFINDER) !=null){
					if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new ElementoListaFinder().getClass()){
						finder = (ElementoListaFinder)sessione.getAttribute(NOMEFINDER);
					}
				}
				if (request.getParameter("AZIONE")!= null)
					azione = request.getParameter("AZIONE");
				gestioneMultiRecord(request,response,azione,finder,sessione,PregeoLogic.LISTA_DATI_PREGEO ,(Vector)sessione.getAttribute(PregeoLogic.LISTA_DATI_PREGEO),NOMEFINDER);
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
				PregeoLogic logic = new PregeoLogic(this.getEnvUtente(request));
				Hashtable ht = logic.mCaricareDettaglio(oggettoSel);

				ArrayList<PregeoInfo> alInfo = (ArrayList<PregeoInfo>)ht.get(PregeoLogic.PREGEO_DETT);
				ArrayList<PregeoFornitura> alForniture = (ArrayList<PregeoFornitura>)ht.get(PregeoLogic.PREGEO_DETT_FOR);
				ArrayList<String> alAltro = (ArrayList<String>)ht.get(PregeoLogic.PREGEO_DETT_ALTRO);
				// LEGGE E METTE IN SESSIONE I SOGGETTI SCARTATI
				final String chiave = alInfo.get(0).getChiave();
				super.leggiScarti(request.getSession(), new EscObject() {
					public String getChiave()
					{
						return chiave;
					}
				}, "PREGEO", request);

				sessione.setAttribute(PregeoLogic.PREGEO_DETT, alInfo);
				sessione.setAttribute(PregeoLogic.PREGEO_DETT_FOR, alForniture);
				sessione.setAttribute(PregeoLogic.PREGEO_DETT_ALTRO, alAltro);

				if(chiamataEsterna)
					nav.chiamataEsternaDettaglio();
				else
					nav.chiamataInternaDettaglio();


				this.chiamaPagina(request,response,"pregeo/pregeoFrame.jsp", nav);

				EscFinder escf = (EscFinder)sessione.getAttribute(NOMEFINDER);


			}
	
	

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
				return ((PregeoInfo)listaOggetti.get(recordSuccessivo)).getChiave();
				}

			public EscFinder getNewFinder(){
						return new ElementoListaFinder();
					}

			public String getTema() {
				return "Pregeo";
			}

		public String getTabellaPerCrossLink() {
			return "PREGEO_FORNITURE";
			}
		
		
		
}

