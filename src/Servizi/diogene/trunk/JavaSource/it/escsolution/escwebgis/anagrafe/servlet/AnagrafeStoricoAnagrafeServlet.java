 /*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.anagrafe.servlet;
import it.escsolution.escwebgis.anagrafe.bean.Anagrafe;
import it.escsolution.escwebgis.anagrafe.bean.AnagrafeStoricoFinder;
import it.escsolution.escwebgis.anagrafe.logic.AnagrafeStoricoAnagrafeLogic;
import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class AnagrafeStoricoAnagrafeServlet extends EscServlet implements EscService {

	private String recordScelto;
	public static final String NOMEFINDER = "FINDER35";
	private AnagrafeStoricoFinder finder = null;
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
				elementoFiltro.setLabel("Codice Fiscale");
				elementoFiltro.setAttributeName("CODICE_FISCALE");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriStringa);
				elementoFiltro.setCampoFiltrato("CODFISC");
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

				/*elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Data Riferimento");
				elementoFiltro.setAttributeName("DATA_RIF");
				elementoFiltro.setTipo("D");
				elementoFiltro.setCampoJS("controllaData");
				elementoFiltro.setListaOperatori(operatoriStringaRid);
				elementoFiltro.setCampoFiltrato("DATA_RIF");
				listaElementiFiltro.add(elementoFiltro);
				*/


				sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
				sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));


				//chiamare la pagina di ricerca
				nav = new PulsantiNavigazione();
				nav.chiamataRicerca();
				this.chiamaPagina(request,response,"anagrafe/anaStoricoFrame.jsp", nav);

			}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response)throws Exception{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();



		if (sessione.getAttribute(NOMEFINDER)!= null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new AnagrafeStoricoFinder().getClass()){
				finder = (AnagrafeStoricoFinder)sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}


		finder = (AnagrafeStoricoFinder)gestireMultiPagina(finder,request);

		AnagrafeStoricoAnagrafeLogic logic = new AnagrafeStoricoAnagrafeLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareLista(vettoreRicerca,finder);

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista


	    Vector vct_lista= (Vector)ht.get(AnagrafeStoricoAnagrafeLogic.LISTA);
		finder = (AnagrafeStoricoFinder)ht.get("FINDER");
		sessione.setAttribute(AnagrafeStoricoAnagrafeLogic.LISTA, vct_lista);
		sessione.setAttribute(NOMEFINDER, finder);

		nav = new PulsantiNavigazione();
		if (chiamataEsterna){

			nav.chiamataEsternaLista();
			nav.setExt("1");
			nav.setPrimo(true);
		}
		else
			nav.chiamataInternaLista();
		this.chiamaPagina(request,response,"anagrafe/anaStoricoFrame.jsp",nav);
	}




	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception{
				// prendere dalla request il parametrio identificativo dell'oggetto cliccato
				// eseguire la query per caricare l'oggetto selezionato dal db
				// chiamare la pagina di dettaglio

				String azione = "";
				HttpSession sessione = request.getSession();
				
				removeOggettiIndiceDaSessione(sessione);
				
				AnagrafeStoricoFinder finder = null;
				//boolean chiamataEsterna = false;
				if (sessione.getAttribute(NOMEFINDER) !=null){
					if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new AnagrafeStoricoFinder().getClass()){
						finder = (AnagrafeStoricoFinder)sessione.getAttribute(NOMEFINDER);
					}
				}

				if (request.getParameter("AZIONE")!= null)
					azione = request.getParameter("AZIONE");
				//gestioneMultiRecord(request,response,azione,finder,sessione,"LISTA_ANAGRAFE",(Vector)sessione.getAttribute("LISTA_ANAGRAFE"),NOMEFINDER);
				gestioneMultiRecord(request,response,azione,finder,sessione,AnagrafeStoricoAnagrafeLogic.LISTA,(Vector)sessione.getAttribute(AnagrafeStoricoAnagrafeLogic.LISTA),NOMEFINDER);
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
				AnagrafeStoricoAnagrafeLogic logic = new AnagrafeStoricoAnagrafeLogic(this.getEnvUtente(request));
				Hashtable ht = logic.mCaricareDettaglio(oggettoSel);

				Vector vct_dettStorico= (Vector)ht.get(AnagrafeStoricoAnagrafeLogic.ANAGRAFE_STORICO);

				sessione.setAttribute(AnagrafeStoricoAnagrafeLogic.ANAGRAFE_STORICO, vct_dettStorico);

				nav = new PulsantiNavigazione();
				if (chiamataEsterna)
					nav.chiamataEsternaDettaglio();
				else
					nav.chiamataInternaDettaglio();

				this.chiamaPagina(request,response,"anagrafe/anaStoricoFrame.jsp", nav);
			}


	public EscFinder getNewFinder(){
		return new AnagrafeStoricoFinder();
	}
	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
		return ((Anagrafe)listaOggetti.get(recordSuccessivo)).getCodAnagrafe();
	}


	public String getTema() {
		return "Popolazione";
	}
	public String getTabellaPerCrossLink() {
		return "DIOGENE.SIT_D_PERSONA";
	}

}



