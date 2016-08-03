/*
 * Created on 12-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.catasto.servlet;

import it.escsolution.escwebgis.catasto.bean.Gauss;
import it.escsolution.escwebgis.catasto.bean.GaussFinder;
import it.escsolution.escwebgis.catasto.logic.CatastoGaussLogic;
import it.escsolution.escwebgis.catasto.logic.CatastoImmobiliLogic;
import it.escsolution.escwebgis.catasto.logic.CatastoTerreniLogic;
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

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CatastoGaussServlet extends EscServlet implements EscService {

			private String recordScelto;
			private CatastoGaussLogic logic = null;
			private GaussFinder finder = null;
			private final String NOMEFINDER = "FINDER12";
			PulsantiNavigazione nav = new PulsantiNavigazione();


	public void EseguiServizio(
		HttpServletRequest request,
		HttpServletResponse response)
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
				//this.leggiCrossLink(request);
				break;
			case 33:
				// ho cliccato su un elemento --> visualizzo il dettaglio
				mCaricareDettaglio(request,response);
				//this.leggiCrossLink(request);
				break;
			/*case 4:
				// ho cliccato su un elemento --> visualizzo il dettaglio
				mCaricareDettaglio(request,response);
				break;*/
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

	private void mCaricareFormRicerca(HttpServletRequest request,HttpServletResponse response) throws Exception{
			// caricare dati che servono nella pagina di ricerca --> eventuali combo

			CatastoImmobiliLogic logic = new CatastoImmobiliLogic(this.getEnvUtente(request));
			Hashtable ht = logic.mCaricareDatiFormRicerca(request.getUserPrincipal().getName());
			Vector vct_comuni = (Vector) ht.get("LISTA_COMUNI");



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
			elementoFiltro.setLabel("Comune");
			elementoFiltro.setAttributeName("Comune");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringa);
			elementoFiltro.setListaValori(vct_comuni);
			//elementoFiltro.setCampoFiltrato("cat_particelle_gauss.COMUNE");
			elementoFiltro.setCampoFiltrato("siticomu.CODI_FISC_LUNA");
			listaElementiFiltro.add(elementoFiltro);

			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Foglio");
			elementoFiltro.setAttributeName("FOGLIO");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("numeroIntero");
			elementoFiltro.setListaOperatori(operatoriStringaRid);
			elementoFiltro.setCampoFiltrato("cat_particelle_gauss.FOGLIO");
			listaElementiFiltro.add(elementoFiltro);


			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Particella");
			elementoFiltro.setAttributeName("PARTICELLA");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("lpad5_0");
			elementoFiltro.setListaOperatori(operatoriStringaRid);
			elementoFiltro.setCampoFiltrato("cat_particelle_gauss.PARTICELLA");
			listaElementiFiltro.add(elementoFiltro);






			sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
			sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
			
			// chiamare la pagina di ricerca
			nav.chiamataRicerca();
			this.chiamaPagina(request,response,"catasto/gaussFrame.jsp",nav);
		}


	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response)throws Exception{
				// prendere dalla request i parametri di ricerca impostati dall'utente
				HttpSession sessione = request.getSession();



				if (sessione.getAttribute(NOMEFINDER)!= null){
					finder = (GaussFinder)sessione.getAttribute(NOMEFINDER);
				}
				else{
					finder = null;
				}
				finder = (GaussFinder)gestireMultiPagina(finder,request);

				CatastoGaussLogic logic = new CatastoGaussLogic(this.getEnvUtente(request));
				Hashtable ht = logic.mCaricareListaGauss(vettoreRicerca,finder);

				// eseguire la query e caricare il vettore con i risultati
				// chiamare la pagina di lista

				Vector vct_lista_gauss = (Vector)ht.get("LISTA_GAUSS");
				finder = (GaussFinder)ht.get("FINDER");
				sessione.setAttribute("LISTA_GAUSS",vct_lista_gauss);
				sessione.setAttribute(NOMEFINDER,finder);

				if(chiamataEsterna){
					nav.chiamataEsternaLista();
					nav.setExt("1");
				}
				else
					nav.chiamataInternaLista();

				this.chiamaPagina(request,response,"catasto/gaussFrame.jsp",nav);
			}

	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception{
				// prendere dalla request il parametro identificativo dell'oggetto cliccato
				// eseguire la query per caricare l'oggetto selezionato dal db
				// chiamare la pagina di dettaglio
					String azione = "";
					HttpSession sessione = request.getSession();
					
					removeOggettiIndiceDaSessione(sessione);
					
					GaussFinder finder = null;
					if (sessione.getAttribute(NOMEFINDER) !=null){
						if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new GaussFinder().getClass()){
							finder = (GaussFinder)sessione.getAttribute(NOMEFINDER);
						}
						else{
							// il finder non corrisponde -->
							finder = null;
						}
					}
					
				String layer=null;
					
				if (request.getParameter("AZIONE")!= null)
						azione = request.getParameter("AZIONE");
				gestioneMultiRecord(request,response,azione,finder,sessione,"LISTA_GAUSS",(Vector)sessione.getAttribute("LISTA_GAUSS"),NOMEFINDER);
				if (azione.equals("")){
					
					oggettoSel="";recordScelto="";
					sessione.removeAttribute("BACK_JS_COUNT");
					sessione.removeAttribute("BACK_RECORD_ENABLE");
					if (request.getParameter("OGGETTO_SEL")!= null){
						oggettoSel = request.getParameter("OGGETTO_SEL");
					}
					// se vengo da mappa
					if (request.getParameter("LAYER")!= null){
						layer = request.getParameter("LAYER");
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


				CatastoGaussLogic logic = new CatastoGaussLogic(this.getEnvUtente(request));
				Hashtable ht = logic.mCaricareDettaglioGauss(oggettoSel, layer);

				mCaricareTerreni(request, response, oggettoSel);

				Gauss gauss = (Gauss)ht.get("GAUSS");
				sessione.setAttribute("GAUSS",gauss);

				//controllo se ho elaborato un fabbricato
				if (gauss.getLayer().equals("FABBRICATI")){
					Vector listUnimm = (Vector)ht.get("UNIMM");
					sessione.setAttribute("GAUSS_UNIMM",listUnimm);
				}

				if(chiamataEsterna)
					nav.chiamataEsternaDettaglio();
				else
					nav.chiamataInternaDettaglio();

				/*
				 * gestione mappa
				 *
				 */
//ALERT: al momento commentato perchè non si trova la corrispondenza nel DB per i dati grafici!!
				/*JavaBeanGlobalVar beanGlobale = logic.mCaricareDatiGrafici(oggettoSel);
				if (beanGlobale != null){
					nav.setMappa(true);
					sessione.setAttribute("MAPPA",beanGlobale);
				}*/
				// fine gestione mappa



				this.chiamaPagina(request,response,"catasto/gaussFrame.jsp",nav);

			}

	private void mCaricareTerreni(HttpServletRequest request,HttpServletResponse response, String particella) throws Exception{
			/*
			 * carico la lista dei terreni
			 */
			HttpSession sessione = request.getSession();

			CatastoTerreniLogic logic = new CatastoTerreniLogic(this.getEnvUtente(request));
			Hashtable ht = logic.mCaricareDettaglioTerrenoGauss(particella);


	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
		return ((Gauss)listaOggetti.get(recordSuccessivo)).getChiave();
	}
	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (GaussFinder)finder2;
		CatastoGaussLogic logic = new CatastoGaussLogic(this.getEnvUtente(request));
		return logic.mCaricareListaGauss(this.vettoreRicerca, finder);
	}
	public EscFinder getNewFinder(){
		return new GaussFinder();
	}

	public String getTema() {

		return "Catasto";
	}

	public String getTabellaPerCrossLink() {
		return "CAT_PARTICELLE_GAUSS";
	}
}
