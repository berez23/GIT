
package it.escsolution.escwebgis.concessioni.servlet;


import it.escsolution.escwebgis.catasto.bean.Immobile;
import it.escsolution.escwebgis.catasto.bean.Sesso;
import it.escsolution.escwebgis.catasto.logic.CatastoImmobiliLogic;
import it.escsolution.escwebgis.catasto.logic.CatastoIntestatariFLogic;
import it.escsolution.escwebgis.catasto.logic.CatastoPlanimetrieComma340Logic;
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
import it.escsolution.escwebgis.concessioni.bean.ConcessioniINFORM;
import it.escsolution.escwebgis.concessioni.bean.ConcessioniINFORMFinder;
import it.escsolution.escwebgis.concessioni.logic.ConcessioniINFORMLogic;
import it.webred.DecodificaPermessi;
import it.webred.cet.permission.CeTUser;
import it.webred.cet.permission.GestionePermessi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ConcessioniINFORMServlet extends EscServlet implements EscService
{

	String pathImg = "";
	
	String pathDatiDiogene = "";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        pathImg = config.getInitParameter("pathImmagini");
        pathDatiDiogene = super.getPathDatiDiogene();
    }
    
	private String recordScelto;

	private ConcessioniINFORMLogic logic = null;

	// private AnagrafeFinder finder = null;
	public static final String NOMEFINDER = "FINDER46";

	private ConcessioniINFORMFinder finder = null;

	PulsantiNavigazione nav;

	public void EseguiServizio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		HttpSession sessione = request.getSession();		
		sessione.setAttribute("FUNZIONALITA", Tema.getNomeFunzionalita(uc));
		
		super.EseguiServizio(request, response);
		try
		{
			if (request.getParameter("popupCatasto") != null)
				mPopupCatasto(request, response);
			else if (request.getParameter("popupCatastoSoggetto") != null && new Boolean(request.getParameter("popupCatastoSoggetto")).booleanValue())
				mPopupCatastoSoggetto(request, response);
			else			
			switch (st)
			{
			case 1:

				pulireSessione(request);
				mCaricareFormRicerca(request, response);
				break;
			case 2:

				mCaricareLista(request, response);
				break;
			case 3:
				// ho cliccato su un elemento --> visualizzo il dettaglio
				mCaricareDettaglio(request, response);
				this.leggiCrossLink(request);
				break;
			case 33:
				 // ho cliccato su un elemento --> visualizzo il dettaglio						 
					 mCaricareDettaglio(request,response);
					 this.leggiCrossLink(request);						 
				 	 break;
			case 99:

				mCaricareImmagine(request, response);
				break;
			}
		}
		catch (FileNotFoundException e) 
		{
			log.error(e.getMessage(),e);
			throw new DiogeneException("File non trovato");
		}		
		catch (it.escsolution.escwebgis.common.DiogeneException de)
		{
			throw de;
		}
		catch (Exception ex)
		{
			log.error(ex.getMessage(),ex);
		}

	}

	
	private void mPopupCatasto(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{

		String f = request.getParameter("f");
		String p = request.getParameter("p");
		String s = request.getParameter("s");
		String d = request.getParameter("d");
		String cod_ente = request.getParameter("cod_ente");

		if(
				(f == null || f.trim().equals("")) || 
				(p == null || p.trim().equals("")) || 
				(s == null || s.trim().equals("")) ||
				(d == null || d.trim().equals("")) ||
				(cod_ente == null || cod_ente.trim().equals("")) 
		  )
			throw new Exception("dati mancanti");
		
		if (s.equals("-"))
			s="0";
		
		String key = cod_ente+"|"+f+"|"+p+"| |"+s+"|"+d;
		
		CatastoImmobiliLogic logic = new CatastoImmobiliLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioImmobile(key, "", pathDatiDiogene);
		request.getSession().setAttribute(CatastoImmobiliLogic.UNIMM, ht.get(CatastoImmobiliLogic.UNIMM));
		request.getSession().setAttribute(CatastoImmobiliLogic.LISTA_CIVICI, ht.get(CatastoImmobiliLogic.LISTA_CIVICI));
		request.getSession().setAttribute(CatastoImmobiliLogic.LISTA_CIVICI_CAT, ht.get(CatastoImmobiliLogic.LISTA_CIVICI_CAT));

		CatastoPlanimetrieComma340Logic logic340 = new CatastoPlanimetrieComma340Logic(this.getEnvUtente(request));
		Immobile imm = (Immobile)ht.get(CatastoImmobiliLogic.UNIMM);
		ArrayList<ArrayList<String>> planimetrieComma340 = logic340.getPlanimetrieComma340(imm.getFoglio(), imm.getNumero(), imm.getUnimm(), pathDatiDiogene);
		request.getSession().setAttribute(CatastoPlanimetrieComma340Logic.PLANIMETRIE_COMMA_340_CIM, planimetrieComma340);

		this.chiamaPagina(request,response,"catasto/cimDetail.jsp",nav);
	}
	
	private void mPopupCatastoSoggetto(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{		
		String codFiscaleDic = (request.getParameter("CODICE_FISCALE_DIC"));
		int annoImposta = Integer.parseInt(request.getParameter("ANNO_IMPOSTA"));
		CatastoIntestatariFLogic logic = new CatastoIntestatariFLogic(this.getEnvUtente(request));
		Vector listaSoggetto = logic.mCaricareCatastoPerSoggetto(codFiscaleDic, annoImposta);
		request.getSession().setAttribute(CatastoImmobiliLogic.SOGGETTO, listaSoggetto);
		this.chiamaPagina(request,response,"catasto/cimDetailPopupSoggetto.jsp",nav);	
	}	
	
	private void mCaricareImmagine(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		EnvUtente eu = new EnvUtente((CeTUser) request.getSession().getAttribute("user"), null, null);
   		if (!GestionePermessi.autorizzato(eu.getUtente(),  eu.getNomeIstanza(), DecodificaPermessi.ITEM_VISUALIZZA_FONTI_DATI, DecodificaPermessi.PERMESSO_SCARICA_PLANIMETRIE,true))
			throw new DiogeneException("Non si possiede il permesso: " + DecodificaPermessi.PERMESSO_SCARICA_PLANIMETRIE);    			

   		boolean openJpg = request.getParameter("openJpg") != null && new Boolean(request.getParameter("openJpg")).booleanValue();
   		
   		ConcessioniINFORMLogic logic = new ConcessioniINFORMLogic(this.getEnvUtente(request));
		String[] val =  logic.mCaricareDatoImg(request.getParameter("img")); 		
		FileInputStream is = new FileInputStream(pathImg+"/"+val[0]+"/"+val[1]);
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

	private void mCaricareFormRicerca(HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		// caricare dati che servono nella pagina di ricerca

		HttpSession sessione = request.getSession();



		Vector listaElementiFiltro = new Vector();
		Vector operatoriStringa = new Vector();
		operatoriStringa.add(new EscOperatoreFiltro("=", "uguale"));
		operatoriStringa.add(new EscOperatoreFiltro("like", "contiene"));

		Vector operatoriNumerici = new Vector();
		operatoriNumerici.add(new EscOperatoreFiltro("=", "="));
		operatoriNumerici.add(new EscOperatoreFiltro("<>", "<>"));
		operatoriNumerici.add(new EscOperatoreFiltro(">", ">"));
		operatoriNumerici.add(new EscOperatoreFiltro("<", "<"));

		Vector vct_tipo_sogg = new Vector();
		//--1=proprietario, 2=richiedente.
		vct_tipo_sogg.add(new Sesso("","Tutti"));
		vct_tipo_sogg.add(new Sesso("1","proprietario"));
		vct_tipo_sogg.add(new Sesso("2","richiedente"));		
		
		Vector operatoriStringaRid = new Vector();
		operatoriStringaRid.add(new EscOperatoreFiltro("=", "uguale"));

		Vector operatoriStringaRid2 = new Vector();
		operatoriStringaRid2.add(new EscOperatoreFiltro("like", "contiene"));

		
		EscElementoFiltro elementoFiltro = new EscElementoFiltro();

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Dati Concessione");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Numero Protocollo");
		elementoFiltro.setAttributeName("rif_numero");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("rif_numero");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Anno Protocollo");
		elementoFiltro.setAttributeName("rif_anno");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setCampoFiltrato("rif_anno");
		listaElementiFiltro.add(elementoFiltro);		
		
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Soggetto");
		elementoFiltro.setSoloLabel(true);
		listaElementiFiltro.add(elementoFiltro);		
		
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Tipo Soggetto");
		elementoFiltro.setAttributeName("tipo_soggetto");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid);
		elementoFiltro.setListaValori(vct_tipo_sogg);
		elementoFiltro.setCampoFiltrato("tipo_soggetto");
		listaElementiFiltro.add(elementoFiltro);		
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Codice Fiscale/P.iva");
		elementoFiltro.setAttributeName("codice_fiscale");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("UPPER(codice_fiscale)");
		listaElementiFiltro.add(elementoFiltro);

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Cognome/Rag.Soc");
		elementoFiltro.setAttributeName("cognome_ragsoc");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("UPPER(cognome_ragsoc)");
		listaElementiFiltro.add(elementoFiltro);
		
		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Nome");
		elementoFiltro.setAttributeName("nome");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("UPPER(nome)");
		listaElementiFiltro.add(elementoFiltro);		

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Indirizzo");
		elementoFiltro.setAttributeName("Indirizzo");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringaRid2);
		elementoFiltro.setCampoFiltrato("INDIRIZZO");
		listaElementiFiltro.add(elementoFiltro);	

		elementoFiltro = new EscElementoFiltro();
		elementoFiltro.setLabel("Civico");
		elementoFiltro.setAttributeName("Civico");
		elementoFiltro.setTipo("S");
		elementoFiltro.setCampoJS("");
		elementoFiltro.setListaOperatori(operatoriStringa);
		elementoFiltro.setCampoFiltrato("CIVICO");
		listaElementiFiltro.add(elementoFiltro);	
		
		sessione.setAttribute("LISTA_RICERCA", listaElementiFiltro);
		sessione.setAttribute("TITOLO", Tema.getNomeFiltro(uc));
		
		// chiamare la pagina di ricerca
		nav = new PulsantiNavigazione();
		nav.chiamataRicerca();
		this.chiamaPagina(request, response, "concessioni/concessioniINFORMFrame.jsp", nav);

	}

	private void mCaricareLista(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// prendere dalla request i parametri di ricerca impostati dall'utente
		HttpSession sessione = request.getSession();

		if (sessione.getAttribute(NOMEFINDER) != null)
		{
			if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new ConcessioniINFORMFinder().getClass())
			{
				finder = (ConcessioniINFORMFinder) sessione.getAttribute(NOMEFINDER);
			}
			else
				finder = null;
		}

		finder = (ConcessioniINFORMFinder) gestireMultiPagina(finder, request);

		ConcessioniINFORMLogic logic = new ConcessioniINFORMLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareListaConcessioni(vettoreRicerca, finder); 

		// eseguire la query e caricare il vettore con i risultati
		// chiamare la pagina di lista

		Vector vct_lista_concessioni = (Vector) ht.get(ConcessioniINFORMLogic.LISTA_CONCESSIONI_INFORM);
		finder = (ConcessioniINFORMFinder) ht.get(NOMEFINDER);
		sessione.setAttribute(ConcessioniINFORMLogic.LISTA_CONCESSIONI_INFORM, vct_lista_concessioni);
		sessione.setAttribute(NOMEFINDER, finder);

		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
		{

			nav.chiamataEsternaLista();
			nav.setExt("1");
			nav.setPrimo(true);
		}
		else
			nav.chiamataInternaLista();
		this.chiamaPagina(request, response, "concessioni/concessioniINFORMFrame.jsp", nav);
	}

	private void mCaricareDettaglio(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// prendere dalla request il parametrio identificativo dell'oggetto
		// cliccato
		// eseguire la query per caricare l'oggetto selezionato dal db
		// chiamare la pagina di dettaglio

		String azione = "";
		HttpSession sessione = request.getSession();
		
		removeOggettiIndiceDaSessione(sessione);
		
		ConcessioniINFORMFinder finder = null;
		// boolean chiamataEsterna = false;
		if (sessione.getAttribute(NOMEFINDER) != null)
		{
			if (((Object) sessione.getAttribute(NOMEFINDER)).getClass() == new ConcessioniINFORMFinder().getClass())
			{
				finder = (ConcessioniINFORMFinder) sessione.getAttribute(NOMEFINDER);
			}
		}

		if (request.getParameter("AZIONE") != null)
			azione = request.getParameter("AZIONE");
		gestioneMultiRecord(request, response, azione, finder, sessione, "LISTA_CONCESSIONI", (Vector) sessione.getAttribute("LISTA_CONCESSIONI"), NOMEFINDER);
		if (azione.equals(""))
		{
			oggettoSel = "";
			recordScelto = "";
			sessione.removeAttribute("BACK_JS_COUNT");
			sessione.removeAttribute("BACK_RECORD_ENABLE");

			if (request.getParameter("OGGETTO_SEL") != null)
			{
				oggettoSel = request.getParameter("OGGETTO_SEL");
			}
			if (request.getParameter("RECORD_SEL") != null)
			{
				recordScelto = request.getParameter("RECORD_SEL");
				if (finder != null)
					finder.setRecordAttuale(new Long(recordScelto).longValue());
			}
		}

		/*
		 * carica il dettaglio
		 */
		ConcessioniINFORMLogic logic = new ConcessioniINFORMLogic(this.getEnvUtente(request));
		Hashtable ht = logic.mCaricareDettaglioConcessioni(oggettoSel);
		ConcessioniINFORM conc = (ConcessioniINFORM) ht.get(ConcessioniINFORMLogic.CONCESSIONI_INFORM);
		sessione.setAttribute(ConcessioniINFORMLogic.CONCESSIONI_INFORM, conc);
		nav = new PulsantiNavigazione();
		if (chiamataEsterna)
			nav.chiamataEsternaDettaglio();
		else
			nav.chiamataInternaDettaglio();

		this.chiamaPagina(request, response, "concessioni/concessioniINFORMFrame.jsp", nav);

		// EscFinder escf = (EscFinder)sessione.getAttribute(NOMEFINDER);

	}


	public Hashtable executeLogic(EscFinder finder2, HttpServletRequest request) throws Exception
	{
		finder = (ConcessioniINFORMFinder) finder2;
		ConcessioniINFORMLogic logic = new ConcessioniINFORMLogic(this.getEnvUtente(request));
		return logic.mCaricareListaConcessioni(this.vettoreRicerca, finder);
	}

	public EscFinder getNewFinder()
	{
		return new ConcessioniINFORMFinder();
	}

	public String getChiaveOggetto(EscFinder finder, Vector listaOggetti, int recordSuccessivo)
	{
		return ((ConcessioniINFORM) listaOggetti.get(recordSuccessivo)).getChiave();
	}

	public String getTema()
	{
		return "Concessioni Edilizie";
	}


}
