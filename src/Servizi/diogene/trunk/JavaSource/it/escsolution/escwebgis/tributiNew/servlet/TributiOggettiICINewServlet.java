package it.escsolution.escwebgis.tributiNew.servlet;

import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.tributiNew.bean.ContribuentiOggettoNew;
import it.escsolution.escwebgis.tributiNew.bean.OggettiICINew;
import it.escsolution.escwebgis.tributiNew.bean.OggettiICINewFinder;
import it.escsolution.escwebgis.tributiNew.bean.TitoloSoggetto;
import it.escsolution.escwebgis.tributiNew.logic.TributiOggettiICINewLogic;
import it.webred.indice.OggettoIndice;

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

public class TributiOggettiICINewServlet extends EscServlet implements EscService {

	PulsantiNavigazione nav = new PulsantiNavigazione();
	private String recordScelto;
	private OggettiICINewFinder finder = null;
	private final String NOMEFINDER = "FINDER108";
	
	private String localDataSource = "jdbc/Diogene_DS";

	public void EseguiServizio(
			HttpServletRequest request,HttpServletResponse response)
					throws ServletException, IOException {
		/*
		 * ad ogni uc corrisponde una funzionalità diversa
		 *
		 */
		super.EseguiServizio(request,response);
		
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		String ext= request.getParameter("IND_EXT");
		
		if (ext!= null && ext.equals("1"))
			EseguiServizioExt(request,response);
		else
			_EseguiServizio(request,response);
		
	}
    
	public void _EseguiServizio(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		
		try{
			
			if (request.getParameter("listavie") != null)
				mListaVie(request, response, "SIT_T_ICI_VIA", null, "DESCRIZIONE", "VIA");
			else{
			
				switch (st){
					case 1:
						// carico la form di ricerca
						pulireSessione(request);
						mCaricareFormRicerca(request,response);
						break;
					case 2:
						// vengo dalla pagina di ricerca --> predispongo la lista e la passo alla pagina di lista
						mCaricareLista(request,response, 2);
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
					pulireSessione(request);
					mCaricareDettaglio(request,response,false);
					
					break;
				case 105:
					pulireSessione(request);
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
		elementoFiltro.setLabel("Anno");
		elementoFiltro.setAttributeName("YEA_DEN");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("SIT_T_ICI_OGGETTO.YEA_DEN");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Foglio");
		elementoFiltro.setAttributeName("FOGLIO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("SIT_T_ICI_OGGETTO.FOGLIO");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Particella");
		elementoFiltro.setAttributeName("NUMERO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("SIT_T_ICI_OGGETTO.NUMERO");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Subalterno");
		elementoFiltro.setAttributeName("SUB");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("SIT_T_ICI_OGGETTO.SUB");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Categoria Catastale");
		elementoFiltro.setAttributeName("CAT");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("SIT_T_ICI_OGGETTO.CAT");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Classe");
		elementoFiltro.setAttributeName("CLS");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("SIT_T_ICI_OGGETTO.CLS");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Via");
		elementoFiltro.setAttributeName("VIA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("DECODE(SIT_T_ICI_VIA.DESCRIZIONE, NULL, SIT_T_ICI_OGGETTO.DES_IND, (DECODE(SIT_T_ICI_VIA.PREFISSO, NULL, '', '', '', SIT_T_ICI_VIA.PREFISSO || ' ') || SIT_T_ICI_VIA.DESCRIZIONE))");
		elementoFiltro.setExtraHTML("<span><input type=\"button\" class=\"TXTmainLabel\" value=\"Cerca\" onclick=\"javascript:danRequest('"+it.escsolution.escwebgis.common.EscServlet.URL_PATH+"/TributiOggettiICINew?listavie='+document.getElementById('VIA').value,'divListaVie')\"/></span><span id=\"divListaVie\" class=\"txtRow\"  ></span> \n");		
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Numero Civico");
		elementoFiltro.setAttributeName("NUM_CIV");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("SIT_T_ICI_OGGETTO.NUM_CIV");
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
		vctTitoliSoggetto.add(new TitoloSoggetto("CTT", "Contitolare"));
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
		this.chiamaPagina(request,response,"tributiNew/oggICINewFrame.jsp", nav);

	}

	/**
	 * @param request
	 * @param response
	 */
	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response, int tipo) throws Exception {
		
		if (request.getParameter("POPUP") != null && new Boolean(request.getParameter("POPUP")).booleanValue()) {
			//ricerca (in popup) per codice fiscale - anno
			mCaricareListaSoggetto(request,response);
			return;					
		}
		
		//prendere dalla request i parametri di ricerca impostati dall'utente
		 HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null){
			finder = (OggettiICINewFinder)sessione.getAttribute(NOMEFINDER);
		}
		else{
			finder = null;
		}

		 finder = (OggettiICINewFinder)gestireMultiPagina(finder,request);

		 TributiOggettiICINewLogic logic = new TributiOggettiICINewLogic(this.getEnvUtente(request));
		 Hashtable ht = null;
		
		 if (tipo == 103)
			 ht = logic.mCaricareListaOggettiICICiv(request.getParameter("OGGETTO_SEL"),finder);
		 else if (tipo == 102)
			 ht = logic.mCaricareListaOggettiICIVia(request.getParameter("OGGETTO_SEL"), finder);
		 else if (tipo == 104)
			 ht = logic.mCaricareListaOggettiICIOggetto(request.getParameter("OGGETTO_SEL"), finder);
		 else
		   ht = logic.mCaricareListaOggettiICI(vettoreRicerca, finder);

		 // eseguire la query e caricare il vettore con i risultati
		 // chiamare la pagina di lista

		 Vector vct_lista_ICI= (Vector)ht.get("LISTA_ICI");
		 finder = (OggettiICINewFinder)ht.get("FINDER");
		 Boolean soloAtt = (Boolean)ht.get(TributiOggettiICINewLogic.SOLO_ATT);

		 sessione.setAttribute("LISTA_ICI",vct_lista_ICI);
		 sessione.setAttribute(NOMEFINDER, finder);
		 sessione.setAttribute(TributiOggettiICINewLogic.SOLO_ATT, soloAtt);

		if(chiamataEsterna)
			nav.chiamataEsternaLista();
		else
			nav.chiamataInternaLista();

		this.chiamaPagina(request,response,"tributiNew/oggICINewFrame.jsp", nav);

	}

	/**
	 * @param request
	 * @param response
	 */
	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response, boolean soloDettaglio) throws Exception {
		// prendere dalla request il parametro identificativo dell'oggetto cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio

		String azione = "";
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
		
		//boolean chiamataEsterna = false;
		if (sessione.getAttribute(NOMEFINDER) !=null){
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new OggettiICINewFinder().getClass()){
				finder = (OggettiICINewFinder)sessione.getAttribute(NOMEFINDER);
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
		TributiOggettiICINewLogic logic = new TributiOggettiICINewLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioOggettiICI(oggettoSel);

		OggettiICINew ici = (OggettiICINew)ht.get("ICI");
		ArrayList contrOggList = (ArrayList)ht.get("CONTR_LIST");
		
		sessione.setAttribute("ICI",ici);
		sessione.setAttribute("CONTR_LIST",contrOggList);
		sessione.setAttribute("SOLO_DETTAGLIO",new Boolean(soloDettaglio));
		
		if(ht.get(TributiOggettiICINewLogic.ICI_DOCFA_COLLEGATI) != null && ((List)ht.get(TributiOggettiICINewLogic.ICI_DOCFA_COLLEGATI)).size() > 0)
		{
			sessione.setAttribute(TributiOggettiICINewLogic.ICI_DOCFA_COLLEGATI, ht.get(TributiOggettiICINewLogic.ICI_DOCFA_COLLEGATI));
		}
		else
		{	
			sessione.removeAttribute(TributiOggettiICINewLogic.ICI_DOCFA_COLLEGATI);
		}
		
		if(chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		
		Vector<OggettoIndice> sOggettiInd = new Vector<OggettoIndice>();
		
		ArrayList listaNomiSoggetti= new ArrayList();
		
		for (int i=0; i< contrOggList.size(); i++){
			
			ContribuentiOggettoNew contrSogg=(ContribuentiOggettoNew) contrOggList.get(i);
			
			OggettoIndice oi = new OggettoIndice();
			
			oi.setIdOrig(contrSogg.getIdSogg());
			
			oi.setFonte("2");
			oi.setProgr("1");
			
			if ((contrSogg.getCognome()!= null && !"".equals(contrSogg.getCognome())) || (contrSogg.getNome()!= null && !"".equals(contrSogg.getNome())))
				oi.setDescrizione(contrSogg.getCognome() + " " + contrSogg.getNome());
			
			String nomeSoggetto= oi.getFonte()+oi.getProgr()+oi.getIdOrig();
			if (!listaNomiSoggetti.contains(nomeSoggetto)){
				listaNomiSoggetti.add(nomeSoggetto);
				sOggettiInd.add(oi);
			}
			
			
		}
		Vector<OggettoIndice> viaInd = new Vector<OggettoIndice>();
		
		if (ici.getIdVia()!= null && !ici.getIdVia().equals("-")){
			
			OggettoIndice oi = new OggettoIndice();
			
			oi.setIdOrig(ici.getIdVia());
			
			oi.setFonte("2");
			oi.setProgr("2");
			
			oi.setDescrizione(ici.getDesInd());
			
			viaInd.add(oi);
		}
	
		sessione.setAttribute("indice_soggetti", sOggettiInd);
		sessione.setAttribute("indice_vie", viaInd);

		OggettoIndice oi = new OggettoIndice();
		oi.setDescrizione(ici.getDesInd());
		oi.setIdOrig(ici.getIdVia() + "|" + (ici.getNumCiv() == null? "":ici.getNumCiv()) + "|" + (ici.getEspCiv() == null? "":ici.getEspCiv()));
		oi.setFonte("2");
		oi.setProgr("2");

		Vector listaInd= new Vector();
		listaInd.add(oi);
		sessione.setAttribute("indice_civici", listaInd);
		
		
		OggettoIndice oi1 = new OggettoIndice();
		oi1.setDescrizione("F:" + ici.getFoglio() + " P:" + ici.getNumero() + " S:" + ici.getSub());
		oi1.setIdOrig(ici.getChiave());
		oi1.setFonte("2");
		oi1.setProgr("2");

		Vector listaOgg= new Vector();
		listaOgg.add(oi1);
		sessione.setAttribute("indice_oggetti", listaOgg);
		
		OggettoIndice oi2 = new OggettoIndice();
		oi2.setDescrizione( "SEZ:" + (ici.getSez()!= null ? ici.getSez():"-") + " F:" + ici.getFoglio() + " P:" + ici.getNumero() );
		oi2.setIdOrig(ici.getChiave());
		oi2.setFonte("2");
		oi2.setProgr("2");

		Vector listaFab= new Vector();
		listaFab.add(oi2);
		sessione.setAttribute("indice_fabbricati", listaFab);
		
		this.chiamaPagina(request,response,"tributiNew/oggICINewFrame.jsp", nav);		
			
	}
	
	private void mCaricareListaSoggetto(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		 HttpSession sessione = request.getSession();

		 TributiOggettiICINewLogic logic = new TributiOggettiICINewLogic(this.getEnvUtente(request));
		 String codFiscaleDic = (request.getParameter("CODICE_FISCALE_DIC"));
		 int annoImposta = Integer.parseInt(request.getParameter("ANNO_IMPOSTA"));

		 Vector vct_lista_ICI = logic.mCaricareListaOggettiICISoggetto(codFiscaleDic, annoImposta);
		 sessione.setAttribute("LISTA_ICI_POPUP",vct_lista_ICI);

		 if(chiamataEsterna)
			 nav.chiamataEsternaLista();
		 else
			 nav.chiamataInternaLista();

		 this.chiamaPagina(request,response,"tributiNew/oggICINewPopupSoggetto.jsp", nav);
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo){
		return ((OggettiICINew)listaOggetti.get(recordSuccessivo)).getChiave();
	}

	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (OggettiICINewFinder)finder2;
		TributiOggettiICINewLogic logic = new TributiOggettiICINewLogic(this.getEnvUtente(request));
		return logic.mCaricareListaOggettiICI(this.vettoreRicerca, finder);
	}

	public EscFinder getNewFinder(){
		return new OggettiICINewFinder();
	}

	public String getTema() {
		return "Tributi";
	}
	
	public String getTabellaPerCrossLink() {
		return "SIT_T_ICI_OGGETTO";
	}
	
	public String getLocalDataSource() {
		return localDataSource;
	}

}



