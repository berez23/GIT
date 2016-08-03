/*
 * Created on 2-lug-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.soggetto.servlet;

import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.soggetto.bean.Soggetto;
import it.escsolution.escwebgis.soggetto.bean.SoggettoFinder;
import it.escsolution.escwebgis.soggetto.bean.TipoSoggetto;
import it.escsolution.escwebgis.soggetto.logic.SoggettoSoggettoLogic;

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
public class SoggettoSoggettoServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private SoggettoSoggettoLogic logic = null;
	private final String NOMEFINDER = "FINDER14";
	private SoggettoFinder finder = null;

	public void EseguiServizio(
		HttpServletRequest request,HttpServletResponse response)
				throws ServletException, IOException {
				/*
				 * ad ogni uc corrisponde una funzionalità diversa
				 *
				 */
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

					HttpSession sessione = request.getSession();

					/** Comune */
					Vector vctComuni = new ComuniLogic(this.getEnvUtente(request)).getListaComuniUtente(request.getUserPrincipal().getName());
					/***/

					Vector vct2 = new Vector();
					vct2.add(new TipoSoggetto("","Tutti"));
					vct2.add(new TipoSoggetto("F","Fisico"));
					vct2.add(new TipoSoggetto("G","Giuridico"));


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
					elementoFiltro.setAttributeName("FK_COMUNE");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringaRid);
					elementoFiltro.setListaValori(vctComuni);
					elementoFiltro.setCampoFiltrato("FK_COMUNI");
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Tipo Soggetto");
					elementoFiltro.setAttributeName("TIPO_PERSONA");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringaRid);
					elementoFiltro.setListaValori(vct2);
					elementoFiltro.setCampoFiltrato("TIPO_PERSONA");
					listaElementiFiltro.add(elementoFiltro);

					elementoFiltro = new EscElementoFiltro();
					elementoFiltro.setLabel("Cognome");
					elementoFiltro.setAttributeName("COGNOME");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					//elementoFiltro.setListaValori(vct1);
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
					elementoFiltro.setLabel("Denominazione");
					elementoFiltro.setAttributeName("DENOMINAZIONE");
					elementoFiltro.setTipo("S");
					elementoFiltro.setCampoJS("");
					elementoFiltro.setListaOperatori(operatoriStringa);
					elementoFiltro.setCampoFiltrato("DENOMINAZIONE");
					listaElementiFiltro.add(elementoFiltro);


					sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
					sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
					//chiamare la pagina di ricerca
					nav.chiamataRicerca();
					this.chiamaPagina(request,response,"soggetto/sogFrame.jsp", nav);

				}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response,boolean notListaPrincipale)throws Exception{
			// prendere dalla request i parametri di ricerca impostati dall'utente
			HttpSession sessione = request.getSession();

			if (sessione.getAttribute(NOMEFINDER)!= null){
				if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new SoggettoFinder().getClass()){
					finder = (SoggettoFinder)sessione.getAttribute(NOMEFINDER);
				}
				else
					finder = null;
			}


			finder = (SoggettoFinder)gestireMultiPagina(finder,request);

			SoggettoSoggettoLogic logic = new SoggettoSoggettoLogic(this.getEnvUtente(request));
			Hashtable ht = logic.mCaricareListaSoggetto(vettoreRicerca,finder);

			// eseguire la query e caricare il vettore con i risultati
			// chiamare la pagina di lista


			Vector vct_lista_soggetti= (Vector)ht.get("LISTA_SOGGETTI");
			finder = (SoggettoFinder)ht.get("FINDER");

			sessione.setAttribute("LISTA_SOGGETTI",vct_lista_soggetti);

			if (!notListaPrincipale){
				finder = (SoggettoFinder)ht.get("FINDER");
				sessione.setAttribute(NOMEFINDER,finder);
			}


			if(chiamataEsterna)
				nav.chiamataEsternaLista();
			else
				nav.chiamataInternaLista();

			this.chiamaPagina(request,response,"soggetto/sogFrame.jsp", nav);
		}

	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception{
				// prendere dalla request il parametrio identificativo dell'oggetto cliccato
				// eseguire la query per caricare l'oggetto selezionato dal db
				// chiamare la pagina di dettaglio

				String azione = "";
				HttpSession sessione = request.getSession();
				SoggettoFinder finder = null;
				//boolean chiamataEsterna = false;
				if (sessione.getAttribute(NOMEFINDER) !=null){
					if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new SoggettoFinder().getClass()){
						finder = (SoggettoFinder)sessione.getAttribute(NOMEFINDER);
					}
				}
				if (request.getParameter("AZIONE")!= null)
					azione = request.getParameter("AZIONE");
				gestioneMultiRecord(request,response,azione,finder,sessione,"LISTA_SOGGETTI",(Vector)sessione.getAttribute("LISTA_SOGGETTI"),NOMEFINDER);
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
				SoggettoSoggettoLogic logic = new SoggettoSoggettoLogic(this.getEnvUtente(request));
				Hashtable ht = logic.mCaricareDettaglioSoggetto(oggettoSel);

				Soggetto sog = (Soggetto)ht.get("SOGGETTO");
				sessione.setAttribute("SOGGETTO",sog);

				TipoSoggetto tipo = (TipoSoggetto)ht.get("TIPO_PERSONA");
				sessione.setAttribute("TIPO_PERSONA",tipo);

				if(chiamataEsterna)
					nav.chiamataEsternaDettaglio();
				else
					nav.chiamataInternaDettaglio();


				this.chiamaPagina(request,response,"soggetto/sogFrame.jsp", nav);

				//EscFinder escf = (EscFinder)sessione.getAttribute(NOMEFINDER);


			}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
				return ((Soggetto)listaOggetti.get(recordSuccessivo)).getCodSoggetto();
				}

			/*public Hashtable executeLogic(EscFinder finder2) throws Exception{
				finder = (SoggettoFinder)finder2;
				SoggettoSoggettoLogic logic = new SoggettoSoggettoLogic(this.getEnvUtente());
				return logic.mCaricareListaSoggetto(this.vettoreRicerca,finder);
				}*/

			public EscFinder getNewFinder(){
						return new SoggettoFinder();
					}

			public String getTema() {
				return "Soggetto";
			}

		public String getTabellaPerCrossLink() {
				return "SOG_SOGGETTI";
			}
}

