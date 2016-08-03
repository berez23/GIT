/*
 * Created on 3-mag-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.anagrafe.servlet;

import it.escsolution.escwebgis.anagrafe.bean.Famiglia;
import it.escsolution.escwebgis.anagrafe.bean.FamigliaFinder;
import it.escsolution.escwebgis.anagrafe.logic.AnagrafeAnagrafeLogic;
import it.escsolution.escwebgis.anagrafe.logic.AnagrafeFamigliaLogic;
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

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AnagrafeFamigliaServlet extends EscServlet implements EscService {

	private String recordScelto;
	private AnagrafeFamigliaLogic logic = null;
	private FamigliaFinder finder = null;
	private final String NOMEFINDER = "FINDER9";
	PulsantiNavigazione nav;

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
			elementoFiltro.setLabel("Comune");
			elementoFiltro.setAttributeName("COMUNE");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringaRid);
			elementoFiltro.setListaValori(vctComuni);
			elementoFiltro.setCampoFiltrato("pop_famiglie.FK_COMUNI");
			listaElementiFiltro.add(elementoFiltro);


			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Codice Famiglia");
			elementoFiltro.setAttributeName("COD_FAMIGLIA");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringa);
			elementoFiltro.setCampoFiltrato("PK_CODI_FAMIGLIE");
			listaElementiFiltro.add(elementoFiltro);


			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Denominazione");
			elementoFiltro.setAttributeName("DENOMINAZIONE");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringa);
			elementoFiltro.setCampoFiltrato("DENOMINAZIONE");
			listaElementiFiltro.add(elementoFiltro);

			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Tipo Famiglia");
			elementoFiltro.setAttributeName("TIPO_FAMIGLIA");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringa);
			elementoFiltro.setCampoFiltrato("TIPO_FAMIGLIA");
			listaElementiFiltro.add(elementoFiltro);

			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Codice Fiscale Capo Famiglia");
			elementoFiltro.setAttributeName("CODICE_FISCALE");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringa);
			elementoFiltro.setCampoFiltrato("COD_FISCALE_CAPO");
			//elementoFiltro.setListaValori(vct_catego);
			listaElementiFiltro.add(elementoFiltro);



			sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
			sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));

			//chiamare la pagina di ricerca
			nav = new PulsantiNavigazione();
			nav.chiamataRicerca();

			this.chiamaPagina(request,response,"anagrafe/famFrame.jsp",nav);

		}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response)throws Exception{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();



		if (sessione.getAttribute(NOMEFINDER)!= null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new FamigliaFinder().getClass()){
				finder = (FamigliaFinder)sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
			}


			finder = (FamigliaFinder)gestireMultiPagina(finder,request);
		AnagrafeFamigliaLogic logic = new AnagrafeFamigliaLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaFamiglia(vettoreRicerca,finder);

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista


		Vector vct_lista_famiglie= (Vector)ht.get("LISTA_FAMIGLIA");
		finder = (FamigliaFinder)ht.get("FINDER");
		sessione.setAttribute("LISTA_FAMIGLIA",vct_lista_famiglie);
		sessione.setAttribute(NOMEFINDER,finder);

		nav = new PulsantiNavigazione();
		if (chiamataEsterna){

			nav.chiamataEsternaLista();
			nav.setExt("1");
		}
		else
			nav.chiamataInternaLista();
		this.chiamaPagina(request,response,"anagrafe/famFrame.jsp", nav);

	}

	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response,boolean notListaPrincipale)throws Exception{
		// prendere dalla request il parametro identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio
			String azione = "";
			HttpSession sessione = request.getSession();
			
			removeOggettiIndiceDaSessione(sessione);
			
			if (sessione.getAttribute(NOMEFINDER) !=null){
				if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new FamigliaFinder().getClass()){
					finder = (FamigliaFinder)sessione.getAttribute(NOMEFINDER);
				}
				else{
					// il finder non corrisponde -->
					finder = null;

				}
			}
			if (request.getParameter("AZIONE")!= null)
					azione = request.getParameter("AZIONE");

			gestioneMultiRecord(request,response,azione,finder,sessione,"LISTA_FAMIGLIA",(Vector)sessione.getAttribute("LISTA_FAMIGLIA"),NOMEFINDER);
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
			 * carica il dettaglio della famiglia
			 */
			mCaricareAnagrafe(request, response, oggettoSel);

			AnagrafeFamigliaLogic logic = new AnagrafeFamigliaLogic(this.getEnvUtente(request));
			Hashtable ht = logic.mCaricareDettaglioFamiglia(oggettoSel);



			Famiglia fam = (Famiglia)ht.get("FAMIGLIA");
			sessione.setAttribute("FAMIGLIA",fam);


			nav = new PulsantiNavigazione();
			if (chiamataEsterna)
				nav.chiamataEsternaDettaglio();
			else
				nav.chiamataInternaDettaglio();

			this.chiamaPagina(request,response,"anagrafe/famFrame.jsp", nav);

	}

	private void mCaricareAnagrafe(HttpServletRequest request,HttpServletResponse response, String chiaveAna) throws Exception{
				/*
				 * carico la lista dei contribuenti
				 */
				AnagrafeAnagrafeLogic logic = new AnagrafeAnagrafeLogic(this.getEnvUtente(request));
				Vector listaAna = (Vector)((Hashtable)logic.mCaricareListaAnagrafePerFamiglia(chiaveAna)).get("LISTA_ANAGRAFE2");


				HttpSession sessione = request.getSession();

			sessione.setAttribute("LISTA_ANAGRAFE2",listaAna);




	}


	public Hashtable executeLogic(FamigliaFinder finder2, HttpServletRequest request) throws Exception{
		finder = (FamigliaFinder)finder2;
		AnagrafeFamigliaLogic logic = new AnagrafeFamigliaLogic(this.getEnvUtente(request));
		return logic.mCaricareListaFamiglia(this.vettoreRicerca, finder);
	}

	public EscFinder getNewFinder(){
			return new FamigliaFinder();
		}
	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
		return ((Famiglia)listaOggetti.get(recordSuccessivo)).getCodFamiglia();
	}
	public String getTema() {
		return "Popolazione";
	}
	public String getTabellaPerCrossLink() {
		return "POP_FAMIGLIE";
	}

}
