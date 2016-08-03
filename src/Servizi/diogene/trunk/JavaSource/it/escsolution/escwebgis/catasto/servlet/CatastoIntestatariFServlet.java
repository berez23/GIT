/*
 * Created on 9-apr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.escsolution.escwebgis.catasto.servlet;


import it.escsolution.escwebgis.catasto.bean.IntestatarioF;
import it.escsolution.escwebgis.catasto.bean.IntestatarioFFinder;
import it.escsolution.escwebgis.catasto.logic.CatastoImmobiliLogic;
import it.escsolution.escwebgis.catasto.logic.CatastoIntestatariFLogic;
import it.escsolution.escwebgis.catasto.logic.CatastoTerreniLogic;
import it.escsolution.escwebgis.catasto.logic.VisuraNazionaleLogic;
import it.escsolution.escwebgis.common.ComuniLogic;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.webred.GlobalParameters;
import it.webred.indice.OggettoIndice;

import java.io.IOException;
import java.util.ArrayList;
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
public class CatastoIntestatariFServlet extends EscServlet implements EscService {


	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private CatastoIntestatariFLogic logic = null;
	private IntestatarioFFinder finder = null;
	private final String NOMEFINDER = "FINDER3";
	
	public void EseguiServizio(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		super.EseguiServizio(request,response);
		
		String ext= request.getParameter("IND_EXT");
		
		if (ext!= null && ext.equals("1"))
			_EseguiServizioExt(request,response);
		else
			_EseguiServizio(request,response);
		
	}

	public void _EseguiServizio(
		HttpServletRequest request,
		HttpServletResponse response)
		throws ServletException, IOException {

		/*
		 * ad ogni uc corrisponde una funzionalitòà diversa
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
				mCaricareLista(request,response,2);
				break;
			case 3:
				// ho cliccato su un elemento --> visualizzo il dettaglio
				mCaricareDettaglio(request,response,false,3);
				//this.leggiCrossLink(request);
				break;
			case 4:
				// ho cliccato su un elemento --> visualizzo il dettaglio
				mCaricareParticelle(request,response);
				break;
			case 5:
				mCaricarePopupTitolarita(request,response,false);
				break;
			case 33:
				// ho cliccato su un elemento --> visualizzo il dettaglio
				mCaricareDettaglio(request,response,false,33);
				//this.leggiCrossLink(request);
				break;

		}
		
		}
		 catch(it.escsolution.escwebgis.common.DiogeneException de)
		 {
		 	throw de;
		 }
		catch(Exception exx){
			log.error(exx.getMessage(),exx);
		}


	}
	
	public void _EseguiServizioExt(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		super.EseguiServizio(request,response);
		try
		{
			
				switch (st){
					case 101:
						//mCaricareDettaglio(request,response,false,101);	
						mCaricareLista(request,response,101);
						break;
					
				}
				
		}
		catch(it.escsolution.escwebgis.common.DiogeneException de)
		{
			throw de;
		}
		catch(Exception ex)
		{
			throw new it.escsolution.escwebgis.common.DiogeneException(ex);
		}
	}
	
	/**
	 * @param request
	 * @param response
	 */
	private void mCaricarePopupTitolarita(HttpServletRequest request,HttpServletResponse response, boolean tipo)throws Exception{
		
	String codFiscale= request.getParameter("COD_FISCALE");
	String nome= request.getParameter("NOME");
	String cognome= request.getParameter("COGNOME");
	String dataNascita= request.getParameter("DATA_NASCITA");
	
	CatastoIntestatariFLogic logic = new CatastoIntestatariFLogic(this.getEnvUtente(request));
	
	//ricavo l'id dell'intestatario in base ai dati
	
	String oggettoSel= logic.mTrovaIntestatario(codFiscale, nome, cognome, dataNascita);
	
	/*
	 * carica il dettaglio dell'intestatario
	 */
	
	Hashtable ht = logic.mCaricareDettaglioIntestatarioF(oggettoSel);
	
	ArrayList intsF = (ArrayList)ht.get("INTESTATARIOF_STO");
	IntestatarioF intF = (IntestatarioF)ht.get("INTESTATARIOF");

	// LEGGE E METTE IN SESSIONE I SOGGETTI SCARTATI
	super.leggiScarti(request.getSession(), intF, "CONS_SOGG_TAB", null, "SOGGETTI CATASTO", request);

	request.setAttribute("INTESTATARIOF_STO", intsF);
	request.setAttribute("INTESTATARIOF", intF);
	request.setAttribute("TIPO",new Boolean(tipo));

//TEST MB
	mCaricareDetParticellePopup(request,response);
//FINE TEST MB

	

	this.chiamaPagina(request,response,"tributi/popupTitolaritaF.jsp", nav);
		
	}


	private void mCaricareFormRicerca(HttpServletRequest request,HttpServletResponse response) throws Exception{

			// caricare dati che servono nella pagina di ricerca --> eventuali combo
			HttpSession sessione = request.getSession();

			/** Comune */
			Vector vctComuni = new ComuniLogic(this.getEnvUtente(request)).getListaComuniUtente(request.getUserPrincipal().getName());
			/***/


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
			elementoFiltro.setListaOperatori(operatoriStringa);
			elementoFiltro.setListaValori(vctComuni);
			elementoFiltro.setCampoFiltrato("siticomu.CODI_FISC_LUNA (+)");
			listaElementiFiltro.add(elementoFiltro);


			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Cognome");
			elementoFiltro.setAttributeName("COGNOME");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringa);
			elementoFiltro.setCampoFiltrato("cons_sogg_tab.RAGI_SOCI");
			listaElementiFiltro.add(elementoFiltro);

			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Nome");
			elementoFiltro.setAttributeName("NOME");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringa);
			elementoFiltro.setCampoFiltrato("cons_sogg_tab.NOME");
			listaElementiFiltro.add(elementoFiltro);

			elementoFiltro = new EscElementoFiltro();
			elementoFiltro.setLabel("Codice Fiscale");
			elementoFiltro.setAttributeName("CODICE_FISCALE");
			elementoFiltro.setTipo("S");
			elementoFiltro.setCampoJS("");
			elementoFiltro.setListaOperatori(operatoriStringa);
			elementoFiltro.setCampoFiltrato("cons_sogg_tab.CODI_FISC");
			listaElementiFiltro.add(elementoFiltro);



			sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
			sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
			
			// chiamare la pagina di ricerca
			nav.chiamataRicerca();
			
			this.chiamaPagina(request,response,"catasto/intFisFrame.jsp",nav);
		}


	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response, boolean tipo, int st )throws Exception{
		// prendere dalla request il parametro identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio

			String azione = "";
			HttpSession sessione = request.getSession();
			removeOggettiIndiceDaSessione(sessione);
			
			 			
			IntestatarioFFinder finder = null;
			
			if (st == 101){

				finder = (IntestatarioFFinder)gestireMultiPagina(finder,request);
				sessione.setAttribute(NOMEFINDER,finder);
			}

			
			if (sessione.getAttribute(NOMEFINDER) !=null){
				if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new IntestatarioFFinder().getClass()){
					finder = (IntestatarioFFinder)sessione.getAttribute(NOMEFINDER);
				}
				else{
					// il finder non corrisponde -->
					finder = null;
				}
			}
			
			
		if (request.getParameter("AZIONE")!= null)
				azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request,response,azione,finder,sessione,"LISTA_INTESTATARIF",(Vector)sessione.getAttribute("LISTA_INTESTATARIF"),NOMEFINDER);
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
		 * carica il dettaglio dell'intestatario
		 */
		CatastoIntestatariFLogic logic = new CatastoIntestatariFLogic(this.getEnvUtente(request));
		VisuraNazionaleLogic visLogic = new VisuraNazionaleLogic(this.getEnvUtente(request));
		
		Hashtable ht = null;
		
		
		
		// Recupero il parametro EXT per verificare se viene da un crossLink FA
		String ext = request.getParameter("EXT");
		
		/*if (st == 101){
			 ht = logic.mCaricareDettaglioFromSoggetto(oggettoSel);
		}
		else*/
			ht = logic.mCaricareDettaglioIntestatarioF(oggettoSel);
		
		ArrayList intsF = (ArrayList)ht.get("INTESTATARIOF_STO");
		IntestatarioF intF = (IntestatarioF)ht.get("INTESTATARIOF");

		// LEGGE E METTE IN SESSIONE I SOGGETTI SCARTATI
		super.leggiScarti(request.getSession(), intF, "CONS_SOGG_TAB", null, "SOGGETTI CATASTO", request);

		sessione.setAttribute("INTESTATARIOF_STO", intsF);
		sessione.setAttribute("INTESTATARIOF", intF);
		sessione.setAttribute("TIPO",new Boolean(tipo));

//TEST MB
		mCaricareDetParticelle(request,response);
//FINE TEST MB
		

			// Aggiungo i valori per l'indice di correlazione
			Vector<OggettoIndice> sOggettiInd = new Vector<OggettoIndice>();
			
			OggettoIndice oi = new OggettoIndice();
			
			oi.setIdOrig(intF.getPkId());
			oi.setFonte("4");
			oi.setProgr("3");
			
			if ((intF.getCognome()!= null && !"".equals(intF.getCognome())) || (intF.getNome()!= null && !"".equals(intF.getNome())))
				oi.setDescrizione(intF.getCognome() + " " + intF.getNome());
			
			sOggettiInd.add(oi);
			
			
			log.debug("lista soggetti: " + sOggettiInd.size());
			sessione.setAttribute("indice_soggetti", sOggettiInd);
		

		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		this.chiamaPagina(request,response,"catasto/intFisFrame.jsp", nav);

	}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response, int tipo)throws Exception{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();
		
		String idSoggetto="";
		if (request.getParameter("OGGETTO_SEL")!= null)
			idSoggetto = request.getParameter("OGGETTO_SEL");


		if (sessione.getAttribute(NOMEFINDER)!= null){
				finder = (IntestatarioFFinder)sessione.getAttribute(NOMEFINDER);
		}
		else{
			finder = null;//new IntestatarioFFinder();
		}

		finder = (IntestatarioFFinder)gestireMultiPagina(finder,request);

		CatastoIntestatariFLogic logic = new CatastoIntestatariFLogic(this.getEnvUtente(request));
		
		Hashtable ht =null;
		if (tipo == 101)
			ht = logic.mCaricareListaIntestatariFromSoggetto(idSoggetto,finder);
		else
		 ht = logic.mCaricareListaIntestatariF(vettoreRicerca,finder);

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista


		Vector vct_lista_intestatari = (Vector)ht.get("LISTA_INTESTATARIF");
		finder = (IntestatarioFFinder)ht.get("FINDER");
//		TEST MB
		boolean saltaListaConUno = GlobalParameters.SALTA_LISTA_CON_UNO.get(this.getEnvUtente(request).getEnte()) == null ? GlobalParameters.SALTA_LISTA_CON_UNO_DEF : GlobalParameters.SALTA_LISTA_CON_UNO.get(this.getEnvUtente(request).getEnte()).booleanValue();
		if (saltaListaConUno && vct_lista_intestatari.size() == 1){
			/*
			 * carica il dettaglio dell'intestatario
			 */
			logic = new CatastoIntestatariFLogic(this.getEnvUtente(request));
			Hashtable htd = logic.mCaricareDettaglioIntestatarioF(((IntestatarioF)vct_lista_intestatari.get(0)).getCodIntestatario());
			
			ArrayList intsF = (ArrayList)htd.get("INTESTATARIOF_STO");
			IntestatarioF intF = (IntestatarioF)htd.get("INTESTATARIOF");

			// LEGGE E METTE IN SESSIONE I SOGGETTI SCARTATI
			super.leggiScarti(request.getSession(), intF, "CONS_SOGG_TAB", null, "SOGGETTI CATASTO", request);

			sessione.setAttribute("INTESTATARIOF_STO", intsF);
			sessione.setAttribute("INTESTATARIOF", intF);
			sessione.setAttribute("TIPO",new Boolean(false));

			mCaricareDetParticelle(request,response);

			if(chiamataEsterna)
				nav.chiamataEsternaDettaglio();
			else
				nav.chiamataInternaDettaglio();

			st = 33;

			this.chiamaPagina(request,response,"catasto/intFisFrame.jsp", nav);

			


		}else {
//FINE TEST MB

		sessione.setAttribute("LISTA_INTESTATARIF",vct_lista_intestatari);
		sessione.setAttribute(NOMEFINDER,finder);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();

		else
			nav.chiamataInternaLista();

		this.chiamaPagina(request,response,"catasto/intFisFrame.jsp", nav);

//		TEST MB
		}
//		FINE TEST MB

	}
	
	

	private void mCaricareDetParticelle(HttpServletRequest request,HttpServletResponse response)throws Exception{
		/*
		 * carica la lista di terreni e fabbricati dell'intestatario
		 */
		HttpSession sessione = request.getSession();
		 // immobili
		 CatastoImmobiliLogic logic = new CatastoImmobiliLogic(this.getEnvUtente(request));
		 IntestatarioF intF = (IntestatarioF)sessione.getAttribute("INTESTATARIOF");
		 Hashtable ht = logic.mCaricareListaImmobiliPerTitolarita(intF.getCodIntestatario());
		 Vector listaImmobili = (Vector)ht.get("LISTA_IMMOBILI");


		// terreni
		CatastoTerreniLogic logic2 = new CatastoTerreniLogic(this.getEnvUtente(request));
		Hashtable ht2 = logic2.mCaricareListaTerreniPerTitolarita(intF.getCodIntestatario());
		Vector listaTerreni = (Vector)ht2.get("LISTA_TERRENI");

		sessione.setAttribute("LISTA_IMMOBILI2",listaImmobili);
		sessione.setAttribute("LISTA_TERRENI2",listaTerreni);

	}
	
	
	private void mCaricareDetParticellePopup(HttpServletRequest request,HttpServletResponse response)throws Exception{
		/*
		 * carica la lista di terreni e fabbricati dell'intestatario
		 */
		
		 // immobili
		 CatastoImmobiliLogic logic = new CatastoImmobiliLogic(this.getEnvUtente(request));
		 IntestatarioF intF = (IntestatarioF)request.getAttribute("INTESTATARIOF");
		 Hashtable ht = logic.mCaricareListaImmobiliPerTitolarita(intF.getCodIntestatario());
		 Vector listaImmobili = (Vector)ht.get("LISTA_IMMOBILI");


		// terreni
		CatastoTerreniLogic logic2 = new CatastoTerreniLogic(this.getEnvUtente(request));
		Hashtable ht2 = logic2.mCaricareListaTerreniPerTitolarita(intF.getCodIntestatario());
		Vector listaTerreni = (Vector)ht2.get("LISTA_TERRENI");

		request.setAttribute("LISTA_IMMOBILI2",listaImmobili);
		request.setAttribute("LISTA_TERRENI2",listaTerreni);

	}

private void mCaricareParticelle(HttpServletRequest request,HttpServletResponse response)throws Exception{
	/*
	 * carica la lista di terreni e fabbricati dell'intestatario
	 */
	HttpSession sessione = request.getSession();
	 // immobili
	 CatastoImmobiliLogic logic = new CatastoImmobiliLogic(this.getEnvUtente(request));
	 IntestatarioF intF = (IntestatarioF)sessione.getAttribute("INTESTATARIOF");
	 Hashtable ht = logic.mCaricareListaImmobiliPerTitolarita(intF.getCodIntestatario());
	 Vector listaImmobili = (Vector)ht.get("LISTA_IMMOBILI");


	// terreni
	CatastoTerreniLogic logic2 = new CatastoTerreniLogic(this.getEnvUtente(request));
	Hashtable ht2 = logic2.mCaricareListaTerreniPerTitolarita(intF.getCodIntestatario());
	Vector listaTerreni = (Vector)ht2.get("LISTA_TERRENI");

	sessione.setAttribute("LISTA_IMMOBILI2",listaImmobili);
	sessione.setAttribute("LISTA_TERRENI2",listaTerreni);

	nav.chiamataPart();
	this.chiamaPagina(request,response,"catasto/intFisFrame.jsp", nav);

}
public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
	return ((IntestatarioF)listaOggetti.get(recordSuccessivo)).getCodIntestatario();
}
public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
	finder = (IntestatarioFFinder)finder2;
	CatastoIntestatariFLogic logic = new CatastoIntestatariFLogic(this.getEnvUtente(request));
	return logic.mCaricareListaIntestatariF(this.vettoreRicerca,finder);
}

public EscFinder getNewFinder(){
		return new IntestatarioFFinder();
	}

	public String getTema() {
		return "Catasto";
	}
	public String getTabellaPerCrossLink() {
		return "CONS_SOGG_TAB/*F*/";
	}
}
