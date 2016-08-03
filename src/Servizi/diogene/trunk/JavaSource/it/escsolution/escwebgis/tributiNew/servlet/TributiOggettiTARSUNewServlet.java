package it.escsolution.escwebgis.tributiNew.servlet;

import it.escsolution.escwebgis.catasto.logic.CatastoPlanimetrieComma340Logic;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;

import it.escsolution.escwebgis.tributiNew.bean.*;
import it.escsolution.escwebgis.tributiNew.logic.*;
import it.webred.indice.OggettoIndice;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.ArrayList;

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

public class TributiOggettiTARSUNewServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private OggettiTARSUNewFinder finder = null;

	private final String NOMEFINDER = "FINDER109";
	
	private String localDataSource = "jdbc/Diogene_DS";
	
	String pathPlanimetrieComma340 = "";
	
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
        pathPlanimetrieComma340 = config.getInitParameter("pathPlanimetrieComma340");
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
		String ext= request.getParameter("IND_EXT");
		
		if (ext!= null && ext.equals("1"))
			EseguiServizioExt(request,response);
		else
			_EseguiServizio(request,response);
		
	}

	public void _EseguiServizio(
			HttpServletRequest request,HttpServletResponse response)
					throws ServletException, IOException {
		/*
		 * ad ogni uc corrisponde una funzionalità diversa
		 *
		 */
		super.EseguiServizio(request,response);
		
		try {
			if (request.getParameter("listavie") != null)
				mListaVie(request, response, "SIT_T_TAR_VIA", null, "DESCRIZIONE", "VIA");
			else if (request.getParameter("listaclassi") != null)
				mListaClassi(request, response, "DES_CLS_RSU");
			else  {
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
						mCaricareDettaglio(request,response,false);
						//this.leggiCrossLink(request);
						break;
					case 5:
						// ho cliccato su un elemento --> visualizzo il dettaglio
						mCaricareDettaglio(request,response,false);
						//this.leggiCrossLink(request);
						break;
					case 33:
						// ho cliccato su un elemento --> visualizzo il dettaglio
						mCaricareDettaglio(request,response,false);
						//this.leggiCrossLink(request);
						break;
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
	
	public void EseguiServizioExt(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		super.EseguiServizio(request,response);
		try
		{
			
				switch (st){
				case 102:
					// carico la form di ricerca
					pulireSessione(request);
					//mCaricareLista(request,response, 102);
					mCaricareDettaglio(request,response,false);
					break;
				case 103:
					// carico la form di ricerca
					pulireSessione(request);
					//mCaricareLista(request,response, 103);
					mCaricareDettaglio(request,response,false);
					break;
				case 104:
					// carico la form di ricerca
					pulireSessione(request);
					//mCaricareLista(request,response, 104);
					mCaricareDettaglio(request,response,false);
					break;
				case 105:
					// carico la form di ricerca
					pulireSessione(request);
					//mCaricareLista(request,response, 104);
					mCaricareDettaglio(request,response,false);
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
	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//caricare dati che servono nella pagina di ricerca --> eventuali combo
		
		HttpSession sessione = request.getSession();
		
		Vector listaElementiFiltro = new Vector();
		
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=","uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like","contiene"));

		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=","uguale"));
		
		// costruisce il vettore di elementi per la pagina di ricerca
		//titolo gruppo
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("DATI OGGETTO");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Classe");
		elementoFiltro.setAttributeName("DES_CLS_RSU");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("SIT_T_TAR_OGGETTO.DES_CLS_RSU");
		elementoFiltro.setExtraHTML("<span><input type=\"button\" class=\"TXTmainLabel\" value=\"Cerca\" onclick=\"javascript:danRequest('"+it.escsolution.escwebgis.common.EscServlet.URL_PATH+"/TributiOggettiTARSUNew?listaclassi='+document.getElementById('DES_CLS_RSU').value,'divListaClassi')\"/></span><span id=\"divListaClassi\" class=\"txtRow\"  ></span> \n");		
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Foglio");
		elementoFiltro.setAttributeName("FOGLIO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("SIT_T_TAR_OGGETTO.FOGLIO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Particella");
		elementoFiltro.setAttributeName("NUMERO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("SIT_T_TAR_OGGETTO.NUMERO");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Subalterno");
		elementoFiltro.setAttributeName("SUB");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("SIT_T_TAR_OGGETTO.SUB");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Via");
		elementoFiltro.setAttributeName("VIA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("DECODE(SIT_T_TAR_VIA.DESCRIZIONE, NULL, SIT_T_TAR_OGGETTO.DES_IND, (DECODE(SIT_T_TAR_VIA.PREFISSO, NULL, '', '', '', SIT_T_TAR_VIA.PREFISSO || ' ') || SIT_T_TAR_VIA.DESCRIZIONE))");
		elementoFiltro.setExtraHTML("<span><input type=\"button\" class=\"TXTmainLabel\" value=\"Cerca\" onclick=\"javascript:danRequest('"+it.escsolution.escwebgis.common.EscServlet.URL_PATH+"/TributiOggettiTARSUNew?listavie='+document.getElementById('VIA').value,'divListaVie')\"/></span><span id=\"divListaVie\" class=\"txtRow\"  ></span> \n");		
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Numero Civico");
		elementoFiltro.setAttributeName("NUM_CIV");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("SIT_T_TAR_OGGETTO.NUM_CIV");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Solo attuali");
		elementoFiltro.setAttributeName("SOLO_ATT");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(null);
		elementoFiltro.setCheckBox(true);
		elementoFiltro.setCampoFiltrato("SOLO_ATT");
		listaElementiFiltro.add(elementoFiltro);
		
		//titolo gruppo
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("DATI SOGGETTO");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Codice Fiscale");
		elementoFiltro.setAttributeName("COD_FISC");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("v.COD_FISC");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cognome");
		elementoFiltro.setAttributeName("COG_DENOM");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("v.COG_DENOM");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Nome");
		elementoFiltro.setAttributeName("NOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("v.NOME");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Denominazione");
		elementoFiltro.setAttributeName("COG_DENOM");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("v.COG_DENOM");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Partita Iva");
		elementoFiltro.setAttributeName("PART_IVA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("v.PART_IVA");
		listaElementiFiltro.add(elementoFiltro);
		
		Vector<TitoloSoggetto> vctTitoliSoggetto = new Vector<TitoloSoggetto>();
		//lista fissa
		vctTitoliSoggetto.add(new TitoloSoggetto("CNT", "Contribuente"));
		vctTitoliSoggetto.add(new TitoloSoggetto("DIC", "Dichiarante"));
		vctTitoliSoggetto.add(new TitoloSoggetto("ULT", "Ulteriori Soggetti"));
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Titolo Soggetto");
		elementoFiltro.setAttributeName("TIT_SOGG");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(null);
		elementoFiltro.setListaValori(vctTitoliSoggetto);
		elementoFiltro.setCampoFiltrato("TIT_SOGG");
		elementoFiltro.setComboSize(vctTitoliSoggetto.size());
		elementoFiltro.setCheckList(true);
		listaElementiFiltro.add(elementoFiltro);

		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		
		// chiamare la pagina di ricerca
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"tributiNew/oggTARSUNewFrame.jsp", nav);

	}

	/**
	 * @param request
	 * @param response
	 */
	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response, int tipo) throws Exception {
		
		//prendere dalla request i parametri di ricerca impostati dall'utente
		 HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null){
			finder = (OggettiTARSUNewFinder)sessione.getAttribute(NOMEFINDER);
		}
		else{
			finder = null;
		}

		 finder = (OggettiTARSUNewFinder)gestireMultiPagina(finder,request);

		 TributiOggettiTARSUNewLogic logic = new TributiOggettiTARSUNewLogic(this.getEnvUtente(request));
		 Hashtable ht = null;
		 
		/* if (tipo == 103)
			 ht = logic.mCaricareListaOggettiTARSUCiv(request.getParameter("OGGETTO_SEL"),finder);
		 else if (tipo == 102)
			 ht = logic.mCaricareListaOggettiTARSUFromVia(request.getParameter("OGGETTO_SEL"),finder);
		 else if (tipo == 104)
			 ht = logic.mCaricareListaOggettiTARSUFromOggetto(request.getParameter("OGGETTO_SEL"),finder);
		 else
		 */
		   ht = logic.mCaricareListaOggettiTARSU(vettoreRicerca, finder);
		

		 // eseguire la query e caricare il vettore con i risultati
		 // chiamare la pagina di lista

		 Vector vct_lista_TARSU= (Vector)ht.get("LISTA_TARSU");
		 finder = (OggettiTARSUNewFinder)ht.get("FINDER");
		 Boolean soloAtt = (Boolean)ht.get(TributiOggettiTARSUNewLogic.SOLO_ATT);

		 sessione.setAttribute("LISTA_TARSU",vct_lista_TARSU);
		 sessione.setAttribute(NOMEFINDER, finder);
		 sessione.setAttribute(TributiOggettiTARSUNewLogic.SOLO_ATT, soloAtt);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();

		this.chiamaPagina(request,response,"tributiNew/oggTARSUNewFrame.jsp", nav);

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
			
			removeOggettiIndiceDaSessione(sessione);
			 
			//boolean chiamataEsterna = false;
			if (sessione.getAttribute(NOMEFINDER) !=null){
				if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new OggettiTARSUNewFinder().getClass()){
					finder = (OggettiTARSUNewFinder)sessione.getAttribute(NOMEFINDER);
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
			}else{
				oggettoSel = "";
				recordScelto = "";
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
		TributiOggettiTARSUNewLogic logic = new TributiOggettiTARSUNewLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioOggettiTARSU(oggettoSel, pathPlanimetrieComma340);

		/*
		 * carico la lista degli intestatari associati
		 */
		OggettiTARSUNew tarsu = (OggettiTARSUNew)ht.get("TARSU");
		if (tarsu!= null)
			if (tarsu.getChiave()!=null)
				mCaricareContribuenti(request, response, tarsu.getChiave());
		sessione.setAttribute("TARSU",tarsu);
		if(ht.get("TARSU_DETTAGLIO_VANI") != null)
			sessione.setAttribute("TARSU_DETTAGLIO_VANI",ht.get("TARSU_DETTAGLIO_VANI"));
		if(ht.get(TributiOggettiTARSUNewLogic.TARSU_DOCFA_COLLEGATI) != null)
			sessione.setAttribute(TributiOggettiTARSUNewLogic.TARSU_DOCFA_COLLEGATI,ht.get(TributiOggettiTARSUNewLogic.TARSU_DOCFA_COLLEGATI));
		if(ht.get("SOGGETTI_ASSOCIATI_TARSU") != null)
			sessione.setAttribute("SOGGETTI_ASSOCIATI_TARSU",ht.get("SOGGETTI_ASSOCIATI_TARSU"));
		ArrayList soggettiAssociati = (ArrayList)ht.get("SOGGETTI_ASSOCIATI_TARSU");
		
		CatastoPlanimetrieComma340Logic logic340 = new CatastoPlanimetrieComma340Logic(this.getEnvUtente(request));
		ArrayList<ArrayList<String>> planimetrieComma340 = logic340.getPlanimetrieComma340(tarsu.getFoglio(), tarsu.getNumero(), tarsu.getSub(), pathPlanimetrieComma340);
		request.getSession().setAttribute(CatastoPlanimetrieComma340Logic.PLANIMETRIE_COMMA_340_TARSU, planimetrieComma340);
		
		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		
Vector<OggettoIndice> sOggettiInd = new Vector<OggettoIndice>();

ArrayList listaNomiSoggetti= new ArrayList();
		
		for (int i=0; i< soggettiAssociati.size(); i++){
			
			SoggettiTributiNew contrSogg=(SoggettiTributiNew) soggettiAssociati.get(i);
			
		OggettoIndice oi = new OggettoIndice();
		
		oi.setIdOrig(contrSogg.getId());
		
			oi.setFonte("2");
			oi.setProgr("4");
		
		if ((contrSogg.getCogDenom()!= null && !"".equals(contrSogg.getCogDenom())) || (contrSogg.getNome()!= null && !"".equals(contrSogg.getNome())))
			oi.setDescrizione(contrSogg.getCogDenom() + " " + contrSogg.getNome());
		
		String nomeSoggetto= oi.getFonte()+oi.getProgr()+oi.getIdOrig();
			if (!listaNomiSoggetti.contains(nomeSoggetto)){
				listaNomiSoggetti.add(nomeSoggetto);
				sOggettiInd.add(oi);
			}
		}
	
		sessione.setAttribute("indice_soggetti", sOggettiInd);
		
		
Vector<OggettoIndice> viaInd = new Vector<OggettoIndice>();
		
		if (tarsu.getIdVia()!= null && !tarsu.getIdVia().equals("-")){
			
			OggettoIndice oi = new OggettoIndice();
			
			oi.setIdOrig(tarsu.getIdVia());
			
			oi.setFonte("2");
			oi.setProgr("3");
			
			oi.setDescrizione(tarsu.getDesInd());
			
			viaInd.add(oi);
		}
	
		
		sessione.setAttribute("indice_vie", viaInd);

		
		OggettoIndice oi = new OggettoIndice();
		oi.setDescrizione(tarsu.getDesInd());
		oi.setIdOrig(tarsu.getIdVia() +"|"+ (tarsu.getNumCiv() == null? "":tarsu.getNumCiv()) + "|" + (tarsu.getEspCiv() == null? "":tarsu.getEspCiv()));
		oi.setFonte("2");
		oi.setProgr("3");

		Vector listaInd= new Vector();
		listaInd.add(oi);
		sessione.setAttribute("indice_civici", listaInd);
		
		
		OggettoIndice oi1 = new OggettoIndice();
		oi1.setDescrizione("F:" + tarsu.getFoglio() + " P:" + tarsu.getNumero() + " S:" + tarsu.getSub());
		oi1.setIdOrig(tarsu.getChiave());
		oi1.setFonte("2");
		oi1.setProgr("3");

		Vector listaOgg= new Vector();
		listaOgg.add(oi1);
		sessione.setAttribute("indice_oggetti", listaOgg);
		
		OggettoIndice oi2 = new OggettoIndice();
		oi2.setDescrizione( "SEZ:" + (tarsu.getSez()!= null ? tarsu.getSez():"-") + " F:" +tarsu.getFoglio() + " P:" + tarsu.getNumero() );
		oi2.setIdOrig(tarsu.getChiave());
		oi2.setFonte("2");
		oi2.setProgr("3");

		Vector listaFab= new Vector();
		listaFab.add(oi2);
		sessione.setAttribute("indice_fabbricati", listaFab);
	
		this.chiamaPagina(request,response,"tributiNew/oggTARSUNewFrame.jsp", nav);
}	
	
	private void mCaricareContribuenti(HttpServletRequest request,HttpServletResponse response, String chiaveTARSU) throws Exception{
		/*
		 * carico la lista dei contribuenti
		 */
		
		/*
		
		TributiContribuentiLogic logic = new TributiContribuentiLogic(this.getEnvUtente());
		Contribuente cont = ((Contribuente)logic.mCaricareListaContribuentiPerOggettoTARSU(chiaveTARSU));


		HttpSession sessione = request.getSession();
		sessione.setAttribute("CONTRIBUENTE_TARSU",cont);
		*/
		
	}
	

	/**
	 * SERVE PER IL FILTRO , PER SUGGERIRE DELLE CLASSI DI OGGETTO TARSU
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void mListaClassi(HttpServletRequest request, HttpServletResponse response, String inputId) throws IOException
	
	{
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "No-cache");
		response.setHeader("Expires", "Thu, 01 Jan 1970 01:00:00 CET");
		if(request.getParameter("listaclassi") != null && request.getParameter("listaclassi").trim().equals(""))
		{
			response.getWriter().println("Nessun criterio di ricerca impostato");
			return;
		}
		TributiOggettiTARSUNewLogic logic = new TributiOggettiTARSUNewLogic(this.getEnvUtente(request));
		List l = logic.cercaClassiOggTARSU(request.getParameter("listaclassi"));
		if(l != null && l.size()>0)
		{
			StringBuffer s = new StringBuffer();
			s.append("<select name=\"listaClassi\" class=\"INPDBComboBox\" id=\"listaClassi\" onchange=\"document.getElementById('"+inputId+"').value=this.options[this.selectedIndex].value;document.getElementById('divListaClassi').innerHTML='';\" > \n");
			Iterator i = l.iterator();
			s.append("<option value=\"\" >Seleziona</option>");
			while(i.hasNext())
			{
				String value = i.next().toString();
				s.append("<option value=\""+value+"\">"+value+"</option> \n");
			}			
			s.append("</select> \n");			
			response.getWriter().print(s.toString());
		}
		else
		{
			response.getWriter().println("Nessun indirizzo trovato");
		}		
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
		return ((OggettiTARSUNew)listaOggetti.get(recordSuccessivo)).getChiave();
	}

	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (OggettiTARSUNewFinder)finder2;
		TributiOggettiTARSUNewLogic logic = new TributiOggettiTARSUNewLogic(this.getEnvUtente(request));
		return logic.mCaricareListaOggettiTARSU(this.vettoreRicerca,finder);
		
	}

	public EscFinder getNewFinder(){
		return new OggettiTARSUNewFinder();
	}

	public String getTema() {
		return "Tributi";
	}
	
	public String getTabellaPerCrossLink() {
		return "SIT_T_TAR_OGGETTO";
	}
	
	public String getLocalDataSource() {
		return localDataSource;
	}

}
