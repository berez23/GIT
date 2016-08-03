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
import it.escsolution.escwebgis.tributi.bean.OggettiICI;
import it.escsolution.escwebgis.tributi.bean.OggettiICIFinder;
import it.escsolution.escwebgis.tributi.logic.TributiContribuentiLogic;
import it.escsolution.escwebgis.tributi.logic.TributiOggettiICILogic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
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
public class TributiOggettiICIServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private OggettiICIFinder finder = null;
	private TributiOggettiICILogic logic = null;
	private final String NOMEFINDER = "FINDER6";

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
				mCaricareDettaglio(request,response,false);
				this.leggiCrossLink(request);
				break;
			/*case 4:
				// ho cliccato su un elemento --> visualizzo il dettaglio
				mCaricareDettaglio(request,response,true);
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
				if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new OggettiICIFinder().getClass()){
					finder = (OggettiICIFinder)sessione.getAttribute(NOMEFINDER);
				}
				else{
					// il finder non corrisponde -->
					//sessione.removeAttribute("FINDER");
					finder = null;
				}
			}
			if (request.getParameter("AZIONE")!= null)
				azione = request.getParameter("AZIONE");
			gestioneMultiRecord(request,response,azione,finder,sessione,"LISTA_ICI",(Vector)sessione.getAttribute("LISTA_ICI"),NOMEFINDER);
			if (!azione.equals("")){
				Vector listaOggettiICI = (Vector)sessione.getAttribute("LISTA_ICI");
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
			TributiOggettiICILogic logic = new TributiOggettiICILogic(this.getEnvUtente(request));
			Hashtable ht = logic.mCaricareDettaglioOggettiICI(oggettoSel);

			OggettiICI ici = (OggettiICI)ht.get("ICI");
			ArrayList contrIci = (ArrayList)ht.get("CONTR_LIST");
			//mCaricareContribuenti(request, response, ici.getCodContribuente());

			
			sessione.setAttribute("ICI",ici);
			sessione.setAttribute("CONTR_LIST",contrIci);
			sessione.setAttribute("SOLO_DETTAGLIO",new Boolean(soloDettaglio));

			if(ht.get(TributiOggettiICILogic.ICI_DOCFA_COLLEGATI) != null && ((List)ht.get(TributiOggettiICILogic.ICI_DOCFA_COLLEGATI)).size() >0)
			{
				sessione.setAttribute(TributiOggettiICILogic.ICI_DOCFA_COLLEGATI,ht.get(TributiOggettiICILogic.ICI_DOCFA_COLLEGATI));
			}
			else
			{	
				sessione.removeAttribute(TributiOggettiICILogic.ICI_DOCFA_COLLEGATI);
			}
			
			if(chiamataEsterna)
				nav.chiamataEsternaDettaglio();
			else
				nav.chiamataInternaDettaglio();

			this.chiamaPagina(request,response,"tributi/oggICIFrame.jsp", nav);			
				
		}


			/**
			 * @param request
			 * @param response
			 */
			private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception {
				
				if (request.getParameter("POPUP") != null && new Boolean(request.getParameter("POPUP")).booleanValue()) {
					//ricerca (in popup) per codice fiscale - anno
					mCaricareListaSoggetto(request,response);
					return;					
				}
				
				//prendere dalla request i parametri di ricerca impostati dall'utente
				 HttpSession sessione = request.getSession();

				if (sessione.getAttribute(NOMEFINDER)!= null){
					finder = (OggettiICIFinder)sessione.getAttribute(NOMEFINDER);
				}
				else{
					finder = null;
				}

				 finder = (OggettiICIFinder)gestireMultiPagina(finder,request);

				 TributiOggettiICILogic logic = new TributiOggettiICILogic(this.getEnvUtente(request));
				 Hashtable ht = logic.mCaricareListaOggettiICI(vettoreRicerca,finder);

				 // eseguire la query e caricare il vettore con i risultati
				 // chiamare la pagina di lista


				 Vector vct_lista_ICI= (Vector)ht.get("LISTA_ICI");
				 finder = (OggettiICIFinder)ht.get("FINDER");
				 //request.setAttribute("LISTA_ICI",vct_lista_ICI);
				 sessione.setAttribute("LISTA_ICI",vct_lista_ICI);
				 sessione.setAttribute(NOMEFINDER,finder);

				if(chiamataEsterna)
					nav.chiamataEsternaLista();
				else
					nav.chiamataInternaLista();

				this.chiamaPagina(request,response,"tributi/oggICIFrame.jsp", nav);

			}
			
			private void mCaricareListaSoggetto(HttpServletRequest request, HttpServletResponse response) throws Exception {
				
				 HttpSession sessione = request.getSession();

				 TributiOggettiICILogic logic = new TributiOggettiICILogic(this.getEnvUtente(request));
				 String codFiscaleDic = (request.getParameter("CODICE_FISCALE_DIC"));
				 int annoImposta = Integer.parseInt(request.getParameter("ANNO_IMPOSTA"));
	
				 Vector vct_lista_ICI = logic.mCaricareListaOggettiICISoggetto(codFiscaleDic, annoImposta);
				 sessione.setAttribute("LISTA_ICI_POPUP",vct_lista_ICI);

				 if(chiamataEsterna)
					 nav.chiamataEsternaLista();
				 else
					 nav.chiamataInternaLista();

				 this.chiamaPagina(request,response,"tributi/oggICIPopupSoggetto.jsp", nav);
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
				elementoFiltro.setCampoFiltrato("tri_oggetti_ici.FK_COMUNI");
				listaElementiFiltro.add(elementoFiltro);

				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Foglio");
				elementoFiltro.setAttributeName("FOGLIO");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriStringa);
				elementoFiltro.setCampoFiltrato("tri_oggetti_ici.FOGLIO_CATASTO");
				listaElementiFiltro.add(elementoFiltro);


				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Particella");
				elementoFiltro.setAttributeName("PARTICELLA");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriStringa);
				elementoFiltro.setCampoFiltrato("tri_oggetti_ici.PARTICELLA_CATASTO");
				listaElementiFiltro.add(elementoFiltro);

				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Subalterno");
				elementoFiltro.setAttributeName("SUBALTERNO");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriStringa);
				elementoFiltro.setCampoFiltrato("tri_oggetti_ici.SUBALTERNO_CATASTO");
				listaElementiFiltro.add(elementoFiltro);

				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Categoria Catastale");
				elementoFiltro.setAttributeName("CATEGORIA");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriStringa);
				elementoFiltro.setCampoFiltrato("tri_oggetti_ici.CATEGORIA_CATASTALE");
				listaElementiFiltro.add(elementoFiltro);

				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Classe");
				elementoFiltro.setAttributeName("CLASSE");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("numeroIntero");
				elementoFiltro.setListaOperatori(operatoriStringa);
				elementoFiltro.setCampoFiltrato("tri_oggetti_ici.CLASSE");
				listaElementiFiltro.add(elementoFiltro);
				
				elementoFiltro = new EscElementoFiltro();
				elementoFiltro.setLabel("Partita");
				elementoFiltro.setAttributeName("PARTITA");
				elementoFiltro.setTipo("S");
				elementoFiltro.setCampoJS("");
				elementoFiltro.setListaOperatori(operatoriStringa);
				elementoFiltro.setCampoFiltrato("tri_oggetti_ici.PAR_CATASTALI");
				listaElementiFiltro.add(elementoFiltro);
				
				sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
				sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
				
				 // chiamare la pagina di ricerca
				 nav.chiamataRicerca();
				 this.chiamaPagina(request,response,"tributi/oggICIFrame.jsp", nav);

			}


	private void mCaricareContribuenti(HttpServletRequest request,HttpServletResponse response, String chiaveICI) throws Exception{
			/*
			 * carico la lista dei contribuenti
			 */
			TributiContribuentiLogic logic = new TributiContribuentiLogic(this.getEnvUtente(request));
			Contribuente cont = (Contribuente)logic.mCaricareListaContribuentiPerOggettoICI(chiaveICI);

			HttpSession sessione = request.getSession();
			sessione.setAttribute("CONTRIBUENTE_ICI",cont);


		}



	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
		return ((OggettiICI)listaOggetti.get(recordSuccessivo)).getChiave();
		}

	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (OggettiICIFinder)finder2;
		TributiOggettiICILogic logic = new TributiOggettiICILogic(this.getEnvUtente(request));
		return logic.mCaricareListaOggettiICI(this.vettoreRicerca,finder);
		}

	public EscFinder getNewFinder(){
				return new OggettiICIFinder();
			}


	public String getTema() {
		return "Tributi";
	}
	public String getTabellaPerCrossLink() {
		return "TRI_OGGETTI_ICI";
	}

}


