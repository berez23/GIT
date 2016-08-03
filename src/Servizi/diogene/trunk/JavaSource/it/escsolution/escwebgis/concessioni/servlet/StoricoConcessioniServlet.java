package it.escsolution.escwebgis.concessioni.servlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.escsolution.escwebgis.common.DiogeneException;
import it.escsolution.escwebgis.common.EnvUtente;
import it.escsolution.escwebgis.common.EscElementoFiltro;
import it.escsolution.escwebgis.common.EscFinder;
import it.escsolution.escwebgis.common.EscOperatoreFiltro;
import it.escsolution.escwebgis.common.EscService;
import it.escsolution.escwebgis.common.EscServlet;
import it.escsolution.escwebgis.common.PulsantiNavigazione;
import it.escsolution.escwebgis.common.Tema;
import it.escsolution.escwebgis.common.TiffUtill;
import it.escsolution.escwebgis.concessioni.bean.StoricoConcessioni;
import it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniCatasto;
import it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniFinder;
import it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniIndirizzo;
import it.escsolution.escwebgis.concessioni.bean.StoricoConcessioniPersona;
import it.escsolution.escwebgis.concessioni.logic.ConcessioniINFORMLogic;
import it.escsolution.escwebgis.concessioni.logic.StoricoConcessioniLogic;
import it.escsolution.escwebgis.licenzeCommercioNew.bean.LicenzeCommercioAnagNew;
import it.escsolution.escwebgis.licenzeCommercioNew.logic.LicenzeCommercioNewLogic;
import it.webred.DecodificaPermessi;
import it.webred.indice.OggettoIndice;
import it.webred.cet.permission.CeTUser;
import it.webred.cet.permission.GestionePermessi;

public class StoricoConcessioniServlet extends EscServlet implements EscService {

	private String recordScelto;
	public static final String NOMEFINDER = "FINDER53";
	private StoricoConcessioniFinder finder = null;
	PulsantiNavigazione nav;
	
	private String localDataSource = "jdbc/Diogene_DS";

	
	String pathDatiDiogene = "";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        pathDatiDiogene = super.getPathDatiDiogene();
    }
    
    
    public void EseguiServizio(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		
    	
    	super.EseguiServizio(request,response);
		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		String ext= request.getParameter("IND_EXT");
		
		if (ext!= null && ext.equals("1"))
			EseguiServizioExt_(request,response);
		else
			EseguiServizio_(request,response);
		
	}
    
	public void EseguiServizio_(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		super.EseguiServizio(request,response);
		try
		{
			if (request.getParameter("listavie") != null) {
				mListaVie(request, response, "SIT_C_CONC_INDIRIZZI", null, "INDIRIZZO", "INDIRIZZO");				
			}else{
				switch (st){
					 case 1:
						 // carico la form di ricerca
						 pulireSessione(request);
						 mCaricareFormRicerca(request,response);
						 break;
					 case 2:
						 // vengo dalla pagina di ricerca --> predispongo la lista e la passo alla pagina di lista
						 if (request.getParameter("popup") != null && new Boolean(request.getParameter("popup")).booleanValue()) {
							 mCaricareListaPopup(request,response);
						 }else{							 
						     //pulireSessione(request);
							 mCaricareLista(request,response,2);							 
						 }
						 break;
					 case 3:
						 // ho cliccato su un elemento --> visualizzo il dettaglio						 
						 if (request.getParameter("popup") != null && new Boolean(request.getParameter("popup")).booleanValue()) {
							 mCaricareDettaglioPopup(request,response);
						 }else{
							 mCaricareDettaglio(request,response);
							 //this.leggiCrossLink(request);						 
						 }
						 break;
					 case 33:
						 // ho cliccato su un elemento --> visualizzo il dettaglio						 
							 mCaricareDettaglio(request,response);
							 //this.leggiCrossLink(request);						 
						 	 break;
					 case 99:
							mCaricareImmagine(request, response);
							break;
					 case 98:
						 	mDownloadFile(request, response);
						 	break;						 
				}
			}			
		}
		catch(it.escsolution.escwebgis.common.DiogeneException de)
		{
			throw de;
		}
		catch(Exception ex)
		{
			log.error(ex.getMessage(),ex);
		}
	}
	
	public void EseguiServizioExt_(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		super.EseguiServizio(request,response);
		try
		{
			
				switch (st){
					case 101:
						mCaricareListaFromSoggetto(request,response);			
						break;
					case 102:
						mCaricareLista(request,response,102);							 
						break;
					case 103:
						mCaricareLista(request,response,103);						 
						break;
					case 104:
						mCaricareLista(request,response,104);		
						break;
					case 105:
						mCaricareLista(request,response,105);
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

	
	private void mCaricareImmagine(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
   		if (!GestionePermessi.autorizzato(eu.getUtente(),  eu.getNomeIstanza(), DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, DecodificaPermessi.PERMESSO_SCARICA_PLANIMETRIE,true))
			throw new DiogeneException("Non si possiede il permesso: " + DecodificaPermessi.PERMESSO_SCARICA_PLANIMETRIE);
   		
   		boolean openJpg = request.getParameter("openJpg") != null && new Boolean(request.getParameter("openJpg")).booleanValue();

   		StoricoConcessioniLogic logic = new StoricoConcessioniLogic(this.getEnvUtente(request));
		String[] val =  logic.mCaricareDatoImg(request.getParameter("img"));
		
		String pathStoricoConcessioniEnte = this.pathDatiDiogene + File.separatorChar + logic.getDirStoricoConcessioniEnte();
		
		
		FileInputStream is = new FileInputStream(pathStoricoConcessioniEnte+"/"+val[0]+"/"+val[1]);
		List<ByteArrayOutputStream> jpgs = TiffUtill.tiffToJpeg(is, true, openJpg);
		//ByteArrayOutputStream baos =  TiffUtill.jpgsTopdf(jpgs, true, TiffUtill.FORMATO_DEF);
		ByteArrayOutputStream baos = null;
		if (openJpg) {
			baos = jpgs.get(0);
		} else {
			baos = TiffUtill.jpgsTopdf(jpgs, false, TiffUtill.FORMATO_DEF);
		}
		ByteArrayInputStream isByte = new ByteArrayInputStream(baos.toByteArray());
		byte[] b = new byte[1024];
		OutputStream out = response.getOutputStream();
		if (openJpg) {
			response.setHeader("Content-Disposition","attachment; filename=\""+val[1]+"" + "\""+".jpg");
			response.setContentType("image/jpeg");
		} else {
			response.setHeader("Content-Disposition","inline;attachment; filename=\""+val[1]+"" + "\""+".pdf");
			response.setContentType("application/pdf");
		}	
		while ( isByte.read( b ) != -1 )
        {
            out.write( b );
        }
		is.close();
	}
	
	private void mDownloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getParameter("PATH_FILE") != null) {
			String pathFile = request.getParameter("PATH_FILE");
			StoricoConcessioniLogic logic = new StoricoConcessioniLogic(this.getEnvUtente(request));
			logic.download(response, pathFile);
		}
	}
	
	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//caricare dati che servono nella pagina di ricerca
		HttpSession sessione = request.getSession();

		Vector listaElementiFiltro = new Vector();
		
		//lista tipi soggetto
		Vector vctTipiSogg = new StoricoConcessioniLogic(new EnvUtente(this.getEnvUtente(request).getUtente(), 
																		getLocalDataSource(),
																		this.getEnvUtente(request).getNomeApplicazione())).getListaTipiSoggetto();

		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=", "uguale"));
		operatoriStringaRid.add(new EscOperatoreFiltro("like", "contiene"));

		Vector operatoriStringaUguale = new Vector();
		operatoriStringaUguale.add(new EscOperatoreFiltro("=", "uguale"));
		
		Vector operatoriNumericiRid = new Vector();
		operatoriNumericiRid.add(new EscOperatoreFiltro("=","="));
		
		Vector<?> vctProvenienze = new StoricoConcessioniLogic(this.getEnvUtente(request)).getListaProvenienze();

		// costruisce il vettore di elementi per la pagina di ricerca
		//titolo gruppo
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("DATI CONCESSIONE");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		//provenienza
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Provenienza");
		elementoFiltro.setAttributeName("PROVENIENZA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaUguale);
		elementoFiltro.setListaValori(vctProvenienze);
		elementoFiltro.setCampoFiltrato("UPPER(SIT_C_CONCESSIONI.PROVENIENZA)");
		listaElementiFiltro.add(elementoFiltro);
		//numero concessione
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Concessione / Fascicolo");
		elementoFiltro.setAttributeName("CONCESSIONE_NUMERO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("CONCESSIONE_NUMERO");
		listaElementiFiltro.add(elementoFiltro);

		//anno concessione
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Anno");
		elementoFiltro.setAttributeName("PROGRESSIVO_ANNO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaUguale);
		elementoFiltro.setCampoFiltrato("PROGRESSIVO_ANNO");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Progressivo");
		elementoFiltro.setAttributeName("PROGRESSIVO_NUMERO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaUguale);
		elementoFiltro.setCampoFiltrato("PROGRESSIVO_NUMERO");
		listaElementiFiltro.add(elementoFiltro);
		
		
		//numero protocollo
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Numero protocollo");
		elementoFiltro.setAttributeName("PROTOCOLLO_NUMERO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaUguale);
		elementoFiltro.setCampoFiltrato("PROTOCOLLO_NUMERO");
		listaElementiFiltro.add(elementoFiltro);
		//anno protocollo
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Anno protocollo");
		elementoFiltro.setAttributeName("PROTOCOLLO_ANNO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaUguale);
		elementoFiltro.setCampoFiltrato("TO_CHAR(PROTOCOLLO_DATA, 'YYYY')");
		listaElementiFiltro.add(elementoFiltro);
		//titolo gruppo
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("DATI SOGGETTO");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		//titolo
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Titolo");
		elementoFiltro.setAttributeName("TITOLO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaUguale);
		elementoFiltro.setListaValori(vctTipiSogg);
		elementoFiltro.setCampoFiltrato("LTRIM(RTRIM(UPPER(SIT_C_CONC_PERSONA.TITOLO)))");
		listaElementiFiltro.add(elementoFiltro);
		//codice fiscale
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Codice Fiscale");
		elementoFiltro.setAttributeName("CODICE_FISCALE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaUguale);
		elementoFiltro.setCampoFiltrato("CODICE_FISCALE"); //campo su cui costruire una or
		listaElementiFiltro.add(elementoFiltro);
		//partita IVA
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Partita IVA");
		elementoFiltro.setAttributeName("PIVA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaUguale);
		elementoFiltro.setCampoFiltrato("PIVA");  //campo su cui costruire una or
		listaElementiFiltro.add(elementoFiltro);
		//denominazione
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Denominazione");
		elementoFiltro.setAttributeName("DENOMINAZIONE");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("DENOMINAZIONE"); //campo su cui costruire una or
		listaElementiFiltro.add(elementoFiltro);
		//cognome
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cognome");
		elementoFiltro.setAttributeName("COGNOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("UPPER(SIT_C_PERSONA.COGNOME)");
		listaElementiFiltro.add(elementoFiltro);
		//nome
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Nome");
		elementoFiltro.setAttributeName("NOME");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("UPPER(SIT_C_PERSONA.NOME)");
		listaElementiFiltro.add(elementoFiltro);
		//titolo gruppo
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("DATI INDIRIZZO");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		//indirizzo
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Indirizzo");
		elementoFiltro.setAttributeName("INDIRIZZO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("UPPER(SIT_C_CONC_INDIRIZZI.INDIRIZZO)");
		elementoFiltro.setExtraHTML("<span><input type=\"button\" class=\"TXTmainLabel\" style=\"text-align:center\" value=\"Cerca\" onclick=\"javascript:danRequest('"+it.escsolution.escwebgis.common.EscServlet.URL_PATH+"/StoricoConcessioni?listavie='+document.getElementById('INDIRIZZO').value,'divListaVie')\"/></span><span id=\"divListaVie\" class=\"txtRow\"  ></span> \n");		
		listaElementiFiltro.add(elementoFiltro);
		//numero civico
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Numero civico");
		elementoFiltro.setAttributeName("CIV_LIV1");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaUguale);
		elementoFiltro.setCampoFiltrato("UPPER(SIT_C_CONC_INDIRIZZI.CIV_LIV1)");
		listaElementiFiltro.add(elementoFiltro);
		//titolo gruppo
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("DATI CATASTALI");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);
		//foglio
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Foglio");
		elementoFiltro.setAttributeName("FOGLIO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaUguale);
		elementoFiltro.setCampoFiltrato("SIT_C_CONCESSIONI_CATASTO.FOGLIO");
		listaElementiFiltro.add(elementoFiltro);
		//particella
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Particella");
		elementoFiltro.setAttributeName("PARTICELLA");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("SIT_C_CONCESSIONI_CATASTO.PARTICELLA");
		listaElementiFiltro.add(elementoFiltro);
		//subalterno
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Subalterno");
		elementoFiltro.setAttributeName("SUBALTERNO");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("SIT_C_CONCESSIONI_CATASTO.SUBALTERNO");
		listaElementiFiltro.add(elementoFiltro);		
		
		sessione.setAttribute("LISTA_RICERCA",listaElementiFiltro);
		sessione.setAttribute("TITOLO",Tema.getNomeFiltro(uc));
		

		//chiamare la pagina di ricerca
		nav = new PulsantiNavigazione();
		nav.chiamataRicerca();
		this.chiamaPagina(request,response,"concessioni/storicoConcessioniFrame.jsp", nav);
	}

	private void mCaricareLista(HttpServletRequest request,HttpServletResponse response, int tipo)throws Exception
	{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER)!= null)
		{
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new StoricoConcessioniFinder().getClass())
			{
				finder = (StoricoConcessioniFinder)sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (StoricoConcessioniFinder)gestireMultiPagina(finder,request);		
		StoricoConcessioniLogic logic = new StoricoConcessioniLogic(this.getEnvUtente(request));
		Hashtable ht = null;
		
		if (tipo == 102 || tipo== 103) 
			ht = logic.mCaricareListaOgg(request.getParameter("OGGETTO_SEL"),"CONC");	//Via o Civico
		else if(tipo == 104 || tipo == 105) 
			ht = logic.mCaricareListaOgg(request.getParameter("OGGETTO_SEL"),"CAT");	//Immobile o Fabbricato
		else
		 ht = logic.mCaricareLista(vettoreRicerca,finder);
		
		
		Vector vct_lista = (Vector)ht.get(StoricoConcessioniLogic.LISTA);
		finder = (StoricoConcessioniFinder)ht.get(StoricoConcessioniLogic.FINDER);
		sessione.setAttribute(StoricoConcessioniLogic.LISTA,vct_lista);
		sessione.setAttribute(NOMEFINDER,finder);

		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
		{
			nav.chiamataEsternaLista();
			nav.setExt("1");
			nav.setPrimo(true);
		}
		else
			nav.chiamataInternaLista();

		this.chiamaPagina(request,response,"concessioni/storicoConcessioniFrame.jsp",nav);
	}
	
	private void mCaricareListaPopup(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		StoricoConcessioniLogic logic = new StoricoConcessioniLogic(this.getEnvUtente(request));
		
		Hashtable ht = null;
		if (request.getParameter("collegate") != null && new Boolean(request.getParameter("collegate")).booleanValue()) {
			String conId = request.getParameter("conId");
			String fg = request.getParameter("fg");
			String part = request.getParameter("part");
			String sub = request.getParameter("sub");
			String codEnte = request.getParameter("cod_ente");
			ht = logic.mCaricareListaCollegate(conId, fg, part, sub, codEnte);
		}else{
			//metto in vettoreRicerca solo i parametri foglio e particella della request
			vettoreRicerca = new Vector();
			vettoreRicerca.add(request.getParameter("fg"));
			vettoreRicerca.add(request.getParameter("part"));			
			ht = logic.mCaricareLista(vettoreRicerca, null);						
		}
		Vector vct_lista = (Vector)ht.get(StoricoConcessioniLogic.LISTA);
		sessione.setAttribute(StoricoConcessioniLogic.LISTA,vct_lista);	
		sessione.setAttribute(NOMEFINDER,finder);

		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
		{
			nav.chiamataEsternaLista();
			nav.setExt("1");
			nav.setPrimo(true);
		}
		else
			nav.chiamataInternaLista();	

		if (request.getParameter("collegate") != null && new Boolean(request.getParameter("collegate")).booleanValue()) {
			this.chiamaPagina(request,response,"concessioni/storicoConcessioniList.jsp?popup=true&collegate=true",nav);
		}else{
			this.chiamaPagina(request,response,"concessioni/storicoConcessioniList.jsp?popup=true",nav);						
		}
		
	}
	
	private void mCaricareListaFromSoggetto(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		
		String idSoggetto= request.getParameter("OGGETTO_SEL");
		HttpSession sessione = request.getSession();
		
		StoricoConcessioniLogic logic = new StoricoConcessioniLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaFromSoggetto(idSoggetto);
		Vector vct_lista = (Vector)ht.get(StoricoConcessioniLogic.LISTA);
		
		sessione.setAttribute(StoricoConcessioniLogic.LISTA, vct_lista);
		
		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
		{
			nav.chiamataEsternaLista();
			nav.setExt("1");
			nav.setPrimo(true);
		}
		else
			nav.chiamataInternaLista();

		this.chiamaPagina(request,response,"concessioni/storicoConcessioniFrame.jsp",nav);
	}
	

	private void mCaricareDettaglio(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		String azione = "";
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
		
		StoricoConcessioniFinder finder = null;
		if (sessione.getAttribute(NOMEFINDER) !=null)
		{
			if (((Object)sessione.getAttribute(NOMEFINDER)).getClass() == new StoricoConcessioniFinder().getClass())
			{
				finder = (StoricoConcessioniFinder)sessione.getAttribute(NOMEFINDER);
			}
		}

		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request, response, azione, finder, sessione, StoricoConcessioniLogic.LISTA, (Vector)sessione.getAttribute(StoricoConcessioniLogic.LISTA), NOMEFINDER);

		if (azione.equals(""))
		{
			oggettoSel="";
			recordScelto="";
			sessione.removeAttribute("BACK_JS_COUNT");
			sessione.removeAttribute("BACK_RECORD_ENABLE");

			if (request.getParameter("OGGETTO_SEL")!= null)
			{
				oggettoSel = request.getParameter("OGGETTO_SEL");
			}
			if (request.getParameter("RECORD_SEL")!= null)
			{
				recordScelto = request.getParameter("RECORD_SEL");
				if (finder!=null)
					finder.setRecordAttuale(new Long(recordScelto).longValue());
			}
		}

		//carica il dettaglio
		StoricoConcessioniLogic logic = new StoricoConcessioniLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglio(oggettoSel, pathDatiDiogene);
		StoricoConcessioni con = (StoricoConcessioni)ht.get(StoricoConcessioniLogic.DATI_CONCESSIONE);
		sessione.setAttribute(StoricoConcessioniLogic.DATI_CONCESSIONE, con);		
		HashMap datiTecnici = (HashMap)ht.get(StoricoConcessioniLogic.DATI_TECNICI);
		sessione.setAttribute(StoricoConcessioniLogic.DATI_TECNICI, datiTecnici);		
		ArrayList soggetti = (ArrayList)ht.get(StoricoConcessioniLogic.LISTA_SOGGETTI);
		sessione.setAttribute(StoricoConcessioniLogic.LISTA_SOGGETTI, soggetti);
		ArrayList datiCatastali = (ArrayList)ht.get(StoricoConcessioniLogic.LISTA_DATI_CATASTALI);
		sessione.setAttribute(StoricoConcessioniLogic.LISTA_DATI_CATASTALI, datiCatastali);
		ArrayList indirizzi = (ArrayList)ht.get(StoricoConcessioniLogic.LISTA_INDIRIZZI);
		sessione.setAttribute(StoricoConcessioniLogic.LISTA_INDIRIZZI, indirizzi);
		//carica gli id storici
		HashMap idStorici = (HashMap)ht.get(StoricoConcessioniLogic.IDSTORICI_CONCESSIONI);
		sessione.setAttribute(StoricoConcessioniLogic.IDSTORICI_CONCESSIONI, idStorici);		

		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		
		
		
		if (soggetti != null && soggetti.size()>0){
		
			// Aggiungo i valori per l'indice di correlazione
			Vector<OggettoIndice> sOggettiInd = new Vector<OggettoIndice>();
			ArrayList listaNomiSoggetti= new ArrayList();
			
			for (int i=0; i< soggetti.size();i++){
				StoricoConcessioniPersona ana=(StoricoConcessioniPersona) soggetti.get(i);
			
			OggettoIndice oi = new OggettoIndice();
			
			oi.setIdOrig(ana.getId());
			oi.setFonte("3");
			oi.setProgr("1");
			
			if ((ana.getCognome()!= null && !"".equals(ana.getCognome())) || (ana.getNome()!= null && !"".equals(ana.getNome())))
				oi.setDescrizione(ana.getCognome() + " " + ana.getNome());
			else
				oi.setDescrizione(ana.getDenominazione());
			
			String nomeSoggetto= oi.getFonte()+oi.getProgr()+oi.getIdOrig();
			if (!listaNomiSoggetti.contains(nomeSoggetto)){
				listaNomiSoggetti.add(nomeSoggetto);
				sOggettiInd.add(oi);
			}
		
			
			}
			sessione.setAttribute("indice_soggetti", sOggettiInd);
			
		}
		
		if (indirizzi != null && indirizzi.size() != 0) {
			
			Vector<OggettoIndice> sOggettiInd = new Vector<OggettoIndice>();
			ArrayList listaNomiCivici= new ArrayList();
			
			
			for (int i=0; i < indirizzi.size(); i++) {
				StoricoConcessioniIndirizzo ind = (StoricoConcessioniIndirizzo) indirizzi.get(i);
				OggettoIndice oi = new OggettoIndice();
				
				oi.setIdOrig(ind.getId());
				oi.setFonte("3");
				oi.setProgr("1");
				oi.setDescrizione(ind.getIndirizzo());
			
				
				String nomeCivico= oi.getFonte()+oi.getProgr()+oi.getIdOrig();
				if (!listaNomiCivici.contains(nomeCivico)){
					listaNomiCivici.add(nomeCivico);
					sOggettiInd.add(oi);
				}
			}
			
			sessione.setAttribute("indice_civici", sOggettiInd);
		}
		
		// Indice oggetti
		if (datiCatastali != null) {
			
			Vector<OggettoIndice> oOggettiInd = new Vector<OggettoIndice>();
			ArrayList listaNomiOggetti= new ArrayList();
			
			Vector<OggettoIndice> oFabbricatiInd = new Vector<OggettoIndice>();
			ArrayList listaNomiFabb= new ArrayList();
			
			
			for (int i=0; i < datiCatastali.size(); i++) {
				StoricoConcessioniCatasto scc = (StoricoConcessioniCatasto) datiCatastali.get(i);
				OggettoIndice oi = new OggettoIndice();
				oi.setIdOrig(scc.getId());
				oi.setDescrizione("F:" + scc.getFoglio() + " P:" + scc.getParticella() + " S:" + scc.getSubalterno());
				oi.setFonte("3");
				oi.setProgr("2");
				
				String nomeOggetto= oi.getFonte()+oi.getProgr()+oi.getIdOrig();
				if (!listaNomiOggetti.contains(nomeOggetto)){
					listaNomiOggetti.add(nomeOggetto);
					oOggettiInd.add(oi);
				}
				
				
				OggettoIndice oi1 = new OggettoIndice();
				oi1.setIdOrig(scc.getId());
				oi1.setDescrizione("SEZ:"+(scc.getSezione()!= null ? scc.getSezione(): "-") +" F:" + scc.getFoglio() + " P:" + scc.getParticella() );
				oi1.setFonte("3");
				oi1.setProgr("2");
				
				String nomeFabb= oi1.getFonte()+oi1.getProgr()+oi1.getIdOrig();
				if (!listaNomiFabb.contains(nomeFabb)){
					listaNomiFabb.add(nomeFabb);
					oFabbricatiInd.add(oi1);
				}
				
				
			}
			
			sessione.setAttribute("indice_oggetti", oOggettiInd);
			sessione.setAttribute("indice_fabbricati", oFabbricatiInd);
		}
		this.chiamaPagina(request,response,"concessioni/storicoConcessioniFrame.jsp", nav);		
	}
	
	private void mCaricareDettaglioPopup(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		String azione = "";
		HttpSession sessione = request.getSession();

		if (request.getParameter("AZIONE")!= null)
			azione = request.getParameter("AZIONE");

		if (azione.equals(""))
		{
			oggettoSel="";
			recordScelto="";
			sessione.removeAttribute("BACK_JS_COUNT");
			sessione.removeAttribute("BACK_RECORD_ENABLE");

			if (request.getParameter("OGGETTO_SEL")!= null)
			{
				oggettoSel = request.getParameter("OGGETTO_SEL");
			}
			if (request.getParameter("RECORD_SEL")!= null)
			{
				recordScelto = request.getParameter("RECORD_SEL");
				if (finder!=null)
					finder.setRecordAttuale(new Long(recordScelto).longValue());
			}
		}

		//carica il dettaglio
		StoricoConcessioniLogic logic = new StoricoConcessioniLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglio(oggettoSel, pathDatiDiogene);
		StoricoConcessioni con = (StoricoConcessioni)ht.get(StoricoConcessioniLogic.DATI_CONCESSIONE);
		sessione.setAttribute(StoricoConcessioniLogic.DATI_CONCESSIONE, con);
		ArrayList soggetti = (ArrayList)ht.get(StoricoConcessioniLogic.LISTA_SOGGETTI);
		sessione.setAttribute(StoricoConcessioniLogic.LISTA_SOGGETTI, soggetti);
		ArrayList datiCatastali = (ArrayList)ht.get(StoricoConcessioniLogic.LISTA_DATI_CATASTALI);
		sessione.setAttribute(StoricoConcessioniLogic.LISTA_DATI_CATASTALI, datiCatastali);
		ArrayList indirizzi = (ArrayList)ht.get(StoricoConcessioniLogic.LISTA_INDIRIZZI);
		sessione.setAttribute(StoricoConcessioniLogic.LISTA_INDIRIZZI, indirizzi);
		//carica gli id storici
		HashMap idStorici = (HashMap)ht.get(StoricoConcessioniLogic.IDSTORICI_CONCESSIONI);
		sessione.setAttribute(StoricoConcessioniLogic.IDSTORICI_CONCESSIONI, idStorici);		

		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();
		
		if (request.getParameter("collegate") != null && new Boolean(request.getParameter("collegate")).booleanValue()) {
			this.chiamaPagina(request,response,"concessioni/storicoConcessioniDetail.jsp?popup=true&collegate=true" 
					+ "&conId" + request.getParameter("conId") + "&fg=" + request.getParameter("fg") + "&part=" + request.getParameter("part")
					+ "&sub" + request.getParameter("sub") + "&cod_ente=" + request.getParameter("cod_ente"), nav);
		}else{
			this.chiamaPagina(request,response,"concessioni/storicoConcessioniDetail.jsp?popup=true&fg=" 
					+ (String)vettoreRicerca.get(0) + "&part=" + (String)vettoreRicerca.get(1)
					, nav);
		}		
		
	}
	
	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception{
		finder = (StoricoConcessioniFinder)finder2;
		StoricoConcessioniLogic logic = new StoricoConcessioniLogic(this.getEnvUtente(request));
		return logic.mCaricareLista(this.vettoreRicerca, finder);
	}


	public EscFinder getNewFinder()
	{
		return new StoricoConcessioniFinder();
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti,int recordSuccessivo)
	{
		StoricoConcessioni con = (StoricoConcessioni)listaOggetti.get(recordSuccessivo);
		String key = con.getChiave();
		return key;
	}

	public String getTema()
	{
		return "Concessioni Edilizie";
	}

	public String getTabellaPerCrossLink()
	{
		return "SIT_C_CONCESSIONI";
	}
	
	public String getLocalDataSource() {
		return localDataSource;
	}

}
