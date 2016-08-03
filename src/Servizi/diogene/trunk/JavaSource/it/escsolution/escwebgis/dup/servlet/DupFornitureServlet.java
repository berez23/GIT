/*
 * Created on 2-lug-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.dup.servlet;


import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.dup.bean.DupForniture;
import it.escsolution.escwebgis.dup.bean.DupFornitureFinder;
import it.escsolution.escwebgis.dup.logic.DupFornitureLogic;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DupFornitureServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private DupFornitureLogic logic = null;
	private final String NOMEFINDER = "FINDER23";
	private DupFornitureFinder finder = null;

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
					 //pulireSessione(request);
					 mCaricareLista(request,response,false);
					 break;
				 case 3:
					 // ho cliccato su un elemento --> visualizzo il dettaglio
					 mCaricareDettaglio(request,response);
					//this.leggiCrossLink(request);
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
					DupFornitureLogic logic = new DupFornitureLogic(this.getEnvUtente(request));
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
					elementoFiltro.setLabel("Codice Amministrazione");
					elementoFiltro.setAttributeName("COD_AMMIN");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringaRid);
					elementoFiltro.setCampoFiltrato("COD_AMMIN");
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Data inizio");
					elementoFiltro.setAttributeName("DATA_INIZIO");
					elementoFiltro.setTipo("D");
					elementoFiltro.setCampoJS("controllaData");
					elementoFiltro.setListaOperatori(operatoriNumerici);
					elementoFiltro.setCampoFiltrato("TO_DATE(TO_CHAR(DATA_INIZIO))");
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Data fine");
					elementoFiltro.setAttributeName("DATA_FINE");
					elementoFiltro.setTipo("D");
					elementoFiltro.setCampoJS("controllaData");
					elementoFiltro.setListaOperatori(operatoriNumerici);
					elementoFiltro.setCampoFiltrato("TO_DATE(TO_CHAR(DATA_FINE))");
					listaElementiFiltro.add(elementoFiltro);


					sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
					sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
					
					//chiamare la pagina di ricerca
					nav.chiamataRicerca();
					this.chiamaPagina(request,response,"dup/fornitFrame.jsp", nav);

				}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response,boolean notListaPrincipale)throws Exception{
			// prendere dalla request i parametri di ricerca impostati dall'utente
			HttpSession sessione = request.getSession();

			if (sessione.getAttribute(NOMEFINDER)!= null){
				if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new DupFornitureFinder().getClass()){
					finder = (DupFornitureFinder)sessione.getAttribute(NOMEFINDER);
				}
				else
					finder = null;
			}


			finder = (DupFornitureFinder)gestireMultiPagina(finder,request);

			DupFornitureLogic logic = new DupFornitureLogic(this.getEnvUtente(request));
			Hashtable ht = logic.mCaricareListacForniture(vettoreRicerca,finder);

			// eseguire la query e caricare il vettore con i risultati
			// chiamare la pagina di lista


			Vector vct_lista_forniture= (Vector)ht.get("LISTA_FORNITURE");
			finder = (DupFornitureFinder)ht.get("FINDER");

			sessione.setAttribute("LISTA_FORNITURE",vct_lista_forniture);

			if (!notListaPrincipale){
				finder = (DupFornitureFinder)ht.get("FINDER");
				sessione.setAttribute(NOMEFINDER,finder);
			}


			//if(chiamataEsterna)
				nav.chiamataEsternaLista();
	//		else
		//		nav.chiamataInternaLista();

			this.chiamaPagina(request,response,"dup/fornitFrame.jsp", nav);
		}

	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception{
				// prendere dalla request il parametrio identificativo dell'oggetto cliccato
				// eseguire la query per caricare l'oggetto selezionato dal db
				// chiamare la pagina di dettaglio

				String azione = "";
				HttpSession sessione = request.getSession();
				DupFornitureFinder finder = null;
				//boolean chiamataEsterna = false;
				if (sessione.getAttribute(NOMEFINDER) !=null){
					if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new DupFornitureFinder().getClass()){
						finder = (DupFornitureFinder)sessione.getAttribute(NOMEFINDER);
					}
				}
				if (request.getParameter("AZIONE")!= null)
					azione = request.getParameter("AZIONE");
				gestioneMultiRecord(request,response,azione,finder,sessione,"LISTA_FORNITURE",(Vector)sessione.getAttribute("LISTA_FORNITURE"),NOMEFINDER);
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
				DupFornitureLogic logic = new DupFornitureLogic(this.getEnvUtente(request));
				Hashtable ht = logic.mCaricareDettaglioContribuente(oggettoSel);

				DupForniture contribuente = (DupForniture)ht.get("CONTRIBUENTE");
				sessione.setAttribute("CONTRIBUENTE",contribuente);

				if(chiamataEsterna)
					nav.chiamataEsternaDettaglio();
				else
					nav.chiamataInternaDettaglio();


				this.chiamaPagina(request,response,"dup/fornitFrame.jsp", nav);

				//EscFinder escf = (EscFinder)sessione.getAttribute(NOMEFINDER);


			}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
				return ((DupForniture)listaOggetti.get(recordSuccessivo)).getIdFornitura();
				}

			/*public Hashtable executeLogic(EscFinder finder2) throws Exception{
				finder = (SoggettoFinder)finder2;
				SoggettoSoggettoLogic logic = new SoggettoSoggettoLogic(this.getEnvUtente(request));
				return logic.mCaricareListaSoggetto(this.vettoreRicerca,finder);
				}*/

			public EscFinder getNewFinder(){
						return new DupFornitureFinder();
					}

			public String getTema() {
				return "Compravendite";
			}

		public String getTabellaPerCrossLink() {
				return "MI_SIATEL_P_FIS";
			}
}

