package it.escsolution.escwebgis.pertinenzeAbit.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.pertinenzeAbit.bean.DatiCatastali;
import it.escsolution.escwebgis.pertinenzeAbit.bean.DatiTitolarita;
import it.escsolution.escwebgis.pertinenzeAbit.bean.PertinenzeAbit;
import it.escsolution.escwebgis.pertinenzeAbit.bean.PertinenzeAbitDatiCatastali;
import it.escsolution.escwebgis.pertinenzeAbit.bean.PertinenzeAbitFinder;
import it.escsolution.escwebgis.pertinenzeAbit.bean.PertinenzeAbitTitolare;
import it.escsolution.escwebgis.pertinenzeAbit.logic.PertinenzeAbitLogic;

public class PertinenzeAbitServlet  extends EscServlet implements EscService{

	private static final long serialVersionUID = -9077572667679991375L;
	
	private PertinenzeAbitLogic pertinenzeAbitLogic = null;
	PulsantiNavigazione nav = new PulsantiNavigazione();
	public final String NOMEFINDER = "FINDER106";
	
	public void EseguiServizio(
			HttpServletRequest request,
			HttpServletResponse response)
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
					mCaricareDettaglio(request, response, false);
					//this.leggiCrossLink(request);
					break;
				case 33:
					// ho cliccato su un bottone visualizza dettaglio --> visualizzo il dettaglio su una PopUp
					mCaricareDettaglio(request, response, false);
					//this.leggiCrossLink(request);
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

	private void mCaricareFormRicerca(HttpServletRequest request,HttpServletResponse response) throws Exception{
		// caricare dati che servono nella pagina di ricerca --> eventuali combo
		PertinenzeAbitLogic logic = new PertinenzeAbitLogic(this.getEnvUtente(request));
		
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
		operatoriStringaRid.add(new EscOperatoreFiltro("=","uguale"));

		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Foglio");
		elementoFiltro.setAttributeName("FOGLIO");
		elementoFiltro.setTipo("N");
		elementoFiltro.setCampoJS("numeroIntero");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("UI.FOGLIO");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Particella");
		elementoFiltro.setAttributeName("PARTICELLA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("lpad5_0");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("UI.PARTICELLA");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Subalterno");
		elementoFiltro.setAttributeName("SUBALTERNO");
		elementoFiltro.setTipo("N");
		elementoFiltro.setCampoJS("numeroIntero");
		elementoFiltro.setListaOperatori(operatoriNumerici);
		elementoFiltro.setCampoFiltrato("UI.UNIMM");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Codice Fiscale");
		elementoFiltro.setAttributeName("CODICE_FISCALE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("SC.CUAA");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cognome");
		elementoFiltro.setAttributeName("COGNOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("ANAG_SOGGETTI.COGNOME");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Nome");
		elementoFiltro.setAttributeName("NOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("ANAG_SOGGETTI.NOME");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Denominazione");
		elementoFiltro.setAttributeName("DENOMINAZIONE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("ANAG_SOGGETTI.DENOMINAZIONE");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Indirizzo Catastale");
		elementoFiltro.setAttributeName("INDIRIZZO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("UIND.IND");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Civico Catastale");
		elementoFiltro.setAttributeName("CIVICO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("UIND.CIV1");
		listaElementiFiltro.add(elementoFiltro);
		
		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"pertinenzeAbit/pertinenzeAbitFrame.jsp",nav);
	}
	
	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response)throws Exception{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		String paginaAttuale = "";
		if (request.getParameter("ACT_PAGE")!= null)
			paginaAttuale = request.getParameter("ACT_PAGE");
		String azione = "";
		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");

		if (sessione.getAttribute(NOMEFINDER)!= null){
			finder = (PertinenzeAbitFinder)sessione.getAttribute(NOMEFINDER);
		}
		else{
			finder = null;//new ImmobiliFinder();
		}
		finder = (PertinenzeAbitFinder)gestireMultiPagina(finder, request);
		
		Hashtable ht = new Hashtable();
		Vector vct_lista_pertinenze = new Vector();
		if (vettoreRicerca != null && vettoreRicerca.size()>0){
			boolean applica = false;
			EscElementoFiltro eef = null;
			for (int i=0; i<vettoreRicerca.size(); i++){
				eef = (EscElementoFiltro)vettoreRicerca.get(i);
				if (eef != null && !eef.getValore().equalsIgnoreCase(""))
					applica = true;
			}
			if (applica){
				PertinenzeAbitLogic logic = new PertinenzeAbitLogic(this.getEnvUtente(request));
				ht = logic.mCaricareListaPertinenze(vettoreRicerca, finder);
				vct_lista_pertinenze = (Vector)ht.get("LISTA_PERTINENZE");
				finder = (PertinenzeAbitFinder)ht.get("FINDER");
				
				sessione.setAttribute("LISTA_PERTINENZE",vct_lista_pertinenze);
				sessione.setAttribute(NOMEFINDER, finder);

				if(chiamataEsterna)
					nav.chiamataEsternaLista();
				else
					nav.chiamataInternaLista();

				this.chiamaPagina(request,response,"pertinenzeAbit/pertinenzeAbitFrame.jsp",nav);
			}else{
				// chiamare la pagina di ricerca
				nav.chiamataRicerca();
				this.st = 1;
				request.getSession().setAttribute("MSG", "Valorizzare almeno una voce");
				this.chiamaPagina(request,response,"pertinenzeAbit/pertinenzeAbitFrame.jsp",nav);
			}
		}
	}
	
	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response,boolean notListaPrincipale)throws Exception{
		// prendere dalla request il parametrio identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio

		String azione = "";
		HttpSession sessione = request.getSession();
		PertinenzeAbitFinder finder = null;
		if (sessione.getAttribute(NOMEFINDER) !=null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new PertinenzeAbitFinder().getClass()){
				finder = (PertinenzeAbitFinder)sessione.getAttribute(NOMEFINDER);
			}
			else{
				// il finder non corrisponde -->
				finder = null;
			}
		}


		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");

		gestioneMultiRecord(request, response, azione, finder, sessione, "LISTA_PERTINENZE", (Vector)sessione.getAttribute("LISTA_PERTINENZE"), NOMEFINDER);
		if (azione.equals("")){
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
		 * carica il dettaglio della pertinenza
		 */
		//mCaricareIntestatari(request,response,oggettoSel,notListaPrincipale);
		PertinenzeAbitLogic logic = new PertinenzeAbitLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioPertinenze(oggettoSel);
		ArrayList<DatiCatastali> alDc = (ArrayList<DatiCatastali>)ht.get(PertinenzeAbitLogic.LISTA_DETTAGLIO_DATI_CATASTALI);
		ArrayList<DatiTitolarita> alTit = (ArrayList<DatiTitolarita>)ht.get(PertinenzeAbitLogic.LISTA_DETTAGLIO_TITOLARITA);
		ArrayList<DatiTitolarita> alAlt = (ArrayList<DatiTitolarita>)ht.get(PertinenzeAbitLogic.LISTA_DETTAGLIO_ALTRE_TITOLARITA);
		ArrayList<PertinenzeAbitTitolare> alPat = (ArrayList<PertinenzeAbitTitolare>)ht.get(PertinenzeAbitLogic.LISTA_DETTAGLIO_TITOLARE);
		ArrayList<String> alCivicoInpertinenza = (ArrayList<String>)ht.get(PertinenzeAbitLogic.CIVICO_IN_PERTINENZA_CARTOGRAFICA);
		ArrayList<PertinenzeAbitDatiCatastali> alUiRes = (ArrayList<PertinenzeAbitDatiCatastali>)ht.get(PertinenzeAbitLogic.UI_RESIDENZIALE_IN_PERTINENZA);
		ArrayList<PertinenzeAbitDatiCatastali> alDiCat = (ArrayList<PertinenzeAbitDatiCatastali>)ht.get(PertinenzeAbitLogic.UI_DI_CATEGORIA_IN_PERTINENZA);
		
		sessione.setAttribute(PertinenzeAbitLogic.LISTA_DETTAGLIO_DATI_CATASTALI, alDc);
		sessione.setAttribute(PertinenzeAbitLogic.LISTA_DETTAGLIO_TITOLARITA, alTit);
		sessione.setAttribute(PertinenzeAbitLogic.LISTA_DETTAGLIO_ALTRE_TITOLARITA, alAlt);
		sessione.setAttribute(PertinenzeAbitLogic.LISTA_DETTAGLIO_TITOLARE, alPat);
		sessione.setAttribute(PertinenzeAbitLogic.CIVICO_IN_PERTINENZA_CARTOGRAFICA, alCivicoInpertinenza);
		sessione.setAttribute(PertinenzeAbitLogic.UI_RESIDENZIALE_IN_PERTINENZA, alUiRes);
		sessione.setAttribute(PertinenzeAbitLogic.UI_DI_CATEGORIA_IN_PERTINENZA, alDiCat);
		
		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		this.chiamaPagina(request,response,"pertinenzeAbit/pertinenzeAbitFrame.jsp",nav);
	}
	/*
	private void mCaricareIntestatari(HttpServletRequest request,HttpServletResponse response, String chiave, boolean notListaPrincipale) throws Exception{
		/*
		 * carico gli intestatari (persone fisiche e giuridiche)
		 */
	/*	HttpSession sessione = request.getSession();

		CatastoIntestatariFLogic logicF =new CatastoIntestatariFLogic(this.getEnvUtente());
		Vector listaIntF = (Vector)((Hashtable)logicF.mCaricareListaIntestatariPerFabbricato(chiave)).get("LISTA_INTESTATARIF");

		CatastoIntestatariGLogic logicG =new CatastoIntestatariGLogic(this.getEnvUtente());
		Vector listaIntG = (Vector)((Hashtable)logicG.mCaricareListaIntestatariPerFabbricato(chiave)).get("LISTA_INTESTATARIG");
		if (notListaPrincipale){
			sessione.setAttribute("LISTA_INTESTATARIF2",listaIntF);
			sessione.setAttribute("LISTA_INTESTATARIG2",listaIntG);
		}
		else{
			sessione.setAttribute("LISTA_INTESTATARIF",listaIntF);
			sessione.setAttribute("LISTA_INTESTATARIG",listaIntG);
		}

	}
	*/
	
	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
		return ((PertinenzeAbit)listaOggetti.get(recordSuccessivo)).getChiave();
	}
	
	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (PertinenzeAbitFinder)finder2;
		PertinenzeAbitLogic logic = new PertinenzeAbitLogic(this.getEnvUtente(request));
		//return logic.mCaricareListaImmobili2(this.vettoreRicerca, finder);
		return null;
	}
	
	public EscFinder getNewFinder(){
		return new PertinenzeAbitFinder();
	}
	
	public String getTema() {
		return "PertinenzeAbit";
	}
	
	public String getTabellaPerCrossLink() {
		return "SITIUIU";
	}

	
}//-----------------------------------------------------------------------------
