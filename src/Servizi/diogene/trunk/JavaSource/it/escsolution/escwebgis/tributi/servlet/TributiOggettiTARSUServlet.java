/*
 * Created on 27-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.tributi.servlet;

import it.escsolution.escwebgis.catasto.bean.Immobile;
import it.escsolution.escwebgis.catasto.logic.CatastoImmobiliLogic;
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
import it.escsolution.escwebgis.tributi.bean.Contribuente;
import it.escsolution.escwebgis.tributi.bean.OggettiTARSU;
import it.escsolution.escwebgis.tributi.bean.OggettiTARSUFinder;
import it.escsolution.escwebgis.tributi.logic.TributiContribuentiLogic;
import it.escsolution.escwebgis.tributi.logic.TributiOggettiTARSULogic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletConfig;
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
public class TributiOggettiTARSUServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private OggettiTARSUFinder finder = null;
	private TributiOggettiTARSULogic logic = null;
	private final String NOMEFINDER = "FINDER7";
	
	String pathDatiDiogene = "";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        pathDatiDiogene = super.getPathDatiDiogene();
    }

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

				
				if (request.getParameter("listavie") != null)
					mListaVie(request, response, "SIT_T_VIE", null, "VIADES","VIA");	
				else  {
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
						mCaricareDettaglio(request,response,false);
						this.leggiCrossLink(request);
						break;
					/*case 4:
						// ho cliccato su un elemento --> visualizzo il dettaglio
						mCaricareDettaglioDaNinc(request,response,true);
						this.leggiCrossLink(request);
						break;
						*/
	
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


		}

	
	
	
	
	/**
	 * @param request
	 * @param response
	 */
	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response,boolean soloDettaglio)throws Exception{
			// prendere dalla request il parametrio identificativo dell'oggetto cliccato
			// eseguire la query per caricare l'oggetto selezionato dal db
			// chiamare la pagina di dettaglio

			String azione = "";
			HttpSession sessione = request.getSession();
			//boolean chiamataEsterna = false;
			if (sessione.getAttribute(NOMEFINDER) !=null){
				if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new OggettiTARSUFinder().getClass()){
					finder = (OggettiTARSUFinder)sessione.getAttribute(NOMEFINDER);
				}
				else{
					// il finder non corrisponde -->
					//sessione.removeAttribute(NOMEFINDER);
					finder = null;
				}
			}
			if (request.getParameter("AZIONE")!= null)
				azione = request.getParameter("AZIONE");
			gestioneMultiRecord(request,response,azione,finder,sessione,"LISTA_TARSU",(Vector)sessione.getAttribute("LISTA_TARSU"),NOMEFINDER);
			if (!azione.equals("")){
				Vector listaOggettiTARSU = (Vector)sessione.getAttribute("LISTA_TARSU");
			}
			else{
				oggettoSel = "";recordScelto = "";
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
		TributiOggettiTARSULogic logic = new TributiOggettiTARSULogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioOggettiTARSU(oggettoSel, pathDatiDiogene);

		/*
		 * carico la lista degli intestatari associati
		 */
		OggettiTARSU tarsu = (OggettiTARSU)ht.get("TARSU");
		if (tarsu!= null)
			if (tarsu.getChiave()!=null)
				mCaricareContribuenti(request, response, tarsu.getChiave());
		sessione.setAttribute("TARSU",tarsu);
		if(ht.get("TARSU_DETTAGLIO_VANI") != null)
			sessione.setAttribute("TARSU_DETTAGLIO_VANI",ht.get("TARSU_DETTAGLIO_VANI"));
		if(ht.get(TributiOggettiTARSULogic.TARSU_DOCFA_COLLEGATI) != null)
			sessione.setAttribute(TributiOggettiTARSULogic.TARSU_DOCFA_COLLEGATI,ht.get(TributiOggettiTARSULogic.TARSU_DOCFA_COLLEGATI));
		
		CatastoPlanimetrieComma340Logic logic340 = new CatastoPlanimetrieComma340Logic(this.getEnvUtente(request));
		ArrayList<ArrayList<String>> planimetrieComma340 = logic340.getPlanimetrieComma340(tarsu.getFoglio(), tarsu.getParCatastali(), tarsu.getSubalterno(), pathDatiDiogene);
		request.getSession().setAttribute(CatastoPlanimetrieComma340Logic.PLANIMETRIE_COMMA_340_TARSU, planimetrieComma340);
		
		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		this.chiamaPagina(request,response,"tributi/oggTARSUFrame.jsp", nav);


}


		/**
		 * @param request
		 * @param response
		 */
	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//prendere dalla request i parametri di ricerca impostati dall'utente
		 HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null){
			finder = (OggettiTARSUFinder)sessione.getAttribute(NOMEFINDER);
		}
		else{
			finder = null;
		}


		 finder = (OggettiTARSUFinder)gestireMultiPagina(finder,request);

		 TributiOggettiTARSULogic logic = new TributiOggettiTARSULogic(this.getEnvUtente(request));
		 Hashtable ht = logic.mCaricareListaOggettiTARSU(vettoreRicerca,finder);

		 // eseguire la query e caricare il vettore con i risultati
		 // chiamare la pagina di lista


		 Vector vct_lista_TARSU= (Vector)ht.get("LISTA_TARSU");
		 finder = (OggettiTARSUFinder)ht.get("FINDER");
		 sessione.setAttribute("LISTA_TARSU",vct_lista_TARSU);
		 sessione.setAttribute(NOMEFINDER,finder);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();

		 this.chiamaPagina(request,response,"tributi/oggTARSUFrame.jsp", nav);

		}

		/**
		 * @param request
		 * @param response
		 */
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
			elementoFiltro.setLabel("Comune");
			elementoFiltro.setAttributeName("FK_COMUNI");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringaRid);
			elementoFiltro.setListaValori(vctComuni);
			elementoFiltro.setCampoFiltrato("FK_COMUNI");
			listaElementiFiltro.add(elementoFiltro);

			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Foglio");
			elementoFiltro.setAttributeName("FOGLIO");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("numeroIntero");
			elementoFiltro.setListaOperatori(operatoriStringaRid);
			elementoFiltro.setCampoFiltrato("FOGLIO_CATASTO");
			listaElementiFiltro.add(elementoFiltro);


			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Particella");
			elementoFiltro.setAttributeName("PARTICELLA_CATASTO");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("numeroIntero");
			elementoFiltro.setListaOperatori(operatoriStringa);
			elementoFiltro.setCampoFiltrato("PARTICELLA_CATASTO");
			listaElementiFiltro.add(elementoFiltro);

			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Subalterno");
			elementoFiltro.setAttributeName("SUBALTERNO");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("numeroIntero");
			elementoFiltro.setListaOperatori(operatoriStringa);
			elementoFiltro.setCampoFiltrato("SUBALTERNO_CATASTO");
			listaElementiFiltro.add(elementoFiltro);

			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Via");
			elementoFiltro.setAttributeName("VIA");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringa);
			elementoFiltro.setCampoFiltrato("V.VIADES");
			elementoFiltro.setExtraHTML("<span><input type=\"button\" class=\"TXTmainLabel\" value=\"Cerca\" onclick=\"javascript:danRequest('"+it.escsolution.escwebgis.common.EscServlet.URL_PATH+"/TributiOggettiTARSU?listavie='+document.getElementById('VIA').value,'divListaVie')\"/></span><span id=\"divListaVie\" class=\"txtRow\"  ></span> \n");		
			listaElementiFiltro.add(elementoFiltro);

			
			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Numero Civico");
			elementoFiltro.setAttributeName("CIVICO");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("numeroIntero");
			elementoFiltro.setListaOperatori(operatoriStringa);
			elementoFiltro.setCampoFiltrato("C.NUMERO");
			listaElementiFiltro.add(elementoFiltro);


			sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
			sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
			
			nav.chiamataRicerca();
			this.chiamaPagina(request,response,"tributi/oggTARSUFrame.jsp", nav);


		}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
		return ((OggettiTARSU)listaOggetti.get(recordSuccessivo)).getParCatastali();
	}

	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (OggettiTARSUFinder)finder2;
		TributiOggettiTARSULogic logic = new TributiOggettiTARSULogic(this.getEnvUtente(request));
		return logic.mCaricareListaOggettiTARSU(this.vettoreRicerca,finder);
	}

	private void mCaricareContribuenti(HttpServletRequest request,HttpServletResponse response, String chiaveTARSU) throws Exception{
		/*
		 * carico la lista dei contribuenti
		 */
		TributiContribuentiLogic logic = new TributiContribuentiLogic(this.getEnvUtente(request));
		Contribuente cont = ((Contribuente)logic.mCaricareListaContribuentiPerOggettoTARSU(chiaveTARSU));


		HttpSession sessione = request.getSession();
		sessione.setAttribute("CONTRIBUENTE_TARSU",cont);
	}

	public EscFinder getNewFinder(){
					return new OggettiTARSUFinder();
				}
	public String getTema() {
		return "Tributi";
	}
	public String getTabellaPerCrossLink() {
		return "TRI_OGGETTI_TARSU";
	}


}
