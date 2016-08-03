package it.escsolution.escwebgis.soggetto.servlet;

import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.soggetto.bean.SoggettoFascicolo;
import it.escsolution.escwebgis.soggetto.bean.SoggettoFascicoloFinder;
import it.escsolution.escwebgis.soggetto.logic.SoggettoFascicoloLogic;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
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
public class SoggettoFascicoloServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private SoggettoFascicoloLogic logic = null;
	private final String NOMEFINDER = "FINDER25";
	private SoggettoFascicoloFinder finder = null;

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
		elementoFiltro.setAttributeName("FK_COMUNE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vctComuni);
		elementoFiltro.setCampoFiltrato("XML_SOGGETTI_CHIAVI.CODENT");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cognome");
		elementoFiltro.setAttributeName("COGNOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("XML_SOGGETTI_CHIAVI.COGNOME");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Nome");
		elementoFiltro.setAttributeName("NOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("XML_SOGGETTI_CHIAVI.NOME");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Denominazione");
		elementoFiltro.setAttributeName("DENOMINAZIONE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("XML_SOGGETTI_CHIAVI.DENOMINAZIONE");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Codice Fiscale");
		elementoFiltro.setAttributeName("CODICEFISCALE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("XML_SOGGETTI_CHIAVI.COD_FISC");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Partita Iva");
		elementoFiltro.setAttributeName("PARTITAIVA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("XML_SOGGETTI_CHIAVI.PART_IVA");
		listaElementiFiltro.add(elementoFiltro);

		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"soggetto/sogFFrame.jsp", nav);
	}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response,boolean notListaPrincipale)throws Exception{

		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new SoggettoFascicoloFinder().getClass()){
				finder = (SoggettoFascicoloFinder)sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (SoggettoFascicoloFinder)gestireMultiPagina(finder,request);

		SoggettoFascicoloLogic logic = new SoggettoFascicoloLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaSoggetto(vettoreRicerca,finder);

		Vector vct_lista= (Vector)ht.get("LISTA");
		finder = (SoggettoFascicoloFinder)ht.get("FINDER");
		List lista_db = (List)ht.get("LISTA_DB");

		sessione.setAttribute("LISTA_SOGGETTIFASCICOLO_DB",lista_db);
		sessione.setAttribute("LISTA_SOGGETTIFASCICOLO",vct_lista);

		if (!notListaPrincipale){
			finder = (SoggettoFascicoloFinder)ht.get("FINDER");
			sessione.setAttribute(NOMEFINDER,finder);
		}

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else{
			nav.chiamataInternaLista();
			nav.setPrimo(false);
		}

		this.chiamaPagina(request,response,"soggetto/sogFFrame.jsp", nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception{
		// prendere dalla request il parametrio identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio

		String azione = "";
		HttpSession sessione = request.getSession();
		SoggettoFascicoloFinder finder = null;

		if (sessione.getAttribute(NOMEFINDER) !=null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new SoggettoFascicoloFinder().getClass()){
				finder = (SoggettoFascicoloFinder)sessione.getAttribute(NOMEFINDER);
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

		//recupero la lista dei db dell'applicazione
		List elencoDB = (List)sessione.getAttribute("LISTA_SOGGETTIFASCICOLO_DB");
		SoggettoFascicoloLogic logic = new SoggettoFascicoloLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioSoggetto(oggettoSel,elencoDB);

		SoggettoFascicolo sog = (SoggettoFascicolo)ht.get("SOGGETTO");
		List elencoCross = (List)ht.get("ELENCO_CROSS");
		sessione.setAttribute("SOGGETTOFASCICOLO",sog);
		sessione.setAttribute("ELENCO_CROSS",elencoCross);

		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();


		this.chiamaPagina(request,response,"soggetto/sogFFrame.jsp", nav);
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
		return ((SoggettoFascicolo)listaOggetti.get(recordSuccessivo)).getCodSoggetto();
	}

	public EscFinder getNewFinder(){
		return new SoggettoFascicoloFinder();
	}

	public String getTema() {
		return "Soggetto";
	}

	public String getTabellaPerCrossLink() {
		return "SIT_SGT";
	}
}

